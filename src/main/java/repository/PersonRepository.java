package repository;

import model.Person;

import java.util.Collection;

public interface PersonRepository extends Repository<Person>{
    String[] getAllNamesWithFirstEmptyString();
    String[] getNamesOfHeadsWithFirstEmptyString();
    Person get(int id);

    boolean add(Collection<Person> persons);

    boolean set(Person person);
}
