package repository.impl;

import org.junit.*;
import org.sqlite.JDBC;
import repository.Repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class InstallationRepositorySQLiteTest {

    private static final String DB_URL = "jdbc:sqlite:TestData.db";
    private static final Repository<String> repository = new InstallationRepositorySQLite(DB_URL, null, null);

    private static final String INSTALLATION_1 = "installation1";
    private static final String INSTALLATION_2 = "installation2";
    private static final String INSTALLATION_3 = "installation3";
    private static final String INSTALLATION_4 = "installation4";
    private static final String INSTALLATION_5 = "installation5";
    private static final String INSTALLATION_6 = "installation6";
    private static final String INSTALLATION_7 = "installation7";

    @BeforeClass
    public static void testCreateTable(){
        assertTrue(repository.createTable());
    }

    @AfterClass
    public static void removeTable() throws SQLException {
        String sql = "DROP TABLE installations;";
        DriverManager.registerDriver(new JDBC());
        try (Connection connection = DriverManager.getConnection(DB_URL);
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
        }
    }

    @Before
    public void fuelingTestDB() throws SQLException {
        String sql = "INSERT INTO installations VALUES "
                + "('" + INSTALLATION_1 + "')"
                + ", ('" + INSTALLATION_2 + "')"
                + ", ('" + INSTALLATION_3 + "')"
                + ", ('" + INSTALLATION_4 + "')"
                + ", ('" + INSTALLATION_5 + "')"
                + ";";

        DriverManager.registerDriver(new JDBC());
        try (Connection connection = DriverManager.getConnection(DB_URL);
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
        }
    }

    @After
    public void clearTestDB() throws Exception {
        String sql = "DELETE FROM installations;";
        DriverManager.registerDriver(new JDBC());
        try (Connection connection = DriverManager.getConnection(DB_URL);
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
        }
    }

    @Test
    public void testGetAll() {
        List<String> expected = new ArrayList<>();
        expected.add(INSTALLATION_1);
        expected.add(INSTALLATION_2);
        expected.add(INSTALLATION_3);
        expected.add(INSTALLATION_4);
        expected.add(INSTALLATION_5);

        assertArrayEquals(expected.toArray(new String[0]), repository.getAll().toArray(new String[0]));
    }

    @Test
    public void testAddNewInstallation() {
        List<String>expected = new ArrayList<>();
        expected.add(INSTALLATION_1);
        expected.add(INSTALLATION_2);
        expected.add(INSTALLATION_3);
        expected.add(INSTALLATION_4);
        expected.add(INSTALLATION_5);
        expected.add(INSTALLATION_6);

        assertTrue(repository.add(INSTALLATION_6));
        assertArrayEquals(expected.toArray(new String[0]), repository.getAll().toArray(new String[0]));
    }

    @Test
    public void testAddExistingInstallation(){
        List<String>expected = new ArrayList<>();
        expected.add(INSTALLATION_1);
        expected.add(INSTALLATION_2);
        expected.add(INSTALLATION_3);
        expected.add(INSTALLATION_4);
        expected.add(INSTALLATION_5);

        assertFalse(repository.add(INSTALLATION_1));
        assertArrayEquals(expected.toArray(new String[0]), repository.getAll().toArray(new String[0]));
    }

    @Test
    public void testSet_replaceExistingInstallationWithExistingInstallation() {
        List<String>expected = new ArrayList<>();
        expected.add(INSTALLATION_1);
        expected.add(INSTALLATION_2);
        expected.add(INSTALLATION_3);
        expected.add(INSTALLATION_4);
        expected.add(INSTALLATION_5);

        assertFalse(repository.set(INSTALLATION_2, INSTALLATION_5));
        assertArrayEquals(expected.toArray(new String[0]), repository.getAll().toArray(new String[0]));
    }

    @Test
    public void testSet_replaceExistingInstallationWithTheSameInstallation() {
        List<String>expected = new ArrayList<>();
        expected.add(INSTALLATION_1);
        expected.add(INSTALLATION_2);
        expected.add(INSTALLATION_3);
        expected.add(INSTALLATION_4);
        expected.add(INSTALLATION_5);

        assertTrue(repository.set(INSTALLATION_2, INSTALLATION_2));
        assertArrayEquals(expected.toArray(new String[0]), repository.getAll().toArray(new String[0]));
    }

    @Test
    public void testSet_replaceExistingInstallationWithNewInstallation() {
        List<String>expected = new ArrayList<>();
        expected.add(INSTALLATION_1);
        expected.add(INSTALLATION_6);
        expected.add(INSTALLATION_3);
        expected.add(INSTALLATION_4);
        expected.add(INSTALLATION_5);

        assertTrue(repository.set(INSTALLATION_2, INSTALLATION_6));
        assertArrayEquals(expected.toArray(new String[0]), repository.getAll().toArray(new String[0]));
    }

    @Test
    public void testSet_replaceNotExistingInstallationWithExistingInstallation() {
        List<String>expected = new ArrayList<>();
        expected.add(INSTALLATION_1);
        expected.add(INSTALLATION_2);
        expected.add(INSTALLATION_3);
        expected.add(INSTALLATION_4);
        expected.add(INSTALLATION_5);

        assertFalse(repository.set(INSTALLATION_6, INSTALLATION_5));
        assertArrayEquals(expected.toArray(new String[0]), repository.getAll().toArray(new String[0]));
    }

    @Test
    public void testSet_replaceNotExistingInstallationWithTheSameInstallation() {
        List<String>expected = new ArrayList<>();
        expected.add(INSTALLATION_1);
        expected.add(INSTALLATION_2);
        expected.add(INSTALLATION_3);
        expected.add(INSTALLATION_4);
        expected.add(INSTALLATION_5);

        assertFalse(repository.set(INSTALLATION_6, INSTALLATION_6));
        assertArrayEquals(expected.toArray(new String[0]), repository.getAll().toArray(new String[0]));
    }

    @Test
    public void testSet_replaceNotExistingInstallationWithNewInstallation() {
        List<String>expected = new ArrayList<>();
        expected.add(INSTALLATION_1);
        expected.add(INSTALLATION_2);
        expected.add(INSTALLATION_3);
        expected.add(INSTALLATION_4);
        expected.add(INSTALLATION_5);

        assertFalse(repository.set(INSTALLATION_6, INSTALLATION_7));
        assertArrayEquals(expected.toArray(new String[0]), repository.getAll().toArray(new String[0]));
    }

    @Test
    public void testRemoveExistingInstallation() {
        List<String>expected = new ArrayList<>();
        expected.add(INSTALLATION_1);
        expected.add(INSTALLATION_3);
        expected.add(INSTALLATION_4);
        expected.add(INSTALLATION_5);

        assertTrue(repository.remove(INSTALLATION_2));
        assertArrayEquals(expected.toArray(new String[0]), repository.getAll().toArray(new String[0]));
    }

    @Test
    public void testRemoveNotExistingInstallation() {
        List<String>expected = new ArrayList<>();
        expected.add(INSTALLATION_1);
        expected.add(INSTALLATION_2);
        expected.add(INSTALLATION_3);
        expected.add(INSTALLATION_4);
        expected.add(INSTALLATION_5);

        assertFalse(repository.remove(INSTALLATION_6));
        assertArrayEquals(expected.toArray(new String[0]), repository.getAll().toArray(new String[0]));
    }

    @Test
    public void testClear() {
        assertTrue(repository.clear());
        assertArrayEquals(new String[0], repository.getAll().toArray(new String[0]));
    }

    @Test
    public void rewriteNewList() {
        List<String>expected = new ArrayList<>();
        expected.add(INSTALLATION_6);
        expected.add(INSTALLATION_7);

        assertTrue(repository.rewrite(expected));
        assertArrayEquals(expected.toArray(new String[0]), repository.getAll().toArray(new String[0]));
    }

    @Test
    public void rewriteSameList() {
        List<String>expected = new ArrayList<>();
        expected.add(INSTALLATION_1);
        expected.add(INSTALLATION_2);
        expected.add(INSTALLATION_3);
        expected.add(INSTALLATION_4);
        expected.add(INSTALLATION_5);

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