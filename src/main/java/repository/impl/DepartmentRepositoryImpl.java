package repository.impl;

import application.Application;
import application.ApplicationContext;
import constants.Action;
import repository.DepartmentRepository;
import repository.Repository;
import ui.model.SaveMessage;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DepartmentRepositoryImpl extends Repository<String> implements DepartmentRepository {
    private static final Logger LOGGER = Logger.getLogger(DepartmentRepository.class.getName());

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
                this.mainList.add(resultSet.getString("department"));
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
    public void addInCurrentThread(ArrayList<String> departments) {
        if (departments != null && !departments.isEmpty()) {
            for (String department : departments) {
                if (!this.mainList.contains(department)){
                    this.mainList.add(department);
                }
            }
            new BackgroundAction().addDepartments(departments);
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
            new BackgroundAction().rewriteDepartments(newList);
        }
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
                            + "SELECT '" + department + "' "
                            + "WHERE NOT EXISTS(SELECT 1 FROM departments WHERE department = '" + department + "');";
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
    }
}