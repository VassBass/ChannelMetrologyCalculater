package service.repository.repos.sensor;

import model.Sensor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.repository.config.RepositoryConfigHolder;
import service.repository.connection.RepositoryDBConnector;
import service.repository.init.SensorRepositoryInitializer;

import javax.annotation.Nonnull;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.EMPTY;

public class SensorRepositorySQLite implements SensorRepository {
    private static final Logger logger = LoggerFactory.getLogger(SensorRepositorySQLite.class);

    private final RepositoryDBConnector connector;
    private final String tableName;

    public SensorRepositorySQLite(RepositoryConfigHolder configHolder, RepositoryDBConnector connector){
        this.tableName = configHolder.getTableName(SensorRepository.class);
        this.connector = connector;
        new SensorRepositoryInitializer(configHolder, connector).init();
    }

    @Override
    public Collection<Sensor> getAll() {
        List<Sensor>sensors = new ArrayList<>();

        String sql = String.format("SELECT * FROM %s;", tableName);
        try (ResultSet resultSet = connector.getResultSet(sql)){
            while (resultSet.next()){
                Sensor sensor = new Sensor();
                sensor.setName(resultSet.getString("name"));
                sensor.setType(resultSet.getString("type"));
                sensor.setNumber(resultSet.getString("number"));
                sensor.setMeasurement(resultSet.getString("measurement"));
                sensor.setValue(resultSet.getString("value"));
                sensor.setErrorFormula(resultSet.getString("error_formula"));
                sensor.setRangeMin(resultSet.getDouble("range_min"));
                sensor.setRangeMax(resultSet.getDouble("range_max"));
                sensors.add(sensor);
            }
        }catch (SQLException e){
            logger.warn("Exception was thrown!", e);
        }

        return sensors;
    }

    @Override
    public Collection<Sensor> getAllByMeasurementName(@Nonnull String measurement) {
        List<Sensor>sensors = new ArrayList<>();

        String sql = String.format("SELECT * FROM %s WHERE measurement = '%s';", tableName, measurement);
        try (ResultSet resultSet = connector.getResultSet(sql)){
            while (resultSet.next()){
                Sensor sensor = new Sensor();
                sensor.setName(resultSet.getString("name"));
                sensor.setType(resultSet.getString("type"));
                sensor.setNumber(resultSet.getString("number"));
                sensor.setMeasurement(resultSet.getString("measurement"));
                sensor.setValue(resultSet.getString("value"));
                sensor.setErrorFormula(resultSet.getString("error_formula"));
                sensor.setRangeMin(resultSet.getDouble("range_min"));
                sensor.setRangeMax(resultSet.getDouble("range_max"));
                sensors.add(sensor);
            }
        }catch (SQLException e){
            logger.warn("Exception was thrown!", e);
        }

        return sensors;
    }

    @Override
    public Collection<String> getAllTypes() {
        List<String>types = new ArrayList<>();
        String sql = String.format("SELECT DISTINCT type FROM %s;", tableName);
        try (ResultSet resultSet = connector.getResultSet(sql)){
            while (resultSet.next()){
                types.add(resultSet.getString("type"));
            }
        }catch (SQLException e){
            logger.warn("Exception was thrown!", e);
        }

        return types;
    }

    @Override
    public String getMeasurementNameBySensorType(@Nonnull String sensorType) {
        String sql = String.format("SELECT measurement FROM %s WHERE type = '%s';", tableName, sensorType);
        String measurement = null;
        try (ResultSet resultSet = connector.getResultSet(sql)){
            if (resultSet.next()){
                measurement = resultSet.getString("measurement");
            }
        }catch (SQLException e){
            logger.warn("Exception was thrown!", e);
        }

        return measurement == null ? EMPTY : measurement;
    }

    @Override
    public Collection<String> getAllSensorsNameByMeasurementName(@Nonnull String measurementName) {
        List<String> names = new ArrayList<>();

        String sql = String.format("SELECT name FROM %s WHERE measurement = '%s';", tableName, measurementName);
        try (ResultSet resultSet = connector.getResultSet(sql)){
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                if (name == null) continue;
                names.add(name);
            }
        }catch (SQLException e){
            logger.warn("Exception was thrown!", e);
        }

