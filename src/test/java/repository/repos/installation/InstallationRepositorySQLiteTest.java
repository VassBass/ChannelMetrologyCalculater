package repository.repos.installation;

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

public class InstallationRepositorySQLiteTest {
    private static final String TEST_REPOSITORY_PROPERTIES_FILE = "properties/repository_test.properties";
    private static final File TEST_DB_FILE = new File("TestData.db");
    private static final String TEST_DB_URL = "jdbc:sqlite:TestData.db";
    private static final String TABLE_NAME = "installations";

    private static final String INSTALLATION_1 = "installation1";
    private static final String INSTALLATION_2 = "installation2";
    private static final String INSTALLATION_3 = "installation3";
    private static final String INSTALLATION_4 = "installation4";
    private static final String INSTALLATION_5 = "installation5";
    private static final String INSTALLATION_6 = "installation6";
    private static final String INSTALLATION_7 = "installation7";

    private InstallationRepository repository;

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @BeforeClass
    public static void createDB() throws IOException, SQLException {
        TEST_DB_FILE.createNewFile();
        String sql = String.format("CREATE TABLE IF NOT EXISTS %s (" +
                "installation text NOT NULL UNIQUE" +
                ", PRIMARY KEY (\"installation\")" +
                ");", TABLE_NAME);
        try (Connection connection = DriverManager.getConnection(TEST_DB_URL);
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
        }
    }

    @AfterClass
    public static void removeDB() throws SQLException {
        String sql = String.format("DROP TABLE IF EXISTS %s", TABLE_NAME);
        DriverManager.registerDriver(new JDBC());
        try (Connection connection = DriverManager.getConnection(TEST_DB_URL);
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
        }
    }

