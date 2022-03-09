package repository.impl;

import application.Application;
import application.ApplicationContext;
import constants.Action;
import org.sqlite.JDBC;
import repository.DepartmentRepository;
import repository.Repository;
import ui.model.SaveMessage;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DepartmentRepositoryImpl extends Repository implements DepartmentRepository {
    private static final Logger LOGGER = Logger.getLogger(DepartmentRepository.class.getName());

    public DepartmentRepositoryImpl(){super();}
    public DepartmentRepositoryImpl(String dbUrl){super(dbUrl);}

    @Override
    protected void init(){
        LOGGER.fine("Initialization ...");
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
        new BackgroundAction().rewriteDepartments(newList);
    }

    @Override
    public void export(ArrayList<String> departments) {
        new BackgroundAction().export(departments);
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

        void export(ArrayList<String>departments){
            this.list = departments;
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
                    this.addDepartment(this.object);
                    break;
                case REMOVE:
                    this.removeDepartment(this.object);
                    break;
                case CLEAR:
                    this.clearDepartments();
                    break;
                case REWRITE:
                    this.rewriteDepartments(this.list);
                    break;
                case SET:
                    this.setDepartment(this.old, this.object);
                    break;
                case EXPORT:
                    this.exportDepartments(this.list);
                    break;
            }
            return null;
        }

        @Override
        protected void done() {
            Application.setBusy(false);
             if (this.saveMessage != null) this.saveMessage.dispose();
        }

        private void addDepartment(String department){
            LOGGER.fine("Get connection with DB");
            try (Connection connection = getConnection()){
                LOGGER.fine("Send request to delete");
                String sql = "DELETE FROM departments WHERE department = '?';";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, department);
                statement.execute();

                LOGGER.fine("Send requests to add");
                sql = "INSERT INTO department ('department') "
                        + "VALUES(?);";
                statement = connection.prepareStatement(sql);
                statement.setString(1, department);
                statement.execute();

                LOGGER.fine("Close connection");
                statement.close();
            }catch (SQLException ex){
                LOGGER.log(Level.SEVERE, "ERROR: ", ex);
            }
        }

        private void removeDepartment(String department){
            LOGGER.fine("Get connection with DB");
            try (Connection connection = getConnection()){
                LOGGER.fine("Send request to delete");
                String sql = "DELETE FROM departments WHERE department = '" + department + "';";
                Statement statement = connection.createStatement();
                statement.execute(sql);

                LOGGER.fine("Close connection");
                statement.close();
            }catch (SQLException ex){
                LOGGER.log(Level.SEVERE, "ERROR: ", ex);
            }
        }

        private void clearDepartments(){
            LOGGER.fine("Get connection with DB");
            try (Connection connection = getConnection()) {
                LOGGER.fine("Send request");
                String sql = "DELETE FROM departments;";
                Statement statement = connection.createStatement();
                statement.execute(sql);

                LOGGER.fine("Close connections");
                statement.close();
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, "ERROR: ", ex);
            }
        }

        private void setDepartment(String oldDepartment, String newDepartment){
            LOGGER.fine("Get connection with DB");
            try (Connection connection = getConnection()){
                LOGGER.fine("Send request to delete");
                Statement statementClear = connection.createStatement();
                String sql = "DELETE FROM departments WHERE department = '" + oldDepartment + "';";
                statementClear.execute(sql);

                LOGGER.fine("Send requests to add");
                sql = "INSERT INTO departments ('department') "
                        + "VALUES(?);";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, newDepartment);
                statement.execute(sql);

                LOGGER.fine("Close connections");
                statementClear.close();
                statement.close();
            }catch (SQLException ex){
                LOGGER.log(Level.SEVERE, "ERROR: ", ex);
            }
        }

        public void rewriteDepartments(ArrayList<String>departments){
            if (departments != null) {
                LOGGER.fine("Get connection with DB");
                try (Connection connection = getConnection()) {
                    LOGGER.fine("Send request to clear");
                    Statement statementClear = connection.createStatement();
                    String sql = "DELETE FROM departments;";
                    statementClear.execute(sql);

                    if (!departments.isEmpty()) {
                        LOGGER.fine("Send requests to add");
                        sql = "INSERT INTO departments ('department') "
                                + "VALUES(?);";
                        PreparedStatement statement = connection.prepareStatement(sql);
                        for (String department : departments) {
                            statement.setString(1, department);
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

        private void exportDepartments(ArrayList<String>departments){
            Calendar date = Calendar.getInstance();
            String fileName = "export_departments ["
                    + date.get(Calendar.DAY_OF_MONTH)
                    + "."
                    + (date.get(Calendar.MONTH) + 1)
                    + "."
                    + date.get(Calendar.YEAR)
                    + "].db";
            String dbUrl = "jdbc:sqlite:Support/Export/" + fileName;
            String sql = "CREATE TABLE IF NOT EXISTS departments ("
                    + "department text NOT NULL UNIQUE"
                    + ", PRIMARY KEY (\"department\")"
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
                sql = "INSERT INTO departments ('department') "
                        + "VALUES(?);";
                preparedStatement = connection.prepareStatement(sql);
                for (String department : departments) {
                    preparedStatement.setString(1, department);
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