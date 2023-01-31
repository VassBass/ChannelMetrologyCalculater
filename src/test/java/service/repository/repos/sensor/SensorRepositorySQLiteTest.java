package service.repository.repos.sensor;

import model.Measurement;
import model.Sensor;
import model.builder.SensorBuilder;
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

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.junit.Assert.*;

public class SensorRepositorySQLiteTest {
    private static final String TEST_REPOSITORY_PROPERTIES_FILE = "properties/test_repository.properties";
    private static final File TEST_DB_FILE = new File("TestData.db");
    private static final String TEST_DB_URL = "jdbc:sqlite:TestData.db";
    private static final String TABLE_NAME = "sensors";

    private List<Sensor> expected;

    private static Sensor createSensor(int number){
        String measurementName = number < 3 ?
                Measurement.TEMPERATURE :
                number < 5 ? Measurement.PRESSURE : Measurement.CONSUMPTION;
        String measurementValue = number < 3 ?
                Measurement.DEGREE_CELSIUS :
                number < 5 ? Measurement.KPA : Measurement.M3_HOUR;
        String type = number < 3 ?
                Sensor.TCM_50M :
                number < 5 ? Sensor.YOKOGAWA : Sensor.ROSEMOUNT;

        return new SensorBuilder(String.valueOf(number))
                .setMeasurementName(measurementName)
                .setMeasurementValue(measurementValue)
                .setType(type)
                .build();
    }

    private SensorRepository repository;

