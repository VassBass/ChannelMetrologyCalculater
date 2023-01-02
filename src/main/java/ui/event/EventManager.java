package ui.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import static ui.event.EventManagerRegistrator.registration;

public class EventManager {
    private static final Logger logger = LoggerFactory.getLogger(EventManager.class);

    private final Map<String, AbstractEventManager> eventManagers = new HashMap<>();

    private static EventManager manager;

    public static EventManager getInstance() {
        if (manager == null) manager = new EventManager();
        return manager;
    }

    private EventManager() {
        registration(eventManagers);
    }

    public void runEvent(EventSource eventSource, Event event) {
        String id = eventSource.getId();
        AbstractEventManager manager = eventManagers.get(id);
        if (manager == null) {
            logger.info(String.format("EventManager for %s not found!", id));
        } else {
            manager.runEvent(event);
        }
    }
}
