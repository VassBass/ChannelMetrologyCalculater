package service.impl;

import def.DefaultDepartments;
import repository.DepartmentRepository;
import repository.impl.DepartmentRepositoryImpl;
import service.DepartmentService;

import java.util.ArrayList;
import java.util.logging.Logger;

public class DepartmentServiceImpl implements DepartmentService {
    private static final Logger LOGGER = Logger.getLogger(DepartmentService.class.getName());

    private DepartmentRepository repository;

    private final String dbUrl;

    public DepartmentServiceImpl(){
        this.dbUrl = null;
    }

    public DepartmentServiceImpl(String dbUrl){
        this.dbUrl = dbUrl;
    }

    @Override
    public void init(){
        this.repository = this.dbUrl == null ? new DepartmentRepositoryImpl() : new DepartmentRepositoryImpl(this.dbUrl);
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
    public void addInCurrentThread(ArrayList<String> departments) {
        this.repository.addInCurrentThread(departments);
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
    public void rewriteInCurrentThread(ArrayList<String>departments){
        this.repository.rewriteInCurrentThread(departments);
    }

    @Override
    public void resetToDefaultInCurrentThread() {
        this.repository.rewriteInCurrentThread(DefaultDepartments.get());
    }

    @Override
    public boolean backgroundTaskIsRun() {
        return this.repository.backgroundTaskIsRun();
    }
}