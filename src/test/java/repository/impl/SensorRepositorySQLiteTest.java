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

@SuppressWarnings("OptionalGetWithoutIsPresent")
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

    @Test
    public void testGetExisted() {
        assertEquals(testSensors[1], repository.get("name1").get());
    }

    @Test
    public void testGetNotExisted() {
        assertFalse(repository.get("Not Existed").isPresent());
    }

    @Test
    public void testAddNotExisted() {
        Sensor toAdd = createSensor(7, new Measurement(Measurement.TEMPERATURE, Measurement.DEGREE_CELSIUS));
        Sensor[] expected = Arrays.copyOf(testSensors, 8);
        expected[expected.length-1] = toAdd;

        assertTrue(repository.add(toAdd));
        assertTrue(repository.get("name7").isPresent());
        assertArrayEquals(expected, repository.getAll().toArray(new Sensor[0]));
    }

    @Test
    public void testAddExisted() {
        assertFalse(repository.add(testSensors[2]));
        assertArrayEquals(testSensors, repository.getAll().toArray(new Sensor[0]));
    }

    @Test
    public void testRemoveExisted() {
        Sensor[] expected = Arrays.copyOf(testSensors, 6);

        assertTrue(repository.remove(testSensors[6]));
        assertFalse(repository.get("name6").isPresent());
        assertArrayEquals(expected, repository.getAll().toArray(new Sensor[0]));
    }

    @Test
    public void testRemoveNotExisted() {
        assertFalse(repository.remove(createSensor(8, new Measurement())));
        assertArrayEquals(testSensors, repository.getAll().toArray(new Sensor[0]));
    }

    @Test
    public void testSetSame() {
        assertTrue(repository.set(testSensors[2], testSensors[2]));
        assertArrayEquals(testSensors, repository.getAll().toArray(new Sensor[0]));
    }

    @Test
    public void testSetNew() {
        Sensor toChange = createSensor(8, new Measurement());

        assertTrue(repository.set(testSensors[2], toChange));
        testSensors[2] = toChange;

        assertFalse(repository.get("name2").isPresent());
        assertTrue(repository.get("name8").isPresent());
        assertArrayEquals(testSensors, repository.getAll().toArray(new Sensor[0]));
    }

    @Test
    public void testSetExisted() {
        assertFalse(repository.set(testSensors[2], testSensors[0]));
        assertTrue(repository.get("name2").isPresent());
        assertTrue(repository.get("name0").isPresent());
        assertArrayEquals(testSensors, repository.getAll().toArray(new Sensor[0]));
    }

    @Test
    public void testSetInsteadNotExisted() {
        assertFalse(repository.set(createSensor(8, new Measurement()), testSensors[0]));
        assertFalse(repository.get("name8").isPresent());
        assertTrue(repository.get("name0").isPresent());
        assertArrayEquals(testSensors, repository.getAll().toArray(new Sensor[0]));
    }

    @Test
    public void testChangeToSameMeasurementValue() {
        assertTrue(repository.changeMeasurementValue(Measurement.DEGREE_CELSIUS, Measurement.DEGREE_CELSIUS));
        assertEquals(testSensors[0].getValue(), repository.get("name0").get().getValue());
        assertEquals(testSensors[0].getMeasurement(), repository.get("name0").get().getMeasurement());
        assertEquals(testSensors[4].getValue(), repository.get("name4").get().getValue());
        assertArrayEquals(testSensors, repository.getAll().toArray(new Sensor[0]));
    }

    @Test
    public void testChangeToNewMeasurementValue() {
        testSensors[0].setValue(Measurement.PA);
        testSensors[1].setValue(Measurement.PA);
        testSensors[2].setValue(Measurement.PA);

        assertTrue(repository.changeMeasurementValue(Measurement.DEGREE_CELSIUS, Measurement.PA));
        assertEquals(testSensors[1].getValue(), repository.get("name1").get().getValue());
        assertEquals(testSensors[1].getMeasurement(), repository.get("name1").get().getMeasurement());
        assertArrayEquals(testSensors, repository.getAll().toArray(new Sensor[0]));
    }

    @Test
    public void testChangeToExistedMeasurementValue() {
        testSensors[0].setValue(Measurement.KPA);
        testSensors[1].setValue(Measurement.KPA);
        testSensors[2].setValue(Measurement.KPA);

        assertTrue(repository.changeMeasurementValue(Measurement.DEGREE_CELSIUS, Measurement.KPA));
        assertEquals(testSensors[1].getValue(), repository.get("name1").get().getValue());
        assertEquals(testSensors[1].getMeasurement(), repository.get("name1").get().getMeasurement());
        assertArrayEquals(testSensors, repository.getAll().toArray(new Sensor[0]));
    }


    @Test
    public void testChangeInsteadNotExistedMeasurementValue() {
        assertTrue(repository.changeMeasurementValue(Measurement.PA, Measurement.KPA));
        assertEquals(testSensors[1].getValue(), repository.get("name1").get().getValue());
        assertEquals(testSensors[1].getMeasurement(), repository.get("name1").get().getMeasurement());
        assertArrayEquals(testSensors, repository.getAll().toArray(new Sensor[0]));
    }

    @Test
    public void testRemoveExistedMeasurementValue(){
        testSensors[0].setValue("");
        testSensors[1].setValue("");
        testSensors[2].setValue("");

        assertTrue(repository.removeMeasurementValue(Measurement.DEGREE_CELSIUS));
        assertEquals("", repository.get("name1").get().getValue());
        assertEquals(testSensors[1].getMeasurement(), repository.get("name1").get().getMeasurement());
        assertArrayEquals(testSensors, repository.getAll().toArray(new Sensor[0]));
    }

    @Test
    public void testRemoveNotExistedMeasurementValue(){
        assertTrue(repository.removeMeasurementValue(Measurement.PA));
        assertEquals(Measurement.DEGREE_CELSIUS, repository.get("name1").get().getValue());
        assertEquals(testSensors[1].getMeasurement(), repository.get("name1").get().getMeasurement());
        assertArrayEquals(testSensors, repository.getAll().toArray(new Sensor[0]));
    }

    @Test
    public void testClear() {
        assertTrue(repository.clear());
        assertArrayEquals(new Sensor[0], repository.getAll().toArray(new Sensor[0]));
    }

    @Test
    public void testRewriteNotEmpty() {
        Sensor[] expected = new Sensor[]{
                createSensor(8, new Measurement()),
                createSensor(9, new Measurement()),
                testSensors[0]
        };
        Sensor[] toRewrite = new Sensor[]{
                createSensor(8, new Measurement()),
                null,
                createSensor(9, new Measurement()),
                testSensors[0]
        };

        assertTrue(repository.rewrite(Arrays.asList(toRewrite)));
        assertArrayEquals(expected, repository.getAll().toArray(new Sensor[0]));
    }

    @Test
    public void testRewriteEmpty() {
        assertTrue(repository.rewrite(new ArrayList<>()));
        assertArrayEquals(new Sensor[0], repository.getAll().toArray(new Sensor[0]));
    }

    @Test
    public void testIsLastInMeasurement(){
        repository.remove(testSensors[6]);

        assertFalse(repository.isLastInMeasurement(testSensors[0]));
        assertTrue(repository.isLastInMeasurement(testSensors[5]));
        assertTrue(repository.isLastInMeasurement(createSensor(8, new Measurement())));
    }

    @Test
    public void testImportDataWithNewAndChanging() {
        testSensors[2].setErrorFormula("New Error-Formula");
        testSensors[4].setNumber("New Number");
        Sensor[] expected = Arrays.copyOf(testSensors, 9);
        expected[expected.length-2] = createSensor(8, new Measurement());
        expected[expected.length-1] = createSensor(9, new Measurement());

        ArrayList<Sensor>forChange = new ArrayList<>();
        forChange.add(testSensors[2]);
        forChange.add(null);
        forChange.add(testSensors[4]);

        ArrayList<Sensor>newSen = new ArrayList<>();
        newSen.add(createSensor(8, new Measurement()));
        newSen.add(null);
        newSen.add(createSensor(9, new Measurement()));

        assertTrue(repository.importData(newSen, forChange));
        assertEquals(testSensors[2].getErrorFormula(), repository.get("name2").get().getErrorFormula());
        assertEquals(testSensors[4].getNumber(), repository.get("name4").get().getNumber());
        assertTrue(repository.get("name9").isPresent());
        assertArrayEquals(expected, repository.getAll().toArray(new Sensor[0]));
    }

    @Test
    public void testImportDataOnlyWithNew() {
        Sensor[] expected = Arrays.copyOf(testSensors, 9);
        expected[expected.length-2] = createSensor(8, new Measurement());
        expected[expected.length-1] = createSensor(9, new Measurement());

        ArrayList<Sensor>newSen = new ArrayList<>();
        newSen.add(createSensor(8, new Measurement()));
        newSen.add(createSensor(9, new Measurement()));

        assertTrue(repository.importData(newSen, new ArrayList<>()));
        assertTrue(repository.get("name9").isPresent());
        assertArrayEquals(expected, repository.getAll().toArray(new Sensor[0]));
    }

    @Test
    public void testImportDataOnlyWithChanging() {
        testSensors[2].setErrorFormula("New Error-Formula");
        testSensors[4].setNumber("New Number");

        ArrayList<Sensor>forChange = new ArrayList<>();
        forChange.add(testSensors[2]);
        forChange.add(testSensors[4]);

        assertTrue(repository.importData(new ArrayList<>(), forChange));
        assertEquals(testSensors[2].getErrorFormula(), repository.get("name2").get().getErrorFormula());
        assertEquals(testSensors[4].getNumber(), repository.get("name4").get().getNumber());
        assertArrayEquals(testSensors, repository.getAll().toArray(new Sensor[0]));
    }

    @Test
    public void testImportDataWithoutNewAndChanging() {
        assertTrue(repository.importData(new ArrayList<>(), new ArrayList<>()));
        assertArrayEquals(testSensors, repository.getAll().toArray(new Sensor[0]));
    }

    @Test
    public void testIsExists() {
        assertTrue(repository.isExists("name0"));
        assertFalse(repository.isExists("name9"));
    }
}