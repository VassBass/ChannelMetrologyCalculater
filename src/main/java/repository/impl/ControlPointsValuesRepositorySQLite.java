package repository.impl;

import model.ControlPointsValues;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.ControlPointsValuesRepository;
import repository.RepositoryJDBC;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

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
    public Optional<ControlPointsValues> getById(@Nonnegative int id) {
        String sql = "SELECT * FROM control_points WHERE id = " + id + " LIMIT 1;";
        try (ResultSet resultSet = getResultSet(sql)){
            if (resultSet.next()){
                ControlPointsValues cpv = new ControlPointsValues();
                cpv.setId(resultSet.getInt("id"));
                cpv.setSensorType(resultSet.getString("sensor_type"));
                cpv.setRangeMin(resultSet.getDouble("range_min"));
                cpv.setRangeMax(resultSet.getDouble("range_max"));
                cpv._setValuesFromString(resultSet.getString("points"));

                return Optional.of(cpv);
            }
        }catch (SQLException e){
            LOGGER.warn("Exception was thrown!", e);
        }

        return Optional.empty();
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
                Integer id = key.next() ? key.getInt(1) : null;
                if (id != null) {
                    cpv.setId(id);
                    return true;
                }
            }

            return false;
        }catch (SQLException e){
            LOGGER.warn("Exception was thrown!", e);
            return false;
        }
    }

    @Override
    public boolean set(@Nonnull ControlPointsValues cpv, @Nullable ControlPointsValues ignored) {
        String sql = "UPDATE control_points SET sensor_type = ?, range_min = ?, range_max = ?, points = ? WHERE id = ?;";
        try (PreparedStatement statement = getPreparedStatement(sql)){
            statement.setString(1, cpv.getSensorType());
            statement.setDouble(2, cpv.getRangeMin());
            statement.setDouble(3, cpv.getRangeMax());
            statement.setString(4, cpv._getValuesString());
            statement.setInt(5, cpv.getId());

            return statement.executeUpdate() > 0;
        }catch (SQLException e){
            LOGGER.warn("Exception was thrown!", e);
            return false;
        }
    }

    @Override
    public List<ControlPointsValues> getBySensorType(@Nonnull String sensorType) {
        List<ControlPointsValues>values = new ArrayList<>();
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
    @Nullable
    public Optional<Integer> addReturnId(@Nonnull ControlPointsValues cpv) {
        String sql = "INSERT INTO control_points (sensor_type, range_min, range_max, points) VALUES (?, ?, ?, ?);";
        try (PreparedStatement statement = getPreparedStatementWithKey(sql)){
            statement.setString(1, cpv.getSensorType());
            statement.setDouble(2, cpv.getRangeMin());
            statement.setDouble(3, cpv.getRangeMax());
            statement.setString(4, cpv._getValuesString());

            if (statement.executeUpdate() > 0){
                ResultSet key = statement.getGeneratedKeys();
                Integer id = key.next() ? key.getInt(1) : null;
                if (id != null) {
                    cpv.setId(id);
                    return Optional.of(id);
                }
            }

            return Optional.empty();
        }catch (SQLException e){
            LOGGER.warn("Exception was thrown!", e);
            return Optional.empty();
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

            return statement.executeUpdate() > 0;
        }catch (SQLException e){
            LOGGER.warn("Exception was thrown!", e);
            return false;
        }
    }

    @Override
    public boolean changeSensorType(@Nonnull String oldSensorType, @Nonnull String newSensorType) {
        String sql = "UPDATE control_points SET "
                + "sensor_type = '" + newSensorType + "' "
                + "WHERE sensor_type = '" + oldSensorType + "';";
        try (Statement statement = getStatement()){
            statement.execute(sql);
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
            return statement.executeUpdate(sql) > 0;
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
            return true;
        }catch (SQLException e){
            LOGGER.warn("Exception was thrown!", e);
            return false;
        }
    }

    @Override
    public boolean rewrite(@Nonnull Collection<ControlPointsValues> list) {
        String sql = "DELETE FROM control_points;";
        try (Statement statement = getStatement()) {
            statement.execute(sql);

            if (!list.isEmpty()) {
                String insertSql = "INSERT INTO control_points (sensor_type, range_min, range_max, points) "
                        + "VALUES ";
                StringBuilder sqlBuilder = new StringBuilder(insertSql);

                for (ControlPointsValues cpv : list) {
                    if (cpv == null) continue;

                    sqlBuilder.append("('").append(cpv.getSensorType()).append("', ")
                            .append(cpv.getRangeMin()).append(", ")
                            .append(cpv.getRangeMax()).append(", ")
                            .append("'").append(cpv._getValuesString()).append("'),");
                }
                sqlBuilder.setCharAt(sqlBuilder.length() - 1, ';');
                statement.execute(sqlBuilder.toString());
            }

            return true;
        }catch (SQLException e){
            LOGGER.warn("Exception was thrown!", e);
            return false;
        }
    }
}