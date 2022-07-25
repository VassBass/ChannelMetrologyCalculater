package repository;

import model.Person;

import java.util.List;

public interface PersonRepository extends Repository<Person>{
    String[] getAllNamesWithFirstEmptyString();
    String[] getNamesOfHeadsWithFirstEmptyString();
    Person get(int id);

    boolean add(List<Person>persons);

    boolean set(Person person);
}
