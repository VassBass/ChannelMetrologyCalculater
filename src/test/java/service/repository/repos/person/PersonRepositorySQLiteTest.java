package service.repository.repos.person;

import model.dto.Person;
import org.junit.*;
import org.sqlite.JDBC;
import service.repository.config.RepositoryConfigHolder;
import service.repository.config.SqliteRepositoryConfigHolder;
import service.repository.connection.RepositoryDBConnector;
import service.repository.connection.SqliteRepositoryDBConnector;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import static org.junit.Assert.*;

public class PersonRepositorySQLiteTest {
    private static final String TEST_REPOSITORY_PROPERTIES_FILE = "properties/test_repository.properties";
    private static final File TEST_DB_FILE = new File("TestData.db");
    private static final String TEST_DB_URL = "jdbc:sqlite:TestData.db";
    private static final String TABLE_NAME = "persons";

    private List<Person> expected;
    private PersonRepository repository;

    private Person createPerson(int number){
        Person person = new Person();
        person.setId(number);
        person.setName("name" + number);
        person.setSurname("surname" + number);
        person.setPatronymic("patronymic" + number);
        person.setPosition("position" + number);

        return person;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @BeforeClass
    public static void createDB() throws IOException, SQLException {
        TEST_DB_FILE.createNewFile();
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
    public static void removeDB() throws SQLException {
        String sql = String.format("DROP TABLE IF EXISTS %s", TABLE_NAME);
        DriverManager.registerDriver(new JDBC());
        try (Connection connection = DriverManager.getConnection(TEST_DB_URL);
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
        }
    }

    @Before
    public void init() throws SQLException {
        expected = new ArrayList<>(7);
        expected.addAll(Arrays.asList(
                createPerson(0),
                createPerson(1),
                createPerson(2),
                createPerson(3),
                createPerson(4),
                createPerson(5),
                createPerson(6)
        ));

        String sql = String.format("INSERT INTO %s (id, name, surname, patronymic, position) VALUES ", TABLE_NAME);
        StringBuilder sqlBuilder = new StringBuilder(sql);

        for (Person person : expected) {
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
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testGetByIdExisted() {
        assertEquals(createPerson(0), repository.getById(0));
        assertEquals(createPerson(5), repository.getById(5));
    }

    @Test
    public void testGetByIdNotExisted() {
        assertNull(repository.getById(50));
    }

    @Test
    public void testAddNotExisted() {
        Person person7 = createPerson(7);
        expected.add(person7);

        assertTrue(repository.add(person7));
        assertEquals(person7, repository.getById(7));

        Collection<Person> actual = repository.getAll();
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testAddExisted() {
        Person person2 = createPerson(2);

        assertFalse(repository.add(person2));
        assertEquals(person2, repository.getById(2));
        assertEquals(1, repository.getAll().stream().filter(p -> p.getId() == 2).count());

        Collection<Person> actual = repository.getAll();
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testSetNew() {
        Person person2 = createPerson(8);
        person2.setId(2);
        expected.set(2, person2);

        assertTrue(repository.set(person2));
        assertNull(repository.getById(8));
        assertEquals(person2, repository.getById(2));
        assertEquals(1, repository.getAll().stream().filter(p -> p.getId() == 2).count());

        Collection<Person> actual = repository.getAll();
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testSetExisted() {
        Person person2 = createPerson(2);

        assertTrue(repository.set(person2));
        assertEquals(person2, repository.getById(2));
        assertEquals(1, repository.getAll().stream().filter(p -> p.getId() == 2).count());

        Collection<Person> actual = repository.getAll();
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testAllNewCollection(){
        Person person7 = createPerson(7);
        Person person8 = createPerson(8);
        List<Person>toAdd = Arrays.asList(person7, person8);
        expected.addAll(toAdd);

        assertTrue(repository.addAll(toAdd));
        assertEquals(person7, repository.getById(7));
        assertEquals(person8, repository.getById(8));

        Collection<Person> actual = repository.getAll();
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testAddAllCollectionWithExists(){
        Person person2 = createPerson(2);
        Person person7 = createPerson(7);
        Person person8 = createPerson(8);
        List<Person>toAdd = Arrays.asList(person7, person8, person2);
        expected.addAll(Arrays.asList(person7, person8));

        assertTrue(repository.addAll(toAdd));
        assertEquals(person7, repository.getById(7));
        assertEquals(person8, repository.getById(8));
        assertEquals(person2, repository.getById(2));
        assertEquals(1, repository.getAll().stream().filter(p -> p.getId() == 2).count());

        Collection<Person> actual = repository.getAll();
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testAllNewCollectionWithNull(){
        Person person7 = createPerson(7);
        Person person8 = createPerson(8);
        List<Person>toAdd = Arrays.asList(person7, person8, null);
        expected.addAll(Arrays.asList(person7, person8));

        assertTrue(repository.addAll(toAdd));
        assertEquals(person7, repository.getById(7));
        assertEquals(person8, repository.getById(8));
        assertFalse(repository.getAll().stream().anyMatch(Objects::isNull));

        Collection<Person> actual = repository.getAll();
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testRemoveExisted() {
        Person person2 = createPerson(2);
        expected.remove(2);

        assertTrue(repository.remove(person2));
        assertNull(repository.getById(2));

        Collection<Person> actual = repository.getAll();
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testRemoveNotExisted() {
        assertFalse(repository.remove(createPerson(8)));
        assertNull(repository.getById(8));

        Collection<Person> actual = repository.getAll();
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testClear() {
        assertTrue(repository.clear());
        assertEquals(0, repository.getAll().size());
    }

    @Test
    public void testRewriteNotEmpty() {
        Person person7 = createPerson(7);
        Person person8 = createPerson(8);
        Person person9 = createPerson(9);
        List<Person> expected = Arrays.asList(person7, person8, person9);
        List<Person> toRewrite = Arrays.asList(person7, person8, null, person9);

        assertTrue(repository.rewrite(toRewrite));
        assertEquals(person7, repository.getById(7));
        assertEquals(person8, repository.getById(8));
        assertEquals(person9, repository.getById(9));
        assertFalse(repository.getAll().stream().anyMatch(Objects::isNull));

        Collection<Person> actual = repository.getAll();
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testRewriteEmpty() {
        assertTrue(repository.rewrite(new ArrayList<>()));
        assertEquals(0, repository.getAll().size());
    }
}