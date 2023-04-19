package repository.init;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.repos.sensor.SensorRepository;
import repository.config.RepositoryConfigHolder;
import repository.connection.RepositoryDBConnector;

import java.sql.SQLException;
import java.sql.Statement;

public class SensorRepositoryInitializer extends RepositoryInitializer {
    private static final Logger logger = LoggerFactory.getLogger(SensorRepositoryInitializer.class);

    public SensorRepositoryInitializer(RepositoryConfigHolder configHolder, RepositoryDBConnector connector) {
        super(configHolder, connector);
    }

    @Override
    public void init() {
        super.init();

        String tableName = configHolder.getTableName(SensorRepository.class);

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
                ");", tableName);

        try (Statement statement = connector.getStatement()) {
            statement.execute(sql);
            logger.info("Initialization completed successfully");
        } catch (SQLException e) {
            logger.warn("Exception was thrown!", e);
        }
    }
}
