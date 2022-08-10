package service.impl;

import def.DefaultPersons;
import model.Person;
import org.junit.*;
import org.sqlite.JDBC;
import repository.impl.PersonRepositorySQLite;
import service.PersonService;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class PersonServiceImplTest {

    private static final String DB_URL = "jdbc:sqlite:TestData.db";
    private static final PersonService service = new PersonServiceImpl(new PersonRepositorySQLite(DB_URL, null, null));

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
    public void setUp() {
        testPersons = new Person[7];
        int n = 1;
        for (;n < 6;n++){
            testPersons[n-1] = createPerson(n, false);
        }
        testPersons[n-1] = createPerson(n++, true);
        testPersons[n-1] = createPerson(n, true);

        service.add(Arrays.asList(testPersons));
    }

    @After
    public void tearDown() throws SQLException {
        testPersons = null;
        service.clear();
        String sql = "DELETE FROM sqlite_sequence;";
        try (Connection connection = DriverManager.getConnection(DB_URL);
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
        }
    }

    @Test
    public void testGetAll(){
        assertArrayEquals(testPersons, service.getAll().toArray(new Person[0]));
    }

    @Test
    public void testGetAllNamesWithFirstEmptyString(){
        String[]expected = new String[8];
        int i = 1;
        expected[i-1] = EMPTY_ARRAY;
        for (;i<8;i++){
            expected[i] = testPersons[i-1]._getFullName();
        }

        assertArrayEquals(expected, service.getAllNamesWithFirstEmptyString());
    }

    @Test
    public void testGetNamesOfHeadsWithFirstEmptyString(){
        String[] expected = new String[]{EMPTY_ARRAY,
                testPersons[testPersons.length - 2]._getFullName(),
                testPersons[testPersons.length - 1]._getFullName()
        };

        assertArrayEquals(expected, service.getNamesOfHeadsWithFirstEmptyString());
    }

    @Test
    public void testGetExisted() {
        assertEquals(createPerson(1,false), service.get(1));
        assertEquals(createPerson(6, true), service.get(6));
    }

    @Test
    public void testGetNotExisted() {
        assertNull(service.get(50));
    }

    @Test
    public void testAddNotExisted() {
        Person[] expected = Arrays.copyOf(testPersons, 8);
        expected[expected.length-1] = createPerson(8, false);

        assertTrue(service.add(createPerson(8, false)));
        assertArrayEquals(expected, service.getAll().toArray(new Person[0]));
    }

    @Test
    public void testAddExisted() {
        Person person2 = createPerson(3, false);
        person2.setId(8);
        Person[] expected = Arrays.copyOf(testPersons, 8);
        expected[expected.length - 1] = person2;

        assertTrue(service.add(createPerson(3, false)));
        assertArrayEquals(expected, service.getAll().toArray(new Person[0]));
    }

    @Test
    public void testSetNew() {
        Person person2 = createPerson(9, false);
        person2.setId(3);
        testPersons[2] = person2;

        assertTrue(service.set(person2, null));
        assertArrayEquals(testPersons, service.getAll().toArray(new Person[0]));

        assertTrue(service.set(person2));
        assertArrayEquals(testPersons, service.getAll().toArray(new Person[0]));
    }

    @Test
    public void testSetExisted() {
        assertTrue(service.set(createPerson(3, false), null));
        assertArrayEquals(testPersons, service.getAll().toArray(new Person[0]));

        assertTrue(service.set(createPerson(3, false)));
        assertArrayEquals(testPersons, service.getAll().toArray(new Person[0]));
    }

    @Test
    public void testAllNewCollection(){
        Person[]expected = Arrays.copyOf(testPersons, 9);
        List<Person> toAdd = Arrays.asList(
                createPerson(8,false),
                createPerson(9,false)
        );
        expected[expected.length - 1] = toAdd.get(1);
        expected[expected.length - 2] = toAdd.get(0);

        assertTrue(service.add(toAdd));
        assertArrayEquals(expected, service.getAll().toArray(new Person[0]));
    }

    @Test
    public void testAllCollectionWithExists(){
        Person[]expected = Arrays.copyOf(testPersons, 10);
        List<Person>toAdd = Arrays.asList(
                createPerson(8,false),
                createPerson(9,false),
                createPerson(3, false)
        );
        expected[expected.length - 1] = toAdd.get(2);
        expected[expected.length - 2] = toAdd.get(1);
        expected[expected.length - 3] = toAdd.get(0);

        Person testPerson = createPerson(3, false);
        testPerson.setId(10);

        assertTrue(service.add(toAdd));
        assertEquals(testPerson, service.get(10));
        expected[expected.length - 1].setId(10);
        assertArrayEquals(expected, service.getAll().toArray(new Person[0]));
    }

    @Test
    public void testAllNewCollectionWithNull(){
        Person[]expected = Arrays.copyOf(testPersons, 9);
        List<Person>toAdd = Arrays.asList(
                createPerson(8,false),
                createPerson(9,false),
                null
        );
        expected[expected.length - 1] = toAdd.get(1);
        expected[expected.length - 2] = toAdd.get(0);

        assertTrue(service.add(toAdd));
        assertArrayEquals(expected, service.getAll().toArray(new Person[0]));
    }

    @Test
    public void testRemoveExisted() {
        Person[] expected = Arrays.copyOf(testPersons, 6);

        assertTrue(service.remove(createPerson(7, true)));
        assertArrayEquals(expected, service.getAll().toArray(new Person[0]));
    }

    @Test
    public void testRemoveNotExisted() {
        assertFalse(service.remove(createPerson(9, true)));
        assertArrayEquals(testPersons, service.getAll().toArray(new Person[0]));
    }

    @Test
    public void testClear() {
        assertTrue(service.clear());
        assertArrayEquals(new Person[0], service.getAll().toArray(new Person[0]));
    }

    @Test
    public void testRewriteNotEmpty() {
        Person[] expected = new Person[]{
                createPerson(8, false),
                createPerson(9,false),
                createPerson(10, false)
        };

        assertTrue(service.rewrite(Arrays.asList(expected)));
        assertArrayEquals(expected, service.getAll().toArray(new Person[0]));
    }

    @Test
    public void testRewriteEmpty() {
        assertTrue(service.rewrite(new ArrayList<Person>()));
        assertArrayEquals(new Person[0], service.getAll().toArray(new Person[0]));
    }

    @Test
    public void testResetToDefault(){
        assertTrue(service.resetToDefault());
        List<Person>def = DefaultPersons.get();
        int id = 8;
        for (Person p : def){
            p.setId(id++);
        }

        assertArrayEquals(def.toArray(new Person[0]), service.getAll().toArray(new Person[0]));
    }
}