package service;

import model.Person;

import java.util.Collection;

public interface PersonService extends Service<Person> {
    /**
     * @return array with first String equals "<Порожньо>"
     */
    String[] getAllNamesWithFirstEmptyString();

    /**
     * @return array with first String equals "<Порожньо>"
     */
    String[] getNamesOfHeadsWithFirstEmptyString();

    boolean add(Collection<Person> persons);
    boolean set(Person person);

    Person get(int id);

    boolean resetToDefault();
}
