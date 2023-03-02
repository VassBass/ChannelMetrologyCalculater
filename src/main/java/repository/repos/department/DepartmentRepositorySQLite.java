package repository.repos.department;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.config.RepositoryConfigHolder;
import repository.connection.RepositoryDBConnector;
import repository.init.DepartmentRepositoryInitializer;

import javax.annotation.Nonnull;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.stream.Collectors;

public class DepartmentRepositorySQLite implements DepartmentRepository {
    private static final Logger logger = LoggerFactory.getLogger(DepartmentRepositorySQLite.class);

    private final String tableName;
    private final RepositoryDBConnector connector;

    public DepartmentRepositorySQLite(RepositoryConfigHolder configHolder, RepositoryDBConnector connector) {
        this.tableName = configHolder.getTableName(DepartmentRepository.class);
        this.connector = connector;
        new DepartmentRepositoryInitializer(configHolder, connector).init();
    }

    /**
     * @return List of departments or empty list if something go wrong
     */
    @Nonnull
    @Override
    public Collection<String> getAll() {
        List<String>departments = new ArrayList<>();
        String sql = String.format("SELECT * FROM %s;", tableName);

        try (ResultSet resultSet = connector.getResultSet(sql)){
            while (resultSet.next()){
                departments.add(resultSet.getString("department"));
            }
        } catch (SQLException e) {
            logger.warn("Exception was thrown!", e);
        }

        return departments;
    }

    /**
     * @param object department for adding
     * @return true if department was added or false if not
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
     * Add departments from @param if there are not exists in DB
     * @param objects need to be added
     * @return true if departments was added or false if not
     */
    @Override
    public boolean addAll(@Nonnull Collection<String>objects){
        Set<String> old = new LinkedHashSet<>(getAll());
        old.addAll(objects.stream().filter(Objects::nonNull).collect(Collectors.toList()));
        return rewrite(old);
    }

    /**
     * @param oldObject to be replaced
     * @param newObject for replace
     * @return true if replace was successful or false if not
     */
    @Override
    public boolean set(@Nonnull String oldObject, @Nonnull String newObject) {
        String sql = String.format("UPDATE %s SET department = '%s' WHERE department = '%s';", tableName, newObject, oldObject);
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
        String sql = String.format("DELETE FROM %s WHERE department = '%s';", tableName, object);
        try (Statement statement = connector.getStatement()){
            return statement.executeUpdate(sql) > 0;
        }catch (SQLException e){
            logger.warn("Exception was thrown!", e);
            return false;
        }
    }

    /**
     * Deletes all departments from DB
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