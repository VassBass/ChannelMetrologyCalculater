package repository.connection;

import repository.config.RepositoryConfigHolder;

import javax.annotation.Nonnull;
import java.sql.*;

public class SqliteRepositoryDBConnector implements RepositoryDBConnector {
    private final RepositoryConfigHolder configHolder;

    public SqliteRepositoryDBConnector(RepositoryConfigHolder configHolder) {
        this.configHolder = configHolder;
    }

    public Connection getConnection() throws SQLException {
        String dbUrl = configHolder.getDBUrl();
        return DriverManager.getConnection(dbUrl);
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
}
