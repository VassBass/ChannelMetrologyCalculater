package repository.config;

public interface RepositoryConfigHolder {
    String getDBUrl();
    String getUser();
    String getPassword();
    String getDBFile();
    String getTableName(Class<?>clazz);
}
