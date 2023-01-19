package service.repository.repos.person;

import model.Person;
import org.junit.*;
import service.repository.config.RepositoryConfigHolder;
import service.repository.config.SqliteRepositoryConfigHolder;
import service.repository.connection.RepositoryDBConnector;
import service.repository.connection.SqliteRepositoryDBConnector;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import static java.util.stream.Collectors.toList;
import static org.junit.Assert.*;

public class PersonRepositorySQLiteTest {
    private static final String TEST_REPOSITORY_PROPERTIES_FILE = "properties/test_repository.properties";
    private static final File TEST_DB_FILE = new File("TestData.db");
    private static final String TEST_DB_URL = "jdbc:sqlite:TestData.db";
    private static final String TABLE_NAME = "persons";

    private List<Person> testPersons;
    private PersonRepository repository;

    private static Person createPerson(int number, boolean boss){
        Person person = new Person();
        person.setId(number);
        person.setName("name" + number);
        person.setSurname("surname" + number);
        person.setPatronymic("patronymic" + number);
        person.setPosition(boss ? Person.HEAD_OF_DEPARTMENT_ASUTP : "position" + number);

        return person;
    }

    @BeforeClass
    public static void createDB() throws IOException, SQLException {
        Files.createFile(TEST_DB_FILE.toPath());
        String sql = String.format("CREATE TABLE IF NOT EXISTS %s (" +
                "id integer NOT NULL UNIQUE" +
                ", name text NOT NULL" +
                ", surname text NOT NULL" +
                ", patronymic text" +
                ", position text NOT NULL" +
                ", PRIMARY KEY (id AUTOINCREMENT)" +
                ");", TABLE_NAME);
        try (Connection connection = DriverManager.getConnection(TEST_DB_URL);
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
        }
    }

    @AfterClass
    public static void removeDB() throws IOException {
        Files.delete(TEST_DB_FILE.toPath());
    }

    @Before
    public void init() throws SQLException {
        testPersons = new ArrayList<>(7);
        testPersons.addAll(Arrays.asList(
                createPerson(0,false),
                createPerson(1,false),
                createPerson(2,false),
                createPerson(3,false),
                createPerson(4,false),
                createPerson(5,true),
                createPerson(6,true)
        ));

        String sql = String.format("INSERT INTO %s (id, name, surname, patronymic, position) VALUES ", TABLE_NAME);
        StringBuilder sqlBuilder = new StringBuilder(sql);

        for (Person person : testPersons) {
            String values = String.format("(%s, '%s', '%s', '%s', '%s'),",
                    person.getId(),
                    person.getName(),
                    person.getSurname(),
                    person.getPatronymic(),
                    person.getPosition());
            sqlBuilder.append(values);
        }
        sqlBuilder.setCharAt(sqlBuilder.length() - 1, ';');

        try (Connection connection = DriverManager.getConnection(TEST_DB_URL);
                Statement statement = connection.createStatement()) {
            statement.execute(sqlBuilder.toString());
        }

        RepositoryConfigHolder configHolder = new SqliteRepositoryConfigHolder(TEST_REPOSITORY_PROPERTIES_FILE);
        RepositoryDBConnector connector = new SqliteRepositoryDBConnector(configHolder);
        repository = new PersonRepositorySQLite(configHolder, connector);
    }

    @After
    public void clearTestDB() throws Exception {
        String sql = String.format("DELETE FROM %s;", TABLE_NAME);
        try (Connection connection = DriverManager.getConnection(TEST_DB_URL);
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
        }
    }

    @Test
    public void testGetAll() {
        Collection<Person> actual = repository.getAll();
        assertEquals(testPersons.size(), actual.size());
        assertTrue(testPersons.containsAll(actual));
    }

    @Test
    public void testGetByIdExisted() {
        assertEquals(createPerson(0,false), repository.getById(0));
        assertEquals(createPerson(5, true), repository.getById(5));
    }

    @Test
    public void testGetByIdNotExisted() {
        assertNull(repository.getById(50));
    }

