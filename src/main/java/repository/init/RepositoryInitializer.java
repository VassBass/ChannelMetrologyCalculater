package repository.init;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.config.RepositoryConfigHolder;
import repository.connection.RepositoryDBConnector;

import java.io.File;
import java.io.IOException;

public abstract class RepositoryInitializer {
    private static final Logger logger = LoggerFactory.getLogger(RepositoryInitializer.class);

    protected final RepositoryConfigHolder configHolder;
    protected final RepositoryDBConnector connector;

    public RepositoryInitializer(RepositoryConfigHolder configHolder, RepositoryDBConnector connector) {
        this.configHolder = configHolder;
        this.connector = connector;
    }

    public void init() {
        String fileName = configHolder.getDBFile();
        File dbFile = new File(fileName);
        try {
            if (dbFile.createNewFile())
                logger.info(String.format("%s was created successfully", fileName));
        } catch (IOException e) {
            logger.warn("Exception was thrown!", e);
        }
    }
}
