package service.impl;

import def.DefaultAreas;
import repository.Repository;
import repository.impl.AreaRepositorySQLite;
import service.AreaService;

import java.util.List;

public class AreaServiceImpl implements AreaService {

    private final Repository<String> repository;

    public AreaServiceImpl(){
        repository = new AreaRepositorySQLite();
    }

    public AreaServiceImpl(Repository<String> repository){
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
    public boolean rewrite(List<String>areas){
        return this.repository.rewrite(areas);
    }

    @Override
    public boolean resetToDefault() {
        return this.repository.rewrite(DefaultAreas.get());
    }
}