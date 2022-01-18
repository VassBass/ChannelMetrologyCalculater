package controller;

import model.Model;
import repository.Repository;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

public class AreasController {
    private static final String ERROR = "Помилка";

    private Window window;
    private ArrayList<String>areas;

    private String exportFileName(Calendar date){
        return "export_areas ["
                + date.get(Calendar.DAY_OF_MONTH)
                + "."
                + (date.get(Calendar.MONTH) + 1)
                + "."
                + date.get(Calendar.YEAR)
                + "].are";
    }

    public void init(Window window){
        try {
            this.areas = new Repository<String>(null, Model.AREA).readList();
        }catch (Exception e){
            System.out.println("File \"" + FileBrowser.FILE_AREAS.getName() + "\" is empty");
            this.areas = this.resetToDefault();
        }
        this.window = window;
    }

    public ArrayList<String> resetToDefault() {
        if (this.areas == null){
            this.areas = new ArrayList<>();
        }else this.areas.clear();

        String OPU1 = "ОВДЗ-1";
        String OPU2 = "ОВДЗ-2";
        String OPU3 = "ОВДЗ-3";
        String OPU4 = "ОВДЗ-4";
        String CPO1 = "ЦВО-1";
        String CPO2 = "ЦВО-2";

        this.areas.add(OPU1);
        this.areas.add(OPU2);
        this.areas.add(OPU3);
        this.areas.add(OPU4);
        this.areas.add(CPO1);
        this.areas.add(CPO2);

        this.save();
        return this.areas;
    }

    public void rewrite(ArrayList<String>newAreasList){
        this.areas = newAreasList;
        this.save();
    }

    public ArrayList<String> getAll() {
        return this.areas;
    }

    public String[] getAllInStrings(){
        return this.areas.toArray(new String[0]);
    }

    public ArrayList<String> add(String object) {
        if (!this.areas.contains(object)){
            this.areas.add(object);
            this.save();
        }
        return this.areas;
    }

    public ArrayList<String> remove(String object) {
        if (this.areas.contains(object)){
            this.areas.remove(object);
            this.save();
        }else {
            this.showNotFoundMessage();
        }
        return this.areas;
    }

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

    public int getIndex(String object) {
        return this.areas.indexOf(object);
    }

    public String get(int index) {
        if (index >= 0) {
            return this.areas.get(index);
        }else {
            return null;
        }
    }

    public void clear() {
        this.areas.clear();
        this.save();
    }

    private void save() {
        new Repository<String>(this.window, Model.AREA).writeList(this.areas);
    }

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

    public void rewriteAll(ArrayList<String>areas){
        this.areas = areas;
        this.save();
    }

    private void showNotFoundMessage() {
        String message = "Ділянка з такою назвою не знайдена в списку ділянок.";
        JOptionPane.showMessageDialog(this.window, message, ERROR, JOptionPane.ERROR_MESSAGE);
    }
}
