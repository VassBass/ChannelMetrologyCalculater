package repository;

import java.util.ArrayList;

public interface ProcessRepository {
    ArrayList<String> getAll();
    String get(int index);
    void add(String object);
    void set(String oldObject, String newObject);
    void remove(String object);
    void clear();
    void rewrite(ArrayList<String>newList);
    void rewriteInCurrentThread(ArrayList<String>newList);
    void export();
}
