package repository;

import model.Person;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;

public interface PersonRepository extends Repository<Person>{
    String[] getAllNamesWithFirstEmptyString();
    String[] getNamesOfHeadsWithFirstEmptyString();
    Person get(@Nonnegative int id);

    boolean add(@Nonnull Collection<Person> persons);

    @Override
    boolean set(@Nonnull Person oldO, @Nullable Person newO);

    boolean set(@Nonnull Person person);
}
