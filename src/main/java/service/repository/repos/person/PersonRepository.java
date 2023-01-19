package service.repository.repos.person;

import model.Person;
import repository.Repository;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

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
