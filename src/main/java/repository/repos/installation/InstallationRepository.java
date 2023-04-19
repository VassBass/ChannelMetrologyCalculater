package repository.repos.installation;

import javax.annotation.Nonnull;
import java.util.Collection;

public interface InstallationRepository {
    Collection<String> getAll();

    boolean set(@Nonnull String oldObject, @Nonnull String newObject);
    boolean rewrite(@Nonnull Collection<String> newList);

    boolean add(@Nonnull String object);
    boolean addAll(Collection<String> objects);

    boolean remove(@Nonnull String object);
    boolean clear();
}
