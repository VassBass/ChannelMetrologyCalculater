package repository.repos.person;

import model.dto.Person;
import repository.config.RepositoryConfigHolder;
import repository.connection.RepositoryDBConnector;

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
        buffer = super.getAll().stream()
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
        int id = person.getId();
        if (id < 0) {
            id = PersonIdGenerator.generateForMap(buffer);
            person.setId(id);
        }
        if (buffer.containsKey(person.getId())) return false;

        buffer.put(id, person);
        return super.add(person);
    }

    @Override
    public boolean addAll(@Nonnull Collection<Person> persons) {
        for (Person p : persons) {
            if (p == null || buffer.containsKey(p.getId())) continue;
            if (p.getId() < 0) p.setId(PersonIdGenerator.generateForMap(buffer));
            buffer.put(p.getId(), p);
        }
        return super.rewrite(buffer.values());
    }

    @Override
    public boolean remove(@Nonnull Person person) {
        return buffer.remove(person.getId()) != null && super.remove(person);
    }

    @Override
    public boolean set(@Nonnull Person person) {
        return buffer.put(person.getId(), person) !=null && super.set(person);
    }

    @Override
    public boolean clear() {
        buffer.clear();
        return super.clear();
    }

    @Override
    public boolean rewrite(@Nonnull Collection<Person> persons) {
        buffer.clear();
        buffer.putAll(persons.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(Person::getId, Function.identity())));
        return super.rewrite(persons);
    }
}
