package repository.init;

import localization.Messages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.config.RepositoryConfigHolder;
import repository.connection.RepositoryDBConnector;
import repository.repos.calculation_method.CalculationMethodRepository;

import java.sql.SQLException;
import java.sql.Statement;

public class CalculationMethodRepositoryInitializer extends RepositoryInitializer {
    private static final Logger logger = LoggerFactory.getLogger(CalculationMethodRepositoryInitializer.class);

    public CalculationMethodRepositoryInitializer(RepositoryConfigHolder configHolder, RepositoryDBConnector connector) {
        super(configHolder, connector);
    }

    @Override
    public void init() {
        super.init();

        String tableName = configHolder.getTableName(CalculationMethodRepository.class);

        String sql = String.format("CREATE TABLE IF NOT EXISTS %s ("
                + "measurement_name text NOT NULL UNIQUE"
                + ", method_name text NOT NULL"
                + ", PRIMARY KEY (measurement_name)"
                + ");", tableName);

        try (Statement statement = connector.getStatement()) {
            statement.execute(sql);
            logger.info(Messages.Log.INIT_SUCCESS);
        } catch (SQLException e) {
            logger.warn(Messages.Log.EXCEPTION_THROWN, e);
        }
    }
}
