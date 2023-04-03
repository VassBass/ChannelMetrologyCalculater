package repository.repos.calculation_method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.config.RepositoryConfigHolder;
import repository.connection.RepositoryDBConnector;
import repository.init.CalculationMethodRepositoryInitializer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class CalculationMethodRepositorySQLite implements  CalculationMethodRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(CalculationMethodRepositorySQLite.class);

    private final String tableName;
    private final RepositoryDBConnector connector;

    public CalculationMethodRepositorySQLite(RepositoryConfigHolder configHolder, RepositoryDBConnector connector) {
        this.tableName = configHolder.getTableName(CalculationMethodRepository.class);
        this.connector = connector;
        new CalculationMethodRepositoryInitializer(configHolder, connector).init();
    }

    @Override
    public Map<String, String> getAll() {
        Map<String, String> methods = new HashMap<>();
        String sql = String.format("SELECT * FROM %s;", tableName);
        try (ResultSet resultSet = connector.getResultSet(sql)){
            while (resultSet.next()){
                String measurementName = resultSet.getString("measurement_name");
                String methodName = resultSet.getString("method_name");
                methods.put(measurementName, methodName);
            }
        }catch (SQLException e){
            LOGGER.warn("Exception was thrown!", e);
        }

        return methods;
    }

    @Override
    public String getMethodNameByMeasurementName(String measurementName) {
        String sql = String.format("SELECT method_name FROM %s WHERE measurement_name = '%s' LIMIT 1;", tableName, measurementName);
        try (ResultSet resultSet = connector.getResultSet(sql)){
            if (resultSet.next()){
                return resultSet.getString("method_name");
            }
        }catch (SQLException e){
            LOGGER.warn("Exception was thrown!", e);
        }
        return null;
    }

    @Override
    public boolean set(String measurementName, String methodName) {
        String sql = String.format("UPDATE %s SET method_name = '%s' WHERE measurement_name = '%s';", tableName, methodName, measurementName);
        try (Statement statement = connector.getStatement()){
                return statement.executeUpdate(sql) > 0;
        }catch (SQLException e){
            LOGGER.warn("Exception was thrown!", e);
            return false;
        }
    }

    @Override
    public boolean add(String measurementName, String methodName) {
        String sql = String.format("INSERT INTO %s (measurement_name, method_name) VALUES('%s', '%s');", tableName, measurementName, methodName);
        try (Statement statement = connector.getStatement()){
            return statement.executeUpdate(sql) > 0;
        }catch (SQLException e){
            LOGGER.warn("Exception was thrown!", e);
            return false;
        }
    }

    @Override
    public boolean removeByMeasurementName(String measurementName) {
        String sql = String.format("DELETE FROM %s WHERE measurement_name = '%s';", tableName, measurementName);
        try (Statement statement = connector.getStatement()){
            return statement.executeUpdate(sql) > 0;
        }catch (SQLException e){
            LOGGER.warn("Exception was thrown!", e);
            return false;
        }
    }
}
