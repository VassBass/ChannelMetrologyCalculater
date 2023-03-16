package repository.init;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import repository.config.RepositoryConfigHolder;
import repository.config.SqliteRepositoryConfigHolder;
import repository.connection.RepositoryDBConnector;
import repository.connection.SqliteRepositoryDBConnector;

import java.sql.*;

import static org.junit.Assert.assertTrue;

public class PersonRepositoryInitializerTest {
    private static final String TEST_REPOSITORY_PROPERTIES_FILE = "properties/repository_test.properties";
    private static final String TABLE_NAME = "persons";

    private static RepositoryConfigHolder configHolder;
    private RepositoryInitializer initializer;

    @Before
    public void setUp() {
        configHolder = new SqliteRepositoryConfigHolder(TEST_REPOSITORY_PROPERTIES_FILE);
        RepositoryDBConnector connector = new SqliteRepositoryDBConnector(configHolder);

        initializer = new PersonRepositoryInitializer(configHolder, connector);
    }

    @AfterClass
    public static void deleteDBFile() throws SQLException {
        String dbUrl = configHolder.getDBUrl();
        try (Statement statement = DriverManager.getConnection(dbUrl).createStatement()) {
            String sql = String.format("DROP TABLE IF EXISTS %s;", TABLE_NAME);
            statement.execute(sql);
        }
    }

    @Test
    public void testInit() throws SQLException {
        String testDBUrl = configHolder.getDBUrl();
        initializer.init();

        try (Connection connection = DriverManager.getConnection(testDBUrl)) {
            DatabaseMetaData dbm = connection.getMetaData();
            try (ResultSet result = dbm.getTables(null, null, TABLE_NAME, null)) {
                assertTrue(result.next());
            }
        }
    }
}