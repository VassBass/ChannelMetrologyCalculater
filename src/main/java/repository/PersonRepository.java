package repository;

import model.Person;

import javax.annotation.Nonnull;
import java.util.Collection;

public interface PersonRepository extends Repository<Person>{
    String[] getAllNamesWithFirstEmptyString();
    String[] getNamesOfHeadsWithFirstEmptyString();
    Person get(int id);

    boolean add(@Nonnull Collection<Person> persons);

    boolean set(@Nonnull Person person);
}
