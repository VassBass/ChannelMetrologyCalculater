package repository.init.reg;

import repository.repos.area.AreaRepository;
import repository.repos.calibrator.CalibratorRepository;
import repository.repos.channel.ChannelRepository;
import repository.repos.control_points.ControlPointsRepository;
import repository.repos.department.DepartmentRepository;
import repository.repos.installation.InstallationRepository;
import repository.repos.measurement.MeasurementRepository;
import repository.repos.measurement_factor.MeasurementFactorRepository;
import repository.repos.person.PersonRepository;
import repository.repos.process.ProcessRepository;
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
            registeredTables.put(DepartmentRepository.class, "departments");
            registeredTables.put(AreaRepository.class, "areas");
            registeredTables.put(ProcessRepository.class, "processes");
            registeredTables.put(InstallationRepository.class, "installations");
            registeredTables.put(CalibratorRepository.class, "calibrators");
            registeredTables.put(PersonRepository.class, "persons");
            registeredTables.put(ControlPointsRepository.class, "controlPoints");
            registeredTables.put(MeasurementFactorRepository.class, "measurement_factors");
            registeredTables.put(SensorErrorRepository.class, "sensors_errors");
        }

        return registeredTables;
    }
}
