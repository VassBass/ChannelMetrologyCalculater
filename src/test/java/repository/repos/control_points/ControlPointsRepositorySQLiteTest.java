package repository.repos.control_points;

import model.dto.ControlPoints;
import model.dto.Sensor;
import model.dto.builder.ControlPointsBuilder;
import org.junit.*;
import org.sqlite.JDBC;
import service.json.JacksonJsonObjectMapper;
import service.json.JsonObjectMapper;
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

import static org.junit.Assert.*;

public class ControlPointsRepositorySQLiteTest {
    private static final String TEST_REPOSITORY_PROPERTIES_FILE = "properties/test_repository.properties";
    private static final File TEST_DB_FILE = new File("TestData.db");
    private static final String TEST_DB_URL = "jdbc:sqlite:TestData.db";
    private static final String TABLE_NAME = "controlPoints";

    private List<ControlPoints> expected;
    private ControlPoints createControlPoints(int number, String sensorType){
        Map<Double, Double> points = new HashMap<>();
        points.put(0D, (double) number);
        points.put(5D, number * 5D);
        points.put(50D, number * 50D);
        points.put(95D, number * 95D);
        points.put(100D, number * 100D);
        return new ControlPointsBuilder(String.valueOf(number))
                .setSensorType(sensorType)
                .setPoints(points)
                .build();
    }

