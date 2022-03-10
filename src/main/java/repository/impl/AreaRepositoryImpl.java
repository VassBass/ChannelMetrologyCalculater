package repository.impl;

import application.Application;
import application.ApplicationContext;
import constants.Action;
import org.sqlite.JDBC;
import repository.AreaRepository;
import repository.Repository;
import ui.model.SaveMessage;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AreaRepositoryImpl extends Repository implements AreaRepository {
    private static final Logger LOGGER = Logger.getLogger(AreaRepository.class.getName());

    private final ArrayList<String>areas = new ArrayList<>();

    public AreaRepositoryImpl(){super();}
    public AreaRepositoryImpl(String dbUrl){super(dbUrl);}

    @Override
    protected void init(){
        String sql = "CREATE TABLE IF NOT EXISTS areas ("
                + "area text NOT NULL UNIQUE"
                + ", PRIMARY KEY (\"area\")"
                + ");";

        LOGGER.fine("Get connection with DB");
        try (Connection connection = this.getConnection()){
            Statement statement = connection.createStatement();

            LOGGER.fine("Send request to create table");
            statement.execute(sql);

            LOGGER.fine("Send request to read areas from DB");
            sql = "SELECT * FROM areas";
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()){
                this.areas.add(resultSet.getString("area"));
            }

            LOGGER.fine("Close connection");
            statement.close();
            resultSet.close();
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Initialization ERROR", ex);
        }
        LOGGER.info("Initialization SUCCESS");
    }

    @Override
    public ArrayList<String> getAll() {
        return this.areas;
    }

    @Override
    public String get(int index) {
        return index < 0 | index >= this.areas.size() ? null : this.areas.get(index);
    }

    @Override
    public void add(String object) {
        if (object != null && !this.areas.contains(object)){
            new BackgroundAction().add(object);
            this.areas.add(object);
        }
    }

    @Override
    public void set(String oldObject, String newObject) {
        if (oldObject != null && newObject != null
                && this.areas.contains(oldObject) && !this.areas.contains(newObject)) {
            new BackgroundAction().set(oldObject, newObject);
        }
    }

    @Override
    public void remove(String object) {
        if (object != null && this.areas.contains(object)) new BackgroundAction().remove(object);
    }

    @Override
    public void clear() {
        this.areas.clear();
        new BackgroundAction().clear();
    }

    @Override
    public void rewrite(ArrayList<String> newList) {
        if (newList != null && !newList.isEmpty()) {
            this.areas.clear();
            this.areas.addAll(newList);
            new BackgroundAction().rewrite(newList);
        }
    }

    @Override
    public void rewriteInCurrentThread(ArrayList<String>newList){
        if (newList != null && !newList.isEmpty()) {
            this.areas.clear();
            this.areas.addAll(newList);
            new BackgroundAction().rewriteAreas(newList);
        }
    }

    @Override
    public void export() {
        new BackgroundAction().export(this.areas);
    }


    private class BackgroundAction extends SwingWorker<Boolean, Void> {
        private String object, old;
        private ArrayList<String>list;
        private constants.Action action;
        private final SaveMessage saveMessage;

        public BackgroundAction(){
            ApplicationContext context = Application.context;
            Window mainScreen = context == null ? null : Application.context.mainScreen;
            this.saveMessage = mainScreen == null ? null : new SaveMessage(mainScreen);
        }

        void add(String object){
            this.object = object;
            this.action = Action.ADD;
            this.start();
        }

        void remove(String object){
            this.object = object;
            this.action = Action.REMOVE;
            this.start();
        }

        void clear(){
            this.action = Action.CLEAR;
            this.start();
        }

        void rewrite(ArrayList<String>list){
            this.list = list;
            this.action = list == null ? Action.CLEAR : Action.REWRITE;
            this.start();
        }

        void set(String oldObject, String newObject){
            this.old = oldObject;
            this.object = newObject;
            this.action = Action.SET;
            this.start();
        }

        void export(ArrayList<String>areas){
            this.list = areas;
            this.action = Action.EXPORT;
            this.start();
        }

        private void start(){
            Application.setBusy(true);
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    if (saveMessage != null) saveMessage.setVisible(true);
                }
            });
            this.execute();
        }

        @Override
        protected Boolean doInBackground() throws Exception {
            switch (this.action){
                case ADD:
                    return this.addArea(this.object);
                case REMOVE:
                    return this.removeArea(this.object);
                case CLEAR:
                    return this.clearAreas();
                case REWRITE:
                    return this.rewriteAreas(this.list);
                case SET:
                    return this.setArea(this.old, this.object);
                case EXPORT:
                    return this.exportAreas(this.list);
            }
            return true;
        }

        @Override
        protected void done() {
            try {
                if (!this.get()){
                    switch (this.action){
                        case ADD:
                            areas.remove(this.object);
                            break;
                        case REMOVE:
                            areas.add(this.object);
                            break;
                        case SET:
                            areas.remove(this.object);
                            if (!areas.contains(this.old)) areas.add(this.old);
                            break;
                    }
                    String message = "Виникла помилка! Данні не збереглися! Спробуйте будь-ласка ще раз!";
                    JOptionPane.showMessageDialog(Application.context.mainScreen, message, "Помилка!", JOptionPane.ERROR_MESSAGE);
                }
            } catch (ExecutionException | InterruptedException e) {
                LOGGER.log(Level.SEVERE, "ERROR: ", e);
            }
            Application.setBusy(false);
            if (this.saveMessage != null) this.saveMessage.dispose();
        }

        private boolean addArea(String area){
            LOGGER.fine("Get connection with DB");
            try (Connection connection = getConnection()){
                LOGGER.fine("Send request to delete");
                String sql = "DELETE FROM areas WHERE area = '?';";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, area);
                statement.execute();

                LOGGER.fine("Send requests to add");
                sql = "INSERT INTO areas ('area') "
                        + "VALUES(?);";
                statement = connection.prepareStatement(sql);
                statement.setString(1, area);
                statement.execute();

                LOGGER.fine("Close connection");
                statement.close();
                return true;
            }catch (SQLException ex){
                LOGGER.log(Level.SEVERE, "ERROR: ", ex);
                return false;
            }
        }

        private boolean removeArea(String area){
            LOGGER.fine("Get connection with DB");
            try (Connection connection = getConnection()){
                LOGGER.fine("Send request to delete");
                String sql = "DELETE FROM areas WHERE area = '" + area + "';";
                Statement statement = connection.createStatement();
                statement.execute(sql);

                LOGGER.fine("Close connection");
                statement.close();
                return true;
            }catch (SQLException ex){
                LOGGER.log(Level.SEVERE, "ERROR: ", ex);
                return false;
            }
        }

        private boolean clearAreas(){
            LOGGER.fine("Get connection with DB");
            try (Connection connection = getConnection()) {
                LOGGER.fine("Send request");
                String sql = "DELETE FROM areas;";
                Statement statement = connection.createStatement();
                statement.execute(sql);

                LOGGER.fine("Close connections");
                statement.close();
                return true;
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, "ERROR: ", ex);
                return false;
            }
        }

        private boolean setArea(String oldArea, String newArea){
            LOGGER.fine("Get connection with DB");
            try (Connection connection = getConnection()){
                LOGGER.fine("Send request to delete");
                Statement statementClear = connection.createStatement();
                String sql = "DELETE FROM areas WHERE area = '" + oldArea + "';";
                statementClear.execute(sql);

                LOGGER.fine("Send requests to add");
                sql = "INSERT INTO areas ('area') "
                        + "VALUES(?);";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, newArea);
                statement.execute(sql);

                LOGGER.fine("Close connections");
                statementClear.close();
                statement.close();
                return true;
            }catch (SQLException ex){
                LOGGER.log(Level.SEVERE, "ERROR: ", ex);
                return false;
            }
        }

        boolean rewriteAreas(ArrayList<String>areas){
            if (areas != null) {
                LOGGER.fine("Get connection with DB");
                try (Connection connection = getConnection()) {
                    LOGGER.fine("Send request to clear");
                    Statement statementClear = connection.createStatement();
                    String sql = "DELETE FROM areas;";
                    statementClear.execute(sql);

                    if (!areas.isEmpty()) {
                        LOGGER.fine("Send requests to add");
                        sql = "INSERT INTO areas ('area') "
                                + "VALUES(?);";
                        PreparedStatement statement = connection.prepareStatement(sql);
                        for (String area : areas) {
                            statement.setString(1, area);
                            statement.execute();
                        }

                        LOGGER.fine("Close connections");
                        statementClear.close();
                        statement.close();
                    }
                    return true;
                } catch (SQLException ex) {
                    LOGGER.log(Level.SEVERE, "ERROR: ", ex);
                    return false;
                }
            }
            return true;
        }

        private boolean exportAreas(ArrayList<String>areas){
            Calendar date = Calendar.getInstance();
            String fileName = "export_areas ["
                    + date.get(Calendar.DAY_OF_MONTH)
                    + "."
                    + (date.get(Calendar.MONTH) + 1)
                    + "."
                    + date.get(Calendar.YEAR)
                    + "].db";
            String dbUrl = "jdbc:sqlite:Support/Export/" + fileName;
            String sql = "CREATE TABLE IF NOT EXISTS areas ("
                    + "area text NOT NULL UNIQUE"
                    + ", PRIMARY KEY (\"area\")"
                    + ");";

            Connection connection = null;
            Statement statement = null;
            PreparedStatement preparedStatement = null;
            try {
                LOGGER.fine("Get connection with DB");
                DriverManager.registerDriver(new JDBC());
                connection = DriverManager.getConnection(dbUrl);
                statement = connection.createStatement();

                LOGGER.fine("Send requests to create table");
                statement.execute(sql);

                LOGGER.fine("Send requests to add");
                sql = "INSERT INTO areas ('area') "
                        + "VALUES(?);";
                preparedStatement = connection.prepareStatement(sql);
                for (String area : areas) {
                    preparedStatement.setString(1, area);
                    preparedStatement.execute();
                }

                LOGGER.fine("Close connection");
                statement.close();
                preparedStatement.close();
                connection.close();
                return true;
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, "Initialization ERROR", ex);
                try {
                    if (statement != null) statement.close();
                    if (preparedStatement != null) preparedStatement.close();
                    if (connection != null) connection.close();
                    return false;
                } catch (SQLException ignored) {
                    return false;
                }
            }
        }
    }
}
