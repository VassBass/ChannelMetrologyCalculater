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
                + ", value text NOT NULL"
                + ");";

        LOGGER.fine("Get connection with DB");
        try (Connection connection = this.getConnection()){
            Statement statement = connection.createStatement();

            LOGGER.fine("Send request to create table");
            statement.execute(sql);

            LOGGER.fine("Send request to read measurements from DB");
            sql = "SELECT * FROM measurements";
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()){
                String name = resultSet.getString("name");
                String value = resultSet.getString("value");
                Measurement measurement = new Measurement(name, value);
                this.mainList.add(measurement);
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
        if (value != null && value.length() > 0) {
            for (Measurement measurement : this.mainList) {
                if (measurement.getValue().equals(value)) {
                    return measurement;
                }
            }
        }
        return null;
    }

    @Override
    public Measurement get(int index) {
        return index < 0 || index >= this.mainList.size() ? null : this.mainList.get(index);
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

        String sql = measurements == null || measurements.isEmpty() ? "DELETE FROM measurements;" : null;

        LOGGER.fine("Get connection with DB");
        if (sql != null){
            try (Connection connection = getConnection()) {
                Statement statement = connection.createStatement();

                LOGGER.fine("Send request");
                statement.execute(sql);

                LOGGER.fine("Close connections");
                statement.close();
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, "ERROR: ", ex);
            }
        }else {
            try (Connection connection = getConnection()){
                LOGGER.fine("Send request to clear");
                Statement statementClear = connection.createStatement();
                sql = "DELETE FROM measurements;";
                statementClear.execute(sql);

                if (!measurements.isEmpty()) {
                    LOGGER.fine("Send requests to add");
                    sql = "INSERT INTO measurements ('name', 'value') VALUES (?, ?);";
                    PreparedStatement statement = connection.prepareStatement(sql);
                    for (Measurement measurement : measurements) {
                        statement.setString(1, measurement.getName());
                        statement.setString(2, measurement.getValue());
                        statement.execute();
                    }

                    LOGGER.fine("Close connections");
                    statementClear.close();
                    statement.close();
                }
            }catch (SQLException ex){
                LOGGER.log(Level.SEVERE, "ERROR: ", ex);
            }
        }
    }
}