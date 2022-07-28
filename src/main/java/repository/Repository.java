package repository;

import java.util.Collection;

public interface Repository<O> {
    boolean createTable();
    Collection<O> getAll();
    boolean add(O o);
    boolean set(O oldO, O newO);
    boolean remove(O o);
    boolean clear();
    boolean rewrite(Collection<O>list);
}
