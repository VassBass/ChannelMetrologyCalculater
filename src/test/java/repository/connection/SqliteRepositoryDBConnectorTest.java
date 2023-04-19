package repository.connection;

import org.junit.*;
import org.junit.runners.MethodSorters;
import org.sqlite.JDBC;
import repository.config.RepositoryConfigHolder;
import repository.config.SqliteRepositoryConfigHolder;

import java.io.File;
import java.io.IOException;
import java.sql.*;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.junit.Assert.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SqliteRepositoryDBConnectorTest {
    private static final String TEST_REPOSITORY_PROPERTIES_FILE = "properties/repository_test.properties";
    private static final String TABLE_NAME = "test";

    private static RepositoryConfigHolder configHolder;
    private SqliteRepositoryDBConnector connector;

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @BeforeClass
    public static void createDBFile() throws IOException {
        configHolder = new SqliteRepositoryConfigHolder(TEST_REPOSITORY_PROPERTIES_FILE);
        new File(configHolder.getDBFile()).createNewFile();
    }

    @Before
    public void setUp() {
        connector = new SqliteRepositoryDBConnector(configHolder);
    }

    @AfterClass
    public static void removeDB() throws SQLException {
        String sql = String.format("DROP TABLE IF EXISTS %s", TABLE_NAME);
        DriverManager.registerDriver(new JDBC());
        try (Connection connection = DriverManager.getConnection(configHolder.getDBUrl());
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
        }
    }

    @Test
    public void $1_testGetConnection() throws SQLException {
        try (Connection connection = connector.getConnection()) {
            assertNotNull(connection);
        }
    }

    @Test
    public void $2_testGetStatement() throws SQLException {
        String sqlCreate = String.format(
                "CREATE TABLE IF NOT EXISTS %s (id integer, test text, PRIMARY KEY (id AUTOINCREMENT));",
                TABLE_NAME);
        try (Statement statement = connector.getStatement()) {
            statement.execute(sqlCreate);
            assertThrows(SQLException.class, () -> statement.execute(EMPTY));
        }
    }

    @Test
    public void $3_testGetPreparedStatementWithKey() throws SQLException {
        assertThrows(SQLException.class, () -> connector.getPreparedStatement(EMPTY));

        String sql = "INSERT INTO test (test) VALUES (?);";
        try (PreparedStatement statement = connector.getPreparedStatement(sql)) {
            statement.setString(1, "test");
            assertEquals(statement.executeUpdate(), 1);
            assertTrue(statement.getGeneratedKeys().getInt(1) > 0);
        }
    }

    @Test
    public void $4_testGetResultSet() throws SQLException {
        assertThrows(SQLException.class, () -> connector.getResultSet(EMPTY));

        String sql = "SELECT test FROM test;";
        try (ResultSet resultSet = connector.getResultSet(sql)) {
            assertTrue(resultSet.next());
            assertEquals("test", resultSet.getString(1));
        }
    }

    @Test
    public void $5_testGetPreparedStatement() throws SQLException {
        assertThrows(SQLException.class, () -> connector.getPreparedStatement(EMPTY));

        String sql = "UPDATE test SET test = ? WHERE test = ?;";
        try (PreparedStatement statement = connector.getPreparedStatement(sql)) {
            statement.setString(1, "test");
            statement.setString(2, "test");
            assertEquals(statement.executeUpdate(), 1);
        }
    }
}