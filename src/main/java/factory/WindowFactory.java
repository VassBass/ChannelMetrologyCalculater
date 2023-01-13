package factory;

import service.MainScreenEventListener;
import ui.mainScreen.MainScreen;

import java.util.HashMap;
import java.util.Map;

public class WindowFactory extends AbstractFactory {
    private final Map<String, Object> windows = new HashMap<>();
    private final Map<String, Object> services = new HashMap<>();

    private final MainScreen mainScreen;

    private final AbstractFactory repositoryFactory;
    private final AbstractFactory eventServiceFactory;

    public WindowFactory(AbstractFactory repositoryFactory,
                         AbstractFactory eventServiceFactory) {
        this.repositoryFactory = repositoryFactory;
        this.eventServiceFactory = eventServiceFactory;

        MainScreenEventListener eventListener = eventServiceFactory.create(MainScreenEventListener.class);
        mainScreen = new MainScreen(repositoryFactory, eventListener);
    }

    public MainScreen getMainScreen() {
        return mainScreen;
    }

    @Override
    public <T> T create(Class<T> clazz) {
        String key = clazz.getName();
        T window = (T) windows.get(key);

        if (window == null) {
            if (clazz.isAssignableFrom(MainScreen.class)) {
                MainScreenEventListener service = (MainScreenEventListener) services.get(key);
                if (service == null) {
                    service = eventServiceFactory.create(MainScreenEventListener.class);
                    services.put(key, service);
                }

                window = (T) new MainScreen(repositoryFactory, service);
            }

            if (window != null) windows.put(key, window);
        }

        return window;
    }
}
