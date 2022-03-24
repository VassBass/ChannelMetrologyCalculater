package service.impl;

import def.DefaultInstallations;
import repository.InstallationRepository;
import repository.impl.InstallationRepositoryImpl;
import service.InstallationService;

import java.util.ArrayList;
import java.util.logging.Logger;

public class InstallationServiceImpl implements InstallationService {
    private static final Logger LOGGER = Logger.getLogger(InstallationService.class.getName());

    private final String dbUrl;
    private InstallationRepository repository;

    public InstallationServiceImpl(){
        this.dbUrl = null;
    }

    public InstallationServiceImpl(String dbUrl){
        this.dbUrl = dbUrl;
    }

    @Override
    public void init(){
        this.repository = this.dbUrl == null ? new InstallationRepositoryImpl() : new InstallationRepositoryImpl(this.dbUrl);
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
    public void addInCurrentThread(ArrayList<String> installations) {
        this.repository.addInCurrentThread(installations);
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
    public void rewriteInCurrentThread(ArrayList<String>installations){
        this.repository.rewriteInCurrentThread(installations);
    }

    @Override
    public void resetToDefault() {
        this.repository.rewriteInCurrentThread(DefaultInstallations.get());
    }
}