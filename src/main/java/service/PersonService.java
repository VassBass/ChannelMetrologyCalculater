package service;

import model.Person;

import java.util.List;

public interface PersonService extends Service<Person> {
    /**
     * @return array with first String equals "<Порожньо>"
     */
    String[] getAllNamesWithFirstEmptyString();

    /**
     * @return array with first String equals "<Порожньо>"
     */
    String[] getNamesOfHeadsWithFirstEmptyString();

    boolean add(List<Person>persons);
    boolean set(Person person);

    Person get(int id);

    boolean resetToDefault();
}
