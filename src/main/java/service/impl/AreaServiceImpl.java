package service.impl;

import def.DefaultAreas;
import repository.Repository;
import repository.impl.AreaRepositorySQLite;
import service.AreaService;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

public class AreaServiceImpl implements AreaService {
    private static AreaServiceImpl service;

    private final Repository<String> repository;

    private AreaServiceImpl(){
        repository = new AreaRepositorySQLite();
    }

    public AreaServiceImpl(Repository<String> repository){
        this.repository = repository == null ? new AreaRepositorySQLite() : repository;
        service = this;
    }

    public static AreaServiceImpl getInstance() {
        if (service == null) service = new AreaServiceImpl();

        return service;
    }

    @Override
    public Collection<String> getAll() {
        return repository.getAll();
    }

    @Override
    public String[] getAllInStrings(){
        return repository.getAll().toArray(new String[0]);
    }

    @Override
    public boolean add(@Nonnull String object) {
        return repository.add(object);
    }

    @Override
    public boolean remove(@Nonnull String object) {
        return repository.remove(object);
    }

    @Override
    public boolean set(@Nonnull String oldObject, @Nonnull String newObject) {
        if (oldObject.equals(newObject)) return true;
        return repository.set(oldObject, newObject);
    }

    @Override
    public boolean clear() {
        return repository.clear();
    }

    @Override
    public boolean rewrite(@Nonnull Collection<String>areas){
        return repository.rewrite(areas);
    }

    @Override
    public boolean resetToDefault() {
        return rewrite(DefaultAreas.get());
    }

    @Override
    public boolean add(@Nonnull Collection<String> areas) {
        Set<String>old = new LinkedHashSet<>(repository.getAll());
        old.addAll(areas);
        return repository.rewrite(old);
    }
}