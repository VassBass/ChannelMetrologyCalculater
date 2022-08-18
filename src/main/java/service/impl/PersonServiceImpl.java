package service.impl;

import def.DefaultPersons;
import model.Person;
import repository.PersonRepository;
import repository.impl.PersonRepositorySQLite;
import service.PersonService;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Optional;

public class PersonServiceImpl implements PersonService {
    private static PersonServiceImpl service;

    private final PersonRepository repository;

    private PersonServiceImpl(){
        this.repository = new PersonRepositorySQLite();
    }

    public PersonServiceImpl(PersonRepository repository){
        this.repository = repository;
    }

    public static PersonServiceImpl getInstance() {
        if (service == null) service = new PersonServiceImpl();

        return service;
    }

    @Override
    public Collection<Person> getAll() {
        return this.repository.getAll();
    }

    @Override
    public String[] getAllNamesWithFirstEmptyString(){
        return this.repository.getAllNamesWithFirstEmptyString();
    }

    @Override
    public String[] getNamesOfHeadsWithFirstEmptyString(){
        return this.repository.getNamesOfHeadsWithFirstEmptyString();
    }

    @Override
    public boolean add(@Nonnull Person person) {
        return this.repository.add(person);
    }

    @Override
    public boolean add(@Nonnull Collection<Person> persons) {
        return this.repository.add(persons);
    }

    @Override
    public boolean remove(@Nonnull Person person) {
        return this.repository.remove(person);
    }

    @Override
    public boolean set(@Nonnull Person person, @Nullable Person ignored) {
        return repository.set(person, ignored);
    }

    @Override
    public boolean set(@Nonnull Person person) {
        return this.repository.set(person);
    }

    @Override
    public Optional<Person> get(@Nonnegative int id) {
        return this.repository.get(id);
    }

    @Override
    public boolean clear() {
        return this.repository.clear();
    }

    @Override
    public boolean rewrite(@Nonnull Collection<Person>persons){
        return this.repository.rewrite(persons);
    }

    @Override
    public boolean resetToDefault(){
        return this.repository.rewrite(DefaultPersons.get());
    }
}