package service;

import model.Person;

import java.util.ArrayList;

public interface PersonService {
    void init();
    ArrayList<Person> getAll();
    String[] getAllNames();
    String[] getNamesOfHeads();
    ArrayList<Person> add(Person person);
    void addInCurrentThread(Person person);
    ArrayList<Person> remove(Person person);
    ArrayList<Person> set(Person oldPerson, Person newPerson);
    void setInCurrentThread(Person oldPerson, Person newPerson);
    Person get(int index);
    void clear();
    void exportData();
    void rewriteInCurrentThread(ArrayList<Person>persons);
    void resetToDefault();
}
