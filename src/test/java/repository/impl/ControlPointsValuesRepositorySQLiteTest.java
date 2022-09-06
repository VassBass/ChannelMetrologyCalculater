package repository.impl;

import model.ControlPointsValues;
import org.junit.*;
import org.sqlite.JDBC;
import repository.ControlPointsValuesRepository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.Assert.*;

@SuppressWarnings("OptionalGetWithoutIsPresent")
public class ControlPointsValuesRepositorySQLiteTest {
    private static final String DB_URL = "jdbc:sqlite:TestData.db";
    private static final ControlPointsValuesRepository repository = new ControlPointsValuesRepositorySQLite(DB_URL, null, null);

    private static ControlPointsValues[] testCpv;

    private static ControlPointsValues createCpv(int id){
        ControlPointsValues cpv = new ControlPointsValues();
        cpv.setId(id);
        cpv.setSensorType("sensorType" + id);
        cpv.setRangeMin(id);
        cpv.setRangeMax(id + 100);
        cpv.setValues(Arrays.asList(id+0D, id+5D, id+50D, id+95D, id+100D));

        return cpv;
    }

    @BeforeClass
    public static void testCreateTable(){
        assertTrue(repository.createTable());
    }

    @AfterClass
    public static void removeTable() throws SQLException {
        String sql = "DROP TABLE control_points;";
        DriverManager.registerDriver(new JDBC());
        try (Connection connection = DriverManager.getConnection(DB_URL);
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
        }
    }

