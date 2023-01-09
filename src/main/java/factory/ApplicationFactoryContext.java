package factory;

import java.util.HashMap;
import java.util.Map;

public class ApplicationFactoryContext {
    private static ApplicationFactoryContext instance;
    private final Map<String, Object> factories = new HashMap<>();

    private ApplicationFactoryContext(){}
    public static ApplicationFactoryContext getInstance() {
        if (instance == null) instance = new ApplicationFactoryContext();
        return instance;
    }

    public <T> T getFactory(Class<T> clazz) {
        String key = clazz.getName();
        T factory = (T) factories.get(key);

        if (factory == null) {
            if (clazz.isAssignableFrom(SQLiteRepositoryFactory.class)) factory = (T) new SQLiteRepositoryFactory();

            if (clazz.isAssignableFrom(WindowFactory.class)) {
                AbstractFactory repoFactory = this.getFactory(SQLiteRepositoryFactory.class);
                AbstractFactory eventServiceFactory = new EventListenerFactory();
                factory = (T) new WindowFactory(repoFactory, eventServiceFactory);
            }

            if (factory != null) factories.put(key, factory);
        }

        return factory;
    }
}
