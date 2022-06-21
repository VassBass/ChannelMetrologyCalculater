package service.impl;

import def.DefaultAreas;
import repository.AreaRepository;
import repository.impl.AreaRepositoryImpl;
import service.AreaService;

import java.util.ArrayList;

public class AreaServiceImpl implements AreaService {

    private final AreaRepository repository;

    public AreaServiceImpl(){
        repository = new AreaRepositoryImpl();
    }

    public AreaServiceImpl(AreaRepository repository){
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
    public boolean rewrite(ArrayList<String>areas){
        return this.repository.rewrite(areas);
    }

    @Override
    public boolean resetToDefault() {
        return this.repository.rewrite(DefaultAreas.get());
    }
}