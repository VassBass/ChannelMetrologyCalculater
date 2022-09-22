package repository.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.PathElementRepository;
import repository.RepositoryJDBC;

import javax.annotation.Nonnull;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class ProcessRepositorySQLite extends RepositoryJDBC implements PathElementRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProcessRepositorySQLite.class);
    private static ProcessRepositorySQLite instance;

    public ProcessRepositorySQLite(){
        setPropertiesFromFile();
        createTable();
    }

    public ProcessRepositorySQLite(String dbUrl, String dbUser, String dbPassword){
        setProperties(dbUrl, dbUser, dbPassword);
        createTable();
    }

    public static ProcessRepositorySQLite getInstance() {
        if (instance == null) instance = new ProcessRepositorySQLite();
        return instance;
    }

    /**
     * Creates table "processes" if it not exists
     */
    @Override
    public boolean createTable(){
        String sql = "CREATE TABLE IF NOT EXISTS processes (process text NOT NULL UNIQUE, PRIMARY KEY (\"process\"));";
        try (Statement statement = getStatement()){
            statement.execute(sql);
            return isTableExists("processes");
        } catch (SQLException e) {
            LOGGER.warn("Exception was thrown!", e);
            return false;
        }
    }

    /**
     * @return List of processes or empty list if something go wrong
     */
    @Override
    public Collection<String> getAll() {
        List<String>areas = new ArrayList<>();
        String sql = "SELECT * FROM processes;";

        try (ResultSet resultSet = getResultSet(sql)){

            while (resultSet.next()){
                areas.add(resultSet.getString("process"));
            }

        } catch (SQLException e) {
            LOGGER.warn("Exception was thrown!", e);
        }

        return areas;
    }

    /**
     * @param object process for adding
     * @return true if process was added or false if not
     */
    @Override
    public boolean add(@Nonnull String object) {
        String sql = "INSERT INTO processes VALUES ('" + object + "');";
        try (Statement statement = getStatement()){
            return statement.executeUpdate(sql) > 0;
        }catch (SQLException e){
            return false;
        }
    }

    /**
     * @param oldObject to be replaced
     * @param newObject for replace
     * @return true if replace was successful or false if not
     */
    @Override
    public boolean set(@Nonnull String oldObject, @Nonnull String newObject) {
        String sql = "UPDATE processes SET process = '" + newObject + "' WHERE process = '" + oldObject + "';";
        try (Statement statement = getStatement()){
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
        String sql = "DELETE FROM processes WHERE process = '" + object + "';";
        try (Statement statement = getStatement()){
            return statement.executeUpdate(sql) > 0;
        }catch (SQLException e){
            LOGGER.warn("Exception was thrown!", e);
            return false;
        }
    }

    /**
     * Deletes all processes from DB
     * @return true if delete was successful or false if not
     */
    @Override
    public boolean clear() {
        String sql = "DELETE FROM processes;";
        try (Statement statement = getStatement()){
            statement.execute(sql);
            return true;
        }catch (SQLException e){
            LOGGER.warn("Exception was thrown!", e);
            return false;
        }
    }

    /**
     * Rewrites all processes in DB
     * @param newList for rewriting
     * @return true if rewrite was successful or false if not
     */
    @Override
    public boolean rewrite(@Nonnull Collection<String> newList) {
        String sql = "DELETE FROM processes;";
        try (Statement statement = getStatement()){
            statement.execute(sql);

            if (!newList.isEmpty()) {
                StringBuilder sqlBuilder = new StringBuilder();
                sqlBuilder.append("INSERT INTO processes VALUES ");
                for (String a : newList) {
                    if (a == null) continue;

                    sqlBuilder.append("('").append(a).append("'),");
                }
                sqlBuilder.setCharAt(sqlBuilder.length() - 1, ';');

                statement.execute(sqlBuilder.toString());
            }

            return true;
        }catch (SQLException e){
            LOGGER.warn("Exception was thrown!", e);
            return false;
        }
    }

    @Override
    public boolean add(Collection<String> objects) {
        Set<String> old = new LinkedHashSet<>(getAll());
        old.addAll(objects);
        return rewrite(old);
    }
}