    @Before
    public void init() throws SQLException {
        String sql = String.format("INSERT INTO %s VALUES ('%s'), ('%s'), ('%s'), ('%s'), ('%s');",
                TABLE_NAME, INSTALLATION_1, INSTALLATION_2, INSTALLATION_3, INSTALLATION_4, INSTALLATION_5);

        DriverManager.registerDriver(new JDBC());
        try (Connection connection = DriverManager.getConnection(TEST_DB_URL);
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
        }

        RepositoryConfigHolder configHolder = new SqliteRepositoryConfigHolder(TEST_REPOSITORY_PROPERTIES_FILE);
        RepositoryDBConnector connector = new SqliteRepositoryDBConnector(configHolder);
        repository = new InstallationRepositorySQLite(configHolder, connector);
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
        List<String> expected = Arrays.asList(INSTALLATION_1, INSTALLATION_2, INSTALLATION_3, INSTALLATION_4, INSTALLATION_5);
        List<String> actual =  new ArrayList<>(repository.getAll());
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testAddNewArea() {
        List<String>expected = Arrays.asList(INSTALLATION_1, INSTALLATION_2, INSTALLATION_3, INSTALLATION_4, INSTALLATION_5, INSTALLATION_6);

        assertTrue(repository.add(INSTALLATION_6));
        List<String> actual =  new ArrayList<>(repository.getAll());
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testAddExistingArea(){
        List<String>expected = Arrays.asList(INSTALLATION_1, INSTALLATION_2, INSTALLATION_3, INSTALLATION_4, INSTALLATION_5);

        assertFalse(repository.add(INSTALLATION_1));
        List<String> actual =  new ArrayList<>(repository.getAll());
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testAddAllNewAreas() {
        List<String> expected = Arrays.asList(INSTALLATION_1, INSTALLATION_2, INSTALLATION_3, INSTALLATION_4, INSTALLATION_5, INSTALLATION_6, INSTALLATION_7);

        assertTrue(repository.addAll(Arrays.asList(INSTALLATION_6, INSTALLATION_7)));
        List<String> actual =  new ArrayList<>(repository.getAll());
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testAddAllWithExistedAreas() {
        List<String> expected = Arrays.asList(INSTALLATION_1, INSTALLATION_2, INSTALLATION_3, INSTALLATION_4, INSTALLATION_5, INSTALLATION_6, INSTALLATION_7);

        assertTrue(repository.addAll(Arrays.asList(INSTALLATION_1, INSTALLATION_6, INSTALLATION_2, INSTALLATION_7)));
        List<String> actual =  new ArrayList<>(repository.getAll());
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testAddAllWithNull() {
        List<String> expected = Arrays.asList(INSTALLATION_1, INSTALLATION_2, INSTALLATION_3, INSTALLATION_4, INSTALLATION_5, INSTALLATION_6, INSTALLATION_7);

        assertTrue(repository.addAll(Arrays.asList(null, INSTALLATION_6, INSTALLATION_7)));
        List<String> actual =  new ArrayList<>(repository.getAll());
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testSet_replaceExistingAreaWithExistingArea() {
        List<String>expected = Arrays.asList(INSTALLATION_1, INSTALLATION_2, INSTALLATION_3, INSTALLATION_4, INSTALLATION_5);

        assertFalse(repository.set(INSTALLATION_2, INSTALLATION_5));
        List<String> actual =  new ArrayList<>(repository.getAll());
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testSet_replaceExistingAreaWithTheSameArea() {
        List<String>expected = Arrays.asList(INSTALLATION_1, INSTALLATION_2, INSTALLATION_3, INSTALLATION_4, INSTALLATION_5);

        assertTrue(repository.set(INSTALLATION_2, INSTALLATION_2));
        List<String> actual =  new ArrayList<>(repository.getAll());
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testSet_replaceExistingAreaWithNewArea() {
        List<String>expected = Arrays.asList(INSTALLATION_1, INSTALLATION_6, INSTALLATION_3, INSTALLATION_4, INSTALLATION_5);

        assertTrue(repository.set(INSTALLATION_2, INSTALLATION_6));
        List<String> actual =  new ArrayList<>(repository.getAll());
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testSet_replaceNotExistingAreaWithExistingArea() {
        List<String>expected = Arrays.asList(INSTALLATION_1, INSTALLATION_2, INSTALLATION_3, INSTALLATION_4, INSTALLATION_5);

        assertFalse(repository.set(INSTALLATION_6, INSTALLATION_5));
        List<String> actual =  new ArrayList<>(repository.getAll());
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testSet_replaceNotExistingAreaWithTheSameArea() {
        List<String>expected = Arrays.asList(INSTALLATION_1, INSTALLATION_2, INSTALLATION_3, INSTALLATION_4, INSTALLATION_5);

        assertFalse(repository.set(INSTALLATION_6, INSTALLATION_6));
        List<String> actual =  new ArrayList<>(repository.getAll());
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testSet_replaceNotExistingAreaWithNewArea() {
        List<String>expected = Arrays.asList(INSTALLATION_1, INSTALLATION_2, INSTALLATION_3, INSTALLATION_4, INSTALLATION_5);

        assertFalse(repository.set(INSTALLATION_6, INSTALLATION_7));
        List<String> actual =  new ArrayList<>(repository.getAll());
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testRemoveExistingArea() {
        List<String>expected = Arrays.asList(INSTALLATION_1, INSTALLATION_3, INSTALLATION_4, INSTALLATION_5);

        assertTrue(repository.remove(INSTALLATION_2));
        List<String> actual =  new ArrayList<>(repository.getAll());
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testRemoveNotExistingArea() {
        List<String>expected = Arrays.asList(INSTALLATION_1, INSTALLATION_2, INSTALLATION_3, INSTALLATION_4, INSTALLATION_5);

        assertFalse(repository.remove(INSTALLATION_6));
        List<String> actual =  new ArrayList<>(repository.getAll());
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
        List<String>expected = Arrays.asList(INSTALLATION_6, INSTALLATION_7);
        List<String> toRewrite = Arrays.asList(INSTALLATION_6, null, INSTALLATION_7);

        assertTrue(repository.rewrite(toRewrite));
        List<String> actual =  new ArrayList<>(repository.getAll());
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testRewriteEmptyList() {
        List<String>expected = new ArrayList<>();

        assertTrue(repository.rewrite(expected));
        List<String> actual =  new ArrayList<>(repository.getAll());
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }
}