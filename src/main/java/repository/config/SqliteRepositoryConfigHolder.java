package repository.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.init.reg.RepositoryTableRegistrar;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static org.apache.commons.lang3.StringUtils.EMPTY;

public class SqliteRepositoryConfigHolder implements RepositoryConfigHolder {
    private static final Logger logger = LoggerFactory.getLogger(SqliteRepositoryConfigHolder.class);

    private static final String PROPERTIES_FILE_PATH = "properties/repository.properties";
    private static final String KEY_DB_URL = "jdbc.sqlite.url";
    private static final String KEY_DB_FILE = "jdbc.sqlite.file";

    private String dbUrl = EMPTY;
    private String dbFile = EMPTY;

    public SqliteRepositoryConfigHolder() {
        this(PROPERTIES_FILE_PATH);
    }

    public SqliteRepositoryConfigHolder(String propertiesFile) {
        try {
            InputStream in = SqliteRepositoryConfigHolder.class.getClassLoader()
                    .getResourceAsStream(propertiesFile);
            if (in == null){
                logger.warn("Couldn't find property file");
            }else {
                Properties properties = new Properties();
                properties.load(in);

                dbUrl = properties.getProperty(KEY_DB_URL, EMPTY);
                dbFile = properties.getProperty(KEY_DB_FILE, EMPTY);
            }
        } catch (IOException e) {
            logger.warn("Exception was thrown: ",e);
        }
    }

    @Override
    public String getDBUrl() {
        return dbUrl;
    }

    @Override
    public String getUser() {
        return EMPTY;
    }

    @Override
    public String getPassword() {
        return EMPTY;
    }

    @Override
    public String getDBFile() {
        return dbFile;
    }

    @Override
    public String getTableName(Class<?> clazz) {
        String name = RepositoryTableRegistrar.getRegisteredTables().get(clazz);
        if (name == null) {
            logger.warn(String.format("Table name for %s not registered", clazz.getName()));
            return EMPTY;
        } else return name;
    }
}
