package service.repository.repos.person;

import model.dto.Person;

import java.util.Collection;
import java.util.Map;

public class PersonIdGenerator {
    private PersonIdGenerator(){}

    public static int generateForMap(Map<Integer, Person> map) {
        int id = 0;
        while (map.containsKey(id)) id++;
        return id;
    }

    public static int generateForCollection(Collection<Person> collection) {
        int id = 0;
        while (collection.contains(new Person(id))) id++;
        return id;
    }

    public static int generateForRepository(PersonRepository repository) {
        int id = 0;
        while (repository.getById(id) != null) id++;
        return id;
    }
}
