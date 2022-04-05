package repository.impl;

import application.Application;
import application.ApplicationContext;
import constants.Action;
import converters.VariableConverter;
import def.DefaultControlPointsValues;
import model.ControlPointsValues;
import model.Sensor;
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

public class ControlPointsValuesRepositoryImpl extends Repository<ControlPointsValues> implements ControlPointsValuesRepository {
    private static final Logger LOGGER = Logger.getLogger(AreaRepository.class.getName());

    public ControlPointsValuesRepositoryImpl(){super();}
    public ControlPointsValuesRepositoryImpl(String dbUrl){super(dbUrl);}

    private boolean backgroundTaskRunning = false;

    @Override
    protected void init() {
        LOGGER.fine("Get connection with DB");
        try (Connection connection = this.getConnection();
            Statement statement = connection.createStatement()){
            String sql = "CREATE TABLE IF NOT EXISTS control_points ("
                    + "sensor_type text NOT NULL"
                    + ", points text NOT NULL"
                    + ", range_min real NOT NULL"
                    + ", range_max real NOT NULL"
                    + ");";
            LOGGER.fine("Send request to create table");
            statement.execute(sql);

            LOGGER.fine("Send request to read control points from DB");
            sql = "SELECT * FROM control_points";
            try (ResultSet resultSet = statement.executeQuery(sql)) {
                while (resultSet.next()) {
                    ControlPointsValues cpv = new ControlPointsValues();
                    cpv.setSensorType(resultSet.getString("sensor_type"));
                    cpv.setValues(VariableConverter.stringToArray(resultSet.getString("points")));
                    cpv.setRangeMin(resultSet.getDouble("range_min"));
                    cpv.setRangeMax(resultSet.getDouble("range_max"));
                    this.mainList.add(cpv);
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Initialization ERROR", ex);
        }
        LOGGER.info("Initialization SUCCESS");
    }

    @Override
    public ArrayList<ControlPointsValues> getAll() {
        return this.mainList;
    }

    @Override
    public ArrayList<ControlPointsValues> getBySensorType(String sensorType) {
        ArrayList<ControlPointsValues>list = new ArrayList<>();
        for (ControlPointsValues cpv : this.mainList){
            if (cpv.getSensorType().equals(sensorType)){
                list.add(cpv);
            }
        }
        return list;
    }

    @Override
    public double[] getValues(String sensorType, double rangeMin, double rangeMax) {
        if (sensorType != null) {
            for (ControlPointsValues cpv : this.mainList) {
                if (cpv.equalsBy(sensorType, rangeMin, rangeMax)) {
                    return cpv.getValues();
                }
            }
        }
        return null;
    }

    /**
     *
     * @param sensorType type of Sensor {@link Sensor#getType()}
     * @param index sequence number in {@link #mainList} among control points with {@link Sensor#getType()}
     * @return null if ControlPointsValues not exists in {@link #mainList}
     * or if sensorType equals null
     * or if index < 0 or if index >= {@link #mainList}.size()
     */
    @Override
    public ControlPointsValues getControlPointsValues(String sensorType, int index) {
        if (index < 0 || index >= this.mainList.size()) return null;

        int i = 0;
        for (ControlPointsValues cpv : this.mainList){
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
            int index = this.mainList.indexOf(cpv);
            if (index >= 0){
                new BackgroundAction().set(this.mainList.get(index), cpv);
                this.mainList.set(index, cpv);
            }else {
                this.mainList.add(cpv);
                new BackgroundAction().add(cpv);
            }
        }
    }

    @Override
    public void putInCurrentThread(ControlPointsValues cpv) {
        if (cpv != null){
            int index = this.mainList.indexOf(cpv);
            if (index >= 0){
                new BackgroundAction().setControlPoints(this.mainList.get(index), cpv);
                this.mainList.set(index, cpv);
            }else {
                this.mainList.add(cpv);
                new BackgroundAction().addControlPoints(cpv);
            }
        }
    }

    @Override
    public void remove(ControlPointsValues cpv) {
        if (cpv != null && this.mainList.remove(cpv)){
            new BackgroundAction().remove(cpv);
        }
    }

    @Override
    public void removeAllInCurrentThread(String sensorType) {
        if (sensorType == null){
            this.mainList.clear();
        }else {
            ArrayList<Integer> indexes = new ArrayList<>();
            for (int i = 0; i < this.mainList.size(); i++) {
                ControlPointsValues cpv = this.mainList.get(i);
                if (cpv.getSensorType().equals(sensorType)) {
                    indexes.add(i);
                }
            }
            Collections.reverse(indexes);
            for (int i : indexes) {
                this.mainList.remove(i);
            }
        }
        new BackgroundAction().clearControlPoints(sensorType);
    }

    /**
     *
     * @param sensorType is type of Sensor whose control points need to be cleared
     * If sensorType is equals null the entire list is cleared
     *
     */
    @Override
    public void clear(String sensorType) {
        if (sensorType != null){
            ArrayList<Integer> indexes = new ArrayList<>();
            for (int i = 0; i < this.mainList.size(); i++) {
                ControlPointsValues cpv = this.mainList.get(i);
                if (cpv.getSensorType().equals(sensorType)) {
                    indexes.add(i);
                }
            }
            Collections.reverse(indexes);
            for (int i : indexes) {
                this.mainList.remove(i);
            }
        }else this.mainList.clear();
        new BackgroundAction().clear(sensorType);
    }

    @Override
    public void resetToDefaultInCurrentThread() {
            LOGGER.fine("Get connection with DB");
            try (Connection connection = getConnection()) {
                LOGGER.fine("Send request to clear");
                Statement statementClear = connection.createStatement();
                String sql = "DELETE FROM control_points;";
                this.mainList.clear();
                statementClear.execute(sql);

                this.mainList.addAll(DefaultControlPointsValues.get());
                if (!this.mainList.isEmpty()) {
                    LOGGER.fine("Send requests to add");
                    sql = "INSERT INTO control_points ('sensor_type', 'points', 'range_min', 'range_max')" +
                            " VALUES (?, ?, ?, ?);";
                    PreparedStatement statement = connection.prepareStatement(sql);
                    for (ControlPointsValues cpv : this.mainList) {
                        statement.setString(1, cpv.getSensorType());
                        statement.setString(2, VariableConverter.arrayToString(cpv.getValues()));
                        statement.setDouble(3, cpv.getRangeMin());
                        statement.setDouble(4, cpv.getRangeMax());
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

    @Override
    public boolean backgroundTaskIsRun() {
        return backgroundTaskRunning;
    }

    private class BackgroundAction extends SwingWorker<Boolean, Void> {
        private ControlPointsValues cpv, oldCpv;
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

        void remove(ControlPointsValues cpv){
            this.cpv = cpv;
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
            backgroundTaskRunning = true;
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
                    return this.removeControlPoints(this.cpv);
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
                            mainList.remove(this.cpv);
                            break;
                        case REMOVE:
                            if (!mainList.contains(this.cpv)) mainList.add(this.cpv);
                            break;
                        case SET:
                            mainList.remove(this.cpv);
                            if (!mainList.contains(this.oldCpv)) mainList.add(this.oldCpv);
                            break;
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

        boolean addControlPoints(ControlPointsValues cpv){
            String sql = "INSERT INTO control_points ('sensor_type', 'points', 'range_min', 'range_max') "
                    + "VALUES(?, ?, ?, ?);";
            LOGGER.fine("Get connection with DB");
            try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)){
                LOGGER.fine("Send requests");
                statement.setString(1, cpv.getSensorType());
                statement.setString(2, VariableConverter.arrayToString(cpv.getValues()));
                statement.setDouble(3, cpv.getRangeMin());
                statement.setDouble(4, cpv.getRangeMax());
                statement.execute();
            }catch (SQLException ex){
                LOGGER.log(Level.SEVERE, "ERROR: ", ex);
                return false;
            }
            return true;
        }

        boolean setControlPoints(ControlPointsValues oldControlPoints, ControlPointsValues newControlPoints){
            LOGGER.fine("Get connection with DB");
            try (Connection connection = getConnection();
                Statement statement = connection.createStatement()){
                LOGGER.fine("Send request to delete");
                LOGGER.fine("Send requests to update");
                String sql = "UPDATE control_points SET "
                        + "sensor_type = '" + newControlPoints.getSensorType() + "', "
                        + "points = '" + VariableConverter.arrayToString(newControlPoints.getValues()) + "', "
                        + "range_min = " + newControlPoints.getRangeMin() + ", "
                        + "range_max = " + newControlPoints.getRangeMax() + " "
                        + "WHERE sensor_type = '" + oldControlPoints.getSensorType() + "' "
                        + "AND range_min = " + oldControlPoints.getRangeMin() + " "
                        + "AND range_max = " + oldControlPoints.getRangeMax() + " "
                        + ";";
                statement.execute(sql);
            }catch (SQLException ex){
                LOGGER.log(Level.SEVERE, "ERROR: ", ex);
                return false;
            }
            return true;
        }

        private boolean removeControlPoints(ControlPointsValues cpv){
            LOGGER.fine("Get connection with DB");
            try (Connection connection = getConnection();
                Statement statement = connection.createStatement()){
                LOGGER.fine("Send request to delete");
                String sql = "DELETE FROM control_points WHERE sensor_type = '" + cpv.getSensorType() + "' "
                        + "AND range_min = " + cpv.getRangeMin() + " "
                        + "AND range_max = " + cpv.getRangeMax()
                        + ";";
                statement.execute(sql);
            }catch (SQLException ex){
                LOGGER.log(Level.SEVERE, "ERROR: ", ex);
                return false;
            }
            return true;
        }

        public boolean clearControlPoints(String sensorType){
            LOGGER.fine("Get connection with DB");
            try (Connection connection = getConnection();
                Statement statement = connection.createStatement()) {
                LOGGER.fine("Send request");
                String sql = sensorType == null ? "DELETE FROM control_points;" : "DELETE FROM control_points WHERE sensor_type = '"+ sensorType + "';";
                statement.execute(sql);
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, "ERROR: ", ex);
                return false;
            }
            return true;
        }
    }
}