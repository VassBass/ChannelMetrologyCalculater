package service.impl;

import def.DefaultInstallations;
import repository.Repository;
import repository.impl.InstallationRepositorySQLite;
import service.InstallationService;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

public class InstallationServiceImpl implements InstallationService {
    private static InstallationServiceImpl service;

    private final Repository<String> repository;

    private InstallationServiceImpl(){
        repository = new InstallationRepositorySQLite();
    }

    public InstallationServiceImpl(@Nonnull Repository<String> repository){
        this.repository = repository;
        service = this;
    }

    public static InstallationServiceImpl getInstance() {
        if (service == null) service = new InstallationServiceImpl();
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
    public boolean rewrite(@Nonnull Collection<String>installations){
        return repository.rewrite(installations);
    }

    @Override
    public boolean resetToDefault() {
        return rewrite(DefaultInstallations.get());
    }

    @Override
    public boolean add(@Nonnull Collection<String> installations) {
        Set<String>old = new LinkedHashSet<>(repository.getAll());
        old.addAll(installations);
        return repository.rewrite(old);
    }
}