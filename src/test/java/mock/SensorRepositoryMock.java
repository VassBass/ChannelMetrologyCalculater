package mock;

import repository.config.RepositoryConfigHolder;
import repository.connection.SqliteRepositoryDBConnector;
import repository.repos.sensor.BufferedSensorRepositorySQLite;
import repository.repos.sensor.SensorRepository;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class SensorRepositoryMock extends BufferedSensorRepositorySQLite {
    private static final RepositoryConfigHolder configHolder = new RepositoryConfigHolderMock();

    public SensorRepositoryMock() throws SQLException, IOException {
        super(configHolder, new SqliteRepositoryDBConnector(configHolder));
        init();
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void init() throws IOException, SQLException {
        new File(configHolder.getDBFile()).createNewFile();
        String sql = String.format("CREATE TABLE IF NOT EXISTS %s (" +
                "channel_code text NOT NULL UNIQUE" +
                ", type text NOT NULL" +
                ", serial_number text" +
                ", measurement_name text NOT NULL" +
                ", measurement_value text" +
                ", error_formula text NOT NULL" +
                ", range_min real NOT NULL" +
                ", range_max real NOT NULL" +
                ", PRIMARY KEY (channel_code)" +
                ");", configHolder.getTableName(SensorRepository.class));
        try (Connection connection = DriverManager.getConnection(configHolder.getDBUrl());
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
        }
    }

    public void dispose() throws SQLException {
        String sql = String.format("DROP TABLE IF EXISTS %s", configHolder.getTableName(SensorRepository.class));
        try (Connection connection = DriverManager.getConnection(configHolder.getDBUrl());
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
        }
    }
}
