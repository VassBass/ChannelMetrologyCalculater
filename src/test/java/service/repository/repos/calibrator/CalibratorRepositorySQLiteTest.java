package service.repository.repos.calibrator;

import model.Calibrator;
import model.Measurement;
import org.junit.*;
import service.json.JacksonJsonObjectMapper;
import service.json.JsonObjectMapper;
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

import static java.util.stream.Collectors.toList;
import static org.junit.Assert.*;

public class CalibratorRepositorySQLiteTest {
    private static final String TEST_REPOSITORY_PROPERTIES_FILE = "properties/test_repository.properties";
    private static final File TEST_DB_FILE = new File("TestData.db");
    private static final String TEST_DB_URL = "jdbc:sqlite:TestData.db";
    private static final String TABLE_NAME = "calibrators";

    private List<Calibrator> expected;
    private Calibrator createCalibrator(int number){
        Calibrator calibrator = new Calibrator("calibrator" + number);
        calibrator.setType("type" + number);
        calibrator.getCertificate().setName("certificate" + number);
        calibrator.getCertificate().setType("certificateType" + number);
        calibrator.getCertificate().setDate("23.03.2022");
        calibrator.getCertificate().setCompany("company" + number);
        calibrator.setNumber(String.valueOf(number));
        String measurement = number < 3 ? Measurement.TEMPERATURE : number < 5 ? Measurement.PRESSURE : Measurement.CONSUMPTION;
        String value = number < 3 ? Measurement.DEGREE_CELSIUS : number < 5 ? Measurement.KPA : Measurement.M3_HOUR;
        calibrator.setMeasurement(measurement);
        calibrator.setValue(value);
        calibrator.setRangeMin(0D);
        calibrator.setRangeMax(100D);
        calibrator.setErrorFormula(number + "+" + number);
        return calibrator;
    }

    private final JsonObjectMapper jsonObjectMapper = JacksonJsonObjectMapper.getInstance();
    private CalibratorRepository repository;

    @BeforeClass
    public static void createDB() throws IOException, SQLException {
        Files.createFile(TEST_DB_FILE.toPath());
        String sql = String.format("CREATE TABLE IF NOT EXISTS %s ("
                + "name text NOT NULL UNIQUE"
                + ", type text NOT NULL"
                + ", number text NOT NULL"
                + ", measurement text NOT NULL"
                + ", value text NOT NULL"
                + ", error_formula text NOT NULL"
                + ", certificate text NOT NULL"
                + ", range_min real NOT NULL"
                + ", range_max real NOT NULL"
                + ", PRIMARY KEY (\"name\")"
                + ");", TABLE_NAME);
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
        for (int n = 0;n < 7;n++) expected.add(createCalibrator(n));

        String insertSql = "INSERT INTO calibrators (name, type, number, measurement, value, error_formula, certificate, range_min, range_max)"
                + " VALUES ";
        StringBuilder sql = new StringBuilder(insertSql);

        for (Calibrator calibrator : expected) {
            String values = String.format("('%s', '%s', '%s', '%s', '%s', '%s', '%s', %s, %s),",
                    calibrator.getName(),
                    calibrator.getType(),
                    calibrator.getNumber(),
                    calibrator.getMeasurement(),
                    calibrator.getValue(),
                    calibrator.getErrorFormula(),
                    jsonObjectMapper.objectToJson(calibrator.getCertificate()),
                    calibrator.getRangeMin(),
                    calibrator.getRangeMax());
            sql.append(values);
        }
        sql.setCharAt(sql.length()-1, ';');

        try (Connection connection = DriverManager.getConnection(TEST_DB_URL);
             Statement statement = connection.createStatement()) {
            statement.execute(sql.toString());
        }

        RepositoryConfigHolder configHolder = new SqliteRepositoryConfigHolder(TEST_REPOSITORY_PROPERTIES_FILE);
        RepositoryDBConnector connector = new SqliteRepositoryDBConnector(configHolder);
        repository = new CalibratorRepositorySQLite(configHolder, connector);
    }

    @After
    public void clearTestDB() throws Exception {
        String sql = "DELETE FROM calibrators;";
        try (Connection connection = DriverManager.getConnection(TEST_DB_URL);
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
        }
    }

