package repository.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import model.Measurement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.MeasurementRepository;
import repository.RepositoryJDBC;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class MeasurementRepositorySQLite extends RepositoryJDBC implements MeasurementRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(MeasurementRepositorySQLite.class);

    public MeasurementRepositorySQLite(){
        setPropertiesFromFile();
        createTable();
    }
    public MeasurementRepositorySQLite(String dbUrl, String dbUser, String dbPassword){
        setProperties(dbUrl, dbUser, dbPassword);
        createTable();
    }

    @Override
    public boolean createTable(){
        String sql = "CREATE TABLE IF NOT EXISTS measurements ("
                + "name text NOT NULL"
                + ", value text NOT NULL UNIQUE"
                + ", factors text NOT NULL, "
                + "PRIMARY KEY(\"value\")"
                + ");";
        try (Statement statement = getStatement()){
            statement.execute(sql);
            return isTableExists("measurements");
        }catch (SQLException e){
            LOGGER.warn("Exception was thrown!", e);
            return false;
        }
    }

    @Override
    public Collection<Measurement> getAll() {
        List<Measurement>measurements = new ArrayList<>();

        LOGGER.info("Reading all measurements from DB");
        String sql = "SELECT * FROM measurements;";
        try (ResultSet resultSet = getResultSet(sql)){
            while (resultSet.next()){
                Measurement measurement = new Measurement();
                measurement.setName(resultSet.getString("name"));
                measurement.setValue(resultSet.getString("value"));
                measurement._setFactors(resultSet.getString("factors"));

                measurements.add(measurement);
            }
        }catch (SQLException | JsonProcessingException e){
            LOGGER.warn("Exception was thrown!", e);
        }

        return measurements;
    }

    @Override
    public String[] getAllNames() {
        List<String>names = new ArrayList<>();

        LOGGER.info("Reading all measurements from DB");
        String sql = "SELECT DISTINCT name FROM measurements;";
        try (ResultSet resultSet = getResultSet(sql)){
            while (resultSet.next()){
                String name = resultSet.getString("name");
                names.add(name);
            }
        }catch (SQLException e){
            LOGGER.warn("Exception was thrown!", e);
        }

        return names.toArray(new String[0]);
    }

    @Override
    public String[] getAllValues() {
        List<String>values = new ArrayList<>();

        LOGGER.info("Reading all measurements from DB");
        String sql = "SELECT value FROM measurements;";
        try (ResultSet resultSet = getResultSet(sql)){
            while (resultSet.next()){
                String val = resultSet.getString("value");
                values.add(val);
            }
        }catch (SQLException e){
            LOGGER.warn("Exception was thrown!", e);
        }

        return values.toArray(new String[0]);
    }

    @Override
    public String[] getValues(Measurement measurement) {
        List<String>values = new ArrayList<>();
        if (measurement == null) return values.toArray(new String[0]);

        LOGGER.info("Reading all measurements from DB");
        String sql = "SELECT value FROM measurements WHERE name = '" + measurement.getName() + "';";
        try (ResultSet resultSet = getResultSet(sql)){
            while (resultSet.next()){
                String val = resultSet.getString("value");
                values.add(val);
            }
        }catch (SQLException e){
            LOGGER.warn("Exception was thrown!", e);
        }

        return values.toArray(new String[0]);
    }

    @Override
    public String[] getValues(String name) {
        List<String>values = new ArrayList<>();
        if (name == null || name.isEmpty()) return values.toArray(new String[0]);

        LOGGER.info("Reading all measurements from DB");
        String sql = "SELECT value FROM measurements WHERE name = '" + name + "';";
        try (ResultSet resultSet = getResultSet(sql)){
            while (resultSet.next()){
                String val = resultSet.getString("value");
                values.add(val);
            }
        }catch (SQLException e){
            LOGGER.warn("Exception was thrown!", e);
        }

        return values.toArray(new String[0]);
    }

    @Override
    public Measurement get(String value) {
        LOGGER.info("Reading measurement with value = {} from DB", value);
        String sql = "SELECT * FROM measurements WHERE value = '" + value + "' LIMIT 1;";
        try (ResultSet resultSet = getResultSet(sql)){
            if (resultSet.next()){
                Measurement measurement = new Measurement();
                measurement.setName(resultSet.getString("name"));
                measurement.setValue(resultSet.getString("value"));
                measurement._setFactors(resultSet.getString("factors"));

                return measurement;
            }else return null;
        }catch (SQLException |JsonProcessingException e){
            LOGGER.warn("Exception was thrown!", e);
            return null;
        }
    }

    @Override
    public boolean add(Measurement measurement) {
        if (measurement == null) return false;

        String sql = "INSERT INTO measurements(name, value, factors) VALUES (?, ?, ?);";
        try (PreparedStatement statement = getPreparedStatement(sql)){
            statement.setString(1, measurement.getName());
            statement.setString(2, measurement.getValue());
            statement.setString(3, measurement._getFactorsJson());
            int result = statement.executeUpdate();

            Collection<Measurement>measurements = getAll();
            for (Measurement m : measurements) {
                if (measurement.getName().equals(m.getName())) {
                    Double factor = 1 / measurement._getFactor(m.getValue());
                    m.addFactor(measurement.getValue(), factor);

                    sql = "UPDATE measurements SET factors = '" + m._getFactorsJson() + "' WHERE value = '" + m.getValue() + "';";
                    try (Statement st = getStatement()) {
                        st.execute(sql);
                    }
                }
            }

            if (result > 0) LOGGER.info("Measurement = \n{}\nwas added successfully", measurement);
            return true;
        }catch (SQLException | JsonProcessingException e){
            LOGGER.warn("Exception was thrown!", e);
            return false;
        }
    }

    @Override
    public boolean changeFactors(String measurementValue, Map<String, Double> factors) {
        if (measurementValue == null || factors == null) return false;

        try (Statement statement = getStatement()) {
            ObjectWriter writer = new ObjectMapper().writer().withDefaultPrettyPrinter();
            String factorsJson = writer.writeValueAsString(factors);
            String sql = "UPDATE measurements SET factors = '" + factorsJson + "' WHERE value = '" + measurementValue + "';";
            int result = statement.executeUpdate(sql);

            if (result > 0) LOGGER.info("Factors of measurement with value: {} was replaced by:\n{}\nsuccessfully", measurementValue, factors);
            return true;
        }catch (SQLException | JsonProcessingException e){
            LOGGER.warn("Exception was thrown!", e);
            return false;
        }
    }

    @Override
    public boolean set(Measurement oldMeasurement, Measurement newMeasurement) {
        if (oldMeasurement == null || newMeasurement == null) return false;
        if (oldMeasurement.isMatch(newMeasurement)) return true;

        String sql = "UPDATE measurements SET name = ?, value = ?, factors = ? WHERE value = ?;";
        try (PreparedStatement statement = getPreparedStatement(sql)){
            statement.setString(1, newMeasurement.getName());
            statement.setString(2, newMeasurement.getValue());
            statement.setString(3, newMeasurement._getFactorsJson());
            statement.setString(4, oldMeasurement.getValue());

            int result = statement.executeUpdate();

            for (Measurement measurement : getAll()){
                Double f = measurement.getFactors().get(oldMeasurement.getValue());
                if (f != null){
                    measurement.getFactors().remove(oldMeasurement.getValue());
                    measurement.getFactors().put(newMeasurement.getValue(), f);

                    sql = "UPDATE measurement SET factors = '" + measurement._getFactorsJson() + "' WHERE value = '" + measurement.getValue() + "';";
                    try (Statement st = getStatement()){
                        st.execute(sql);
                    }
                }
            }

            if (result > 0) LOGGER.info("Measurement:\n{}\nwas replaced by:\n{}\nsuccessfully", oldMeasurement, newMeasurement);
            return true;
        }catch (SQLException | JsonProcessingException e){
            LOGGER.warn("Exception was thrown!", e);
            return false;
        }
    }

    @Override
    public boolean remove(Measurement measurement) {
        if (measurement == null) return false;

        String sql = "DELETE FROM measurements WHERE value = '" + measurement.getValue() + "';";
        try (Statement statement = getStatement()){
            int result = statement.executeUpdate(sql);

            if (result > 0) {
                for (Measurement m : getAll()) {
                    m.getFactors().remove(measurement.getValue());
                    sql = "UPDATE measurements SET factors = '" + m._getFactorsJson() + "' WHERE value = '" + m.getValue() + "';";
                    statement.execute(sql);
                }

                LOGGER.info("Measurement = {} was removed successfully", measurement);
            }else LOGGER.info("Measurement = {} not found", measurement);

            return true;
        }catch (SQLException | JsonProcessingException e){
            LOGGER.warn("Exception was thrown!", e);
            return false;
        }
    }

    @Override
    public boolean clear() {
        String sql = "DELETE FROM measurements;";
        try (Statement statement = getStatement()){
            statement.execute(sql);
            LOGGER.info("Measurements list in DB was cleared successfully");
            return true;
        }catch (SQLException e){
            LOGGER.warn("Exception was thrown!", e);
            return false;
        }
    }

    @Override
    public Collection<Measurement> getMeasurements(String name) {
        if (name == null) return new ArrayList<>();

        LOGGER.info("Reading all measurements with name = {} from DB", name);
        List<Measurement>measurements = new ArrayList<>();
        String sql = "SELECT * FROM measurements WHERE name = '" + name + "';";
        try (ResultSet resultSet = getResultSet(sql)){
            while (resultSet.next()){
                Measurement measurement = new Measurement();
                measurement.setName(resultSet.getString("name"));
                measurement.setValue(resultSet.getString("value"));
                measurement._setFactors(resultSet.getString("factors"));

                measurements.add(measurement);
            }
        }catch (SQLException | JsonProcessingException e){
            LOGGER.warn("Exception was thrown!", e);
        }

        return measurements;
    }

    @Override
    public boolean rewrite(Collection<Measurement> measurements) {
        if (measurements == null) return false;

        String sql = "DELETE FROM measurements;";
        try (Statement statement = getStatement()) {
            statement.execute(sql);
            LOGGER.info("Measurements list in DB was cleared successfully");

            if (!measurements.isEmpty()) {
                String insertSql = "INSERT INTO measurements (name, value, factors) "
                        + "VALUES ";
                StringBuilder sqlBuilder = new StringBuilder(insertSql);

                for (Measurement measurement : measurements) {
                    sqlBuilder.append("('").append(measurement.getName()).append("', ")
                            .append("'").append(measurement.getValue()).append("', ")
                            .append("'").append(measurement._getFactorsJson()).append("'),");
                }
                sqlBuilder.setCharAt(sqlBuilder.length()-1, ';');
                statement.execute(sqlBuilder.toString());
            }

            LOGGER.info("The list of old measurements has been rewritten to the new one:\n{}", measurements);
            return true;
        } catch (SQLException | JsonProcessingException e) {
            LOGGER.warn("Exception was thrown!", e);
            return false;
        }
    }

    @Override
    public boolean isLastInMeasurement(String measurementValue) {
        if (measurementValue != null) {
            String measurementName = get(measurementValue).getName();
            int number = 0;
            for (Measurement measurement : getAll()) {
                if (measurement.getName().equals(measurementName)) number++;
                if (number > 1) return false;
            }
            return true;
        }else return false;
    }

    @Override
    public boolean exists(String measurementValue) {
        if (measurementValue == null) return false;

        String sql = "SELECT * FROM measurements WHERE value = '" + measurementValue + "' LIMIT 1;";
        try (ResultSet resultSet = getResultSet(sql)){
            return resultSet.next();
        }catch (SQLException e){
            LOGGER.warn("Exception was thrown!", e);
            return false;
        }
    }

    @Override
    public boolean exists(String oldValue, String newValue) {
        if (oldValue == null || newValue == null || oldValue.equals(newValue)) return false;

        return exists(newValue);
    }
}