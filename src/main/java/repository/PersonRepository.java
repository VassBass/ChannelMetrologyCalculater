package repository;

import model.Person;

import java.util.ArrayList;

public interface PersonRepository {
    ArrayList<Person>getAll();

    String[] getAllNamesWithFirstEmptyString();

    String[] getNamesOfHeadsWithFirstEmptyString();

    Person get(int id);
    boolean add(Person person);
    boolean add(ArrayList<Person>persons);
    boolean remove(Person person);
    boolean set(Person person);
    boolean clear();
    boolean rewrite(ArrayList<Person>persons);
}
