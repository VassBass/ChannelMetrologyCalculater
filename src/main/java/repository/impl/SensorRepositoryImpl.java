package repository.impl;

import application.Application;
import application.ApplicationContext;
import constants.Action;
import constants.SensorType;
import model.Sensor;
import org.sqlite.JDBC;
import repository.Repository;
import repository.SensorRepository;
import ui.model.SaveMessage;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SensorRepositoryImpl extends Repository implements SensorRepository {
    private static final Logger LOGGER = Logger.getLogger(SensorRepository.class.getName());

    private final ArrayList<Sensor>sensors = new ArrayList<>();

    public SensorRepositoryImpl(){super();}
    public SensorRepositoryImpl(String dbUrl){super(dbUrl);}

    @Override
    protected void init() {
        LOGGER.fine("Get connection with DB");
        try (Connection connection = this.getConnection()){
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
            Statement statement = connection.createStatement();

            LOGGER.fine("Send request to create table");
            statement.execute(sql);

            LOGGER.fine("Send request to read sensors from DB");
            sql = "SELECT * FROM sensors";
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()){
                Sensor sensor = new Sensor();
                sensor.setName(resultSet.getString("name"));
                sensor.setType(resultSet.getString("type"));
                sensor.setNumber(resultSet.getString("number"));
                sensor.setMeasurement(resultSet.getString("measurement"));
                sensor.setValue(resultSet.getString("value"));
                sensor.setErrorFormula(resultSet.getString("error_formula"));
                sensor.setRangeMin(resultSet.getDouble("range_min"));
                sensor.setRangeMax(resultSet.getDouble("range_max"));
                this.sensors.add(sensor);
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
    public ArrayList<Sensor> getAll() {
        return this.sensors;
    }

    @Override
    public String[] getAllTypes() {
        ArrayList<String>types = new ArrayList<>();
        for (Sensor sensor : this.sensors){
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
        for (Sensor sensor : this.sensors){
            String type = sensor.getType();
            if (!type.contains(SensorType.ROSEMOUNT)) {
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
            for (Sensor sensor : this.sensors) {
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
        for (Sensor sensor : this.sensors) {
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
            int index = this.sensors.indexOf(sensor);
            return index < 0 ? null : this.sensors.get(index);
        }else return null;
    }

    @Override
    public Sensor get(int index) {
        return index < 0 | index >= this.sensors.size() ? null : this.sensors.get(index);
    }

    @Override
    public void add(Sensor sensor) {
        if (sensor != null && !this.sensors.contains(sensor)) {
            this.sensors.add(sensor);
            new BackgroundAction().add(sensor);
        }
    }

    @Override
    public void addInCurrentThread(Sensor sensor) {
        if (sensor != null && !this.sensors.contains(sensor)) {
            this.sensors.add(sensor);
            new BackgroundAction().addSensor(sensor);
        }
    }

    @Override
    public void removeInCurrentThread(Sensor sensor) {
        if (sensor != null && this.sensors.remove(sensor)) {
            new BackgroundAction().removeSensor(sensor);
        }
    }

    @Override
    public void setInCurrentThread(Sensor oldSensor, Sensor newSensor) {
        if (oldSensor != null && newSensor != null && this.sensors.contains(oldSensor) && !this.sensors.contains(newSensor)){
            int index = this.sensors.indexOf(oldSensor);
            this.sensors.set(index, newSensor);
            new BackgroundAction().setSensor(oldSensor, newSensor);
        }
    }

    @Override
    public void rewriteInCurrentThread(ArrayList<Sensor> sensors) {
        if (sensors != null && !sensors.isEmpty()) {
            this.sensors.clear();
            this.sensors.addAll(sensors);
            new BackgroundAction().rewriteSensors(sensors);
        }
    }

    @Override
    public void clear() {
        this.sensors.clear();
        new BackgroundAction().clear();
    }

    @Override
    public void export() {
        new BackgroundAction().export(this.sensors);
    }

    @Override
    public void importData(ArrayList<Sensor> newSensors, ArrayList<Sensor> sensorsForChange) {
        for (Sensor sensor : sensorsForChange){
            int index = this.sensors.indexOf(sensor);
            if (index >= 0) this.sensors.set(index, sensor);
        }
        this.sensors.addAll(newSensors);
        new BackgroundAction().rewriteSensors(this.sensors);
    }

    @Override
    public void rewrite(ArrayList<Sensor> sensors) {
        this.sensors.clear();
        this.sensors.addAll(sensors);
        new BackgroundAction().rewrite(sensors);
    }

    @Override
    public boolean isLastInMeasurement(Sensor sensor) {
        if (sensor != null) {
            String measurement = sensor.getMeasurement();
            int numberOfSensors = 0;
            for (Sensor s : this.sensors) {
                if (s.getMeasurement().equals(measurement)) {
                    numberOfSensors++;
                }
            }
            return numberOfSensors <= 1;
        }else return false;
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

        void export(ArrayList<Sensor>sensors){
            this.list = sensors;
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
                    return this.addSensor(this.sensor);
                case CLEAR:
                    return this.clearSensors();
                case REWRITE:
                    return this.rewriteSensors(this.list);
                case EXPORT:
                    return this.exportSensors(this.list);
            }
            return true;
        }

        @Override
        protected void done() {
            try {
                if (!this.get()){
                    if (this.action == Action.ADD) {
                        sensors.remove(this.sensor);
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

        boolean addSensor(Sensor sensor){
            LOGGER.fine("Get connection with DB");
            try (Connection connection = getConnection()){
                LOGGER.fine("Send request to delete");
                String sql = "DELETE FROM sensors WHERE name = '?';";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, sensor.getName());
                statement.execute();

                LOGGER.fine("Send requests to add");
                sql = "INSERT INTO sensors ('name', 'type', 'number', 'measurement', 'value', 'error_formula', 'range_min', 'range_max') "
                        + "VALUES(?, ?, ?, ?, ?, ?, ?, ?);";
                statement = connection.prepareStatement(sql);
                statement.setString(1, sensor.getName());
                statement.setString(2, sensor.getType());
                statement.setString(3, sensor.getNumber());
                statement.setString(4, sensor.getMeasurement());
                statement.setString(5, sensor.getValue());
                statement.setString(6, sensor.getErrorFormula());
                statement.setDouble(7, sensor.getRangeMin());
                statement.setDouble(8, sensor.getRangeMax());
                statement.execute();

                LOGGER.fine("Close connection");
                statement.close();
                return true;
            }catch (SQLException ex){
                LOGGER.log(Level.SEVERE, "ERROR: ", ex);
                return false;
            }
        }

        void removeSensor(Sensor sensor){
            LOGGER.fine("Get connection with DB");
            try (Connection connection = getConnection()){
                LOGGER.fine("Send request to delete");
                String sql = "DELETE FROM sensors WHERE name = '" + sensor.getName() + "';";
                Statement statement = connection.createStatement();
                statement.execute(sql);

                LOGGER.fine("Close connection");
                statement.close();
            }catch (SQLException ex){
                LOGGER.log(Level.SEVERE, "ERROR: ", ex);
            }
        }

        private boolean clearSensors(){
            LOGGER.fine("Get connection with DB");
            try (Connection connection = getConnection()) {
                LOGGER.fine("Send request");
                String sql = "DELETE FROM sensors;";
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

        void setSensor(Sensor oldSensor, Sensor newSensor){
            LOGGER.fine("Get connection with DB");
            try (Connection connection = getConnection()){
                LOGGER.fine("Send request to delete");
                Statement statementClear = connection.createStatement();
                String sql = "DELETE FROM sensors WHERE name = '" + oldSensor.getName() + "';";
                statementClear.execute(sql);

                LOGGER.fine("Send requests to add");
                sql = "INSERT INTO sensors ('name', 'type', 'number', 'measurement', 'value', 'error_formula', 'range_min', 'range_max') "
                        + "VALUES(?, ?, ?, ?, ?, ?, ?, ?);";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, newSensor.getName());
                statement.setString(2, newSensor.getType());
                statement.setString(3, newSensor.getNumber());
                statement.setString(4, newSensor.getMeasurement());
                statement.setString(5, newSensor.getValue());
                statement.setString(6, newSensor.getErrorFormula());
                statement.setDouble(7, newSensor.getRangeMin());
                statement.setDouble(8, newSensor.getRangeMax());
                statement.execute(sql);

                LOGGER.fine("Close connections");
                statementClear.close();
                statement.close();
            }catch (SQLException ex){
                LOGGER.log(Level.SEVERE, "ERROR: ", ex);
            }
        }

        boolean rewriteSensors(ArrayList<Sensor>sensors){
            LOGGER.fine("Get connection with DB");
            try (Connection connection = getConnection()) {
                LOGGER.fine("Send request to clear");
                Statement statementClear = connection.createStatement();
                String sql = "DELETE FROM sensors;";
                statementClear.execute(sql);

                if (!sensors.isEmpty()) {
                    LOGGER.fine("Send requests to add");
                    sql = "INSERT INTO sensors ('name', 'type', 'number', 'measurement', 'value', 'error_formula', 'range_min', 'range_max') "
                            + "VALUES(?, ?, ?, ?, ?, ?, ?, ?);";
                    PreparedStatement statement = connection.prepareStatement(sql);
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

        private boolean exportSensors(ArrayList<Sensor>sensors){
            Calendar date = Calendar.getInstance();
            String fileName = "export_sensors ["
                    + date.get(Calendar.DAY_OF_MONTH)
                    + "."
                    + (date.get(Calendar.MONTH) + 1)
                    + "."
                    + date.get(Calendar.YEAR)
                    + "].db";
            String dbUrl = "jdbc:sqlite:Support/Export/" + fileName;
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
                sql = "INSERT INTO sensors ('name', 'type', 'number', 'measurement', 'value', 'error_formula', 'range_min', 'range_max') "
                        + "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?);";
                preparedStatement = connection.prepareStatement(sql);
                for (Sensor sensor : sensors) {
                    preparedStatement.setString(1, sensor.getName());
                    preparedStatement.setString(2, sensor.getType());
                    preparedStatement.setString(3, sensor.getNumber());
                    preparedStatement.setString(4, sensor.getMeasurement());
                    preparedStatement.setString(5, sensor.getValue());
                    preparedStatement.setString(6, sensor.getErrorFormula());
                    preparedStatement.setDouble(7, sensor.getRangeMin());
                    preparedStatement.setDouble(8, sensor.getRangeMax());
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