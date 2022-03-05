package service.impl;

import application.Application;
import constants.WorkPositions;
import def.DefaultPersons;
import model.Person;
import repository.PersonRepository;
import repository.impl.PersonRepositoryImpl;
import service.FileBrowser;
import service.PersonService;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.Logger;

public class PersonServiceImpl implements PersonService {
    private static final Logger LOGGER = Logger.getLogger(PersonService.class.getName());

    private static final String EMPTY_ARRAY = "<Порожньо>";
    private static final String ERROR = "Помилка";

    private final PersonRepository repository;
    private ArrayList<Person> persons;

    private String exportFileName(Calendar date){
        return "export_persons ["
                + date.get(Calendar.DAY_OF_MONTH)
                + "."
                + (date.get(Calendar.MONTH) + 1)
                + "."
                + date.get(Calendar.YEAR)
                + "].per";
    }

    public PersonServiceImpl(){
        this.repository = new PersonRepositoryImpl();
        this.init();
    }

    public PersonServiceImpl(String dbUrl){
        this.repository = new PersonRepositoryImpl(dbUrl);
        this.init();
    }

    @Override
    public void init(){
        this.persons = this.repository.getAll();
        LOGGER.info("Initialization SUCCESS");
    }

    @Override
    public ArrayList<Person> getAll() {
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
        for (Person worker : this.persons){
            if (worker.getPosition().equals(WorkPositions.HEAD_OF_DEPARTMENT_ASUTP)){
                heads.add(worker.getFullName());
            }
        }
        return heads.toArray(new String[0]);
    }

    @Override
    public ArrayList<Person> add(Person person) {
        if (person != null){
            if (!this.persons.contains(person)){
                this.persons.add(person);
                this.repository.add(person);
            }else {
                this.showExistMessage();
            }
        }
        return this.persons;
    }

    @Override
    public ArrayList<Person> remove(Person person) {
        if (person != null){
            if (this.persons.contains(person)) {
                this.persons.remove(person);
                this.repository.remove(person);
            }else {
                this.showNotFoundMessage();
            }
        }
        return this.persons;
    }

    @Override
    public ArrayList<Person> set(Person oldPerson, Person newPerson) {
        if (oldPerson != null && newPerson != null){
            int index = this.persons.indexOf(oldPerson);
            if (index >= 0){
                this.persons.set(index, newPerson);
                this.repository.set(oldPerson, newPerson);
            }
        }
        return this.persons;
    }

    @Override
    public Person get(int index) {
        if (index >= 0) {
            return this.persons.get(index);
        }else {
            return null;
        }
    }

    @Override
    public void clear() {
        this.persons.clear();
        this.repository.clear();
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
    public void rewriteInCurrentThread(ArrayList<Person>persons){
        this.persons = persons;
        this.repository.rewriteInCurrentThread(persons);
    }

    @Override
    public void resetToDefault(){
        this.persons = DefaultPersons.get();
        this.repository.rewrite(this.persons);
    }

    private void showNotFoundMessage() {
        if (Application.context != null) {
            String message = "Працівник не знайден в списку працівників.";
            JOptionPane.showMessageDialog(Application.context.mainScreen, message, ERROR, JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showExistMessage() {
        if (Application.context != null) {
            String message = "Працівник з такими даними вже існує в списку працівниців.-";
            JOptionPane.showMessageDialog(Application.context.mainScreen, message, ERROR, JOptionPane.ERROR_MESSAGE);
        }
    }
}