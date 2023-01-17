package service.repository.init;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import service.repository.config.RepositoryConfigHolder;
import service.repository.config.SqliteRepositoryConfigHolder;
import service.repository.connection.RepositoryDBConnector;
import service.repository.connection.SqliteRepositoryDBConnector;
import service.repository.repos.department.DepartmentRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.*;

import static org.junit.Assert.assertTrue;

public class DepartmentRepositoryInitializerTest {
    private static final String TEST_REPOSITORY_PROPERTIES_FILE = "properties/test_repository.properties";
    private static final File TEST_DB_FILE = new File("TestData.db");
    private static final String TEST_DB_URL = "jdbc:sqlite:TestData.db";

    private String tableName;
    private RepositoryInitializer initializer;

    @BeforeClass
    public static void refreshDBFile() throws IOException {
        if (!TEST_DB_FILE.exists()) Files.createFile(TEST_DB_FILE.toPath());
    }

    @Before
    public void setUp() {
        RepositoryConfigHolder configHolder = new SqliteRepositoryConfigHolder(TEST_REPOSITORY_PROPERTIES_FILE);
        RepositoryDBConnector connector = new SqliteRepositoryDBConnector(configHolder);

        tableName = configHolder.getTableName(DepartmentRepository.class);
        initializer = new DepartmentRepositoryInitializer(configHolder, connector);
    }

    @AfterClass
    public static void deleteDBFile() throws IOException {
        if (TEST_DB_FILE.exists()) Files.delete(TEST_DB_FILE.toPath());
    }

    @Test
    public void testInit() throws SQLException {
        initializer.init();

        try (Connection connection = DriverManager.getConnection(TEST_DB_URL)) {
            DatabaseMetaData dbm = connection.getMetaData();
            try (ResultSet result = dbm.getTables(null, null, tableName, null)) {
                assertTrue(result.next());
            }
        }
    }
}