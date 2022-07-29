package service.impl;

import def.DefaultInstallations;
import repository.Repository;
import repository.impl.InstallationRepositorySQLite;
import service.InstallationService;

import java.util.Collection;

public class InstallationServiceImpl implements InstallationService {
    private final Repository<String> repository;

    public InstallationServiceImpl(){
        this.repository = new InstallationRepositorySQLite();
    }

    public InstallationServiceImpl(Repository<String> repository){
        this.repository = repository;
    }

    @Override
    public Collection<String> getAll() {
        return this.repository.getAll();
    }

    @Override
    public String[] getAllInStrings(){
        return this.repository.getAll().toArray(new String[0]);
    }

    @Override
    public boolean add(String object) {
        return this.repository.add(object);
    }

    @Override
    public boolean remove(String object) {
        return this.repository.remove(object);
    }

    @Override
    public boolean set(String oldObject, String newObject) {
        return this.repository.set(oldObject, newObject);
    }

    @Override
    public boolean clear() {
        return this.repository.clear();
    }

    @Override
    public boolean rewrite(Collection<String>installations){
        return this.repository.rewrite(installations);
    }

    @Override
    public boolean resetToDefault() {
        return this.repository.rewrite(DefaultInstallations.get());
    }
}