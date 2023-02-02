package service.repository.repos.measurement;

import model.Measurement;
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
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class BufferedMeasurementRepositorySQLiteTest {
    private static final String TEST_REPOSITORY_PROPERTIES_FILE = "properties/test_repository.properties";
    private static final File TEST_DB_FILE = new File("TestData.db");
    private static final String TEST_DB_URL = "jdbc:sqlite:TestData.db";
    private static final String TABLE_NAME = "measurements";

    private List<Measurement> expected;

    private MeasurementRepository repository;

    @BeforeClass
    public static void createDB() throws IOException, SQLException {
        Files.createFile(TEST_DB_FILE.toPath());
        String sql = String.format("CREATE TABLE IF NOT EXISTS %s (" +
                "name text NOT NULL" +
                ", value text NOT NULL UNIQUE" +
                ", PRIMARY KEY(value)" +
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
        expected = new ArrayList<>(Arrays.asList(
                new Measurement(Measurement.TEMPERATURE, Measurement.DEGREE_CELSIUS),
                new Measurement(Measurement.PRESSURE, Measurement.KPA),
                new Measurement(Measurement.PRESSURE, Measurement.PA)
        ));

        String insertSql = String.format("INSERT INTO %s (name, value)"
                + " VALUES ", TABLE_NAME);
        StringBuilder sql = new StringBuilder(insertSql);

        for (Measurement measurement : expected) {
            String values = String.format("('%s', '%s'),",
                    measurement.getName(), measurement.getValue());
            sql.append(values);
        }
        sql.setCharAt(sql.length()-1, ';');

        try (Connection connection = DriverManager.getConnection(TEST_DB_URL);
             Statement statement = connection.createStatement()) {
            statement.execute(sql.toString());
        }

        RepositoryConfigHolder configHolder = new SqliteRepositoryConfigHolder(TEST_REPOSITORY_PROPERTIES_FILE);
        RepositoryDBConnector connector = new SqliteRepositoryDBConnector(configHolder);
        repository = new BufferedMeasurementRepositorySQLite(configHolder, connector);
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
        Collection<Measurement> actual = repository.getAll();
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testGetAllNames() {
        List<String> expected = this.expected.stream()
                .map(Measurement::getName)
                .distinct()
                .collect(Collectors.toList());

        Collection<String> actual = Arrays.asList(repository.getAllNames());
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testGetAllValues() {
        List<String> expected = this.expected.stream()
                .map(Measurement::getValue)
                .collect(Collectors.toList());

        Collection<String> actual = Arrays.asList(repository.getAllValues());
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testGetValues() {
        List<String> expected = this.expected.stream()
                .filter(m -> m.getName().equals(Measurement.PRESSURE))
                .map(Measurement::getValue)
                .collect(Collectors.toList());

        Collection<String> actual = Arrays.asList(repository.getValues(Measurement.PRESSURE));
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testGetByExistedValue() {
        Measurement expected = new Measurement(Measurement.PRESSURE, Measurement.KPA);

        assertEquals(expected, repository.getByValue(Measurement.KPA));
    }

    @Test
    public void testGetByNotExistedValue() {
        assertNull(repository.getByValue(Measurement.MM_ACVA));
    }

    @Test
    public void testAddNew() {
        Measurement m = new Measurement(Measurement.CONSUMPTION, Measurement.M3_HOUR);
        expected.add(m);

        assertTrue(repository.add(m));
        assertEquals(m, repository.getByValue(Measurement.M3_HOUR));

        Collection<Measurement> actual = repository.getAll();
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testAddExisted() {
        Measurement m = new Measurement(Measurement.TEMPERATURE, Measurement.DEGREE_CELSIUS);
        assertFalse(repository.add(m));

        Collection<Measurement> actual = repository.getAll();
        assertEquals(1, actual.stream().filter(me -> me.equals(m)).count());

        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testSetNewInsteadExisted() {
        Measurement measurementTemperature = new Measurement(Measurement.TEMPERATURE, Measurement.DEGREE_CELSIUS);
        Measurement measurementConsumption = new Measurement(Measurement.CONSUMPTION, Measurement.M3_HOUR);
        expected.set(0, measurementConsumption);

        assertTrue(repository.set(measurementTemperature, measurementConsumption));
        assertNull(repository.getByValue(Measurement.DEGREE_CELSIUS));
        assertEquals(measurementConsumption, repository.getByValue(Measurement.M3_HOUR));

        Collection<Measurement> actual = repository.getAll();
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testSetNewInsteadNotExisted() {
        Measurement measurementPressure = new Measurement(Measurement.PRESSURE, Measurement.MM_ACVA);
        Measurement measurementConsumption = new Measurement(Measurement.CONSUMPTION, Measurement.M3_HOUR);

        assertFalse(repository.set(measurementPressure, measurementConsumption));
        assertNull(repository.getByValue(Measurement.MM_ACVA));
        assertNull(repository.getByValue(Measurement.M3_HOUR));

        Collection<Measurement> actual = repository.getAll();
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }


    @Test
    public void testSetSame() {
        Measurement measurementTemperature = new Measurement(Measurement.TEMPERATURE, Measurement.DEGREE_CELSIUS);

        assertTrue(repository.set(measurementTemperature, measurementTemperature));

        Collection<Measurement> actual = repository.getAll();
        assertEquals(1, actual.stream().filter(m -> m.equals(measurementTemperature)).count());

        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testSetExistedInsteadExisted() {
        Measurement measurementTemperature = new Measurement(Measurement.TEMPERATURE, Measurement.DEGREE_CELSIUS);
        Measurement measurementPressure = new Measurement(Measurement.PRESSURE, Measurement.KPA);

        assertFalse(repository.set(measurementPressure, measurementTemperature));
        assertEquals(measurementTemperature, repository.getByValue(Measurement.DEGREE_CELSIUS));
        assertEquals(measurementPressure, repository.getByValue(Measurement.KPA));

        Collection<Measurement> actual = repository.getAll();
        assertEquals(1, actual.stream().filter(m -> m.equals(measurementTemperature)).count());
        assertEquals(1, actual.stream().filter(m -> m.equals(measurementPressure)).count());

        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testSetExistedInsteadNotExisted() {
        Measurement measurementTemperature = new Measurement(Measurement.TEMPERATURE, Measurement.DEGREE_CELSIUS);
        Measurement measurementConsumption = new Measurement(Measurement.CONSUMPTION, Measurement.M3_HOUR);

        assertFalse(repository.set(measurementConsumption, measurementTemperature));
        assertEquals(measurementTemperature, repository.getByValue(Measurement.DEGREE_CELSIUS));
        assertNull(repository.getByValue(Measurement.M3_HOUR));

        Collection<Measurement> actual = repository.getAll();
        assertEquals(1, actual.stream().filter(m -> m.equals(measurementTemperature)).count());

        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testRemoveExisted() {
        Measurement measurementTemperature = new Measurement(Measurement.TEMPERATURE, Measurement.DEGREE_CELSIUS);
        expected.removeIf(m -> m.getValue().equals(Measurement.DEGREE_CELSIUS));

        assertTrue(repository.remove(measurementTemperature));
        assertNull(repository.getByValue(Measurement.DEGREE_CELSIUS));

        Collection<Measurement> actual = repository.getAll();
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testRemoveNotExisted() {
        assertFalse(repository.remove(new Measurement(Measurement.CONSUMPTION, Measurement.M3_HOUR)));
        assertNull(repository.getByValue(Measurement.M3_HOUR));

        Collection<Measurement> actual = repository.getAll();
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testClear() {
        assertTrue(repository.clear());
        assertEquals(0, repository.getAll().size());
    }

    @Test
    public void testGetMeasurementsByExistedName() {
        expected.removeIf(m -> !m.getName().equals(Measurement.PRESSURE));

        Collection<Measurement> actual = repository.getMeasurementsByName(Measurement.PRESSURE);
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testGetMeasurementsByNotExistedName() {
        Collection<Measurement> actual = repository.getMeasurementsByName(Measurement.CONSUMPTION);
        assertNotNull(actual);
        assertEquals(0, actual.size());
    }

    @Test
    public void testRewrite() {
        Collection<Measurement> toRewrite = Arrays.asList(
                new Measurement(Measurement.TEMPERATURE, Measurement.DEGREE_CELSIUS),
                null,
                new Measurement(Measurement.CONSUMPTION, Measurement.M3_HOUR)
        );
        List<Measurement> expected = toRewrite.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        assertTrue(repository.rewrite(toRewrite));

        Collection<Measurement> actual = repository.getAll();
        assertFalse(actual.stream().anyMatch(Objects::isNull));
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testIsLastInMeasurement() {
        assertTrue(repository.isLastInMeasurement(Measurement.DEGREE_CELSIUS));
        assertFalse(repository.isLastInMeasurement(Measurement.KPA));
    }

    @Test
    public void testExists() {
        assertTrue(repository.exists(Measurement.DEGREE_CELSIUS));
        assertFalse(repository.exists(Measurement.M3_HOUR));

        assertFalse(repository.exists(Measurement.DEGREE_CELSIUS, Measurement.DEGREE_CELSIUS));
        assertFalse(repository.exists(Measurement.DEGREE_CELSIUS, Measurement.M3_HOUR));
        assertTrue(repository.exists(Measurement.DEGREE_CELSIUS, Measurement.KPA));
        assertTrue(repository.exists(Measurement.M3_HOUR, Measurement.DEGREE_CELSIUS));
        assertTrue(repository.exists(Measurement.M3_HOUR, Measurement.MM_ACVA));
    }
}