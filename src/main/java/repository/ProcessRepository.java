package repository;

import java.util.ArrayList;

public interface ProcessRepository {
    void createTable();
    ArrayList<String> getAll();
    boolean add(String object);
    boolean set(String oldObject, String newObject);
    boolean remove(String object);
    boolean clear();
    boolean rewrite(ArrayList<String>newList);
}
