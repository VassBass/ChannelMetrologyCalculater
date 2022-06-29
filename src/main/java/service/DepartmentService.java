package service;

import java.util.ArrayList;

public interface DepartmentService {
    ArrayList<String> getAll();
    String[] getAllInStrings();
    boolean add(String object);
    boolean remove(String object);
    boolean set(String oldObject, String newObject);
    boolean clear();
    boolean rewrite(ArrayList<String>departments);
    boolean resetToDefault();
}
