package service.impl;

import def.DefaultPersons;
import model.Person;
import repository.PersonRepository;
import repository.impl.PersonRepositoryImpl;
import service.PersonService;

import java.util.ArrayList;

public class PersonServiceImpl implements PersonService {
    private final PersonRepository repository;

    public PersonServiceImpl(){
        this.repository = new PersonRepositoryImpl();
    }

    public PersonServiceImpl(PersonRepository repository){
        this.repository = repository;
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
    public boolean add(Person person) {
        return this.repository.add(person);
    }

    @Override
    public boolean add(ArrayList<Person> persons) {
        return this.repository.add(persons);
    }

    @Override
    public boolean remove(Person person) {
        return this.repository.remove(person);
    }

    @Override
    public boolean set(Person person) {
        return this.repository.set(person);
    }

    @Override
    public Person get(int id) {
        return this.repository.get(id);
    }

    @Override
    public boolean clear() {
        return this.repository.clear();
    }

    @Override
    public boolean rewrite(ArrayList<Person>persons){
        return this.repository.rewrite(persons);
    }

    @Override
    public boolean resetToDefault(){
        return this.repository.rewrite(DefaultPersons.get());
    }
}