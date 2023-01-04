package service;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.EMPTY;

public class DataTransfer {
    private static DataTransfer instance;

    public static DataTransfer getInstance() {
        if (instance == null) instance = new DataTransfer();
        return instance;
    }

    private DataTransfer(){}

    private Map<String, String> stringMap;
    private Map<String, Integer> intMap;
    private Map<String, Double> doubleMap;

    public String extractString(String key) {
        if (stringMap == null) stringMap = new HashMap<>();
        String str = stringMap.remove(key);
        return str == null ?
                EMPTY :
                str;
    }

    public Optional<Integer> extractInt(String key) {
        if (intMap == null) intMap = new HashMap<>();
        Integer i = intMap.remove(key);
        return i == null ?
                Optional.empty() :
                Optional.of(i);
    }

    public Optional<Double> extractDouble(String key) {
        if (doubleMap == null) doubleMap = new HashMap<>();
        Double dbl = doubleMap.remove(key);
        return dbl == null ?
                Optional.empty() :
                Optional.of(dbl);
    }

    public void put(@Nonnull String key, @Nonnull String value) {
        if (stringMap == null) stringMap = new HashMap<>();
        stringMap.put(key, value);
    }

    public void put(@Nonnull String key, int value) {
        if (intMap == null) intMap = new HashMap<>();
        intMap.put(key, value);
    }

    public void put(@Nonnull String key, double value) {
        if (doubleMap == null) doubleMap = new HashMap<>();
        doubleMap.put(key, value);
    }
}
