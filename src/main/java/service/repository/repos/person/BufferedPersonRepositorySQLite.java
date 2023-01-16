package service.repository.repos.person;

import model.Person;
import service.repository.config.RepositoryConfigHolder;
import service.repository.connection.RepositoryDBConnector;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public class BufferedPersonRepositorySQLite extends PersonRepositorySQLite {
    private final Map<Integer, Person> buffer;

    public BufferedPersonRepositorySQLite(RepositoryConfigHolder configHolder, RepositoryDBConnector connector) {
        super(configHolder, connector);
        buffer = getAll().stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(Person::getId, Function.identity()));
    }

    @Override
    public Collection<Person> getAll() {
        return buffer.values();
    }

    @Override
    public Person getById(int id) {
        return buffer.get(id);
    }

    @Override
    public boolean add(@Nonnull Person person) {
        return
    }

    @Override
    public boolean add(@Nonnull Collection<Person> persons) {
        return super.add(persons);
    }

    @Override
    public boolean remove(@Nonnull Person person) {
        return super.remove(person);
    }

    @Override
    public boolean set(@Nonnull Person person) {
        return super.set(person);
    }

    @Override
    public boolean clear() {
        return super.clear();
    }

    @Override
    public boolean rewrite(@Nonnull Collection<Person> persons) {
        return super.rewrite(persons);
    }
}
