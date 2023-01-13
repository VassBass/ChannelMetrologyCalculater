package repository.impl;

import org.junit.*;
import org.sqlite.JDBC;
import repository.Repository;
import service.repository.repos.process.ProcessRepositorySQLite;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class ProcessRepositorySQLiteTest {

    private static final String DB_URL = "jdbc:sqlite:TestData.db";
    private static final Repository<String> repository = new ProcessRepositorySQLite(DB_URL, null, null);

    private static final String PROCESS_1 = "process1";
    private static final String PROCESS_2 = "process2";
    private static final String PROCESS_3 = "process3";
    private static final String PROCESS_4 = "process4";
    private static final String PROCESS_5 = "process5";
    private static final String PROCESS_6 = "process6";
    private static final String PROCESS_7 = "process7";

    @BeforeClass
    public static void testCreateTable(){
        assertTrue(repository.createTable());
    }

    @AfterClass
    public static void removeTable() throws SQLException {
        String sql = "DROP TABLE processes;";
        DriverManager.registerDriver(new JDBC());
        try (Connection connection = DriverManager.getConnection(DB_URL);
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
        }
    }

    @Before
    public void fuelingTestDB() throws SQLException {
        String sql = "INSERT INTO processes VALUES "
                + "('" + PROCESS_1 + "')"
                + ", ('" + PROCESS_2 + "')"
                + ", ('" + PROCESS_3 + "')"
                + ", ('" + PROCESS_4 + "')"
                + ", ('" + PROCESS_5 + "')"
                + ";";

        DriverManager.registerDriver(new JDBC());
        try (Connection connection = DriverManager.getConnection(DB_URL);
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
        }
    }

    @After
    public void clearTestDB() throws Exception {
        String sql = "DELETE FROM processes;";
        DriverManager.registerDriver(new JDBC());
        try (Connection connection = DriverManager.getConnection(DB_URL);
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
        }
    }

    @Test
    public void testGetAll() {
        List<String>expected = Arrays.asList(
                PROCESS_1,
                PROCESS_2,
                PROCESS_3,
                PROCESS_4,
                PROCESS_5
        );

        assertArrayEquals(expected.toArray(new String[0]), repository.getAll().toArray(new String[0]));
    }

    @Test
    public void testAddNewProcess() {
        List<String>expected = Arrays.asList(
                PROCESS_1,
                PROCESS_2,
                PROCESS_3,
                PROCESS_4,
                PROCESS_5,
                PROCESS_6
        );

        assertTrue(repository.add(PROCESS_6));
        assertArrayEquals(expected.toArray(new String[0]), repository.getAll().toArray(new String[0]));
    }

    @Test
    public void testAddExistingProcess(){
        List<String>expected = Arrays.asList(
                PROCESS_1,
                PROCESS_2,
                PROCESS_3,
                PROCESS_4,
                PROCESS_5
        );

        assertFalse(repository.add(PROCESS_1));
        assertArrayEquals(expected.toArray(new String[0]), repository.getAll().toArray(new String[0]));
    }

    @Test
    public void testSet_replaceExistingProcessWithExistingProcess() {
        List<String>expected = Arrays.asList(
                PROCESS_1,
                PROCESS_2,
                PROCESS_3,
                PROCESS_4,
                PROCESS_5
        );

        assertFalse(repository.set(PROCESS_2, PROCESS_5));
        assertArrayEquals(expected.toArray(new String[0]), repository.getAll().toArray(new String[0]));
    }

    @Test
    public void testSet_replaceExistingProcessWithTheSameProcess() {
        List<String>expected = Arrays.asList(
                PROCESS_1,
                PROCESS_2,
                PROCESS_3,
                PROCESS_4,
                PROCESS_5
        );

        assertTrue(repository.set(PROCESS_2, PROCESS_2));
        assertArrayEquals(expected.toArray(new String[0]), repository.getAll().toArray(new String[0]));
    }

    @Test
    public void testSet_replaceExistingProcessWithNewProcess() {
        List<String>expected = Arrays.asList(
                PROCESS_1,
                PROCESS_6,
                PROCESS_3,
                PROCESS_4,
                PROCESS_5
        );

        assertTrue(repository.set(PROCESS_2, PROCESS_6));
        assertArrayEquals(expected.toArray(new String[0]), repository.getAll().toArray(new String[0]));
    }

    @Test
    public void testSet_replaceNotExistingProcessWithExistingProcess() {
        List<String>expected = Arrays.asList(
                PROCESS_1,
                PROCESS_2,
                PROCESS_3,
                PROCESS_4,
                PROCESS_5
        );

        assertFalse(repository.set(PROCESS_6, PROCESS_5));
        assertArrayEquals(expected.toArray(new String[0]), repository.getAll().toArray(new String[0]));
    }

    @Test
    public void testSet_replaceNotExistingProcessWithTheSameProcess() {
        List<String>expected = Arrays.asList(
                PROCESS_1,
                PROCESS_2,
                PROCESS_3,
                PROCESS_4,
                PROCESS_5
        );

        assertFalse(repository.set(PROCESS_6, PROCESS_6));
        assertArrayEquals(expected.toArray(new String[0]), repository.getAll().toArray(new String[0]));
    }

    @Test
    public void testSet_replaceNotExistingProcessWithNewProcess() {
        List<String>expected = Arrays.asList(
                PROCESS_1,
                PROCESS_2,
                PROCESS_3,
                PROCESS_4,
                PROCESS_5
        );

        assertFalse(repository.set(PROCESS_6, PROCESS_7));
        assertArrayEquals(expected.toArray(new String[0]), repository.getAll().toArray(new String[0]));
    }

    @Test
    public void testRemoveExistingProcess() {
        List<String>expected = Arrays.asList(
                PROCESS_1,
                PROCESS_3,
                PROCESS_4,
                PROCESS_5
        );

        assertTrue(repository.remove(PROCESS_2));
        assertArrayEquals(expected.toArray(new String[0]), repository.getAll().toArray(new String[0]));
    }

    @Test
    public void testRemoveNotExistingProcess() {
        List<String>expected = Arrays.asList(
                PROCESS_1,
                PROCESS_2,
                PROCESS_3,
                PROCESS_4,
                PROCESS_5
        );

        assertFalse(repository.remove(PROCESS_6));
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
                PROCESS_6,
                PROCESS_7
        );
        List<String> toRewrite = Arrays.asList(
                PROCESS_6,
                null,
                PROCESS_7
        );

        assertTrue(repository.rewrite(toRewrite));
        assertArrayEquals(expected.toArray(new String[0]), repository.getAll().toArray(new String[0]));
    }

    @Test
    public void rewriteEmptyList() {
        List<String>expected = new ArrayList<>();

        assertTrue(repository.rewrite(expected));
        assertArrayEquals(expected.toArray(new String[0]), repository.getAll().toArray(new String[0]));
    }
}