    @Before
    public void fuelingTestDB() throws SQLException {
        testCpv = new ControlPointsValues[7];
        for (int n=0;n < 7;n++){
            testCpv[n] = createCpv(n);
        }

        String insertSql = "INSERT INTO control_points (id, sensor_type, range_min, range_max, points) "
                + "VALUES ";
        StringBuilder sql = new StringBuilder(insertSql);

        for (ControlPointsValues cpv : testCpv) {
            sql.append("(").append(cpv.getId()).append(", ")
                    .append("'").append(cpv.getSensorType()).append("', ")
                    .append(cpv.getRangeMin()).append(", ")
                    .append(cpv.getRangeMax()).append(", ")
                    .append("'").append(cpv._getValuesString()).append("'),");
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
        String sql = "DELETE FROM control_points;";
        String clearId = "DELETE FROM sqlite_sequence;";
        DriverManager.registerDriver(new JDBC());
        try (Connection connection = DriverManager.getConnection(DB_URL);
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
            statement.execute(clearId);
        }
    }

    @Test
    public void testGetAll(){
        assertArrayEquals(testCpv, repository.getAll().toArray(new ControlPointsValues[0]));
    }

    @Test
    public void testGetByIdExisted() {
        assertEquals(createCpv(0), repository.getById(0).get());
        assertEquals(createCpv(5), repository.getById(5).get());
    }

    @Test
    public void testGetByIdNotExisted() {
        assertFalse(repository.getById(50).isPresent());
    }

    @Test
    public void testAddNotExisted() {
        ControlPointsValues[] expected = Arrays.copyOf(testCpv, 8);
        expected[expected.length-1] = createCpv(7);

        assertTrue(repository.add(createCpv(7)));
        assertArrayEquals(expected, repository.getAll().toArray(new ControlPointsValues[0]));
    }

    @Test
    public void testAddExisted() {
        ControlPointsValues cpv2 = createCpv(2);
        cpv2.setId(7);
        ControlPointsValues[] expected = Arrays.copyOf(testCpv, 8);
        expected[expected.length - 1] = cpv2;

        assertTrue(repository.add(createCpv(2)));
        assertArrayEquals(expected, repository.getAll().toArray(new ControlPointsValues[0]));
    }

    @Test
    public void testSetNew() {
        ControlPointsValues cpv2 = createCpv(8);
        cpv2.setId(2);
        testCpv[2] = cpv2;

        assertTrue(repository.set(cpv2, null));
        assertEquals(cpv2.getSensorType(), repository.getById(2).get().getSensorType());
        assertArrayEquals(testCpv, repository.getAll().toArray(new ControlPointsValues[0]));

        assertTrue(repository.set(cpv2));
        assertEquals(cpv2.getSensorType(), repository.getById(2).get().getSensorType());
        assertArrayEquals(testCpv, repository.getAll().toArray(new ControlPointsValues[0]));
    }

    @Test
    public void testSetExisted() {
        assertTrue(repository.set(createCpv(2), null));
        assertEquals(createCpv(2).getSensorType(), repository.getById(2).get().getSensorType());
        assertArrayEquals(testCpv, repository.getAll().toArray(new ControlPointsValues[0]));

        assertTrue(repository.set(createCpv(2)));
        assertEquals(createCpv(2).getSensorType(), repository.getById(2).get().getSensorType());
        assertArrayEquals(testCpv, repository.getAll().toArray(new ControlPointsValues[0]));
    }

    @Test
    public void testAddReturnIdNotExisted() {
        ControlPointsValues[] expected = Arrays.copyOf(testCpv, 8);
        expected[expected.length-1] = createCpv(7);

        assertEquals(Optional.of(7), repository.addReturnId(createCpv(7)));
        assertArrayEquals(expected, repository.getAll().toArray(new ControlPointsValues[0]));
    }

    @Test
    public void testAddReturnIdExisted() {
        ControlPointsValues cpv2 = createCpv(2);
        cpv2.setId(7);
        ControlPointsValues[] expected = Arrays.copyOf(testCpv, 8);
        expected[expected.length - 1] = cpv2;

        assertEquals(Optional.of(7), repository.addReturnId(createCpv(2)));
        assertArrayEquals(expected, repository.getAll().toArray(new ControlPointsValues[0]));
    }

    @Test
    public void testChangeSensorTypeEquals(){
        assertTrue(repository.changeSensorType(createCpv(1).getSensorType(), createCpv(1).getSensorType()));
        assertEquals(testCpv[1].getSensorType(), repository.getById(1).get().getSensorType());
        assertArrayEquals(testCpv, repository.getAll().toArray(new ControlPointsValues[0]));
    }

    @Test
    public void testChangeSensorTypeNotEquals(){
        testCpv[1].setSensorType("type");

        assertTrue(repository.changeSensorType(createCpv(1).getSensorType(), "type"));
        assertEquals(testCpv[1].getSensorType(), repository.getById(1).get().getSensorType());
        assertArrayEquals(testCpv, repository.getAll().toArray(new ControlPointsValues[0]));
    }

    @Test
    public void testRemoveExisted() {
        ControlPointsValues[] expected = Arrays.copyOf(testCpv, 6);

        assertTrue(repository.remove(createCpv(6)));
        assertArrayEquals(expected, repository.getAll().toArray(new ControlPointsValues[0]));
    }

    @Test
    public void testRemoveNotExisted() {
        assertFalse(repository.remove(createCpv(8)));
        assertArrayEquals(testCpv, repository.getAll().toArray(new ControlPointsValues[0]));
    }

    @Test
    public void testRemoveAllExisted(){
        ControlPointsValues[] expected = Arrays.copyOf(testCpv, 6);

        assertTrue(repository.removeAll(createCpv(6).getSensorType()));
        assertArrayEquals(expected, repository.getAll().toArray(new ControlPointsValues[0]));
    }

    @Test
    public void testRemoveAllNotExisted(){
        assertTrue(repository.removeAll("type"));
        assertArrayEquals(testCpv, repository.getAll().toArray(new ControlPointsValues[0]));
    }

    @Test
    public void testClear() {
        assertTrue(repository.clear());
        assertArrayEquals(new ControlPointsValues[0], repository.getAll().toArray(new ControlPointsValues[0]));
    }

    @Test
    public void testRewriteNotEmpty() {
        ControlPointsValues[] expected = new ControlPointsValues[]{
                createCpv(7),
                createCpv(8),
                createCpv(9)
        };
        ControlPointsValues[] toRewrite = new ControlPointsValues[]{
                createCpv(7),
                createCpv(8),
                null,
                createCpv(9)
        };

        assertTrue(repository.rewrite(Arrays.asList(toRewrite)));
        assertArrayEquals(expected, repository.getAll().toArray(new ControlPointsValues[0]));
    }

    @Test
    public void testRewriteEmpty() {
        assertTrue(repository.rewrite(new ArrayList<>()));
        assertArrayEquals(new ControlPointsValues[0], repository.getAll().toArray(new ControlPointsValues[0]));
    }
}