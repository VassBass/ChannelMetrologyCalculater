package service;

import model.Person;
import org.junit.*;
import org.sqlite.JDBC;
import repository.PersonRepository;
import repository.impl.PersonRepositorySQLite;
import service.impl.PersonServiceImpl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.Assert.*;

public class PersonServiceTest {

    private static final String DB_URL = "jdbc:sqlite:TestData.db";
    private static final PersonRepository repository = new PersonRepositorySQLite(DB_URL, null, null);
    private static final PersonService service = new PersonServiceImpl(repository);

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
    public void fuelingTestPersonsList() {
        testPersons = new Person[7];
        int n = 0;
        for (;n < 5;n++){
            repository.add(createPerson(n, false));
            testPersons[n] = createPerson(n, false);
        }
        repository.add(createPerson(n, true));
        testPersons[n] = createPerson(n++, true);
        repository.add(createPerson(n, true));
        testPersons[n] = createPerson(n, true);
    }

    @After
    public void clearTestPersonsList() {
        testPersons = null;
        repository.clear();
    }

    @Test
    public void getAllNamesWithFirstEmptyString() {
        String[]expected = new String[8];
        int i = 0;
        expected[i++] = EMPTY_ARRAY;
        for (;i<8;i++){
            expected[i] = testPersons[i-1]._getFullName();
        }

        assertArrayEquals(expected, service.getAllNamesWithFirstEmptyString());
    }

    @Test
    public void getNamesOfHeadsWithFirstEmptyString() {
        String[] expected = new String[]{EMPTY_ARRAY,
                testPersons[testPersons.length - 2]._getFullName(),
                testPersons[testPersons.length - 1]._getFullName()
        };

        assertArrayEquals(expected, service.getNamesOfHeadsWithFirstEmptyString());
    }
}