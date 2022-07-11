package service.impl;

import def.DefaultInstallations;
import repository.InstallationRepository;
import repository.impl.InstallationRepositoryImpl;
import service.InstallationService;

import java.util.List;

public class InstallationServiceImpl implements InstallationService {
    private final InstallationRepository repository;

    public InstallationServiceImpl(){
        this.repository = new InstallationRepositoryImpl();
    }

    public InstallationServiceImpl(InstallationRepository repository){
        this.repository = repository;
    }

    @Override
    public List<String> getAll() {
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
    public boolean rewrite(List<String>installations){
        return this.repository.rewrite(installations);
    }

    @Override
    public boolean resetToDefault() {
        return this.repository.rewrite(DefaultInstallations.get());
    }
}