package service.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.repository.config.RepositoryConfigHolder;
import service.repository.connection.RepositoryDBConnector;
import service.repository.repos.area.AreaRepository;
import service.repository.repos.area.BufferedAreaRepositorySQLite;
import service.repository.repos.calibrator.BufferedCalibratorRepositorySQLite;
import service.repository.repos.calibrator.CalibratorRepository;
import service.repository.repos.channel.BufferedChannelRepositorySQLite;
import service.repository.repos.channel.ChannelRepository;
import service.repository.repos.control_points.BufferedControlPointsRepositorySQLite;
import service.repository.repos.control_points.ControlPointsRepository;
import service.repository.repos.department.BufferedDepartmentRepositorySQLite;
import service.repository.repos.department.DepartmentRepository;
import service.repository.repos.installation.BufferedInstallationRepositorySQLite;
import service.repository.repos.installation.InstallationRepository;
import service.repository.repos.measurement.BufferedMeasurementRepositorySQLite;
import service.repository.repos.measurement.MeasurementRepository;
import service.repository.repos.measurement_factor.BufferedMeasurementFactorRepositorySQLite;
import service.repository.repos.measurement_factor.MeasurementFactorRepository;
import service.repository.repos.person.BufferedPersonRepositorySQLite;
import service.repository.repos.person.PersonRepository;
import service.repository.repos.process.BufferedProcessRepositorySQLite;
import service.repository.repos.process.ProcessRepository;
import service.repository.repos.sensor.BufferedSensorRepositorySQLite;
import service.repository.repos.sensor.SensorRepository;
import service.root.ImplementationFactory;

import java.util.HashMap;
import java.util.Map;

public class RepositoryImplementationFactory implements ImplementationFactory {
    private static final Logger logger = LoggerFactory.getLogger(RepositoryImplementationFactory.class);
    private final Map<String, Object> implementations = new HashMap<>();

    private final RepositoryConfigHolder configHolder;
    private final RepositoryDBConnector connector;

    public RepositoryImplementationFactory(RepositoryConfigHolder configHolder, RepositoryDBConnector connector) {
        this.configHolder = configHolder;
        this.connector = connector;
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

            if (i == null) logger.warn(String.format("Can't find implementation for %s", key));
            else implementations.put(key, i);
        }

        return i;
    }
}
