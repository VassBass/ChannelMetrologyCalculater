package ui.event;

import java.util.HashMap;
import java.util.Map;

public class HashMapEventDataSource<O> extends EventDataSource<O> {
    private Map<String, String> stringMap;
    private Map<String, Integer> intMap;
    private Map<String, Double> doubleMap;
    private Map<String, Boolean> booleanMap;
    private Map<String, O> objectMap;

    @Override
    public Map<String, String> getStringBuffer() {
        if (stringMap == null) stringMap = new HashMap<>();
        return stringMap;
    }

    @Override
    public Map<String, Integer> getIntBuffer() {
        if (intMap == null) intMap = new HashMap<>();
        return intMap;
    }

    @Override
    public Map<String, Double> getDoubleBuffer() {
        if (doubleMap == null) doubleMap = new HashMap<>();
        return doubleMap;
    }

    @Override
    public Map<String, Boolean> getBooleanBuffer() {
        if (booleanMap == null) booleanMap = new HashMap<>();
        return booleanMap;
    }

    @Override
    public Map<String, O> getObjectBuffer() {
        if (objectMap == null) objectMap = new HashMap<>();
        return objectMap;
    }

    @Override
    public void put(String key, String value) {
        if (stringMap == null) stringMap = new HashMap<>();
        stringMap.put(key, value);
    }

    @Override
    public void put(String key, Integer value) {
        if (intMap == null) intMap = new HashMap<>();
        intMap.put(key, value);
    }

    @Override
    public void put(String key, Double value) {
        if (doubleMap == null) doubleMap = new HashMap<>();
        doubleMap.put(key, value);
    }

    @Override
    public void put(String key, Boolean value) {
        if (booleanMap == null) booleanMap = new HashMap<>();
        booleanMap.put(key, value);
    }

    @Override
    public void put(String key, O value) {
        if (objectMap == null) objectMap = new HashMap<>();
        objectMap.put(key, value);
    }
}
