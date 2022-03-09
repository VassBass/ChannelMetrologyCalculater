package repository.impl;

import application.Application;
import application.ApplicationContext;
import constants.Action;
import org.sqlite.JDBC;
import repository.InstallationRepository;
import ui.model.SaveMessage;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InstallationRepositoryImpl implements InstallationRepository {
    private static final Logger LOGGER = Logger.getLogger(InstallationRepository.class.getName());
    private final String dbUrl;

    public InstallationRepositoryImpl(){
        this.dbUrl = Application.pathToDB;
        this.init();
    }

    public InstallationRepositoryImpl(String dbUrl){
        this.dbUrl = dbUrl;
        this.init();
    }

    private Connection getConnection() throws SQLException {
        DriverManager.registerDriver(new JDBC());
        return DriverManager.getConnection(this.dbUrl);
    }

    private void init(){
        LOGGER.fine("Initialization ...");
        String sql = "CREATE TABLE IF NOT EXISTS installations ("
                + "installation text NOT NULL UNIQUE"
                + ", PRIMARY KEY (\"installation\")"
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
        ArrayList<String>installations = new ArrayList<>();
        LOGGER.fine("Get connection with DB");
        try (Connection connection = this.getConnection()){
            Statement statement = connection.createStatement();

            LOGGER.fine("Send request");
            String sql = "SELECT * FROM installations";
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()){
                installations.add(resultSet.getString("installation"));
            }

            LOGGER.fine("Close connections");
            resultSet.close();
            statement.close();
        }catch (SQLException ex){
            LOGGER.log(Level.SEVERE, "ERROR: ", ex);
        }
        return installations;
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
        new BackgroundAction().rewriteInstallations(newList);
    }

    @Override
    public void export(ArrayList<String> installations) {
        new BackgroundAction().export(installations);
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
        protected Void doInBackground() throws Exception {
            switch (this.action){
                case ADD:
                    this.addInstallation(this.object);
                    break;
                case REMOVE:
                    this.removeInstallation(this.object);
                    break;
                case CLEAR:
                    this.clearInstallations();
                    break;
                case REWRITE:
                    this.rewriteInstallations(this.list);
                    break;
                case SET:
                    this.setInstallation(this.old, this.object);
                    break;
                case EXPORT:
                    this.exportInstallations(this.list);
                    break;
            }
            return null;
        }

        @Override
        protected void done() {
            Application.setBusy(false);
            if (this.saveMessage != null) this.saveMessage.dispose();
        }

        private void addInstallation(String installation){
            LOGGER.fine("Get connection with DB");
            try (Connection connection = getConnection()){
                LOGGER.fine("Send request to delete");
                String sql = "DELETE FROM installations WHERE installation = '?';";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, installation);
                statement.execute();

                LOGGER.fine("Send requests to add");
                sql = "INSERT INTO installations ('installation') "
                        + "VALUES(?);";
                statement = connection.prepareStatement(sql);
                statement.setString(1, installation);
                statement.execute();

                LOGGER.fine("Close connection");
                statement.close();
            }catch (SQLException ex){
                LOGGER.log(Level.SEVERE, "ERROR: ", ex);
            }
        }

        private void removeInstallation(String installation){
            LOGGER.fine("Get connection with DB");
            try (Connection connection = getConnection()){
                LOGGER.fine("Send request to delete");
                String sql = "DELETE FROM installations WHERE installation = '" + installation + "';";
                Statement statement = connection.createStatement();
                statement.execute(sql);

                LOGGER.fine("Close connection");
                statement.close();
            }catch (SQLException ex){
                LOGGER.log(Level.SEVERE, "ERROR: ", ex);
            }
        }

        private void clearInstallations(){
            LOGGER.fine("Get connection with DB");
            try (Connection connection = getConnection()) {
                LOGGER.fine("Send request");
                String sql = "DELETE FROM installations;";
                Statement statement = connection.createStatement();
                statement.execute(sql);

                LOGGER.fine("Close connections");
                statement.close();
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, "ERROR: ", ex);
            }
        }

        private void setInstallation(String oldInstallation, String newInstallation){
            LOGGER.fine("Get connection with DB");
            try (Connection connection = getConnection()){
                LOGGER.fine("Send request to delete");
                Statement statementClear = connection.createStatement();
                String sql = "DELETE FROM installations WHERE installation = '" + oldInstallation + "';";
                statementClear.execute(sql);

                LOGGER.fine("Send requests to add");
                sql = "INSERT INTO installations ('installation') "
                        + "VALUES(?);";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, newInstallation);
                statement.execute(sql);

                LOGGER.fine("Close connections");
                statementClear.close();
                statement.close();
            }catch (SQLException ex){
                LOGGER.log(Level.SEVERE, "ERROR: ", ex);
            }
        }

        public void rewriteInstallations(ArrayList<String>installations){
            if (installations != null) {
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
                } catch (SQLException ex) {
                    LOGGER.log(Level.SEVERE, "ERROR: ", ex);
                }
            }
        }

        private void exportInstallations(ArrayList<String>installations){
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
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, "Initialization ERROR", ex);
                try {
                    if (statement != null) statement.close();
                    if (preparedStatement != null) preparedStatement.close();
                    if (connection != null) connection.close();
                } catch (SQLException ignored) {}
            }
        }
    }
}