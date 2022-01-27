package controller;

import model.Model;
import repository.Repository;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

public class ProcessesController {
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

    public void init(Window window){
        try {
            this.processes = new Repository<String>(null, Model.PROCESS).readList();
        }catch (Exception e){
            System.out.println("File \"" + FileBrowser.FILE_PROCESSES.getName() + "\" is empty");
            this.processes = this.resetToDefault();
        }
        this.window = window;
    }

    public ArrayList<String> resetToDefault() {
        if (this.processes == null){
            this.processes = new ArrayList<>();
        }else this.processes.clear();

        String barmak = "Бармак";
        String section = "Секція";
        String tract = "Тракт";
        String line = "Технологічна лінія";

        this.processes.add(barmak);
        this.processes.add(section);
        this.processes.add(tract);
        this.processes.add(line);

        this.save();
        return this.processes;
    }

    public ArrayList<String> getAll() {
        return this.processes;
    }

    public String[] getAllInStrings(){
        return this.processes.toArray(new String[0]);
    }

    public ArrayList<String> add(String object) {
        if (!this.processes.contains(object)){
            this.processes.add(object);
            this.save();
        }
        return this.processes;
    }

    public ArrayList<String> remove(String object) {
        if (this.processes.contains(object)){
            this.processes.remove(object);
            this.save();
        }else {
            this.showNotFoundMessage();
        }
        return this.processes;
    }

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

    public String get(int index) {
        if (index >= 0) {
            return this.processes.get(index);
        }else {
            return null;
        }
    }

    public void clear() {
        this.processes.clear();
        this.save();
    }

    private void save() {
        new Repository<String>(this.window, Model.PROCESS).writeList(this.processes);
    }

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

    public void rewriteAll(ArrayList<String>processes){
        this.processes = processes;
        new Repository<String>(null, Model.PROCESS).writeListInCurrentThread(processes);
    }

    private void showNotFoundMessage() {
        String message = "Процес з такою назвою не знайдено в списку процесів.";
        JOptionPane.showMessageDialog(this.window, message, ERROR, JOptionPane.ERROR_MESSAGE);
    }
}