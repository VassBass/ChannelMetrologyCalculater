package repository;

import model.Person;

import java.util.List;

public interface PersonRepository {
    List<Person> getAll();

    String[] getAllNamesWithFirstEmptyString();

    String[] getNamesOfHeadsWithFirstEmptyString();

    Person get(int id);
    boolean add(Person person);
    boolean add(List<Person>persons);
    boolean remove(Person person);
    boolean set(Person person);
    boolean clear();
    boolean rewrite(List<Person>persons);
}
