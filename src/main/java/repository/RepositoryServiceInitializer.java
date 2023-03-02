package repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.config.RepositoryConfigHolder;
import repository.config.SqliteRepositoryConfigHolder;
import repository.connection.RepositoryDBConnector;
import repository.connection.SqliteRepositoryDBConnector;
import service.root.ServiceInitializer;

public class RepositoryServiceInitializer implements ServiceInitializer {
    private static final Logger logger = LoggerFactory.getLogger(RepositoryServiceInitializer.class);

    @Override
    public void init() {
        RepositoryConfigHolder configHolder = new SqliteRepositoryConfigHolder();
        RepositoryDBConnector connector = new SqliteRepositoryDBConnector(configHolder);
        RepositoryFactory.init(configHolder, connector);
        logger.info(("Initialization completed successfully"));
    }
}
