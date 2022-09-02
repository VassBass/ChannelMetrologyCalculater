package repository.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import model.Measurement;
import org.junit.*;
import org.sqlite.JDBC;
import repository.MeasurementRepository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class MeasurementRepositorySQLiteTest {

    private static final String DB_URL = "jdbc:sqlite:TestData.db";
    private static final MeasurementRepository repository = new MeasurementRepositorySQLite(DB_URL, null, null);

    private static Measurement[] testMeasurements;

    private static final Measurement degreeCelsius = new Measurement(Measurement.TEMPERATURE, Measurement.DEGREE_CELSIUS);
    private static final Measurement kPa = new Measurement(Measurement.PRESSURE, Measurement.KPA);
    private static final Measurement pa = new Measurement(Measurement.PRESSURE, Measurement.PA);
    private static final Measurement m3H = new Measurement(Measurement.CONSUMPTION, Measurement.M3_HOUR);
    private static final Measurement cmS = new Measurement(Measurement.CONSUMPTION, Measurement.CM_S);

    @BeforeClass
    public static void testCreateTableAndPutFactors(){
        assertTrue(repository.createTable());

        Map<String, Double>factorsKPa = new HashMap<>();
        factorsKPa.put(Measurement.PA, 1000D);
        kPa.setFactors(factorsKPa);

        Map<String, Double>factorsPa = new HashMap<>();
        factorsPa.put(Measurement.KPA, 0.001);
        pa.setFactors(factorsPa);
    }

    @AfterClass
    public static void removeTable() throws SQLException {
        String sql = "DROP TABLE measurements;";
        DriverManager.registerDriver(new JDBC());
        try (Connection connection = DriverManager.getConnection(DB_URL);
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
        }
    }

    @Before
    public void fuelingTestDB() throws SQLException, JsonProcessingException {
        testMeasurements = new Measurement[]{degreeCelsius, kPa, pa, m3H};

        String insertSql = "INSERT INTO measurements (name, value, factors) "
                + "VALUES ";
        StringBuilder sql = new StringBuilder(insertSql);

        for (Measurement measurement : testMeasurements) {
            sql.append("('").append(measurement.getName()).append("',")
                    .append("'").append(measurement.getValue()).append("',")
                    .append("'").append(measurement._getFactorsJson()).append("'),");
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
        String sql = "DELETE FROM measurements;";
        DriverManager.registerDriver(new JDBC());
        try (Connection connection = DriverManager.getConnection(DB_URL);
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
        }
    }

    @Test
    public void testGetAll() {
        assertArrayEquals(testMeasurements, repository.getAll().toArray(new Measurement[0]));
    }

    @Test
    public void testGetAllNames() {
        String[]expected = Arrays.stream(testMeasurements).map(Measurement::getName).distinct().toArray(String[]::new);

        assertArrayEquals(expected, repository.getAllNames());
    }

    @Test
    public void testGetAllValues(){
        String[]expected = Arrays.stream(testMeasurements).map(Measurement::getValue).sorted().toArray(String[]::new);

        assertArrayEquals(expected, repository.getAllValues());
    }

    @Test
    public void testGetValues() {
        String[]expected = Arrays.stream(testMeasurements)
                .filter(m -> m.getName().equals(Measurement.PRESSURE))
                .map(Measurement::getValue)
                .toArray(String[]::new);

        assertArrayEquals(expected, repository.getValues(kPa));
        assertArrayEquals(expected, repository.getValues(Measurement.PRESSURE));
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Test
    public void testGetExisted() {
        assertEquals(degreeCelsius, repository.get(degreeCelsius.getValue()).get());
        assertEquals(kPa, repository.get(kPa.getValue()).get());
    }

    @Test
    public void testGetNotExisted() {
        assertFalse(repository.get("Not Existed").isPresent());
    }

    @Test
    public void testAddNotExisted() {
        Measurement[] expected = Arrays.copyOf(testMeasurements, 5);
        expected[expected.length-1] = cmS;

        assertTrue(repository.add(cmS));
        assertArrayEquals(expected, repository.getAll().toArray(new Measurement[0]));
    }

    @Test
    public void testAddExisted() {
        assertFalse(repository.add(kPa));
        assertArrayEquals(testMeasurements, repository.getAll().toArray(new Measurement[0]));
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Test
    public void testChangeFactors() throws JsonProcessingException {
        Map<String, Double> newFactors = new HashMap<>();
        newFactors.put(Measurement.KPA, 10D);
        kPa.setFactors(newFactors);

        Map<String, Double>notExpected = new HashMap<>();
        notExpected.put(Measurement.KPA, 10D);
        notExpected.put(null, 50D);
        notExpected.put(Measurement.BAR, null);

        assertTrue(repository.changeFactors(kPa.getValue(), notExpected));
        assertEquals(kPa._getFactorsJson(), repository.get(kPa.getValue()).get()._getFactorsJson());
        assertArrayEquals(testMeasurements, repository.getAll().toArray(new Measurement[0]));
    }

    @Test
    public void testRemoveExisted() {
        Measurement[] expected = Arrays.copyOf(testMeasurements, 3);

        assertTrue(repository.remove(m3H));
        assertArrayEquals(expected, repository.getAll().toArray(new Measurement[0]));
    }

    @Test
    public void testRemoveNotExisted() {
        assertFalse(repository.remove(cmS));
        assertArrayEquals(testMeasurements, repository.getAll().toArray(new Measurement[0]));
    }

    @Test
    public void testSetSame() {
        assertTrue(repository.set(kPa, kPa));
        assertArrayEquals(testMeasurements, repository.getAll().toArray(new Measurement[0]));
    }

    @Test
    public void testSetNew() {
        testMeasurements[2] = cmS;

        assertTrue(repository.set(pa, cmS));
        assertArrayEquals(testMeasurements, repository.getAll().toArray(new Measurement[0]));
    }

    @Test
    public void testSetExisted() {
        assertFalse(repository.set(testMeasurements[2], degreeCelsius));
        assertArrayEquals(testMeasurements, repository.getAll().toArray(new Measurement[0]));
    }

    @Test
    public void testSetInsteadNotExisted() {
        assertFalse(repository.set(cmS, degreeCelsius));
        assertArrayEquals(testMeasurements, repository.getAll().toArray(new Measurement[0]));
    }

    @Test
    public void testClear() {
        assertTrue(repository.clear());
        assertArrayEquals(new Measurement[0], repository.getAll().toArray(new Measurement[0]));
    }

    @Test
    public void testGetMeasurements() {
        Measurement[] expected = Arrays.stream(testMeasurements)
                .filter(m -> m.getName().equals(Measurement.PRESSURE))
                .toArray(Measurement[]::new);

        assertArrayEquals(expected, repository.getMeasurements(Measurement.PRESSURE).toArray(new Measurement[0]));
    }

    @Test
    public void testRewriteNotEmpty() {
        Measurement[] expected = new Measurement[]{cmS, kPa, degreeCelsius};
        Measurement[] toRewrite = new Measurement[]{cmS, null, kPa, degreeCelsius};

        assertTrue(repository.rewrite(Arrays.asList(toRewrite)));
        assertArrayEquals(expected, repository.getAll().toArray(new Measurement[0]));
    }

    @Test
    public void testRewriteEmpty() {
        assertTrue(repository.rewrite(new ArrayList<>()));
        assertArrayEquals(new Measurement[0], repository.getAll().toArray(new Measurement[0]));
    }

    @Test
    public void testIsLastInMeasurement() {
        assertTrue(repository.isLastInMeasurement(degreeCelsius.getValue()));
        assertFalse(repository.isLastInMeasurement(kPa.getValue()));
    }

    @Test
    public void testExists() {
        assertTrue(repository.exists(kPa.getValue()));
        assertFalse(repository.exists(cmS.getValue()));
    }

    @Test
    public void testExistsEquals() {
        assertFalse(repository.exists(kPa.getValue(), kPa.getValue()));
    }

    @Test
    public void testExistsNew() {
        assertFalse(repository.exists(kPa.getValue(), cmS.getValue()));
    }

    @Test
    public void testExistsExist() {
        assertTrue(repository.exists(kPa.getValue(), degreeCelsius.getValue()));
    }

    @Test
    public void testExistsInsteadNotExist() {
        assertTrue(repository.exists(cmS.getValue(), kPa.getValue()));
    }
}