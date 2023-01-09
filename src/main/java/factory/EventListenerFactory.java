package factory;

import service.MainScreenEventListener;
import service.impl.DefaultMainScreenEventListener;

public class EventListenerFactory extends AbstractFactory {
    @Override
    public <T> T create(Class<T> clazz) {
        if (clazz.isAssignableFrom(MainScreenEventListener.class)) return (T) new DefaultMainScreenEventListener();

        return null;
    }
}
