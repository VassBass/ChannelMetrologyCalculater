package repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class Repository {
    private static final Logger LOGGER = LoggerFactory.getLogger(Repository.class);

    private static final String ERROR = "Помилка";

    private String dbUrl, dbUser, dbPassword;

    /**
     * Gets url of db from jdbc.properties file
     *
     * @see #dbUrl
     * @see #dbUser
     * @see #dbPassword
     */
    public void setPropertiesFromFile(){
        try {
            String propertiesFileName = "jdbc.properties";
            InputStream in = Repository.class.getClassLoader().getResourceAsStream(propertiesFileName);
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
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(dbUrl, dbUser,dbPassword);
    }

    public Statement getStatement() throws SQLException {
        return getConnection().createStatement();
    }

    public ResultSet getResultSet(String sql) throws SQLException {
        return getStatement().executeQuery(sql);
    }

    public PreparedStatement getPreparedStatement(String sql) throws SQLException {
        return getConnection().prepareStatement(sql);
    }

    public PreparedStatement getPreparedStatementWithKey(String sql) throws SQLException {
        return getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
    }

    public void setProperties(String dbUrl, String dbUser, String dbPassword){
        this.dbUrl = dbUrl;
        this.dbUser = dbUser;
        this.dbPassword = dbPassword;
    }
}
