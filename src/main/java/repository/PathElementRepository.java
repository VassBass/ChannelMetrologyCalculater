package repository;

import java.util.Collection;

public interface PathElementRepository extends Repository<String> {
    boolean add(Collection<String>objects);
}
