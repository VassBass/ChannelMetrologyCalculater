package repository.impl;

import application.Application;
import application.ApplicationContext;
import com.fasterxml.jackson.core.JsonProcessingException;
import constants.Action;
import model.Calibrator;
import model.Measurement;
import repository.CalibratorRepository;
import repository.Repository;
import ui.model.SaveMessage;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CalibratorRepositoryImpl extends Repository<Calibrator> implements CalibratorRepository {
    private static final Logger LOGGER = Logger.getLogger(CalibratorRepository.class.getName());

    private boolean backgroundTaskRunning = false;

    public CalibratorRepositoryImpl(){super();}
    public CalibratorRepositoryImpl(String dbUrl){super(dbUrl);}

    @Override
    protected void init() {
        LOGGER.fine("Get connection with DB");
        try (Connection connection = this.getConnection();
            Statement statement = connection.createStatement()){
            String sql = "CREATE TABLE IF NOT EXISTS calibrators ("
                    + "name text NOT NULL UNIQUE"
                    + ", type text NOT NULL"
                    + ", number text NOT NULL"
                    + ", measurement text NOT NULL"
                    + ", value text NOT NULL"
                    + ", error_formula text NOT NULL"
                    + ", certificate text NOT NULL"
                    + ", range_min real NOT NULL"
                    + ", range_max real NOT NULL"
                    + ", PRIMARY KEY (\"name\")"
                    + ");";
            LOGGER.fine("Send request to create table");
            statement.execute(sql);

            LOGGER.fine("Send request to read calibrators from DB");
            sql = "SELECT * FROM calibrators";
            try (ResultSet resultSet = statement.executeQuery(sql)) {
                while (resultSet.next()) {
                    Calibrator calibrator = new Calibrator();
                    calibrator.setName(resultSet.getString("name"));
                    calibrator.setType(resultSet.getString("type"));
                    calibrator.setNumber(resultSet.getString("number"));
                    calibrator.setMeasurement(resultSet.getString("measurement"));
                    calibrator.setValue(resultSet.getString("value"));
                    calibrator.setErrorFormula(resultSet.getString("error_formula"));
                    String certificateString = resultSet.getString("certificate");
                    calibrator.setCertificate(Calibrator.Certificate.fromString(certificateString));
                    calibrator.setRangeMin(resultSet.getDouble("range_min"));
                    calibrator.setRangeMax(resultSet.getDouble("range_max"));
                    this.mainList.add(calibrator);
                }
            }
        } catch (SQLException | JsonProcessingException ex) {
            LOGGER.log(Level.SEVERE, "Initialization ERROR", ex);
        }
        LOGGER.info("Initialization SUCCESS");
    }

    @Override
    public ArrayList<Calibrator> getAll() {
        return this.mainList;
    }

    @Override
    public String[] getAllNames(Measurement measurement) {
        if (measurement == null) return null;

        ArrayList<String>cal = new ArrayList<>();
        for (Calibrator c : this.mainList){
            if (c.getMeasurement().equals(measurement.getName())){
                cal.add(c.getName());
            }
        }
        return cal.toArray(new String[0]);
    }

    @Override
    public Calibrator get(String name) {
        if (name == null || name.length() == 0) return null;
        Calibrator calibrator = new Calibrator();
        calibrator.setName(name);
        int index = this.mainList.indexOf(calibrator);
        return index >= 0 ? this.mainList.get(index) : null;
    }

    @Override
    public Calibrator get(int index) {
        return index < 0 | index >= this.mainList.size() ? null : this.mainList.get(index);
    }

    @Override
    public void add(Calibrator calibrator) {
        if (calibrator != null && !this.mainList.contains(calibrator)) {
            this.mainList.add(calibrator);
            new BackgroundAction().add(calibrator);
        }
    }

    @Override
    public void addInCurrentThread(Calibrator calibrator) {
        if (calibrator != null && !this.mainList.contains(calibrator)) {
            this.mainList.add(calibrator);
            new BackgroundAction().addCalibrator(calibrator);
        }
    }

    @Override
    public void remove(Calibrator calibrator) {
        if (calibrator != null && this.mainList.remove(calibrator)){
            new BackgroundAction().remove(calibrator);
        }
    }

    @Override
    public void remove(int index) {
        if (index >= 0 && index < this.mainList.size()){
            new BackgroundAction().remove(this.mainList.get(index));
            this.mainList.remove(index);
        }
    }

    @Override
    public void removeByMeasurementInCurrentThread(String measurementValue) {
        if (measurementValue != null) {
            ArrayList<Integer> indexes = new ArrayList<>();
            for (int index = 0; index < this.mainList.size(); index++) {
                String value = this.mainList.get(index).getValue();
                if (value.equals(measurementValue)) indexes.add(index);
            }
            Collections.reverse(indexes);
            for (int index : indexes) {
                this.mainList.remove(index);
            }

            LOGGER.fine("Get connection with DB");
            try (Connection connection = getConnection();
                 Statement statement = connection.createStatement()) {
                String sql = "DELETE FROM calibrators WHERE value = '" + measurementValue + "';";
                LOGGER.fine("Send request to delete");
                statement.execute(sql);
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, "ERROR: ", ex);
            }
        }
    }

    @Override
    public void set(Calibrator oldCalibrator, Calibrator newCalibrator) {
        if (oldCalibrator != null && newCalibrator != null
                && this.mainList.contains(oldCalibrator)){
            int oldIndex = this.mainList.indexOf(oldCalibrator);
            int newIndex = this.mainList.indexOf(newCalibrator);
            if (newIndex == -1 || oldIndex == newIndex) {
                this.mainList.set(oldIndex, newCalibrator);
                new BackgroundAction().set(oldCalibrator, newCalibrator);
            }
        }
    }

    @Override
    public void setInCurrentThread(Calibrator oldCalibrator, Calibrator newCalibrator) {
        if (oldCalibrator != null && newCalibrator != null
                && this.mainList.contains(oldCalibrator) && !this.mainList.contains(newCalibrator)){
            int index = this.mainList.indexOf(oldCalibrator);
            this.mainList.set(index, newCalibrator);
            new BackgroundAction().setCalibrator(oldCalibrator, newCalibrator);
        }
    }

    @Override
    public void changeMeasurementValueInCurrentThread(String oldValue, String newValue) {
        if (oldValue != null && newValue != null){
            for (Calibrator calibrator : this.mainList){
                if (calibrator.getValue().equals(oldValue)){
                    calibrator.setValue(newValue);
                }
            }

            LOGGER.fine("Get connection with DB");
            try (Connection connection = getConnection();
                 Statement statement = connection.createStatement()) {
                String sql = "UPDATE calibrators SET value = '" + newValue + "' WHERE value = '" + oldValue + "';";
                LOGGER.fine("Send request to update");
                statement.execute(sql);
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, "ERROR: ", ex);
            }
        }
    }

    @Override
    public void clear() {
        this.mainList.clear();
        new BackgroundAction().clear();
    }

    @Override
    public void rewriteInCurrentThread(ArrayList<Calibrator> calibrators) {
        if (calibrators != null && !calibrators.isEmpty()) {
            this.mainList.clear();
            this.mainList.addAll(calibrators);
            new BackgroundAction().rewriteCalibrators(calibrators);
        }
    }



    @Override
    public void rewrite(ArrayList<Calibrator> calibrators) {
        if (calibrators != null && !calibrators.isEmpty()) {
            this.mainList.clear();
            this.mainList.addAll(calibrators);
            new BackgroundAction().rewrite(calibrators);
        }
    }

    @Override
    public void importDataInCurrentThread(ArrayList<Calibrator> newCalibrators, ArrayList<Calibrator> calibratorsForChange) {
        if (calibratorsForChange != null && !calibratorsForChange.isEmpty()) {
            for (Calibrator calibrator : calibratorsForChange) {
                int index = this.mainList.indexOf(calibrator);
                if (index >= 0) this.mainList.set(index, calibrator);
            }
        }
        if (newCalibrators != null && !newCalibrators.isEmpty()) this.mainList.addAll(newCalibrators);
        new BackgroundAction().rewriteCalibrators(this.mainList);
    }

    @Override
    public boolean isExists(Calibrator calibrator) {
        if (calibrator == null){
            return true;
        }else{
            return this.mainList.contains(calibrator);
        }
    }

    @Override
    public boolean backgroundTaskIsRun() {
        return this.backgroundTaskRunning;
    }

    private class BackgroundAction extends SwingWorker<Boolean, Void> {
        private Calibrator calibrator, old;
        private ArrayList<Calibrator>list;
        private Action action;
        private final SaveMessage saveMessage;

        public BackgroundAction(){
            ApplicationContext context = Application.context;
            Window mainScreen = context == null ? null : Application.context.mainScreen;
            this.saveMessage = mainScreen == null ? null : new SaveMessage(mainScreen);
        }

        void add(Calibrator calibrator){
            this.calibrator = calibrator;
            this.action = Action.ADD;
            this.start();
        }

        void remove(Calibrator calibrator){
            this.calibrator = calibrator;
            this.action = Action.REMOVE;
            this.start();
        }

        void clear(){
            this.action = Action.CLEAR;
            this.start();
        }

        void rewrite(ArrayList<Calibrator>list){
            this.list = list;
            this.action = list == null ? Action.CLEAR : Action.REWRITE;
            this.start();
        }

        void set(Calibrator oldCalibrator, Calibrator newCalibrator){
            this.old = oldCalibrator;
            this.calibrator = newCalibrator;
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
                    return this.addCalibrator(this.calibrator);
                case REMOVE:
                    return this.removeCalibrator(this.calibrator);
                case CLEAR:
                    return this.clearCalibrators();
                case SET:
                    return this.setCalibrator(this.old, this.calibrator);
                case REWRITE:
                    return this.rewriteCalibrators(this.list);
            }
            return true;
        }

        @Override
        protected void done() {
            try {
                if (!this.get()){
                    switch (this.action){
                        case ADD:
                            mainList.remove(this.calibrator);
                            break;
                        case REMOVE:
                            if (!mainList.contains(this.calibrator)) mainList.add(this.calibrator);
                            break;
                        case SET:
                            mainList.remove(this.calibrator);
                            if (!mainList.contains(this.old)) mainList.add(this.old);
                            break;
                    }
                    String message = "?????????????? ??????????????! ?????????? ???? ????????????????????! ?????????????????? ????????-?????????? ???? ??????!";
                    JOptionPane.showMessageDialog(Application.context.mainScreen, message, "??????????????!", JOptionPane.ERROR_MESSAGE);
                }
            } catch (ExecutionException | InterruptedException e) {
                LOGGER.log(Level.SEVERE, "ERROR: ", e);
            }
            Application.setBusy(false);
            backgroundTaskRunning = false;
            if (this.saveMessage != null) this.saveMessage.dispose();
        }

        boolean addCalibrator(Calibrator calibrator){
            String sql = "INSERT INTO calibrators ('name', 'type', 'number', 'measurement', 'value', 'error_formula', 'certificate', 'range_min', 'range_max') "
                    + "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?);";
            LOGGER.fine("Get connection with DB");
            try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)){

                LOGGER.fine("Send requests to add");
                statement.setString(1, calibrator.getName());
                statement.setString(2, calibrator.getType());
                statement.setString(3, calibrator.getNumber());
                statement.setString(4, calibrator.getMeasurement());
                statement.setString(5, calibrator.getValue());
                statement.setString(6, calibrator.getErrorFormula());
                statement.setString(7, calibrator.getCertificate().toString());
                statement.setDouble(8, calibrator.getRangeMin());
                statement.setDouble(9, calibrator.getRangeMax());
                statement.execute();
            }catch (SQLException ex){
                LOGGER.log(Level.SEVERE, "ERROR: ", ex);
                return false;
            }
            return true;
        }

        private boolean removeCalibrator(Calibrator calibrator){
            LOGGER.fine("Get connection with DB");
            try (Connection connection = getConnection();
                Statement statement = connection.createStatement()){
                LOGGER.fine("Send request to delete");
                String sql = "DELETE FROM calibrators WHERE name = '" + calibrator.getName() + "';";
                statement.execute(sql);
            }catch (SQLException ex){
                LOGGER.log(Level.SEVERE, "ERROR: ", ex);
                return false;
            }
            return true;
        }

        private boolean clearCalibrators(){
            LOGGER.fine("Get connection with DB");
            try (Connection connection = getConnection();
                Statement statement = connection.createStatement()) {
                LOGGER.fine("Send request");
                String sql = "DELETE FROM calibrators;";
                statement.execute(sql);
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, "ERROR: ", ex);
                return false;
            }
            return true;
        }

        boolean setCalibrator(Calibrator oldCalibrator, Calibrator newCalibrator){
            LOGGER.fine("Get connection with DB");
            try (Connection connection = getConnection();
                Statement statement = connection.createStatement()){
                LOGGER.fine("Send requests to update");
                String sql = "UPDATE calibrators SET "
                        + "name = '" + newCalibrator.getName() + "', "
                        + "type = '" + newCalibrator.getType() + "', "
                        + "number = '" + newCalibrator.getNumber() + "', "
                        + "measurement = '" + newCalibrator.getMeasurement() + "', "
                        + "value = '" + newCalibrator.getValue() + "', "
                        + "error_formula = '" + newCalibrator.getErrorFormula() + "', "
                        + "certificate = '" + newCalibrator.getCertificate() + "', "
                        + "range_min = " + newCalibrator.getRangeMin() + ", "
                        + "range_max = " + newCalibrator.getRangeMax() + " "
                        + "WHERE name = '" + oldCalibrator.getName() + "';";
                statement.execute(sql);
            }catch (SQLException ex){
                LOGGER.log(Level.SEVERE, "ERROR: ", ex);
                return false;
            }
            return true;
        }

        public boolean rewriteCalibrators(ArrayList<Calibrator>calibrators){
            String clearSql = "DELETE FROM calibrators;";
            String insertSql = "INSERT INTO calibrators ('name', 'type', 'number', 'measurement', 'value', 'error_formula', 'certificate', 'range_min', 'range_max') "
                    + "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?);";
            LOGGER.fine("Get connection with DB");
            try (Connection connection = getConnection();
                Statement statementClear = connection.createStatement();
                PreparedStatement statement = connection.prepareStatement(insertSql)) {
                LOGGER.fine("Send request to clear");
                statementClear.execute(clearSql);

                if (!calibrators.isEmpty()) {
                    LOGGER.fine("Send requests to add");
                    for (Calibrator calibrator : calibrators) {
                        statement.setString(1, calibrator.getName());
                        statement.setString(2, calibrator.getType());
                        statement.setString(3, calibrator.getNumber());
                        statement.setString(4, calibrator.getMeasurement());
                        statement.setString(5, calibrator.getValue());
                        statement.setString(6, calibrator.getErrorFormula());
                        statement.setString(7, calibrator.getCertificate().toString());
                        statement.setDouble(8, calibrator.getRangeMin());
                        statement.setDouble(9, calibrator.getRangeMax());
                        statement.execute();
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