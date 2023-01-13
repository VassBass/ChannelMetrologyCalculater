package ui.event.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ui.event.AbstractEventManager;
import ui.event.EventSource;
import ui.event.EventType;

import java.util.HashMap;
import java.util.Map;

public class EventManager {
    private static final Logger logger = LoggerFactory.getLogger(EventManager.class);

    private static final Map<String, AbstractEventManager> EVENT_MANAGERS = new HashMap<>();

    private static EventManager manager;

    public static EventManager getInstance() {
        if (manager == null) manager = new EventManager();
        return manager;
    }

    private EventManager() {
        registration();
    }

    public void runEvent(EventSource eventSource, EventType eventType) {
        String id = eventSource.getId();
        AbstractEventManager manager = EVENT_MANAGERS.get(id);
        if (manager == null) {
            logger.info(String.format("EventManager for %s not found!", id));
        } else {
            manager.runEvent(event);
        }
    }

    public static void registration() {

    }
}
