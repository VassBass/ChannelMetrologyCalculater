package service;

import java.util.ArrayList;

public interface ProcessService {
    void init();
    ArrayList<String> getAll();
    String[] getAllInStrings();
    ArrayList<String> add(String object);
    void addInCurrentThread(ArrayList<String>processes);
    ArrayList<String> remove(String object);
    ArrayList<String> set(String oldObject, String newObject);
    String get(int index);
    void clear();
    void exportData();
    void rewriteInCurrentThread(ArrayList<String>process);
    void resetToDefault();
}
