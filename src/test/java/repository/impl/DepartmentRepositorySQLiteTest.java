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

public class DepartmentRepositorySQLiteTest {

    private static final String DB_URL = "jdbc:sqlite:TestData.db";
    private static final Repository<String> repository = new DepartmentRepositorySQLite(DB_URL, null, null);

    private static final String DEPARTMENT_1 = "department1";
    private static final String DEPARTMENT_2 = "department2";
    private static final String DEPARTMENT_3 = "department3";
    private static final String DEPARTMENT_4 = "department4";
    private static final String DEPARTMENT_5 = "department5";
    private static final String DEPARTMENT_6 = "department6";
    private static final String DEPARTMENT_7 = "department7";

    @BeforeClass
    public static void testCreateTable(){
        assertTrue(repository.createTable());
    }

    @AfterClass
    public static void removeTable() throws SQLException {
        String sql = "DROP TABLE departments;";
        DriverManager.registerDriver(new JDBC());
        try (Connection connection = DriverManager.getConnection(DB_URL);
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
        }
    }

    @Before
    public void fuelingTestDB() throws SQLException {
        String sql = "INSERT INTO departments VALUES "
                + "('" + DEPARTMENT_1 + "')"
                + ", ('" + DEPARTMENT_2 + "')"
                + ", ('" + DEPARTMENT_3 + "')"
                + ", ('" + DEPARTMENT_4 + "')"
                + ", ('" + DEPARTMENT_5 + "')"
                + ";";

        DriverManager.registerDriver(new JDBC());
        try (Connection connection = DriverManager.getConnection(DB_URL);
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
        }
    }

    @After
    public void clearTestDB() throws Exception {
        String sql = "DELETE FROM departments;";
        DriverManager.registerDriver(new JDBC());
        try (Connection connection = DriverManager.getConnection(DB_URL);
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
        }
    }

    @Test
    public void testGetAll() {
        List<String>expected = Arrays.asList(
                DEPARTMENT_1,
                DEPARTMENT_2,
                DEPARTMENT_3,
                DEPARTMENT_4,
                DEPARTMENT_5
        );

        assertArrayEquals(expected.toArray(new String[0]), repository.getAll().toArray(new String[0]));
    }

    @Test
    public void testAddNewDepartment() {
        List<String>expected = Arrays.asList(
                DEPARTMENT_1,
                DEPARTMENT_2,
                DEPARTMENT_3,
                DEPARTMENT_4,
                DEPARTMENT_5,
                DEPARTMENT_6
        );

        assertTrue(repository.add(DEPARTMENT_6));
        assertArrayEquals(expected.toArray(new String[0]), repository.getAll().toArray(new String[0]));
    }

    @Test
    public void testAddExistingDepartment(){
        List<String>expected = Arrays.asList(
                DEPARTMENT_1,
                DEPARTMENT_2,
                DEPARTMENT_3,
                DEPARTMENT_4,
                DEPARTMENT_5
        );

        assertFalse(repository.add(DEPARTMENT_1));
        assertArrayEquals(expected.toArray(new String[0]), repository.getAll().toArray(new String[0]));
    }

    @Test
    public void testSet_replaceExistingDepartmentWithExistingDepartment() {
        List<String>expected = Arrays.asList(
                DEPARTMENT_1,
                DEPARTMENT_2,
                DEPARTMENT_3,
                DEPARTMENT_4,
                DEPARTMENT_5
        );

        assertFalse(repository.set(DEPARTMENT_2, DEPARTMENT_5));
        assertArrayEquals(expected.toArray(new String[0]), repository.getAll().toArray(new String[0]));
    }

    @Test
    public void testSet_replaceExistingDepartmentWithTheSameDepartment() {
        List<String>expected = Arrays.asList(
                DEPARTMENT_1,
                DEPARTMENT_2,
                DEPARTMENT_3,
                DEPARTMENT_4,
                DEPARTMENT_5
        );

        assertTrue(repository.set(DEPARTMENT_2, DEPARTMENT_2));
        assertArrayEquals(expected.toArray(new String[0]), repository.getAll().toArray(new String[0]));
    }

