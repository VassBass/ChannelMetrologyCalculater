package service.impl;

import def.DefaultProcesses;
import model.Model;
import repository.Repository;
import service.FileBrowser;
import service.ProcessService;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.Logger;

public class ProcessServiceImpl implements ProcessService {
    private static final Logger LOGGER = Logger.getLogger(ProcessService.class.getName());

    private static final String ERROR = "Помилка";

    private Window window;
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
    public void init(Window window){
        LOGGER.info("ProcessService: initialization start ...");
        try {
            this.processes = new Repository<String>(null, Model.PROCESS).readList();
        }catch (Exception e){
            LOGGER.info("ProcessService: file \"" + FileBrowser.FILE_PROCESSES.getName() + "\" is empty");
            LOGGER.info("ProcessService: set default list");
            this.processes = DefaultProcesses.get();
            this.save();
        }
        this.window = window;
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
        if (!this.processes.contains(object)){
            this.processes.add(object);
            this.save();
        }
        return this.processes;
    }

    @Override
    public ArrayList<String> remove(String object) {
        if (this.processes.contains(object)){
            this.processes.remove(object);
            this.save();
        }else {
            this.showNotFoundMessage();
        }
        return this.processes;
    }

    @Override
    public ArrayList<String> set(String oldObject, String newObject) {
        if (oldObject != null){
            if (newObject == null){
                this.remove(oldObject);
            }else {
                int index = this.processes.indexOf(oldObject);
                this.processes.set(index, newObject);
            }
            this.save();
        }
        return this.processes;
    }

    @Override
    public String get(int index) {
        if (index >= 0) {
            return this.processes.get(index);
        }else {
            return null;
        }
    }

    @Override
    public void clear() {
        this.processes.clear();
        this.save();
    }

    @Override
    public void save() {
        new Repository<String>(this.window, Model.PROCESS).writeList(this.processes);
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
        this.processes = processes;
        new Repository<String>(null, Model.PROCESS).writeListInCurrentThread(processes);
    }

    private void showNotFoundMessage() {
        String message = "Процес з такою назвою не знайдено в списку процесів.";
        JOptionPane.showMessageDialog(this.window, message, ERROR, JOptionPane.ERROR_MESSAGE);
    }
}
