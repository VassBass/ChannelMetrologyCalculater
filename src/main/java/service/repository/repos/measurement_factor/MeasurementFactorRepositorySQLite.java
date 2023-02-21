package service.repository.repos.measurement_factor;

import model.dto.MeasurementTransformFactor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.repository.config.RepositoryConfigHolder;
import service.repository.connection.RepositoryDBConnector;
import service.repository.init.MeasurementFactorsRepositoryInitializer;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

public class MeasurementFactorRepositorySQLite implements MeasurementFactorRepository {
    private static final Logger logger = LoggerFactory.getLogger(MeasurementFactorRepositorySQLite.class);

    private final RepositoryDBConnector connector;
    private final String tableName;

    public MeasurementFactorRepositorySQLite(RepositoryConfigHolder configHolder, RepositoryDBConnector connector){
        this.connector = connector;
        this.tableName = configHolder.getTableName(MeasurementFactorRepository.class);
        new MeasurementFactorsRepositoryInitializer(configHolder, connector).init();
    }

    @Override
    public Collection<MeasurementTransformFactor> getAll() {
        Collection<MeasurementTransformFactor> factors = new ArrayList<>();
        String sql = String.format("SELECT * FROM %s;", tableName);
        try (ResultSet resultSet = connector.getResultSet(sql)) {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String source = resultSet.getString("source");
                String result = resultSet.getString("result");
                double factor = resultSet.getDouble("factor");

                factors.add(new MeasurementTransformFactor(id, source, result, factor));
            }
        } catch (SQLException e) {
            logger.warn("Exception was thrown!", e);
        }
        return factors;
    }

    @Override
    public MeasurementTransformFactor getById(@Nonnegative int id) {
        String sql = String.format("SELECT * FROM %s WHERE id = %s LIMIT 1;", tableName, id);
        try (ResultSet resultSet = connector.getResultSet(sql)) {
            if (resultSet.next()) {
                String source = resultSet.getString("source");
                String result = resultSet.getString("result");
                double factor = resultSet.getDouble("factor");

                return new MeasurementTransformFactor(id, source, result, factor);
            }
        } catch (SQLException e) {
            logger.warn("Exception was thrown!", e);
        }
        return null;
    }

    @Override
    public Collection<MeasurementTransformFactor> getBySource(@Nonnull String source) {
        Collection<MeasurementTransformFactor> factors = new ArrayList<>();
        String sql = String.format("SELECT * FROM %s WHERE source = '%s';", tableName, source);
        try (ResultSet resultSet = connector.getResultSet(sql)) {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String result = resultSet.getString("result");
                double factor = resultSet.getDouble("factor");

                factors.add(new MeasurementTransformFactor(id, source, result, factor));
            }
        } catch (SQLException e) {
            logger.warn("Exception was thrown!", e);
        }
        return factors;
    }

    @Override
    public Collection<MeasurementTransformFactor> getByResult(@Nonnull String result) {
        Collection<MeasurementTransformFactor> factors = new ArrayList<>();
        String sql = String.format("SELECT * FROM %s WHERE result = '%s';", tableName, result);
        try (ResultSet resultSet = connector.getResultSet(sql)) {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String source = resultSet.getString("source");
                double factor = resultSet.getDouble("factor");

                factors.add(new MeasurementTransformFactor(id, source, result, factor));
            }
        } catch (SQLException e) {
            logger.warn("Exception was thrown!", e);
        }
        return factors;
    }

    @Override
    public int add(@Nonnull String source, @Nonnull String result, double factor) {
        int id = MeasurementFactorIdGenerator.generateForRepository(this);
        String sql = String.format("INSERT INTO %s (id, source, result, factor) VALUES (?, ?, ?, ?);", tableName);
        try (PreparedStatement statement = connector.getPreparedStatement(sql)) {
            statement.setInt(1, id);
            statement.setString(2, source);
            statement.setString(3, result);
            statement.setDouble(4, factor);

            statement.executeUpdate();
            return id;
        } catch (SQLException e) {
            logger.warn("Exception was thrown!", e);
        }
        return -1;
    }

    @Override
    public boolean set(@Nonnull MeasurementTransformFactor mtf) {
        String sql = String.format("UPDATE %s SET source = ?, result = ?, factor = ? WHERE id = ?;", tableName);
        try (PreparedStatement statement = connector.getPreparedStatement(sql)) {
            statement.setString(1, mtf.getTransformFrom());
            statement.setString(2, mtf.getTransformTo());
            statement.setDouble(3, mtf.getTransformFactor());
            statement.setInt(4, mtf.getId());

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.warn("Exception was thrown!", e);
            return false;
        }
    }

    @Override
    public boolean rewrite(@Nonnull Collection<MeasurementTransformFactor> mtf) {
        String sql = String.format("DELETE FROM %s;", tableName);
            try (Statement statement = connector.getStatement()) {
                statement.execute(sql);

                if (!mtf.isEmpty()) {
                    sql = String.format("INSERT INTO %s (id, source, result, factor) VALUES ", tableName);
                    StringBuilder sqlBuilder = new StringBuilder(sql);
                    for (MeasurementTransformFactor m : mtf) {
                        if (m == null) continue;

                        String values = String.format("(%s, '%s', '%s', %s),",
                                m.getId(), m.getTransformFrom(), m.getTransformTo(), m.getTransformFactor());
                        sqlBuilder.append(values);
                    }
                    sqlBuilder.setCharAt(sqlBuilder.length() - 1, ';');
                    return statement.executeUpdate(sqlBuilder.toString()) > 0;
                } else return true;
            } catch (SQLException e) {
                logger.warn("Exception was thrown!", e);
                return false;
            }
    }

    @Override
    public boolean changeAllSources(@Nonnull String oldValue, @Nonnull String newValue) {
        String sql = String.format("UPDATE %s SET source = '%s' WHERE source = '%s';", tableName, newValue, oldValue);
        try (Statement statement = connector.getStatement()) {
            statement.executeUpdate(sql);
            return true;
        } catch (SQLException e) {
            logger.warn("Exception was thrown!", e);
            return false;
        }
    }

    @Override
    public boolean changeAllResults(@Nonnull String oldValue, @Nonnull String newValue) {
        String sql = String.format("UPDATE %s SET result = '%s' WHERE result = '%s';", tableName, newValue, oldValue);
        try (Statement statement = connector.getStatement()) {
            statement.executeUpdate(sql);
            return true;
        } catch (SQLException e) {
            logger.warn("Exception was thrown!", e);
            return false;
        }
    }

    @Override
    public boolean changeFactor(int id, double factor) {
        String sql = String.format("UPDATE %s SET factor = %s WHERE id = %s", tableName, factor, id);
        try (Statement statement = connector.getStatement()) {
            return statement.executeUpdate(sql) > 0;
        } catch (SQLException e) {
            logger.warn("Exception was thrown!", e);
            return false;
        }
    }

    @Override
    public boolean removeById(int id) {
        String sql = String.format("DELETE FROM %s WHERE id = %s;", tableName, id);
        try (Statement statement = connector.getStatement()) {
            return statement.executeUpdate(sql) > 0;
        } catch (SQLException e) {
            logger.warn("Exception was thrown!", e);
            return false;
        }
    }

    @Override
    public boolean removeBySource(@Nonnull String source) {
        String sql = String.format("DELETE FROM %s WHERE source = '%s';", tableName, source);
        try (Statement statement = connector.getStatement()) {
            statement.executeUpdate(sql);
            return true;
        } catch (SQLException e) {
            logger.warn("Exception was thrown!", e);
            return false;
        }
    }

    @Override
    public boolean removeByResult(@Nonnull String result) {
        String sql = String.format("DELETE FROM %s WHERE result = '%s';", tableName, result);
        try (Statement statement = connector.getStatement()) {
            statement.executeUpdate(sql);
            return true;
        } catch (SQLException e) {
            logger.warn("Exception was thrown!", e);
            return false;
        }
    }
}
