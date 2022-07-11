package repository;

import java.util.List;

public interface InstallationRepository {
    List<String> getAll();
    boolean add(String object);
    boolean set(String oldObject, String newObject);
    boolean remove(String object);
    boolean clear();
    boolean rewrite(List<String>newList);
}
