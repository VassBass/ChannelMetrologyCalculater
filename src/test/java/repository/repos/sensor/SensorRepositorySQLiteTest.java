package repository.repos.sensor;

import model.dto.Measurement;
import model.dto.Sensor;
import model.dto.builder.SensorBuilder;
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
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class SensorRepositorySQLiteTest {
    private static final String TEST_REPOSITORY_PROPERTIES_FILE = "properties/repository_test.properties";
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
        String type = number < 2 ?
                Sensor.TCM_50M :
                number == 2 ? Sensor.Pt100 :
                number < 5 ? Sensor.YOKOGAWA : Sensor.ROSEMOUNT;

        return new SensorBuilder(String.valueOf(number))
                .setMeasurementName(measurementName)
                .setMeasurementValue(measurementValue)
                .setType(type)
                .setErrorFormula("Formula for: " + type)
                .build();
    }

    private SensorRepository repository;

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @BeforeClass
    public static void createDB() throws IOException, SQLException {
        TEST_DB_FILE.createNewFile();
        String sql = String.format("CREATE TABLE IF NOT EXISTS %s (" +
                "channel_code text NOT NULL UNIQUE" +
                ", type text NOT NULL" +
                ", serial_number text" +
                ", measurement_name text NOT NULL" +
                ", measurement_value text" +
                ", error_formula text NOT NULL" +
                ", range_min real NOT NULL" +
                ", range_max real NOT NULL" +
                ", PRIMARY KEY (channel_code)" +
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
        expected = new ArrayList<>(7);
        for (int x = 0; x < 7; x++) expected.add(createSensor(x));

        String sql = String.format("INSERT INTO %s (channel_code, type, serial_number, measurement_name, measurement_value, " +
                "error_formula, range_min, range_max) "
                + "VALUES ", TABLE_NAME);
        StringBuilder sqlBuilder = new StringBuilder(sql);

        for (Sensor sensor : expected) {
            String values = String.format("('%s', '%s', '%s', '%s', '%s', '%s', %s, %s),",
                    sensor.getChannelCode(),
                    sensor.getType(),
                    sensor.getSerialNumber(),
                    sensor.getMeasurementName(),
                    sensor.getMeasurementValue(),
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
                .filter(s -> s.getMeasurementName().equals(Measurement.TEMPERATURE))
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
        List<String> expected = Arrays.asList(Sensor.TCM_50M, Sensor.Pt100, Sensor.ROSEMOUNT, Sensor.YOKOGAWA);

        Collection<String> actual = repository.getAllTypes();
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testGetAllSensorsTypesByExistedMeasurementName() {
        List<String> expected = this.expected.stream()
                .filter(s -> s.getMeasurementName().equals(Measurement.TEMPERATURE))
                .map(Sensor::getType)
                .distinct()
                .collect(Collectors.toList());

        Collection<String> actual = repository.getAllSensorsTypesByMeasurementName(Measurement.TEMPERATURE);
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testGetAllSensorsTypesByNotExistedMeasurementName() {
        Collection<String> actual = repository.getAllSensorsTypesByMeasurementName("Not Existed");
        assertNotNull(actual);
        assertTrue(actual.isEmpty());
    }

    @Test
    public void testGetExisted() {
        Sensor sensor0 = createSensor(0);

        assertEquals(sensor0, repository.get(sensor0.getChannelCode()));
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
        assertEquals(sensor8, repository.get(sensor8.getChannelCode()));

        Collection<Sensor> actual = repository.getAll();
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testAddExisted() {
        Sensor sensor0 = createSensor(0);

        assertFalse(repository.add(sensor0));
        assertNotNull(repository.get(sensor0.getChannelCode()));

        Collection<Sensor> actual = repository.getAll();
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testRemoveExisted() {
        Sensor sensor0 = createSensor(0);
        expected.remove(0);

        assertTrue(repository.remove(sensor0));
        assertNull(repository.get(sensor0.getChannelCode()));

        Collection<Sensor> actual = repository.getAll();
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testRemoveNotExisted() {
        Sensor sensor8 = createSensor(8);

        assertFalse(repository.remove(sensor8));
        assertNull(repository.get(sensor8.getChannelCode()));

        Collection<Sensor> actual = repository.getAll();
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testRemoveByExistedChannelCode() {
        String channelCode = "0";
        expected.remove(0);

        assertTrue(repository.removeByChannelCode(channelCode));
        assertNull(repository.get(channelCode));

        Collection<Sensor> actual = repository.getAll();
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testRemoveByNotExistedChannelCode() {
        String channelCode = "Not Existed";

        assertFalse(repository.removeByChannelCode(channelCode));
        assertNull(repository.get(channelCode));

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
        assertNull(repository.get(sensor0.getChannelCode()));
        assertEquals(sensor8, repository.get(sensor8.getChannelCode()));

        Collection<Sensor> actual = repository.getAll();
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testSetNewInsteadNotExisted() {
        Sensor sensor9 = createSensor(9);
        Sensor sensor8 = createSensor(8);

        assertFalse(repository.set(sensor9, sensor8));
        assertNull(repository.get(sensor8.getChannelCode()));
        assertNull(repository.get(sensor9.getChannelCode()));

        Collection<Sensor> actual = repository.getAll();
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testSetSameExisted() {
        Sensor sensor0 = createSensor(0);

        assertTrue(repository.set(sensor0, sensor0));
        assertEquals(sensor0, repository.get(sensor0.getChannelCode()));

        Collection<Sensor> actual = repository.getAll();
        assertEquals(1, actual.stream().filter(s -> s.equals(sensor0)).count());

        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testSetSameNotExisted() {
        Sensor sensor8 = createSensor(8);

        assertFalse(repository.set(sensor8, sensor8));
        assertNull(repository.get(sensor8.getChannelCode()));

        Collection<Sensor> actual = repository.getAll();
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testSetChangedInsteadExisted() {
        String changedNumber = "Changed number";
        Sensor sensor0 = createSensor(0);
        sensor0.setSerialNumber(changedNumber);
        expected.set(0, sensor0);

        assertTrue(repository.set(sensor0, sensor0));

        Sensor repoSensor = repository.get(sensor0.getChannelCode());
        assertNotNull(repoSensor);
        assertEquals(sensor0, repoSensor);
        assertEquals(changedNumber, repoSensor.getSerialNumber());

        Collection<Sensor> actual = repository.getAll();
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testSetChangedInsteadNotExisted() {
        String changedNumber = "Changed number";
        Sensor sensor0 = createSensor(0);
        sensor0.setSerialNumber(changedNumber);
        Sensor sensor8 = createSensor(8);

        assertFalse(repository.set(sensor8, sensor0));
        assertNull(repository.get(sensor8.getChannelCode()));

        Sensor repoSensor = repository.get(sensor0.getChannelCode());
        assertNotNull(repoSensor);
        assertEquals(sensor0, repoSensor);
        assertNotEquals(changedNumber, repoSensor.getSerialNumber());

        Collection<Sensor> actual = repository.getAll();
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testSetExistedInsteadChanged() {
        String changedNumber = "Changed number";
        Sensor sensor0 = createSensor(0);
        Sensor sensor0changed = createSensor(0);
        sensor0changed.setSerialNumber(changedNumber);

        assertTrue(repository.set(sensor0changed, sensor0));

        Sensor repoSensor = repository.get(sensor0.getChannelCode());
        assertNotNull(repoSensor);
        assertEquals(sensor0, repoSensor);
        assertNotEquals(changedNumber, repoSensor.getSerialNumber());

        Collection<Sensor> actual = repository.getAll();
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testChangeMeasurementValueToNew() {
        expected.forEach(s -> {
            if (s.getMeasurementValue().equals(Measurement.DEGREE_CELSIUS)) s.setMeasurementValue(Measurement.ML_BAR);
        });

        assertTrue(repository.changeMeasurementValue(Measurement.DEGREE_CELSIUS, Measurement.ML_BAR));

        Collection<Sensor> actual = repository.getAll();
        assertEquals(0, actual.stream().filter(s -> s.getMeasurementValue().equals(Measurement.DEGREE_CELSIUS)).count());
        assertEquals(3, actual.stream().filter(s -> s.getMeasurementValue().equals(Measurement.ML_BAR)).count());

        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testChangeMeasurementValueToExisted() {
        expected.forEach(s -> {
            if (s.getMeasurementValue().equals(Measurement.DEGREE_CELSIUS)) s.setMeasurementValue(Measurement.M3_HOUR);
        });

        assertTrue(repository.changeMeasurementValue(Measurement.DEGREE_CELSIUS, Measurement.M3_HOUR));

        Collection<Sensor> actual = repository.getAll();
        assertEquals(0, actual.stream().filter(s -> s.getMeasurementValue().equals(Measurement.DEGREE_CELSIUS)).count());
        assertEquals(5, actual.stream().filter(s -> s.getMeasurementValue().equals(Measurement.M3_HOUR)).count());

        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testChangeNotExistedMeasurementValueToNew() {
        assertTrue(repository.changeMeasurementValue(Measurement.CM_S, Measurement.ML_BAR));

        Collection<Sensor> actual = repository.getAll();
        assertEquals(0, actual.stream().filter(s -> s.getMeasurementValue().equals(Measurement.CM_S)).count());
        assertEquals(0, actual.stream().filter(s -> s.getMeasurementValue().equals(Measurement.ML_BAR)).count());

        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testChangeNotExistedMeasurementValueToExisted() {
        assertTrue(repository.changeMeasurementValue(Measurement.ML_BAR, Measurement.DEGREE_CELSIUS));

        Collection<Sensor> actual = repository.getAll();
        assertEquals(0, actual.stream().filter(s -> s.getMeasurementValue().equals(Measurement.ML_BAR)).count());
        assertEquals(3, actual.stream().filter(s -> s.getMeasurementValue().equals(Measurement.DEGREE_CELSIUS)).count());

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
        sensor0.setSerialNumber(changedNumber);
        Sensor sensor1 = createSensor(1);
        sensor1.setSerialNumber(changedNumber);
        Sensor sensor2 = createSensor(2);
        sensor2.setSerialNumber(changedNumber);
        Sensor sensor8 = createSensor(8);
        Sensor sensor9 = createSensor(9);

        expected.set(0, sensor0);
        expected.set(1, sensor1);
        expected.set(2, sensor2);
        expected.addAll(Arrays.asList(sensor8, sensor9));

        assertTrue(repository.importData(Arrays.asList(sensor8, null, sensor9), Arrays.asList(sensor0, sensor2, null, sensor1)));
        assertFalse(repository.getAll().stream().anyMatch(Objects::isNull));

        Sensor actualSensor0 = repository.get(sensor0.getChannelCode());
        assertNotNull(actualSensor0);
        assertEquals(sensor0, actualSensor0);
        assertEquals(sensor0.getSerialNumber(), actualSensor0.getSerialNumber());

        Sensor actualSensor1 = repository.get(sensor1.getChannelCode());
        assertNotNull(actualSensor1);
        assertEquals(sensor1, actualSensor1);
        assertEquals(sensor1.getSerialNumber(), actualSensor1.getSerialNumber());

        Sensor actualSensor2 = repository.get(sensor2.getChannelCode());
        assertNotNull(actualSensor2);
        assertEquals(sensor2, actualSensor2);
        assertEquals(sensor2.getSerialNumber(), actualSensor2.getSerialNumber());

        assertEquals(sensor8, repository.get(sensor8.getChannelCode()));
        assertEquals(sensor9, repository.get(sensor9.getChannelCode()));

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

        assertEquals(sensor8, repository.get(sensor8.getChannelCode()));
        assertEquals(sensor9, repository.get(sensor9.getChannelCode()));

        Collection<Sensor> actual = repository.getAll();
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testImportDataOnlyWithChanging() {
        String changedNumber = "Changed number";
        Sensor sensor0 = createSensor(0);
        sensor0.setSerialNumber(changedNumber);
        Sensor sensor1 = createSensor(1);
        sensor1.setSerialNumber(changedNumber);
        Sensor sensor2 = createSensor(2);
        sensor2.setSerialNumber(changedNumber);

        expected.set(0, sensor0);
        expected.set(1, sensor1);
        expected.set(2, sensor2);

        assertTrue(repository.importData(new ArrayList<>(), Arrays.asList(sensor0, sensor2, null, sensor1)));
        assertFalse(repository.getAll().stream().anyMatch(Objects::isNull));

        Sensor actualSensor0 = repository.get(sensor0.getChannelCode());
        assertNotNull(actualSensor0);
        assertEquals(sensor0, actualSensor0);
        assertEquals(sensor0.getSerialNumber(), actualSensor0.getSerialNumber());

        Sensor actualSensor1 = repository.get(sensor1.getChannelCode());
        assertNotNull(actualSensor1);
        assertEquals(sensor1, actualSensor1);
        assertEquals(sensor1.getSerialNumber(), actualSensor1.getSerialNumber());

        Sensor actualSensor2 = repository.get(sensor2.getChannelCode());
        assertNotNull(actualSensor2);
        assertEquals(sensor2, actualSensor2);
        assertEquals(sensor2.getSerialNumber(), actualSensor2.getSerialNumber());

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
    public void testIsExists() {
        assertTrue(repository.isExists("0"));
        assertFalse(repository.isExists("8"));
    }

    @Test
    public void testGetExistedErrorFormula() {
        String expected = "Formula for: " + Sensor.YOKOGAWA;

        assertEquals(expected, repository.getErrorFormula(Sensor.YOKOGAWA));
    }

    @Test
    public void testGetNotExistedErrorFormula() {
        assertTrue(repository.getErrorFormula("Not Existed").isEmpty());
    }

    @Test
    public void setErrorFormulaToExistedSensors() {
        String oldFormula = "Formula for: " + Sensor.TCM_50M;
        String newFormula = "New Formula";
        expected.forEach(s -> {
            if (s.getType().equals(Sensor.TCM_50M)) s.setErrorFormula(newFormula);
        });

        Collection<Sensor> actual = repository.getAll();
        assertEquals(2, actual.stream().filter(s -> s.getErrorFormula().equals(oldFormula)).count());
        assertEquals(0, actual.stream().filter(s -> s.getErrorFormula().equals(newFormula)).count());
        assertTrue(repository.setErrorFormula(Sensor.TCM_50M, newFormula));

        actual = repository.getAll();
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
        assertEquals(0, actual.stream().filter(s -> s.getErrorFormula().equals(oldFormula)).count());
        assertEquals(2, actual.stream().filter(s -> s.getErrorFormula().equals(newFormula)).count());
    }

    @Test
    public void setErrorFormulaToNotExistedSensors() {
        String newFormula = "New Formula";

        Collection<Sensor> actual = repository.getAll();
        assertEquals(0, actual.stream().filter(s -> s.getErrorFormula().equals(newFormula)).count());
        assertTrue(repository.setErrorFormula("Not Existed", newFormula));

        actual = repository.getAll();
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
        assertEquals(0, actual.stream().filter(s -> s.getErrorFormula().equals(newFormula)).count());
    }
}