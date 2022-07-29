package service.impl;

import def.DefaultAreas;
import repository.Repository;
import repository.impl.AreaRepositorySQLite;
import service.AreaService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

public class AreaServiceImpl implements AreaService {

    private final Repository<String> repository;
    private final Set<String> mainSet;

    public AreaServiceImpl(){
        repository = new AreaRepositorySQLite();
        mainSet = new LinkedHashSet<>(repository.getAll());
    }

    public AreaServiceImpl(Repository<String> repository){
        this.repository = repository == null ? new AreaRepositorySQLite() : repository;
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
        } else {
            return false;
        }
    }

    @Override
    public boolean remove(String object) {
        if (object == null) return false;

        if (mainSet.remove(object)) {
            return repository.remove(object);
        } else {
            return false;
        }
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
        }else {
            return false;
        }
    }

    @Override
    public boolean rewrite(Collection<String>areas){
        if (repository.rewrite(areas)){
            mainSet.clear();
            return mainSet.addAll(areas);
        }else {
            return false;
        }
    }

    @Override
    public boolean resetToDefault() {
        return rewrite(DefaultAreas.get());
    }
}