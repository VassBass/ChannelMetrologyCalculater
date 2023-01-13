package repository.impl;

import model.Calibrator;
import model.Measurement;
import org.junit.*;
import org.sqlite.JDBC;
import service.repository.repos.calibrator.CalibratorRepository;
import service.repository.repos.calibrator.CalibratorRepositorySQLite;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

@SuppressWarnings("OptionalGetWithoutIsPresent")
public class CalibratorRepositorySQLiteTest {

    private static final String DB_URL = "jdbc:sqlite:TestData.db";
    private static final CalibratorRepository repository = new CalibratorRepositorySQLite(DB_URL, null, null);

    private static Calibrator[] testCalibrators;

    private static Calibrator createCalibrator(int number){
        Calibrator calibrator = new Calibrator("calibrator" + number);
        calibrator.setType("type" + number);
        calibrator.setCertificateName("certificate" + number);
        calibrator.setCertificateType("certificateType" + number);
        calibrator.setCertificateDate("23.03.2022");
        calibrator.setCertificateCompany("company" + number);
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

    @BeforeClass
    public static void testCreateTable(){
        assertTrue(repository.createTable());
    }

    @AfterClass
    public static void removeTable() throws SQLException {
        String sql = "DROP TABLE calibrators;";
        DriverManager.registerDriver(new JDBC());
        try (Connection connection = DriverManager.getConnection(DB_URL);
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
        }
    }

    @Before
    public void fuelingTestDB() throws SQLException {
        testCalibrators = new Calibrator[7];
        for (int n = 0;n < 7;n++){
            testCalibrators[n] = createCalibrator(n);
        }

        String insertSql = "INSERT INTO calibrators (name, type, number, measurement, value, error_formula, certificate, range_min, range_max) "
                + "VALUES ";
        StringBuilder sql = new StringBuilder(insertSql);

        for (Calibrator calibrator : testCalibrators) {
            sql.append("('").append(calibrator.getName()).append("', ")
                    .append("'").append(calibrator.getType()).append("', ")
                    .append("'").append(calibrator.getNumber()).append("', ")
                    .append("'").append(calibrator.getMeasurement()).append("', ")
                    .append("'").append(calibrator.getValue()).append("', ")
                    .append("'").append(calibrator.getErrorFormula()).append("', ")
                    .append("'").append(calibrator.getCertificate()).append("', ")
                    .append(calibrator.getRangeMin()).append(", ")
                    .append(calibrator.getRangeMax()).append("),");
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
        String sql = "DELETE FROM calibrators;";
        DriverManager.registerDriver(new JDBC());
        try (Connection connection = DriverManager.getConnection(DB_URL);
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
        }
    }

    @Test
    public void testGetAll() {
        assertArrayEquals(testCalibrators, repository.getAll().toArray(new Calibrator[0]));
    }

    @Test
    public void testGetAllNames() {
        String[] expectedTemperature = new String[3];
        String[] expectedPressure = new String[2];
        String[] expectedConsumption = new String[2];
        for (int i=0;i< testCalibrators.length;i++){
            if (i < 3) {
                expectedTemperature[i] = testCalibrators[i].getName();
            }else if (i < 5){
                expectedPressure[i-3] = testCalibrators[i].getName();
            }else {
                expectedConsumption[i-5] = testCalibrators[i].getName();
            }
        }

        assertArrayEquals(expectedTemperature, repository.getAllNames(new Measurement(Measurement.TEMPERATURE, Measurement.DEGREE_CELSIUS)));
        assertArrayEquals(expectedPressure, repository.getAllNames(new Measurement(Measurement.PRESSURE, Measurement.KPA)));
        assertArrayEquals(expectedConsumption, repository.getAllNames(new Measurement(Measurement.CONSUMPTION, Measurement.M3_HOUR)));
    }

    @Test
    public void testGetExisted() {
        assertEquals(createCalibrator(1), repository.get("calibrator1").get());
    }

    @Test
    public void testGetNotExisted() {
        assertFalse(repository.get("Not Existed").isPresent());
    }

    @Test
    public void testAddNotExisted() {
        Calibrator[] expected = Arrays.copyOf(testCalibrators, 8);
        expected[expected.length-1] = createCalibrator(7);

        assertTrue(repository.add(createCalibrator(7)));
        assertArrayEquals(expected, repository.getAll().toArray(new Calibrator[0]));
    }

    @Test
    public void testAddExisted() {
        assertFalse(repository.add(createCalibrator(2)));
        assertArrayEquals(testCalibrators, repository.getAll().toArray(new Calibrator[0]));
    }

    @Test
    public void testRemoveExisted() {
        Calibrator[] expected = Arrays.copyOf(testCalibrators, 6);

        assertTrue(repository.remove(createCalibrator(6)));
        assertArrayEquals(expected, repository.getAll().toArray(new Calibrator[0]));
    }

    @Test
    public void testRemoveNotExisted() {
        assertFalse(repository.remove(createCalibrator(8)));
        assertArrayEquals(testCalibrators, repository.getAll().toArray(new Calibrator[0]));
    }

    @Test
    public void testRemoveByMeasurementValueExisted() {
        Calibrator[]expected = Arrays.copyOf(testCalibrators, 5);

        assertTrue(repository.removeByMeasurementValue(Measurement.M3_HOUR));
        assertArrayEquals(expected, repository.getAll().toArray(new Calibrator[0]));
    }

    @Test
    public void testRemoveByMeasurementValueNotExisted() {
        assertFalse(repository.removeByMeasurementValue("Not Existed"));
        assertArrayEquals(testCalibrators, repository.getAll().toArray(new Calibrator[0]));
    }

    @Test
    public void testSetSame() {
        assertTrue(repository.set(createCalibrator(2), createCalibrator(2)));
        assertArrayEquals(testCalibrators, repository.getAll().toArray(new Calibrator[0]));
    }

    @Test
    public void testSetNew() {
        testCalibrators[2] = createCalibrator(8);

        assertTrue(repository.set(createCalibrator(2), createCalibrator(8)));
        assertFalse(repository.get("calibrator2").isPresent());
        assertTrue(repository.get("calibrator8").isPresent());
        assertArrayEquals(testCalibrators, repository.getAll().toArray(new Calibrator[0]));
    }

    @Test
    public void testSetExisted() {
        assertFalse(repository.set(createCalibrator(2), createCalibrator(0)));
        assertTrue(repository.get("calibrator2").isPresent());
        assertTrue(repository.get("calibrator0").isPresent());
        assertArrayEquals(testCalibrators, repository.getAll().toArray(new Calibrator[0]));
    }

    @Test
    public void testSetInsteadNotExisted() {
        assertFalse(repository.set(createCalibrator(8), createCalibrator(0)));
        assertTrue(repository.get("calibrator0").isPresent());
        assertArrayEquals(testCalibrators, repository.getAll().toArray(new Calibrator[0]));
    }

    @Test
    public void testChangeToSameMeasurementValue() {
        assertTrue(repository.changeMeasurementValue(Measurement.DEGREE_CELSIUS, Measurement.DEGREE_CELSIUS));
        assertEquals(testCalibrators[0].getValue(), repository.get("calibrator0").get().getValue());
        assertEquals(testCalibrators[0].getMeasurement(), repository.get("calibrator0").get().getMeasurement());
        assertEquals(testCalibrators[4].getValue(), repository.get("calibrator4").get().getValue());
        assertArrayEquals(testCalibrators, repository.getAll().toArray(new Calibrator[0]));
    }

    @Test
    public void testChangeToNewMeasurementValue() {
        testCalibrators[0].setValue(Measurement.PA);
        testCalibrators[1].setValue(Measurement.PA);
        testCalibrators[2].setValue(Measurement.PA);

        assertTrue(repository.changeMeasurementValue(Measurement.DEGREE_CELSIUS, Measurement.PA));
        assertEquals(testCalibrators[1].getValue(), repository.get("calibrator1").get().getValue());
        assertEquals(testCalibrators[1].getMeasurement(), repository.get("calibrator1").get().getMeasurement());
        assertArrayEquals(testCalibrators, repository.getAll().toArray(new Calibrator[0]));
    }

    @Test
    public void testChangeToExistedMeasurementValue() {
        testCalibrators[0].setValue(Measurement.KPA);
        testCalibrators[1].setValue(Measurement.KPA);
        testCalibrators[2].setValue(Measurement.KPA);

        assertTrue(repository.changeMeasurementValue(Measurement.DEGREE_CELSIUS, Measurement.KPA));
        assertEquals(testCalibrators[1].getValue(), repository.get("calibrator1").get().getValue());
        assertEquals(testCalibrators[1].getMeasurement(), repository.get("calibrator1").get().getMeasurement());
        assertArrayEquals(testCalibrators, repository.getAll().toArray(new Calibrator[0]));
    }

    @Test
    public void testChangeInsteadNotExistedMeasurementValue() {
        assertFalse(repository.changeMeasurementValue(Measurement.PA, Measurement.KPA));
        assertEquals(testCalibrators[1].getValue(), repository.get("calibrator1").get().getValue());
        assertEquals(testCalibrators[1].getMeasurement(), repository.get("calibrator1").get().getMeasurement());
        assertArrayEquals(testCalibrators, repository.getAll().toArray(new Calibrator[0]));
    }

    @Test
    public void testClear() {
        assertTrue(repository.clear());
        assertArrayEquals(new Calibrator[0], repository.getAll().toArray(new Calibrator[0]));
    }

    @Test
    public void testRewriteNotEmpty() {
        Calibrator[] expected = new Calibrator[]{createCalibrator(8), createCalibrator(9), createCalibrator(0)};
        Calibrator[] toRewrite = new Calibrator[]{createCalibrator(8), null, createCalibrator(9), createCalibrator(0)};

        assertTrue(repository.rewrite(Arrays.asList(toRewrite)));
        assertArrayEquals(expected, repository.getAll().toArray(new Calibrator[0]));
    }

    @Test
    public void testRewriteEmpty() {
        assertTrue(repository.rewrite(new ArrayList<>()));
        assertArrayEquals(new Calibrator[0], repository.getAll().toArray(new Calibrator[0]));
    }

    @Test
    public void testImportDataWithNewAndChanging() {
        testCalibrators[2].setErrorFormula("New Error-Formula");
        testCalibrators[4].setNumber("New Number");
        Calibrator[] expected = Arrays.copyOf(testCalibrators, 9);
        expected[expected.length-2] = createCalibrator(8);
        expected[expected.length-1] = createCalibrator(9);

        ArrayList<Calibrator>forChange = new ArrayList<>();
        forChange.add(testCalibrators[2]);
        forChange.add(null);
        forChange.add(testCalibrators[4]);

        ArrayList<Calibrator>newCal = new ArrayList<>();
        newCal.add(createCalibrator(8));
        newCal.add(null);
        newCal.add(createCalibrator(9));

        assertTrue(repository.importData(newCal, forChange));
        assertArrayEquals(expected, repository.getAll().toArray(new Calibrator[0]));
    }

    @Test
    public void testImportDataOnlyWithNew() {
        Calibrator[] expected = Arrays.copyOf(testCalibrators, 9);
        expected[expected.length-2] = createCalibrator(8);
        expected[expected.length-1] = createCalibrator(9);

        ArrayList<Calibrator>newCal = new ArrayList<>();
        newCal.add(createCalibrator(8));
        newCal.add(createCalibrator(9));

        assertTrue(repository.importData(newCal, new ArrayList<>()));
        assertArrayEquals(expected, repository.getAll().toArray(new Calibrator[0]));
    }

    @Test
    public void testImportDataOnlyWithChanging() {
        testCalibrators[2].setErrorFormula("New Error-Formula");
        testCalibrators[4].setNumber("New Number");

        ArrayList<Calibrator>forChange = new ArrayList<>();
        forChange.add(testCalibrators[2]);
        forChange.add(testCalibrators[4]);

        assertTrue(repository.importData(new ArrayList<>(), forChange));
        assertArrayEquals(testCalibrators, repository.getAll().toArray(new Calibrator[0]));
    }

    @Test
    public void testImportDataWithoutNewAndChanging() {
        assertTrue(repository.importData(new ArrayList<>(), new ArrayList<>()));
        assertArrayEquals(testCalibrators, repository.getAll().toArray(new Calibrator[0]));
    }

    @Test
    public void testIsExists() {
        assertTrue(repository.isExists(createCalibrator(0)));
        assertFalse(repository.isExists(createCalibrator(8)));
    }
}