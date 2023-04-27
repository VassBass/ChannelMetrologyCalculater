package service.channel.list.ui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.channel.list.ChannelListManager;
import service.channel.list.ChannelListService;
import service.channel.list.ui.swing.SwingChannelListButtonsPanel;
import service.channel.list.ui.swing.SwingChannelListInfoTable;
import service.channel.list.ui.swing.SwingChannelListSearchPanel;
import service.channel.list.ui.swing.SwingChannelListTable;

import java.util.HashMap;
import java.util.Map;

public class ChannelListSwingContext {
    private static final Logger logger = LoggerFactory.getLogger(ChannelListSwingContext.class);

    private final Map<Class<?>, Object> buffer = new HashMap<>();
    private final ChannelListService service;
    private ChannelListManager manager;

    public ChannelListSwingContext(ChannelListService service) {
        this.service = service;
    }

    @SuppressWarnings("unchecked")
    public <T> T getElement(Class<T> clazz) {
        if (manager == null) {
            String message = "Before use context you must register manager!";
            logger.warn(message);
            return null;
        }

        T element = (T) buffer.get(clazz);

        if (element == null) {
            if (clazz.isAssignableFrom(ChannelListInfoTable.class) || clazz.isAssignableFrom(SwingChannelListInfoTable.class)) {
                element = (T) new SwingChannelListInfoTable(service);
                buffer.put(ChannelListInfoTable.class, element);
                buffer.put(SwingChannelListInfoTable.class, element);
            }
            if (clazz.isAssignableFrom(ChannelListSearchPanel.class) || clazz.isAssignableFrom(SwingChannelListSearchPanel.class)) {
                element = (T) new SwingChannelListSearchPanel(manager);
                buffer.put(ChannelListSearchPanel.class, element);
                buffer.put(SwingChannelListSearchPanel.class, element);
            }
            if (clazz.isAssignableFrom(SwingChannelListButtonsPanel.class)) {
                element = (T) new SwingChannelListButtonsPanel(manager);
                buffer.put(clazz, element);
            }
            if (clazz.isAssignableFrom(ChannelListTable.class) || clazz.isAssignableFrom(SwingChannelListTable.class)) {
                element = (T) new SwingChannelListTable(manager, service);
                buffer.put(ChannelListTable.class, element);
                buffer.put(SwingChannelListTable.class, element);
            }

            if (element == null) logger.warn(String.format("Can't find implementation for %s", clazz.getName()));
        }

        return element;
    }

    public void registerManager(ChannelListManager manager) {
        this.manager = manager;
    }
}
