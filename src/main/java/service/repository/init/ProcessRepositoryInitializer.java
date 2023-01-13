package service.repository.init;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.repository.repos.process.ProcessRepository;
import service.repository.config.RepositoryConfigHolder;
import service.repository.connection.RepositoryDBConnector;

import java.sql.SQLException;
import java.sql.Statement;

public class ProcessRepositoryInitializer implements RepositoryInitializer {
    private static final Logger logger = LoggerFactory.getLogger(ProcessRepositoryInitializer.class);

    private final RepositoryConfigHolder configHolder;
    private final RepositoryDBConnector connector;

    public ProcessRepositoryInitializer(RepositoryConfigHolder configHolder, RepositoryDBConnector connector) {
        this.configHolder = configHolder;
        this.connector = connector;
    }

    @Override
    public void init() {
        String tableName = configHolder.getTableName(ProcessRepository.class);

        String sql = String.format("CREATE TABLE IF NOT EXISTS %s (" +
                "process text NOT NULL UNIQUE" +
                ", PRIMARY KEY (\"process\")" +
                ");", tableName);

        try (Statement statement = connector.getStatement()) {
            statement.execute(sql);
            logger.info("Initialization completed successfully");
        } catch (SQLException e) {
            logger.warn("Exception was thrown!", e);
        }
    }
}
