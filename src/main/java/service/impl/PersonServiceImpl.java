package service.impl;

import model.Person;
import service.repository.repos.person.PersonRepository;
import service.repository.repos.person.PersonRepositorySQLite;
import service.PersonService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PersonServiceImpl implements PersonService {
    private static PersonServiceImpl instance;

    private final PersonRepository repository;

    private static final String EMPTY_ARRAY = "<Порожньо>";

    private PersonServiceImpl(){
        this.repository = PersonRepositorySQLite.getInstance();
    }

    public PersonServiceImpl(PersonRepository repository){
        this.repository = repository;
    }

    public static PersonServiceImpl getInstance() {
        if (instance == null) instance = new PersonServiceImpl();

        return instance;
    }

    @Override
    public String[] getAllNamesWithFirstEmptyString(){
        List<String> names = new ArrayList<>();
        names.add(EMPTY_ARRAY);
        names.addAll(repository.getAll().stream().map(Person::_getFullName).collect(Collectors.toList()));
        return names.toArray(new String[0]);
    }

    @Override
    public String[] getNamesOfHeadsWithFirstEmptyString(){
        List<String>heads = new ArrayList<>();
        heads.add(EMPTY_ARRAY);
        heads.addAll(repository.getAll().stream()
                .filter(p -> p.getPosition().equals(Person.HEAD_OF_DEPARTMENT_ASUTP))
                .map(Person::_getFullName)
                .collect(Collectors.toList()));
        return heads.toArray(new String[0]);
    }
}