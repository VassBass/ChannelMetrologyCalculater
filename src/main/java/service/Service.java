package service;

import java.util.List;

public interface Service<O> {
    List<O> getAll();
    boolean add(O object);
    boolean remove(O object);
    boolean set(O oldObject, O newObject);
    boolean clear();
    boolean rewrite(List<O>objects);
}
