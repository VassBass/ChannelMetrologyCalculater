package ui.event;

import java.util.HashMap;
import java.util.Map;

public class HashMapsEvent<O> extends Event<O> {
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
}
