package service;

import model.Person;

import java.util.ArrayList;

public interface PersonService {
    ArrayList<Person> getAll();

    /**
     * @return array with first String equals "<Порожньо>"
     */
    String[] getAllNamesWithFirstEmptyString();

    /**
     * @return array with first String equals "<Порожньо>"
     */
    String[] getNamesOfHeadsWithFirstEmptyString();

    boolean add(Person person);
    boolean add(ArrayList<Person>persons);
    boolean remove(Person person);
    boolean set(Person person);

    Person get(int id);

    boolean clear();
    boolean rewrite(ArrayList<Person>persons);
    boolean resetToDefault();
}
