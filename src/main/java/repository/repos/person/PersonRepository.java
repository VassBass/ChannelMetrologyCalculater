package repository.repos.person;

import model.dto.Person;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;

public interface PersonRepository {
    Collection<Person> getAll();
    @Nullable Person getById(@Nonnegative int id);

    boolean add(@Nonnull Person person);
    boolean addAll(@Nonnull Collection<Person> persons);

    boolean set(@Nonnull Person person);
    boolean rewrite(@Nonnull Collection<Person> persons);

    boolean remove(@Nonnull Person person);
    boolean clear();
}
