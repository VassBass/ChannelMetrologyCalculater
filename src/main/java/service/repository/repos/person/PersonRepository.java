package service.repository.repos.person;

import model.Person;
import repository.Repository;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Optional;

public interface PersonRepository extends Repository<Person> {
    Optional<Person> getById(@Nonnegative int id);
    boolean add(@Nonnull Collection<Person> persons);
    @Override
    boolean set(@Nonnull Person oldO, @Nullable Person newO);
    boolean set(@Nonnull Person person);
}
