package repository.impl;

import application.Application;
import application.ApplicationContext;
import constants.Action;
import org.sqlite.JDBC;
import repository.ProcessRepository;
import repository.Repository;
import ui.model.SaveMessage;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProcessRepositoryImpl extends Repository implements ProcessRepository {
    private static final Logger LOGGER = Logger.getLogger(ProcessRepository.class.getName());

    public ProcessRepositoryImpl(){
        super();
    }

    public ProcessRepositoryImpl(String dbUrl){
        super(dbUrl);
    }

    @Override
    protected void init(){
        LOGGER.fine("Initialization ...");
        String sql = "CREATE TABLE IF NOT EXISTS processes ("
                + "process text NOT NULL UNIQUE"
                + ", PRIMARY KEY (\"process\")"
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
        ArrayList<String>processes = new ArrayList<>();
        LOGGER.fine("Get connection with DB");
        try (Connection connection = this.getConnection()){
            Statement statement = connection.createStatement();

            LOGGER.fine("Send request");
            String sql = "SELECT * FROM processes";
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()){
                processes.add(resultSet.getString("process"));
            }

            LOGGER.fine("Close connections");
            resultSet.close();
            statement.close();
        }catch (SQLException ex){
            LOGGER.log(Level.SEVERE, "ERROR: ", ex);
        }
        return processes;
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
        new BackgroundAction().rewriteProcesses(newList);
    }

    @Override
    public void export(ArrayList<String> processes) {
        new BackgroundAction().export(processes);
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

        void export(ArrayList<String>processes){
            this.list = processes;
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
                    this.addProcess(this.object);
                    break;
                case REMOVE:
                    this.removeProcess(this.object);
                    break;
                case CLEAR:
                    this.clearProcesses();
                    break;
                case REWRITE:
                    this.rewriteProcesses(this.list);
                    break;
                case SET:
                    this.setProcess(this.old, this.object);
                    break;
                case EXPORT:
                    this.exportProcesses(this.list);
                    break;
            }
            return null;
        }

        @Override
        protected void done() {
            Application.setBusy(false);
            if (this.saveMessage != null) this.saveMessage.dispose();
        }

        private void addProcess(String process){
            LOGGER.fine("Get connection with DB");
            try (Connection connection = getConnection()){
                LOGGER.fine("Send request to delete");
                String sql = "DELETE FROM processes WHERE process = '?';";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, process);
                statement.execute();

                LOGGER.fine("Send requests to add");
                sql = "INSERT INTO process ('process') "
                        + "VALUES(?);";
                statement = connection.prepareStatement(sql);
                statement.setString(1, process);
                statement.execute();

                LOGGER.fine("Close connection");
                statement.close();
            }catch (SQLException ex){
                LOGGER.log(Level.SEVERE, "ERROR: ", ex);
            }
        }

        private void removeProcess(String process){
            LOGGER.fine("Get connection with DB");
            try (Connection connection = getConnection()){
                LOGGER.fine("Send request to delete");
                String sql = "DELETE FROM processes WHERE process = '" + process + "';";
                Statement statement = connection.createStatement();
                statement.execute(sql);

                LOGGER.fine("Close connection");
                statement.close();
            }catch (SQLException ex){
                LOGGER.log(Level.SEVERE, "ERROR: ", ex);
            }
        }

        private void clearProcesses(){
            LOGGER.fine("Get connection with DB");
            try (Connection connection = getConnection()) {
                LOGGER.fine("Send request");
                String sql = "DELETE FROM processes;";
                Statement statement = connection.createStatement();
                statement.execute(sql);

                LOGGER.fine("Close connections");
                statement.close();
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, "ERROR: ", ex);
            }
        }

        private void setProcess(String oldProcess, String newProcess){
            LOGGER.fine("Get connection with DB");
            try (Connection connection = getConnection()){
                LOGGER.fine("Send request to delete");
                Statement statementClear = connection.createStatement();
                String sql = "DELETE FROM processes WHERE process = '" + oldProcess + "';";
                statementClear.execute(sql);

                LOGGER.fine("Send requests to add");
                sql = "INSERT INTO processes ('process') "
                        + "VALUES(?);";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, newProcess);
                statement.execute(sql);

                LOGGER.fine("Close connections");
                statementClear.close();
                statement.close();
            }catch (SQLException ex){
                LOGGER.log(Level.SEVERE, "ERROR: ", ex);
            }
        }

        public void rewriteProcesses(ArrayList<String>processes){
            if (processes != null) {
                LOGGER.fine("Get connection with DB");
                try (Connection connection = getConnection()) {
                    LOGGER.fine("Send request to clear");
                    Statement statementClear = connection.createStatement();
                    String sql = "DELETE FROM processes;";
                    statementClear.execute(sql);

                    if (!processes.isEmpty()) {
                        LOGGER.fine("Send requests to add");
                        sql = "INSERT INTO processes ('process') "
                                + "VALUES(?);";
                        PreparedStatement statement = connection.prepareStatement(sql);
                        for (String process : processes) {
                            statement.setString(1, process);
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

        private void exportProcesses(ArrayList<String>processes){
            Calendar date = Calendar.getInstance();
            String fileName = "export_processes ["
                    + date.get(Calendar.DAY_OF_MONTH)
                    + "."
                    + (date.get(Calendar.MONTH) + 1)
                    + "."
                    + date.get(Calendar.YEAR)
                    + "].db";
            String dbUrl = "jdbc:sqlite:Support/Export/" + fileName;
            String sql = "CREATE TABLE IF NOT EXISTS processes ("
                    + "process text NOT NULL UNIQUE"
                    + ", PRIMARY KEY (\"process\")"
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
                sql = "INSERT INTO processes ('process') "
                        + "VALUES(?);";
                preparedStatement = connection.prepareStatement(sql);
                for (String process : processes) {
                    preparedStatement.setString(1, process);
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