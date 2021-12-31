package controller;

import constants.Strings;
import model.Model;
import repository.Repository;
import ui.main.MainScreen;

import javax.swing.*;
import java.util.ArrayList;

public class ProcessesController implements Controller<String> {
    private final MainScreen mainScreen;
    private final ArrayList<String> processes;

    public ProcessesController(MainScreen mainScreen){
        this.mainScreen = mainScreen;
        this.processes = new Repository<String>(null, Model.PROCESS).readList();
    }

    @Override
    public void resetToDefault() {
        this.processes.clear();

        String barmak = "Бармак";
        String section = "Секція";
        String tract = "Тракт";
        String line = "Технологічна лінія";

        this.processes.add(barmak);
        this.processes.add(section);
        this.processes.add(tract);
        this.processes.add(line);

        this.save();
    }

    @Override
    public ArrayList<String> getAll() {
        return this.processes;
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
    public void remove(String object) {
        if (this.processes.contains(object)){
            this.processes.remove(object);
            this.save();
        }else {
            this.showNotFoundMessage();
        }
    }

    @Override
    public void set(String oldObject, String newObject) {
        if (oldObject != null){
            if (newObject == null){
                this.remove(oldObject);
            }else {
                int index = this.processes.indexOf(oldObject);
                this.processes.set(index, newObject);
            }
            this.save();
        }
    }

    @Override
    public String get(String object) {
        int index = this.processes.indexOf(object);
        if (index >= 0) {
            return this.processes.get(index);
        }else {
            this.showNotFoundMessage();
            return null;
        }
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
        new Repository<String>(this.mainScreen, Model.PROCESS).writeList(this.processes);
    }

    @Override
    public void showNotFoundMessage() {
        String message = "Процес з такою назвою не знайдено в списку процесів.";
        JOptionPane.showMessageDialog(this.mainScreen, message, Strings.ERROR, JOptionPane.ERROR_MESSAGE);
    }
}