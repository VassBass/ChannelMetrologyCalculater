package ui.event;

public class EventSourceIdGenerator {
    public static String generate(Class<?> eventSource) {
        return eventSource.getName() + "_EventSource";
    }
}
