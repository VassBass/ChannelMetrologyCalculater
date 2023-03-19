package mock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.RepositoryFactory;
import repository.config.RepositoryConfigHolder;
import repository.connection.RepositoryDBConnector;
import repository.connection.SqliteRepositoryDBConnector;
import repository.repos.area.AreaRepository;
import repository.repos.area.BufferedAreaRepositorySQLite;
import repository.repos.calibrator.BufferedCalibratorRepositorySQLite;
import repository.repos.calibrator.CalibratorRepository;
import repository.repos.channel.BufferedChannelRepositorySQLite;
import repository.repos.channel.ChannelRepository;
import repository.repos.control_points.BufferedControlPointsRepositorySQLite;
import repository.repos.control_points.ControlPointsRepository;
import repository.repos.department.BufferedDepartmentRepositorySQLite;
import repository.repos.department.DepartmentRepository;
import repository.repos.installation.BufferedInstallationRepositorySQLite;
import repository.repos.installation.InstallationRepository;
import repository.repos.measurement.BufferedMeasurementRepositorySQLite;
import repository.repos.measurement.MeasurementRepository;
import repository.repos.measurement_factor.BufferedMeasurementFactorRepositorySQLite;
import repository.repos.measurement_factor.MeasurementFactorRepository;
import repository.repos.person.BufferedPersonRepositorySQLite;
import repository.repos.person.PersonRepository;
import repository.repos.process.BufferedProcessRepositorySQLite;
import repository.repos.process.ProcessRepository;
import repository.repos.sensor.BufferedSensorRepositorySQLite;
import repository.repos.sensor.SensorRepository;
import repository.repos.sensor_error.BufferedSensorErrorRepositorySQLite;
import repository.repos.sensor_error.SensorErrorRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RepositoryMockFactory extends RepositoryFactory {
    private static final Logger logger = LoggerFactory.getLogger(RepositoryMockFactory.class);

    private final Map<String, Object> implementations = new HashMap<>();

    private final RepositoryConfigHolder configHolder;
    private final RepositoryDBConnector connector;

    public RepositoryMockFactory() {
        configHolder = new RepositoryConfigHolderMock();
        connector = new SqliteRepositoryDBConnector(configHolder);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getImplementation(Class<T> clazz) {
        String key = clazz.getName();
        T i = (T) implementations.get(key);

        if (i == null) {
            if (clazz.isAssignableFrom(MeasurementRepository.class))
                i = (T) new BufferedMeasurementRepositorySQLite(configHolder, connector);
            if (clazz.isAssignableFrom(SensorRepository.class))
                i = (T) new BufferedSensorRepositorySQLite(configHolder, connector);
            if (clazz.isAssignableFrom(DepartmentRepository.class))
                i = (T) new BufferedDepartmentRepositorySQLite(configHolder, connector);
            if (clazz.isAssignableFrom(AreaRepository.class))
                i = (T) new BufferedAreaRepositorySQLite(configHolder, connector);
            if (clazz.isAssignableFrom(ProcessRepository.class))
                i = (T) new BufferedProcessRepositorySQLite(configHolder, connector);
            if (clazz.isAssignableFrom(InstallationRepository.class))
                i = (T) new BufferedInstallationRepositorySQLite(configHolder, connector);
            if (clazz.isAssignableFrom(PersonRepository.class))
                i = (T) new BufferedPersonRepositorySQLite(configHolder, connector);
            if (clazz.isAssignableFrom(CalibratorRepository.class))
                i = (T) new BufferedCalibratorRepositorySQLite(configHolder, connector);
            if (clazz.isAssignableFrom(ChannelRepository.class))
                i = (T) new BufferedChannelRepositorySQLite(configHolder, connector);
            if (clazz.isAssignableFrom(ControlPointsRepository.class))
                i = (T) new BufferedControlPointsRepositorySQLite(configHolder, connector);
            if (clazz.isAssignableFrom(MeasurementFactorRepository.class))
                i = (T) new BufferedMeasurementFactorRepositorySQLite(configHolder, connector);
            if (clazz.isAssignableFrom(SensorErrorRepository.class))
                i = (T) new BufferedSensorErrorRepositorySQLite(configHolder, connector);

            if (i == null) logger.warn(String.format("Can't find implementation for %s", key));
            else implementations.put(key, i);
        }

        return i;
    }

    public void dispose() {
        try (Statement statement = connector.getStatement()) {
            String sql = "SELECT name FROM sqlite_master WHERE type = 'table'";
            ResultSet resultSet = statement.executeQuery(sql);
            List<String> tableNames = new ArrayList<>();
            while (resultSet.next()) tableNames.add(resultSet.getString("name"));
            for (String table : tableNames) {
                String dropTableSql = "DROP TABLE IF EXISTS " + table;
                statement.executeUpdate(dropTableSql);
            }
        } catch (SQLException e) {
            String message = "Disposing of repositories was interrupted of exception. " +
                    "It's can take affect to future tests. Please, drop all tables from RestData.db yourself.";
            logger.warn(message, e);
        }
    }
}
