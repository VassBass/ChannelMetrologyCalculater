package service.repository.repos.measurement;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import model.Measurement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.json.JacksonJsonObjectMapper;
import service.json.JsonObjectMapper;
import service.repository.config.RepositoryConfigHolder;
import service.repository.connection.RepositoryDBConnector;

import javax.annotation.Nonnull;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MeasurementRepositorySQLite implements MeasurementRepository {
    private static final Logger logger = LoggerFactory.getLogger(MeasurementRepositorySQLite.class);

    private final RepositoryDBConnector connector;
    private final String tableName;
    private final JsonObjectMapper jsonObjectMapper = JacksonJsonObjectMapper.getInstance();

    public MeasurementRepositorySQLite(RepositoryConfigHolder configHolder,
                                       RepositoryDBConnector connector){
        this.connector = connector;
        this.tableName = configHolder.getTableName(MeasurementRepository.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Collection<Measurement> getAll() {
        List<Measurement>measurements = new ArrayList<>();

        String sql = String.format("SELECT * FROM %s;", tableName);
        try (ResultSet resultSet = connector.getResultSet(sql)){
            while (resultSet.next()){
                Measurement measurement = new Measurement();
                measurement.setName(resultSet.getString("name"));
                measurement.setValue(resultSet.getString("value"));
                String factorsJson = resultSet.getString("factors");
                Map<String, Double> factors = jsonObjectMapper.JsonToObject(factorsJson, Map.class);
                if (factors != null) measurement.setFactors(factors);

                measurements.add(measurement);
            }
        }catch (SQLException e){
            logger.warn("Exception was thrown!", e);
        }

        return measurements;
    }

    @Override
    public String[] getAllNames() {
        List<String>names = new ArrayList<>();

        String sql = String.format("SELECT DISTINCT name FROM %s;", tableName);
        try (ResultSet resultSet = connector.getResultSet(sql)){
            while (resultSet.next()){
                String name = resultSet.getString("name");
                names.add(name);
            }
        }catch (SQLException e){
            logger.warn("Exception was thrown!", e);
        }

        return names.toArray(new String[0]);
    }

    @Override
    public String[] getAllValues() {
        List<String>values = new ArrayList<>();

        String sql = String.format("SELECT value FROM %s;", tableName);
        try (ResultSet resultSet = connector.getResultSet(sql)){
            while (resultSet.next()){
                String val = resultSet.getString("value");
                values.add(val);
            }
        }catch (SQLException e){
            logger.warn("Exception was thrown!", e);
        }

        return values.toArray(new String[0]);
    }

    @Override
    public String[] getValues(@Nonnull String name) {
        List<String>values = new ArrayList<>();

        String sql = String.format("SELECT value FROM %s WHERE name = '%s';", tableName, name);
        try (ResultSet resultSet = connector.getResultSet(sql)){
            while (resultSet.next()){
                String val = resultSet.getString("value");
                values.add(val);
            }
        }catch (SQLException e){
            logger.warn("Exception was thrown!", e);
        }

        return values.toArray(new String[0]);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Measurement get(@Nonnull String value) {
        String sql = String.format("SELECT * FROM %s WHERE value = '%s' LIMIT 1;", tableName, value);
        try (ResultSet resultSet = connector.getResultSet(sql)){
            if (resultSet.next()){
                Measurement measurement = new Measurement();
                measurement.setName(resultSet.getString("name"));
                measurement.setValue(resultSet.getString("value"));

                String factorsJson = resultSet.getString("factors");
                Map<String, Double> factors = jsonObjectMapper.JsonToObject(factorsJson, Map.class);
                if (factors != null) measurement.setFactors(factors);

                return measurement;
            }
        }catch (SQLException e){
            logger.warn("Exception was thrown!", e);
        }

        return null;
    }

    @Override
    public boolean add(@Nonnull Measurement measurement) {
        String sql = String.format("INSERT INTO %s (name, value, factors) VALUES (?, ?, ?);", tableName);
        try (PreparedStatement statement = connector.getPreparedStatement(sql)){
            statement.setString(1, measurement.getName());
            statement.setString(2, measurement.getValue());
            String factorsJson = jsonObjectMapper.objectToJson(measurement.getFactors());
            statement.setString(3, factorsJson);
            statement.execute();

            Collection<Measurement>measurements = getAll();
            for (Measurement m : measurements) {
                if (measurement.getName().equals(m.getName())) {
                    Double f = measurement.findFactor(m.getValue());
                    if (f == null) continue;

                    Double factor = 1 / f;
                    m.putFactor(measurement.getValue(), factor);
                    String mFactorsJson = jsonObjectMapper.objectToJson(factor);

                    sql = String.format("UPDATE %s SET factors = '%s' WHERE value = '%s';",
                            tableName, mFactorsJson, m.getValue());
                    try (Statement st = connector.getStatement()) {
                        st.execute(sql);
                    }
                }
            }

            return true;
        }catch (SQLException e){
            logger.warn("Exception was thrown!", e);
            return false;
        }
    }

    @Override
    public boolean changeFactors(@Nonnull String measurementValue, @Nonnull Map<String, Double> factors) {
        Map<String, Double>checkedMap = factors.entrySet().stream()
                .filter(e -> e.getKey() != null && e.getValue() != null)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        try (Statement statement = connector.getStatement()) {
            ObjectWriter writer = new ObjectMapper().writer().withDefaultPrettyPrinter();
            String factorsJson = writer.writeValueAsString(checkedMap);
            String sql = String.format("UPDATE %s SET factors = '%s' WHERE value = '%s';",
                    tableName, factorsJson, measurementValue);
            statement.execute(sql);

            return true;
        }catch (SQLException | JsonProcessingException e){
            logger.warn("Exception was thrown!", e);
            return false;
        }
    }

    @Override
    public boolean set(@Nonnull Measurement oldMeasurement, @Nonnull Measurement newMeasurement) {
        String sql = String.format("UPDATE %s SET name = ?, value = ?, factors = ? WHERE value = ?;", tableName);
        try (PreparedStatement statement = connector.getPreparedStatement(sql)){
            statement.setString(1, newMeasurement.getName());
            statement.setString(2, newMeasurement.getValue());
            String factorsJson = jsonObjectMapper.objectToJson(newMeasurement.getFactors());
            statement.setString(3, factorsJson);
            statement.setString(4, oldMeasurement.getValue());

            int result = statement.executeUpdate();

            if (result > 0) {
                for (Measurement measurement : getAll()) {
                    Double f = measurement.getFactors().get(oldMeasurement.getValue());
                    if (f != null) {
                        Map<String, Double> mFactors = measurement.getFactors();
                        mFactors.remove(oldMeasurement.getValue());
                        mFactors.put(newMeasurement.getValue(), f);
                        String mFactorsJson = jsonObjectMapper.objectToJson(mFactors);

                        sql = String.format("UPDATE %s SET factors = '%s' WHERE value = '%s';",
                                tableName, mFactorsJson, measurement.getValue());
                        try (Statement st = connector.getStatement()) {
                            st.execute(sql);
                        }
                    }
                }
                return true;
            }
        }catch (SQLException e){
            logger.warn("Exception was thrown!", e);
        }

        return false;
    }

    @Override
    public boolean remove(@Nonnull Measurement measurement) {
        String sql = String.format("DELETE FROM %s WHERE value = '%s';", tableName, measurement.getValue());
        try (Statement statement = connector.getStatement()){
            int result = statement.executeUpdate(sql);

            if (result > 0) {
                for (Measurement m : getAll()) {
                    Map<String, Double> factors = m.getFactors();
                    factors.remove(measurement.getValue());
                    String factorsJson = jsonObjectMapper.objectToJson(factors);
                    sql = String.format("UPDATE %s SET factors = '%s' WHERE value = '%s';",
                            tableName, factorsJson, m.getValue());
                    statement.execute(sql);
                }
                return true;
            }
        }catch (SQLException e){
            logger.warn("Exception was thrown!", e);
        }

        return false;
    }

    @Override
    public boolean clear() {
        String sql = String.format("DELETE FROM %s;", tableName);
        try (Statement statement = connector.getStatement()){
            statement.execute(sql);
            return true;
        }catch (SQLException e){
            logger.warn("Exception was thrown!", e);
            return false;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public Collection<Measurement> getMeasurements(@Nonnull String name) {
        List<Measurement>measurements = new ArrayList<>();
        String sql = String.format("SELECT * FROM %s WHERE name = '%s';", tableName, name);
        try (ResultSet resultSet = connector.getResultSet(sql)){
            while (resultSet.next()){
                Measurement measurement = new Measurement();
                measurement.setName(resultSet.getString("name"));
                measurement.setValue(resultSet.getString("value"));

                String factorsJson = resultSet.getString("factors");
                Map<String, Double> factors = jsonObjectMapper.JsonToObject(factorsJson, Map.class);
                if (factors != null) measurement.setFactors(factors);

                measurements.add(measurement);
            }
        }catch (SQLException e){
            logger.warn("Exception was thrown!", e);
        }

        return measurements;
    }

    @Override
    public boolean rewrite(@Nonnull Collection<Measurement> measurements) {
        String sql = String.format("DELETE FROM %s;", tableName);
        try (Statement statement = connector.getStatement()) {
            statement.execute(sql);

            if (!measurements.isEmpty()) {
                String insertSql = String.format("INSERT INTO %s (name, value, factors) VALUES ", tableName);
                StringBuilder sqlBuilder = new StringBuilder(insertSql);

                for (Measurement measurement : measurements) {
                    if (measurement == null) continue;

                    String factorsJson = jsonObjectMapper.objectToJson(measurement.getFactors());
                    String values = String.format("('%s', '%s', '%s'),",
                            measurement.getName(), measurement.getValue(), factorsJson);
                    sqlBuilder.append(values);
                }
                sqlBuilder.setCharAt(sqlBuilder.length()-1, ';');
                statement.execute(sqlBuilder.toString());
            }

            return true;
        } catch (SQLException e) {
            logger.warn("Exception was thrown!", e);
            return false;
        }
    }

    @Override
    public boolean isLastInMeasurement(@Nonnull String measurementValue) {
        Measurement measurement = get(measurementValue);
        if (measurement != null){
            String sql = String.format("SELECT count(*) AS n FROM %s WHERE name = '%s';", tableName, measurement.getName());
            try (ResultSet resultSet = connector.getResultSet(sql)){
                if (resultSet.next()){
                    return resultSet.getInt("n") <= 1;
                }
            } catch (SQLException e) {
                logger.warn("Exception was thrown!", e);
            }
        }

        return false;
    }

    @Override
    public boolean exists(@Nonnull String measurementValue) {
        String sql = String.format("SELECT * FROM %s WHERE value = '%s' LIMIT 1;", tableName, measurementValue);
        try (ResultSet resultSet = connector.getResultSet(sql)){
            return resultSet.next();
        }catch (SQLException e){
            logger.warn("Exception was thrown!", e);
            return false;
        }
    }

    @Override
    public boolean exists(@Nonnull String oldValue, @Nonnull String newValue) {
        if (oldValue.equals(newValue)) return false;
        return exists(newValue);
    }
}