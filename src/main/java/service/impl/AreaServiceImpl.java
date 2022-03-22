package service.impl;

import def.DefaultAreas;
import repository.AreaRepository;
import repository.impl.AreaRepositoryImpl;
import service.AreaService;

import java.util.ArrayList;
import java.util.logging.Logger;

public class AreaServiceImpl implements AreaService {
    private static final Logger LOGGER = Logger.getLogger(AreaService.class.getName());

    private final String dbUrl;
    private AreaRepository repository;

    public AreaServiceImpl(){
        this.dbUrl = null;
    }

    public AreaServiceImpl(String dbUrl){
        this.dbUrl = dbUrl;
    }

    @Override
    public void init(){
        this.repository = this.dbUrl == null ? new AreaRepositoryImpl() : new AreaRepositoryImpl(this.dbUrl);
        LOGGER.info("Initialization SUCCESS");
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
    public ArrayList<String> add(String object) {
        this.repository.add(object);
        return this.repository.getAll();
    }

    @Override
    public void addInCurrentThread(ArrayList<String> areas) {
        this.repository.addInCurrentThread(areas);
    }

    @Override
    public ArrayList<String> remove(String object) {
        this.repository.remove(object);
        return this.repository.getAll();
    }

    @Override
    public ArrayList<String> set(String oldObject, String newObject) {
        this.repository.set(oldObject, newObject);
        return this.repository.getAll();
    }

    @Override
    public String get(int index) {
        return this.repository.get(index);
    }

    @Override
    public void clear() {
        this.repository.clear();
    }

    @Override
    public void exportData(){
        this.repository.export();
    }

    @Override
    public void rewriteInCurrentThread(ArrayList<String>areas){
        this.repository.rewriteInCurrentThread(areas);
    }

    @Override
    public void resetToDefault() {
        this.repository.rewrite(DefaultAreas.get());
    }
}