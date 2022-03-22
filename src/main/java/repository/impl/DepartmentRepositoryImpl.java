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
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DepartmentRepositoryImpl extends Repository implements DepartmentRepository {
    private static final Logger LOGGER = Logger.getLogger(DepartmentRepository.class.getName());

    private final ArrayList<String>departments = new ArrayList<>();

    public DepartmentRepositoryImpl(){super();}
    public DepartmentRepositoryImpl(String dbUrl){super(dbUrl);}

    @Override
    protected void init(){
        LOGGER.fine("Get connection with DB");
        try (Connection connection = this.getConnection()){
            String sql = "CREATE TABLE IF NOT EXISTS departments ("
                    + "department text NOT NULL UNIQUE"
                    + ", PRIMARY KEY (\"department\")"
                    + ");";
            Statement statement = connection.createStatement();

            LOGGER.fine("Send request to create table");
            statement.execute(sql);

            LOGGER.fine("Send request to read departments from DB");
            sql = "SELECT * FROM departments";
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()){
                this.departments.add(resultSet.getString("department"));
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
        return this.departments;
    }

    @Override
    public String get(int index) {
        return index < 0 | index >= this.departments.size() ? null : this.departments.get(index);
    }

    @Override
    public void add(String object) {
        if (object != null && !this.departments.contains(object)) {
            this.departments.add(object);
            new BackgroundAction().add(object);
        }
    }

    @Override
    public void addInCurrentThread(ArrayList<String> departments) {
        if (departments != null && !departments.isEmpty()) {
            for (String department : departments) {
                if (!this.departments.contains(department)){
                    this.departments.add(department);
                }
            }
            new BackgroundAction().addDepartments(departments);
        }
    }

    @Override
    public void set(String oldObject, String newObject) {
        if (oldObject != null && newObject != null
                && this.departments.contains(oldObject) && !this.departments.contains(newObject)) {
            int index = this.departments.indexOf(oldObject);
            this.departments.set(index, newObject);
            new BackgroundAction().set(oldObject, newObject);
        }
    }

    @Override
    public void remove(String object) {
        if (object != null && this.departments.remove(object)) {
            new BackgroundAction().remove(object);
        }
    }

    @Override
    public void clear() {
        this.departments.clear();
        new BackgroundAction().clear();
    }

    @Override
    public void rewrite(ArrayList<String> newList) {
        if (newList != null && !newList.isEmpty()) {
            this.departments.clear();
            this.departments.addAll(newList);
            new BackgroundAction().rewrite(newList);
        }
    }

    @Override
    public void rewriteInCurrentThread(ArrayList<String>newList){
        if (newList != null && !newList.isEmpty()) {
            this.departments.clear();
            this.departments.addAll(newList);
            new BackgroundAction().rewriteDepartments(newList);
        }
    }

    @Override
    public void export() {
        new BackgroundAction().export(this.departments);
    }

    private class BackgroundAction extends SwingWorker<Boolean, Void>{
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
        protected Boolean doInBackground() throws Exception {
            switch (this.action){
                case ADD:
                    return this.addDepartment(this.object);
                case REMOVE:
                    return this.removeDepartment(this.object);
                case CLEAR:
                    return this.clearDepartments();
                case REWRITE:
                    return this.rewriteDepartments(this.list);
                case SET:
                    return this.setDepartment(this.old, this.object);
                case EXPORT:
                    return this.exportDepartments(this.list);
            }
            return true;
        }

        @Override
        protected void done() {
            try {
                if (!this.get()){
                    switch (this.action){
                        case ADD:
                            departments.remove(this.object);
                            break;
                        case REMOVE:
                            if (!departments.contains(this.object)) departments.add(this.object);
                            break;
                        case SET:
                            departments.remove(this.object);
                            if (!departments.contains(this.old)) departments.add(this.old);
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

        private boolean addDepartment(String department){
            LOGGER.fine("Get connection with DB");
            try (Connection connection = getConnection()){
                LOGGER.fine("Send requests to add");
                String sql = "INSERT INTO department ('department') "
                        + "VALUES(?);";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, department);
                statement.execute();

                LOGGER.fine("Close connection");
                statement.close();
                return true;
            }catch (SQLException ex){
                LOGGER.log(Level.SEVERE, "ERROR: ", ex);
                return false;
            }
        }

        void addDepartments(ArrayList<String>departments){
            LOGGER.fine("Get connection with DB");
            try (Connection connection = getConnection()){
                Statement statement = connection.createStatement();
                LOGGER.fine("Send request to add");
                for (String department : departments){
                    String sql = "INSERT INTO departments(department)"
                            + "SELECT " + department + " "
                            + "WHERE NOT EXISTS(SELECT 1 FROM departments WHERE department = " + department + ");";
                    statement.execute(sql);
                }

                LOGGER.fine("Close connection");
                statement.close();
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, "Error: ", ex);
            }
        }

        private boolean removeDepartment(String department){
            LOGGER.fine("Get connection with DB");
            try (Connection connection = getConnection()){
                LOGGER.fine("Send request to delete");
                String sql = "DELETE FROM departments WHERE department = '" + department + "';";
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

        private boolean clearDepartments(){
            LOGGER.fine("Get connection with DB");
            try (Connection connection = getConnection()) {
                LOGGER.fine("Send request");
                String sql = "DELETE FROM departments;";
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

        private boolean setDepartment(String oldDepartment, String newDepartment){
            LOGGER.fine("Get connection with DB");
            try (Connection connection = getConnection()){
                LOGGER.fine("Send request");
                Statement statement = connection.createStatement();
                String sql = "UPDATE departments SET department = '" + newDepartment + "' WHERE department = '" + oldDepartment + "';";
                statement.execute(sql);

                LOGGER.fine("Close connections");
                statement.close();
                return true;
            }catch (SQLException ex){
                LOGGER.log(Level.SEVERE, "ERROR: ", ex);
                return false;
            }
        }

        public boolean rewriteDepartments(ArrayList<String>departments){
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
                return true;
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, "ERROR: ", ex);
                return false;
            }
        }

        private boolean exportDepartments(ArrayList<String>departments){
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