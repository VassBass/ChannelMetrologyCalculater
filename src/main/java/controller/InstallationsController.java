package controller;

import constants.Strings;
import model.Model;
import repository.Repository;
import ui.main.MainScreen;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;

public class InstallationsController implements Controller<String> {
    private final MainScreen mainScreen;
    private final ArrayList<String> installations;

    public InstallationsController(MainScreen mainScreen){
        this.mainScreen = mainScreen;
        this.installations = new Repository<String>(null, Model.INSTALLATION).readList();
    }

    @Override
    public void resetToDefault() {
        this.installations.clear();

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
    }

    @Override
    public ArrayList<String> getAll() {
        return installations;
    }

    @Override
    public void add(String object) {
        if (!this.installations.contains(object)){
            this.installations.add(object);
            this.save();
        }
    }

    @Override
    public void remove(String object) {
        if (this.installations.contains(object)){
            this.installations.remove(object);
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
                int index = this.installations.indexOf(oldObject);
                this.installations.set(index, newObject);
            }
            this.save();
        }
    }

    @Override
    public String get(String object) {
        int index = this.installations.indexOf(object);
        if (index >= 0) {
            return this.installations.get(index);
        }else {
            this.showNotFoundMessage();
            return null;
        }
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
        new Repository<String>(this.mainScreen, Model.INSTALLATION).writeList(this.installations);
    }

    @Override
    public void showNotFoundMessage() {
        String message = "Установка з такою назвою не знайдена в списку установок.";
        JOptionPane.showMessageDialog(this.mainScreen, message, Strings.ERROR, JOptionPane.ERROR_MESSAGE);
    }
}
