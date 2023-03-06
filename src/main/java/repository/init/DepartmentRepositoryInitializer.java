package repository.init;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.repos.department.DepartmentRepository;
import repository.config.RepositoryConfigHolder;
import repository.connection.RepositoryDBConnector;

import java.sql.SQLException;
import java.sql.Statement;

public class DepartmentRepositoryInitializer extends RepositoryInitializer {
    private static final Logger logger = LoggerFactory.getLogger(DepartmentRepository.class);

    public DepartmentRepositoryInitializer(RepositoryConfigHolder configHolder, RepositoryDBConnector connector) {
        super(configHolder, connector);
    }

    @Override
    public void init() {
        super.init();

        String tableName = configHolder.getTableName(DepartmentRepository.class);

        String sql = String.format("CREATE TABLE IF NOT EXISTS %s (" +
                "department text NOT NULL UNIQUE" +
                ", PRIMARY KEY (\"department\")" +
                ");", tableName);

        try (Statement statement = connector.getStatement()) {
            statement.execute(sql);
            logger.info("Initialization completed successfully");
        } catch (SQLException e) {
            logger.warn("Exception was thrown!", e);
        }
    }
}