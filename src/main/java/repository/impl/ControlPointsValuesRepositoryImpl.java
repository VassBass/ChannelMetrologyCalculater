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

public class ControlPointsValuesRepositoryImpl extends Repository<ControlPointsValues> implements ControlPointsValuesRepository {
    private static final Logger LOGGER = Logger.getLogger(AreaRepository.class.getName());

    public ControlPointsValuesRepositoryImpl(){super();}
    public ControlPointsValuesRepositoryImpl(String dbUrl){super(dbUrl);}

    @Override
    protected void init() {
        LOGGER.fine("Get connection with DB");
        try (Connection connection = this.getConnection();
            Statement statement = connection.createStatement()){
            String sql = "CREATE TABLE IF NOT EXISTS control_points ("
                    + "id integer NOT NULL UNIQUE"
                    + ", sensor_type text NOT NULL"
                    + ", points text NOT NULL"
                    + ", range_min real NOT NULL"
                    + ", range_max real NOT NULL"
                    + ", PRIMARY KEY (\"id\" AUTOINCREMENT)"
                    + ");";
            LOGGER.fine("Send request to create table");
            statement.execute(sql);

            LOGGER.fine("Send request to read control points from DB");
            sql = "SELECT * FROM control_points";
            try (ResultSet resultSet = statement.executeQuery(sql)) {
                while (resultSet.next()) {
                    ControlPointsValues cpv = new ControlPointsValues();
                    cpv.setId(resultSet.getInt("id"));
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

    @Override
    public ControlPointsValues getControlPointsValues(String sensorType, int index) {
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
            new BackgroundAction().remove(cpv.getId());
        }
    }

    @Override
    public void removeAllInCurrentThread(String sensorType) {
        ArrayList<Integer>indexes = new ArrayList<>();
        for (int i=0;i<this.mainList.size();i++){
            ControlPointsValues cpv = this.mainList.get(i);
            if (cpv.getSensorType().equals(sensorType)){
                indexes.add(i);
            }
        }
        Collections.reverse(indexes);
        for (int i : indexes){
            this.mainList.remove(i);
        }
        new BackgroundAction().clearControlPoints(sensorType);
    }

    @Override
    public void clear(String sensorType) {
        ArrayList<Integer>indexes = new ArrayList<>();
        for (int i=0;i<this.mainList.size();i++){
            ControlPointsValues cpv = this.mainList.get(i);
            if (cpv.getSensorType().equals(sensorType)){
                indexes.add(i);
            }
        }
        Collections.reverse(indexes);
        for (int i : indexes){
            this.mainList.remove(i);
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
                this.mainList.clear();
                statementClear.execute(sql);

                this.mainList.addAll(DefaultControlPointsValues.get());
                if (!this.mainList.isEmpty()) {
                    LOGGER.fine("Send requests to add");
                    sql = "INSERT INTO control_points ('id', 'sensor_type', 'points', 'range_min', 'range_max')" +
                            " VALUES (?, ?, ?, ?, ?);";
                    PreparedStatement statement = connection.prepareStatement(sql);
                    for (ControlPointsValues cpv : this.mainList) {
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
            if (this.saveMessage != null) this.saveMessage.dispose();
        }

        boolean addControlPoints(ControlPointsValues cpv){
            String sql = "INSERT INTO control_points ('id', 'sensor_type', 'points', 'range_min', 'range_max') "
                    + "VALUES(?, ?, ?, ?, ?);";
            LOGGER.fine("Get connection with DB");
            try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)){
                LOGGER.fine("Send requests");
                statement.setInt(1, cpv.getId());
                statement.setString(2, cpv.getSensorType());
                statement.setString(3, VariableConverter.arrayToString(cpv.getValues()));
                statement.setDouble(4, cpv.getRangeMin());
                statement.setDouble(5, cpv.getRangeMax());
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
                        + "id = " + newControlPoints.getId() + ", "
                        + "sensor_type = '" + newControlPoints.getSensorType() + "', "
                        + "points = '" + VariableConverter.arrayToString(newControlPoints.getValues()) + "', "
                        + "range_min = " + newControlPoints.getRangeMin() + ", "
                        + "range_max = " + newControlPoints.getRangeMax() + " "
                        + "WHERE id = " + oldControlPoints.getId() + ";";
                statement.execute(sql);
            }catch (SQLException ex){
                LOGGER.log(Level.SEVERE, "ERROR: ", ex);
                return false;
            }
            return true;
        }

        private boolean removeControlPoints(int id){
            LOGGER.fine("Get connection with DB");
            try (Connection connection = getConnection();
                Statement statement = connection.createStatement()){
                LOGGER.fine("Send request to delete");
                String sql = "DELETE FROM control_points WHERE id = '" + id + "';";
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
                String sql = "DELETE FROM control_points WHERE sensor_type = '"+ sensorType + "';";
                statement.execute(sql);
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, "ERROR: ", ex);
                return false;
            }
            return true;
        }
    }
}