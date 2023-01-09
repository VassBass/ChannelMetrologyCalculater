package ui.event;

import java.util.Collections;
import java.util.Map;

public abstract class Event<O> {
    public abstract Map<String, String> getStringBuffer();
    public abstract Map<String, Integer> getIntBuffer();
    public abstract Map<String, Double> getDoubleBuffer();
    public abstract Map<String, Boolean> getBooleanBuffer();
    public abstract Map<String, O> getObjectBuffer();

    public static final Event<Void> emptyEvent = new Event<Void>() {
        @Override
        public Map<String, String> getStringBuffer() {
            return Collections.emptyMap();
        }

        @Override
        public Map<String, Integer> getIntBuffer() {
            return Collections.emptyMap();
        }

        @Override
        public Map<String, Double> getDoubleBuffer() {
            return Collections.emptyMap();
        }

        @Override
        public Map<String, Boolean> getBooleanBuffer() {
            return Collections.emptyMap();
        }

        @Override
        public Map<String, Void> getObjectBuffer() {
            return Collections.emptyMap();
        }
    };
}
