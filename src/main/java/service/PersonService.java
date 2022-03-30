package service;

import model.Person;

import java.util.ArrayList;

public interface PersonService {
    void init();
    ArrayList<Person> getAll();
    String[] getAllNames();
    String[] getNamesOfHeads();

    // if person is exists in list or person == null does nothing
    ArrayList<Person> add(Person person);
    void addInCurrentThread(ArrayList<Person>persons);
    void addInCurrentThread(Person person);
    ArrayList<Person> remove(Person person);
    ArrayList<Person> set(Person oldPerson, Person newPerson);
    void setInCurrentThread(Person oldPerson, Person newPerson);

    // if (index < 0 || index > personsList.size()) return null
    Person get(int index);

    void clear();
    void rewriteInCurrentThread(ArrayList<Person>persons);
    void resetToDefaultInCurrentThread();
    boolean backgroundTaskIsRun();
}
