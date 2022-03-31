package service.impl;

import def.DefaultPersons;
import model.Person;
import repository.PersonRepository;
import repository.impl.PersonRepositoryImpl;
import service.PersonService;

import java.util.ArrayList;
import java.util.logging.Logger;

public class PersonServiceImpl implements PersonService {
    private static final Logger LOGGER = Logger.getLogger(PersonService.class.getName());

    private final String dbUrl;
    private PersonRepository repository;

    public PersonServiceImpl(){
        this.dbUrl = null;
    }

    public PersonServiceImpl(String dbUrl){
        this.dbUrl = dbUrl;
    }

    @Override
    public void init(){
        this.repository = this.dbUrl == null ? new PersonRepositoryImpl() : new PersonRepositoryImpl(this.dbUrl);
        LOGGER.info("Initialization SUCCESS");
    }

    @Override
    public ArrayList<Person> getAll() {
        return this.repository.getAll();
    }

    @Override
    public String[] getAllNamesWithFirstEmptyString(){
        return this.repository.getAllNamesWithFirstEmptyString();
    }

    @Override
    public String[] getNamesOfHeadsWithFirstEmptyString(){
        return this.repository.getNamesOfHeadsWithFirstEmptyString();
    }

    @Override
    public ArrayList<Person> add(Person person) {
        this.repository.add(person);
        return this.repository.getAll();
    }

    @Override
    public void addInCurrentThread(ArrayList<Person> persons) {
        this.repository.addInCurrentThread(persons);
    }

    @Override
    public void addInCurrentThread(Person person) {
        this.repository.addInCurrentThread(person);
    }

    @Override
    public ArrayList<Person> remove(Person person) {
        this.repository.remove(person);
        return this.repository.getAll();
    }

    @Override
    public ArrayList<Person> set(Person oldPerson, Person newPerson) {
        this.repository.set(oldPerson, newPerson);
        return this.repository.getAll();
    }

    @Override
    public void setInCurrentThread(Person oldPerson, Person newPerson) {
        this.repository.setInCurrentThread(oldPerson, newPerson);
    }

    @Override
    public Person get(int index) {
        return this.repository.get(index);
    }

    @Override
    public void clear() {
        this.repository.clear();
    }

    @Override
    public void rewriteInCurrentThread(ArrayList<Person>persons){
        this.repository.rewriteInCurrentThread(persons);
    }

    @Override
    public void resetToDefaultInCurrentThread(){
        this.repository.rewriteInCurrentThread(DefaultPersons.get());
    }

    @Override
    public boolean backgroundTaskIsRun() {
        return this.repository.backgroundTaskIsRun();
    }
}