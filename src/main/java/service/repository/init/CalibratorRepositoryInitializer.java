package service.repository.init;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.CalibratorRepository;
import service.repository.config.RepositoryConfigHolder;
import service.repository.connection.RepositoryDBConnector;

import java.sql.SQLException;
import java.sql.Statement;

public class CalibratorRepositoryInitializer implements RepositoryInitializer {
    private static final Logger logger = LoggerFactory.getLogger(CalibratorRepositoryInitializer.class);

    private final RepositoryConfigHolder configHolder;
    private final RepositoryDBConnector connector;

    public CalibratorRepositoryInitializer(RepositoryConfigHolder configHolder, RepositoryDBConnector connector) {
        this.configHolder = configHolder;
        this.connector = connector;
    }

    @Override
    public void init() {
        String tableName = configHolder.getTableName(CalibratorRepository.class);

        String sql = String.format("CREATE TABLE IF NOT EXISTS %s ("
                + "name text NOT NULL UNIQUE"
                + ", type text NOT NULL"
                + ", number text NOT NULL"
                + ", measurement text NOT NULL"
                + ", value text NOT NULL"
                + ", error_formula text NOT NULL"
                + ", certificate text NOT NULL"
                + ", range_min real NOT NULL"
                + ", range_max real NOT NULL"
                + ", PRIMARY KEY (\"name\")"
                + ");", tableName);

        try (Statement statement = connector.getStatement()) {
            statement.execute(sql);
            logger.info("Initialization completed successfully");
        } catch (SQLException e) {
            logger.warn("Exception was thrown!", e);
        }
    }
}
