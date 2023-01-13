package service.repository.init;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.repository.config.RepositoryConfigHolder;
import service.repository.connection.RepositoryDBConnector;
import service.repository.repos.measurement.MeasurementRepository;

import java.sql.SQLException;
import java.sql.Statement;

public class MeasurementRepositoryInitializer implements RepositoryInitializer {
    private static final Logger logger = LoggerFactory.getLogger(MeasurementRepositoryInitializer.class);

    private final RepositoryConfigHolder configHolder;
    private final RepositoryDBConnector connector;

    public MeasurementRepositoryInitializer(RepositoryConfigHolder configHolder, RepositoryDBConnector connector) {
        this.configHolder = configHolder;
        this.connector = connector;
    }

    @Override
    public void init() {
        String tableName = configHolder.getTableName(MeasurementRepository.class);

        String sql = String.format("CREATE TABLE IF NOT EXISTS %s (" +
                "name text NOT NULL" +
                ", value text NOT NULL UNIQUE" +
                ", factors text NOT NULL" +
                ", PRIMARY KEY(\"value\")" +
                ");", tableName);

        try (Statement statement = connector.getStatement()) {
            statement.execute(sql);
            logger.info("Initialization completed successfully");
        } catch (SQLException e) {
            logger.warn("Exception was thrown!", e);
        }
    }
}
