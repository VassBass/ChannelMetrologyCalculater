package repository.init.reg;

import repository.repos.calculation_method.CalculationMethodRepository;
import repository.repos.calibrator.CalibratorRepository;
import repository.repos.channel.ChannelRepository;
import repository.repos.control_points.ControlPointsRepository;
import repository.repos.measurement.MeasurementRepository;
import repository.repos.measurement_factor.MeasurementFactorRepository;
import repository.repos.person.PersonRepository;
import repository.repos.sensor.SensorRepository;
import repository.repos.sensor_error.SensorErrorRepository;

import java.util.HashMap;
import java.util.Map;

public class RepositoryTableRegistrar {
    private static Map<Class<?>, String> registeredTables;

    public static Map<Class<?>, String> getRegisteredTables() {
        if (registeredTables == null) {
            registeredTables = new HashMap<>();

            registeredTables.put(MeasurementRepository.class, "measurements");
            registeredTables.put(SensorRepository.class, "sensors");
            registeredTables.put(ChannelRepository.class, "channels");
            registeredTables.put(CalibratorRepository.class, "calibrators");
            registeredTables.put(PersonRepository.class, "persons");
            registeredTables.put(ControlPointsRepository.class, "controlPoints");
            registeredTables.put(MeasurementFactorRepository.class, "measurement_factors");
            registeredTables.put(SensorErrorRepository.class, "sensors_errors");
            registeredTables.put(CalculationMethodRepository.class, "calculation_methods");
        }

        return registeredTables;
    }
}
