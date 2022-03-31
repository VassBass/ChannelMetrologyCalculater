package repository;

import model.Person;

import java.util.ArrayList;

public interface PersonRepository {
    ArrayList<Person>getAll();

    //return array with first String equals "<Порожньо>"
    String[] getAllNamesWithFirstEmptyString();

    //return array with first String equals "<Порожньо>"
    String[] getNamesOfHeadsWithFirstEmptyString();

    Person get(int index);
    void add(Person person);
    void addInCurrentThread(ArrayList<Person>persons);
    void addInCurrentThread(Person person);
    void remove(Person person);
    void set(Person oldPerson, Person newPerson);
    void setInCurrentThread(Person oldPerson, Person newPerson);
    void clear();
    void rewriteInCurrentThread(ArrayList<Person>persons);
    void rewrite(ArrayList<Person>persons);
    boolean backgroundTaskIsRun();
}
