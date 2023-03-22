package repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.config.RepositoryConfigHolder;
import repository.connection.RepositoryDBConnector;
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

import java.util.HashMap;
import java.util.Map;

public class RepositoryFactory {
    private static final Logger logger = LoggerFactory.getLogger(RepositoryFactory.class);
    private final Map<Class<?>, Object> implementations = new HashMap<>();

    protected final RepositoryConfigHolder configHolder;
    protected final RepositoryDBConnector connector;

    public RepositoryFactory(RepositoryConfigHolder configHolder, RepositoryDBConnector connector) {
        this.configHolder = configHolder;
        this.connector = connector;
    }

    @SuppressWarnings("unchecked")
    public <T> T getImplementation(Class<T> clazz) {
        T i = (T) implementations.get(clazz);

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

            if (i == null) logger.warn(String.format("Can't find implementation for %s", clazz.getName()));
            else implementations.put(clazz, i);
        }

        return i;
    }
}
