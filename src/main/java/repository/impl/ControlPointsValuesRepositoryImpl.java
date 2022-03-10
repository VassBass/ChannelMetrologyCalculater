package repository.impl;

import application.Application;
import application.ApplicationContext;
import constants.Action;
import converters.VariableConverter;
import def.DefaultControlPointsValues;
import model.ControlPointsValues;
import repository.AreaRepository;
import repository.ControlPointsValuesRepository;
import repository.Repository;
import ui.model.SaveMessage;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ControlPointsValuesRepositoryImpl extends Repository implements ControlPointsValuesRepository {
    private static final Logger LOGGER = Logger.getLogger(AreaRepository.class.getName());

    public ControlPointsValuesRepositoryImpl(){super();}
    public ControlPointsValuesRepositoryImpl(String dbUrl){super(dbUrl);}

    @Override
    protected void init() {
        String sql = "CREATE TABLE IF NOT EXISTS control_points ("
                + "id integer NOT NULL UNIQUE"
                + ", sensor_type text NOT NULL"
                + ", points text NOT NULL"
                + ", range_min real NOT NULL"
                + ", range_max real NOT NULL"
                + ", PRIMARY KEY (\"id\" AUTOINCREMENT)"
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
    public ArrayList<ControlPointsValues> getAll() {
        ArrayList<ControlPointsValues> controlPointsValues = new ArrayList<>();
        LOGGER.fine("Get connection with DB");
        try (Connection connection = this.getConnection()){
            Statement statement = connection.createStatement();

            LOGGER.fine("Send request");
            String sql = "SELECT * FROM control_points";
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()){
                ControlPointsValues cpv = new ControlPointsValues();
                cpv.setId(resultSet.getInt("id"));
                cpv.setSensorType(resultSet.getString("sensor_type"));
                cpv.setValues(VariableConverter.stringToArray(resultSet.getString("points")));
                cpv.setRangeMin(resultSet.getDouble("range_min"));
                cpv.setRangeMax(resultSet.getDouble("range_max"));
                controlPointsValues.add(cpv);
            }

            LOGGER.fine("Close connections");
            resultSet.close();
            statement.close();
        }catch (SQLException ex){
            LOGGER.log(Level.SEVERE, "ERROR: ", ex);
        }
        return controlPointsValues;
    }

    @Override
    public void put(ControlPointsValues cpv) {
        new BackgroundAction().put(cpv);
    }

    @Override
    public void remove(int id) {
        new BackgroundAction().remove(id);
    }

    @Override
    public void removeAllInCurrentThread(String sensorType) {
        new BackgroundAction().clearControlPoints(sensorType);
    }

    @Override
    public void clear(String sensorType) {
        new BackgroundAction().clear(sensorType);
    }

    @Override
    public void resetToDefault() {
            LOGGER.fine("Get connection with DB");
            try (Connection connection = getConnection()) {
                LOGGER.fine("Send request to clear");
                Statement statementClear = connection.createStatement();
                String sql = "DELETE FROM control_points;";
                statementClear.execute(sql);

                ArrayList<ControlPointsValues>cpvs = DefaultControlPointsValues.get();
                if (!cpvs.isEmpty()) {
                    LOGGER.fine("Send requests to add");
                    sql = "INSERT INTO control_points ('id', 'sensor_type', 'points', 'range_min', 'range_max')" +
                            " VALUES (?, ?, ?, ?, ?);";
                    PreparedStatement statement = connection.prepareStatement(sql);
                    for (ControlPointsValues cpv : cpvs) {
                        statement.setInt(1, cpv.getId());
                        statement.setString(2, cpv.getSensorType());
                        statement.setString(3, VariableConverter.arrayToString(cpv.getValues()));
                        statement.setDouble(4, cpv.getRangeMin());
                        statement.setDouble(5, cpv.getRangeMax());
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

    private class BackgroundAction extends SwingWorker<Void, Void> {
        private ControlPointsValues cpv;
        private int id;
        private String sensorType;
        private Action action;
        private final SaveMessage saveMessage;

        public BackgroundAction(){
            ApplicationContext context = Application.context;
            Window mainScreen = context == null ? null : Application.context.mainScreen;
            this.saveMessage = mainScreen == null ? null : new SaveMessage(mainScreen);
        }

        void put(ControlPointsValues cpv){
            this.cpv = cpv;
            this.action = Action.ADD;
            this.start();
        }

        void remove(int id){
            this.id = id;
            this.action = Action.REMOVE;
            this.start();
        }

        void clear(String sensorType){
            this.sensorType = sensorType;
            this.action = Action.CLEAR;
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
                    this.putControlPoints(this.cpv);
                    break;
                case REMOVE:
                    this.removeControlPoints(this.id);
                    break;
                case CLEAR:
                    this.clearControlPoints(this.sensorType);
                    break;
            }
            return null;
        }

        @Override
        protected void done() {
            Application.setBusy(false);
            if (this.saveMessage != null) this.saveMessage.dispose();
        }

        private void putControlPoints(ControlPointsValues cpv){
            LOGGER.fine("Get connection with DB");
            try (Connection connection = getConnection()){
                LOGGER.fine("Send request to delete");
                String sql = "DELETE FROM control_points WHERE id = '?';";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setInt(1, cpv.getId());
                statement.execute();

                LOGGER.fine("Send requests to add");
                sql = "INSERT INTO control_points ('id', 'sensor_type', 'points', 'range_min', 'range_max') "
                        + "VALUES(?, ?, ?, ?, ?);";
                statement = connection.prepareStatement(sql);
                statement.setInt(1, cpv.getId());
                statement.setString(2, cpv.getSensorType());
                statement.setString(3, VariableConverter.arrayToString(cpv.getValues()));
                statement.setDouble(4, cpv.getRangeMin());
                statement.setDouble(5, cpv.getRangeMax());
                statement.execute();

                LOGGER.fine("Close connection");
                statement.close();
            }catch (SQLException ex){
                LOGGER.log(Level.SEVERE, "ERROR: ", ex);
            }
        }

        private void removeControlPoints(int id){
            LOGGER.fine("Get connection with DB");
            try (Connection connection = getConnection()){
                LOGGER.fine("Send request to delete");
                String sql = "DELETE FROM control_points WHERE id = '" + id + "';";
                Statement statement = connection.createStatement();
                statement.execute(sql);

                LOGGER.fine("Close connection");
                statement.close();
            }catch (SQLException ex){
                LOGGER.log(Level.SEVERE, "ERROR: ", ex);
            }
        }

        public void clearControlPoints(String sensorType){
            LOGGER.fine("Get connection with DB");
            try (Connection connection = getConnection()) {
                LOGGER.fine("Send request");
                String sql = "DELETE FROM control_points WHERE sensor_type = '"+ sensorType + "';";
                Statement statement = connection.createStatement();
                statement.execute(sql);

                LOGGER.fine("Close connections");
                statement.close();
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, "ERROR: ", ex);
            }
        }
    }
}
