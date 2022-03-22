package repository.impl;

import application.Application;
import application.ApplicationContext;
import constants.Action;
import org.sqlite.JDBC;
import repository.InstallationRepository;
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

public class InstallationRepositoryImpl extends Repository implements InstallationRepository {
    private static final Logger LOGGER = Logger.getLogger(InstallationRepository.class.getName());

    private final ArrayList<String>installations = new ArrayList<>();

    public InstallationRepositoryImpl(){super();}
    public InstallationRepositoryImpl(String dbUrl){super(dbUrl);}

    public void init(){
        LOGGER.fine("Get connection with DB");
        try (Connection connection = this.getConnection()){
            String sql = "CREATE TABLE IF NOT EXISTS installations ("
                    + "installation text NOT NULL UNIQUE"
                    + ", PRIMARY KEY (\"installation\")"
                    + ");";
            Statement statement = connection.createStatement();

            LOGGER.fine("Send request to create table");
            statement.execute(sql);

            LOGGER.fine("Send request to read installations from DB");
            sql = "SELECT * FROM installations";
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()){
                this.installations.add(resultSet.getString("installation"));
            }

            LOGGER.fine("Close connection");
            resultSet.close();
            statement.close();
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Initialization ERROR", ex);
        }
        LOGGER.info("Initialization SUCCESS");
    }

    @Override
    public ArrayList<String> getAll() {
        return this.installations;
    }

    @Override
    public String get(int index) {
        return index < 0 | index >= this.installations.size() ? null : this.installations.get(index);
    }

    @Override
    public void add(String object) {
        if (object != null && !this.installations.contains(object)) {
            this.installations.add(object);
            new BackgroundAction().add(object);
        }
    }

    @Override
    public void addInCurrentThread(ArrayList<String> installations) {
        if (installations != null && !installations.isEmpty()) {
            for (String installation : installations) {
                if (!this.installations.contains(installation)){
                    this.installations.add(installation);
                }
            }
            new BackgroundAction().addInstallations(installations);
        }
    }

    @Override
    public void set(String oldObject, String newObject) {
        if (oldObject != null && newObject != null
                && this.installations.contains(oldObject) && !this.installations.contains(newObject)) {
            int index = this.installations.indexOf(oldObject);
            this.installations.set(index, newObject);
            new BackgroundAction().set(oldObject, newObject);
        }
    }

    @Override
    public void remove(String object) {
        if (object != null && this.installations.remove(object)) {
            new BackgroundAction().remove(object);
        }
    }

    @Override
    public void clear() {
        this.installations.clear();
        new BackgroundAction().clear();
    }

    @Override
    public void rewrite(ArrayList<String> newList) {
        if (newList != null && !newList.isEmpty()) {
            this.installations.clear();
            this.installations.addAll(newList);
            new BackgroundAction().rewrite(newList);
        }
    }

    @Override
    public void rewriteInCurrentThread(ArrayList<String>newList){
        if (newList != null && !newList.isEmpty()) {
            this.installations.clear();
            this.installations.addAll(newList);
            new BackgroundAction().rewriteInstallations(newList);
        }
    }

    @Override
    public void export() {
        new BackgroundAction().export(this.installations);
    }

    private class BackgroundAction extends SwingWorker<Boolean, Void> {
        private String object, old;
        private ArrayList<String>list;
        private Action action;
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

        void export(ArrayList<String>installations){
            this.list = installations;
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
                    return this.addInstallation(this.object);
                case REMOVE:
                    return this.removeInstallation(this.object);
                case CLEAR:
                    return this.clearInstallations();
                case REWRITE:
                    return this.rewriteInstallations(this.list);
                case SET:
                    return this.setInstallation(this.old, this.object);
                case EXPORT:
                    return this.exportInstallations(this.list);
            }
            return true;
        }

        @Override
        protected void done() {
            try {
                if (!this.get()){
                    switch (this.action){
                        case ADD:
                            installations.remove(this.object);
                            break;
                        case REMOVE:
                            if (!installations.contains(this.object)) installations.add(this.object);
                            break;
                        case SET:
                            installations.remove(this.object);
                            if (!installations.contains(this.old)) installations.add(this.old);
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

        private boolean addInstallation(String installation){
            LOGGER.fine("Get connection with DB");
            try (Connection connection = getConnection()){
                LOGGER.fine("Send requests to add");
                String sql = "INSERT INTO installations ('installation') "
                        + "VALUES(?);";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, installation);
                statement.execute();

                LOGGER.fine("Close connection");
                statement.close();
                return true;
            }catch (SQLException ex){
                LOGGER.log(Level.SEVERE, "ERROR: ", ex);
                return false;
            }
        }

        void addInstallations(ArrayList<String>installations){
            LOGGER.fine("Get connection with DB");
            try (Connection connection = getConnection()){
                Statement statement = connection.createStatement();
                LOGGER.fine("Send request to add");
                for (String installation : installations){
                    String sql = "INSERT INTO installations(installation)"
                            + "SELECT " + installation + " "
                            + "WHERE NOT EXISTS(SELECT 1 FROM installations WHERE installation = " + installation + ");";
                    statement.execute(sql);
                }

                LOGGER.fine("Close connection");
                statement.close();
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, "Error: ", ex);
            }
        }

        private boolean removeInstallation(String installation){
            LOGGER.fine("Get connection with DB");
            try (Connection connection = getConnection()){
                LOGGER.fine("Send request to delete");
                String sql = "DELETE FROM installations WHERE installation = '" + installation + "';";
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

        private boolean clearInstallations(){
            LOGGER.fine("Get connection with DB");
            try (Connection connection = getConnection()) {
                LOGGER.fine("Send request");
                String sql = "DELETE FROM installations;";
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

        private boolean setInstallation(String oldInstallation, String newInstallation){
            LOGGER.fine("Get connection with DB");
            try (Connection connection = getConnection()){
                LOGGER.fine("Send request");
                Statement statement = connection.createStatement();
                String sql = "UPDATE installations SET installation = '" + newInstallation + "' WHERE installation = '" + oldInstallation + "';";
                statement.execute(sql);

                LOGGER.fine("Close connections");
                statement.close();
                return true;
            }catch (SQLException ex){
                LOGGER.log(Level.SEVERE, "ERROR: ", ex);
                return false;
            }
        }

        public boolean rewriteInstallations(ArrayList<String>installations){
            LOGGER.fine("Get connection with DB");
            try (Connection connection = getConnection()) {
                LOGGER.fine("Send request to clear");
                Statement statementClear = connection.createStatement();
                String sql = "DELETE FROM installations;";
                statementClear.execute(sql);

                if (!installations.isEmpty()) {
                    LOGGER.fine("Send requests to add");
                    sql = "INSERT INTO installations ('installation') "
                                + "VALUES(?);";
                    PreparedStatement statement = connection.prepareStatement(sql);
                    for (String installation : installations) {
                        statement.setString(1, installation);
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

        private boolean exportInstallations(ArrayList<String>installations){
            Calendar date = Calendar.getInstance();
            String fileName = "export_installations ["
                    + date.get(Calendar.DAY_OF_MONTH)
                    + "."
                    + (date.get(Calendar.MONTH) + 1)
                    + "."
                    + date.get(Calendar.YEAR)
                    + "].db";
            String dbUrl = "jdbc:sqlite:Support/Export/" + fileName;
            String sql = "CREATE TABLE IF NOT EXISTS installations ("
                    + "installation text NOT NULL UNIQUE"
                    + ", PRIMARY KEY (\"installation\")"
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
                sql = "INSERT INTO installations ('installation') "
                        + "VALUES(?);";
                preparedStatement = connection.prepareStatement(sql);
                for (String installation : installations) {
                    preparedStatement.setString(1, installation);
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
                } catch (SQLException ignored) {}
                return false;
            }
        }
    }
}