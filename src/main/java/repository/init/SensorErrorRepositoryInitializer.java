package repository.init;

import localization.Messages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.config.RepositoryConfigHolder;
import repository.connection.RepositoryDBConnector;
import repository.repos.sensor_error.SensorErrorRepository;

import java.sql.SQLException;
import java.sql.Statement;

public class SensorErrorRepositoryInitializer extends RepositoryInitializer {
    private static final Logger logger = LoggerFactory.getLogger(SensorErrorRepositoryInitializer.class);

    public SensorErrorRepositoryInitializer(RepositoryConfigHolder configHolder, RepositoryDBConnector connector) {
        super(configHolder, connector);
    }

    @Override
    public void init() {
        super.init();

        String tableName = configHolder.getTableName(SensorErrorRepository.class);

        String sql = String.format("CREATE TABLE IF NOT EXISTS %s (" +
                "id text NOT NULL UNIQUE" +
                ", type text NOT NULL" +
                ", error_formula text NOT NULL" +
                ", range_min real NOT NULL" +
                ", range_max real NOT NULL" +
                ", measurement_value NOT NULL" +
                ", PRIMARY KEY (id)" +
                ");", tableName);

        try (Statement statement = connector.getStatement()) {
            statement.execute(sql);
            logger.info(Messages.Log.INIT_SUCCESS);
        } catch (SQLException e) {
            logger.warn(Messages.Log.EXCEPTION_THROWN, e);
        }
    }
}