    @Test
    public void testGetAll() {
        List<Calibrator> actual = new ArrayList<>(repository.getAll());
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testGetAllNames() {
        List<String> expectedTemperature = new ArrayList<>(3);
        List<String> expectedPressure = new ArrayList<>(2);
        List<String> expectedConsumption = new ArrayList<>(2);
        for (int i=0;i< expected.size();i++){
            if (i < 3) {
                expectedTemperature.add(expected.get(i).getName());
            }else if (i < 5){
                expectedPressure.add(expected.get(i).getName());
            }else {
                expectedConsumption.add(expected.get(i).getName());
            }
        }
        Measurement temperature = new Measurement(Measurement.TEMPERATURE, Measurement.DEGREE_CELSIUS);
        Measurement pressure = new Measurement(Measurement.PRESSURE, Measurement.KPA);
        Measurement consumption = new Measurement(Measurement.CONSUMPTION, Measurement.M3_HOUR);

        List<String> actualTemperature = Arrays.asList(repository.getAllNames(temperature));
        List<String> actualPressure = Arrays.asList(repository.getAllNames(pressure));
        List<String> actualConsumption = Arrays.asList(repository.getAllNames(consumption));

        assertEquals(expectedTemperature.size(), actualTemperature.size());
        assertEquals(expectedPressure.size(), actualPressure.size());
        assertEquals(expectedConsumption.size(), actualConsumption.size());
        assertTrue(expectedTemperature.containsAll(actualTemperature));
        assertTrue(expectedPressure.containsAll(actualPressure));
        assertTrue(expectedConsumption.containsAll(actualConsumption));
    }

    @Test
    public void testGetExisted() {
        Calibrator calibrator1 = createCalibrator(1);
        assertEquals(calibrator1, repository.get(calibrator1.getName()));
    }

    @Test
    public void testGetNotExisted() {
        assertNull(repository.get("Not Existed"));
    }

    @Test
    public void testAddNotExisted() {
        Calibrator calibrator7 = createCalibrator(7);
        expected.add(calibrator7);

        assertTrue(repository.add(createCalibrator(7)));
        assertEquals(calibrator7, repository.get(calibrator7.getName()));

        List<Calibrator> actual = new ArrayList<>(repository.getAll());
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testAddExisted() {
        Calibrator calibrator2 = createCalibrator(2);

        assertFalse(repository.add(calibrator2));
        assertEquals(calibrator2, repository.get(calibrator2.getName()));

        List<Calibrator> actual = new ArrayList<>(repository.getAll());
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testRemoveExisted() {
        Calibrator calibrator2 = createCalibrator(2);
        expected.remove(2);

        assertTrue(repository.remove(calibrator2));
        assertNull(repository.get(calibrator2.getName()));

        List<Calibrator> actual = new ArrayList<>(repository.getAll());
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testRemoveNotExisted() {
        Calibrator calibrator8 = createCalibrator(8);

        assertFalse(repository.remove(calibrator8));
        assertNull(repository.get(calibrator8.getName()));

        List<Calibrator> actual = new ArrayList<>(repository.getAll());
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testRemoveByMeasurementValueExisted() {
        Calibrator calibrator0 = createCalibrator(0);
        Calibrator calibrator1 = createCalibrator(1);
        Calibrator calibrator2 = createCalibrator(2);
        expected.removeIf(c -> c.getValue().equals(Measurement.DEGREE_CELSIUS));

        assertTrue(repository.removeByMeasurementValue(Measurement.DEGREE_CELSIUS));
        assertNull(repository.get(calibrator0.getName()));
        assertNull(repository.get(calibrator1.getName()));
        assertNull(repository.get(calibrator2.getName()));

        List<Calibrator> actual = new ArrayList<>(repository.getAll());
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testRemoveByMeasurementValueNotExisted() {
        assertFalse(repository.removeByMeasurementValue("Not Existed"));
        List<Calibrator> actual = new ArrayList<>(repository.getAll());
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testSetSame() {
        Calibrator calibrator2 = createCalibrator(2);

        assertTrue(repository.set(calibrator2, calibrator2));
        assertEquals(calibrator2, repository.get(calibrator2.getName()));
        assertEquals(1, repository.getAll().stream()
                .filter(c -> c.equals(calibrator2)).count());

        List<Calibrator> actual = new ArrayList<>(repository.getAll());
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testSetNew() {
        Calibrator calibrator2 = createCalibrator(2);
        Calibrator calibrator8 = createCalibrator(8);
        expected.set(2, calibrator8);

        assertTrue(repository.set(calibrator2, calibrator8));
        assertNull(repository.get(calibrator2.getName()));
        assertEquals(calibrator8, repository.get(calibrator8.getName()));

        List<Calibrator> actual = new ArrayList<>(repository.getAll());
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testSetExisted() {
        Calibrator calibrator0 = createCalibrator(0);
        Calibrator calibrator2 = createCalibrator(2);

        assertFalse(repository.set(calibrator2, calibrator0));
        assertEquals(calibrator0, repository.get(calibrator0.getName()));
        assertEquals(calibrator2, repository.get(calibrator2.getName()));
        assertEquals(1, repository.getAll().stream()
                .filter(c -> c.equals(calibrator0)).count());
        assertEquals(1, repository.getAll().stream()
                .filter(c -> c.equals(calibrator2)).count());

        List<Calibrator> actual = new ArrayList<>(repository.getAll());
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testSetInsteadNotExisted() {
        Calibrator calibrator2 = createCalibrator(2);
        Calibrator calibrator8 = createCalibrator(8);

        assertFalse(repository.set(calibrator8, calibrator2));
        assertEquals(calibrator2, repository.get(calibrator2.getName()));
        assertNull(repository.get(calibrator8.getName()));
        assertEquals(1, repository.getAll().stream()
                .filter(c -> c.equals(calibrator2)).count());

        List<Calibrator> actual = new ArrayList<>(repository.getAll());
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testChangeToSameMeasurementValue() {
        Calibrator calibrator0 = createCalibrator(0);
        Calibrator calibrator1 = createCalibrator(1);
        Calibrator calibrator2 = createCalibrator(2);
        String degreeCelsius = Measurement.DEGREE_CELSIUS;

        assertTrue(repository.changeMeasurementValue(degreeCelsius, degreeCelsius));
        assertEquals(3, repository.getAll().stream()
                .filter(c -> c.getValue().equals(degreeCelsius)).count());
        assertEquals(degreeCelsius, repository.get(calibrator0.getName()).getValue());
        assertEquals(degreeCelsius, repository.get(calibrator1.getName()).getValue());
        assertEquals(degreeCelsius, repository.get(calibrator2.getName()).getValue());

        List<Calibrator> actual = new ArrayList<>(repository.getAll());
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testChangeToNewMeasurementValue() {
        expected = expected.stream()
                .peek(c -> {
                    if (c.getValue().equals(Measurement.DEGREE_CELSIUS)) {
                        c.setValue(Measurement.PA);
                    }
                }).collect(toList());

        assertTrue(repository.changeMeasurementValue(Measurement.DEGREE_CELSIUS, Measurement.PA));
        assertEquals(3, repository.getAll().stream()
                .filter(c -> c.getValue().equals(Measurement.PA)).count());
        assertEquals(Measurement.PA, repository.get("calibrator0").getValue());
        assertEquals(Measurement.PA, repository.get("calibrator1").getValue());
        assertEquals(Measurement.PA, repository.get("calibrator2").getValue());

        List<Calibrator> actual = new ArrayList<>(repository.getAll());
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testChangeToExistedMeasurementValue() {
        expected = expected.stream()
                .peek(c -> {
                    if (c.getValue().equals(Measurement.DEGREE_CELSIUS)) {
                        c.setValue(Measurement.KPA);
                    }
                }).collect(toList());

        assertTrue(repository.changeMeasurementValue(Measurement.DEGREE_CELSIUS, Measurement.KPA));
        assertEquals(5, repository.getAll().stream()
                .filter(c -> c.getValue().equals(Measurement.KPA)).count());
        assertEquals(Measurement.KPA, repository.get("calibrator0").getValue());
        assertEquals(Measurement.KPA, repository.get("calibrator1").getValue());
        assertEquals(Measurement.KPA, repository.get("calibrator2").getValue());
        assertEquals(Measurement.KPA, repository.get("calibrator3").getValue());
        assertEquals(Measurement.KPA, repository.get("calibrator4").getValue());

        List<Calibrator> actual = new ArrayList<>(repository.getAll());
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testChangeInsteadNotExistedMeasurementValue() {
        assertFalse(repository.changeMeasurementValue(Measurement.PA, Measurement.KPA));

        assertFalse(repository.getAll().stream().anyMatch(c -> c.getValue().equals(Measurement.PA)));

        assertEquals(3, repository.getAll().stream()
                .filter(c -> c.getValue().equals(Measurement.DEGREE_CELSIUS)).count());
        assertEquals(2, repository.getAll().stream()
                .filter(c -> c.getValue().equals(Measurement.KPA)).count());
        assertEquals(2, repository.getAll().stream()
                .filter(c -> c.getValue().equals(Measurement.M3_HOUR)).count());
        assertEquals(Measurement.DEGREE_CELSIUS, repository.get("calibrator0").getValue());
        assertEquals(Measurement.DEGREE_CELSIUS, repository.get("calibrator1").getValue());
        assertEquals(Measurement.DEGREE_CELSIUS, repository.get("calibrator2").getValue());
        assertEquals(Measurement.KPA, repository.get("calibrator3").getValue());
        assertEquals(Measurement.KPA, repository.get("calibrator4").getValue());
        assertEquals(Measurement.M3_HOUR, repository.get("calibrator5").getValue());
        assertEquals(Measurement.M3_HOUR, repository.get("calibrator6").getValue());

        List<Calibrator> actual = new ArrayList<>(repository.getAll());
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testClear() {
        assertTrue(repository.clear());
        assertEquals(0, repository.getAll().size());
    }

    @Test
    public void testRewriteNotEmpty() {
        Calibrator calibrator8 = createCalibrator(8);
        Calibrator calibrator9 = createCalibrator(9);
        Calibrator calibrator0 = createCalibrator(0);
        expected = Arrays.asList(calibrator8, calibrator9, calibrator0);
        List<Calibrator> toRewrite = Arrays.asList(calibrator8, null, calibrator9, calibrator0);

        assertTrue(repository.rewrite(toRewrite));
        List<Calibrator> actual = new ArrayList<>(repository.getAll());
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testRewriteEmpty() {
        assertTrue(repository.rewrite(new ArrayList<>()));
        assertEquals(0, repository.getAll().size());
    }

    @Test
    public void testImportDataWithNewAndChanges() {
        String newErrorFormula = "New Error-Formula";
        String newNumber = "New Number";
        Calibrator calibrator8 = createCalibrator(8);
        Calibrator calibrator9 = createCalibrator(9);
        Calibrator calibrator2 = expected.get(2);
        Calibrator calibrator4 = expected.get(4);

        calibrator2.setErrorFormula(newErrorFormula);
        calibrator4.setNumber(newNumber);
        expected.add(calibrator8);
        expected.add(calibrator9);

        List<Calibrator>forChange = Arrays.asList(calibrator2, null, calibrator4);
        List<Calibrator>newCal = Arrays.asList(calibrator8, null, calibrator9);

        assertTrue(repository.importData(newCal, forChange));
        assertEquals(calibrator2, repository.get(calibrator2.getName()));
        assertEquals(calibrator4, repository.get(calibrator4.getName()));
        assertEquals(calibrator8, repository.get(calibrator8.getName()));
        assertEquals(calibrator9, repository.get(calibrator9.getName()));
        assertEquals(calibrator2.getErrorFormula(), repository.get(calibrator2.getName()).getErrorFormula());
        assertEquals(calibrator4.getNumber(), repository.get(calibrator4.getName()).getNumber());
        assertFalse(repository.getAll().stream().anyMatch(Objects::isNull));

        List<Calibrator> actual = new ArrayList<>(repository.getAll());
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testImportDataWithNew() {
        Calibrator calibrator8 = createCalibrator(8);
        Calibrator calibrator9 = createCalibrator(9);

        expected.add(calibrator8);
        expected.add(calibrator9);

        List<Calibrator>newCal = Arrays.asList(calibrator8, null, calibrator9);

        assertTrue(repository.importData(newCal, new ArrayList<>()));
        assertEquals(calibrator8, repository.get(calibrator8.getName()));
        assertEquals(calibrator9, repository.get(calibrator9.getName()));
        assertFalse(repository.getAll().stream().anyMatch(Objects::isNull));

        List<Calibrator> actual = new ArrayList<>(repository.getAll());
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testImportDataWithChanges() {
        String newErrorFormula = "New Error-Formula";
        String newNumber = "New Number";
        Calibrator calibrator2 = expected.get(2);
        Calibrator calibrator4 = expected.get(4);

        calibrator2.setErrorFormula(newErrorFormula);
        calibrator4.setNumber(newNumber);

        List<Calibrator>forChange = Arrays.asList(calibrator2, null, calibrator4);

        assertTrue(repository.importData(new ArrayList<>(), forChange));
        assertEquals(calibrator2, repository.get(calibrator2.getName()));
        assertEquals(calibrator4, repository.get(calibrator4.getName()));
        assertEquals(calibrator2.getErrorFormula(), repository.get(calibrator2.getName()).getErrorFormula());
        assertEquals(calibrator4.getNumber(), repository.get(calibrator4.getName()).getNumber());
        assertFalse(repository.getAll().stream().anyMatch(Objects::isNull));

        List<Calibrator> actual = new ArrayList<>(repository.getAll());
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testImportDataWithoutNewAndChanging() {
        assertTrue(repository.importData(new ArrayList<>(), new ArrayList<>()));
        List<Calibrator> actual = new ArrayList<>(repository.getAll());
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testIsExists() {
        assertTrue(repository.isExists(createCalibrator(0)));
        assertFalse(repository.isExists(createCalibrator(8)));
    }
}