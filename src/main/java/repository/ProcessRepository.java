package repository;

import java.util.Collection;

public interface ProcessRepository extends Repository<String> {
    boolean add(Collection<String> objects);
}
