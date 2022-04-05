package repository.impl;

import model.Measurement;
import repository.MeasurementRepository;
import repository.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MeasurementRepositoryImpl extends Repository<Measurement> implements MeasurementRepository {
    private static final Logger LOGGER = Logger.getLogger(MeasurementRepository.class.getName());

    public MeasurementRepositoryImpl(){super();}
    public MeasurementRepositoryImpl(String dbUrl){super(dbUrl);}

    @Override
    protected void init(){
        String sql = "CREATE TABLE IF NOT EXISTS measurements ("
                + "name text NOT NULL"
                + ", value text NOT NULL UNIQUE, "
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
                    Measurement measurement = new Measurement(name, value);
                    this.mainList.add(measurement);
                }
            }
        } catch (SQLException ex) {
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
    public Measurement get(int index) {
        return index < 0 || index >= this.mainList.size() ? null : this.mainList.get(index);
    }

    @Override
    public void add(Measurement measurement) {
        if (measurement != null && !this.mainList.contains(measurement)){
            try (Connection connection = this.getConnection();
                Statement statement = connection.createStatement()){
                String sql = "INSERT INTO measurements ('name', 'value') "
                        + "VALUES ("
                        + "'" + measurement.getName() + "', "
                        + "'" + measurement.getValue() + "'"
                        + ");";
                statement.execute(sql);

                this.mainList.add(measurement);
            }catch (SQLException ex){
                LOGGER.log(Level.SEVERE, "Error: ", ex);
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
            }catch (SQLException ex){
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
                        sql = "INSERT INTO measurements ('name', 'value') " +
                                "VALUES ('" + measurement.getName() + "', " +
                                "'" + measurement.getValue() + "');";
                        statement.execute(sql);
                    }
                }
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, "ERROR: ", ex);
            }
        }
    }
}