        return names;
    }

    @Override
    public Sensor get(@Nonnull String sensorName) {
        String sql = String.format("SELECT * FROM %s WHERE name = '%s' LIMIT 1;", tableName, sensorName);
        try (ResultSet resultSet = connector.getResultSet(sql)){
            if (resultSet.next()){
                Sensor sensor = new Sensor();
                sensor.setName(resultSet.getString("name"));
                sensor.setType(resultSet.getString("type"));
                sensor.setNumber(resultSet.getString("number"));
                sensor.setMeasurement(resultSet.getString("measurement"));
                sensor.setValue(resultSet.getString("value"));
                sensor.setErrorFormula(resultSet.getString("error_formula"));
                sensor.setRangeMin(resultSet.getDouble("range_min"));
                sensor.setRangeMax(resultSet.getDouble("range_max"));

                return sensor;
            }
        }catch (SQLException e){
            logger.warn("Exception was thrown!", e);
        }

        return null;
    }

    @Override
    public boolean add(@Nonnull Sensor sensor) {
        String sql = String.format("INSERT INTO %s (name, type, number, measurement, value, error_formula, range_min, range_max) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?);",
                tableName);
        try (PreparedStatement statement = connector.getPreparedStatement(sql)){
            statement.setString(1, sensor.getName());
            statement.setString(2, sensor.getType());
            statement.setString(3, sensor.getNumber());
            statement.setString(4, sensor.getMeasurement());
            statement.setString(5, sensor.getValue());
            statement.setString(6, sensor.getErrorFormula());
            statement.setDouble(7, sensor.getRangeMin());
            statement.setDouble(8, sensor.getRangeMax());

            return statement.executeUpdate() > 0;
        }catch (SQLException e){
            logger.warn("Exception was thrown!", e);
            return false;
        }
    }

    @Override
    public boolean remove(@Nonnull Sensor sensor) {
        String sql = String.format("DELETE FROM %s WHERE name = '%s';", tableName, sensor.getName());
        try (Statement statement = connector.getStatement()){
            return statement.executeUpdate(sql) > 0;
        }catch (SQLException e){
            logger.warn("Exception was thrown!", e);
            return false;
        }
    }

    @Override
    public boolean set(@Nonnull Sensor oldSensor, @Nonnull Sensor newSensor) {
        String sql = String.format("UPDATE %s SET name = ?, type = ?, number = ?, measurement = ?, value = ?, error_formula = ?" +
                ", range_min = ?, range_max = ? WHERE name = ?" +
                ";", tableName);
        try (PreparedStatement statement = connector.getPreparedStatement(sql)){
            statement.setString(1, newSensor.getName());
            statement.setString(2, newSensor.getType());
            statement.setString(3, newSensor.getNumber());
            statement.setString(4, newSensor.getMeasurement());
            statement.setString(5, newSensor.getValue());
            statement.setString(6, newSensor.getErrorFormula());
            statement.setDouble(7, newSensor.getRangeMin());
            statement.setDouble(8, newSensor.getRangeMax());
            statement.setString(9, oldSensor.getName());

            return statement.executeUpdate() > 0;
        }catch (SQLException e){
            logger.warn("Exception was thrown!", e);
            return false;
        }
    }

    @Override
    public boolean changeMeasurementValue(@Nonnull String oldValue, @Nonnull String newValue) {
        String sql = String.format("UPDATE %s SET value = '%s' WHERE value = '%s';", tableName, newValue, oldValue);
        try (Statement statement = connector.getStatement()){
            statement.execute(sql);
            return true;
        } catch (SQLException e) {
            logger.warn("Exception was thrown!", e);
            return false;
        }
    }

    @Override
    public boolean removeMeasurementValue(@Nonnull String measurementValue) {
        String sql = String.format("UPDATE %s SET value = '' WHERE value = '%s';", tableName, measurementValue);
        try (Statement statement = connector.getStatement()){
            statement.execute(sql);
            return true;
        } catch (SQLException e) {
            logger.warn("Exception was thrown!", e);
            return false;
        }
    }

    @Override
    public boolean rewrite(@Nonnull Collection<Sensor> sensors) {
        String sql = String.format("DELETE FROM %s;", tableName);
        try (Statement statement = connector.getStatement()) {
            statement.execute(sql);

            if (!sensors.isEmpty()) {
                String insertSql = String.format("INSERT INTO %s (name, type, number, measurement, value, error_formula, range_min, range_max) "
                        + "VALUES ", tableName);
                StringBuilder sqlBuilder = new StringBuilder(insertSql);

                for (Sensor sensor : sensors) {
                    if (sensor == null) continue;
                    String values = String.format("('%s', '%s', '%s', '%s', '%s', '%s', %s, %s),",
                            sensor.getName(),
                            sensor.getType(),
                            sensor.getNumber(),
                            sensor.getMeasurement(),
                            sensor.getValue(),
                            sensor.getErrorFormula(),
                            sensor.getRangeMin(),
                            sensor.getRangeMax());
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
    public boolean isLastInMeasurement(@Nonnull Sensor sensor) {
        String sql = String.format("SELECT name FROM %s WHERE measurement = '%s';", tableName, sensor.getMeasurement());
        try (ResultSet resultSet = connector.getResultSet(sql)){
            int n = 0;
            while (resultSet.next()) if (++n > 1) return false;
            return true;
        }catch (SQLException e){
            logger.warn("Exception was thrown!", e);
            return true;
        }
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
    public boolean importData(@Nonnull Collection<Sensor> newSensors, @Nonnull Collection<Sensor> sensorsForChange) {
        if (!sensorsForChange.isEmpty()){
            for (Sensor s : sensorsForChange){
                if (s == null) continue;

                String sql = String.format("UPDATE %s SET "
                        + "type = ?, number = ?, measurement = ?, value = ?, error_formula = ?, range_min = ?, range_max = ? "
                        + "WHERE name = ?;", tableName);
                try (PreparedStatement statement = connector.getPreparedStatement(sql)){
                    statement.setString(1, s.getType());
                    statement.setString(2, s.getNumber());
                    statement.setString(3, s.getMeasurement());
                    statement.setString(4, s.getValue());
                    statement.setString(5, s.getErrorFormula());
                    statement.setDouble(6, s.getRangeMin());
                    statement.setDouble(7, s.getRangeMax());
                    statement.setString(8, s.getName());

                    statement.execute();
                } catch (SQLException e) {
                    logger.warn("Exception was thrown!", e);
                    return false;
                }
            }
        }

        if (!newSensors.isEmpty()){
            String sql = String.format("INSERT INTO %s (name, type, number, measurement, value, error_formula, range_min, range_max) "
                    + "VALUES ", tableName);
            StringBuilder sqlBuilder = new StringBuilder(sql);
            try (Statement statement = connector.getStatement()) {
                for (Sensor sensor : newSensors) {
                    if (sensor == null) continue;
                    String values = String.format("('%s', '%s', '%s', '%s', '%s', '%s', %s, %s),",
                            sensor.getName(), sensor.getType(), sensor.getNumber(), sensor.getMeasurement(), sensor.getValue(),
                            sensor.getErrorFormula(), sensor.getRangeMin(), sensor.getRangeMax());
                    sqlBuilder.append(values);
                }
                sqlBuilder.setCharAt(sqlBuilder.length()-1, ';');
                statement.execute(sqlBuilder.toString());
            } catch (SQLException e) {
                logger.warn("Exception was thrown!", e);
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean isExists(@Nonnull String sensorName) {
        String sql = String.format("SELECT name FROM %s WHERE name = '%s' LIMIT 1;", tableName, sensorName);
        try (ResultSet resultSet = connector.getResultSet(sql)){
            return resultSet.next();
        }catch (SQLException e){
            logger.warn("Exception was thrown!", e);
            return true;
        }
    }
}