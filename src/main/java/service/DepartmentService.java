package service;

import java.util.ArrayList;

public interface DepartmentService {
    void init();
    ArrayList<String> getAll();
    String[] getAllInStrings();
    ArrayList<String> add(String object);
    void addInCurrentThread(ArrayList<String>departments);
    ArrayList<String> remove(String object);
    ArrayList<String> set(String oldObject, String newObject);
    String get(int index);
    void clear();
    void rewriteInCurrentThread(ArrayList<String>departments);
    void resetToDefaultInCurrentThread();
    boolean backgroundTaskIsRun();
}
