package service;

import java.util.ArrayList;

public interface AreaService {
    void init();
    ArrayList<String> getAll();
    String[] getAllInStrings();
    ArrayList<String> add(String object);
    void addInCurrentThread(ArrayList<String> areas);
    ArrayList<String> remove(String object);
    ArrayList<String> set(String oldObject, String newObject);
    String get(int index);
    void clear();
    void rewriteInCurrentThread(ArrayList<String>areas);
    void resetToDefaultInCurrentThread();
    boolean backgroundTaskIsRun();
}