    private final JsonObjectMapper jsonObjectMapper = JacksonJsonObjectMapper.getInstance();
    private ControlPointsRepository repository;

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @BeforeClass
    public static void createDB() throws IOException, SQLException {
        TEST_DB_FILE.createNewFile();
        String sql = String.format("CREATE TABLE IF NOT EXISTS %s ("
                + "name text NOT NULL UNIQUE"
                + ", sensor_type text NOT NULL"
                + ", points text NOT NULL"
                + ", PRIMARY KEY (\"name\")"
                + ");", TABLE_NAME);
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
        expected.add(createControlPoints(0, Sensor.TCM_50M));
        expected.add(createControlPoints(1, Sensor.TCM_50M));
        expected.add(createControlPoints(2, Sensor.TCM_50M));
        expected.add(createControlPoints(3, Sensor.ROSEMOUNT));
        expected.add(createControlPoints(4, Sensor.ROSEMOUNT));
        expected.add(createControlPoints(5, Sensor.YOKOGAWA));
        expected.add(createControlPoints(6, Sensor.YOKOGAWA));

        String insertSql = String.format("INSERT INTO %s (name, sensor_type, points)"
                + " VALUES ", TABLE_NAME);
        StringBuilder sql = new StringBuilder(insertSql);

        for (ControlPoints cp : expected) {
            String values = String.format("('%s', '%s', '%s'),",
                    cp.getName(),
                    cp.getSensorType(),
                    jsonObjectMapper.objectToJson(cp.getValues()));
            sql.append(values);
        }
        sql.setCharAt(sql.length()-1, ';');

        try (Connection connection = DriverManager.getConnection(TEST_DB_URL);
             Statement statement = connection.createStatement()) {
            statement.execute(sql.toString());
        }

        RepositoryConfigHolder configHolder = new SqliteRepositoryConfigHolder(TEST_REPOSITORY_PROPERTIES_FILE);
        RepositoryDBConnector connector = new SqliteRepositoryDBConnector(configHolder);
        repository = new ControlPointsRepositorySQLite(configHolder, connector);
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
    public void testGetAll(){
        Collection<ControlPoints> actual = repository.getAll();
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testGetAllByExistedSensorType() {
        expected.removeIf(cp -> !cp.getSensorType().equals(Sensor.TCM_50M));

        Collection<ControlPoints> actual = repository.getAllBySensorType(Sensor.TCM_50M);
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testGetAllByNotExistedSensorType() {
        assertTrue(repository.getAllBySensorType(Sensor.Pt100).isEmpty());

        Collection<ControlPoints> actual = repository.getAll();
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testGetExisted() {
        ControlPoints controlPoints0 = createControlPoints(0, Sensor.TCM_50M);
        assertEquals(controlPoints0, repository.get(controlPoints0.getName()));

        Collection<ControlPoints> actual = repository.getAll();
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testGetNotExisted() {
        assertNull(repository.get("Not Existed"));

        Collection<ControlPoints> actual = repository.getAll();
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testAddNew() {
        ControlPoints controlPoints8 = createControlPoints(8, Sensor.Pt100);
        expected.add(controlPoints8);

        assertTrue(repository.add(controlPoints8));
        assertEquals(controlPoints8, repository.get(controlPoints8.getName()));

        Collection<ControlPoints> actual = repository.getAll();
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testAddExisted() {
        ControlPoints controlPoints0 = createControlPoints(0, Sensor.TCM_50M);

        assertFalse(repository.add(controlPoints0));
        Collection<ControlPoints> actual = repository.getAll();

        assertEquals(1, actual.stream().filter(cp -> cp.equals(controlPoints0)).count());

        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testSetNewInsteadExisted() {
        ControlPoints controlPoints8 = createControlPoints(8, Sensor.Pt100);
        ControlPoints controlPoints0 = createControlPoints(0, Sensor.TCM_50M);
        expected.set(0, controlPoints8);

        assertTrue(repository.set(controlPoints0, controlPoints8));
        assertNull(repository.get(controlPoints0.getName()));
        assertEquals(controlPoints8, repository.get(controlPoints8.getName()));

        Collection<ControlPoints> actual = repository.getAll();
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testSetExistedInsteadExisted() {
        ControlPoints controlPoints2 = createControlPoints(2, Sensor.TCM_50M);
        ControlPoints controlPoints0 = createControlPoints(0, Sensor.TCM_50M);

        assertFalse(repository.set(controlPoints0, controlPoints2));
        assertEquals(controlPoints0, repository.get(controlPoints0.getName()));
        assertEquals(controlPoints2, repository.get(controlPoints2.getName()));

        Collection<ControlPoints> actual = repository.getAll();
        assertEquals(1, actual.stream().filter(cp -> cp.equals(controlPoints0)).count());
        assertEquals(1, actual.stream().filter(cp -> cp.equals(controlPoints2)).count());

        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testSetNewInsteadNew() {
        ControlPoints controlPoints8 = createControlPoints(8, Sensor.Pt100);
        ControlPoints controlPoints9 = createControlPoints(9, Sensor.Pt100);

        assertFalse(repository.set(controlPoints8, controlPoints9));
        assertNull(repository.get(controlPoints8.getName()));
        assertNull(repository.get(controlPoints9.getName()));

        Collection<ControlPoints> actual = repository.getAll();
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testSetExistedInsteadNew() {
        ControlPoints controlPoints8 = createControlPoints(8, Sensor.Pt100);
        ControlPoints controlPoints0 = createControlPoints(0, Sensor.TCM_50M);

        assertFalse(repository.set(controlPoints8, controlPoints0));
        assertNull(repository.get(controlPoints8.getName()));
        assertEquals(controlPoints0, repository.get(controlPoints0.getName()));

        Collection<ControlPoints> actual = repository.getAll();
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testChangeExistedSensorTypeToNew() {
        expected.forEach(cp -> {
            if (cp.getSensorType().equals(Sensor.TCM_50M)) cp.setSensorType(Sensor.Pt100);
        });

        assertTrue(repository.changeSensorType(Sensor.TCM_50M, Sensor.Pt100));
        assertEquals(Sensor.Pt100, repository.get("0").getSensorType());
        assertEquals(Sensor.Pt100, repository.get("1").getSensorType());
        assertEquals(Sensor.Pt100, repository.get("2").getSensorType());

        Collection<ControlPoints> actual = repository.getAll();
        assertEquals(3, actual.stream().filter(cp -> cp.getSensorType().equals(Sensor.Pt100)).count());

        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testChangeExistedSensorTypeToExisted() {
        expected.forEach(cp -> {
            if (cp.getSensorType().equals(Sensor.TCM_50M)) cp.setSensorType(Sensor.YOKOGAWA);
        });

        assertTrue(repository.changeSensorType(Sensor.TCM_50M, Sensor.YOKOGAWA));
        assertEquals(Sensor.YOKOGAWA, repository.get("0").getSensorType());
        assertEquals(Sensor.YOKOGAWA, repository.get("1").getSensorType());
        assertEquals(Sensor.YOKOGAWA, repository.get("2").getSensorType());

        Collection<ControlPoints> actual = repository.getAll();
        assertEquals(5, actual.stream().filter(cp -> cp.getSensorType().equals(Sensor.YOKOGAWA)).count());

        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testChangeNotExistedSensorTypeToNew() {
        String notExistedSensorType = "NotExisted";
        assertTrue(repository.changeSensorType(notExistedSensorType, Sensor.Pt100));

        Collection<ControlPoints> actual = repository.getAll();
        assertEquals(0, actual.stream().filter(cp -> cp.getSensorType().equals(Sensor.Pt100)).count());
        assertEquals(0, actual.stream().filter(cp -> cp.getSensorType().equals(notExistedSensorType)).count());

        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testChangeNotExistedSensorTypeToExisted() {
        assertTrue(repository.changeSensorType(Sensor.Pt100, Sensor.TCM_50M));

        Collection<ControlPoints> actual = repository.getAll();
        assertEquals(3, actual.stream().filter(cp -> cp.getSensorType().equals(Sensor.TCM_50M)).count());
        assertEquals(0, actual.stream().filter(cp -> cp.getSensorType().equals(Sensor.Pt100)).count());

        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testRemoveByNameExisted() {
        ControlPoints controlPoints0 = createControlPoints(0, Sensor.TCM_50M);
        expected.remove(0);

        assertTrue(repository.removeByName(controlPoints0.getName()));
        assertNull(repository.get(controlPoints0.getName()));

        Collection<ControlPoints> actual = repository.getAll();
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testRemoveByNameNotExisted() {
        String notExistedName = "NotExisted";

        assertFalse(repository.removeByName(notExistedName));
        assertNull(repository.get(notExistedName));

        Collection<ControlPoints> actual = repository.getAll();
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testRemoveByExistedSensorType() {
        expected.removeIf(cp -> cp.getSensorType().equals(Sensor.TCM_50M));

        assertTrue(repository.removeBySensorType(Sensor.TCM_50M));

        Collection<ControlPoints> actual = repository.getAll();
        assertEquals(0, actual.stream().filter(cp -> cp.getSensorType().equals(Sensor.TCM_50M)).count());

        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testRemoveByNotExistedSensorType() {
        assertTrue(repository.removeBySensorType(Sensor.Pt100));

        Collection<ControlPoints> actual = repository.getAll();
        assertEquals(0, actual.stream().filter(cp -> cp.getSensorType().equals(Sensor.Pt100)).count());

        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testClear() {
        assertTrue(repository.clear());
        assertTrue(repository.getAll().isEmpty());
    }

    @Test
    public void testRewrite() {
        ControlPoints controlPoints0 = createControlPoints(0, Sensor.TCM_50M);
        ControlPoints controlPoints2 = createControlPoints(2, Sensor.TCM_50M);
        ControlPoints controlPoints8 = createControlPoints(8, Sensor.Pt100);
        ControlPoints controlPoints9 = createControlPoints(9, Sensor.Pt100);
        ControlPoints controlPoints5 = createControlPoints(5, Sensor.YOKOGAWA);
        List<ControlPoints> expected = Arrays.asList(
                controlPoints0, controlPoints2, controlPoints8, controlPoints9, controlPoints5
        );

        assertTrue(repository.rewrite(Arrays.asList(
                controlPoints0, controlPoints2, null, controlPoints5, controlPoints8, controlPoints9)));
        assertEquals(controlPoints0, repository.get(controlPoints0.getName()));
        assertEquals(controlPoints2, repository.get(controlPoints2.getName()));
        assertEquals(controlPoints8, repository.get(controlPoints8.getName()));
        assertEquals(controlPoints9, repository.get(controlPoints9.getName()));
        assertEquals(controlPoints5, repository.get(controlPoints5.getName()));

        Collection<ControlPoints> actual = repository.getAll();
        assertFalse(actual.stream().anyMatch(Objects::isNull));

        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }
}