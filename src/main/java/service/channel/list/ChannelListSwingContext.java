package service.channel.list;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.channel.list.ui.ChannelListSearchPanel;
import service.channel.list.ui.ChannelListTable;
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

    public ChannelListSwingContext(ChannelListService service) {
        this.service = service;
    }

    private ChannelListManager manager;

    @SuppressWarnings("unchecked")
    public <T> T getElement(Class<T> clazz) {
        if (manager == null) {
            String message = "Before use context you must register manager!";
            logger.warn(message);
            return null;
        }

        T element = (T) buffer.get(clazz);

        if (element == null) {
            if (clazz.isAssignableFrom(ChannelListTable.class) || clazz.isAssignableFrom(SwingChannelListInfoTable.class))
                element = (T) new SwingChannelListInfoTable(service);
            if (clazz.isAssignableFrom(ChannelListSearchPanel.class) || clazz.isAssignableFrom(SwingChannelListSearchPanel.class))
                element = (T) new SwingChannelListSearchPanel(manager);
            if (clazz.isAssignableFrom(SwingChannelListButtonsPanel.class))
                element = (T) new SwingChannelListButtonsPanel(manager);
            if (clazz.isAssignableFrom(ChannelListTable.class) || clazz.isAssignableFrom(SwingChannelListTable.class))
                element = (T) new SwingChannelListTable(manager, service);

            if (element == null) logger.warn(String.format("Can't find implementation for %s", clazz.getName()));
            else buffer.put(clazz, element);
        }

        return element;
    }

    public void registerManager(ChannelListManager manager) {
        this.manager = manager;
    }
}
