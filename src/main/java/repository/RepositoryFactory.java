package repository;

import localization.Messages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.config.RepositoryConfigHolder;
import repository.connection.RepositoryDBConnector;
import repository.repos.calculation_method.BufferedCalculationMethodRepositorySQLite;
import repository.repos.calculation_method.CalculationMethodRepository;
import repository.repos.calibrator.BufferedCalibratorRepositorySQLite;
import repository.repos.calibrator.CalibratorRepository;
import repository.repos.channel.BufferedChannelRepositorySQLite;
import repository.repos.channel.ChannelRepository;
import repository.repos.control_points.BufferedControlPointsRepositorySQLite;
import repository.repos.control_points.ControlPointsRepository;
import repository.repos.measurement.BufferedMeasurementRepositorySQLite;
import repository.repos.measurement.MeasurementRepository;
import repository.repos.measurement_factor.BufferedMeasurementFactorRepositorySQLite;
import repository.repos.measurement_factor.MeasurementFactorRepository;
import repository.repos.person.BufferedPersonRepositorySQLite;
import repository.repos.person.PersonRepository;
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
            if (clazz.isAssignableFrom(CalculationMethodRepository.class))
                i = (T) new BufferedCalculationMethodRepositorySQLite(configHolder, connector);

            if (i == null) logger.warn(Messages.Log.missingImplementation(clazz));
            else implementations.put(clazz, i);
        }

        return i;
    }
}
