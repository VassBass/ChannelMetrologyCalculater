package repository;

import java.util.ArrayList;

public interface InstallationRepository {
    ArrayList<String> getAll();
    boolean add(String object);
    boolean set(String oldObject, String newObject);
    boolean remove(String object);
    boolean clear();
    boolean rewrite(ArrayList<String>newList);
}
