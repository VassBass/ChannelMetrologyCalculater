package repository.init;

import localization.Messages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.repos.person.PersonRepository;
import repository.config.RepositoryConfigHolder;
import repository.connection.RepositoryDBConnector;

import java.sql.SQLException;
import java.sql.Statement;

public class PersonRepositoryInitializer extends RepositoryInitializer {
    private static final Logger logger = LoggerFactory.getLogger(PersonRepositoryInitializer.class);

    public PersonRepositoryInitializer(RepositoryConfigHolder configHolder, RepositoryDBConnector connector) {
        super(configHolder, connector);
    }

    @Override
    public void init() {
        super.init();

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
            logger.info(Messages.Log.INIT_SUCCESS);
        } catch (SQLException e) {
            logger.warn(Messages.Log.EXCEPTION_THROWN, e);
        }
    }
}
