package service.repository.repos.measurement;

import model.Measurement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

public class MeasurementRepositorySQLite implements MeasurementRepository {
    private static final Logger logger = LoggerFactory.getLogger(MeasurementRepositorySQLite.class);

    private final RepositoryDBConnector connector;
    private final String tableName;

    public MeasurementRepositorySQLite(RepositoryConfigHolder configHolder,
                                       RepositoryDBConnector connector){
        this.connector = connector;
        this.tableName = configHolder.getTableName(MeasurementRepository.class);
    }

    @Override
    public Collection<Measurement> getAll() {
        List<Measurement>measurements = new ArrayList<>();

        String sql = String.format("SELECT * FROM %s;", tableName);
        try (ResultSet resultSet = connector.getResultSet(sql)){
            while (resultSet.next()){
                String name = resultSet.getString("name");
                String value = resultSet.getString("value");

                measurements.add(new Measurement(name, value));
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
    public Measurement getByValue(@Nonnull String value) {
        String sql = String.format("SELECT * FROM %s WHERE value = '%s' LIMIT 1;", tableName, value);
        try (ResultSet resultSet = connector.getResultSet(sql)){
            if (resultSet.next()){
                String name = resultSet.getString("name");

                return new Measurement(name, value);
            }
        }catch (SQLException e){
            logger.warn("Exception was thrown!", e);
        }

        return null;
    }

    @Override
    public boolean add(@Nonnull Measurement measurement) {
        String sql = String.format("INSERT INTO %s (name, value) VALUES (?, ?);", tableName);
        try (PreparedStatement statement = connector.getPreparedStatement(sql)){
            statement.setString(1, measurement.getName());
            statement.setString(2, measurement.getValue());
            statement.execute();

            return true;
        }catch (SQLException e){
            logger.warn("Exception was thrown!", e);
            return false;
        }
    }

    @Override
    public boolean set(@Nonnull Measurement oldMeasurement, @Nonnull Measurement newMeasurement) {
        String sql = String.format("UPDATE %s SET name = ?, value = ? WHERE value = ?;", tableName);
        try (PreparedStatement statement = connector.getPreparedStatement(sql)){
            statement.setString(1, newMeasurement.getName());
            statement.setString(2, newMeasurement.getValue());
            statement.setString(3, oldMeasurement.getValue());

            return statement.executeUpdate() > 0;
        }catch (SQLException e){
            logger.warn("Exception was thrown!", e);
        }

        return false;
    }

    @Override
    public boolean remove(@Nonnull Measurement measurement) {
        String sql = String.format("DELETE FROM %s WHERE value = '%s';", tableName, measurement.getValue());
        try (Statement statement = connector.getStatement()){
            return statement.executeUpdate(sql) > 0;
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
    public Collection<Measurement> getMeasurementsByName(@Nonnull String name) {
        List<Measurement>measurements = new ArrayList<>();
        String sql = String.format("SELECT * FROM %s WHERE name = '%s';", tableName, name);
        try (ResultSet resultSet = connector.getResultSet(sql)){
            while (resultSet.next()){
                String value = resultSet.getString("value");

                measurements.add(new Measurement(name, value));
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
                String insertSql = String.format("INSERT INTO %s (name, value) VALUES ", tableName);
                StringBuilder sqlBuilder = new StringBuilder(insertSql);
                for (Measurement measurement : measurements) {
                    if (measurement == null) continue;

                    String values = String.format("('%s', '%s'),",
                            measurement.getName(), measurement.getValue());
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
        Measurement measurement = getByValue(measurementValue);
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
        if (!exists(oldValue)) return true;
        if (oldValue.equals(newValue)) return false;

        return exists(newValue);
    }
}