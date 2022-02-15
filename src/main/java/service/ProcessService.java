package service;

import java.awt.*;
import java.util.ArrayList;

public interface ProcessService {
    void init(Window window);
    ArrayList<String> getAll();
    String[] getAllInStrings();
    ArrayList<String> add(String object);
    ArrayList<String> remove(String object);
    ArrayList<String> set(String oldObject, String newObject);
    String get(int index);
    void clear();
    void save();
    boolean exportData();
    void rewriteInCurrentThread(ArrayList<String>processs);
}
