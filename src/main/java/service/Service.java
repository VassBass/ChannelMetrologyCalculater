package service;

import javax.annotation.Nonnull;
import java.util.Collection;

public interface Service<O> {
    Collection<O> getAll();
    boolean add(@Nonnull O object);
    boolean remove(@Nonnull O object);
    boolean set(@Nonnull O oldObject, @Nonnull O newObject);
    boolean clear();
    boolean rewrite(@Nonnull Collection<O>objects);
}
