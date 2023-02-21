package service.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.repository.config.RepositoryConfigHolder;
import service.repository.config.SqliteRepositoryConfigHolder;
import service.repository.connection.RepositoryDBConnector;
import service.repository.connection.SqliteRepositoryDBConnector;
import service.application.FactoryImplementationHolder;
import service.application.ImplementationFactory;
import service.application.ServiceExecuter;

public class RepositoryServiceExecuter implements ServiceExecuter {
    private static final Logger logger = LoggerFactory.getLogger(RepositoryServiceExecuter.class);

    private final FactoryImplementationHolder factoryImplementationHolder;

    public RepositoryServiceExecuter(FactoryImplementationHolder factoryImplementationHolder) {
        this.factoryImplementationHolder = factoryImplementationHolder;
    }

    @Override
    public void execute() {
        logger.info(("Start execution of RepositoryService"));
        RepositoryConfigHolder configHolder = new SqliteRepositoryConfigHolder();
        RepositoryDBConnector connector = new SqliteRepositoryDBConnector(configHolder);
        ImplementationFactory factory = new RepositoryImplementationFactory(configHolder, connector);
        factoryImplementationHolder.factoryRegistration(factory);
    }
}
