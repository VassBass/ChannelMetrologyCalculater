package repository.impl;

import application.Application;
import application.ApplicationContext;
import constants.Action;
import repository.AreaRepository;
import repository.Repository;
import ui.model.SaveMessage;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AreaRepositoryImpl extends Repository implements AreaRepository {
    private static final Logger LOGGER = Logger.getLogger(AreaRepository.class.getName());

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

            LOGGER.fine("Send request");
            statement.execute(sql);

            LOGGER.fine("Close connection");
            statement.close();
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Initialization ERROR", ex);
        }
        LOGGER.info("Initialization SUCCESS");
    }

    @Override
    public ArrayList<String> getAll() {
        ArrayList<String>areas = new ArrayList<>();
        LOGGER.fine("Get connection with DB");
        try (Connection connection = this.getConnection()){
            Statement statement = connection.createStatement();

            LOGGER.fine("Send request");
            String sql = "SELECT * FROM areas";
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()){
                areas.add(resultSet.getString("area"));
            }

            LOGGER.fine("Close connections");
            resultSet.close();
            statement.close();
        }catch (SQLException ex){
            LOGGER.log(Level.SEVERE, "ERROR: ", ex);
        }
        return areas;
    }

    @Override
    public void add(String object) {
        new BackgroundAction().add(object);
    }

    @Override
    public void set(String oldObject, String newObject) {
        new BackgroundAction().set(oldObject, newObject);
    }

    @Override
    public void remove(String object) {
        new BackgroundAction().remove(object);
    }

    @Override
    public void clear() {
        new BackgroundAction().clear();
    }

    @Override
    public void rewrite(ArrayList<String> newList) {
        new BackgroundAction().rewrite(newList);
    }

    @Override
    public void rewriteInCurrentThread(ArrayList<String>newList){
        if (newList != null) {
            LOGGER.fine("Get connection with DB");
            try (Connection connection = this.getConnection()) {
                LOGGER.fine("Send request to clear");
                Statement statementClear = connection.createStatement();
                String sql = "DELETE FROM areas;";
                statementClear.execute(sql);

                if (!newList.isEmpty()) {
                    LOGGER.fine("Send requests to add");
                    sql = "INSERT INTO areas ('area') VALUES (?);";
                    PreparedStatement statement = connection.prepareStatement(sql);
                    for (String area : newList) {
                        statement.setString(1, area);
                        statement.execute();
                    }

                    LOGGER.fine("Close connections");
                    statementClear.close();
                    statement.close();
                }
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, "ERROR: ", ex);
            }
        }
    }


    private class BackgroundAction extends SwingWorker<Void, Void> {
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
            this.action = constants.Action.ADD;
            this.start();
        }

        void remove(String object){
            this.object = object;
            this.action = constants.Action.REMOVE;
            this.start();
        }

        void clear(){
            this.action = constants.Action.CLEAR;
            this.start();
        }

        void rewrite(ArrayList<String>list){
            this.list = list;
            this.action = list == null ? constants.Action.CLEAR : constants.Action.REWRITE;
            this.start();
        }

        void set(String oldObject, String newObject){
            this.old = oldObject;
            this.object = newObject;
            this.action = Action.SET;
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
        protected Void doInBackground() throws Exception {
            String sql = null;
            switch (this.action){
                case ADD:
                    sql = "REPLACE INTO areas (area) "
                            + "VALUES('" + this.object + "');";
                    break;
                case REMOVE:
                    sql = "DELETE FROM areas WHERE area = '" + this.object + "';";
                    break;
                case CLEAR:
                    sql = "DELETE FROM areas;";
                    break;
            }
            if (sql != null) {
                LOGGER.fine("Get connection with DB");
                try (Connection connection = getConnection()) {
                    Statement statement = connection.createStatement();

                    LOGGER.fine("Send request");
                    statement.execute(sql);

                    LOGGER.fine("Close connections");
                    statement.close();
                } catch (SQLException ex) {
                    LOGGER.log(Level.SEVERE, "ERROR: ", ex);
                }
            }else if (this.list != null) {
                LOGGER.fine("Get connection with DB");
                try (Connection connection = getConnection()){
                    LOGGER.fine("Send request to clear");
                    Statement statementClear = connection.createStatement();
                    sql = "DELETE FROM areas;";
                    statementClear.execute(sql);

                    if (!this.list.isEmpty()) {
                        LOGGER.fine("Send requests to add");
                        sql = "INSERT INTO areas ('area') VALUES (?);";
                        PreparedStatement statement = connection.prepareStatement(sql);
                        for (String area : this.list) {
                            statement.setString(1, area);
                            statement.execute();
                        }

                        LOGGER.fine("Close connections");
                        statementClear.close();
                        statement.close();
                    }
                }catch (SQLException ex){
                    LOGGER.log(Level.SEVERE, "ERROR: ", ex);
                }
            }else if (this.old != null){
                LOGGER.fine("Get connection with DB");
                try (Connection connection = getConnection()){
                    LOGGER.fine("Send request to delete");
                    Statement statementClear = connection.createStatement();
                    sql = "DELETE FROM areas WHERE area = '" + this.old + "';";
                    statementClear.execute(sql);

                    LOGGER.fine("Send requests to add");
                    sql = "INSERT INTO areas ('area') VALUES ('" + this.object + "');";
                    Statement statement = connection.createStatement();
                    statement.execute(sql);

                    LOGGER.fine("Close connections");
                    statementClear.close();
                    statement.close();
                }catch (SQLException ex){
                    LOGGER.log(Level.SEVERE, "ERROR: ", ex);
                }
            }
            return null;
        }

        @Override
        protected void done() {
            Application.setBusy(false);
            if (this.saveMessage != null) this.saveMessage.dispose();
        }
    }
}