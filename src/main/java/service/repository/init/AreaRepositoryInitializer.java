package service.repository.init;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.repository.repos.area.AreaRepository;
import service.repository.config.RepositoryConfigHolder;
import service.repository.connection.RepositoryDBConnector;

import java.sql.SQLException;
import java.sql.Statement;

public class AreaRepositoryInitializer implements RepositoryInitializer {
    private static final Logger logger = LoggerFactory.getLogger(AreaRepositoryInitializer.class);

    private final RepositoryConfigHolder configHolder;
    private final RepositoryDBConnector connector;

    public AreaRepositoryInitializer(RepositoryConfigHolder configHolder, RepositoryDBConnector connector) {
        this.configHolder = configHolder;
        this.connector = connector;
    }

    @Override
    public void init() {
        String tableName = configHolder.getTableName(AreaRepository.class);

        String sql = String.format("CREATE TABLE IF NOT EXISTS %s (" +
                "area text NOT NULL UNIQUE" +
                ", PRIMARY KEY (\"area\")" +
                ");", tableName);

        try (Statement statement = connector.getStatement()) {
            statement.execute(sql);
            logger.info("Initialization completed successfully");
        } catch (SQLException e) {
            logger.warn("Exception was thrown!", e);
        }
    }
}
