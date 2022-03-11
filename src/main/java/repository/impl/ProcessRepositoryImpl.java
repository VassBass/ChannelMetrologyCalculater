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
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProcessRepositoryImpl extends Repository implements ProcessRepository {
    private static final Logger LOGGER = Logger.getLogger(ProcessRepository.class.getName());

    private final ArrayList<String> processes = new ArrayList<>();

    public ProcessRepositoryImpl(){
        super();
    }

    public ProcessRepositoryImpl(String dbUrl){
        super(dbUrl);
    }

    @Override
    protected void init(){
        LOGGER.fine("Get connection with DB");
        try (Connection connection = this.getConnection()){
            String sql = "CREATE TABLE IF NOT EXISTS processes ("
                    + "process text NOT NULL UNIQUE"
                    + ", PRIMARY KEY (\"process\")"
                    + ");";
            Statement statement = connection.createStatement();

            LOGGER.fine("Send request to create table");
            statement.execute(sql);

            LOGGER.fine("Send request to read processes from DB");
            sql = "SELECT * FROM processes";
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()){
                this.processes.add(resultSet.getString("process"));
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
        return this.processes;
    }

    @Override
    public String get(int index) {
        return index < 0 | index >= this.processes.size() ? null : this.processes.get(index);
    }

    @Override
    public void add(String object) {
        if (object != null && !this.processes.contains(object)) {
            this.processes.add(object);
            new BackgroundAction().add(object);
        }
    }

    @Override
    public void set(String oldObject, String newObject) {
        if (oldObject != null && newObject != null
                && this.processes.contains(oldObject) && !this.processes.contains(newObject)) {
            int index = this.processes.indexOf(oldObject);
            this.processes.set(index, newObject);
            new BackgroundAction().set(oldObject, newObject);
        }
    }

    @Override
    public void remove(String object) {
        if (object != null && this.processes.contains(object)) {
            this.processes.remove(object);
            new BackgroundAction().remove(object);
        }
    }

    @Override
    public void clear() {
        this.processes.clear();
        new BackgroundAction().clear();
    }

    @Override
    public void rewrite(ArrayList<String> newList) {
        if (newList != null && !newList.isEmpty()) {
            this.processes.clear();
            this.processes.addAll(newList);
            new BackgroundAction().rewrite(newList);
        }
    }

    @Override
    public void rewriteInCurrentThread(ArrayList<String>newList){
        if (newList != null && !newList.isEmpty()) {
            this.processes.clear();
            this.processes.addAll(newList);
            new BackgroundAction().rewriteProcesses(newList);
        }
    }

    @Override
    public void export() {
        new BackgroundAction().export(this.processes);
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
        protected Boolean doInBackground() throws Exception {
            switch (this.action){
                case ADD:
                    return this.addProcess(this.object);
                case REMOVE:
                    return this.removeProcess(this.object);
                case CLEAR:
                    return this.clearProcesses();
                case REWRITE:
                    return this.rewriteProcesses(this.list);
                case SET:
                    return this.setProcess(this.old, this.object);
                case EXPORT:
                    return this.exportProcesses(this.list);
            }
            return true;
        }

        @Override
        protected void done() {
            try {
                if (!this.get()){
                    switch (this.action){
                        case ADD:
                            processes.remove(this.object);
                            break;
                        case REMOVE:
                            processes.add(this.object);
                            break;
                        case SET:
                            processes.remove(this.object);
                            if (!processes.contains(this.old)) processes.add(this.old);
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

        private boolean addProcess(String process){
            LOGGER.fine("Get connection with DB");
            try (Connection connection = getConnection()){
                LOGGER.fine("Send requests to add");
                String sql = "INSERT INTO process ('process') "
                        + "VALUES(?);";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, process);
                statement.execute();

                LOGGER.fine("Close connection");
                statement.close();
                return true;
            }catch (SQLException ex){
                LOGGER.log(Level.SEVERE, "ERROR: ", ex);
                return false;
            }
        }

        private boolean removeProcess(String process){
            LOGGER.fine("Get connection with DB");
            try (Connection connection = getConnection()){
                LOGGER.fine("Send request to delete");
                String sql = "DELETE FROM processes WHERE process = '" + process + "';";
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

        private boolean clearProcesses(){
            LOGGER.fine("Get connection with DB");
            try (Connection connection = getConnection()) {
                LOGGER.fine("Send request");
                String sql = "DELETE FROM processes;";
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

        private boolean setProcess(String oldProcess, String newProcess){
            LOGGER.fine("Get connection with DB");
            try (Connection connection = getConnection()){
                LOGGER.fine("Send request");
                Statement statement = connection.createStatement();
                String sql = "UPDATE processes SET process = '" + newProcess + "' WHERE process = '" + oldProcess + "';";
                statement.execute(sql);

                LOGGER.fine("Close connections");
                statement.close();
                return true;
            }catch (SQLException ex){
                LOGGER.log(Level.SEVERE, "ERROR: ", ex);
                return false;
            }
        }

        public boolean rewriteProcesses(ArrayList<String>processes){
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
                return true;
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, "ERROR: ", ex);
                return false;
            }
        }

        private boolean exportProcesses(ArrayList<String>processes){
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