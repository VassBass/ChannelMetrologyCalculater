package service.impl;

import def.DefaultProcesses;
import repository.ProcessRepository;
import repository.impl.ProcessRepositoryImpl;
import service.ProcessService;

import java.util.ArrayList;

public class ProcessServiceImpl implements ProcessService {
    private final ProcessRepository repository;

    public ProcessServiceImpl(){
        this.repository = new ProcessRepositoryImpl();
    }

    public ProcessServiceImpl(ProcessRepository repository){
        this.repository = repository;
    }

    @Override
    public ArrayList<String> getAll() {
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
    public boolean rewrite(ArrayList<String>processes){
        return this.repository.rewrite(processes);
    }

    @Override
    public boolean resetToDefault() {
        return this.repository.rewrite(DefaultProcesses.get());
    }
}