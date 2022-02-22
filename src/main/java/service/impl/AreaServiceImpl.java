package service.impl;

import def.DefaultAreas;
import model.Model;
import repository.Repository;
import service.AreaService;
import service.FileBrowser;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.Logger;

public class AreaServiceImpl implements AreaService {
    private static final Logger LOGGER = Logger.getLogger(AreaService.class.getName());

    private static final String ERROR = "Помилка";

    private Window window;
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

    @Override
    public void init(Window window){
        LOGGER.info("AreaService: initialization start ...");
        try {
            this.areas = new Repository<String>(null, Model.AREA).readList();
        }catch (Exception e){
            LOGGER.info("AreaService: file \"" + FileBrowser.FILE_AREAS.getName() + "\" is empty");
            LOGGER.info("AreaService: set default list");
            this.areas = DefaultAreas.get();
            this.save();
        }
        this.window = window;
        LOGGER.info("AreaService: initialization SUCCESS");
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
        if (!this.areas.contains(object)){
            this.areas.add(object);
            this.save();
        }
        return this.areas;
    }

    @Override
    public ArrayList<String> remove(String object) {
        if (this.areas.contains(object)){
            this.areas.remove(object);
            this.save();
        }else {
            this.showNotFoundMessage();
        }
        return this.areas;
    }

    @Override
    public ArrayList<String> set(String oldObject, String newObject) {
        if (oldObject != null){
            if (newObject == null){
                this.remove(oldObject);
            }else {
                int index = this.areas.indexOf(oldObject);
                this.areas.set(index, newObject);
            }
            this.save();
        }
        return this.areas;
    }

    @Override
    public String get(int index) {
        if (index >= 0) {
            return this.areas.get(index);
        }else {
            return null;
        }
    }

    @Override
    public void clear() {
        this.areas.clear();
        this.save();
    }

    @Override
    public void save() {
        new Repository<String>(this.window, Model.AREA).writeList(this.areas);
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
        this.areas = areas;
        new Repository<String>(null, Model.AREA).writeListInCurrentThread(areas);
    }

    private void showNotFoundMessage() {
        String message = "Ділянка з такою назвою не знайдена в списку ділянок.";
        JOptionPane.showMessageDialog(this.window, message, ERROR, JOptionPane.ERROR_MESSAGE);
    }
}
