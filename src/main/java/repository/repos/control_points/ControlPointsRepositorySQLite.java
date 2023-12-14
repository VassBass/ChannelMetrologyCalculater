package repository.repos.control_points;

import localization.Messages;
import model.dto.ControlPoints;
import model.dto.builder.ControlPointsBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.config.RepositoryConfigHolder;
import repository.connection.RepositoryDBConnector;
import repository.init.ControlPointsRepositoryInitializer;
import service.json.JacksonJsonObjectMapper;
import service.json.JsonObjectMapper;

import javax.annotation.Nonnull;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

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
                        .setPoints(jsonMapper.jsonToDoubleMap(jsonPoints))
                        .build();
                controlPoints.add(cp);
            }
        }catch (SQLException e){
            logger.warn(Messages.Log.EXCEPTION_THROWN, e);
        }

        return controlPoints;
    }

    @Override
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
                        .setPoints(jsonMapper.jsonToDoubleMap(jsonPoints))
                        .build();
                controlPoints.add(cp);
            }
        }catch (SQLException e) {
            logger.warn(Messages.Log.EXCEPTION_THROWN, e);
        }

        return controlPoints;
    }

    @Override
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
                        .setPoints(jsonMapper.jsonToDoubleMap(jsonPoints))
                        .build();
            }
        }catch (SQLException e){
            logger.warn(Messages.Log.EXCEPTION_THROWN, e);
        }

        return null;
    }

    @Override
    public boolean add(@Nonnull ControlPoints controlPoints) {
        String sql = String.format("INSERT INTO %s (name, sensor_type, points) VALUES (?, ?, ?);", tableName);
        try (PreparedStatement statement = connector.getPreparedStatementWithKey(sql)){
            statement.setString(1, controlPoints.getName());
            statement.setString(2, controlPoints.getSensorType());
            statement.setString(3, jsonMapper.doubleMapToJson(controlPoints.getValues()));

            return statement.executeUpdate() > 0;
        }catch (SQLException e){
            logger.warn(Messages.Log.EXCEPTION_THROWN, e);
            return false;
        }
    }

    @Override
    public boolean set(@Nonnull ControlPoints oldControlPoints, @Nonnull ControlPoints newControlPoints) {
        String sql = String.format("UPDATE %s SET name = ?, sensor_type = ?, points = ? WHERE name = ?;", tableName);
        try (PreparedStatement statement = connector.getPreparedStatement(sql)){
            statement.setString(1, newControlPoints.getName());
            statement.setString(2, newControlPoints.getSensorType());
            statement.setString(3, jsonMapper.doubleMapToJson(newControlPoints.getValues()));
            statement.setString(4, oldControlPoints.getName());

            return statement.executeUpdate() > 0;
        }catch (SQLException e){
            logger.warn(Messages.Log.EXCEPTION_THROWN, e);
            return false;
        }
    }

    @Override
    public boolean changeSensorType(@Nonnull String oldSensorType, @Nonnull String newSensorType) {
        String regex = "^.*(?=\\s\\[)";
        Collection<ControlPoints> all = getAll();
        all.forEach(cp -> {
            if (cp.getSensorType().equals(oldSensorType)) {
                cp.setName(cp.getName().replaceAll(regex, newSensorType));
                cp.setSensorType(newSensorType);
            }
        });
        return rewrite(all);
    }

    @Override
    public boolean removeByName(@Nonnull String name) {
        String sql = String.format("DELETE FROM %s WHERE name = '%s';", tableName, name);
        try (Statement statement = connector.getStatement()){
            return statement.executeUpdate(sql) > 0;
        }catch (SQLException e){
            logger.warn(Messages.Log.EXCEPTION_THROWN, e);
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
            logger.warn(Messages.Log.EXCEPTION_THROWN, e);
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
            logger.warn(Messages.Log.EXCEPTION_THROWN, e);
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
                            jsonMapper.doubleMapToJson(cp.getValues()));
                    sqlBuilder.append(values);
                }
                sqlBuilder.setCharAt(sqlBuilder.length() - 1, ';');
                statement.execute(sqlBuilder.toString());
            }

            return true;
        }catch (SQLException e){
            logger.warn(Messages.Log.EXCEPTION_THROWN, e);
            return false;
        }
    }
}