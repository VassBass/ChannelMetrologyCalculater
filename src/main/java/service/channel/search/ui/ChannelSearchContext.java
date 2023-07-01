package service.channel.search.ui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.channel.search.ChannelSearchManager;
import service.channel.search.ui.swing.SwingButtonPanel;
import service.channel.search.ui.swing.SwingDatePanel;
import service.channel.search.ui.swing.SwingLocationPanel;

import java.util.HashMap;
import java.util.Map;

public class ChannelSearchContext {
    private static final Logger logger = LoggerFactory.getLogger(ChannelSearchContext.class);

    private final Map<Class<?>, Object> buffer = new HashMap<>();
    private ChannelSearchManager manager;

    @SuppressWarnings("unchecked")
    public <T> T getElement(Class<T> clazz) {
        if (manager == null) {
            String message = "Before use context you must register manager!";
            logger.warn(message);
            return null;
        }

        T element = (T) buffer.get(clazz);

        if (element == null) {
            if (clazz.isAssignableFrom(DatePanel.class) || clazz.isAssignableFrom(SwingDatePanel.class)) {
                element = (T) new SwingDatePanel();
                buffer.put(DatePanel.class, element);
                buffer.put(SwingDatePanel.class, element);
            }
            if (clazz.isAssignableFrom(LocationPanel.class) || clazz.isAssignableFrom(SwingLocationPanel.class)) {
                element = (T) new SwingLocationPanel();
                buffer.put(LocationPanel.class, element);
                buffer.put(SwingLocationPanel.class, element);
            }
            if (clazz.isAssignableFrom(SwingButtonPanel.class)) {
                element = (T) new SwingButtonPanel(manager);
                buffer.put(clazz, element);
            }

            if (element == null) logger.warn(String.format("Can't find implementation for %s", clazz.getName()));
        }

        return element;
    }

    public void registerManager(ChannelSearchManager manager) {
        this.manager = manager;
    }
}
