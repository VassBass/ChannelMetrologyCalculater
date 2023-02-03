package service.repository.connection;

import org.junit.*;
import org.junit.runners.MethodSorters;
import service.repository.config.RepositoryConfigHolder;
import service.repository.config.SqliteRepositoryConfigHolder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.junit.Assert.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SqliteRepositoryDBConnectorTest {
    private static final String TEST_REPOSITORY_PROPERTIES_FILE = "properties/test_repository.properties";

    private static RepositoryConfigHolder configHolder;
    private SqliteRepositoryDBConnector connector;

    @BeforeClass
    public static void createDBFile() throws IOException {
        configHolder = new SqliteRepositoryConfigHolder(TEST_REPOSITORY_PROPERTIES_FILE);
        Files.createFile(Paths.get(configHolder.getDBFile()));
    }

    @Before
    public void setUp() {
        connector = new SqliteRepositoryDBConnector(configHolder);
    }

    @AfterClass
    public static void deleteDBFile() throws IOException {
        Files.delete(Paths.get(configHolder.getDBFile()));
    }

    @Test
    public void $1_testGetConnection() throws SQLException {
        try (Connection connection = connector.getConnection()) {
            assertNotNull(connection);
        }
    }

    @Test
    public void $2_testGetStatement() throws SQLException {
        String sqlCreate = "CREATE TABLE IF NOT EXISTS test (id integer, test text, PRIMARY KEY (id AUTOINCREMENT));";
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