package repository.impl;

import application.Application;
import application.ApplicationContext;
import constants.Action;
import repository.InstallationRepository;
import repository.Repository;
import ui.model.SaveMessage;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InstallationRepositoryImpl extends Repository<String> implements InstallationRepository {
    private static final Logger LOGGER = Logger.getLogger(InstallationRepository.class.getName());

    private boolean backgroundTaskRunning = false;

    public InstallationRepositoryImpl(){super();}
    public InstallationRepositoryImpl(String dbUrl){super(dbUrl);}

    public void init(){
        LOGGER.fine("Get connection with DB");
        try (Connection connection = this.getConnection();
            Statement statement = connection.createStatement()){
            String sql = "CREATE TABLE IF NOT EXISTS installations ("
                    + "installation text NOT NULL UNIQUE"
                    + ", PRIMARY KEY (\"installation\")"
                    + ");";
            LOGGER.fine("Send request to create table");
            statement.execute(sql);

            LOGGER.fine("Send request to read installations from DB");
            sql = "SELECT * FROM installations";
            try (ResultSet resultSet = statement.executeQuery(sql)) {
                while (resultSet.next()) {
                    this.mainList.add(resultSet.getString("installation"));
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
    public void addInCurrentThread(ArrayList<String> installations) {
        if (installations != null && !installations.isEmpty()) {
            for (String installation : installations) {
                if (!this.mainList.contains(installation)){
                    this.mainList.add(installation);
                }
            }
            new BackgroundAction().addInstallations(installations);
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
            new BackgroundAction().rewriteInstallations(newList);
        }
    }

    @Override
    public boolean backgroundTaskIsRun() {
        return this.backgroundTaskRunning;
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
                    return this.addInstallation(this.object);
                case REMOVE:
                    return this.removeInstallation(this.object);
                case CLEAR:
                    return this.clearInstallations();
                case REWRITE:
                    return this.rewriteInstallations(this.list);
                case SET:
                    return this.setInstallation(this.old, this.object);
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
                    if (Application.context != null) JOptionPane.showMessageDialog(Application.context.mainScreen, message, "Помилка!", JOptionPane.ERROR_MESSAGE);
                }
            } catch (ExecutionException | InterruptedException e) {
                LOGGER.log(Level.SEVERE, "ERROR: ", e);
            }
            Application.setBusy(false);
            backgroundTaskRunning = false;
            if (this.saveMessage != null) this.saveMessage.dispose();
        }

        private boolean addInstallation(String installation){
            LOGGER.fine("Get connection with DB");
            try (Connection connection = getConnection();
                Statement statement = connection.createStatement()){
                LOGGER.fine("Send requests to add");
                String sql = "INSERT INTO installations ('installation') VALUES('" + installation + "');";
                statement.execute(sql);
            }catch (SQLException ex){
                LOGGER.log(Level.SEVERE, "ERROR: ", ex);
                return false;
            }
            return true;
        }

        void addInstallations(ArrayList<String>installations){
            LOGGER.fine("Get connection with DB");
            try (Connection connection = getConnection();
                Statement statement = connection.createStatement()){
                LOGGER.fine("Send request to add");
                for (String installation : installations){
                    String sql = "INSERT INTO installations(installation)"
                            + "SELECT '" + installation + "' "
                            + "WHERE NOT EXISTS(SELECT 1 FROM installations WHERE installation = '" + installation + "');";
                    statement.execute(sql);
                }
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, "Error: ", ex);
            }
        }

        private boolean removeInstallation(String installation){
            LOGGER.fine("Get connection with DB");
            try (Connection connection = getConnection();
                Statement statement = connection.createStatement()){
                LOGGER.fine("Send request to delete");
                String sql = "DELETE FROM installations WHERE installation = '" + installation + "';";
                statement.execute(sql);
            }catch (SQLException ex){
                LOGGER.log(Level.SEVERE, "ERROR: ", ex);
                return false;
            }
            return true;
        }

        private boolean clearInstallations(){
            LOGGER.fine("Get connection with DB");
            try (Connection connection = getConnection();
                Statement statement = connection.createStatement()) {
                LOGGER.fine("Send request");
                String sql = "DELETE FROM installations;";
                statement.execute(sql);
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, "ERROR: ", ex);
                return false;
            }
            return true;
        }

        private boolean setInstallation(String oldInstallation, String newInstallation){
            LOGGER.fine("Get connection with DB");
            try (Connection connection = getConnection();
                Statement statement = connection.createStatement()){
                LOGGER.fine("Send request");
                String sql = "UPDATE installations SET installation = '" + newInstallation + "' WHERE installation = '" + oldInstallation + "';";
                statement.execute(sql);
            }catch (SQLException ex){
                LOGGER.log(Level.SEVERE, "ERROR: ", ex);
                return false;
            }
            return true;
        }

        public boolean rewriteInstallations(ArrayList<String>installations){
            LOGGER.fine("Get connection with DB");
            try (Connection connection = getConnection();
                Statement statement = connection.createStatement()) {
                LOGGER.fine("Send request to clear");
                String sql = "DELETE FROM installations;";
                statement.execute(sql);

                if (!installations.isEmpty()) {
                    LOGGER.fine("Send requests to add");
                    for (String installation : installations) {
                        sql = "INSERT INTO installations ('installation') VALUES('" + installation + "');";
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