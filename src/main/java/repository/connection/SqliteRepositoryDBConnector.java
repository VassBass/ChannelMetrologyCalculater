package repository.connection;

import repository.config.RepositoryConfigHolder;

import javax.annotation.Nonnull;
import java.sql.*;

public class SqliteRepositoryDBConnector implements RepositoryDBConnector {
    private final RepositoryConfigHolder configHolder;

    
    /**
     * The SqliteRepositoryDBConnector function is used to connect the SqliteRepository class to the database.
     * 
     *
     * @param RepositoryConfigHolder configHolder Hold the configuration of the database
     *
     * @return A sqliterepositorydbconnector object
     *
     * @docauthor Trelent
     */
    public SqliteRepositoryDBConnector(RepositoryConfigHolder configHolder) {
        this.configHolder = configHolder;
    }

    
    /**
     * The getConnection function returns a connection to the database.
     * 
     *
     *
     * @return A connection to the database
     *
     * @docauthor Trelent
     */
    public Connection getConnection() throws SQLException {
        String dbUrl = configHolder.getDBUrl();
        return DriverManager.getConnection(dbUrl);
    }

    
    /**
     * The getStatement function returns a Statement object that is used to execute SQL queries.
     * 
     *
     *
     * @return A statement object
     *
     * @docauthor Trelent
     */
    public Statement getStatement() throws SQLException {
        return getConnection().createStatement();
    }

    
    /**
     * The getResultSet function is used to execute a SQL query and return the result set.
     * 
     *
     * @param @Nonnull String sql Ensure that the sql parameter is not null
     *
     * @return A resultset object
     *
     * @docauthor Trelent
     */
    public ResultSet getResultSet(@Nonnull String sql) throws SQLException {
        return getStatement().executeQuery(sql);
    }

    
    /**
     * The getPreparedStatement function is used to create a PreparedStatement object.
     * 
     *
     * @param @Nonnull String sql Pass the sql statement to be executed
     *
     * @return A preparedstatement object
     *
     * @docauthor Trelent
     */
    public PreparedStatement getPreparedStatement(@Nonnull String sql) throws SQLException {
        return getConnection().prepareStatement(sql);
    }

    
    /**
     * The getPreparedStatementWithKey function is used to create a PreparedStatement object that can be used to
     * execute an SQL statement and retrieve the generated keys.
     * 
     *
     * @param @Nonnull String sql Make sure that the sql parameter is not null
     *
     * @return A prepared statement that can be used to execute the given sql
     *
     * @docauthor Trelent
     */
    public PreparedStatement getPreparedStatementWithKey(@Nonnull String sql) throws SQLException {
        return getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
    }
}
