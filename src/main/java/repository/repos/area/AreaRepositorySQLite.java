package repository.repos.area;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.config.RepositoryConfigHolder;
import repository.connection.RepositoryDBConnector;
import repository.init.AreaRepositoryInitializer;

import javax.annotation.Nonnull;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import static java.util.stream.Collectors.toList;

public class AreaRepositorySQLite implements AreaRepository {
    private static final Logger logger = LoggerFactory.getLogger(AreaRepositorySQLite.class);

    private final String tableName;
    private final RepositoryDBConnector connector;

    public AreaRepositorySQLite(RepositoryConfigHolder configHolder, RepositoryDBConnector connector) {
        this.tableName = configHolder.getTableName(AreaRepository.class);
        this.connector = connector;
        new AreaRepositoryInitializer(configHolder, connector).init();
    }

    /**
     * @return List of areas or empty list if something go wrong
     */
    @Override
    public Collection<String> getAll() {
        List<String>areas = new ArrayList<>();
        String sql = String.format("SELECT * FROM %s;", tableName);

        try (ResultSet resultSet = connector.getResultSet(sql)){
            while (resultSet.next()){
                areas.add(resultSet.getString("area"));
            }
        } catch (SQLException e) {
            logger.warn("Exception was thrown!", e);
        }

        return areas;
    }

    /**
     * @param object area for adding
     * @return true if area was added or false if not
     */
    @Override
    public boolean add(@Nonnull String object) {
        String sql = String.format("INSERT INTO %s VALUES ('%s');", tableName, object);
        try (Statement statement = connector.getStatement()){
            return statement.executeUpdate(sql) > 0;
        }catch (SQLException e){
            return false;
        }
    }

    /**
     * Add areas from @param if there are not exists in DB
     * @param areas need to be added
     * @return true if areas was added or false if not
     */
    @Override
    public boolean addAll(@Nonnull Collection<String> areas) {
        Set<String> old = new LinkedHashSet<>(getAll());
        old.addAll(areas.stream().filter(Objects::nonNull).collect(toList()));
        return rewrite(old);
    }

    /**
     * @param oldObject to be replaced
     * @param newObject for replace
     * @return true if replace was successful or false if not
     */
    @Override
    public boolean set(@Nonnull String oldObject, @Nonnull String newObject) {
        String sql = String.format("UPDATE %s SET area = '%s' WHERE area = '%s';", tableName, newObject, oldObject);
        try (Statement statement = connector.getStatement()){
            return statement.executeUpdate(sql) > 0;
        }catch (SQLException e){
            return false;
        }
    }

    /**
     * @param object for delete
     * @return true if delete was successful or false if not
     */
    @Override
    public boolean remove(@Nonnull String object) {
        String sql = String.format("DELETE FROM %s WHERE area = '%s';", tableName, object);
        try (Statement statement = connector.getStatement()){
            return statement.executeUpdate(sql) > 0;
        }catch (SQLException e){
            logger.warn("Exception was thrown!", e);
            return false;
        }
    }

    /**
     * Deletes all areas from DB
     * @return true if delete was successful or false if not
     */
    @Override
    public boolean clear() {
        String sql = String.format("DELETE FROM %s;", tableName);
        try (Statement statement = connector.getStatement()){
            statement.execute(sql);
            return true;
        }catch (SQLException e){
            logger.warn("Exception was thrown!", e);
            return false;
        }
    }

    /**
     * Rewrites all areas in DB
     * @param newList for rewriting
     * @return true if rewrite was successful or false if not
     */
    @Override
    public boolean rewrite(@Nonnull Collection<String> newList) {
        String sql = String.format("DELETE FROM %s;", tableName);
        try (Statement statement = connector.getStatement()){
            statement.execute(sql);

            if (!newList.isEmpty()) {
                String insertSql = String.format("INSERT INTO %s VALUES ", tableName);
                StringBuilder sqlBuilder = new StringBuilder(insertSql);
                for (String a : newList) {
                    if (a == null) continue;
                    String values = String.format("('%s'),", a);
                    sqlBuilder.append(values);
                }
                sqlBuilder.setCharAt(sqlBuilder.length() - 1, ';');
                statement.execute(sqlBuilder.toString());
            }

            return true;
        }catch (SQLException e){
            logger.warn("Exception was thrown!", e);
            return false;
        }
    }
}