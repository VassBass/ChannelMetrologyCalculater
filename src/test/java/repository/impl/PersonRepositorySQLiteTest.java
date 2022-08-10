package repository.impl;

import model.Person;
import org.junit.*;
import org.sqlite.JDBC;
import repository.PersonRepository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class PersonRepositorySQLiteTest {

    private static final String DB_URL = "jdbc:sqlite:TestData.db";
    private static final PersonRepository repository = new PersonRepositorySQLite(DB_URL, null, null);

    private static final String EMPTY_ARRAY = "<Порожньо>";

    private static Person[] testPersons;

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
    public static void testCreateTable(){
        assertTrue(repository.createTable());
    }

    @AfterClass
    public static void removeTable() throws SQLException {
        String sql = "DROP TABLE persons;";
        DriverManager.registerDriver(new JDBC());
        try (Connection connection = DriverManager.getConnection(DB_URL);
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
        }
    }

    @Before
    public void fuelingTestDB() throws SQLException {
        testPersons = new Person[7];
        int n = 0;
        for (;n < 5;n++){
            testPersons[n] = createPerson(n, false);
        }
        testPersons[n] = createPerson(n++, true);
        testPersons[n] = createPerson(n, true);

        String insertSql = "INSERT INTO persons (id, name, surname, patronymic, position) "
                + "VALUES ";
        StringBuilder sql = new StringBuilder(insertSql);

        for (Person p : testPersons) {
            sql.append("('").append(p.getId()).append("', ")
                    .append("'").append(p.getName()).append("', ")
                    .append("'").append(p.getSurname()).append("', ")
                    .append("'").append(p.getPatronymic()).append("', ")
                    .append("'").append(p.getPosition()).append("'),");
        }
        sql.setCharAt(sql.length()-1, ';');

        DriverManager.registerDriver(new JDBC());
        try (Connection connection = DriverManager.getConnection(DB_URL);
             Statement statement = connection.createStatement()) {
            statement.execute(sql.toString());
        }
    }

    @After
    public void clearTestDB() throws Exception {
        String sql = "DELETE FROM persons;";
        DriverManager.registerDriver(new JDBC());
        try (Connection connection = DriverManager.getConnection(DB_URL);
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
        }
    }

    @Test
    public void testGetAll(){
        assertArrayEquals(testPersons, repository.getAll().toArray(new Person[0]));
    }

    @Test
    public void testGetAllNamesWithFirstEmptyString(){
        String[]expected = new String[8];
        int i = 0;
        expected[i++] = EMPTY_ARRAY;
        for (;i<8;i++){
            expected[i] = testPersons[i-1]._getFullName();
        }

        assertArrayEquals(expected, repository.getAllNamesWithFirstEmptyString());
    }

    @Test
    public void testGetNamesOfHeadsWithFirstEmptyString(){
        String[] expected = new String[]{EMPTY_ARRAY,
                testPersons[testPersons.length - 2]._getFullName(),
                testPersons[testPersons.length - 1]._getFullName()
        };

        assertArrayEquals(expected, repository.getNamesOfHeadsWithFirstEmptyString());
    }

    @Test
    public void testGetExisted() {
        assertEquals(createPerson(0,false), repository.get(0));
        assertEquals(createPerson(5, true), repository.get(5));
    }

    @Test
    public void testGetNotExisted() {
        assertNull(repository.get(-1));
        assertNull(repository.get(50));
    }

    @Test
    public void testAddNotExisted() {
        Person[] expected = Arrays.copyOf(testPersons, 8);
        expected[expected.length-1] = createPerson(7, false);

        assertTrue(repository.add(createPerson(7, false)));
        assertArrayEquals(expected, repository.getAll().toArray(new Person[0]));
    }

    @Test
    public void testAddExisted() {
        Person person2 = createPerson(2, false);
        person2.setId(7);
        Person[] expected = Arrays.copyOf(testPersons, 8);
        expected[expected.length - 1] = person2;

        assertTrue(repository.add(createPerson(2, false)));
        assertArrayEquals(expected, repository.getAll().toArray(new Person[0]));
    }

    @Test
    public void testSetNew() {
        Person person2 = createPerson(8, false);
        person2.setId(2);
        testPersons[2] = person2;

        assertTrue(repository.set(person2, null));
        assertArrayEquals(testPersons, repository.getAll().toArray(new Person[0]));

        assertTrue(repository.set(person2));
        assertArrayEquals(testPersons, repository.getAll().toArray(new Person[0]));
    }

    @Test
    public void testSetExisted() {
        assertTrue(repository.set(createPerson(2, false), null));
        assertArrayEquals(testPersons, repository.getAll().toArray(new Person[0]));

        assertTrue(repository.set(createPerson(2, false)));
        assertArrayEquals(testPersons, repository.getAll().toArray(new Person[0]));
    }

    @Test
    public void testAllNewCollection(){
        Person[]expected = Arrays.copyOf(testPersons, 9);
        List<Person>toAdd = Arrays.asList(
                createPerson(7,false),
                createPerson(8,false)
        );
        expected[expected.length - 1] = toAdd.get(1);
        expected[expected.length - 2] = toAdd.get(0);

        assertTrue(repository.add(toAdd));
        assertArrayEquals(expected, repository.getAll().toArray(new Person[0]));
    }

    @Test
    public void testAllCollectionWithExists(){
        Person[]expected = Arrays.copyOf(testPersons, 10);
        List<Person>toAdd = Arrays.asList(
                createPerson(7,false),
                createPerson(8,false),
                createPerson(2, false)
        );
        expected[expected.length - 1] = toAdd.get(2);
        expected[expected.length - 2] = toAdd.get(1);
        expected[expected.length - 3] = toAdd.get(0);

        Person testPerson = createPerson(2, false);
        testPerson.setId(9);

        assertTrue(repository.add(toAdd));
        assertEquals(testPerson, repository.get(9));
        expected[expected.length - 1].setId(9);
        assertArrayEquals(expected, repository.getAll().toArray(new Person[0]));
    }

    @Test
    public void testAllNewCollectionWithNull(){
        Person[]expected = Arrays.copyOf(testPersons, 9);
        List<Person>toAdd = Arrays.asList(
                createPerson(7,false),
                createPerson(8,false),
                null
        );
        expected[expected.length - 1] = toAdd.get(1);
        expected[expected.length - 2] = toAdd.get(0);

        assertTrue(repository.add(toAdd));
        assertArrayEquals(expected, repository.getAll().toArray(new Person[0]));
    }

    @Test
    public void testRemoveExisted() {
        Person[] expected = Arrays.copyOf(testPersons, 6);

        assertTrue(repository.remove(createPerson(6, true)));
        assertArrayEquals(expected, repository.getAll().toArray(new Person[0]));
    }

    @Test
    public void testRemoveNotExisted() {
        assertFalse(repository.remove(createPerson(8, true)));
        assertArrayEquals(testPersons, repository.getAll().toArray(new Person[0]));
    }

    @Test
    public void testClear() {
        assertTrue(repository.clear());
        assertArrayEquals(new Person[0], repository.getAll().toArray(new Person[0]));
    }

    @Test
    public void testRewriteNotEmpty() {
        Person[] expected = new Person[]{
                createPerson(7, false),
                createPerson(8,false),
                createPerson(9, false)
        };

        assertTrue(repository.rewrite(Arrays.asList(expected)));
        assertArrayEquals(expected, repository.getAll().toArray(new Person[0]));
    }

    @Test
    public void testRewriteEmpty() {
        assertTrue(repository.rewrite(new ArrayList<Person>()));
        assertArrayEquals(new Person[0], repository.getAll().toArray(new Person[0]));
    }
}