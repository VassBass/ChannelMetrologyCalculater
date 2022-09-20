package repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.swing.*;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public abstract class RepositoryJDBC {
    private static final Logger LOGGER = LoggerFactory.getLogger(RepositoryJDBC.class);

    private static final String ERROR = "Помилка";

    private static String dbUrl, dbUser, dbPassword;

    private static boolean propertiesWasLoaded = false;

    /**
     * Gets url of db from jdbc.properties file
     *
     * @see #dbUrl
     * @see #dbUser
     * @see #dbPassword
     */
    public void setPropertiesFromFile(){
        if (propertiesWasLoaded) return;

        try {
            String propertiesFileName = "jdbc.properties";
            InputStream in = RepositoryJDBC.class.getClassLoader().getResourceAsStream(propertiesFileName);
            if (in == null){
                LOGGER.warn("Couldn't find property file");
                String message = "Database connection error. Want to try again?";
                int result = JOptionPane.showConfirmDialog(null, message, ERROR, JOptionPane.OK_CANCEL_OPTION);
                if (result == 0){
                    setPropertiesFromFile();
                }else {
                    System.exit(0);
                }
            }else {
                LOGGER.info("Properties file was found. Trying to read it.");
                Properties properties = new Properties();
                properties.load(in);

                dbUrl = properties.getProperty("jdbc.URL");
                dbUser = properties.getProperty("jdbc.USER");
                dbPassword = properties.getProperty("jdbc.PASSWORD");

                LOGGER.info("Properties file was read successful.");
                LOGGER.debug("dbUrl = {}\ndbUser = {}\ndbPassword = {}", dbUrl, dbUser, dbPassword);
            }
        } catch (IOException e) {
            LOGGER.warn("Exception was thrown: ",e);
            String message = "Database connection error. Want to try again?";
            int result = JOptionPane.showConfirmDialog(null, message, ERROR, JOptionPane.OK_CANCEL_OPTION);
            if (result == 0){
                setPropertiesFromFile();
            }else {
                System.exit(0);
            }
        }

        propertiesWasLoaded = true;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(dbUrl, dbUser,dbPassword);
    }

    public Statement getStatement() throws SQLException {
        return getConnection().createStatement();
    }

    public ResultSet getResultSet(@Nonnull String sql) throws SQLException {
        return getStatement().executeQuery(sql);
    }

    public PreparedStatement getPreparedStatement(@Nonnull String sql) throws SQLException {
        return getConnection().prepareStatement(sql);
    }

    public PreparedStatement getPreparedStatementWithKey(@Nonnull String sql) throws SQLException {
        return getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
    }

    public void setProperties(@Nonnull String url, @Nullable String user, @Nullable String password){
        dbUrl = url;
        dbUser = user;
        dbPassword = password;
    }

    public boolean isTableExists(@Nonnull String tableName) throws SQLException {
        try (Connection connection = getConnection()) {
            DatabaseMetaData dbm = connection.getMetaData();
            try (ResultSet result = dbm.getTables(null, null, tableName, null)) {
                return result.next();
            }
        }
    }
}
