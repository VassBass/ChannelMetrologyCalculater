package repository.impl;

import application.Application;
import application.ApplicationContext;
import constants.Action;
import repository.ProcessRepository;
import repository.Repository;
import ui.model.SaveMessage;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProcessRepositoryImpl extends Repository<String> implements ProcessRepository {
    private static final Logger LOGGER = Logger.getLogger(ProcessRepository.class.getName());

    private boolean backgroundTaskRunning = false;

    public ProcessRepositoryImpl(){
        super();
    }

    public ProcessRepositoryImpl(String dbUrl){
        super(dbUrl);
    }

    @Override
    protected void init(){
        LOGGER.fine("Get connection with DB");
        try (Connection connection = this.getConnection();
            Statement statement = connection.createStatement()){
            String sql = "CREATE TABLE IF NOT EXISTS processes ("
                    + "process text NOT NULL UNIQUE"
                    + ", PRIMARY KEY (\"process\")"
                    + ");";
            LOGGER.fine("Send request to create table");
            statement.execute(sql);

            LOGGER.fine("Send request to read processes from DB");
            sql = "SELECT * FROM processes";
            try (ResultSet resultSet = statement.executeQuery(sql)) {
                while (resultSet.next()) {
                    this.mainList.add(resultSet.getString("process"));
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Initialization ERROR", ex);
        }
        LOGGER.info("Initialization SUCCESS");
    }

    @Override
    public ArrayList<String> getAll() {
        return this.mainList;
    }

    @Override
    public String get(int index) {
        return index < 0 | index >= this.mainList.size() ? null : this.mainList.get(index);
    }

    @Override
    public void add(String object) {
        if (object != null && !this.mainList.contains(object)) {
            this.mainList.add(object);
            new BackgroundAction().add(object);
        }
    }

    @Override
    public void addInCurrentThread(ArrayList<String> processes) {
        if (processes != null && !processes.isEmpty()) {
            for (String process : processes) {
                if (!this.mainList.contains(process)){
                    this.mainList.add(process);
                }
            }
            new BackgroundAction().addProcesses(processes);
        }
    }

    @Override
    public void set(String oldObject, String newObject) {
        if (oldObject != null && newObject != null
                && this.mainList.contains(oldObject)) {
            int oldIndex = this.mainList.indexOf(oldObject);
            int newIndex = this.mainList.indexOf(newObject);
            if (newIndex == -1 || oldIndex == newIndex) {
                this.mainList.set(oldIndex, newObject);
                new BackgroundAction().set(oldObject, newObject);
            }
        }
    }

    @Override
    public void remove(String object) {
        if (object != null && this.mainList.remove(object)) {
            new BackgroundAction().remove(object);
        }
    }

    @Override
    public void clear() {
        this.mainList.clear();
        new BackgroundAction().clear();
    }

    @Override
    public void rewrite(ArrayList<String> newList) {
        if (newList != null && !newList.isEmpty()) {
            this.mainList.clear();
            this.mainList.addAll(newList);
            new BackgroundAction().rewrite(newList);
        }
    }

    @Override
    public void rewriteInCurrentThread(ArrayList<String>newList){
        if (newList != null && !newList.isEmpty()) {
            this.mainList.clear();
            this.mainList.addAll(newList);
            new BackgroundAction().rewriteProcesses(newList);
        }
    }

    @Override
    public boolean backgroundTaskIsRun() {
        return this.backgroundTaskRunning;
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

        private void start(){
            Application.setBusy(true);
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    if (saveMessage != null) saveMessage.setVisible(true);
                }
            });
            backgroundTaskRunning = true;
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
            }
            return true;
        }

        @Override
        protected void done() {
            try {
                if (!this.get()){
                    switch (this.action){
                        case ADD:
                            mainList.remove(this.object);
                            break;
                        case REMOVE:
                            if (!mainList.contains(this.object)) mainList.add(this.object);
                            break;
                        case SET:
                            mainList.remove(this.object);
                            if (!mainList.contains(this.old)) mainList.add(this.old);
                            break;
                    }
                    String message = "?????????????? ??????????????! ?????????? ???? ????????????????????! ?????????????????? ????????-?????????? ???? ??????!";
                    if (Application.context != null) JOptionPane.showMessageDialog(Application.context.mainScreen, message, "??????????????!", JOptionPane.ERROR_MESSAGE);
                }
            } catch (ExecutionException | InterruptedException e) {
                LOGGER.log(Level.SEVERE, "ERROR: ", e);
            }
            Application.setBusy(false);
            backgroundTaskRunning = false;
            if (this.saveMessage != null) this.saveMessage.dispose();
        }

        private boolean addProcess(String process){
            LOGGER.fine("Get connection with DB");
            try (Connection connection = getConnection();
                Statement statement = connection.createStatement()){
                LOGGER.fine("Send requests to add");
                String sql = "INSERT INTO processes ('process') "
                        + "VALUES('" + process + "');";
                statement.execute(sql);
            }catch (SQLException ex){
                LOGGER.log(Level.SEVERE, "ERROR: ", ex);
                return false;
            }
            return true;
        }

        void addProcesses(ArrayList<String>processes){
            LOGGER.fine("Get connection with DB");
            try (Connection connection = getConnection();
                Statement statement = connection.createStatement()){
                LOGGER.fine("Send request to add");
                for (String process : processes){
                    String sql = "INSERT INTO processes(process)"
                            + "SELECT '" + process + "' "
                            + "WHERE NOT EXISTS(SELECT 1 FROM processes WHERE process = '" + process + "');";
                    statement.execute(sql);
                }
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, "Error: ", ex);
            }
        }

        private boolean removeProcess(String process){
            LOGGER.fine("Get connection with DB");
            try (Connection connection = getConnection();
                Statement statement = connection.createStatement()){
                LOGGER.fine("Send request to delete");
                String sql = "DELETE FROM processes WHERE process = '" + process + "';";
                statement.execute(sql);
            }catch (SQLException ex){
                LOGGER.log(Level.SEVERE, "ERROR: ", ex);
                return false;
            }
            return true;
        }

        private boolean clearProcesses(){
            LOGGER.fine("Get connection with DB");
            try (Connection connection = getConnection();
                Statement statement = connection.createStatement()) {
                LOGGER.fine("Send request");
                String sql = "DELETE FROM processes;";
                statement.execute(sql);
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, "ERROR: ", ex);
                return false;
            }
            return true;
        }

        private boolean setProcess(String oldProcess, String newProcess){
            LOGGER.fine("Get connection with DB");
            try (Connection connection = getConnection();
                Statement statement = connection.createStatement()){
                LOGGER.fine("Send request");
                String sql = "UPDATE processes SET process = '" + newProcess + "' WHERE process = '" + oldProcess + "';";
                statement.execute(sql);
            }catch (SQLException ex){
                LOGGER.log(Level.SEVERE, "ERROR: ", ex);
                return false;
            }
            return true;
        }

        public boolean rewriteProcesses(ArrayList<String>processes){
            LOGGER.fine("Get connection with DB");
            try (Connection connection = getConnection();
                Statement statement = connection.createStatement()) {
                LOGGER.fine("Send request to clear");
                String sql = "DELETE FROM processes;";
                statement.execute(sql);

                if (!processes.isEmpty()) {
                    LOGGER.fine("Send requests to add");
                    for (String process : processes) {
                        sql = "INSERT INTO processes ('process') "
                                + "VALUES('" + process + "');";
                        statement.execute(sql);
                    }
                }
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, "ERROR: ", ex);
                return false;
            }
            return true;
        }
    }
}