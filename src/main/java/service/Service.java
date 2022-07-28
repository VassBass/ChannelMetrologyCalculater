package service;

import java.util.Collection;

public interface Service<O> {
    Collection<O> getAll();
    boolean add(O object);
    boolean remove(O object);
    boolean set(O oldObject, O newObject);
    boolean clear();
    boolean rewrite(Collection<O>objects);
}
