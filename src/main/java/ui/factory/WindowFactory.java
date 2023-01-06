package ui.factory;

import factory.AbstractFactory;
import repository.SQLiteRepositoryFactory;
import ui.mainScreen.MainScreen;

import java.util.HashMap;
import java.util.Map;

public class WindowFactory extends AbstractFactory {
    private final Map<String, Object> singletons = new HashMap<>();

    private final AbstractFactory repositoryFactory;

    public WindowFactory(AbstractFactory repositoryFactory) {
        this.repositoryFactory = repositoryFactory;
    }

    @Override
    public <T> T create(Class<T> clazz) {
        if (clazz.isAssignableFrom(MainScreen.class)) {
            String key = MainScreen.class.getName();
            T singleton = (T) singletons.get(key);
            if (singleton == null) {
                singleton = (T) new MainScreen(repositoryFactory);
                singletons.put(key, singleton);
            }
            return singleton;
        }

        return null;
    }
}
