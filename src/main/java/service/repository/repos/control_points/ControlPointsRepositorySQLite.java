package service.repository.repos.control_points;

import model.dto.ControlPoints;
import model.dto.builder.ControlPointsBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.json.JacksonJsonObjectMapper;
import service.json.JsonObjectMapper;
import service.repository.config.RepositoryConfigHolder;
import service.repository.connection.RepositoryDBConnector;
import service.repository.init.ControlPointsRepositoryInitializer;

import javax.annotation.Nonnull;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class ControlPointsRepositorySQLite implements ControlPointsRepository {
    private static final Logger logger = LoggerFactory.getLogger(ControlPointsRepositorySQLite.class);

    private final String tableName;
    private final RepositoryDBConnector connector;

    private final JsonObjectMapper jsonMapper = JacksonJsonObjectMapper.getInstance();

    public ControlPointsRepositorySQLite(RepositoryConfigHolder configHolder, RepositoryDBConnector connector) {
        this.tableName = configHolder.getTableName(ControlPointsRepository.class);
        this.connector = connector;
        new ControlPointsRepositoryInitializer(configHolder, connector).init();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Collection<ControlPoints> getAll() {
        Collection<ControlPoints> controlPoints = new ArrayList<>();
        String sql = String.format("SELECT * FROM %s;", tableName);
        try (ResultSet resultSet = connector.getResultSet(sql)){
            while (resultSet.next()){
                String name = resultSet.getString("name");
                String sensorType = resultSet.getString("sensor_type");
                String jsonPoints = resultSet.getString("points");

                ControlPoints cp = new ControlPointsBuilder()
                        .setName(name)
                        .setSensorType(sensorType)
                        .setPoints(jsonMapper.JsonToObject(jsonPoints, Map.class))
                        .build();
                controlPoints.add(cp);
            }
        }catch (SQLException e){
            logger.warn("Exception was thrown!", e);
        }

        return controlPoints;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Collection<ControlPoints> getAllBySensorType(@Nonnull String sensorType) {
        Collection<ControlPoints> controlPoints = new ArrayList<>();
        String sql = String.format("SELECT * FROM %s WHERE sensor_type = '%s';", tableName, sensorType);
        try (ResultSet resultSet = connector.getResultSet(sql)) {
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                String senType = resultSet.getString("sensor_type");
                String jsonPoints = resultSet.getString("points");

                ControlPoints cp = new ControlPointsBuilder(name)
                        .setSensorType(senType)
                        .setPoints(jsonMapper.JsonToObject(jsonPoints, Map.class))
                        .build();
                controlPoints.add(cp);
            }
        }catch (SQLException e) {
            logger.warn("Exception was thrown!", e);
        }

        return controlPoints;
    }

    @Override
    @SuppressWarnings("unchecked")
    public ControlPoints get(@Nonnull String name) {
        String sql = String.format("SELECT * FROM %s WHERE name = '%s' LIMIT 1;", tableName, name);
        try (ResultSet resultSet = connector.getResultSet(sql)){
            if (resultSet.next()){
                String n = resultSet.getString("name");
                String sensorType = resultSet.getString("sensor_type");
                String jsonPoints = resultSet.getString("points");

                return new ControlPointsBuilder()
                        .setName(n)
                        .setSensorType(sensorType)
                        .setPoints(jsonMapper.JsonToObject(jsonPoints, Map.class))
                        .build();
            }
        }catch (SQLException e){
            logger.warn("Exception was thrown!", e);
        }

        return null;
    }

    @Override
    public boolean add(@Nonnull ControlPoints controlPoints) {
        String sql = String.format("INSERT INTO %s (name, sensor_type, points) VALUES (?, ?, ?);", tableName);
        try (PreparedStatement statement = connector.getPreparedStatementWithKey(sql)){
            statement.setString(1, controlPoints.getName());
            statement.setString(2, controlPoints.getSensorType());
            statement.setString(3, jsonMapper.objectToJson(controlPoints.getValues()));

            return statement.executeUpdate() > 0;
        }catch (SQLException e){
            logger.warn("Exception was thrown!", e);
            return false;
        }
    }

    @Override
    public boolean set(@Nonnull ControlPoints oldControlPoints, @Nonnull ControlPoints newControlPoints) {
        String sql = String.format("UPDATE %s SET name = ?, sensor_type = ?, points = ? WHERE name = ?;", tableName);
        try (PreparedStatement statement = connector.getPreparedStatement(sql)){
            statement.setString(1, newControlPoints.getName());
            statement.setString(2, newControlPoints.getSensorType());
            statement.setString(3, jsonMapper.objectToJson(newControlPoints.getValues()));
            statement.setString(4, oldControlPoints.getName());

            return statement.executeUpdate() > 0;
        }catch (SQLException e){
            logger.warn("Exception was thrown!", e);
            return false;
        }
    }

    @Override
    public boolean changeSensorType(@Nonnull String oldSensorType, @Nonnull String newSensorType) {
        String sql = String.format("UPDATE %s SET sensor_type = '%s' WHERE sensor_type = '%s';",
                tableName, newSensorType, oldSensorType);
        try (Statement statement = connector.getStatement()){
            statement.execute(sql);
            return true;
        } catch (SQLException e) {
            logger.warn("Exception was thrown!", e);
            return false;
        }
    }

    @Override
    public boolean removeByName(@Nonnull String name) {
        String sql = String.format("DELETE FROM %s WHERE name = '%s';", tableName, name);
        try (Statement statement = connector.getStatement()){
            return statement.executeUpdate(sql) > 0;
        }catch (SQLException e){
            logger.warn("Exception was thrown!", e);
            return false;
        }
    }

    @Override
    public boolean removeBySensorType(@Nonnull String sensorType) {
        String sql = String.format("DELETE FROM %s WHERE sensor_type = '%s';", tableName, sensorType);
        try (Statement statement = connector.getStatement()){
            statement.execute(sql);
            return true;
        }catch (SQLException e){
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
    public boolean rewrite(@Nonnull Collection<ControlPoints> list) {
        String sql = String.format("DELETE FROM %s;", tableName);
        try (Statement statement = connector.getStatement()) {
            statement.execute(sql);

            if (!list.isEmpty()) {
                String insertSql = String.format("INSERT INTO %s (name, sensor_type, points) VALUES ", tableName);
                StringBuilder sqlBuilder = new StringBuilder(insertSql);
                for (ControlPoints cp : list) {
                    if (cp == null) continue;
                    String values = String.format("('%s', '%s', '%s'),",
                            cp.getName(),
                            cp.getSensorType(),
                            jsonMapper.objectToJson(cp.getValues()));
                    sqlBuilder.append(values);
                }
                sqlBuilder.setCharAt(sqlBuilder.length() - 1, ';');
                statement.execute(sqlBuilder.toString());
            }

            return true;
        }catch (SQLException e){
            logger.warn("Exception was thrown!", e);
            return false;
        }
    }
}