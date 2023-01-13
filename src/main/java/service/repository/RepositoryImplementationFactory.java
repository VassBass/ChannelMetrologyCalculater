package service.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.application.ImplementationFactory;
import service.repository.config.RepositoryConfigHolder;
import service.repository.connection.RepositoryDBConnector;
import service.repository.repos.area.AreaRepository;
import service.repository.repos.area.AreaRepositorySQLite;
import service.repository.repos.channel.ChannelRepository;
import service.repository.repos.channel.ChannelRepositorySQLite;
import service.repository.repos.department.DepartmentRepository;
import service.repository.repos.department.DepartmentRepositorySQLite;
import service.repository.repos.installation.InstallationRepository;
import service.repository.repos.installation.InstallationRepositorySQLite;
import service.repository.repos.measurement.MeasurementRepository;
import service.repository.repos.measurement.MeasurementRepositorySQLite;
import service.repository.repos.process.ProcessRepository;
import service.repository.repos.process.ProcessRepositorySQLite;
import service.repository.repos.sensor.SensorRepository;
import service.repository.repos.sensor.SensorRepositorySQLite;

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
            if (clazz.isAssignableFrom(MeasurementRepository.class)) {
                i = (T) new MeasurementRepositorySQLite(configHolder, connector);
                implementations.put(key, i);
            }

            if (clazz.isAssignableFrom(SensorRepository.class)) {
                i = (T) new SensorRepositorySQLite(configHolder, connector);
                implementations.put(key, i);
            }

            if (clazz.isAssignableFrom(DepartmentRepository.class)) {
                i = (T) new DepartmentRepositorySQLite(configHolder, connector);
                implementations.put(key, i);
            }

            if (clazz.isAssignableFrom(AreaRepository.class)) {
                i = (T) new AreaRepositorySQLite(configHolder, connector);
                implementations.put(key, i);
            }

            if (clazz.isAssignableFrom(ProcessRepository.class)) {
                i = (T) new ProcessRepositorySQLite(configHolder, connector);
                implementations.put(key, i);
            }

            if (clazz.isAssignableFrom(InstallationRepository.class)) {
                i = (T) new InstallationRepositorySQLite(configHolder, connector);
                implementations.put(key, i);
            }

            if (clazz.isAssignableFrom(ChannelRepository.class)) {
                i = (T) new ChannelRepositorySQLite(configHolder, connector, this);
                implementations.put(key, i);
            }
        }

        logger.warn(String.format("Can't find implementation for %s", key));
        return i;
    }
}
