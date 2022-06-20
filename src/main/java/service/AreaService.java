package service;

import java.util.ArrayList;

public interface AreaService {
    ArrayList<String> getAll();
    String[] getAllInStrings();
    ArrayList<String> add(String object);
    ArrayList<String> remove(String object);
    ArrayList<String> set(String oldObject, String newObject);
    boolean clear();
    boolean rewrite(ArrayList<String>areas);
    boolean resetToDefault();
}
