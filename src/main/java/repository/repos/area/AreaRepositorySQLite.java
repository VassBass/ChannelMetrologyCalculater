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
     * The getAll function returns a collection of all the areas in the repository.
     * 
     *
     *
     * @return A list of all the areas in the database
     *
     * @docauthor Trelent
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
     * The add function adds a new object to the repository.
     * 
     *
     * @param @Nonnull String object Pass the object to be added
     *
     * @return True if the object was added successfully, false otherwise
     *
     * @docauthor Trelent
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
     * The addAll function adds all the areas in a collection to the repository.
     * 
     *
     * @param @Nonnull Collection<String> areas Check if the collection contains any null values
     *
     * @return True if the objects was added successfully, false otherwise
     *
     * @docauthor Trelent
     */
    @Override
    public boolean addAll(@Nonnull Collection<String> areas) {
        Set<String> old = new LinkedHashSet<>(getAll());
        old.addAll(areas.stream().filter(Objects::nonNull).collect(toList()));
        return rewrite(old);
    }

    
    /**
     * The set function is used to update the value of an existing key in the repository.
     * 
     *
     * @param @Nonnull String oldObject Find the old object in the database
     * @param @Nonnull String newObject Set the new value for the area
    
     *
     * @return True if the object is updated in the database
     *
     * @docauthor Trelent
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
     * The remove function removes an area from the repository.
     * 
     *
     * @param @Nonnull String object Specify the object to be removed from the database
     *
     * @return True if the object is removed from the database
     *
     * @docauthor Trelent
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
     * The clear function deletes all the rows in the table.
     * 
     *
     *
     * @return True if the rows is removed from the database
     *
     * @docauthor Trelent
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
     * The rewrite function is used to rewrite the entire table with a new list of values.
     * 
     *
     * @param @Nonnull Collection<String> newList Pass the new list of strings to be written
     *
     * @return True if the rewrite was successful, and false otherwise
     *
     * @docauthor Trelent
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