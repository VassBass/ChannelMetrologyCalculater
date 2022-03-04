package service;

import java.util.ArrayList;

public interface InstallationService {
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
