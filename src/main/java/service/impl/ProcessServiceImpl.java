package service.impl;

import def.DefaultProcesses;
import repository.Repository;
import repository.impl.ProcessRepositorySQLite;
import service.ProcessService;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

public class ProcessServiceImpl implements ProcessService {
    private static ProcessServiceImpl service;

    private final Repository<String> repository;
    private final Set<String>mainSet;

    private ProcessServiceImpl(){
        this.repository = new ProcessRepositorySQLite();
        mainSet = new LinkedHashSet<>(repository.getAll());
    }

    public ProcessServiceImpl(@Nonnull Repository<String> repository){
        this.repository = repository;
        mainSet = new LinkedHashSet<>(this.repository.getAll());
    }

    public static ProcessServiceImpl getInstance(){
        if (service == null) service = new ProcessServiceImpl();
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
    public boolean rewrite(Collection<String>processes){
        if (repository.rewrite(processes)){
            mainSet.clear();
            return mainSet.addAll(processes);
        }else return false;
    }

    @Override
    public boolean resetToDefault() {
        return rewrite(DefaultProcesses.get());
    }

    public boolean add(ArrayList<String> processes) {
        if (mainSet.addAll(processes)){
            return repository.rewrite(mainSet);
        }else return false;
    }
}