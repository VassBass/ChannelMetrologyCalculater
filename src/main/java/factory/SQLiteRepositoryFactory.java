package factory;

import repository.*;
import repository.impl.*;
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
import service.repository.repos.person.PersonRepository;
import service.repository.repos.person.PersonRepositorySQLite;
import service.repository.repos.process.ProcessRepository;
import service.repository.repos.process.ProcessRepositorySQLite;
import service.repository.repos.sensor.SensorRepository;
import service.repository.repos.sensor.SensorRepositorySQLite;

import java.util.HashMap;
import java.util.Map;

public class SQLiteRepositoryFactory extends AbstractFactory {
    private final Map<String, Object> repos = new HashMap<>();

    @Override
    public <T> T create(Class<T> clazz) {
        String key = clazz.getName();
        T repo = (T) repos.get(key);

        if (repo == null) {
            if (clazz.isAssignableFrom(MeasurementRepository.class)) repo = (T) new MeasurementRepositorySQLite();
            if (clazz.isAssignableFrom(ChannelRepository.class)) repo = (T) new ChannelRepositorySQLite(this);
            if (clazz.isAssignableFrom(AreaRepository.class)) repo = (T) new AreaRepositorySQLite();
            if (clazz.isAssignableFrom(DepartmentRepository.class)) repo = (T) new DepartmentRepositorySQLite();
            if (clazz.isAssignableFrom(InstallationRepository.class)) repo = (T) new InstallationRepositorySQLite();
            if (clazz.isAssignableFrom(ProcessRepository.class)) repo = (T) new ProcessRepositorySQLite();
            if (clazz.isAssignableFrom(CalibratorRepository.class)) repo = (T) new CalibratorRepositorySQLite();
            if (clazz.isAssignableFrom(ControlPointsValuesRepository.class)) repo = (T) new ControlPointsValuesRepositorySQLite();
            if (clazz.isAssignableFrom(PersonRepository.class)) repo = (T) new PersonRepositorySQLite();
            if (clazz.isAssignableFrom(SensorRepository.class)) repo = (T) new SensorRepositorySQLite();

            if (repo != null) repos.put(key, repo);
        }

        return repo;
    }
}
