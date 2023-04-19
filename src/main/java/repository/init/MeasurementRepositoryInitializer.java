package repository.init;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.config.RepositoryConfigHolder;
import repository.connection.RepositoryDBConnector;
import repository.repos.measurement.MeasurementRepository;

import java.sql.SQLException;
import java.sql.Statement;

public class MeasurementRepositoryInitializer extends RepositoryInitializer {
    private static final Logger logger = LoggerFactory.getLogger(MeasurementRepositoryInitializer.class);

    public MeasurementRepositoryInitializer(RepositoryConfigHolder configHolder, RepositoryDBConnector connector) {
        super(configHolder, connector);
    }

    @Override
    public void init() {
        super.init();

        String tableName = configHolder.getTableName(MeasurementRepository.class);

        String sql = String.format("CREATE TABLE IF NOT EXISTS %s (" +
                "name text NOT NULL" +
                ", value text NOT NULL UNIQUE" +
                ", PRIMARY KEY(value)" +
                ");", tableName);

        try (Statement statement = connector.getStatement()) {
            statement.execute(sql);
            logger.info("Initialization completed successfully");
        } catch (SQLException e) {
            logger.warn("Exception was thrown!", e);
        }
    }
}
