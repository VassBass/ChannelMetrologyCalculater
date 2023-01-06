package repository;

import factory.AbstractFactory;
import repository.impl.ChannelRepositorySQLite;
import repository.impl.MeasurementRepositorySQLite;

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

        return null;
    }
}
