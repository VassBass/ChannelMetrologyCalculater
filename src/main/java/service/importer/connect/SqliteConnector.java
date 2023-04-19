package service.importer.connect;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SqliteConnector {
    private static final String PREFIX = "jdbc:sqlite:";

    @Nullable
    public static Connection getDBConnection(@Nonnull File file) throws SQLException {
        if (file.exists()) {
            String dbUrl = PREFIX + file.getAbsolutePath();
            return DriverManager.getConnection(dbUrl);
        }

        return null;
    }
}
