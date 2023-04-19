package repository.repos.department;

import org.junit.*;
import org.sqlite.JDBC;
import repository.config.RepositoryConfigHolder;
import repository.config.SqliteRepositoryConfigHolder;
import repository.connection.RepositoryDBConnector;
import repository.connection.SqliteRepositoryDBConnector;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class DepartmentRepositorySQLiteTest {
    private static final String TEST_REPOSITORY_PROPERTIES_FILE = "properties/repository_test.properties";
    private static final File TEST_DB_FILE = new File("TestData.db");
    private static final String TEST_DB_URL = "jdbc:sqlite:TestData.db";
    private static final String TABLE_NAME = "departments";

    private static final String DEPARTMENT_1 = "department1";
    private static final String DEPARTMENT_2 = "department2";
    private static final String DEPARTMENT_3 = "department3";
    private static final String DEPARTMENT_4 = "department4";
    private static final String DEPARTMENT_5 = "department5";
    private static final String DEPARTMENT_6 = "department6";
    private static final String DEPARTMENT_7 = "department7";

    private DepartmentRepository repository;

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @BeforeClass
    public static void createDB() throws IOException, SQLException {
        TEST_DB_FILE.createNewFile();
        String sql = String.format("CREATE TABLE IF NOT EXISTS %s (" +
                "department text NOT NULL UNIQUE" +
                ", PRIMARY KEY (\"department\")" +
                ");", TABLE_NAME);
        try (Connection connection = DriverManager.getConnection(TEST_DB_URL);
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
        }
    }

    @AfterClass
    public static void removeDB() throws SQLException {
        String sql = String.format("DROP TABLE IF EXISTS %s", TABLE_NAME);
        try (Connection connection = DriverManager.getConnection(TEST_DB_URL);
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
        }
    }

    @Before
    public void init() throws SQLException {
        String sql = String.format("INSERT INTO %s VALUES ('%s'), ('%s'), ('%s'), ('%s'), ('%s');",
                TABLE_NAME, DEPARTMENT_1, DEPARTMENT_2, DEPARTMENT_3, DEPARTMENT_4, DEPARTMENT_5);

        DriverManager.registerDriver(new JDBC());
        try (Connection connection = DriverManager.getConnection(TEST_DB_URL);
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
        }

        RepositoryConfigHolder configHolder = new SqliteRepositoryConfigHolder(TEST_REPOSITORY_PROPERTIES_FILE);
        RepositoryDBConnector connector = new SqliteRepositoryDBConnector(configHolder);
        repository = new DepartmentRepositorySQLite(configHolder, connector);
    }

    @After
    public void clearTestDB() throws Exception {
        String sql = String.format("DELETE FROM %s;", TABLE_NAME);
        try (Connection connection = DriverManager.getConnection(TEST_DB_URL);
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
        }
    }

    @Test
    public void testGetAll() {
        List<String> expected = Arrays.asList(DEPARTMENT_1, DEPARTMENT_2, DEPARTMENT_3, DEPARTMENT_4, DEPARTMENT_5);
        List<String> actual = new ArrayList<>(repository.getAll());
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testAddNewArea() {
        List<String>expected = Arrays.asList(DEPARTMENT_1, DEPARTMENT_2, DEPARTMENT_3, DEPARTMENT_4, DEPARTMENT_5, DEPARTMENT_6);

        assertTrue(repository.add(DEPARTMENT_6));
        List<String> actual = new ArrayList<>(repository.getAll());
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testAddExistingArea(){
        List<String>expected = Arrays.asList(DEPARTMENT_1, DEPARTMENT_2, DEPARTMENT_3, DEPARTMENT_4, DEPARTMENT_5);

        assertFalse(repository.add(DEPARTMENT_1));
        List<String> actual = new ArrayList<>(repository.getAll());
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testAddAllNewAreas() {
        List<String> expected = Arrays.asList(DEPARTMENT_1, DEPARTMENT_2, DEPARTMENT_3, DEPARTMENT_4, DEPARTMENT_5, DEPARTMENT_6, DEPARTMENT_7);

        assertTrue(repository.addAll(Arrays.asList(DEPARTMENT_6, DEPARTMENT_7)));
        List<String> actual = new ArrayList<>(repository.getAll());
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testAddAllWithExistedAreas() {
        List<String> expected = Arrays.asList(DEPARTMENT_1, DEPARTMENT_2, DEPARTMENT_3, DEPARTMENT_4, DEPARTMENT_5, DEPARTMENT_6, DEPARTMENT_7);

        assertTrue(repository.addAll(Arrays.asList(DEPARTMENT_1, DEPARTMENT_6, DEPARTMENT_2, DEPARTMENT_7)));
        List<String> actual = new ArrayList<>(repository.getAll());
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testAddAllWithNull() {
        List<String> expected = Arrays.asList(DEPARTMENT_1, DEPARTMENT_2, DEPARTMENT_3, DEPARTMENT_4, DEPARTMENT_5, DEPARTMENT_6, DEPARTMENT_7);

        assertTrue(repository.addAll(Arrays.asList(null, DEPARTMENT_6, DEPARTMENT_7)));
        List<String> actual = new ArrayList<>(repository.getAll());
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testSet_replaceExistingAreaWithExistingArea() {
        List<String>expected = Arrays.asList(DEPARTMENT_1, DEPARTMENT_2, DEPARTMENT_3, DEPARTMENT_4, DEPARTMENT_5);

        assertFalse(repository.set(DEPARTMENT_2, DEPARTMENT_5));
        List<String> actual = new ArrayList<>(repository.getAll());
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testSet_replaceExistingAreaWithTheSameArea() {
        List<String>expected = Arrays.asList(DEPARTMENT_1, DEPARTMENT_2, DEPARTMENT_3, DEPARTMENT_4, DEPARTMENT_5);

        assertTrue(repository.set(DEPARTMENT_2, DEPARTMENT_2));
        List<String> actual = new ArrayList<>(repository.getAll());
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testSet_replaceExistingAreaWithNewArea() {
        List<String>expected = Arrays.asList(DEPARTMENT_1, DEPARTMENT_6, DEPARTMENT_3, DEPARTMENT_4, DEPARTMENT_5);

        assertTrue(repository.set(DEPARTMENT_2, DEPARTMENT_6));
        List<String> actual = new ArrayList<>(repository.getAll());
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testSet_replaceNotExistingAreaWithExistingArea() {
        List<String>expected = Arrays.asList(DEPARTMENT_1, DEPARTMENT_2, DEPARTMENT_3, DEPARTMENT_4, DEPARTMENT_5);

        assertFalse(repository.set(DEPARTMENT_6, DEPARTMENT_5));
        List<String> actual = new ArrayList<>(repository.getAll());
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testSet_replaceNotExistingAreaWithTheSameArea() {
        List<String>expected = Arrays.asList(DEPARTMENT_1, DEPARTMENT_2, DEPARTMENT_3, DEPARTMENT_4, DEPARTMENT_5);

        assertFalse(repository.set(DEPARTMENT_6, DEPARTMENT_6));
        List<String> actual = new ArrayList<>(repository.getAll());
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testSet_replaceNotExistingAreaWithNewArea() {
        List<String>expected = Arrays.asList(DEPARTMENT_1, DEPARTMENT_2, DEPARTMENT_3, DEPARTMENT_4, DEPARTMENT_5);

        assertFalse(repository.set(DEPARTMENT_6, DEPARTMENT_7));
        List<String> actual = new ArrayList<>(repository.getAll());
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testRemoveExistingArea() {
        List<String>expected = Arrays.asList(DEPARTMENT_1, DEPARTMENT_3, DEPARTMENT_4, DEPARTMENT_5);

        assertTrue(repository.remove(DEPARTMENT_2));
        List<String> actual = new ArrayList<>(repository.getAll());
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testRemoveNotExistingArea() {
        List<String>expected = Arrays.asList(DEPARTMENT_1, DEPARTMENT_2, DEPARTMENT_3, DEPARTMENT_4, DEPARTMENT_5);

        assertFalse(repository.remove(DEPARTMENT_6));
        List<String> actual = new ArrayList<>(repository.getAll());
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testClear() {
        assertTrue(repository.clear());
        assertEquals(0, repository.getAll().size());
    }

    @Test
    public void testRewriteNewList() {
        List<String>expected = Arrays.asList(DEPARTMENT_6, DEPARTMENT_7);
        List<String> toRewrite = Arrays.asList(DEPARTMENT_6, null, DEPARTMENT_7);

        assertTrue(repository.rewrite(toRewrite));
        List<String> actual = new ArrayList<>(repository.getAll());
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testRewriteEmptyList() {
        List<String>expected = new ArrayList<>();

        assertTrue(repository.rewrite(expected));
        List<String> actual = new ArrayList<>(repository.getAll());
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }
}