package service;

import model.Person;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNullableByDefault;
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

    boolean add(@Nonnull Collection<Person> persons);

    @Override
    boolean set(@Nonnull Person person, @Nullable Person ignored);
    boolean set(@Nonnull Person person);

    Person get(@Nonnegative int id);

    boolean resetToDefault();
}
