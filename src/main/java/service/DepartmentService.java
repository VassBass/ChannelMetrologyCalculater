package service;

import java.awt.*;
import java.util.ArrayList;

public interface DepartmentService {
    void init();
    ArrayList<String> getAll();
    String[] getAllInStrings();
    ArrayList<String> add(String object);
    ArrayList<String> remove(String object);
    ArrayList<String> set(String oldObject, String newObject);
    String get(int index);
    void clear();
    boolean exportData();
    void rewriteInCurrentThread(ArrayList<String>areas);
    void resetToDefault();
}
