package service.impl;

import application.Application;
import def.DefaultAreas;
import repository.AreaRepository;
import repository.impl.AreaRepositoryImpl;
import service.AreaService;
import service.FileBrowser;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.Logger;

public class AreaServiceImpl implements AreaService {
    private static final Logger LOGGER = Logger.getLogger(AreaService.class.getName());

    private final AreaRepository repository;

    private static final String ERROR = "Помилка";

    private ArrayList<String> areas;

    private String exportFileName(Calendar date){
        return "export_areas ["
                + date.get(Calendar.DAY_OF_MONTH)
                + "."
                + (date.get(Calendar.MONTH) + 1)
                + "."
                + date.get(Calendar.YEAR)
                + "].are";
    }

    public AreaServiceImpl(){
        this.repository = new AreaRepositoryImpl();
    }

    public AreaServiceImpl(String dbUrl){
        this.repository = new AreaRepositoryImpl(dbUrl);
    }

    @Override
    public void init(){
        LOGGER.fine("AreaService: initialization start ...");
        this.areas = this.repository.getAll();
        LOGGER.info("Initialization SUCCESS");
    }

    @Override
    public ArrayList<String> getAll() {
        return this.areas;
    }

    @Override
    public String[] getAllInStrings(){
        return this.areas.toArray(new String[0]);
    }

    @Override
    public ArrayList<String> add(String object) {
        if (object != null && !this.areas.contains(object)){
            this.areas.add(object);
            this.repository.add(object);
        }
        return this.areas;
    }

    @Override
    public ArrayList<String> remove(String object) {
        if (object != null) {
            if (this.areas.contains(object)) {
                this.areas.remove(object);
                this.repository.remove(object);
            } else {
                this.showNotFoundMessage();
            }
        }
        return this.areas;
    }

    @Override
    public ArrayList<String> set(String oldObject, String newObject) {
        if (oldObject != null && newObject != null){
            int index = this.areas.indexOf(oldObject);
            if (index >= 0) {
                this.areas.set(index, newObject);
                this.repository.set(oldObject, newObject);
            }
        }
        return this.areas;
    }

    @Override
    public String get(int index) {
        return index < 0 | index >= this.areas.size() ? null : this.areas.get(index);
    }

    @Override
    public void clear() {
        this.areas.clear();
        this.repository.clear();
    }

    @Override
    public boolean exportData(){
        try {
            String fileName = this.exportFileName(Calendar.getInstance());
            FileBrowser.saveToFile(FileBrowser.exportFile(fileName), this.areas);
            return true;
        }catch (IOException e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void rewriteInCurrentThread(ArrayList<String>areas){
        if (areas != null) {
            this.areas = areas;
            this.repository.rewriteInCurrentThread(areas);
        }
    }

    @Override
    public void resetToDefault() {
        this.areas = DefaultAreas.get();
        this.repository.rewrite(this.areas);
    }

    private void showNotFoundMessage() {
        if (Application.context != null) {
            String message = "Ділянка з такою назвою не знайдена в списку ділянок.";
            JOptionPane.showMessageDialog(Application.context.mainScreen, message, ERROR, JOptionPane.ERROR_MESSAGE);
        }
    }
}
