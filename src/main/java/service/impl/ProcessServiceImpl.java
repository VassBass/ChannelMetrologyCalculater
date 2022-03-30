package service.impl;

import def.DefaultProcesses;
import repository.ProcessRepository;
import repository.impl.ProcessRepositoryImpl;
import service.ProcessService;

import java.util.ArrayList;
import java.util.logging.Logger;

public class ProcessServiceImpl implements ProcessService {
    private static final Logger LOGGER = Logger.getLogger(ProcessService.class.getName());

    private final String dbUrl;
    private ProcessRepository repository;

    public ProcessServiceImpl(){
        this.dbUrl = null;
    }

    public ProcessServiceImpl(String dbUrl){
        this.dbUrl = dbUrl;
    }

    @Override
    public void init(){
        this.repository = dbUrl == null ? new ProcessRepositoryImpl() : new ProcessRepositoryImpl(this.dbUrl);
        LOGGER.info("ProcessService: initialization SUCCESS");
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
    public void addInCurrentThread(ArrayList<String> processes) {
        this.repository.addInCurrentThread(processes);
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
    public void rewriteInCurrentThread(ArrayList<String>processes){
        this.repository.rewriteInCurrentThread(processes);
    }

    @Override
    public void resetToDefaultInCurrentThread() {
        this.repository.rewriteInCurrentThread(DefaultProcesses.get());
    }

    @Override
    public boolean backgroundTaskIsRun() {
        return this.repository.backgroundTaskIsRun();
    }
}