package service.repository.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.repository.init.reg.RepositoryTableRegistrar;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class SqliteRepositoryConfigHolder implements RepositoryConfigHolder {
    private static final Logger logger = LoggerFactory.getLogger(SqliteRepositoryConfigHolder.class);

    private static final String PROPERTIES_FILE_PATH = "properties/repository.properties";
    private static final String KEY_DB_URL = "jdbc.sqlite.url";

    private String dbUrl;

    public SqliteRepositoryConfigHolder() {
        try {
            InputStream in = SqliteRepositoryConfigHolder.class.getClassLoader()
                    .getResourceAsStream(PROPERTIES_FILE_PATH);
            if (in == null){
                logger.warn("Couldn't find property file");
            }else {
                Properties properties = new Properties();
                properties.load(in);

                dbUrl = properties.getProperty(KEY_DB_URL, "");
            }
        } catch (IOException e) {
            logger.warn("Exception was thrown: ",e);
        } finally {
            if (dbUrl == null) dbUrl = "";
        }
    }

    @Override
    public String getDBUrl() {
        return dbUrl;
    }

    @Override
    public String getUser() {
        return "";
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public String getTableName(Class<?> clazz) {
        return RepositoryTableRegistrar.getRegisteredTables().get(clazz);
    }
}
