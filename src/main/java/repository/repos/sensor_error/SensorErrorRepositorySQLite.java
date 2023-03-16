package repository.repos.sensor_error;

import model.dto.SensorError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.config.RepositoryConfigHolder;
import repository.connection.RepositoryDBConnector;
import repository.init.SensorErrorRepositoryInitializer;

import javax.annotation.Nonnull;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SensorErrorRepositorySQLite implements SensorErrorRepository {
    private static final Logger logger = LoggerFactory.getLogger(SensorErrorRepositorySQLite.class);

    private final RepositoryDBConnector connector;
    private final String tableName;

    public SensorErrorRepositorySQLite(RepositoryConfigHolder configHolder, RepositoryDBConnector connector){
        this.tableName = configHolder.getTableName(SensorErrorRepository.class);
        this.connector = connector;
        new SensorErrorRepositoryInitializer(configHolder, connector).init();
    }

    @Override
    public Collection<SensorError> getAll() {
        List<SensorError> result = new ArrayList<>();

        String sql = String.format("SELECT * FROM %s;", tableName);
        try (ResultSet resultSet = connector.getResultSet(sql)) {
            while (resultSet.next()) {
                String type = resultSet.getString("type");
                double rangeMin = resultSet.getDouble("range_min");
                double rangeMax = resultSet.getDouble("range_max");
                String measurementValue = resultSet.getString("measurement_value");
                String errorFormula = resultSet.getString("error_formula");

                result.add(SensorError.create(type, rangeMin, rangeMax, measurementValue, errorFormula));
            }
        } catch (SQLException e) {
            logger.warn("Exception was thrown.", e);
        }

        return result;
    }

    @Override
    public Collection<SensorError> getBySensorType(@Nonnull String sensorType) {
        List<SensorError> result = new ArrayList<>();

        String sql = String.format("SELECT * FROM %s WHERE type = '%s';", tableName, sensorType);
        try (ResultSet resultSet = connector.getResultSet(sql)) {
            while (resultSet.next()) {
                String type = resultSet.getString("type");
                double rangeMin = resultSet.getDouble("range_min");
                double rangeMax = resultSet.getDouble("range_max");
                String measurementValue = resultSet.getString("measurement_value");
                String errorFormula = resultSet.getString("error_formula");

                result.add(SensorError.create(type, rangeMin, rangeMax, measurementValue, errorFormula));
            }
        } catch (SQLException e) {
            logger.warn("Exception was thrown.", e);
        }

        return result;
    }

    @Override
    public boolean add(@Nonnull SensorError error) {
        String sql = String.format("INSERT INTO %s (id, type, range_min, range_max, measurement_value, error_formula) " +
                "VALUES (?, ?, ?, ?, ?, ?)", tableName);
        try (PreparedStatement statement = connector.getPreparedStatement(sql)) {
            statement.setString(1, error.getId());
            statement.setString(2, error.getType());
            statement.setDouble(3, error.getRangeMin());
            statement.setDouble(4, error.getRangeMax());
            statement.setString(5, error.getMeasurementValue());
            statement.setString(6, error.getErrorFormula());

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.warn("Exception was thrown.", e);
            return false;
        }
    }

    @Override
    public SensorError getById(@Nonnull String id) {
        String sql = String.format("SELECT * FROM %s WHERE id = '%s' LIMIT 1;", tableName, id);
        try (ResultSet resultSet = connector.getResultSet(sql)) {
            if (resultSet.next()) {
                String type = resultSet.getString("type");
                double rangeMin = resultSet.getDouble("range_min");
                double rangeMax = resultSet.getDouble("range_max");
                String measurementValue = resultSet.getString("measurement_value");
                String errorFormula = resultSet.getString("error_formula");

                return SensorError.create(type, rangeMin, rangeMax, measurementValue, errorFormula);
            }
        } catch (SQLException e) {
            logger.warn("Exception was thrown.", e);
        }

        return null;
    }

    @Override
    public boolean set(@Nonnull String oldId, @Nonnull SensorError newError) {
        String sql = String.format("UPDATE %s SET id = ?, type = ?, range_min = ?, range_max = ?, measurement_value = ?, error_formula = ? " +
                "WHERE id = ?", tableName);
        try (PreparedStatement statement = connector.getPreparedStatement(sql)) {
            statement.setString(1, newError.getId());
            statement.setString(2, newError.getType());
            statement.setDouble(3, newError.getRangeMin());
            statement.setDouble(4, newError.getRangeMax());
            statement.setString(5, newError.getMeasurementValue());
            statement.setString(6, newError.getErrorFormula());
            statement.setString(7, oldId);

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.warn("Exception was thrown.", e);
            return false;
        }
    }

    @Override
    public boolean removeById(@Nonnull String id) {
        try (Statement statement = connector.getStatement()) {
            String sql = String.format("DELETE FROM %s WHERE id = '%s';", tableName, id);
            return statement.executeUpdate(sql) > 0;
        } catch (SQLException e) {
            logger.warn("Exception was thrown.", e);
            return false;
        }
    }

    @Override
    public boolean removeBySensorType(@Nonnull String sensorType) {
        try (Statement statement = connector.getStatement()) {
            String sql = String.format("DELETE FROM %s WHERE type = '%s';", tableName, sensorType);
            statement.executeUpdate(sql);
            return true;
        } catch (SQLException e) {
            logger.warn("Exception was thrown.", e);
            return false;
        }
    }

    @Override
    public boolean removeByMeasurementValue(@Nonnull String value) {
        try (Statement statement = connector.getStatement()) {
            String sql = String.format("DELETE FROM %s WHERE measurement_value = '%s';", tableName, value);
            statement.executeUpdate(sql);
            return true;
        } catch (SQLException e) {
            logger.warn("Exception was thrown.", e);
            return false;
        }
    }

    @Override
    public boolean isExists(@Nonnull String id) {
        String sql = String.format("SELECT * FROM %s WHERE id = '%s' LIMIT 1;", tableName, id);
        try (ResultSet resultSet = connector.getResultSet(sql)) {
            return resultSet.next();
        } catch (SQLException e) {
            logger.warn("Exception was thrown.", e);
            return true;
        }
    }

    @Override
    public boolean isExists(String oldId, String newId) {
        if (oldId == null || newId == null || !isExists(oldId)) return true;
        if (oldId.equals(newId)) return false;
        return isExists(newId);
    }
}
