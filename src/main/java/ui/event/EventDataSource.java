package ui.event;

import java.util.Collections;
import java.util.Map;

public abstract class EventDataSource<O> {
    public abstract Map<String, String> getStringBuffer();
    public abstract Map<String, Integer> getIntBuffer();
    public abstract Map<String, Double> getDoubleBuffer();
    public abstract Map<String, Boolean> getBooleanBuffer();
    public abstract Map<String, O> getObjectBuffer();

    public abstract void put(String key, String value);
    public abstract void put(String key, Integer value);
    public abstract void put(String key, Double value);
    public abstract void put(String key, Boolean value);
    public abstract void put(String key, O value);

    public static final EventDataSource<Void> empty = new EventDataSource<Void>() {
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

        @Override
        public void put(String key, String value) {}

        @Override
        public void put(String key, Integer value) {}

        @Override
        public void put(String key, Double value) {}

        @Override
        public void put(String key, Boolean value) {}

        @Override
        public void put(String key, Void value) {}
    };
}
