package service.impl;

import application.Application;
import def.DefaultInstallations;
import repository.InstallationRepository;
import repository.impl.InstallationRepositoryImpl;
import service.FileBrowser;
import service.InstallationService;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.Logger;

public class InstallationServiceImpl implements InstallationService {
    private static final Logger LOGGER = Logger.getLogger(InstallationService.class.getName());

    private final InstallationRepository repository;

    private static final String ERROR = "Помилка";

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

    public InstallationServiceImpl(){
        this.repository = new InstallationRepositoryImpl();
    }

    public InstallationServiceImpl(String dbUrl){
        this.repository = new InstallationRepositoryImpl(dbUrl);
    }

    @Override
    public void init(){
        LOGGER.fine("Initialization start ...");
        this.installations = this.repository.getAll();
        LOGGER.info("Initialization SUCCESS");
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
        if (object != null && !this.installations.contains(object)){
            this.installations.add(object);
            this.repository.add(object);
        }
        return this.installations;
    }

    @Override
    public ArrayList<String> remove(String object) {
        if (object != null) {
            if (this.installations.contains(object)) {
                this.installations.remove(object);
                this.repository.remove(object);
            } else {
                this.showNotFoundMessage();
            }
        }
        return this.installations;
    }

    @Override
    public ArrayList<String> set(String oldObject, String newObject) {
        if (oldObject != null && newObject != null){
            int index = this.installations.indexOf(oldObject);
            if (index >= 0) {
                this.installations.set(index, newObject);
                this.repository.set(oldObject, newObject);
            }
        }
        return this.installations;
    }

    @Override
    public String get(int index) {
        return index < 0 | index >= this.installations.size() ? null : this.installations.get(index);
    }

    @Override
    public void clear() {
        this.installations.clear();
        this.repository.clear();
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
        if (installations != null) {
            this.installations = installations;
            this.repository.rewriteInCurrentThread(installations);
        }
    }

    @Override
    public void resetToDefault() {
        this.installations = DefaultInstallations.get();
        this.repository.rewrite(this.installations);
    }

    private void showNotFoundMessage() {
        if (Application.context != null) {
            String message = "Установка з такою назвою не знайдена в списку установок.";
            JOptionPane.showMessageDialog(Application.context.mainScreen, message, ERROR, JOptionPane.ERROR_MESSAGE);
        }
    }
}
