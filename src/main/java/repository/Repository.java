package repository;

import javax.annotation.Nonnull;
import java.util.Collection;

public interface Repository<O> {
    boolean createTable();
    Collection<O> getAll();
    boolean add(@Nonnull O o);
    boolean set(@Nonnull O oldO, @Nonnull O newO);
    boolean remove(@Nonnull O o);
    boolean clear();
    boolean rewrite(@Nonnull Collection<O>list);
}
