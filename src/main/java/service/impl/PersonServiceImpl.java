package service.impl;

import constants.WorkPositions;
import def.DefaultPersons;
import model.Model;
import model.Worker;
import repository.Repository;
import service.FileBrowser;
import service.PersonService;
import support.Comparator;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.Logger;

public class PersonServiceImpl implements PersonService {
    private static final Logger LOGGER = Logger.getLogger(PersonService.class.getName());

    private static final String EMPTY_ARRAY = "<Порожньо>";
    private static final String ERROR = "Помилка";

    private Window window;
    private ArrayList<Worker> persons;

    private String exportFileName(Calendar date){
        return "export_persons ["
                + date.get(Calendar.DAY_OF_MONTH)
                + "."
                + (date.get(Calendar.MONTH) + 1)
                + "."
                + date.get(Calendar.YEAR)
                + "].per";
    }

    @Override
    public void init(Window window){
        LOGGER.info("PersonService: initialization start ...");
        try {
            this.persons = new Repository<Worker>(null, Model.PERSON).readList();
        }catch (Exception e){
            LOGGER.info("PersonService: file \"" + FileBrowser.FILE_PERSONS.getName() + "\" is empty");
            LOGGER.info("PersonService: set default list");
            this.persons = DefaultPersons.get();
            this.save();
        }
        this.window = window;
        LOGGER.info("PersonService: initialization SUCCESS");
    }

    @Override
    public ArrayList<Worker> getAll() {
        return this.persons;
    }

    @Override
    public String[] getAllNames(){
        int length = this.persons.size() + 1;
        String[] persons = new String[length];
        persons[0] = EMPTY_ARRAY;
        for (int x = 0; x< this.persons.size(); x++){
            int y = x+1;
            persons[y] = this.persons.get(x).getFullName();
        }
        return persons;
    }

    @Override
    public String[] getNamesOfHeads(){
        ArrayList<String>heads = new ArrayList<>();
        heads.add(EMPTY_ARRAY);
        for (Worker worker : this.persons){
            if (worker.getPosition().equals(WorkPositions.HEAD_OF_DEPARTMENT_ASUTP)){
                heads.add(worker.getFullName());
            }
        }
        return heads.toArray(new String[0]);
    }

    @Override
    public ArrayList<Worker> add(Worker worker) {
        boolean exist = false;
        for (Worker person : this.persons){
            if (Comparator.personsMatch(person, worker)){
                exist = true;
                break;
            }
        }
        if (exist){
            this.showExistMessage();
        }else {
            this.persons.add(worker);
            this.save();
        }
        return this.persons;
    }

    @Override
    public ArrayList<Worker> remove(Worker person) {
        boolean removed = false;

        for (Worker worker : this.persons){
            if (Comparator.personsMatch(worker, person)){
                this.persons.remove(worker);
                removed = true;
                break;
            }
        }

        if (removed){
            this.save();
        }else {
            this.showNotFoundMessage();
        }
        return this.persons;
    }

    @Override
    public ArrayList<Worker> set(Worker oldPerson, Worker newPerson) {
        if (oldPerson != null){
            if (newPerson == null){
                this.remove(oldPerson);
            }else {
                if (!Comparator.personsMatch(oldPerson, newPerson)) {
                    for (int p=0;p<this.persons.size();p++){
                        Worker person = this.persons.get(p);
                        if (Comparator.personsMatch(person, oldPerson)){
                            this.persons.set(p, newPerson);
                            break;
                        }
                    }
                }
            }
            this.save();
        }
        return this.persons;
    }

    @Override
    public Worker get(int index) {
        if (index >= 0) {
            return this.persons.get(index);
        }else {
            return null;
        }
    }

    @Override
    public void clear() {
        this.persons.clear();
        this.save();
    }

    @Override
    public void save() {
        new Repository<Worker>(this.window, Model.PERSON).writeList(this.persons);
    }

    @Override
    public boolean exportData(){
        try {
            String fileName = this.exportFileName(Calendar.getInstance());
            FileBrowser.saveToFile(FileBrowser.exportFile(fileName), this.persons);
            return true;
        }catch (IOException e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void rewriteInCurrentThread(ArrayList<Worker>workers){
        this.persons = workers;
        new Repository<Worker>(null,Model.PERSON).writeListInCurrentThread(workers);
    }

    private void showNotFoundMessage() {
        String message = "Працівник не знайден в списку працівників.";
        JOptionPane.showMessageDialog(this.window, message, ERROR, JOptionPane.ERROR_MESSAGE);
    }

    private void showExistMessage() {
        String message = "Працівник з такими даними вже існує в списку працівниців.-";
        JOptionPane.showMessageDialog(this.window, message, ERROR, JOptionPane.ERROR_MESSAGE);
    }
}