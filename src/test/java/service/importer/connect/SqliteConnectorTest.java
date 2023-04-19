package service.importer.connect;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.*;

public class SqliteConnectorTest {

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Test
    public void testGetDBConnection() throws IOException, SQLException {
        File testFile = new File("TestData.db");
        testFile.createNewFile();

        try (Connection connection = SqliteConnector.getDBConnection(testFile)) {
            assertNotNull(connection);
        }
    }
}