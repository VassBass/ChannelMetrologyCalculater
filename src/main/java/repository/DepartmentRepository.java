package repository;

import java.util.ArrayList;

public interface DepartmentRepository {
    ArrayList<String>getAll();
    String get(int index);
    void add(String object);
    void addInCurrentThread(ArrayList<String>departments);
    void set(String oldObject, String newObject);
    void remove(String object);
    void clear();
    void rewrite(ArrayList<String>newList);
    void rewriteInCurrentThread(ArrayList<String>newList);
}
