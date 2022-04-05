package repository.impl;

import application.Application;
import application.ApplicationContext;
import constants.Action;
import model.Sensor;
import repository.Repository;
import repository.SensorRepository;
import ui.model.SaveMessage;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SensorRepositoryImpl extends Repository<Sensor> implements SensorRepository {
    private static final Logger LOGGER = Logger.getLogger(SensorRepository.class.getName());

    private boolean backgroundTaskRunning = false;

    public SensorRepositoryImpl(){super();}
    public SensorRepositoryImpl(String dbUrl){super(dbUrl);}

    @Override
    protected void init() {
        LOGGER.fine("Get connection with DB");
        try (Connection connection = this.getConnection();
            Statement statement = connection.createStatement()){
            String sql = "CREATE TABLE IF NOT EXISTS sensors ("
                    + "name text NOT NULL UNIQUE"
                    + ", type text NOT NULL"
                    + ", number text"
                    + ", measurement text NOT NULL"
                    + ", value text"
                    + ", error_formula text NOT NULL"
                    + ", range_min real NOT NULL"
                    + ", range_max real NOT NULL"
                    + ", PRIMARY KEY (\"name\")"
                    + ");";
            LOGGER.fine("Send request to create table");
            statement.execute(sql);

            LOGGER.fine("Send request to read sensors from DB");
            sql = "SELECT * FROM sensors";
            try (ResultSet resultSet = statement.executeQuery(sql)) {
                while (resultSet.next()) {
                    Sensor sensor = new Sensor();
                    sensor.setName(resultSet.getString("name"));
                    sensor.setType(resultSet.getString("type"));
                    sensor.setNumber(resultSet.getString("number"));
                    sensor.setMeasurement(resultSet.getString("measurement"));
                    sensor.setValue(resultSet.getString("value"));
                    sensor.setErrorFormula(resultSet.getString("error_formula"));
                    sensor.setRangeMin(resultSet.getDouble("range_min"));
                    sensor.setRangeMax(resultSet.getDouble("range_max"));
                    this.mainList.add(sensor);
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Initialization ERROR", ex);
        }
        LOGGER.info("Initialization SUCCESS");
    }

    @Override
    public ArrayList<Sensor> getAll() {
        return this.mainList;
    }

    @Override
    public String[] getAllTypes() {
        ArrayList<String>types = new ArrayList<>();
        for (Sensor sensor : this.mainList){
            String type = sensor.getType();
            boolean exist = false;
            for (String t : types){
                if (t.equals(type)){
                    exist = true;
                    break;
                }
            }
            if (!exist){
                types.add(type);
            }
        }
        return types.toArray(new String[0]);
    }

    @Override
    public String[] getAllTypesWithoutROSEMOUNT() {
        ArrayList<String>types = new ArrayList<>();
        for (Sensor sensor : this.mainList){
            String type = sensor.getType();
            if (!type.contains(Sensor.ROSEMOUNT)) {
                boolean exist = false;
                for (String t : types) {
                    if (t.equals(type)) {
                        exist = true;
                        break;
                    }
                }
                if (!exist) {
                    types.add(type);
                }
            }
        }
        return types.toArray(new String[0]);
    }

    @Override
    public String getMeasurement(String sensorType) {
        if (sensorType != null && sensorType.length() > 0) {
            for (Sensor sensor : this.mainList) {
                if (sensor.getType().equals(sensorType)) {
                    return sensor.getMeasurement();
                }
            }
        }
        return null;
    }

    @Override
    public String[] getAllSensorsName(String measurementName) {
        ArrayList<String> names = new ArrayList<>();
        for (Sensor sensor : this.mainList) {
            if (sensor.getMeasurement().equals(measurementName)) {
                names.add(sensor.getName());
            }
        }
        return names.toArray(new String[0]);
    }

    @Override
    public Sensor get(String sensorName) {
        if (sensorName != null && sensorName.length() > 0) {
            Sensor sensor = new Sensor();
            sensor.setName(sensorName);
            int index = this.mainList.indexOf(sensor);
            return index < 0 ? null : this.mainList.get(index);
        }else return null;
    }

    @Override
    public Sensor get(int index) {
        return index < 0 | index >= this.mainList.size() ? null : this.mainList.get(index);
    }

    @Override
    public void add(Sensor sensor) {
        if (sensor != null && !this.mainList.contains(sensor)) {
            this.mainList.add(sensor);
            new BackgroundAction().add(sensor);
        }
    }

    @Override
    public void addInCurrentThread(Sensor sensor) {
        if (sensor != null && !this.mainList.contains(sensor)) {
            this.mainList.add(sensor);
            new BackgroundAction().addSensor(sensor);
        }
    }

    @Override
    public void removeInCurrentThread(Sensor sensor) {
        if (sensor != null && this.mainList.remove(sensor)) {
            new BackgroundAction().removeSensor(sensor);
        }
    }

    @Override
    public void setInCurrentThread(Sensor oldSensor, Sensor newSensor) {
        if (oldSensor != null && newSensor != null
                && this.mainList.contains(oldSensor)){
            int oldIndex = this.mainList.indexOf(oldSensor);
            int newIndex = this.mainList.indexOf(newSensor);
            if (newIndex == -1 || oldIndex == newIndex) {
                this.mainList.set(oldIndex, newSensor);
                new BackgroundAction().setSensor(oldSensor, newSensor);
            }
        }
    }

    @Override
    public void rewriteInCurrentThread(ArrayList<Sensor> sensors) {
        if (sensors != null && !sensors.isEmpty()) {
            this.mainList.clear();
            this.mainList.addAll(sensors);
            new BackgroundAction().rewriteSensors(sensors);
        }
    }

    @Override
    public void clear() {
        this.mainList.clear();
        new BackgroundAction().clear();
    }

    @Override
    public void importDataInCurrentThread(ArrayList<Sensor> newSensors, ArrayList<Sensor> sensorsForChange) {
        if (sensorsForChange != null) {
            for (Sensor sensor : sensorsForChange) {
                int index = this.mainList.indexOf(sensor);
                if (index >= 0) this.mainList.set(index, sensor);
            }
        }
        if (newSensors != null) this.mainList.addAll(newSensors);
        new BackgroundAction().rewriteSensors(this.mainList);
    }

    @Override
    public void rewrite(ArrayList<Sensor> sensors) {
        this.mainList.clear();
        this.mainList.addAll(sensors);
        new BackgroundAction().rewrite(sensors);
    }

    @Override
    public boolean isLastInMeasurement(Sensor sensor) {
        if (sensor != null) {
            String measurement = sensor.getMeasurement();
            int numberOfSensors = 0;
            for (Sensor s : this.mainList) {
                if (s.getMeasurement().equals(measurement)) {
                    numberOfSensors++;
                }
            }
            return numberOfSensors == 1;
        }else return false;
    }

    @Override
    public boolean backgroundTaskIsRun() {
        return this.backgroundTaskRunning;
    }

    private class BackgroundAction extends SwingWorker<Boolean, Void> {
        private Sensor sensor;
        private ArrayList<Sensor>list;
        private Action action;
        private final SaveMessage saveMessage;

        public BackgroundAction(){
            ApplicationContext context = Application.context;
            Window mainScreen = context == null ? null : Application.context.mainScreen;
            this.saveMessage = mainScreen == null ? null : new SaveMessage(mainScreen);
        }

        void add(Sensor sensor){
            this.sensor = sensor;
            this.action = Action.ADD;
            this.start();
        }

        void clear(){
            this.action = Action.CLEAR;
            this.start();
        }

        void rewrite(ArrayList<Sensor>list){
            this.list = list;
            this.action = list == null ? constants.Action.CLEAR : constants.Action.REWRITE;
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
                    return this.addSensor(this.sensor);
                case CLEAR:
                    return this.clearSensors();
                case REWRITE:
                    return this.rewriteSensors(this.list);
            }
            return true;
        }

        @Override
        protected void done() {
            try {
                if (!this.get()){
                    if (this.action == Action.ADD) {
                        mainList.remove(this.sensor);
                    }
                    String message = "Виникла помилка! Данні не збереглися! Спробуйте будь-ласка ще раз!";
                    JOptionPane.showMessageDialog(Application.context.mainScreen, message, "Помилка!", JOptionPane.ERROR_MESSAGE);
                }
            } catch (ExecutionException | InterruptedException e) {
                LOGGER.log(Level.SEVERE, "ERROR: ", e);
            }
            Application.setBusy(false);
            backgroundTaskRunning = false;
            if (this.saveMessage != null) this.saveMessage.dispose();
        }

        boolean addSensor(Sensor sensor){
            String sql = "INSERT INTO sensors ('name', 'type', 'number', 'measurement', 'value', 'error_formula', 'range_min', 'range_max') "
                    + "VALUES(?, ?, ?, ?, ?, ?, ?, ?);";
            LOGGER.fine("Get connection with DB");
            try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)){
                LOGGER.fine("Send requests to add");
                statement.setString(1, sensor.getName());
                statement.setString(2, sensor.getType());
                statement.setString(3, sensor.getNumber());
                statement.setString(4, sensor.getMeasurement());
                statement.setString(5, sensor.getValue());
                statement.setString(6, sensor.getErrorFormula());
                statement.setDouble(7, sensor.getRangeMin());
                statement.setDouble(8, sensor.getRangeMax());
                statement.execute();
            }catch (SQLException ex){
                LOGGER.log(Level.SEVERE, "ERROR: ", ex);
                return false;
            }
            return true;
        }

        void removeSensor(Sensor sensor){
            LOGGER.fine("Get connection with DB");
            try (Connection connection = getConnection();
                Statement statement = connection.createStatement()){
                LOGGER.fine("Send request to delete");
                String sql = "DELETE FROM sensors WHERE name = '" + sensor.getName() + "';";
                statement.execute(sql);
            }catch (SQLException ex){
                LOGGER.log(Level.SEVERE, "ERROR: ", ex);
            }
        }

        private boolean clearSensors(){
            LOGGER.fine("Get connection with DB");
            try (Connection connection = getConnection();
                Statement statement = connection.createStatement()) {
                LOGGER.fine("Send request");
                String sql = "DELETE FROM sensors;";
                statement.execute(sql);
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, "ERROR: ", ex);
                return false;
            }
            return true;
        }

        void setSensor(Sensor oldSensor, Sensor newSensor){
            LOGGER.fine("Get connection with DB");
            try (Connection connection = getConnection();
                Statement statement = connection.createStatement()){
                LOGGER.fine("Send requests to update");
                String sql = "UPDATE sensors SET "
                        + "name = '" + newSensor.getName() + "', "
                        + "type = '" + newSensor.getType() + "', "
                        + "number = '" + newSensor.getNumber() + "', "
                        + "measurement = '" + newSensor.getMeasurement() + "', "
                        + "value = '" + newSensor.getValue() + "', "
                        + "error_formula = '" + newSensor.getErrorFormula() + "', "
                        + "range_min = " + newSensor.getRangeMin() + ", "
                        + "range_max = " + newSensor.getRangeMax() + " "
                        + "WHERE name = '" + oldSensor.getName() + "';";
                statement.execute(sql);
            }catch (SQLException ex){
                LOGGER.log(Level.SEVERE, "ERROR: ", ex);
            }
        }

        boolean rewriteSensors(ArrayList<Sensor>sensors){
            String clearSql = "DELETE FROM sensors;";
            String insertSql = "INSERT INTO sensors ('name', 'type', 'number', 'measurement', 'value', 'error_formula', 'range_min', 'range_max') "
                    + "VALUES(?, ?, ?, ?, ?, ?, ?, ?);";
            LOGGER.fine("Get connection with DB");
            try (Connection connection = getConnection();
                Statement statementClear = connection.createStatement();
                PreparedStatement statement = connection.prepareStatement(insertSql)) {
                LOGGER.fine("Send request to clear");
                statementClear.execute(clearSql);

                if (!sensors.isEmpty()) {
                    LOGGER.fine("Send requests to add");
                    for (Sensor sensor : sensors) {
                        statement.setString(1, sensor.getName());
                        statement.setString(2, sensor.getType());
                        statement.setString(3, sensor.getNumber());
                        statement.setString(4, sensor.getMeasurement());
                        statement.setString(5, sensor.getValue());
                        statement.setString(6, sensor.getErrorFormula());
                        statement.setDouble(7, sensor.getRangeMin());
                        statement.setDouble(8, sensor.getRangeMax());
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