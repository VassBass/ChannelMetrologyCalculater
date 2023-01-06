package repository;

import java.util.Collection;

public interface AreaRepository extends Repository<String> {
    boolean add(Collection<String> objects);
}
