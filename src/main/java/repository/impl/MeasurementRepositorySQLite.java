package repository.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import model.Measurement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.MeasurementRepository;
import repository.RepositoryJDBC;

import javax.annotation.Nonnull;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.stream.Collectors;

public class MeasurementRepositorySQLite extends RepositoryJDBC implements MeasurementRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(MeasurementRepositorySQLite.class);
    private static MeasurementRepositorySQLite instance;

    private MeasurementRepositorySQLite(){
        setPropertiesFromFile();
        createTable();
    }
    public MeasurementRepositorySQLite(String dbUrl, String dbUser, String dbPassword){
        setProperties(dbUrl, dbUser, dbPassword);
        createTable();
    }

    public static MeasurementRepositorySQLite getInstance() {
        if (instance == null) instance = new MeasurementRepositorySQLite();
        return instance;
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
    public String[] getValues(@Nonnull Measurement measurement) {
        List<String>values = new ArrayList<>();

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
    public String[] getValues(@Nonnull String name) {
        List<String>values = new ArrayList<>();

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
    public Optional<Measurement> get(@Nonnull String value) {
        LOGGER.info("Reading measurement with value = {} from DB", value);
        String sql = "SELECT * FROM measurements WHERE value = '" + value + "' LIMIT 1;";
        try (ResultSet resultSet = getResultSet(sql)){
            if (resultSet.next()){
                Measurement measurement = new Measurement();
                measurement.setName(resultSet.getString("name"));
                measurement.setValue(resultSet.getString("value"));
                measurement._setFactors(resultSet.getString("factors"));

                return Optional.of(measurement);
            }else return Optional.empty();
        }catch (SQLException |JsonProcessingException e){
            LOGGER.warn("Exception was thrown!", e);
            return Optional.empty();
        }
    }

    public Optional<Measurement> getWithLoggerTurnOff(@Nonnull String value) {
        String sql = "SELECT * FROM measurements WHERE value = '" + value + "' LIMIT 1;";
        try (ResultSet resultSet = getResultSet(sql)){
            if (resultSet.next()){
                Measurement measurement = new Measurement();
                measurement.setName(resultSet.getString("name"));
                measurement.setValue(resultSet.getString("value"));
                measurement._setFactors(resultSet.getString("factors"));

                return Optional.of(measurement);
            }else return Optional.empty();
        }catch (SQLException |JsonProcessingException e){
            LOGGER.warn("Exception was thrown!", e);
            return Optional.empty();
        }
    }

    @Override
    public boolean add(@Nonnull Measurement measurement) {
        String sql = "INSERT INTO measurements(name, value, factors) VALUES (?, ?, ?);";
        try (PreparedStatement statement = getPreparedStatement(sql)){
            statement.setString(1, measurement.getName());
            statement.setString(2, measurement.getValue());
            statement.setString(3, measurement._getFactorsJson());
            int result = statement.executeUpdate();

            Collection<Measurement>measurements = getAll();
            for (Measurement m : measurements) {
                if (measurement.getName().equals(m.getName())) {
                    Double f = measurement._getFactor(m.getValue());
                    if (f == null) continue;

                    Double factor = 1 / f;
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
    public boolean changeFactors(@Nonnull String measurementValue, @Nonnull Map<String, Double> factors) {
        Map<String, Double>checkedMap = factors.entrySet().stream()
                .filter(e -> e.getKey() != null && e.getValue() != null)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        try (Statement statement = getStatement()) {
            ObjectWriter writer = new ObjectMapper().writer().withDefaultPrettyPrinter();
            String factorsJson = writer.writeValueAsString(checkedMap);
            String sql = "UPDATE measurements SET factors = '" + factorsJson + "' WHERE value = '" + measurementValue + "';";
            int result = statement.executeUpdate(sql);

            if (result > 0) LOGGER.info("Factors of measurement with value: {} was replaced by:\n{}\nsuccessfully", measurementValue, checkedMap);
            return true;
        }catch (SQLException | JsonProcessingException e){
            LOGGER.warn("Exception was thrown!", e);
            return false;
        }
    }

    @Override
    public boolean set(@Nonnull Measurement oldMeasurement, @Nonnull Measurement newMeasurement) {
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

                    sql = "UPDATE measurements SET factors = '" + measurement._getFactorsJson() + "' WHERE value = '" + measurement.getValue() + "';";
                    try (Statement st = getStatement()){
                        st.execute(sql);
                    }
                }
            }

            if (result > 0) {
                LOGGER.info("Measurement:\n{}\nwas replaced by:\n{}\nsuccessfully", oldMeasurement, newMeasurement);
                return true;
            }else {
                LOGGER.info("Measurement:\n{}\nnot found", oldMeasurement);
                return false;
            }
        }catch (SQLException | JsonProcessingException e){
            LOGGER.warn("Exception was thrown!", e);
            return false;
        }
    }

    @Override
    public boolean remove(@Nonnull Measurement measurement) {
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
                return true;
            }else {
                LOGGER.info("Measurement = {} not found", measurement);
                return false;
            }
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
    public Collection<Measurement> getMeasurements(@Nonnull String name) {
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
    public boolean rewrite(@Nonnull Collection<Measurement> measurements) {
        String sql = "DELETE FROM measurements;";
        try (Statement statement = getStatement()) {
            statement.execute(sql);
            LOGGER.info("Measurements list in DB was cleared successfully");

            if (!measurements.isEmpty()) {
                String insertSql = "INSERT INTO measurements (name, value, factors) "
                        + "VALUES ";
                StringBuilder sqlBuilder = new StringBuilder(insertSql);

                for (Measurement measurement : measurements) {
                    if (measurement == null) continue;

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
    public boolean isLastInMeasurement(@Nonnull String measurementValue) {
        Optional<Measurement>m = get(measurementValue);
        if (m.isPresent()){
            Measurement measurement = m.get();
            String sql = "SELECT count(*) AS n FROM measurements WHERE name = '" + measurement.getName() + "';";
            try (ResultSet resultSet = getResultSet(sql)){
                if (resultSet.next()){
                    return resultSet.getInt("n") <= 1;
                }else throw new SQLException();
            } catch (SQLException e) {
                LOGGER.warn("Exception was thrown!", e);
                return false;
            }
        }else {
            LOGGER.info("Measurement with value = '" + measurementValue + "' not found");
            return false;
        }
    }

    @Override
    public boolean exists(@Nonnull String measurementValue) {
        String sql = "SELECT * FROM measurements WHERE value = '" + measurementValue + "' LIMIT 1;";
        try (ResultSet resultSet = getResultSet(sql)){
            return resultSet.next();
        }catch (SQLException e){
            LOGGER.warn("Exception was thrown!", e);
            return false;
        }
    }

    @Override
    public boolean exists(@Nonnull String oldValue, @Nonnull String newValue) {
        if (oldValue.equals(newValue)) return false;
        return exists(newValue);
    }
}