    @BeforeClass
    public static void createDB() throws IOException, SQLException {
        Files.createFile(TEST_DB_FILE.toPath());
        String sql = String.format("CREATE TABLE IF NOT EXISTS %s (" +
                "name text NOT NULL UNIQUE" +
                ", type text NOT NULL" +
                ", number text" +
                ", measurement text NOT NULL" +
                ", value text" +
                ", error_formula text NOT NULL" +
                ", range_min real NOT NULL" +
                ", range_max real NOT NULL" +
                ", PRIMARY KEY (\"name\")" +
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
        expected = new ArrayList<>(7);
        for (int x = 0; x < 7; x++) expected.add(createSensor(x));

        String sql = String.format("INSERT INTO %s (name, type, number, measurement, value, error_formula, range_min, range_max) "
                + "VALUES ", TABLE_NAME);
        StringBuilder sqlBuilder = new StringBuilder(sql);

        for (Sensor sensor : expected) {
            String values = String.format("('%s', '%s', '%s', '%s', '%s', '%s', %s, %s),",
                    sensor.getName(),
                    sensor.getType(),
                    sensor.getNumber(),
                    sensor.getMeasurement(),
                    sensor.getValue(),
                    sensor.getErrorFormula(),
                    sensor.getRangeMin(),
                    sensor.getRangeMax());
            sqlBuilder.append(values);
        }
        sqlBuilder.setCharAt(sqlBuilder.length()-1, ';');

        try (Connection connection = DriverManager.getConnection(TEST_DB_URL);
             Statement statement = connection.createStatement()) {
            statement.execute(sqlBuilder.toString());
        }

        RepositoryConfigHolder configHolder = new SqliteRepositoryConfigHolder(TEST_REPOSITORY_PROPERTIES_FILE);
        RepositoryDBConnector connector = new SqliteRepositoryDBConnector(configHolder);
        repository = new SensorRepositorySQLite(configHolder, connector);
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
        Collection<Sensor> actual = repository.getAll();
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testGetAllByExistedMeasurementName() {
        List<Sensor> expected = this.expected.stream()
                .filter(s -> s.getMeasurement().equals(Measurement.TEMPERATURE))
                .collect(Collectors.toList());

        Collection<Sensor> actual = repository.getAllByMeasurementName(Measurement.TEMPERATURE);
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testGetAllByNotExistedMeasurementName() {
        Collection<Sensor> actual = repository.getAllByMeasurementName("Not Existed");
        assertNotNull(actual);
        assertTrue(actual.isEmpty());
    }

    @Test
    public void testGetAllTypes() {
        List<String> expected = Arrays.asList(Sensor.TCM_50M, Sensor.ROSEMOUNT, Sensor.YOKOGAWA);

        Collection<String> actual = repository.getAllTypes();
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testGetMeasurementNameBySensorType() {
        String expected = Measurement.TEMPERATURE;

        assertEquals(expected, repository.getMeasurementNameBySensorType(Sensor.TCM_50M));
        assertTrue(repository.getMeasurementNameBySensorType("Not existed").isEmpty());
    }

    @Test
    public void testGetAllSensorsNameByExistedMeasurementName() {
        List<String> expected = this.expected.stream()
                .filter(s -> s.getMeasurement().equals(Measurement.TEMPERATURE))
                .map(Sensor::getName)
                .collect(Collectors.toList());

        Collection<String> actual = repository.getAllSensorsNameByMeasurementName(Measurement.TEMPERATURE);
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testGetAllSensorsNameByNotExistedMeasurementName() {
        Collection<String> actual = repository.getAllSensorsNameByMeasurementName("Not Existed");
        assertNotNull(actual);
        assertTrue(actual.isEmpty());
    }

    @Test
    public void testGetExisted() {
        Sensor sensor0 = createSensor(0);

        assertEquals(sensor0, repository.get(sensor0.getName()));
    }

    @Test
    public void testGetNotExisted() {
        assertNull(repository.get("Not existed"));
    }

    @Test
    public void testAddNew() {
        Sensor sensor8 = createSensor(8);
        expected.add(sensor8);

        assertTrue(repository.add(sensor8));
        assertEquals(sensor8, repository.get(sensor8.getName()));

        Collection<Sensor> actual = repository.getAll();
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testAddExisted() {
        Sensor sensor0 = createSensor(0);

        assertFalse(repository.add(sensor0));
        assertNotNull(repository.get(sensor0.getName()));

        Collection<Sensor> actual = repository.getAll();
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testRemoveExisted() {
        Sensor sensor0 = createSensor(0);
        expected.remove(0);

        assertTrue(repository.remove(sensor0));
        assertNull(repository.get(sensor0.getName()));

        Collection<Sensor> actual = repository.getAll();
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testRemoveNotExisted() {
        Sensor sensor8 = createSensor(8);

        assertFalse(repository.remove(sensor8));
        assertNull(repository.get(sensor8.getName()));

        Collection<Sensor> actual = repository.getAll();
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testRemoveExistedMeasurementValue() {
        expected.forEach(s -> {
            if (s.getValue().equals(Measurement.DEGREE_CELSIUS)) s.setValue(EMPTY);
        });

        assertTrue(repository.removeMeasurementValue(Measurement.DEGREE_CELSIUS));

        Sensor sensor0 = repository.get("0");
        assertNotNull(sensor0);
        assertTrue(sensor0.getValue().isEmpty());

        Sensor sensor1 = repository.get("1");
        assertNotNull(sensor1);
        assertTrue(sensor1.getValue().isEmpty());

        Sensor sensor2 = repository.get("2");
        assertNotNull(sensor2);
        assertTrue(sensor2.getValue().isEmpty());

        Collection<Sensor> actual = repository.getAll();
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testRemoveNotExistedMeasurementValue() {
        assertTrue(repository.removeMeasurementValue("Not existed"));

        Sensor sensor0 = repository.get("0");
        assertNotNull(sensor0);
        assertEquals(Measurement.DEGREE_CELSIUS, sensor0.getValue());

        Sensor sensor3 = repository.get("3");
        assertNotNull(sensor3);
        assertEquals(Measurement.KPA, sensor3.getValue());

        Sensor sensor5 = repository.get("5");
        assertNotNull(sensor5);
        assertEquals(Measurement.M3_HOUR, sensor5.getValue());

        Collection<Sensor> actual = repository.getAll();
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testClear() {
        assertTrue(repository.clear());
        assertEquals(0, repository.getAll().size());
    }

    @Test
    public void testSetNewInsteadExisted() {
        Sensor sensor0 = createSensor(0);
        Sensor sensor8 = createSensor(8);
        expected.set(0, sensor8);

        assertTrue(repository.set(sensor0, sensor8));
        assertNull(repository.get(sensor0.getName()));
        assertEquals(sensor8, repository.get(sensor8.getName()));

        Collection<Sensor> actual = repository.getAll();
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testSetNewInsteadNotExisted() {
        Sensor sensor9 = createSensor(9);
        Sensor sensor8 = createSensor(8);

        assertFalse(repository.set(sensor9, sensor8));
        assertNull(repository.get(sensor8.getName()));
        assertNull(repository.get(sensor9.getName()));

        Collection<Sensor> actual = repository.getAll();
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testSetSameExisted() {
        Sensor sensor0 = createSensor(0);

        assertTrue(repository.set(sensor0, sensor0));
        assertEquals(sensor0, repository.get(sensor0.getName()));

        Collection<Sensor> actual = repository.getAll();
        assertEquals(1, actual.stream().filter(s -> s.equals(sensor0)).count());

        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testSetSameNotExisted() {
        Sensor sensor8 = createSensor(8);

        assertFalse(repository.set(sensor8, sensor8));
        assertNull(repository.get(sensor8.getName()));

        Collection<Sensor> actual = repository.getAll();
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testSetChangedInsteadExisted() {
        String changedNumber = "Changed number";
        Sensor sensor0 = createSensor(0);
        sensor0.setNumber(changedNumber);
        expected.set(0, sensor0);

        assertTrue(repository.set(sensor0, sensor0));

        Sensor repoSensor = repository.get(sensor0.getName());
        assertNotNull(repoSensor);
        assertEquals(sensor0, repoSensor);
        assertEquals(changedNumber, repoSensor.getNumber());

        Collection<Sensor> actual = repository.getAll();
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testSetChangedInsteadNotExisted() {
        String changedNumber = "Changed number";
        Sensor sensor0 = createSensor(0);
        sensor0.setNumber(changedNumber);
        Sensor sensor8 = createSensor(8);

        assertFalse(repository.set(sensor8, sensor0));
        assertNull(repository.get(sensor8.getName()));

        Sensor repoSensor = repository.get(sensor0.getName());
        assertNotNull(repoSensor);
        assertEquals(sensor0, repoSensor);
        assertNotEquals(changedNumber, repoSensor.getNumber());

        Collection<Sensor> actual = repository.getAll();
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testSetExistedInsteadChanged() {
        String changedNumber = "Changed number";
        Sensor sensor0 = createSensor(0);
        Sensor sensor0changed = createSensor(0);
        sensor0changed.setNumber(changedNumber);

        assertTrue(repository.set(sensor0changed, sensor0));

        Sensor repoSensor = repository.get(sensor0.getName());
        assertNotNull(repoSensor);
        assertEquals(sensor0, repoSensor);
        assertNotEquals(changedNumber, repoSensor.getNumber());

        Collection<Sensor> actual = repository.getAll();
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testChangeMeasurementValueToNew() {
        expected.forEach(s -> {
            if (s.getValue().equals(Measurement.DEGREE_CELSIUS)) s.setValue(Measurement.ML_BAR);
        });

        assertTrue(repository.changeMeasurementValue(Measurement.DEGREE_CELSIUS, Measurement.ML_BAR));

        Collection<Sensor> actual = repository.getAll();
        assertEquals(0, actual.stream().filter(s -> s.getValue().equals(Measurement.DEGREE_CELSIUS)).count());
        assertEquals(3, actual.stream().filter(s -> s.getValue().equals(Measurement.ML_BAR)).count());

        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testChangeMeasurementValueToExisted() {
        expected.forEach(s -> {
            if (s.getValue().equals(Measurement.DEGREE_CELSIUS)) s.setValue(Measurement.M3_HOUR);
        });

        assertTrue(repository.changeMeasurementValue(Measurement.DEGREE_CELSIUS, Measurement.M3_HOUR));

        Collection<Sensor> actual = repository.getAll();
        assertEquals(0, actual.stream().filter(s -> s.getValue().equals(Measurement.DEGREE_CELSIUS)).count());
        assertEquals(5, actual.stream().filter(s -> s.getValue().equals(Measurement.M3_HOUR)).count());

        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testChangeNotExistedMeasurementValueToNew() {
        assertTrue(repository.changeMeasurementValue(Measurement.CM_S, Measurement.ML_BAR));

        Collection<Sensor> actual = repository.getAll();
        assertEquals(0, actual.stream().filter(s -> s.getValue().equals(Measurement.CM_S)).count());
        assertEquals(0, actual.stream().filter(s -> s.getValue().equals(Measurement.ML_BAR)).count());

        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testChangeNotExistedMeasurementValueToExisted() {
        assertTrue(repository.changeMeasurementValue(Measurement.ML_BAR, Measurement.DEGREE_CELSIUS));

        Collection<Sensor> actual = repository.getAll();
        assertEquals(0, actual.stream().filter(s -> s.getValue().equals(Measurement.ML_BAR)).count());
        assertEquals(3, actual.stream().filter(s -> s.getValue().equals(Measurement.DEGREE_CELSIUS)).count());

        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testRewrite() {
        Sensor sensor0 = createSensor(0);
        Sensor sensor2 = createSensor(2);
        Sensor sensor8 = createSensor(8);
        List<Sensor> toRewrite = Arrays.asList(sensor0, sensor8, null, sensor2);
        List<Sensor> expected = Arrays.asList(sensor0, sensor8, sensor2);

        assertTrue(repository.rewrite(toRewrite));

        Collection<Sensor> actual = repository.getAll();
        assertFalse(actual.stream().anyMatch(Objects::isNull));

        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testImportDataWithNewAndChanging() {
        String changedNumber = "Changed number";
        Sensor sensor0 = createSensor(0);
        sensor0.setNumber(changedNumber);
        Sensor sensor1 = createSensor(1);
        sensor1.setNumber(changedNumber);
        Sensor sensor2 = createSensor(2);
        sensor2.setNumber(changedNumber);
        Sensor sensor8 = createSensor(8);
        Sensor sensor9 = createSensor(9);

        expected.set(0, sensor0);
        expected.set(1, sensor1);
        expected.set(2, sensor2);
        expected.addAll(Arrays.asList(sensor8, sensor9));

        assertTrue(repository.importData(Arrays.asList(sensor8, null, sensor9), Arrays.asList(sensor0, sensor2, null, sensor1)));
        assertFalse(repository.getAll().stream().anyMatch(Objects::isNull));

        Sensor actualSensor0 = repository.get(sensor0.getName());
        assertNotNull(actualSensor0);
        assertEquals(sensor0, actualSensor0);
        assertEquals(sensor0.getNumber(), actualSensor0.getNumber());

        Sensor actualSensor1 = repository.get(sensor1.getName());
        assertNotNull(actualSensor1);
        assertEquals(sensor1, actualSensor1);
        assertEquals(sensor1.getNumber(), actualSensor1.getNumber());

        Sensor actualSensor2 = repository.get(sensor2.getName());
        assertNotNull(actualSensor2);
        assertEquals(sensor2, actualSensor2);
        assertEquals(sensor2.getNumber(), actualSensor2.getNumber());

        assertEquals(sensor8, repository.get(sensor8.getName()));
        assertEquals(sensor9, repository.get(sensor9.getName()));

        Collection<Sensor> actual = repository.getAll();
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testImportDataOnlyWithNew() {
        Sensor sensor8 = createSensor(8);
        Sensor sensor9 = createSensor(9);
        expected.addAll(Arrays.asList(sensor8, sensor9));

        assertTrue(repository.importData(Arrays.asList(sensor8, null, sensor9), new ArrayList<>()));
        assertFalse(repository.getAll().stream().anyMatch(Objects::isNull));

        assertEquals(sensor8, repository.get(sensor8.getName()));
        assertEquals(sensor9, repository.get(sensor9.getName()));

        Collection<Sensor> actual = repository.getAll();
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testImportDataOnlyWithChanging() {
        String changedNumber = "Changed number";
        Sensor sensor0 = createSensor(0);
        sensor0.setNumber(changedNumber);
        Sensor sensor1 = createSensor(1);
        sensor1.setNumber(changedNumber);
        Sensor sensor2 = createSensor(2);
        sensor2.setNumber(changedNumber);

        expected.set(0, sensor0);
        expected.set(1, sensor1);
        expected.set(2, sensor2);

        assertTrue(repository.importData(new ArrayList<>(), Arrays.asList(sensor0, sensor2, null, sensor1)));
        assertFalse(repository.getAll().stream().anyMatch(Objects::isNull));

        Sensor actualSensor0 = repository.get(sensor0.getName());
        assertNotNull(actualSensor0);
        assertEquals(sensor0, actualSensor0);
        assertEquals(sensor0.getNumber(), actualSensor0.getNumber());

        Sensor actualSensor1 = repository.get(sensor1.getName());
        assertNotNull(actualSensor1);
        assertEquals(sensor1, actualSensor1);
        assertEquals(sensor1.getNumber(), actualSensor1.getNumber());

        Sensor actualSensor2 = repository.get(sensor2.getName());
        assertNotNull(actualSensor2);
        assertEquals(sensor2, actualSensor2);
        assertEquals(sensor2.getNumber(), actualSensor2.getNumber());

        Collection<Sensor> actual = repository.getAll();
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testImportDataWithoutNewAndChanging() {
        assertTrue(repository.importData(new ArrayList<>(), new ArrayList<>()));

        Collection<Sensor> actual = repository.getAll();
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testIsLastInMeasurement() {
        repository.remove(createSensor(0));
        repository.remove(createSensor(1));
        assertTrue(repository.isLastInMeasurement(createSensor(2)));
        assertFalse(repository.isLastInMeasurement(createSensor(3)));
    }

    @Test
    public void testIsExists() {
        assertTrue(repository.isExists("0"));
        assertFalse(repository.isExists("8"));
    }
}