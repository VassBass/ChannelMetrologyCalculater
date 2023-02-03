package service.repository.init;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.repository.repos.channel.ChannelRepository;
import service.repository.config.RepositoryConfigHolder;
import service.repository.connection.RepositoryDBConnector;

import java.sql.SQLException;
import java.sql.Statement;

public class ChannelRepositoryInitializer extends RepositoryInitializer {
    private static final Logger logger = LoggerFactory.getLogger(ChannelRepositoryInitializer.class);

    public ChannelRepositoryInitializer(RepositoryConfigHolder configHolder, RepositoryDBConnector connector) {
        super(configHolder, connector);
    }

    @Override
    public void init() {
        super.init();

        String tableName = configHolder.getTableName(ChannelRepository.class);

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
                + ", sensor_name text NOT NULL"
                + ", frequency real NOT NULL"
                + ", range_min real NOT NULL"
                + ", range_max real NOT NULL"
                + ", allowable_error_percent real NOT NULL"
                + ", allowable_error_value real NOT NULL"
                + ", control_points text NOT NULL"
                + ", PRIMARY KEY (\"code\")"
                + ");", tableName);

        try (Statement statement = connector.getStatement()) {
            statement.execute(sql);
            logger.info("Initialization completed successfully");
        } catch (SQLException e) {
            logger.warn("Exception was thrown!", e);
        }
    }
}
