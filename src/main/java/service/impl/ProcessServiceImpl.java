package service.impl;

import def.DefaultProcesses;
import repository.Repository;
import repository.impl.ProcessRepositorySQLite;
import service.ProcessService;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

public class ProcessServiceImpl implements ProcessService {
    private static ProcessServiceImpl service;

    private final Repository<String> repository;

    private ProcessServiceImpl(){
        this.repository = new ProcessRepositorySQLite();
    }

    public ProcessServiceImpl(@Nonnull Repository<String> repository){
        this.repository = repository;
        service = this;
    }

    public static ProcessServiceImpl getInstance(){
        if (service == null) service = new ProcessServiceImpl();
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
    public boolean rewrite(@Nonnull Collection<String>processes){
        return repository.rewrite(processes);
    }

    @Override
    public boolean resetToDefault() {
        return rewrite(DefaultProcesses.get());
    }

    @Override
    public boolean add(@Nonnull Collection<String> processes) {
        Set<String> old = new LinkedHashSet<>(repository.getAll());
        old.addAll(processes);
        return rewrite(old);
    }
}