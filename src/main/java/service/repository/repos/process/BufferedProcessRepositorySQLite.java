package service.repository.repos.process;

import service.repository.config.RepositoryConfigHolder;
import service.repository.connection.RepositoryDBConnector;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BufferedProcessRepositorySQLite extends ProcessRepositorySQLite {
    private final List<String> buffer;

    public BufferedProcessRepositorySQLite(RepositoryConfigHolder configHolder, RepositoryDBConnector connector) {
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
    public boolean add(@Nonnull Collection<String> objects) {
        objects.forEach(o -> {
            if (!buffer.contains(o)) buffer.add(o);
        });
        return super.add(objects);
    }

    @Override
    public boolean set(@Nonnull String oldObject, @Nonnull String newObject) {
        if (oldObject.equals(newObject)) return true;

        int index = buffer.indexOf(oldObject);
        if (index >= 0) {
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
        buffer.clear();
        buffer.addAll(newList);
        return super.rewrite(newList);
    }
}
