package mock;

import repository.config.SqliteRepositoryConfigHolder;

public class RepositoryConfigHolderMock extends SqliteRepositoryConfigHolder {
    private static final String TEST_REPOSITORY_PROPERTIES_FILE = "properties/repository_test.properties";

    public RepositoryConfigHolderMock() {
        super(TEST_REPOSITORY_PROPERTIES_FILE);
    }
}
