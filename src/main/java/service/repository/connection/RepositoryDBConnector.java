package service.repository.connection;

import javax.annotation.Nonnull;
import java.sql.*;

public interface RepositoryDBConnector {
    Connection getConnection() throws SQLException;
    Statement getStatement() throws SQLException;
    ResultSet getResultSet(@Nonnull String sql) throws SQLException;
    PreparedStatement getPreparedStatement(@Nonnull String sql) throws SQLException;
    PreparedStatement getPreparedStatementWithKey(@Nonnull String sql) throws SQLException;
}
