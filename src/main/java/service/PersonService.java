package service;

import model.Person;

import java.util.ArrayList;

public interface PersonService {
    void init();
    ArrayList<Person> getAll();
    String[] getAllNames();
    String[] getNamesOfHeads();
    ArrayList<Person> add(Person person);
    ArrayList<Person> remove(Person person);
    ArrayList<Person> set(Person oldPerson, Person newPerson);
    Person get(int index);
    void clear();
    boolean exportData();
    void rewriteInCurrentThread(ArrayList<Person>persons);
    void resetToDefault();
}
