package repository;

import model.Person;

import java.util.ArrayList;

public interface PersonRepository {
    ArrayList<Person>getAll();
    String[] getAllNames();
    String[] getNamesOfHeads();
    Person get(int index);
    void add(Person person);
    void remove(Person person);
    void set(Person oldPerson, Person newPerson);
    void clear();
    void rewriteInCurrentThread(ArrayList<Person>persons);
    void rewrite(ArrayList<Person>persons);
    void export();
}
