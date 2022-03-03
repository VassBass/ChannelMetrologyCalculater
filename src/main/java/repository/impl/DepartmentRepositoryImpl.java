package repository.impl;

import application.Application;
import application.ApplicationContext;
import constants.Action;
import org.sqlite.JDBC;
import repository.DepartmentRepository;
import ui.model.SaveMessage;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DepartmentRepositoryImpl implements DepartmentRepository {
    private static final Logger LOGGER = Logger.getLogger(DepartmentRepository.class.getName());
    private final String dbUrl;

    public DepartmentRepositoryImpl(){
        this.dbUrl = Application.pathToDB;
        this.init();
    }

    public DepartmentRepositoryImpl(String dbUrl){
        this.dbUrl = dbUrl;
        this.init();
    }

    private Connection getConnection() throws SQLException {
        DriverManager.registerDriver(new JDBC());
        return DriverManager.getConnection(this.dbUrl);
    }

    private void init(){
        LOGGER.info("Initialization ...");
        String sql = "CREATE TABLE IF NOT EXISTS departments ("
                + "department text NOT NULL UNIQUE"
                + ", PRIMARY KEY (\"department\")"
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
        ArrayList<String>departments = new ArrayList<>();
        LOGGER.fine("Get connection with DB");
        try (Connection connection = this.getConnection()){
            Statement statement = connection.createStatement();

            LOGGER.fine("Send request");
            String sql = "SELECT * FROM departments";
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()){
                departments.add(resultSet.getString("department"));
            }

            LOGGER.fine("Close connections");
            resultSet.close();
            statement.close();
        }catch (SQLException ex){
            LOGGER.log(Level.SEVERE, "ERROR: ", ex);
        }
        return departments;
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
        LOGGER.fine("Get connection with DB");
        try (Connection connection = this.getConnection()){
            LOGGER.fine("Send request to clear");
            Statement statementClear = connection.createStatement();
            String sql = "DELETE FROM departments;";
            statementClear.execute(sql);

            LOGGER.fine("Send requests to add");
            sql = "INSERT INTO departments ('department') VALUES (?);";
            PreparedStatement statement = connection.prepareStatement(sql);
            for (String department : newList){
                statement.setString(1, department);
                statement.execute();
            }

            LOGGER.fine("Close connections");
            statementClear.close();
            statement.close();
        }catch (SQLException ex){
            LOGGER.log(Level.SEVERE, "ERROR: ", ex);
        }
    }

    private class BackgroundAction extends SwingWorker<Void, Void>{
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
                    sql = "REPLACE INTO departments (department) "
                            + "VALUES('" + this.object + "');";
                    break;
                case REMOVE:
                    sql = "DELETE FROM departments WHERE department = '" + this.object + "';";
                    break;
                case CLEAR:
                    sql = "DELETE FROM departments;";
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
                    sql = "DELETE FROM departments;";
                    statementClear.execute(sql);

                    LOGGER.fine("Send requests to add");
                    sql = "INSERT INTO departments ('department') VALUES (?);";
                    PreparedStatement statement = connection.prepareStatement(sql);
                    for (String department : this.list){
                        statement.setString(1, department);
                        statement.execute();
                    }

                    LOGGER.fine("Close connections");
                    statementClear.close();
                    statement.close();
                }catch (SQLException ex){
                    LOGGER.log(Level.SEVERE, "ERROR: ", ex);
                }
            }else if (this.old != null){
                LOGGER.fine("Get connection with DB");
                try (Connection connection = getConnection()){
                    LOGGER.fine("Send request to delete");
                    Statement statementClear = connection.createStatement();
                    sql = "DELETE FROM departments WHERE department = '" + this.old + "';";
                    statementClear.execute(sql);

                    LOGGER.fine("Send requests to add");
                    sql = "INSERT INTO departments ('department') VALUES ('" + this.object + "');";
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