    @Test
    public void testSet_replaceExistingDepartmentWithNewDepartment() {
        List<String>expected = Arrays.asList(
                DEPARTMENT_1,
                DEPARTMENT_6,
                DEPARTMENT_3,
                DEPARTMENT_4,
                DEPARTMENT_5
        );

        assertTrue(repository.set(DEPARTMENT_2, DEPARTMENT_6));
        assertArrayEquals(expected.toArray(new String[0]), repository.getAll().toArray(new String[0]));
    }

    @Test
    public void testSet_replaceNotExistingDepartmentWithExistingDepartment() {
        List<String>expected = Arrays.asList(
                DEPARTMENT_1,
                DEPARTMENT_2,
                DEPARTMENT_3,
                DEPARTMENT_4,
                DEPARTMENT_5
        );

        assertFalse(repository.set(DEPARTMENT_6, DEPARTMENT_5));
        assertArrayEquals(expected.toArray(new String[0]), repository.getAll().toArray(new String[0]));
    }

    @Test
    public void testSet_replaceNotExistingDepartmentWithTheSameDepartment() {
        List<String>expected = Arrays.asList(
                DEPARTMENT_1,
                DEPARTMENT_2,
                DEPARTMENT_3,
                DEPARTMENT_4,
                DEPARTMENT_5
        );

        assertFalse(repository.set(DEPARTMENT_6, DEPARTMENT_6));
        assertArrayEquals(expected.toArray(new String[0]), repository.getAll().toArray(new String[0]));
    }

    @Test
    public void testSet_replaceNotExistingDepartmentWithNewDepartment() {
        List<String>expected = Arrays.asList(
                DEPARTMENT_1,
                DEPARTMENT_2,
                DEPARTMENT_3,
                DEPARTMENT_4,
                DEPARTMENT_5
        );

        assertFalse(repository.set(DEPARTMENT_6, DEPARTMENT_7));
        assertArrayEquals(expected.toArray(new String[0]), repository.getAll().toArray(new String[0]));
    }

    @Test
    public void testRemoveExistingDepartment() {
        List<String>expected = Arrays.asList(
                DEPARTMENT_1,
                DEPARTMENT_3,
                DEPARTMENT_4,
                DEPARTMENT_5
        );

        assertTrue(repository.remove(DEPARTMENT_2));
        assertArrayEquals(expected.toArray(new String[0]), repository.getAll().toArray(new String[0]));
    }

    @Test
    public void testRemoveNotExistingDepartment() {
        List<String>expected = Arrays.asList(
                DEPARTMENT_1,
                DEPARTMENT_2,
                DEPARTMENT_3,
                DEPARTMENT_4,
                DEPARTMENT_5
        );

        assertFalse(repository.remove(DEPARTMENT_6));
        assertArrayEquals(expected.toArray(new String[0]), repository.getAll().toArray(new String[0]));
    }

    @Test
    public void testClear() {
        assertTrue(repository.clear());
        assertArrayEquals(new String[0], repository.getAll().toArray(new String[0]));
    }

    @Test
    public void rewriteNewList() {
        List<String>expected = Arrays.asList(
                DEPARTMENT_6,
                DEPARTMENT_7
        );

        assertTrue(repository.rewrite(expected));
        assertArrayEquals(expected.toArray(new String[0]), repository.getAll().toArray(new String[0]));
    }

    @Test
    public void rewriteSameList() {
        List<String>expected = Arrays.asList(
                DEPARTMENT_1,
                DEPARTMENT_2,
                DEPARTMENT_3,
                DEPARTMENT_4,
                DEPARTMENT_5
        );

        assertTrue(repository.rewrite(expected));
        assertArrayEquals(expected.toArray(new String[0]), repository.getAll().toArray(new String[0]));
    }

    @Test
    public void rewriteEmptyList() {
        List<String>expected = new ArrayList<>();

        assertTrue(repository.rewrite(expected));
        assertArrayEquals(expected.toArray(new String[0]), repository.getAll().toArray(new String[0]));
    }
}