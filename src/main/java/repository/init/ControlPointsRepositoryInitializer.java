package repository.init;

import localization.Messages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.config.RepositoryConfigHolder;
import repository.connection.RepositoryDBConnector;
import repository.repos.control_points.ControlPointsRepository;

import java.sql.SQLException;
import java.sql.Statement;

public class ControlPointsRepositoryInitializer extends RepositoryInitializer {
    private static final Logger logger = LoggerFactory.getLogger(ControlPointsRepositoryInitializer.class);

    public ControlPointsRepositoryInitializer(RepositoryConfigHolder configHolder, RepositoryDBConnector connector) {
        super(configHolder, connector);
    }

    @Override
    public void init() {
        super.init();

        String tableName = configHolder.getTableName(ControlPointsRepository.class);

        String sql = String.format("CREATE TABLE IF NOT EXISTS %s ("
                + "name text NOT NULL UNIQUE"
                + ", sensor_type text NOT NULL"
                + ", points text NOT NULL"
                + ", PRIMARY KEY (name)"
                + ");", tableName);

        try (Statement statement = connector.getStatement()) {
            statement.execute(sql);
            logger.info(Messages.Log.INIT_SUCCESS);
        } catch (SQLException e) {
            logger.warn(Messages.Log.EXCEPTION_THROWN, e);
        }
    }
}
