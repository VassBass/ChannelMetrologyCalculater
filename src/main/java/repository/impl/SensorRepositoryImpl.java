package repository.impl;

import application.Application;
import application.ApplicationContext;
import constants.Action;
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
import java.util.logging.Level;
import java.util.logging.Logger;

public class SensorRepositoryImpl extends Repository implements SensorRepository {
    private static final Logger LOGGER = Logger.getLogger(SensorRepository.class.getName());

    public SensorRepositoryImpl(){super();}
    public SensorRepositoryImpl(String dbUrl){super(dbUrl);}

    @Override
    protected void init() {
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
    public ArrayList<Sensor> getAll() {
        ArrayList<Sensor>sensors = new ArrayList<>();
        LOGGER.fine("Get connection with DB");
        try (Connection connection = this.getConnection()){
            Statement statement = connection.createStatement();

            LOGGER.fine("Send request");
            String sql = "SELECT * FROM sensors";
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
                sensors.add(sensor);
            }

            LOGGER.fine("Close connections");
            resultSet.close();
            statement.close();
        }catch (SQLException ex){
            LOGGER.log(Level.SEVERE, "ERROR: ", ex);
        }
        return sensors;
    }

    @Override
    public void add(Sensor sensor) {
        new BackgroundAction().add(sensor);
    }

    @Override
    public void removeInCurrentThread(String sensorName) {
        new BackgroundAction().removeSensor(sensorName);
    }

    @Override
    public void setInCurrentThread(Sensor oldSensor, Sensor newSensor) {
        new BackgroundAction().setSensor(oldSensor, newSensor);
    }

    @Override
    public void rewriteInCurrentThread(ArrayList<Sensor> sensors) {
        new BackgroundAction().rewriteSensors(sensors);
    }

    @Override
    public void clear() {
        new BackgroundAction().clear();
    }

    @Override
    public void export(ArrayList<Sensor> sensors) {
        new BackgroundAction().export(sensors);
    }

    @Override
    public void rewrite(ArrayList<Sensor> sensors) {
        new BackgroundAction().rewrite(sensors);
    }

    private class BackgroundAction extends SwingWorker<Void, Void> {
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
        protected Void doInBackground() throws Exception {
            switch (this.action){
                case ADD:
                    this.addSensor(this.sensor);
                    break;
                case CLEAR:
                    this.clearSensors();
                    break;
                case REWRITE:
                    this.rewriteSensors(this.list);
                    break;
                case EXPORT:
                    this.exportSensors(this.list);
                    break;
            }
            return null;
        }

        @Override
        protected void done() {
            Application.setBusy(false);
            if (this.saveMessage != null) this.saveMessage.dispose();
        }

        private void addSensor(Sensor sensor){
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
            }catch (SQLException ex){
                LOGGER.log(Level.SEVERE, "ERROR: ", ex);
            }
        }

        void removeSensor(String sensorName){
            LOGGER.fine("Get connection with DB");
            try (Connection connection = getConnection()){
                LOGGER.fine("Send request to delete");
                String sql = "DELETE FROM sensors WHERE name = '" + sensorName + "';";
                Statement statement = connection.createStatement();
                statement.execute(sql);

                LOGGER.fine("Close connection");
                statement.close();
            }catch (SQLException ex){
                LOGGER.log(Level.SEVERE, "ERROR: ", ex);
            }
        }

        private void clearSensors(){
            LOGGER.fine("Get connection with DB");
            try (Connection connection = getConnection()) {
                LOGGER.fine("Send request");
                String sql = "DELETE FROM sensors;";
                Statement statement = connection.createStatement();
                statement.execute(sql);

                LOGGER.fine("Close connections");
                statement.close();
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, "ERROR: ", ex);
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

        void rewriteSensors(ArrayList<Sensor>sensors){
            if (sensors != null) {
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
                } catch (SQLException ex) {
                    LOGGER.log(Level.SEVERE, "ERROR: ", ex);
                }
            }
        }

        private void exportSensors(ArrayList<Sensor>sensors){
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