package repository.repos.department;

import repository.config.RepositoryConfigHolder;
import repository.connection.RepositoryDBConnector;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class BufferedDepartmentRepositorySQLite extends DepartmentRepositorySQLite {
    private final List<String> buffer;

    public BufferedDepartmentRepositorySQLite(RepositoryConfigHolder configHolder, RepositoryDBConnector connector) {
        super(configHolder, connector);
        buffer = new ArrayList<>(super.getAll());
    }

    @Nonnull
    @Override
    public Collection<String> getAll() {
        return buffer;
    }

    @Override
    public boolean add(@Nonnull String object) {
        if (buffer.contains(object)) return false;

        buffer.add(object);
        return super.add(object);
    }

    @Override
    public boolean addAll(@Nonnull Collection<String> objects) {
        objects.forEach(o -> {
            if (o != null && !buffer.contains(o)) buffer.add(o);
        });
        return super.addAll(objects);
    }

    @Override
    public boolean set(@Nonnull String oldObject, @Nonnull String newObject) {
        if (!buffer.contains(oldObject)) return false;
        if (!oldObject.equals(newObject) && buffer.contains(newObject)) return false;

        int index = buffer.indexOf(oldObject);
        if (index >= 0) {
            if (oldObject.equals(newObject)) return true;

            buffer.set(index, newObject);
            return super.set(oldObject, newObject);
        } else return false;
    }

    @Override
    public boolean remove(@Nonnull String object) {
        return buffer.remove(object) && super.remove(object);
    }

    @Override
    public boolean clear() {
        buffer.clear();
        return super.clear();
    }

    @Override
    public boolean rewrite(@Nonnull Collection<String> newList) {
        if (buffer.equals(newList)) return true;

        buffer.clear();
        buffer.addAll(newList.stream().filter(Objects::nonNull).collect(Collectors.toList()));
        return super.rewrite(newList);
    }
}
