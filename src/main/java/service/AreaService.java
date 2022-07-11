package service;

import java.util.List;

public interface AreaService {
    List<String> getAll();
    String[] getAllInStrings();
    boolean add(String object);
    boolean remove(String object);
    boolean set(String oldObject, String newObject);
    boolean clear();
    boolean rewrite(List<String>areas);
    boolean resetToDefault();
}
