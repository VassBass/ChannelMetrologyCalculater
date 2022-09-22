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
        String sql = "SELECT * FROM measurements WHERE value = '" + value + "' LIMIT 1;";
        try (ResultSet resultSet = getResultSet(sql)){
            if (resultSet.next()){
                Measurement measurement = new Measurement();
                measurement.setName(resultSet.getString("name"));
                measurement.setValue(resultSet.getString("value"));
                measurement._setFactors(resultSet.getString("factors"));

                return Optional.of(measurement);
            }
        }catch (SQLException |JsonProcessingException e){
            LOGGER.warn("Exception was thrown!", e);
        }

        return Optional.empty();
    }

    @Override
    public boolean add(@Nonnull Measurement measurement) {
        String sql = "INSERT INTO measurements(name, value, factors) VALUES (?, ?, ?);";
        try (PreparedStatement statement = getPreparedStatement(sql)){
            statement.setString(1, measurement.getName());
            statement.setString(2, measurement.getValue());
            statement.setString(3, measurement._getFactorsJson());
            statement.execute();

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
            statement.execute(sql);

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

            if (result > 0) {
                for (Measurement measurement : getAll()) {
                    Double f = measurement.getFactors().get(oldMeasurement.getValue());
                    if (f != null) {
                        measurement.getFactors().remove(oldMeasurement.getValue());
                        measurement.getFactors().put(newMeasurement.getValue(), f);

                        sql = "UPDATE measurements SET factors = '" + measurement._getFactorsJson() + "' WHERE value = '" + measurement.getValue() + "';";
                        try (Statement st = getStatement()) {
                            st.execute(sql);
                        }
                    }
                }
                return true;
            }
        }catch (SQLException | JsonProcessingException e){
            LOGGER.warn("Exception was thrown!", e);
        }

        return false;
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
                return true;
            }
        }catch (SQLException | JsonProcessingException e){
            LOGGER.warn("Exception was thrown!", e);
        }

        return false;
    }

    @Override
    public boolean clear() {
        String sql = "DELETE FROM measurements;";
        try (Statement statement = getStatement()){
            statement.execute(sql);
            return true;
        }catch (SQLException e){
            LOGGER.warn("Exception was thrown!", e);
            return false;
        }
    }

    @Override
    public Collection<Measurement> getMeasurements(@Nonnull String name) {
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
                }
            } catch (SQLException e) {
                LOGGER.warn("Exception was thrown!", e);
            }
        }

        return false;
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