package repository.init;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.config.RepositoryConfigHolder;
import repository.connection.RepositoryDBConnector;
import repository.repos.measurement_factor.MeasurementFactorRepository;

import java.sql.SQLException;
import java.sql.Statement;

public class MeasurementFactorsRepositoryInitializer extends RepositoryInitializer {
    private static final Logger logger = LoggerFactory.getLogger(MeasurementFactorsRepositoryInitializer.class);

    public MeasurementFactorsRepositoryInitializer(RepositoryConfigHolder configHolder, RepositoryDBConnector connector) {
        super(configHolder, connector);
    }

    @Override
    public void init() {
        super.init();

        String tableName = configHolder.getTableName(MeasurementFactorRepository.class);

        String sql = String.format("CREATE TABLE IF NOT EXISTS %s (" +
                "id INTEGER NOT NULL UNIQUE" +
                ", source TEXT NOT NULL" +
                ", result TEXT NOT NULL" +
                ", factor REAL NOT NULL" +
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
