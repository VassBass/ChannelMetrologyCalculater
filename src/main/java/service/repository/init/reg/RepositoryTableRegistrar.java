package service.repository.init.reg;

import service.repository.repos.area.AreaRepository;
import service.repository.repos.calibrator.CalibratorRepository;
import service.repository.repos.channel.ChannelRepository;
import service.repository.repos.control_points.ControlPointsRepository;
import service.repository.repos.department.DepartmentRepository;
import service.repository.repos.installation.InstallationRepository;
import service.repository.repos.measurement.MeasurementRepository;
import service.repository.repos.measurement_factor.MeasurementFactorRepository;
import service.repository.repos.person.PersonRepository;
import service.repository.repos.process.ProcessRepository;
import service.repository.repos.sensor.SensorRepository;

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
        }

        return registeredTables;
    }
}
