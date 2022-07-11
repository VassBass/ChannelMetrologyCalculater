package service;

import model.Person;

import java.util.List;

public interface PersonService {
    List<Person> getAll();

    /**
     * @return array with first String equals "<Порожньо>"
     */
    String[] getAllNamesWithFirstEmptyString();

    /**
     * @return array with first String equals "<Порожньо>"
     */
    String[] getNamesOfHeadsWithFirstEmptyString();

    boolean add(Person person);
    boolean add(List<Person>persons);
    boolean remove(Person person);
    boolean set(Person person);

    Person get(int id);

    boolean clear();
    boolean rewrite(List<Person>persons);
    boolean resetToDefault();
}
