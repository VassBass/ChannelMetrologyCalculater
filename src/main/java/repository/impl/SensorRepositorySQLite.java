package repository.impl;

import model.Sensor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.RepositoryJDBC;
import repository.SensorRepository;

import javax.annotation.Nonnull;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SensorRepositorySQLite extends RepositoryJDBC implements SensorRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(SensorRepositorySQLite.class);
    private static SensorRepositorySQLite instance;

    private SensorRepositorySQLite(){
        setPropertiesFromFile();
        createTable();
    }

    public SensorRepositorySQLite(String dbUrl, String dbUser, String dbPassword){
        setProperties(dbUrl, dbUser, dbPassword);
        createTable();
    }

    public static SensorRepositorySQLite getInstance() {
        if (instance == null) instance = new SensorRepositorySQLite();
        return instance;
    }

    @Override
    public boolean createTable(){
        String sql = "CREATE TABLE IF NOT EXISTS sensors ("
                + "name text NOT NULL UNIQUE"
                + ", type text NOT NULL"
                + ", number text"
                + ", measurement text NOT NULL"
                + ", value text"
                + ", error_formula text NOT NULL"
                + ", range_min real NOT NULL"
                + ", range_max real NOT NULL"
                + ", PRIMARY KEY (\"name\")"
                + ");";
        try (Statement statement = getStatement()){
            statement.execute(sql);
            return isTableExists("sensors");
        }catch (SQLException e){
            LOGGER.warn("Exception was thrown!", e);
            return false;
        }
    }

    @Override
    public Collection<Sensor> getAll() {
        List<Sensor>sensors = new ArrayList<>();

        LOGGER.info("Reading all sensors from DB");
        String sql = "SELECT * FROM sensors;";
        try (ResultSet resultSet = getResultSet(sql)){
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
            LOGGER.warn("Exception was thrown!", e);
        }

        return sensors;
    }

    @Override
    public Collection<Sensor> getAll(@Nonnull String measurement) {
        List<Sensor>sensors = new ArrayList<>();

        LOGGER.info("Reading all sensors with measurement = {} from DB", measurement);
        String sql = "SELECT * FROM sensors WHERE measurement = '" + measurement + "';";
        try (ResultSet resultSet = getResultSet(sql)){
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
            LOGGER.warn("Exception was thrown!", e);
        }

        return sensors;
    }

    @Override
    public List<String> getAllTypes() {
        List<String>types = new ArrayList<>();
        LOGGER.info("Reading all sensors types from DB");
        String sql = "SELECT DISTINCT type FROM sensors";
        try (ResultSet resultSet = getResultSet(sql)){
            while (resultSet.next()){
                types.add(resultSet.getString("type"));
            }
        }catch (SQLException e){
            LOGGER.warn("Exception was thrown!", e);
        }

        return types;
    }

    @Override
    public String getMeasurement(@Nonnull String sensorType) {
        LOGGER.info("Reading sensor measurement by type = {} from DB", sensorType);
        String sql = "SELECT measurement FROM sensors WHERE type = '" + sensorType + "';";
        try (ResultSet resultSet = getResultSet(sql)){
            if (resultSet.next()){
                return resultSet.getString("measurement");
            }
        }catch (SQLException e){
            LOGGER.warn("Exception was thrown!", e);
        }

        return null;
    }

    @Override
    public List<String> getAllSensorsName(@Nonnull String measurementName) {
        List<String> names = new ArrayList<>();

        LOGGER.info("Reading all sensors names by measurement = {} from DB", measurementName);
        String sql = "SELECT name FROM sensors WHERE measurement = '" + measurementName + "';";
        try (ResultSet resultSet = getResultSet(sql)){
            names.add(resultSet.getString("name"));
        }catch (SQLException e){
            LOGGER.warn("Exception was thrown!", e);
        }

        return names;
    }

    @Override
    public Sensor get(@Nonnull String sensorName) {
        LOGGER.info("Reading sensor with name = {} from DB", sensorName);
        String sql = "SELECT * FROM sensors WHERE name = '" + sensorName + "' LIMIT 1;";
        try (ResultSet resultSet = getResultSet(sql)){
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
            LOGGER.warn("Exception was thrown!", e);
        }

        LOGGER.info("Sensor with name = {} not found", sensorName);
        return null;
    }

    @Override
    public boolean add(@Nonnull Sensor sensor) {
        String sql = "INSERT INTO sensors (name, type, number, measurement, value, error_formula, range_min, range_max) VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
        try (PreparedStatement statement = getPreparedStatement(sql)){
            statement.setString(1, sensor.getName());
            statement.setString(2, sensor.getType());
            statement.setString(3, sensor.getNumber());
            statement.setString(4, sensor.getMeasurement());
            statement.setString(5, sensor.getValue());
            statement.setString(6, sensor.getErrorFormula());
            statement.setDouble(7, sensor.getRangeMin());
            statement.setDouble(8, sensor.getRangeMax());

            int result = statement.executeUpdate();
            if (result > 0) LOGGER.info("Sensor = {} was added successfully", sensor.getName());
            return true;
        }catch (SQLException e){
            LOGGER.warn("Exception was thrown!", e);
            return false;
        }
    }

    @Override
    public boolean remove(@Nonnull Sensor sensor) {
        String sql = "DELETE FROM sensors WHERE name = '" + sensor.getName() + "';";
        try (Statement statement = getStatement()){
            int result = statement.executeUpdate(sql);
            if (result > 0){
                LOGGER.info("Sensor = {} was removed successfully", sensor.getName());
            }else {
                LOGGER.info("Sensor with name = {} not found", sensor.getName());
            }
            return true;
        }catch (SQLException e){
            LOGGER.warn("Exception was thrown!", e);
            return false;
        }
    }

    @Override
    public boolean set(@Nonnull Sensor oldSensor, @Nonnull Sensor newSensor) {
        if (oldSensor.isMatch(newSensor)) return true;

        String sql = "UPDATE sensors SET name = ?, type = ?, number = ?, measurement = ?, value = ?, error_formula = ?, "
                + "range_min = ?, range_max = ? WHERE name = ?;";
        try (PreparedStatement statement = getPreparedStatement(sql)){
            statement.setString(1, newSensor.getName());
            statement.setString(2, newSensor.getType());
            statement.setString(3, newSensor.getNumber());
            statement.setString(4, newSensor.getMeasurement());
            statement.setString(5, newSensor.getValue());
            statement.setString(6, newSensor.getErrorFormula());
            statement.setDouble(7, newSensor.getRangeMin());
            statement.setDouble(8, newSensor.getRangeMax());
            statement.setString(9, oldSensor.getName());

            int result = statement.executeUpdate();
            if (result > 0) LOGGER.info("Sensor:\n{}\nwas replaced by:\n{}\nsuccessfully", oldSensor, newSensor);
            return true;
        }catch (SQLException e){
            LOGGER.warn("Exception was thrown!", e);
            return false;
        }
    }

    @Override
    public boolean changeMeasurementValue(@Nonnull String oldValue, @Nonnull String newValue) {
        if (oldValue.equals(newValue)) return true;

        String sql = "UPDATE sensors SET measurement = '" + newValue + "' WHERE measurement = '" + oldValue + "';";
        try (Statement statement = getStatement()){
            int result = statement.executeUpdate(sql);
            LOGGER.info("Changed measurementValue of {} sensors from {} to {}", result, oldValue, newValue);
            return true;
        } catch (SQLException e) {
            LOGGER.warn("Exception was thrown!", e);
            return false;
        }
    }

    @Override
    public boolean removeMeasurementValue(@Nonnull String measurementValue) {
        String sql = "UPDATE sensors SET measurement = '' WHERE measurement = '" + measurementValue + "';";
        try (Statement statement = getStatement()){
            int result = statement.executeUpdate(sql);
            LOGGER.info("Measurement value = {} was removed from {} sensors",measurementValue, result);
            return true;
        } catch (SQLException e) {
            LOGGER.warn("Exception was thrown!", e);
            return false;
        }
    }

    @Override
    public boolean rewrite(@Nonnull Collection<Sensor> sensors) {
        String sql = "DELETE FROM sensors;";
        try (Statement statement = getStatement()) {
            statement.execute(sql);
            LOGGER.info("Sensors list in DB was cleared successfully");

            if (!sensors.isEmpty()) {
                String insertSql = "INSERT INTO sensors (name, type, number, measurement, value, error_formula, range_min, range_max) "
                        + "VALUES ";
                StringBuilder sqlBuilder = new StringBuilder(insertSql);

                for (Sensor sensor : sensors) {
                    sqlBuilder.append("('").append(sensor.getName()).append("', ")
                            .append("'").append(sensor.getType()).append("', ")
                            .append("'").append(sensor.getNumber()).append("', ")
                            .append("'").append(sensor.getMeasurement()).append("', ")
                            .append("'").append(sensor.getValue()).append("', ")
                            .append("'").append(sensor.getErrorFormula()).append("', ")
                            .append(sensor.getRangeMin()).append(", ")
                            .append(sensor.getRangeMax()).append("),");
                }
                sqlBuilder.setCharAt(sqlBuilder.length()-1, ';');
                statement.execute(sqlBuilder.toString());
            }

            LOGGER.info("The list of old sensors has been rewritten to the new one:\n{}", sensors);
            return true;
        } catch (SQLException e) {
            LOGGER.warn("Exception was thrown!", e);
            return false;
        }
    }

    @Override
    public boolean isLastInMeasurement(@Nonnull Sensor sensor) {
        String sql = "SELECT name FROM sensors WHERE measurement = '" + sensor.getMeasurement() + "';";
        try (ResultSet resultSet = getResultSet(sql)){
            int n = 0;
            while (resultSet.next()) {
                if (++n > 1) {
                    return false;
                }
            }
            return true;
        }catch (SQLException e){
            LOGGER.warn("Exception was thrown!", e);
            return true;
        }
    }

    @Override
    public boolean clear() {
        String sql = "DELETE FROM sensors;";
        try (Statement statement = getStatement()){
            statement.execute(sql);
            LOGGER.info("Sensors list in DB was cleared successfully");
            return true;
        }catch (SQLException e){
            LOGGER.warn("Exception was thrown!", e);
            return false;
        }
    }

    @Override
    public boolean importData(@Nonnull Collection<Sensor> newSensors, @Nonnull Collection<Sensor> sensorsForChange) {
        int changeResult = 0;
        int addResult = 0;
        if (!sensorsForChange.isEmpty()){
            for (Sensor s : sensorsForChange){
                String sql = "UPDATE sensors SET "
                        + "type = ?, number = ?, measurement = ?, value = ?, error_formula = ?, range_min = ?, range_max = ? "
                        + "WHERE name = ?;";
                try (PreparedStatement statement = getPreparedStatement(sql)){
                    statement.setString(1, s.getType());
                    statement.setString(2, s.getNumber());
                    statement.setString(3, s.getMeasurement());
                    statement.setString(4, s.getValue());
                    statement.setString(5, s.getErrorFormula());
                    statement.setDouble(6, s.getRangeMin());
                    statement.setDouble(7, s.getRangeMax());

                    statement.setString(8, s.getName());

                    statement.execute();
                    changeResult++;
                } catch (SQLException e) {
                    LOGGER.warn("Exception was thrown!", e);
                    return false;
                }
            }
        }

        if (!newSensors.isEmpty()){
            String sql = "INSERT INTO sensors (name, type, number, measurement, value, error_formula, range_min, range_max) "
                    + "VALUES ";
            StringBuilder sqlBuilder = new StringBuilder(sql);
            try (Statement statement = getStatement()) {
                for (Sensor sensor : newSensors) {
                    sqlBuilder.append("('").append(sensor.getName()).append("', ")
                            .append("'").append(sensor.getType()).append("', ")
                            .append("'").append(sensor.getNumber()).append("', ")
                            .append("'").append(sensor.getMeasurement()).append("', ")
                            .append("'").append(sensor.getValue()).append("', ")
                            .append("'").append(sensor.getErrorFormula()).append("', ")
                            .append(sensor.getRangeMin()).append(", ")
                            .append(sensor.getRangeMax()).append("),");
                }
                sqlBuilder.setCharAt(sqlBuilder.length()-1, ';');
                addResult = statement.executeUpdate(sqlBuilder.toString());
            } catch (SQLException e) {
                LOGGER.warn("Exception was thrown!", e);
                return false;
            }
        }

        LOGGER.info("Sensors import was successful");
        LOGGER.info("Changed = {} | Added = {}", changeResult, addResult);
        return true;
    }

    @Override
    public boolean isExists(@Nonnull String sensorName) {
        String sql = "SELECT name FROM sensors WHERE name = '" + sensorName + "' LIMIT 1;";
        try (ResultSet resultSet = getResultSet(sql)){
            return resultSet.next();
        }catch (SQLException e){
            LOGGER.warn("Exception was thrown!", e);
            return true;
        }
    }
}