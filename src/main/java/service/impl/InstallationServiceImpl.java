package service.impl;

import def.DefaultInstallations;
import repository.Repository;
import repository.impl.InstallationRepositorySQLite;
import service.InstallationService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

public class InstallationServiceImpl implements InstallationService {
    private final Repository<String> repository;
    private final Set<String> mainSet;

    public InstallationServiceImpl(){
        repository = new InstallationRepositorySQLite();
        mainSet = new LinkedHashSet<>(repository.getAll());
    }

    public InstallationServiceImpl(Repository<String> repository){
        this.repository = repository;
        mainSet = new LinkedHashSet<>(this.repository.getAll());
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
    public boolean add(String object) {
        if (object == null) return false;

        if (mainSet.add(object)) {
            return repository.add(object);
        } else return false;
    }

    @Override
    public boolean remove(String object) {
        if (object == null) return false;

        if (mainSet.remove(object)) {
            return repository.remove(object);
        } else return false;
    }

    @Override
    public boolean set(String oldObject, String newObject) {
        if (oldObject == null || newObject == null) return false;
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
    public boolean rewrite(Collection<String>installations){
        if (repository.rewrite(installations)){
            mainSet.clear();
            return mainSet.addAll(installations);
        }else return false;
    }

    @Override
    public boolean resetToDefault() {
        return rewrite(DefaultInstallations.get());
    }
}