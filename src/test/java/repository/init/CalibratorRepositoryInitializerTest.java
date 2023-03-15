package repository.init;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.config.RepositoryConfigHolder;
import repository.config.SqliteRepositoryConfigHolder;
import repository.connection.RepositoryDBConnector;
import repository.connection.SqliteRepositoryDBConnector;

import java.io.File;
import java.io.IOException;
import java.sql.*;

import static org.junit.Assert.assertTrue;

public class CalibratorRepositoryInitializerTest {
    private static final Logger logger = LoggerFactory.getLogger(CalibratorRepositoryInitializerTest.class);

    private static final String TEST_REPOSITORY_PROPERTIES_FILE = "properties/repository_test.properties";
    private static final String TABLE_NAME = "calibrators";

    private static RepositoryConfigHolder configHolder;
    private RepositoryInitializer initializer;

    @Before
    public void setUp() {
        configHolder = new SqliteRepositoryConfigHolder(TEST_REPOSITORY_PROPERTIES_FILE);
        RepositoryDBConnector connector = new SqliteRepositoryDBConnector(configHolder);

        initializer = new CalibratorRepositoryInitializer(configHolder, connector);
    }

    @AfterClass
    public static void deleteDBFile() throws IOException {
        String dbFileName = configHolder.getDBFile();
        if (!new File(dbFileName).delete()) {
            logger.warn(String.format("%s has not been deleted! This may affect the following tests!", dbFileName));
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