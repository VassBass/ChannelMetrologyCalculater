package repository;

import java.util.List;

public interface Repository<O> {
    boolean createTable();
    List<O>getAll();
    boolean add(O o);
    boolean set(O oldO, O newO);
    boolean remove(O o);
    boolean clear();
    boolean rewrite(List<O>list);
}
