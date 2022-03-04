package service.impl;

import application.Application;
import def.DefaultProcesses;
import repository.ProcessRepository;
import repository.impl.ProcessRepositoryImpl;
import service.FileBrowser;
import service.ProcessService;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.Logger;

public class ProcessServiceImpl implements ProcessService {
    private static final Logger LOGGER = Logger.getLogger(ProcessService.class.getName());

    private final ProcessRepository repository;

    private static final String ERROR = "Помилка";

    public ProcessServiceImpl(){
        this.repository = new ProcessRepositoryImpl();

    }

    public ProcessServiceImpl(String dbUrl){
        this.repository = new ProcessRepositoryImpl(dbUrl);
    }

    private ArrayList<String> processes;

    private String exportFileName(Calendar date){
        return "export_processes ["
                + date.get(Calendar.DAY_OF_MONTH)
                + "."
                + (date.get(Calendar.MONTH) + 1)
                + "."
                + date.get(Calendar.YEAR)
                + "].prc";
    }

    @Override
    public void init(){
        LOGGER.fine("ProcessService: initialization start ...");
        this.processes = this.repository.getAll();
        LOGGER.info("ProcessService: initialization SUCCESS");
    }

    @Override
    public ArrayList<String> getAll() {
        return this.processes;
    }

    @Override
    public String[] getAllInStrings(){
        return this.processes.toArray(new String[0]);
    }

    @Override
    public ArrayList<String> add(String object) {
        if (object != null && !this.processes.contains(object)){
            this.processes.add(object);
            this.repository.add(object);
        }
        return this.processes;
    }

    @Override
    public ArrayList<String> remove(String object) {
        if (object != null) {
            if (this.processes.contains(object)) {
                this.processes.remove(object);
                this.repository.remove(object);
            } else {
                this.showNotFoundMessage();
            }
        }
        return this.processes;
    }

    @Override
    public ArrayList<String> set(String oldObject, String newObject) {
        if (oldObject != null && newObject != null){
            int index = this.processes.indexOf(oldObject);
            if (index >= 0) {
                this.processes.set(index, newObject);
                this.repository.set(oldObject, newObject);
            }
        }
        return this.processes;
    }

    @Override
    public String get(int index) {
        return index < 0 | index >= this.processes.size() ? null : this.processes.get(index);
    }

    @Override
    public void clear() {
        this.processes.clear();
        this.repository.clear();
    }

    @Override
    public boolean exportData(){
        try {
            String fileName = this.exportFileName(Calendar.getInstance());
            FileBrowser.saveToFile(FileBrowser.exportFile(fileName), this.processes);
            return true;
        }catch (IOException e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void rewriteInCurrentThread(ArrayList<String>processes){
        if (processes != null) {
            this.processes = processes;
            this.repository.rewriteInCurrentThread(processes);
        }
    }

    @Override
    public void resetToDefault() {
        this.processes = DefaultProcesses.get();
        this.repository.rewrite(this.processes);
    }

    private void showNotFoundMessage() {
        if (Application.context != null) {
            String message = "Процес з такою назвою не знайдено в списку процесів.";
            JOptionPane.showMessageDialog(Application.context.mainScreen, message, ERROR, JOptionPane.ERROR_MESSAGE);
        }
    }
}
