package repository.impl;

import def.DefaultControlPointsValues;
import model.ControlPointsValues;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.ControlPointsValuesRepository;
import repository.RepositoryJDBC;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ControlPointsValuesRepositorySQLite extends RepositoryJDBC implements ControlPointsValuesRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(ControlPointsValuesRepositorySQLite.class);

    public ControlPointsValuesRepositorySQLite(){
        setPropertiesFromFile();
        createTable();
    }
    public ControlPointsValuesRepositorySQLite(String dbUrl, String dbUser, String dbPassword){
        setProperties(dbUrl, dbUser, dbPassword);
        createTable();
    }

    @Override
    public boolean createTable(){
        String sql = "CREATE TABLE IF NOT EXISTS control_points ("
                + "id integer NOT NULL UNIQUE"
                + ", sensor_type text NOT NULL"
                + ", points text NOT NULL"
                + ", range_min real NOT NULL"
                + ", range_max real NOT NULL"
                + ", PRIMARY KEY (\"id\" AUTOINCREMENT)"
                + ");";
        try (Statement statement = getStatement()){
            statement.execute(sql);
            return isTableExists("control_points");
        }catch (SQLException e){
            LOGGER.warn("Exception was thrown!", e);
            return false;
        }
    }

    @Override
    public Collection<ControlPointsValues> getAll() {
        List<ControlPointsValues>values = new ArrayList<>();
        LOGGER.info("Reading all control_points from DB");
        String sql = "SELECT * FROM control_points;";
        try (ResultSet resultSet = getResultSet(sql)){
            while (resultSet.next()){
                ControlPointsValues cpv = new ControlPointsValues();
                cpv.setId(resultSet.getInt("id"));
                cpv.setSensorType(resultSet.getString("sensor_type"));
                cpv.setRangeMin(resultSet.getDouble("range_min"));
                cpv.setRangeMax(resultSet.getDouble("range_max"));
                cpv._setValuesFromString(resultSet.getString("points"));

                values.add(cpv);
            }
        }catch (SQLException e){
            LOGGER.warn("Exception was thrown!", e);
        }

        return values;
    }

    @Override
    public boolean add(@Nonnull ControlPointsValues cpv) {
        String sql = "INSERT INTO control_points (sensor_type, range_min, range_max, points) VALUES (?, ?, ?, ?);";
        try (PreparedStatement statement = getPreparedStatementWithKey(sql)){
            statement.setString(1, cpv.getSensorType());
            statement.setDouble(2, cpv.getRangeMin());
            statement.setDouble(3, cpv.getRangeMax());
            statement.setString(4, cpv._getValuesString());

            if (statement.executeUpdate() > 0){
                ResultSet key = statement.getGeneratedKeys();
                int id = key.next() ? key.getInt("id") : -1;
                if (id > 0) LOGGER.info("Control_points = {} was added successfully", cpv);
                return id > 0;
            }else {
                LOGGER.info("Control_points = {} not added", cpv);
                return false;
            }

        }catch (SQLException e){
            LOGGER.warn("Exception was thrown!", e);
            return false;
        }
    }

    @Override
    public boolean set(@Nonnull ControlPointsValues cpv, @Nonnull ControlPointsValues ignored) {
        String sql = "UPDATE control_points SET range_min = ?, range_max = ?, points = ? WHERE id = ?;";
        try (PreparedStatement statement = getPreparedStatement(sql)){
            statement.setDouble(1, cpv.getRangeMin());
            statement.setDouble(2, cpv.getRangeMax());
            statement.setString(3, cpv._getValuesString());
            statement.setInt(4, cpv.getId());

            int result = statement.executeUpdate();
            if (result > 0) LOGGER.info("Control_points with id = {} was updated by : {}", cpv.getId(), cpv);
            return result > 0;
        }catch (SQLException e){
            LOGGER.warn("Exception was thrown!", e);
            return false;
        }
    }

    @Override
    public List<ControlPointsValues> getBySensorType(@Nonnull String sensorType) {
        List<ControlPointsValues>values = new ArrayList<>();
        LOGGER.info("Reading from DB all control_points with sensor_type = {}", sensorType);
        String sql = "SELECT * FROM control_points WHERE sensor_type = '" + sensorType + "';";
        try (ResultSet resultSet = getResultSet(sql)){
            while (resultSet.next()){
                ControlPointsValues cpv = new ControlPointsValues();
                cpv.setId(resultSet.getInt("id"));
                cpv.setSensorType(resultSet.getString("sensor_type"));
                cpv.setRangeMin(resultSet.getDouble("range_min"));
                cpv.setRangeMax(resultSet.getDouble("range_max"));
                cpv._setValuesFromString(resultSet.getString("points"));

                values.add(cpv);
            }
        }catch (SQLException e){
            LOGGER.warn("Exception was thrown!", e);
        }

        return values;
    }

    @Override
    public ControlPointsValues getControlPointsValues(int id) {
        if (id <= 0) return null;

        String sql = "SELECT * FROM control_points WHERE id = " + id + " LIMIT 1;";
        LOGGER.info("Reading control_points from DB with id = {}", id);
        try (ResultSet resultSet = getResultSet(sql)){
            if (resultSet.next()){
                ControlPointsValues cpv = new ControlPointsValues();
                cpv.setId(resultSet.getInt("id"));
                cpv.setSensorType(resultSet.getString("sensor_type"));
                cpv.setRangeMin(resultSet.getDouble("range_min"));
                cpv.setRangeMax(resultSet.getDouble("range_max"));
                cpv._setValuesFromString(resultSet.getString("points"));

                return cpv;
            }else {
                LOGGER.info("Control points with id = {} not found", id);
                return null;
            }
        }catch (SQLException e){
            LOGGER.warn("Exception was thrown!", e);
            return null;
        }
    }

    @Override
    @Nullable
    public Integer addReturnId(@Nonnull ControlPointsValues cpv) {
        String sql = "INSERT INTO control_points (sensor_type, range_min, range_max, points) VALUES (?, ?, ?, ?);";
        try (PreparedStatement statement = getPreparedStatementWithKey(sql)){
            statement.setString(1, cpv.getSensorType());
            statement.setDouble(2, cpv.getRangeMin());
            statement.setDouble(3, cpv.getRangeMax());
            statement.setString(4, cpv._getValuesString());

            if (statement.executeUpdate() > 0){
                ResultSet key = statement.getGeneratedKeys();
                int id = key.next() ? key.getInt("id") : -1;
                if (id > 0) LOGGER.info("Control_points = {} was added successfully", cpv);
                return id;
            }else {
                LOGGER.info("Control_points = {} not added", cpv);
                return null;
            }

        }catch (SQLException e){
            LOGGER.warn("Exception was thrown!", e);
            return null;
        }
    }

    @Override
    public boolean set(@Nonnull ControlPointsValues cpv) {
        String sql = "UPDATE control_points SET range_min = ?, range_max = ?, points = ? WHERE id = ?;";
        try (PreparedStatement statement = getPreparedStatement(sql)){
            statement.setDouble(1, cpv.getRangeMin());
            statement.setDouble(2, cpv.getRangeMax());
            statement.setString(3, cpv._getValuesString());
            statement.setInt(4, cpv.getId());

            int result = statement.executeUpdate();
            if (result > 0) LOGGER.info("Control_points with id = {} was updated by : {}", cpv.getId(), cpv);
            return result > 0;
        }catch (SQLException e){
            LOGGER.warn("Exception was thrown!", e);
            return false;
        }
    }

    @Override
    public boolean changeSensorType(@Nonnull String oldSensorType, @Nonnull String newSensorType) {
        if (oldSensorType.equals(newSensorType)) return true;

        String sql = "UPDATE control_points SET "
                + "sensor_type = '" + newSensorType + "' "
                + "WHERE sensor_type = '" + oldSensorType + "';";
        try (Statement statement = getStatement()){
            statement.execute(sql);
            LOGGER.info("Sensor type in all control_points was updated from = {}, to = {}", oldSensorType, newSensorType);
            return true;
        } catch (SQLException e) {
            LOGGER.warn("Exception was thrown!", e);
            return false;
        }
    }

    @Override
    public boolean remove(@Nonnull ControlPointsValues cpv) {
        String sql = "DELETE FROM control_points WHERE id = " + cpv.getId() + ";";
        try (Statement statement = getStatement()){
            int result = statement.executeUpdate(sql);
            if (result > 0){
                LOGGER.info("Control_points with id = {} was removed successfully", cpv.getId());
            }else {
                LOGGER.info("Control_points with id = {} not found", cpv.getId());
            }
            return true;
        }catch (SQLException e){
            LOGGER.warn("Exception was thrown!", e);
            return false;
        }
    }

    @Override
    public boolean removeAll(@Nonnull String sensorType) {
        String sql = "DELETE FROM control_points WHERE sensor_type = '" + sensorType + "';";
        try (Statement statement = getStatement()){
            statement.execute(sql);

            LOGGER.info("All control_points with sensor_type = {} was removed", sensorType);
            return true;
        }catch (SQLException e){
            LOGGER.warn("Exception was thrown!", e);
            return false;
        }
    }

    @Override
    public boolean clear() {
        String sql = "DELETE FROM control_points;";
        try (Statement statement = getStatement()){
            statement.execute(sql);
            LOGGER.info("Control_points list in DB was cleared successfully");
            return true;
        }catch (SQLException e){
            LOGGER.warn("Exception was thrown!", e);
            return false;
        }
    }

    @Override
    public boolean rewrite(@Nonnull Collection<ControlPointsValues> list) {
        return false;
    }

    @Override
    public boolean resetToDefault() {
        String sql = "DELETE FROM control_points;";
        try (Statement statement = getStatement()) {
            statement.execute(sql);

            String insertSql = "INSERT INTO control_points (sensor_type, range_min, range_max, points) "
                    + "VALUES ";
            StringBuilder sqlBuilder = new StringBuilder(insertSql);

            for (ControlPointsValues cpv : DefaultControlPointsValues.get()) {
                sqlBuilder.append("('").append(cpv.getSensorType()).append("', ")
                        .append(cpv.getRangeMin()).append(", ")
                        .append(cpv.getRangeMax()).append(", ")
                        .append("'").append(cpv._getValuesString()).append("'),");
            }
            sqlBuilder.setCharAt(sqlBuilder.length() - 1, ';');
            statement.execute(sqlBuilder.toString());

            LOGGER.info("Control_points list in DB was reset to default list successfully");
            return true;
        }catch (SQLException e){
            LOGGER.warn("Exception was thrown!", e);
            return false;
        }
    }
}