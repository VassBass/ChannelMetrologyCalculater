package service.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.repository.config.RepositoryConfigHolder;
import service.repository.config.SqliteRepositoryConfigHolder;
import service.repository.connection.RepositoryDBConnector;
import service.repository.connection.SqliteRepositoryDBConnector;
import service.root.ServiceInitializer;

public class RepositoryServiceInitializer implements ServiceInitializer {
    private static final Logger logger = LoggerFactory.getLogger(RepositoryServiceInitializer.class);

    @Override
    public void init() {
        RepositoryConfigHolder configHolder = new SqliteRepositoryConfigHolder();
        RepositoryDBConnector connector = new SqliteRepositoryDBConnector(configHolder);
        RepositoryImplementationFactory.init(configHolder, connector);
        logger.info(("Initialization completed successfully"));
    }
}
