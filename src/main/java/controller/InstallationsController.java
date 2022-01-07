package controller;

import constants.Strings;
import model.Model;
import repository.Repository;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class InstallationsController {
    private Window window;
    private ArrayList<String> installations;

    public void init(Window window){
        try {
            this.installations = new Repository<String>(null, Model.INSTALLATION).readList();
        }catch (Exception e){
            System.out.println("File \"" + FileBrowser.FILE_INSTALLATIONS.getName() + "\" is empty");
            this.installations = this.resetToDefault();
        }
        this.window = window;
    }

    public ArrayList<String> resetToDefault() {
        if (this.installations == null){
            this.installations = new ArrayList<>();
        }else this.installations.clear();

        String conveyor = "Конвеєр";
        String KKD = "ККД (Конусна крупного дроблення)";
        String KSD = "КСД (Конусна среднього дроблення)";
        String KMD = "КМД (Конусна мілкого дроблення)";
        String grohot = "Грохот";
        String bunker = "Бункер";
        String mill = "Млин";
        String klasifikator1 = "Класифікатор односпіральний";
        String klasifikator2 = "Класифікатор двухспіральний";
        String ZUMPF = "ЗУМПФ";
        String gidrociklon710 = "Гідроциклон 710мм";
        String gidrociklon350 = "Гідроциклон 350мм";
        String pump = "Насос";
        String MGS5 = "МГС-5";
        String MGS9 = "МГС-9";
        String MS = "Магнітний сепаратор";
        String FM = "Флотомашина";
        String ventilator = "Вентилятор";
        String pich = "Піч";
        String reshotka = "Решітка";
        String cooller = "Охолоджувач";

        this.installations.add(conveyor);
        this.installations.add(KKD);
        this.installations.add(KSD);
        this.installations.add(KMD);
        this.installations.add(grohot);
        this.installations.add(bunker);
        this.installations.add(mill);
        this.installations.add(klasifikator1);
        this.installations.add(klasifikator2);
        this.installations.add(ZUMPF);
        this.installations.add(gidrociklon710);
        this.installations.add(gidrociklon350);
        this.installations.add(pump);
        this.installations.add(MGS5);
        this.installations.add(MGS9);
        this.installations.add(MS);
        this.installations.add(FM);
        this.installations.add(ventilator);
        this.installations.add(pich);
        this.installations.add(reshotka);
        this.installations.add(cooller);

        this.save();
        return this.installations;
    }

    public ArrayList<String> getAll() {
        return this.installations;
    }

    public String[] getAllInStrings(){
        return this.installations.toArray(new String[0]);
    }

    public ArrayList<String> add(String object) {
        if (!this.installations.contains(object)){
            this.installations.add(object);
            this.save();
        }
        return this.installations;
    }

    public ArrayList<String> remove(String object) {
        if (this.installations.contains(object)){
            this.installations.remove(object);
            this.save();
        }else {
            this.showNotFoundMessage();
        }
        return this.installations;
    }

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

    public int getIndex(String object) {
        return this.installations.indexOf(object);
    }

    public String get(int index) {
        if (index >= 0) {
            return this.installations.get(index);
        }else {
            return null;
        }
    }

    public void clear() {
        this.installations.clear();
        this.save();
    }

    private void save() {
        new Repository<String>(this.window, Model.INSTALLATION).writeList(this.installations);
    }

    private void showNotFoundMessage() {
        String message = "Установка з такою назвою не знайдена в списку установок.";
        JOptionPane.showMessageDialog(this.window, message, Strings.ERROR, JOptionPane.ERROR_MESSAGE);
    }
}
