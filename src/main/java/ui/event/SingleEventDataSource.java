package ui.event;

import java.util.Collections;
import java.util.Map;

public class SingleEventDataSource<V> extends EventDataSource<V> {
    private final String key;
    private final V value;

    public SingleEventDataSource(String key, V value) {
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

    @Override
    public void put(String key, String value) {}

    @Override
    public void put(String key, Integer value) {}

    @Override
    public void put(String key, Double value) {}

    @Override
    public void put(String key, Boolean value) {}

    @Override
    public void put(String key, V value) {}
}
