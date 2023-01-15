package service.repository.config;

public interface RepositoryConfigHolder {
    String getDBUrl();
    String getUser();
    String getPassword();
    String getTableName(Class<?>clazz);
}