package repository.impl;

import model.Calibrator;
import model.Measurement;
import model.Sensor;
import org.junit.*;
import org.sqlite.JDBC;
import repository.CalibratorRepository;
import repository.SensorRepository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.Assert.*;

public class SensorRepositorySQLiteTest {

    private static final String DB_URL = "jdbc:sqlite:TestData.db";
    private static final SensorRepository repository = new SensorRepositorySQLite(DB_URL, null, null);

    private static Sensor[] testSensors;

    private static Sensor createSensor(int number, Measurement measurement){
        Sensor sensor = new Sensor("sensor" + number);
        sensor.setType("type" + number);
        sensor.setName("name" + number);
        sensor.setRange(number, number+100);
        sensor.setNumber(String.valueOf(number));
        sensor.setValue(measurement.getValue());
        sensor.setMeasurement(measurement.getName());
        sensor.setErrorFormula("2 * " + number);
        return sensor;
    }

    @BeforeClass
    public static void testCreateTable(){
        assertTrue(repository.createTable());
    }

    @AfterClass
    public static void removeTable() throws SQLException {
        String sql = "DROP TABLE sensors;";
        DriverManager.registerDriver(new JDBC());
        try (Connection connection = DriverManager.getConnection(DB_URL);
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
        }
    }

    @Before
    public void fuelingTestDB() throws SQLException {
        testSensors = new Sensor[7];
        for (int n = 0;n < 7;n++){
            if (n < 3) {
                testSensors[n] = createSensor(n, new Measurement(Measurement.TEMPERATURE, Measurement.DEGREE_CELSIUS));
            }else if (n < 5) {
                testSensors[n] = createSensor(n, new Measurement(Measurement.PRESSURE, Measurement.KPA));
            }else {
                testSensors[n] = createSensor(n, new Measurement(Measurement.CONSUMPTION, Measurement.M3_HOUR));
            }
        }

        String insertSql = "INSERT INTO sensors (type, name, range_min, range_max, number, value, measurement, error_formula) "
                + "VALUES ";
        StringBuilder sql = new StringBuilder(insertSql);

        for (Sensor sensor : testSensors) {
            sql.append("('").append(sensor.getType()).append("',")
                    .append("'").append(sensor.getName()).append("',")
                    .append(sensor.getRangeMin()).append(",")
                    .append(sensor.getRangeMax()).append(",")
                    .append("'").append(sensor.getNumber()).append("',")
                    .append("'").append(sensor.getValue()).append("',")
                    .append("'").append(sensor.getMeasurement()).append("',")
                    .append("'").append(sensor.getErrorFormula()).append("'),");
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
        String sql = "DELETE FROM sensors;";
        DriverManager.registerDriver(new JDBC());
        try (Connection connection = DriverManager.getConnection(DB_URL);
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
        }
    }

    @Test
    public void testGetAll() {
        assertArrayEquals(testSensors, repository.getAll().toArray(new Sensor[0]));

        Sensor[] expected = Arrays.stream(testSensors).filter(s -> s.getMeasurement().equals(Measurement.TEMPERATURE)).toArray(Sensor[]::new);
        assertArrayEquals(expected, repository.getAll(Measurement.TEMPERATURE).toArray(new Sensor[0]));

        assertArrayEquals(new Sensor[0], repository.getAll("Not Existed").toArray(new Sensor[0]));
    }

    @Test
    public void testGetAllTypes() {
        String[] expected = Arrays.stream(testSensors).map(Sensor::getType).toArray(String[]::new);

        assertArrayEquals(expected, repository.getAllTypes().toArray(new String[0]));
    }

    @Test
    public void testGetMeasurementExisted(){
        Measurement expected = new Measurement(Measurement.TEMPERATURE, Measurement.DEGREE_CELSIUS);

        Optional<String> actual = repository.getMeasurement(createSensor(1, expected).getType());
        assertTrue(actual.isPresent());
        assertEquals(expected.getName(), actual.get());
    }

    @Test
    public void testGetMeasurementNotExisted(){
        Optional<String> actual = repository.getMeasurement("Not Exist");
        assertFalse(actual.isPresent());
    }

    @Test
    public void testGetAllSensorsName(){
        String[] expected = Arrays.stream(testSensors)
                .filter(s -> s.getMeasurement().equals(Measurement.TEMPERATURE))
                .map(Sensor::getName)
                .toArray(String[]::new);

        repository.getAll().forEach(System.out::println);

        assertArrayEquals(expected, repository.getAllSensorsName(Measurement.TEMPERATURE).toArray(new String[0]));
        assertArrayEquals(new String[0], repository.getAllSensorsName("Not Exist").toArray(new String[0]));
    }
//
//    @SuppressWarnings("OptionalGetWithoutIsPresent")
//    @Test
//    public void testGetExisted() {
//        assertEquals(createCalibrator(1), repository.get("calibrator1").get());
//    }
//
//    @Test
//    public void testGetNotExisted() {
//        assertFalse(repository.get("Not Existed").isPresent());
//    }
//
//    @Test
//    public void testAddNotExisted() {
//        Calibrator[] expected = Arrays.copyOf(testCalibrators, 8);
//        expected[expected.length-1] = createCalibrator(7);
//
//        assertTrue(repository.add(createCalibrator(7)));
//        assertArrayEquals(expected, repository.getAll().toArray(new Calibrator[0]));
//    }
//
//    @Test
//    public void testAddExisted() {
//        assertFalse(repository.add(createCalibrator(2)));
//        assertArrayEquals(testCalibrators, repository.getAll().toArray(new Calibrator[0]));
//    }
//
//    @Test
//    public void testRemoveExisted() {
//        Calibrator[] expected = Arrays.copyOf(testCalibrators, 6);
//
//        assertTrue(repository.remove(createCalibrator(6)));
//        assertArrayEquals(expected, repository.getAll().toArray(new Calibrator[0]));
//    }
//
//    @Test
//    public void testRemoveNotExisted() {
//        assertFalse(repository.remove(createCalibrator(8)));
//        assertArrayEquals(testCalibrators, repository.getAll().toArray(new Calibrator[0]));
//    }
//
//    @Test
//    public void testRemoveByMeasurementValueExisted() {
//        Calibrator[]expected = Arrays.copyOf(testCalibrators, 5);
//
//        assertTrue(repository.removeByMeasurementValue(Measurement.M3_HOUR));
//        assertArrayEquals(expected, repository.getAll().toArray(new Calibrator[0]));
//    }
//
//    @Test
//    public void testRemoveByMeasurementValueNotExisted() {
//        assertFalse(repository.removeByMeasurementValue("Not Existed"));
//        assertArrayEquals(testCalibrators, repository.getAll().toArray(new Calibrator[0]));
//    }
//
//    @Test
//    public void testSetSame() {
//        assertTrue(repository.set(createCalibrator(2), createCalibrator(2)));
//        assertArrayEquals(testCalibrators, repository.getAll().toArray(new Calibrator[0]));
//    }
//
//    @Test
//    public void testSetNew() {
//        testCalibrators[2] = createCalibrator(8);
//
//        assertTrue(repository.set(createCalibrator(2), createCalibrator(8)));
//        assertArrayEquals(testCalibrators, repository.getAll().toArray(new Calibrator[0]));
//    }
//
//    @Test
//    public void testSetExisted() {
//        assertFalse(repository.set(createCalibrator(2), createCalibrator(0)));
//        assertArrayEquals(testCalibrators, repository.getAll().toArray(new Calibrator[0]));
//    }
//
//    @Test
//    public void testSetInsteadNotExisted() {
//        assertFalse(repository.set(createCalibrator(8), createCalibrator(0)));
//        assertArrayEquals(testCalibrators, repository.getAll().toArray(new Calibrator[0]));
//    }
//
//    @Test
//    public void testChangeToSameMeasurementValue() {
//        assertTrue(repository.changeMeasurementValue(Measurement.DEGREE_CELSIUS, Measurement.DEGREE_CELSIUS));
//        assertArrayEquals(testCalibrators, repository.getAll().toArray(new Calibrator[0]));
//    }
//
//    @Test
//    public void testChangeToNewMeasurementValue() {
//        testCalibrators[0].setValue(Measurement.PA);
//        testCalibrators[1].setValue(Measurement.PA);
//        testCalibrators[2].setValue(Measurement.PA);
//
//        assertTrue(repository.changeMeasurementValue(Measurement.DEGREE_CELSIUS, Measurement.PA));
//        assertArrayEquals(testCalibrators, repository.getAll().toArray(new Calibrator[0]));
//    }
//
//    @Test
//    public void testChangeToExistedMeasurementValue() {
//        testCalibrators[0].setValue(Measurement.KPA);
//        testCalibrators[1].setValue(Measurement.KPA);
//        testCalibrators[2].setValue(Measurement.KPA);
//
//        assertTrue(repository.changeMeasurementValue(Measurement.DEGREE_CELSIUS, Measurement.KPA));
//        assertArrayEquals(testCalibrators, repository.getAll().toArray(new Calibrator[0]));
//    }
//
//    @Test
//    public void testChangeInsteadNotExistedMeasurementValue() {
//        assertFalse(repository.changeMeasurementValue(Measurement.PA, Measurement.KPA));
//        assertArrayEquals(testCalibrators, repository.getAll().toArray(new Calibrator[0]));
//    }
//
//    @Test
//    public void testClear() {
//        assertTrue(repository.clear());
//        assertArrayEquals(new Calibrator[0], repository.getAll().toArray(new Calibrator[0]));
//    }
//
//    @Test
//    public void testRewriteNotEmpty() {
//        Calibrator[] expected = new Calibrator[]{createCalibrator(8), createCalibrator(9), createCalibrator(0)};
//        Calibrator[] toRewrite = new Calibrator[]{createCalibrator(8), null, createCalibrator(9), createCalibrator(0)};
//
//        assertTrue(repository.rewrite(Arrays.asList(toRewrite)));
//        assertArrayEquals(expected, repository.getAll().toArray(new Calibrator[0]));
//    }
//
//    @Test
//    public void testRewriteEmpty() {
//        assertTrue(repository.rewrite(new ArrayList<>()));
//        assertArrayEquals(new Calibrator[0], repository.getAll().toArray(new Calibrator[0]));
//    }
//
//    @Test
//    public void testImportDataWithNewAndChanging() {
//        testCalibrators[2].setErrorFormula("New Error-Formula");
//        testCalibrators[4].setNumber("New Number");
//        Calibrator[] expected = Arrays.copyOf(testCalibrators, 9);
//        expected[expected.length-2] = createCalibrator(8);
//        expected[expected.length-1] = createCalibrator(9);
//
//        ArrayList<Calibrator>forChange = new ArrayList<>();
//        forChange.add(testCalibrators[2]);
//        forChange.add(null);
//        forChange.add(testCalibrators[4]);
//
//        ArrayList<Calibrator>newCal = new ArrayList<>();
//        newCal.add(createCalibrator(8));
//        newCal.add(null);
//        newCal.add(createCalibrator(9));
//
//        assertTrue(repository.importData(newCal, forChange));
//        assertArrayEquals(expected, repository.getAll().toArray(new Calibrator[0]));
//    }
//
//    @Test
//    public void testImportDataOnlyWithNew() {
//        Calibrator[] expected = Arrays.copyOf(testCalibrators, 9);
//        expected[expected.length-2] = createCalibrator(8);
//        expected[expected.length-1] = createCalibrator(9);
//
//        ArrayList<Calibrator>newCal = new ArrayList<>();
//        newCal.add(createCalibrator(8));
//        newCal.add(createCalibrator(9));
//
//        assertTrue(repository.importData(newCal, new ArrayList<>()));
//        assertArrayEquals(expected, repository.getAll().toArray(new Calibrator[0]));
//    }
//
//    @Test
//    public void testImportDataOnlyWithChanging() {
//        testCalibrators[2].setErrorFormula("New Error-Formula");
//        testCalibrators[4].setNumber("New Number");
//
//        ArrayList<Calibrator>forChange = new ArrayList<>();
//        forChange.add(testCalibrators[2]);
//        forChange.add(testCalibrators[4]);
//
//        assertTrue(repository.importData(new ArrayList<>(), forChange));
//        assertArrayEquals(testCalibrators, repository.getAll().toArray(new Calibrator[0]));
//    }
//
//    @Test
//    public void testImportDataWithoutNewAndChanging() {
//        assertTrue(repository.importData(new ArrayList<>(), new ArrayList<>()));
//        assertArrayEquals(testCalibrators, repository.getAll().toArray(new Calibrator[0]));
//    }
//
//    @Test
//    public void testIsExists() {
//        assertTrue(repository.isExists(createCalibrator(0)));
//        assertFalse(repository.isExists(createCalibrator(8)));
//    }
}