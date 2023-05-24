package repository.repos.sensor_error;

import model.dto.Measurement;
import model.dto.Sensor;
import model.dto.SensorError;
import org.junit.*;
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
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class BufferedSensorErrorRepositorySQLiteTest {
    private static final String TEST_REPOSITORY_PROPERTIES_FILE = "properties/repository_test.properties";
    private static final File TEST_DB_FILE = new File("TestData.db");
    private static final String TEST_DB_URL = "jdbc:sqlite:TestData.db";
    private static final String TABLE_NAME = "sensors_errors";

    private List<SensorError> expected;
    private SensorErrorRepository repository;

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @BeforeClass
    public static void createDB() throws IOException, SQLException {
        TEST_DB_FILE.createNewFile();
        String sql = String.format("CREATE TABLE IF NOT EXISTS %s (" +
                "id text NOT NULL UNIQUE" +
                ", type text NOT NULL" +
                ", measurement_value text NOT NULL" +
                ", error_formula text NOT NULL" +
                ", range_min real NOT NULL" +
                ", range_max real NOT NULL" +
                ", PRIMARY KEY (id)" +
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
        expected = new ArrayList<>(Arrays.asList(
                SensorError.create(Sensor.TCM_50M, 0,100, Measurement.DEGREE_CELSIUS, "0.8"),
                SensorError.create(Sensor.Pt100, -50,180, Measurement.DEGREE_CELSIUS, "0.8*2"),
                SensorError.create(Sensor.YOKOGAWA, 0,50, Measurement.M3_HOUR, "0.8/3")
        ));

        String sql = String.format("INSERT INTO %s (id, type, range_min, range_max, measurement_value, error_formula) "
                + "VALUES ", TABLE_NAME);
        StringBuilder sqlBuilder = new StringBuilder(sql);

        for (SensorError error : expected) {
            String values = String.format("('%s', '%s', %s, %s, '%s', '%s'),",
                    error.getId(),
                    error.getType(),
                    error.getRangeMin(),
                    error.getRangeMax(),
                    error.getMeasurementValue(),
                    error.getErrorFormula());
            sqlBuilder.append(values);
        }
        sqlBuilder.setCharAt(sqlBuilder.length()-1, ';');

        try (Connection connection = DriverManager.getConnection(TEST_DB_URL);
             Statement statement = connection.createStatement()) {
            statement.execute(sqlBuilder.toString());
        }

        RepositoryConfigHolder configHolder = new SqliteRepositoryConfigHolder(TEST_REPOSITORY_PROPERTIES_FILE);
        RepositoryDBConnector connector = new SqliteRepositoryDBConnector(configHolder);
        repository = new BufferedSensorErrorRepositorySQLite(configHolder, connector);
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
        Collection<SensorError> actual = repository.getAll();
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testGetByExistedSensorType() {
        expected = expected.stream().filter(se -> se.getType().equals(Sensor.TCM_50M)).collect(Collectors.toList());

        Collection<SensorError> actual = repository.getBySensorType(Sensor.TCM_50M);
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testGetByNotExistedSensorType() {
        Collection<SensorError> actual = repository.getBySensorType("Not existed");
        assertNotNull(actual);
        assertTrue(actual.isEmpty());
    }

    @Test
    public void testAddNew() {
        SensorError newError = SensorError.create(
                Sensor.ROSEMOUNT, 0,100, Measurement.CM_S, "0.1 * convR");
        expected.add(newError);

        assertTrue(repository.add(newError));
        assertEquals(newError, repository.getById(newError.getId()));

        Collection<SensorError> actual = repository.getAll();
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testAddExisted() {
        SensorError existedError = expected.get(0);

        assertFalse(repository.add(existedError));
        assertEquals(existedError, repository.getById(existedError.getId()));

        Collection<SensorError> actual = repository.getAll();
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testGetByExistedId() {
        SensorError expected = SensorError.getCopyOf(this.expected.get(0));

        assertEquals(expected, repository.getById(expected.getId()));

        Collection<SensorError> actual = repository.getAll();
        assertEquals(this.expected.size(), actual.size());
        assertTrue(this.expected.containsAll(actual));
    }

    @Test
    public void testGetByNotExistedId() {
        assertNull(repository.getById("Not Existed"));

        Collection<SensorError> actual = repository.getAll();
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testSetNewInsteadExisted() {
        SensorError newError = SensorError.create(Sensor.ROSEMOUNT, 0,100, Measurement.CM_S, "");
        SensorError oldError = SensorError.getCopyOf(expected.get(0));
        expected.set(0, newError);

        assertTrue(repository.set(oldError.getId(), newError));
        assertNull(repository.getById(oldError.getId()));
        assertEquals(newError, repository.getById(newError.getId()));

        Collection<SensorError> actual = repository.getAll();
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testSetNewInsteadNotExisted() {
        SensorError newError = SensorError.create(Sensor.ROSEMOUNT, 0,100, Measurement.CM_S, "");

        assertFalse(repository.set("Not existed", newError));
        assertNull(repository.getById(newError.getId()));

        Collection<SensorError> actual = repository.getAll();
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testSetExistedInsteadExisted() {
        SensorError oldError = SensorError.getCopyOf(expected.get(0));

        assertTrue(repository.set(oldError.getId(), oldError));
        assertEquals(oldError, repository.getById(oldError.getId()));

        Collection<SensorError> actual = repository.getAll();
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testSetExistedInsteadNotExisted() {
        SensorError oldError = SensorError.getCopyOf(expected.get(0));

        assertFalse(repository.set("Not existed", oldError));
        assertEquals(oldError, repository.getById(oldError.getId()));

        Collection<SensorError> actual = repository.getAll();
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testSetChangedInsteadExisted() {
        SensorError changedError = expected.get(0);
        changedError.setErrorFormula("0.1");

        assertTrue(repository.set(changedError.getId(), changedError));

        SensorError changedErrorFromDB = repository.getById(changedError.getId());
        assertEquals(changedError, changedErrorFromDB);
        assertEquals(changedError.getErrorFormula(), changedErrorFromDB.getErrorFormula());

        Collection<SensorError> actual = repository.getAll();
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testRemoveByExistedId() {
        String id = SensorError.getCopyOf(expected.get(0)).getId();
        expected.remove(0);

        assertTrue(repository.removeById(id));
        assertNull(repository.getById(id));

        Collection<SensorError> actual = repository.getAll();
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testRemoveByNotExistedId() {
        String id = "Not existed";

        assertFalse(repository.removeById(id));
        assertNull(repository.getById(id));

        Collection<SensorError> actual = repository.getAll();
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testRemoveByExistedSensorType() {
        expected.removeIf(se -> se.getType().equals(Sensor.TCM_50M));

        repository.removeBySensorType(Sensor.TCM_50M);

        Collection<SensorError> actual = repository.getAll();
        assertFalse(actual.stream().anyMatch(se -> se.getType().equals(Sensor.TCM_50M)));
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testRemoveByNotExistedSensorType() {
        repository.removeBySensorType("Not existed");

        Collection<SensorError> actual = repository.getAll();
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testRemoveByExistedMeasurementValue() {
        expected.removeIf(se -> se.getMeasurementValue().equals(Measurement.DEGREE_CELSIUS));

        repository.removeByMeasurementValue(Measurement.DEGREE_CELSIUS);

        Collection<SensorError> actual = repository.getAll();
        assertFalse(actual.stream().anyMatch(se -> se.getMeasurementValue().equals(Measurement.DEGREE_CELSIUS)));
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testRemoveByNotExistedMeasurementValue() {
        repository.removeByMeasurementValue("Not existed");

        Collection<SensorError> actual = repository.getAll();
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testIsExists() {
        String existedId = SensorError.getCopyOf(expected.get(0)).getId();
        String notExistedId = "Not existed";

        assertTrue(repository.isExists(existedId));
        assertFalse(repository.isExists(notExistedId));

        assertTrue(repository.isExists(existedId, SensorError.getCopyOf(expected.get(1)).getId()));
        assertTrue(repository.isExists(notExistedId, existedId));
        assertTrue(repository.isExists(null, existedId));
        assertTrue(repository.isExists(null, notExistedId));
        assertTrue(repository.isExists(existedId, null));
        assertTrue(repository.isExists(notExistedId, null));
        assertTrue(repository.isExists(null, null));

        assertFalse(repository.isExists(existedId, existedId));
        assertFalse(repository.isExists(existedId, notExistedId));
    }

    @Test
    public void testRewrite() {
        SensorError error1 = SensorError.create("type1", 0.00, 100.00, "value1", "1");
        SensorError error2 = SensorError.create("type2", 1.00, 101.00, "value2", "2");
        SensorError error3 = SensorError.create("type3", 2.00, 102.00, "value3", "3");
        List<SensorError> toRewrite = Arrays.asList(error1, error2, null, error3);
        List<SensorError> expected = Arrays.asList(error1, error2, error3);

        assertTrue(repository.rewrite(toRewrite));

        Collection<SensorError> actual = repository.getAll();
        assertFalse(actual.stream().anyMatch(Objects::isNull));

        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testChangeTypeToNew() {
        expected.forEach(s -> {
            if (s.getType().equals(Sensor.TCM_50M)) s.setType("new type");
        });

        assertTrue(repository.changeSensorType(Sensor.TCM_50M, "new type"));

        Collection<SensorError> actual = repository.getAll();
        assertEquals(0, actual.stream().filter(s -> s.getType().equals(Sensor.TCM_50M)).count());
        assertEquals(0, actual.stream().filter(s -> s.getId().contains(Sensor.TCM_50M)).count());
        assertEquals(1, actual.stream().filter(s -> s.getType().equals("new type")).count());
        assertEquals(1, actual.stream().filter(s -> s.getId().contains("new type")).count());

        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testChangeTypeToExisted() {
        expected.forEach(s -> {
            if (s.getType().equals(Sensor.TCM_50M)) s.setType(Sensor.Pt100);
        });

        assertTrue(repository.changeSensorType(Sensor.TCM_50M, Sensor.Pt100));

        Collection<SensorError> actual = repository.getAll();
        assertEquals(0, actual.stream().filter(s -> s.getType().equals(Sensor.TCM_50M)).count());
        assertEquals(0, actual.stream().filter(s -> s.getId().contains(Sensor.TCM_50M)).count());
        assertEquals(2, actual.stream().filter(s -> s.getType().equals(Sensor.Pt100)).count());
        assertEquals(2, actual.stream().filter(s -> s.getId().contains(Sensor.Pt100)).count());

        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testChangeNotExistedTypeToNew() {
        assertTrue(repository.changeSensorType("not existed", "new"));

        Collection<SensorError> actual = repository.getAll();
        assertEquals(0, actual.stream().filter(s -> s.getType().equals("not existed")).count());
        assertEquals(0, actual.stream().filter(s -> s.getId().contains("not existed")).count());
        assertEquals(0, actual.stream().filter(s -> s.getType().equals("new")).count());
        assertEquals(0, actual.stream().filter(s -> s.getId().contains("new")).count());

        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testChangeNotExistedTypeToExisted() {
        assertTrue(repository.changeSensorType("not existed", Sensor.TCM_50M));

        Collection<SensorError> actual = repository.getAll();
        assertEquals(0, actual.stream().filter(s -> s.getType().equals("not existed")).count());
        assertEquals(0, actual.stream().filter(s -> s.getId().contains("not existed")).count());
        assertEquals(1, actual.stream().filter(s -> s.getType().equals(Sensor.TCM_50M)).count());
        assertEquals(1, actual.stream().filter(s -> s.getId().contains(Sensor.TCM_50M)).count());

        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testChangeMeasurementValueToNew() {
        final String newValue = "new value";
        expected.forEach(s -> {
            if (s.getMeasurementValue().equals(Measurement.DEGREE_CELSIUS)) s.setMeasurementValue(newValue);
        });

        assertTrue(repository.changeMeasurementValue(Measurement.DEGREE_CELSIUS, newValue));

        Collection<SensorError> actual = repository.getAll();
        assertEquals(0, actual.stream().filter(s -> s.getMeasurementValue().equals(Measurement.DEGREE_CELSIUS)).count());
        assertEquals(0, actual.stream().filter(s -> s.getId().contains(Measurement.DEGREE_CELSIUS)).count());
        assertEquals(2, actual.stream().filter(s -> s.getMeasurementValue().equals(newValue)).count());
        assertEquals(2, actual.stream().filter(s -> s.getId().contains(newValue)).count());

        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testChangeMeasurementValueToExisted() {
        expected.forEach(s -> {
            if (s.getMeasurementValue().equals(Measurement.DEGREE_CELSIUS)) s.setMeasurementValue(Measurement.M3_HOUR);
        });

        assertTrue(repository.changeMeasurementValue(Measurement.DEGREE_CELSIUS, Measurement.M3_HOUR));

        Collection<SensorError> actual = repository.getAll();
        assertEquals(0, actual.stream().filter(s -> s.getMeasurementValue().equals(Measurement.DEGREE_CELSIUS)).count());
        assertEquals(0, actual.stream().filter(s -> s.getId().contains(Measurement.DEGREE_CELSIUS)).count());
        assertEquals(3, actual.stream().filter(s -> s.getMeasurementValue().equals(Measurement.M3_HOUR)).count());
        assertEquals(3, actual.stream().filter(s -> s.getId().contains(Measurement.M3_HOUR)).count());

        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testChangeNotExistedMeasurementValueToNew() {
        final String notExistedValue = "not existed value";
        final String newValue = "new value";
        assertTrue(repository.changeMeasurementValue(notExistedValue, newValue));

        Collection<SensorError> actual = repository.getAll();
        assertEquals(0, actual.stream().filter(s -> s.getMeasurementValue().equals(notExistedValue)).count());
        assertEquals(0, actual.stream().filter(s -> s.getId().contains(notExistedValue)).count());
        assertEquals(0, actual.stream().filter(s -> s.getMeasurementValue().equals(newValue)).count());
        assertEquals(0, actual.stream().filter(s -> s.getId().contains(newValue)).count());

        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testChangeNotExistedMeasurementValueToExisted() {
        final String notExistedValue = "not existed value";
        assertTrue(repository.changeSensorType(notExistedValue, Measurement.DEGREE_CELSIUS));

        Collection<SensorError> actual = repository.getAll();
        assertEquals(0, actual.stream().filter(s -> s.getMeasurementValue().equals(notExistedValue)).count());
        assertEquals(0, actual.stream().filter(s -> s.getId().contains(notExistedValue)).count());
        assertEquals(2, actual.stream().filter(s -> s.getMeasurementValue().equals(Measurement.DEGREE_CELSIUS)).count());
        assertEquals(2, actual.stream().filter(s -> s.getId().contains(Measurement.DEGREE_CELSIUS)).count());

        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }
}