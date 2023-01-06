package repository;

import java.util.Collection;

public interface InstallationRepository extends Repository<String> {
    boolean add(Collection<String> objects);
}
