package repository.impl;

import application.Application;
import application.ApplicationContext;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import constants.Action;
import model.Measurement;
import repository.MeasurementRepository;
import repository.Repository;
import ui.model.SaveMessage;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MeasurementRepositoryImpl extends Repository<Measurement> implements MeasurementRepository {
    private static final Logger LOGGER = Logger.getLogger(MeasurementRepository.class.getName());

    private boolean backgroundTaskRunning = false;

    public MeasurementRepositoryImpl(){super();}
    public MeasurementRepositoryImpl(String dbUrl){super(dbUrl);}

    @SuppressWarnings("unchecked")
    @Override
    protected void init(){
        String sql = "CREATE TABLE IF NOT EXISTS measurements ("
                + "name text NOT NULL"
                + ", value text NOT NULL UNIQUE"
                + ", factors text NOT NULL, "
                + "PRIMARY KEY(\"value\")"
                + ");";

        LOGGER.fine("Get connection with DB");
        try (Connection connection = this.getConnection();
            Statement statement = connection.createStatement()){
            LOGGER.fine("Send request to create table");
            statement.execute(sql);

            LOGGER.fine("Send request to read measurements from DB");
            sql = "SELECT * FROM measurements";
            try (ResultSet resultSet = statement.executeQuery(sql)) {
                while (resultSet.next()) {
                    String name = resultSet.getString("name");
                    String value = resultSet.getString("value");
                    String factorsJson = resultSet.getString("factors");
                    HashMap<String, Double>factors = new ObjectMapper().readValue(factorsJson, HashMap.class);
                    Measurement measurement = new Measurement(name, value);
                    measurement.setFactors(factors);
                    this.mainList.add(measurement);
                }
            }
        } catch (SQLException | JsonProcessingException ex) {
            LOGGER.log(Level.SEVERE, "Initialization ERROR", ex);
        }
        LOGGER.info("Initialization SUCCESS");
    }

    @Override
    public ArrayList<Measurement> getAll() {
        return this.mainList;
    }

    @Override
    public String[] getAllNames() {
        ArrayList<String>names = new ArrayList<>();
        for (Measurement measurement : this.mainList){
            String name = measurement.getName();
            boolean exist = false;
            for (String n : names){
                if (n.equals(name)) {
                    exist = true;
                    break;
                }
            }
            if (!exist){
                names.add(name);
            }
        }
        return names.toArray(new String[0]);
    }

    @Override
    public String[] getAllValues() {
        String[]values = new String[this.mainList.size()];
        for (int m=0;m<this.mainList.size();m++){
            values[m] = this.mainList.get(m).getValue();
        }
        return values;
    }

    @Override
    public String[] getValues(Measurement measurement) {
        if (measurement != null) {
            ArrayList<String> values = new ArrayList<>();
            for (Measurement m : this.mainList) {
                if (m.getName().equals(measurement.getName())) {
                    values.add(m.getValue());
                }
            }
            return values.toArray(new String[0]);
        } else return null;
    }

    @Override
    public String[] getValues(String name) {
        if (name != null && name.length() > 0) {
            ArrayList<String> values = new ArrayList<>();
            for (Measurement measurement : this.mainList) {
                if (measurement.getName().equals(name)) {
                    values.add(measurement.getValue());
                }
            }
            return values.toArray(new String[0]);
        }else return null;
    }

    @Override
    public Measurement get(String value) {
        if (value != null && value.length() > 0){
            int index = this.mainList.indexOf(new Measurement("", value));
            return index < 0 ? null : this.mainList.get(index);
        }else return null;
    }

    @Override
    public ArrayList<Measurement> addInCurrentThread(Measurement measurement) {
        if (measurement != null && !this.mainList.contains(measurement)){
            if (this.mainList.add(measurement)){
                HashMap<String, Double>factors = measurement.getFactors();

                LOGGER.fine("Get connection with DB");
                try (Connection connection = getConnection();
                     Statement statement = connection.createStatement()){

                    LOGGER.fine("Send requests to add");
                    String sql = "INSERT INTO measurements('name', 'value', 'factors') "
                            + "VALUES ('" + measurement.getName() + "', "
                            + "'" + measurement.getValue() + "', "
                            + "'" + measurement._getFactorsJson() + "');";
                    statement.execute(sql);
                    for (Measurement m : this.mainList) {
                        if (measurement.getName().equals(m.getName())) {
                            Double factor = 1 / factors.get(m.getValue());
                            m.getFactors().put(measurement.getValue(), factor);

                            sql = "UPDATE measurements SET factors = '" + m._getFactorsJson() + "' "
                                    + "WHERE value = '" + m.getValue() + "';";

                            statement.execute(sql);
                        }
                    }
                }catch (SQLException | JsonProcessingException ex){
                    LOGGER.log(Level.SEVERE, "ERROR: ", ex);
                }
            }
        }
        return this.mainList;
    }

    @Override
    public void changeFactors(String measurementValue, HashMap<String, Double> factors) {
        Measurement measurement = get(measurementValue);
        if (measurement != null && factors != null){
            measurement.setFactors(factors);
            new BackgroundAction().changeFactors(measurementValue, factors);
        }
    }

    @Override
    public void changeInCurrentThread(Measurement oldMeasurement, Measurement newMeasurement) {
        int index = this.mainList.indexOf(oldMeasurement);
        if (oldMeasurement != null && newMeasurement != null
                && index >= 0 && !this.mainList.contains(newMeasurement)){
            this.mainList.set(index, newMeasurement);

            LOGGER.fine("Get connection with DB");
            try (Connection connection = getConnection();
                 Statement statement = connection.createStatement()){

                LOGGER.fine("Send request");
                String sql = "UPDATE measurements SET value = '" + newMeasurement.getValue() + "' "
                        + "WHERE value = '" + oldMeasurement.getValue() + "';";

                statement.execute(sql);

                for (Measurement m : this.mainList){
                    if (!m.getValue().equals(newMeasurement.getValue())) {
                        Double val = m.getFactors().get(oldMeasurement.getValue());
                        m.getFactors().remove(oldMeasurement.getValue());
                        m.getFactors().put(newMeasurement.getValue(), val);
                        sql = "UPDATE measurements SET factors = '" + m._getFactorsJson() + "' WHERE value = '" + m.getValue() + "';";
                        statement.execute(sql);
                    }
                }
            }catch (SQLException | JsonProcessingException ex){
                LOGGER.log(Level.SEVERE, "ERROR: ", ex);
            }
        }
    }

    @Override
    public void delete(Measurement measurement) {
        if (measurement != null && this.mainList.contains(measurement)){
            try (Connection connection = this.getConnection();
                Statement statement = connection.createStatement()){
                String sql = "DELETE FROM measurements "
                        + "WHERE value = '" + measurement.getValue() + "';";
                statement.execute(sql);

                this.mainList.remove(measurement);

                for (Measurement m : this.mainList){
                    m.getFactors().remove(measurement.getValue());
                    sql = "UPDATE measurements SET factors = '" + m._getFactorsJson() + "' WHERE value = '" + m.getValue() + "';";
                    statement.execute(sql);
                }
            }catch (SQLException | JsonProcessingException ex){
                LOGGER.log(Level.SEVERE, "Error: ", ex);
            }
        }
    }

    @Override
    public void clear() {
        try (Connection connection = this.getConnection();
             Statement statement = connection.createStatement()){
            String sql = "DELETE FROM measurements;";
            statement.execute(sql);

            this.mainList.clear();
        }catch (SQLException ex){
            LOGGER.log(Level.SEVERE, "Error: ", ex);
        }
    }

    @Override
    public ArrayList<Measurement> getMeasurements(String name) {
        if (name != null && name.length() > 0) {
            ArrayList<Measurement> measurements = new ArrayList<>();
            for (Measurement measurement : this.mainList) {
                if (measurement.getName().equals(name)) {
                    measurements.add(measurement);
                }
            }
            return measurements;
        }else return null;
    }

    @Override
    public void rewriteInCurrentThread(ArrayList<Measurement> measurements) {
        this.mainList.clear();
        this.mainList.addAll(measurements);

        if (measurements != null && !measurements.isEmpty()) {
            LOGGER.fine("Get connection with DB");
            try (Connection connection = getConnection();
                 Statement statement = connection.createStatement()) {
                LOGGER.fine("Send request to clear");
                String sql = "DELETE FROM measurements;";
                statement.execute(sql);

                if (!measurements.isEmpty()) {
                    LOGGER.fine("Send requests to add");
                    for (Measurement measurement : measurements) {
                        sql = "INSERT INTO measurements ('name', 'value', 'factors') "
                                + "VALUES ('" + measurement.getName() + "', "
                                + "'" + measurement.getValue() + "', "
                                + "'" + measurement._getFactorsJson() + "'"
                                + ");";
                        statement.execute(sql);
                    }
                }
            } catch (SQLException | JsonProcessingException ex) {
                LOGGER.log(Level.SEVERE, "ERROR: ", ex);
            }
        }
    }

    @Override
    public boolean backgroundTaskIsRun() {
        return this.backgroundTaskRunning;
    }

    @Override
    public boolean isLastInMeasurement(String measurementValue) {
        if (measurementValue != null) {
            String measurementName = get(measurementValue).getName();
            int number = 0;
            for (Measurement measurement : this.mainList) {
                if (measurement.getName().equals(measurementName)) number++;
            }
            return number == 1;
        }else return false;
    }

    @Override
    public boolean exists(String measurementValue) {
        Measurement measurement = new Measurement("",measurementValue);
        return this.mainList.contains(measurement);
    }

    @Override
    public boolean exists(String oldValue, String newValue) {
        int oldIndex = this.mainList.indexOf(new Measurement("", oldValue));
        int newIndex = this.mainList.indexOf(new Measurement("", newValue));
        return newIndex >= 0 && newIndex != oldIndex;
    }

    private class BackgroundAction extends SwingWorker<Boolean, Void> {
        private HashMap<String, Double>factors;
        private String value;
        private Action action;
        private final SaveMessage saveMessage;

        public BackgroundAction(){
            ApplicationContext context = Application.context;
            Window mainScreen = context == null ? null : Application.context.mainScreen;
            this.saveMessage = mainScreen == null ? null : new SaveMessage(mainScreen);
        }

        void changeFactors(String measurementValue, HashMap<String, Double>factors){
            this.value = measurementValue;
            this.factors = factors;
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
            if (this.action == Action.SET) {
                return changeMeasurementFactors(value, factors);
            }
            return true;
        }

        @Override
        protected void done() {
            Application.setBusy(false);
            backgroundTaskRunning = false;
            if (this.saveMessage != null) this.saveMessage.dispose();
        }

        boolean changeMeasurementFactors(String measurementValue, HashMap<String, Double>factors){
            Connection connection = null;
            Statement statement = null;
            try {
                ObjectWriter writer = new ObjectMapper().writer().withDefaultPrettyPrinter();
                String json = writer.writeValueAsString(factors);
                String sql = "UPDATE measurements SET factors = '" + json + "'"
                + "WHERE value = '" + measurementValue + "';";
                connection = getConnection();
                statement = connection.createStatement();
                statement.execute(sql);
                return true;
            } catch (JsonProcessingException | SQLException ex) {
                LOGGER.log(Level.SEVERE, "ERROR: ", ex);
                try {
                    if (statement != null) statement.close();
                    if (connection != null) connection.close();
                } catch (SQLException ignored) {}
                return false;
            }
        }
    }
}