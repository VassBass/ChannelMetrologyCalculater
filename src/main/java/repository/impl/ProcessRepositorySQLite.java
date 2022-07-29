package repository.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.Repository;
import repository.RepositoryJDBC;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ProcessRepositorySQLite extends RepositoryJDBC implements Repository<String> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProcessRepositorySQLite.class);

    public ProcessRepositorySQLite(){
        setPropertiesFromFile();
        createTable();
    }

    public ProcessRepositorySQLite(String dbUrl, String dbUser, String dbPassword){
        setProperties(dbUrl, dbUser, dbPassword);
        createTable();
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

        LOGGER.info("Reading all processes from DB");
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
    public boolean add(String object) {
        if (object == null) return false;

        String sql = "INSERT INTO processes VALUES ('" + object + "');";
        try (Statement statement = getStatement()){
            int result = statement.executeUpdate(sql);
            if (result > 0){
                LOGGER.info("Process = {} was added successfully", object);
                return true;
            } else return false;
        }catch (SQLException e){
            LOGGER.info("Process = {} is already exists", object);
            return false;
        }
    }

    /**
     * @param oldObject to be replaced
     * @param newObject for replace
     * @return true if replace was successful or false if not
     */
    @Override
    public boolean set(String oldObject, String newObject) {
        if (oldObject == null || newObject == null) return false;

        String sql = "UPDATE processes SET process = '" + newObject + "' WHERE process = '" + oldObject + "';";
        try (Statement statement = getStatement()){
            int result = statement.executeUpdate(sql);
            if (result > 0) {
                LOGGER.info("Process = {} was replaced by department = {} successfully", oldObject, newObject);
                return true;
            }else {
                LOGGER.info("Process = {} was not found", oldObject);
                return false;
            }
        }catch (SQLException e){
            LOGGER.info("Process = {} is already exists", newObject);
            return false;
        }
    }

    /**
     * @param object for delete
     * @return true if delete was successful or false if not
     */
    @Override
    public boolean remove(String object) {
        if (object == null) return false;

        String sql = "DELETE FROM processes WHERE process = '" + object + "';";
        try (Statement statement = getStatement()){
            int result = statement.executeUpdate(sql);
            if (result > 0) {
                LOGGER.info("Process = {} was removed successfully", object);
                return true;
            } else {
                LOGGER.info("Process = {} was not found", object);
                return false;
            }
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
            LOGGER.info("Processes list in DB was cleared successfully");
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
    public boolean rewrite(Collection<String> newList) {
        if (newList == null) return false;

        String sql = "DELETE FROM processes;";
        try (Statement statement = getStatement()){
            statement.execute(sql);
            LOGGER.info("Processes list in DB was cleared successfully");

            if (!newList.isEmpty()) {
                StringBuilder sqlBuilder = new StringBuilder();
                sqlBuilder.append("INSERT INTO processes VALUES ");
                for (String a : newList) {
                    sqlBuilder.append("('").append(a).append("'),");
                }
                sqlBuilder.setCharAt(sqlBuilder.length() - 1, ';');

                statement.execute(sqlBuilder.toString());
            }

            LOGGER.info("The list of old processes has been rewritten to the new one:\n{}", newList);
            return true;
        }catch (SQLException e){
            LOGGER.warn("Exception was thrown!", e);
            return false;
        }
    }
}