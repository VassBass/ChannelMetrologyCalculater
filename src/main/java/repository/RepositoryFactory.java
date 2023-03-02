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

import java.util.HashMap;
import java.util.Map;

public class RepositoryFactory {
    private static volatile RepositoryFactory instance;

    private static final Logger logger = LoggerFactory.getLogger(RepositoryFactory.class);
    private final Map<String, Object> implementations = new HashMap<>();

    private static RepositoryConfigHolder configHolder;
    private static RepositoryDBConnector connector;

    public static RepositoryFactory getInstance() {
        if (configHolder == null || connector == null) {
            String message = "Before getting the instance, you need to execute the static init() method to initialize the factory";
            logger.warn(message);
            return null;
        }

        if (instance == null) {
            synchronized (RepositoryFactory.class) {
                if (instance == null) instance = new RepositoryFactory();
            }
        }
        return instance;
    }

    public static void init(RepositoryConfigHolder config, RepositoryDBConnector conn) {
        configHolder = config;
        connector = conn;
    }

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

            if (i == null) logger.warn(String.format("Can't find implementation for %s", key));
            else implementations.put(key, i);
        }

        return i;
    }
}
