package repository.repos.calculation_method;

import model.dto.Measurement;
import org.junit.*;
import org.sqlite.JDBC;
import repository.config.RepositoryConfigHolder;
import repository.config.SqliteRepositoryConfigHolder;
import repository.connection.RepositoryDBConnector;
import repository.connection.SqliteRepositoryDBConnector;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class BufferedCalculationMethodRepositorySQLiteTest {
    private static final String TEST_REPOSITORY_PROPERTIES_FILE = "properties/repository_test.properties";
    private static final File TEST_DB_FILE = new File("TestData.db");
    private static final String TEST_DB_URL = "jdbc:sqlite:TestData.db";
    private static final String TABLE_NAME = "calculation_methods";

    private Map<String, String> expected;

    private CalculationMethodRepository repository;

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @BeforeClass
    public static void createDB() throws IOException, SQLException {
        TEST_DB_FILE.createNewFile();
        String sql = String.format("CREATE TABLE IF NOT EXISTS %s (" +
                "measurement_name text NOT NULL UNIQUE" +
                ", method_name text NOT NULL" +
                ", PRIMARY KEY (measurement_name)" +
                ");", TABLE_NAME);
        try (Connection connection = DriverManager.getConnection(TEST_DB_URL);
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
        }
    }

    @AfterClass
    public static void removeDB() throws SQLException {
        String sql = String.format("DROP TABLE IF EXISTS %s", TABLE_NAME);
        try (Connection connection = DriverManager.getConnection(TEST_DB_URL);
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
        }
    }

    @Before
    public void init() throws SQLException {
        expected = new HashMap<>();
        expected.put(Measurement.TEMPERATURE, "temperature");
        expected.put(Measurement.PRESSURE, "pressure");

        String sql = String.format("INSERT INTO %s (measurement_name, method_name) VALUES ('%s', '%s'), ('%s', '%s');",
                TABLE_NAME,
                Measurement.TEMPERATURE, "temperature", Measurement.PRESSURE, "pressure");

        DriverManager.registerDriver(new JDBC());
        try (Connection connection = DriverManager.getConnection(TEST_DB_URL);
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
        }

        RepositoryConfigHolder configHolder = new SqliteRepositoryConfigHolder(TEST_REPOSITORY_PROPERTIES_FILE);
        RepositoryDBConnector connector = new SqliteRepositoryDBConnector(configHolder);
        repository = new BufferedCalculationMethodRepositorySQLite(configHolder, connector);
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
        Map<String, String> actual = repository.getAll();
        assertEquals(expected.size(), actual.size());
        for (Map.Entry<String, String> entry : expected.entrySet()) {
            assertEquals(entry.getValue(), actual.get(entry.getKey()));
        }
    }

    @Test
    public void testGetMethodNameByExistedMeasurementName() {
        String expected = this.expected.get(Measurement.TEMPERATURE);
        assertEquals(expected, repository.getMethodNameByMeasurementName(Measurement.TEMPERATURE));
    }

    @Test
    public void testGetMethodNameByNotExistedMeasurementName() {
        assertNull(repository.getMethodNameByMeasurementName(Measurement.CONSUMPTION));
    }

    @Test
    public void testAddNewMethod() {
        expected.put(Measurement.CONSUMPTION, "consumption");

        assertTrue(repository.add(Measurement.CONSUMPTION, "consumption"));
        assertEquals("consumption", repository.getMethodNameByMeasurementName(Measurement.CONSUMPTION));

        Map<String, String> actual = repository.getAll();
        assertEquals(expected.size(), actual.size());
        for (Map.Entry<String, String> entry : expected.entrySet()) {
            assertEquals(entry.getValue(), actual.get(entry.getKey()));
        }
    }

    @Test
    public void testAddExistingMethod(){
        assertFalse(repository.add(Measurement.TEMPERATURE, "consumption"));
        assertNotEquals("consumption", repository.getMethodNameByMeasurementName(Measurement.TEMPERATURE));

        Map<String, String> actual = repository.getAll();
        assertEquals(expected.size(), actual.size());
        for (Map.Entry<String, String> entry : expected.entrySet()) {
            assertEquals(entry.getValue(), actual.get(entry.getKey()));
        }
    }

    @Test
    public void testSet_replaceExistingMethodWithExistingMethod() {
        expected.put(Measurement.TEMPERATURE, "pressure");

        assertTrue(repository.set(Measurement.TEMPERATURE, "pressure"));
        assertEquals("pressure", repository.getMethodNameByMeasurementName(Measurement.TEMPERATURE));

        Map<String, String> actual = repository.getAll();
        assertEquals(expected.size(), actual.size());
        for (Map.Entry<String, String> entry : expected.entrySet()) {
            assertEquals(entry.getValue(), actual.get(entry.getKey()));
        }
    }

    @Test
    public void testSet_replaceExistingMethodWithTheSameMethod() {
        assertTrue(repository.set(Measurement.TEMPERATURE, "temperature"));
        assertEquals("temperature", repository.getMethodNameByMeasurementName(Measurement.TEMPERATURE));

        Map<String, String> actual = repository.getAll();
        assertEquals(expected.size(), actual.size());
        for (Map.Entry<String, String> entry : expected.entrySet()) {
            assertEquals(entry.getValue(), actual.get(entry.getKey()));
        }
    }

    @Test
    public void testSet_replaceExistingMethodWithNewMethod() {
        expected.put(Measurement.TEMPERATURE, "consumption");

        assertTrue(repository.set(Measurement.TEMPERATURE, "consumption"));
        assertEquals("consumption", repository.getMethodNameByMeasurementName(Measurement.TEMPERATURE));

        Map<String, String> actual = repository.getAll();
        assertEquals(expected.size(), actual.size());
        for (Map.Entry<String, String> entry : expected.entrySet()) {
            assertEquals(entry.getValue(), actual.get(entry.getKey()));
        }
    }

    @Test
    public void testSet_replaceNotExistingMethodWithExistingMethod() {
        assertFalse(repository.set(Measurement.CONSUMPTION, "pressure"));
        assertNull(repository.getMethodNameByMeasurementName(Measurement.CONSUMPTION));

        Map<String, String> actual = repository.getAll();
        assertEquals(expected.size(), actual.size());
        for (Map.Entry<String, String> entry : expected.entrySet()) {
            assertEquals(entry.getValue(), actual.get(entry.getKey()));
        }
    }

    @Test
    public void testSet_replaceNotExistingMethodWithNewMethod() {
        assertFalse(repository.set(Measurement.CONSUMPTION, "consumption"));
        assertNull(repository.getMethodNameByMeasurementName(Measurement.CONSUMPTION));

        Map<String, String> actual = repository.getAll();
        assertEquals(expected.size(), actual.size());
        for (Map.Entry<String, String> entry : expected.entrySet()) {
            assertEquals(entry.getValue(), actual.get(entry.getKey()));
        }
    }

    @Test
    public void testRemoveExistingMethod() {
        expected.remove(Measurement.TEMPERATURE);

        assertTrue(repository.removeByMeasurementName(Measurement.TEMPERATURE));
        assertNull(repository.getMethodNameByMeasurementName(Measurement.TEMPERATURE));

        Map<String, String> actual = repository.getAll();
        assertEquals(expected.size(), actual.size());
        for (Map.Entry<String, String> entry : expected.entrySet()) {
            assertEquals(entry.getValue(), actual.get(entry.getKey()));
        }
    }

    @Test
    public void testRemoveNotExistingMethod() {
        assertFalse(repository.removeByMeasurementName(Measurement.CONSUMPTION));
        assertNull(repository.getMethodNameByMeasurementName(Measurement.CONSUMPTION));

        Map<String, String> actual = repository.getAll();
        assertEquals(expected.size(), actual.size());
        for (Map.Entry<String, String> entry : expected.entrySet()) {
            assertEquals(entry.getValue(), actual.get(entry.getKey()));
        }
    }
}