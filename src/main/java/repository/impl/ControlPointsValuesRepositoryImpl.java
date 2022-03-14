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
import java.util.Collections;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ControlPointsValuesRepositoryImpl extends Repository implements ControlPointsValuesRepository {
    private static final Logger LOGGER = Logger.getLogger(AreaRepository.class.getName());

    private final ArrayList<ControlPointsValues>controlPoints = new ArrayList<>();

    public ControlPointsValuesRepositoryImpl(){super();}
    public ControlPointsValuesRepositoryImpl(String dbUrl){super(dbUrl);}

    @Override
    protected void init() {
        LOGGER.fine("Get connection with DB");
        try (Connection connection = this.getConnection()){
            String sql = "CREATE TABLE IF NOT EXISTS control_points ("
                    + "id integer NOT NULL UNIQUE"
                    + ", sensor_type text NOT NULL"
                    + ", points text NOT NULL"
                    + ", range_min real NOT NULL"
                    + ", range_max real NOT NULL"
                    + ", PRIMARY KEY (\"id\" AUTOINCREMENT)"
                    + ");";
            Statement statement = connection.createStatement();

            LOGGER.fine("Send request to create table");
            statement.execute(sql);

            LOGGER.fine("Send request to read control points from DB");
            sql = "SELECT * FROM control_points";
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()){
                ControlPointsValues cpv = new ControlPointsValues();
                cpv.setId(resultSet.getInt("id"));
                cpv.setSensorType(resultSet.getString("sensor_type"));
                cpv.setValues(VariableConverter.stringToArray(resultSet.getString("points")));
                cpv.setRangeMin(resultSet.getDouble("range_min"));
                cpv.setRangeMax(resultSet.getDouble("range_max"));
                this.controlPoints.add(cpv);
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
    public ArrayList<ControlPointsValues> getAll() {
        return this.controlPoints;
    }

    @Override
    public ArrayList<ControlPointsValues> getBySensorType(String sensorType) {
        ArrayList<ControlPointsValues>list = new ArrayList<>();
        for (ControlPointsValues cpv : this.controlPoints){
            if (cpv.getSensorType().equals(sensorType)){
                list.add(cpv);
            }
        }
        return list;
    }

    @Override
    public double[] getValues(String sensorType, double rangeMin, double rangeMax) {
        if (sensorType != null) {
            for (ControlPointsValues cpv : this.controlPoints) {
                if (cpv.equalsBy(sensorType, rangeMin, rangeMax)) {
                    return cpv.getValues();
                }
            }
        }
        return null;
    }

    @Override
    public ControlPointsValues getControlPointsValues(String sensorType, int index) {
        int i = 0;
        for (ControlPointsValues cpv : this.controlPoints){
            if (cpv.getSensorType().equals(sensorType)){
                if (i == index){
                    return cpv;
                }else {
                    i++;
                }
            }
        }
        return null;
    }

    @Override
    public void put(ControlPointsValues cpv) {
        if (cpv != null){
            int index = this.controlPoints.indexOf(cpv);
            if (index >= 0){
                new BackgroundAction().set(this.controlPoints.get(index), cpv);
                this.controlPoints.set(index, cpv);
            }else {
                this.controlPoints.add(cpv);
                new BackgroundAction().add(cpv);
            }
        }
    }

    @Override
    public void remove(ControlPointsValues cpv) {
        if (cpv != null && this.controlPoints.remove(cpv)){
            new BackgroundAction().remove(cpv.getId());
        }
    }

    @Override
    public void removeAllInCurrentThread(String sensorType) {
        ArrayList<Integer>indexes = new ArrayList<>();
        for (int i=0;i<this.controlPoints.size();i++){
            ControlPointsValues cpv = this.controlPoints.get(i);
            if (cpv.getSensorType().equals(sensorType)){
                indexes.add(i);
            }
        }
        Collections.reverse(indexes);
        for (int i : indexes){
            this.controlPoints.remove(i);
        }
        new BackgroundAction().clearControlPoints(sensorType);
    }

    @Override
    public void clear(String sensorType) {
        ArrayList<Integer>indexes = new ArrayList<>();
        for (int i=0;i<this.controlPoints.size();i++){
            ControlPointsValues cpv = this.controlPoints.get(i);
            if (cpv.getSensorType().equals(sensorType)){
                indexes.add(i);
            }
        }
        Collections.reverse(indexes);
        for (int i : indexes){
            this.controlPoints.remove(i);
        }
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

    private class BackgroundAction extends SwingWorker<Boolean, Void> {
        private ControlPointsValues cpv, oldCpv;
        private int id;
        private String sensorType;
        private Action action;
        private final SaveMessage saveMessage;

        public BackgroundAction(){
            ApplicationContext context = Application.context;
            Window mainScreen = context == null ? null : Application.context.mainScreen;
            this.saveMessage = mainScreen == null ? null : new SaveMessage(mainScreen);
        }

        void add(ControlPointsValues cpv){
            this.cpv = cpv;
            this.action = Action.ADD;
            this.start();
        }

        void set(ControlPointsValues oldCpv, ControlPointsValues newCpv){
            this.oldCpv = oldCpv;
            this.cpv =newCpv;
            this.action = Action.SET;
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
        protected Boolean doInBackground() throws Exception {
            switch (this.action){
                case ADD:
                    return this.addControlPoints(this.cpv);
                case SET:
                    return this.setControlPoints(this.oldCpv, this.cpv);
                case REMOVE:
                    return this.removeControlPoints(this.id);
                case CLEAR:
                    return this.clearControlPoints(this.sensorType);
            }
            return true;
        }

        @Override
        protected void done() {
            try {
                if (!this.get()){
                    switch (this.action){
                        case ADD:
                            controlPoints.remove(this.cpv);
                            break;
                        case REMOVE:
                            if (!controlPoints.contains(this.cpv)) controlPoints.add(this.cpv);
                            break;
                        case SET:
                            controlPoints.remove(this.cpv);
                            if (!controlPoints.contains(this.oldCpv)) controlPoints.add(this.oldCpv);
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

        private boolean addControlPoints(ControlPointsValues cpv){
            LOGGER.fine("Get connection with DB");
            try (Connection connection = getConnection()){
                LOGGER.fine("Send requests");
                String sql = "INSERT INTO control_points ('id', 'sensor_type', 'points', 'range_min', 'range_max') "
                        + "VALUES(?, ?, ?, ?, ?);";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setInt(1, cpv.getId());
                statement.setString(2, cpv.getSensorType());
                statement.setString(3, VariableConverter.arrayToString(cpv.getValues()));
                statement.setDouble(4, cpv.getRangeMin());
                statement.setDouble(5, cpv.getRangeMax());
                statement.execute();

                LOGGER.fine("Close connection");
                statement.close();
                return true;
            }catch (SQLException ex){
                LOGGER.log(Level.SEVERE, "ERROR: ", ex);
                return false;
            }
        }

        private boolean setControlPoints(ControlPointsValues oldControlPoints, ControlPointsValues newControlPoints){
            LOGGER.fine("Get connection with DB");
            try (Connection connection = getConnection()){
                LOGGER.fine("Send request to delete");
                String sql = "DELETE FROM control_points WHERE id = '?';";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setInt(1, oldControlPoints.getId());
                statement.execute();

                LOGGER.fine("Send requests to add");
                sql = "INSERT INTO control_points ('id', 'sensor_type', 'points', 'range_min', 'range_max') "
                        + "VALUES(?, ?, ?, ?, ?);";
                statement = connection.prepareStatement(sql);
                statement.setInt(1, newControlPoints.getId());
                statement.setString(2, newControlPoints.getSensorType());
                statement.setString(3, VariableConverter.arrayToString(newControlPoints.getValues()));
                statement.setDouble(4, newControlPoints.getRangeMin());
                statement.setDouble(5, newControlPoints.getRangeMax());
                statement.execute();

                LOGGER.fine("Close connection");
                statement.close();
                return true;
            }catch (SQLException ex){
                LOGGER.log(Level.SEVERE, "ERROR: ", ex);
                return false;
            }
        }

        private boolean removeControlPoints(int id){
            LOGGER.fine("Get connection with DB");
            try (Connection connection = getConnection()){
                LOGGER.fine("Send request to delete");
                String sql = "DELETE FROM control_points WHERE id = '" + id + "';";
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

        public boolean clearControlPoints(String sensorType){
            LOGGER.fine("Get connection with DB");
            try (Connection connection = getConnection()) {
                LOGGER.fine("Send request");
                String sql = "DELETE FROM control_points WHERE sensor_type = '"+ sensorType + "';";
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
    }
}