    @Test
    public void testAddNotExisted() {
        testPersons.add(createPerson(7, false));

        assertTrue(repository.add(createPerson(7, false)));
        Collection<Person> actual = repository.getAll();
        assertEquals(testPersons.size(), actual.size());
        assertTrue(testPersons.containsAll(actual));
    }

    @Test
    public void testAddExisted() {
        assertFalse(repository.add(createPerson(2, false)));
        Collection<Person> actual = repository.getAll();
        assertEquals(testPersons.size(), actual.size());
        assertTrue(testPersons.containsAll(actual));
    }

    @Test
    public void testSetNew() {
        Person person2 = createPerson(8, false);
        person2.setId(2);
        testPersons.set(2, person2);

        assertTrue(repository.set(person2));
        Collection<Person> actual = repository.getAll();
        assertEquals(testPersons.size(), actual.size());
        assertTrue(testPersons.containsAll(actual));
    }

    @Test
    public void testSetExisted() {
        assertTrue(repository.set(createPerson(2, false)));
        Collection<Person> actual = repository.getAll();
        assertEquals(testPersons.size(), actual.size());
        assertTrue(testPersons.containsAll(actual));
    }

    @Test
    public void testAllNewCollection(){
        List<Person>toAdd = Arrays.asList(
                createPerson(7,false),
                createPerson(8,false)
        );
        testPersons.addAll(toAdd);

        assertTrue(repository.addAll(toAdd));
        Collection<Person> actual = repository.getAll();
        assertEquals(testPersons.size(), actual.size());
        assertTrue(testPersons.containsAll(actual));
    }

    @Test
    public void testAddAllCollectionWithExists(){
        Person person2 = createPerson(2, false);
        Person person7 = createPerson(7, false);
        Person person8 = createPerson(8, false);
        List<Person>toAdd = Arrays.asList(person7, person8, person2);
        testPersons.addAll(Arrays.asList(person7, person8));

        assertTrue(repository.addAll(toAdd));
        Collection<Person> actual = repository.getAll();
        assertEquals(testPersons.size(), actual.size());
        assertTrue(testPersons.containsAll(actual));
    }

    @Test
    public void testAllNewCollectionWithNull(){
        List<Person>toAdd = Arrays.asList(
                createPerson(7,false),
                createPerson(8,false),
                null
        );
        testPersons.addAll(toAdd.stream().filter(Objects::nonNull).collect(toList()));

        assertTrue(repository.addAll(toAdd));
        Collection<Person> actual = repository.getAll();
        assertEquals(testPersons.size(), actual.size());
        assertTrue(testPersons.containsAll(actual));
    }

    @Test
    public void testRemoveExisted() {
        testPersons.remove(testPersons.size() - 1);

        assertTrue(repository.remove(createPerson(6, true)));
        Collection<Person> actual = repository.getAll();
        assertEquals(testPersons.size(), actual.size());
        assertTrue(testPersons.containsAll(actual));
    }

    @Test
    public void testRemoveNotExisted() {
        assertFalse(repository.remove(createPerson(8, true)));
        Collection<Person> actual = repository.getAll();
        assertEquals(testPersons.size(), actual.size());
        assertTrue(testPersons.containsAll(actual));
    }

    @Test
    public void testClear() {
        assertTrue(repository.clear());
        assertEquals(0, repository.getAll().size());
    }

    @Test
    public void testRewriteNotEmpty() {
        List<Person> expected = Arrays.asList(
                createPerson(7, false),
                createPerson(8,false),
                createPerson(9, false)
        );

        List<Person> toRewrite = Arrays.asList(
                createPerson(7, false),
                createPerson(8,false),
                null,
                createPerson(9, false)
        );

        assertTrue(repository.rewrite(toRewrite));
        Collection<Person> actual = repository.getAll();
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testRewriteEmpty() {
        assertTrue(repository.rewrite(new ArrayList<>()));
        Collection<Person> actual = repository.getAll();
        assertEquals(0, actual.size());
    }
}