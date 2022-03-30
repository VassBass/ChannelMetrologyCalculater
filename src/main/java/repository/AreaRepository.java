package repository;

import java.util.ArrayList;

public interface AreaRepository {
    ArrayList<String> getAll();
    String get(int index);
    void add(String object);
    void addInCurrentThread(ArrayList<String>areas);
    void set(String oldObject, String newObject);
    void remove(String object);
    void clear();
    void rewrite(ArrayList<String>newList);
    void rewriteInCurrentThread(ArrayList<String>newList);
    boolean backgroundTaskIsRun();
}
