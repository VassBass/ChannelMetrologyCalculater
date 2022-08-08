package service.impl;

import def.DefaultInstallations;
import repository.Repository;
import repository.impl.InstallationRepositorySQLite;
import service.InstallationService;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

public class InstallationServiceImpl implements InstallationService {
    private static InstallationServiceImpl service;

    private final Repository<String> repository;
    private final Set<String> mainSet;

    private InstallationServiceImpl(){
        repository = new InstallationRepositorySQLite();
        mainSet = new LinkedHashSet<>(repository.getAll());
    }

    public InstallationServiceImpl(@Nonnull Repository<String> repository){
        this.repository = repository;
        mainSet = new LinkedHashSet<>(this.repository.getAll());
    }

    public static InstallationServiceImpl getInstance() {
        if (service == null) service = new InstallationServiceImpl();
        return service;
    }

    @Override
    public Collection<String> getAll() {
        return mainSet;
    }

    @Override
    public String[] getAllInStrings(){
        return mainSet.toArray(new String[0]);
    }

    @Override
    public boolean add(@Nonnull String object) {
        if (mainSet.add(object)) {
            return repository.add(object);
        } else return false;
    }

    @Override
    public boolean remove(@Nonnull String object) {
        if (mainSet.remove(object)) {
            return repository.remove(object);
        } else return false;
    }

    @Override
    public boolean set(@Nonnull String oldObject, @Nonnull String newObject) {
        if (oldObject.equals(newObject)) return true;

        ArrayList<String> list = new ArrayList<>(mainSet);
        int indexOfOld = list.indexOf(oldObject);
        int indexOfNew = list.indexOf(newObject);
        if (indexOfOld >= 0 && indexOfNew < 0){
            list.set(indexOfOld, newObject);
            mainSet.clear();
            mainSet.addAll(list);
            return true;
        }else return false;
    }

    @Override
    public boolean clear() {
        if (repository.clear()){
            mainSet.clear();
            return true;
        }else return false;
    }

    @Override
    public boolean rewrite(@Nonnull Collection<String>installations){
        if (repository.rewrite(installations)){
            mainSet.clear();
            return mainSet.addAll(installations);
        }else return false;
    }

    @Override
    public boolean resetToDefault() {
        return rewrite(DefaultInstallations.get());
    }

    @Override
    public boolean add(@Nonnull Collection<String> installations) {
        if (mainSet.addAll(installations)){
            return repository.rewrite(mainSet);
        }else return false;
    }
}