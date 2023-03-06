package mock;

import repository.config.RepositoryConfigHolder;
import repository.connection.SqliteRepositoryDBConnector;
import repository.repos.channel.BufferedChannelRepositorySQLite;
import repository.repos.channel.ChannelRepository;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ChannelRepositoryMock extends BufferedChannelRepositorySQLite {
    private static final RepositoryConfigHolder configHolder = new RepositoryConfigHolderMock();

    public ChannelRepositoryMock() throws SQLException, IOException {
        super(configHolder, new SqliteRepositoryDBConnector(configHolder));
        init();
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void init() throws SQLException, IOException {
        new File(configHolder.getDBFile()).createNewFile();
        String sql = String.format("CREATE TABLE IF NOT EXISTS %s ("
                + "code text NOT NULL UNIQUE"
                + ", name text NOT NULL"
                + ", department text"
                + ", area text"
                + ", process text"
                + ", installation text"
                + ", technology_number text NOT NULL"
                + ", protocol_number text"
                + ", reference text"
                + ", date text"
                + ", suitability text NOT NULL"
                + ", measurement_name text NOT NULL"
                + ", measurement_value text NOT NULL"
                + ", frequency real NOT NULL"
                + ", range_min real NOT NULL"
                + ", range_max real NOT NULL"
                + ", allowable_error_percent real NOT NULL"
                + ", allowable_error_value real NOT NULL"
                + ", PRIMARY KEY (\"code\")"
                + ");", configHolder.getTableName(ChannelRepository.class));
        try (Connection connection = DriverManager.getConnection(configHolder.getDBUrl());
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
        }
    }

    public void dispose() throws SQLException {
        String sql = String.format("DROP TABLE IF EXISTS %s", configHolder.getTableName(ChannelRepository.class));
        try (Connection connection = DriverManager.getConnection(configHolder.getDBUrl());
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
        }
    }
}
