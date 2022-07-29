package service.impl;

import def.DefaultProcesses;
import repository.Repository;
import repository.impl.ProcessRepositorySQLite;
import service.ProcessService;

import java.util.Collection;

public class ProcessServiceImpl implements ProcessService {
    private final Repository<String> repository;

    public ProcessServiceImpl(){
        this.repository = new ProcessRepositorySQLite();
    }

    public ProcessServiceImpl(Repository<String> repository){
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
    public boolean rewrite(Collection<String>processes){
        return this.repository.rewrite(processes);
    }

    @Override
    public boolean resetToDefault() {
        return this.repository.rewrite(DefaultProcesses.get());
    }
}