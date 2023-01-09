package ui.event;

import java.util.Collections;
import java.util.Map;

public class SimpleEvent<V> extends Event<V> {
    private final String key;
    private final V value;

    public SimpleEvent(String key, V value) {
        this.key = key;
        this.value = value;
    }
    @Override
    public Map<String, String> getStringBuffer() {
        return value.getClass().isAssignableFrom(String.class) ?
                Collections.singletonMap(key, (String) value) :
                Collections.emptyMap();
    }

    @Override
    public Map<String, Integer> getIntBuffer() {
        return value.getClass().isAssignableFrom(Integer.class) ?
                Collections.singletonMap(key, (Integer) value) :
                Collections.emptyMap();
    }

    @Override
    public Map<String, Double> getDoubleBuffer() {
        return value.getClass().isAssignableFrom(Double.class) ?
                Collections.singletonMap(key, (Double) value) :
                Collections.emptyMap();
    }

    @Override
    public Map<String, Boolean> getBooleanBuffer() {
        return value.getClass().isAssignableFrom(Boolean.class) ?
                Collections.singletonMap(key, (Boolean) value) :
                Collections.emptyMap();
    }

    @Override
    public Map<String, V> getObjectBuffer() {
        return Collections.singletonMap(key, value);
    }
}
