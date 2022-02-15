package service.impl;

import def.DefaultInstallations;
import model.Model;
import repository.Repository;
import service.FileBrowser;
import service.InstallationService;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

public class InstallationServiceImpl implements InstallationService {
    private static final String ERROR = "Помилка";

    private Window window;
    private ArrayList<String> installations;

    private String exportFileName(Calendar date){
        return "export_installations ["
                + date.get(Calendar.DAY_OF_MONTH)
                + "."
                + (date.get(Calendar.MONTH) + 1)
                + "."
                + date.get(Calendar.YEAR)
                + "].ins";
    }

    @Override
    public void init(Window window){
        try {
            this.installations = new Repository<String>(null, Model.INSTALLATION).readList();
        }catch (Exception e){
            System.out.println("File \"" + FileBrowser.FILE_INSTALLATIONS.getName() + "\" is empty");
            this.installations = DefaultInstallations.get();
            this.save();
        }
        this.window = window;
    }

    @Override
    public ArrayList<String> getAll() {
        return this.installations;
    }

    @Override
    public String[] getAllInStrings(){
        return this.installations.toArray(new String[0]);
    }

    @Override
    public ArrayList<String> add(String object) {
        if (!this.installations.contains(object)){
            this.installations.add(object);
            this.save();
        }
        return this.installations;
    }

    @Override
    public ArrayList<String> remove(String object) {
        if (this.installations.contains(object)){
            this.installations.remove(object);
            this.save();
        }else {
            this.showNotFoundMessage();
        }
        return this.installations;
    }

    @Override
    public ArrayList<String> set(String oldObject, String newObject) {
        if (oldObject != null){
            if (newObject == null){
                this.remove(oldObject);
            }else {
                int index = this.installations.indexOf(oldObject);
                this.installations.set(index, newObject);
            }
            this.save();
        }
        return this.installations;
    }

    @Override
    public String get(int index) {
        if (index >= 0) {
            return this.installations.get(index);
        }else {
            return null;
        }
    }

    @Override
    public void clear() {
        this.installations.clear();
        this.save();
    }

    @Override
    public void save() {
        new Repository<String>(this.window, Model.INSTALLATION).writeList(this.installations);
    }

    @Override
    public boolean exportData(){
        try {
            String fileName = this.exportFileName(Calendar.getInstance());
            FileBrowser.saveToFile(FileBrowser.exportFile(fileName), this.installations);
            return true;
        }catch (IOException e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void rewriteInCurrentThread(ArrayList<String>installations){
        this.installations = installations;
        new Repository<String>(null, Model.INSTALLATION).writeListInCurrentThread(installations);
    }

    private void showNotFoundMessage() {
        String message = "Установка з такою назвою не знайдена в списку установок.";
        JOptionPane.showMessageDialog(this.window, message, ERROR, JOptionPane.ERROR_MESSAGE);
    }
}
