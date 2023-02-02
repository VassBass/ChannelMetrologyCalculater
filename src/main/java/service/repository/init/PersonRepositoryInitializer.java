package service.repository.init;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.repository.repos.person.PersonRepository;
import service.repository.config.RepositoryConfigHolder;
import service.repository.connection.RepositoryDBConnector;

import java.sql.SQLException;
import java.sql.Statement;

public class PersonRepositoryInitializer implements RepositoryInitializer {
    private static final Logger logger = LoggerFactory.getLogger(PersonRepositoryInitializer.class);

    private final RepositoryConfigHolder configHolder;
    private final RepositoryDBConnector connector;

    public PersonRepositoryInitializer(RepositoryConfigHolder configHolder, RepositoryDBConnector connector) {
        this.configHolder = configHolder;
        this.connector = connector;
    }

    @Override
    public void init() {
        String tableName = configHolder.getTableName(PersonRepository.class);

        String sql = String.format("CREATE TABLE IF NOT EXISTS %s (" +
                "id integer NOT NULL UNIQUE" +
                ", name text NOT NULL" +
                ", surname text NOT NULL" +
                ", patronymic text" +
                ", position text NOT NULL" +
                ", PRIMARY KEY (id)" +
                ");", tableName);

        try (Statement statement = connector.getStatement()) {
            statement.execute(sql);
            logger.info("Initialization completed successfully");
        } catch (SQLException e) {
            logger.warn("Exception was thrown!", e);
        }
    }
}
