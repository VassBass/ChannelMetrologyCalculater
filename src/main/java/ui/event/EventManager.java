package ui.event;

import java.util.HashMap;
import java.util.Map;

import static ui.event.EventManagerRegistrator.registration;

public class EventManager {
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
        eventManagers
                .getOrDefault(eventSource.getId(), DefaultEventManager.getInstance())
                .runEvent(event);
    }
}
