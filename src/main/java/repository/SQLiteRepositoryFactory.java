package repository;

import factory.AbstractFactory;
import repository.impl.*;

import java.util.HashMap;
import java.util.Map;

public class SQLiteRepositoryFactory extends AbstractFactory {
    private final Map<String, Object> singletons = new HashMap<>();

    @Override
    public <T> T create(Class<T> clazz) {
        if (clazz.isAssignableFrom(MeasurementRepository.class)) {
            String key = MeasurementRepository.class.getName();
            T singleton = (T) singletons.get(key);
            if (singleton == null) {
                singleton = (T) new MeasurementRepositorySQLite();
                singletons.put(key, singleton);
            }
            return singleton;
        }

        if (clazz.isAssignableFrom(ChannelRepository.class)) {
            String key = ChannelRepository.class.getName();
            T singleton = (T) singletons.get(key);
            if (singleton == null) {
                singleton = (T) new ChannelRepositorySQLite(this);
                singletons.put(key, singleton);
            }
            return singleton;
        }

        if (clazz.isAssignableFrom(AreaRepository.class)) {
            String key = AreaRepository.class.getName();
            T singleton = (T) singletons.get(key);
            if (singleton == null) {
                singleton = (T) new AreaRepositorySQLite();
                singletons.put(key, singleton);
            }
            return singleton;
        }

        if (clazz.isAssignableFrom(DepartmentRepository.class)) {
            String key = DepartmentRepository.class.getName();
            T singleton = (T) singletons.get(key);
            if (singleton == null) {
                singleton = (T) new DepartmentRepositorySQLite();
                singletons.put(key, singleton);
            }
            return singleton;
        }

        if (clazz.isAssignableFrom(InstallationRepository.class)) {
            String key = InstallationRepository.class.getName();
            T singleton = (T) singletons.get(key);
            if (singleton == null) {
                singleton = (T) new InstallationRepositorySQLite();
                singletons.put(key, singleton);
            }
            return singleton;
        }

        if (clazz.isAssignableFrom(ProcessRepository.class)) {
            String key = ProcessRepository.class.getName();
            T singleton = (T) singletons.get(key);
            if (singleton == null) {
                singleton = (T) new ProcessRepositorySQLite();
                singletons.put(key, singleton);
            }
            return singleton;
        }

        if (clazz.isAssignableFrom(CalibratorRepository.class)) {
            String key = CalibratorRepository.class.getName();
            T singleton = (T) singletons.get(key);
            if (singleton == null) {
                singleton = (T) new CalibratorRepositorySQLite();
                singletons.put(key, singleton);
            }
            return singleton;
        }

        if (clazz.isAssignableFrom(ControlPointsValuesRepository.class)) {
            String key = ControlPointsValuesRepository.class.getName();
            T singleton = (T) singletons.get(key);
            if (singleton == null) {
                singleton = (T) new ControlPointsValuesRepositorySQLite();
                singletons.put(key, singleton);
            }
            return singleton;
        }

        if (clazz.isAssignableFrom(PersonRepository.class)) {
            String key = PersonRepository.class.getName();
            T singleton = (T) singletons.get(key);
            if (singleton == null) {
                singleton = (T) new PersonRepositorySQLite();
                singletons.put(key, singleton);
            }
            return singleton;
        }

        if (clazz.isAssignableFrom(SensorRepository.class)) {
            String key = SensorRepository.class.getName();
            T singleton = (T) singletons.get(key);
            if (singleton == null) {
                singleton = (T) new SensorRepositorySQLite();
                singletons.put(key, singleton);
            }
            return singleton;
        }

        return null;
    }
}
