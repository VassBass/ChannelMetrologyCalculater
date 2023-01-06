package repository;

import java.util.Collection;

public interface DepartmentRepository extends Repository<String> {
    boolean add(Collection<String> objects);
}
