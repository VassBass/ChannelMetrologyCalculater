package repository.impl;

import org.junit.*;
import org.sqlite.JDBC;
import repository.Repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class AreaRepositorySQLiteTest {

    private static final String DB_URL = "jdbc:sqlite:TestData.db";
    private static final Repository<String>repository = new AreaRepositorySQLite(DB_URL, null, null);

    private static final String AREA_1 = "area1";
    private static final String AREA_2 = "area2";
    private static final String AREA_3 = "area3";
    private static final String AREA_4 = "area4";
    private static final String AREA_5 = "area5";
    private static final String AREA_6 = "area6";
    private static final String AREA_7 = "area7";

    @BeforeClass
    public static void testCreateTable(){
        assertTrue(repository.createTable());
    }

    @AfterClass
    public static void removeTable() throws SQLException {
        String sql = "DROP TABLE areas;";
        DriverManager.registerDriver(new JDBC());
        try (Connection connection = DriverManager.getConnection(DB_URL);
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
        }
    }

    @Before
    public void fuelingTestDB() throws SQLException {
        String sql = "INSERT INTO areas VALUES "
                + "('" + AREA_1 + "')"
                + ", ('" + AREA_2 + "')"
                + ", ('" + AREA_3 + "')"
                + ", ('" + AREA_4 + "')"
                + ", ('" + AREA_5 + "')"
                + ";";

        DriverManager.registerDriver(new JDBC());
        try (Connection connection = DriverManager.getConnection(DB_URL);
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
        }
    }

    @After
    public void clearTestDB() throws Exception {
        String sql = "DELETE FROM areas;";
        DriverManager.registerDriver(new JDBC());
        try (Connection connection = DriverManager.getConnection(DB_URL);
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
        }
    }

    @Test
    public void testGetAll() {
        List<String>expected = Arrays.asList(
                AREA_1,
                AREA_2,
                AREA_3,
                AREA_4,
                AREA_5
        );

        assertArrayEquals(expected.toArray(new String[0]), repository.getAll().toArray(new String[0]));
    }

    @Test
    public void testAddNewArea() {
        List<String>expected = Arrays.asList(
                AREA_1,
                AREA_2,
                AREA_3,
                AREA_4,
                AREA_5,
                AREA_6
        );

        assertTrue(repository.add(AREA_6));
        assertArrayEquals(expected.toArray(new String[0]), repository.getAll().toArray(new String[0]));
    }

    @Test
    public void testAddExistingArea(){
        List<String>expected = Arrays.asList(
                AREA_1,
                AREA_2,
                AREA_3,
                AREA_4,
                AREA_5
        );

        assertFalse(repository.add(AREA_1));
        assertArrayEquals(expected.toArray(new String[0]), repository.getAll().toArray(new String[0]));
    }

    @Test
    public void testSet_replaceExistingAreaWithExistingArea() {
        List<String>expected = Arrays.asList(
                AREA_1,
                AREA_2,
                AREA_3,
                AREA_4,
                AREA_5
        );

        assertFalse(repository.set(AREA_2, AREA_5));
        assertArrayEquals(expected.toArray(new String[0]), repository.getAll().toArray(new String[0]));
    }

    @Test
    public void testSet_replaceExistingAreaWithTheSameArea() {
        List<String>expected = Arrays.asList(
                AREA_1,
                AREA_2,
                AREA_3,
                AREA_4,
                AREA_5
        );

        assertTrue(repository.set(AREA_2, AREA_2));
        assertArrayEquals(expected.toArray(new String[0]), repository.getAll().toArray(new String[0]));
    }

    @Test
    public void testSet_replaceExistingAreaWithNewArea() {
        List<String>expected = Arrays.asList(
                AREA_1,
                AREA_6,
                AREA_3,
                AREA_4,
                AREA_5
        );

        assertTrue(repository.set(AREA_2, AREA_6));
        assertArrayEquals(expected.toArray(new String[0]), repository.getAll().toArray(new String[0]));
    }

    @Test
    public void testSet_replaceNotExistingAreaWithExistingArea() {
        List<String>expected = Arrays.asList(
                AREA_1,
                AREA_2,
                AREA_3,
                AREA_4,
                AREA_5
        );

        assertFalse(repository.set(AREA_6, AREA_5));
        assertArrayEquals(expected.toArray(new String[0]), repository.getAll().toArray(new String[0]));
    }

    @Test
    public void testSet_replaceNotExistingAreaWithTheSameArea() {
        List<String>expected = Arrays.asList(
                AREA_1,
                AREA_2,
                AREA_3,
                AREA_4,
                AREA_5
        );

        assertFalse(repository.set(AREA_6, AREA_6));
        assertArrayEquals(expected.toArray(new String[0]), repository.getAll().toArray(new String[0]));
    }

    @Test
    public void testSet_replaceNotExistingAreaWithNewArea() {
        List<String>expected = Arrays.asList(
                AREA_1,
                AREA_2,
                AREA_3,
                AREA_4,
                AREA_5
        );

        assertFalse(repository.set(AREA_6, AREA_7));
        assertArrayEquals(expected.toArray(new String[0]), repository.getAll().toArray(new String[0]));
    }

    @Test
    public void testRemoveExistingArea() {
        List<String>expected = Arrays.asList(
                AREA_1,
                AREA_3,
                AREA_4,
                AREA_5
        );

        assertTrue(repository.remove(AREA_2));
        assertArrayEquals(expected.toArray(new String[0]), repository.getAll().toArray(new String[0]));
    }

    @Test
    public void testRemoveNotExistingArea() {
        List<String>expected = Arrays.asList(
                AREA_1,
                AREA_2,
                AREA_3,
                AREA_4,
                AREA_5
        );

        assertFalse(repository.remove(AREA_6));
        assertArrayEquals(expected.toArray(new String[0]), repository.getAll().toArray(new String[0]));
    }

    @Test
    public void testClear() {
        assertTrue(repository.clear());
        assertArrayEquals(new String[0], repository.getAll().toArray(new String[0]));
    }

    @Test
    public void testRewriteNewList() {
        List<String>expected = Arrays.asList(
                AREA_6,
                AREA_7
        );
        List<String> toRewrite = Arrays.asList(
                AREA_6,
                null,
                AREA_7
        );

        assertTrue(repository.rewrite(toRewrite));
        assertArrayEquals(expected.toArray(new String[0]), repository.getAll().toArray(new String[0]));
    }

    @Test
    public void testRewriteEmptyList() {
        List<String>expected = new ArrayList<>();

        assertTrue(repository.rewrite(expected));
        assertArrayEquals(expected.toArray(new String[0]), repository.getAll().toArray(new String[0]));
    }
}