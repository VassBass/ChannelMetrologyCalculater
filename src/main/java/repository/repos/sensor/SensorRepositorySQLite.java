package repository.repos.sensor;

import model.dto.Sensor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.config.RepositoryConfigHolder;
import repository.connection.RepositoryDBConnector;
import repository.init.SensorRepositoryInitializer;

import javax.annotation.Nonnull;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
                sensor.setChannelCode(resultSet.getString("channel_code"));
                sensor.setType(resultSet.getString("type"));
                sensor.setSerialNumber(resultSet.getString("serial_number"));
                sensor.setMeasurementName(resultSet.getString("measurement_name"));
                sensor.setMeasurementValue(resultSet.getString("measurement_value"));
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

        String sql = String.format("SELECT * FROM %s WHERE measurement_name = '%s';", tableName, measurement);
        try (ResultSet resultSet = connector.getResultSet(sql)){
            while (resultSet.next()){
                Sensor sensor = new Sensor();
                sensor.setChannelCode(resultSet.getString("channel_code"));
                sensor.setType(resultSet.getString("type"));
                sensor.setSerialNumber(resultSet.getString("serial_number"));
                sensor.setMeasurementName(resultSet.getString("measurement_name"));
                sensor.setMeasurementValue(resultSet.getString("measurement_value"));
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
    public Collection<String> getAllSensorsTypesByMeasurementName(@Nonnull String measurementName) {
        List<String> names = new ArrayList<>();

        String sql = String.format("SELECT DISTINCT type FROM %s WHERE measurement_name = '%s';", tableName, measurementName);
        try (ResultSet resultSet = connector.getResultSet(sql)){
            while (resultSet.next()) {
                String type = resultSet.getString("type");
                if (type == null) continue;
                names.add(type);
            }
        }catch (SQLException e){
            logger.warn("Exception was thrown!", e);
        }

        return names;
    }

    @Override
    public Sensor get(@Nonnull String channelCode) {
        String sql = String.format("SELECT * FROM %s WHERE channel_code = '%s' LIMIT 1;", tableName, channelCode);
        try (ResultSet resultSet = connector.getResultSet(sql)){
            if (resultSet.next()){
                Sensor sensor = new Sensor();
                sensor.setChannelCode(resultSet.getString("channel_code"));
                sensor.setType(resultSet.getString("type"));
                sensor.setSerialNumber(resultSet.getString("serial_number"));
                sensor.setMeasurementName(resultSet.getString("measurement_name"));
                sensor.setMeasurementValue(resultSet.getString("measurement_value"));
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
        String sql = String.format("INSERT INTO %s (channel_code, type, serial_number, measurement_name, measurement_value, " +
                        "error_formula, range_min, range_max) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?);",
                tableName);
        try (PreparedStatement statement = connector.getPreparedStatement(sql)){
            statement.setString(1, sensor.getChannelCode());
            statement.setString(2, sensor.getType());
            statement.setString(3, sensor.getSerialNumber());
            statement.setString(4, sensor.getMeasurementName());
            statement.setString(5, sensor.getMeasurementValue());
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
        String sql = String.format("DELETE FROM %s WHERE channel_code = '%s';", tableName, sensor.getChannelCode());
        try (Statement statement = connector.getStatement()){
            return statement.executeUpdate(sql) > 0;
        }catch (SQLException e){
            logger.warn("Exception was thrown!", e);
            return false;
        }
    }

    @Override
    public boolean removeByChannelCode(@Nonnull String channelCode) {
        String sql = String.format("DELETE FROM %s WHERE channel_code = '%s';", tableName, channelCode);
        try (Statement statement = connector.getStatement()){
            return statement.executeUpdate(sql) > 0;
        }catch (SQLException e){
            logger.warn("Exception was thrown!", e);
            return false;
        }
    }

    @Override
    public boolean set(@Nonnull Sensor oldSensor, @Nonnull Sensor newSensor) {
        String sql = String.format("UPDATE %s SET " +
                "channel_code = ?, type = ?, serial_number = ?, measurement_name = ?, measurement_value = ?, error_formula = ?" +
                ", range_min = ?, range_max = ? WHERE channel_code = ?" +
                ";", tableName);
        try (PreparedStatement statement = connector.getPreparedStatement(sql)){
            statement.setString(1, newSensor.getChannelCode());
            statement.setString(2, newSensor.getType());
            statement.setString(3, newSensor.getSerialNumber());
            statement.setString(4, newSensor.getMeasurementName());
            statement.setString(5, newSensor.getMeasurementValue());
            statement.setString(6, newSensor.getErrorFormula());
            statement.setDouble(7, newSensor.getRangeMin());
            statement.setDouble(8, newSensor.getRangeMax());
            statement.setString(9, oldSensor.getChannelCode());

            return statement.executeUpdate() > 0;
        }catch (SQLException e){
            logger.warn("Exception was thrown!", e);
            return false;
        }
    }

    @Override
    public boolean changeMeasurementValue(@Nonnull String oldValue, @Nonnull String newValue) {
        String sql = String.format("UPDATE %s SET measurement_value = '%s' WHERE measurement_value = '%s';", tableName, newValue, oldValue);
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
                String insertSql = String.format("INSERT INTO %s (channel_code, type, serial_number, " +
                        "measurement_name, measurement_value, error_formula, range_min, range_max) "
                        + "VALUES ", tableName);
                StringBuilder sqlBuilder = new StringBuilder(insertSql);

                for (Sensor sensor : sensors) {
                    if (sensor == null) continue;
                    String values = String.format("('%s', '%s', '%s', '%s', '%s', '%s', %s, %s),",
                            sensor.getChannelCode(),
                            sensor.getType(),
                            sensor.getSerialNumber(),
                            sensor.getMeasurementName(),
                            sensor.getMeasurementValue(),
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
                        + "type = ?, serial_number = ?, measurement_name = ?, measurement_value = ?, error_formula = ?, " +
                        "range_min = ?, range_max = ? "
                        + "WHERE channel_code = ?;", tableName);
                try (PreparedStatement statement = connector.getPreparedStatement(sql)){
                    statement.setString(1, s.getType());
                    statement.setString(2, s.getSerialNumber());
                    statement.setString(3, s.getMeasurementName());
                    statement.setString(4, s.getMeasurementValue());
                    statement.setString(5, s.getErrorFormula());
                    statement.setDouble(6, s.getRangeMin());
                    statement.setDouble(7, s.getRangeMax());
                    statement.setString(8, s.getChannelCode());

                    statement.execute();
                } catch (SQLException e) {
                    logger.warn("Exception was thrown!", e);
                    return false;
                }
            }
        }

        if (!newSensors.isEmpty()){
            String sql = String.format("INSERT INTO %s (channel_code, type, serial_number, measurement_name, measurement_value, " +
                    "error_formula, range_min, range_max) "
                    + "VALUES ", tableName);
            StringBuilder sqlBuilder = new StringBuilder(sql);
            try (Statement statement = connector.getStatement()) {
                for (Sensor sensor : newSensors) {
                    if (sensor == null) continue;
                    String values = String.format("('%s', '%s', '%s', '%s', '%s', '%s', %s, %s),",
                            sensor.getChannelCode(), sensor.getType(), sensor.getSerialNumber(), sensor.getMeasurementName(),
                            sensor.getMeasurementValue(), sensor.getErrorFormula(), sensor.getRangeMin(), sensor.getRangeMax());
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
    public boolean isExists(@Nonnull String channelCode) {
        String sql = String.format("SELECT channel_code FROM %s WHERE channel_code = '%s' LIMIT 1;", tableName, channelCode);
        try (ResultSet resultSet = connector.getResultSet(sql)){
            return resultSet.next();
        }catch (SQLException e){
            logger.warn("Exception was thrown!", e);
            return true;
        }
    }
}