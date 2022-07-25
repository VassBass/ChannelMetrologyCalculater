package repository.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.Repository;
import repository.RepositoryJDBC;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AreaRepositorySQLite extends RepositoryJDBC implements Repository<String> {
    private static final Logger LOGGER = LoggerFactory.getLogger(AreaRepositorySQLite.class);

    public AreaRepositorySQLite(){
        setPropertiesFromFile();
        createTable();
    }

    public AreaRepositorySQLite(String dbUrl, String dbUser, String dbPassword){
        setProperties(dbUrl, dbUser, dbPassword);
        createTable();
    }

    /**
     * Creates table "areas" if it not exists
     */
    @Override
    public boolean createTable(){
        String sql = "CREATE TABLE IF NOT EXISTS areas (area text NOT NULL UNIQUE, PRIMARY KEY (\"area\"));";
        try (Statement statement = getStatement()){
            statement.execute(sql);
            return isTableExists("areas");
        } catch (SQLException e) {
            LOGGER.warn("Exception was thrown!", e);
            return false;
        }
    }

    /**
     * @return List of areas or empty list if something go wrong
     */
    @Override
    public List<String> getAll() {
        List<String>areas = new ArrayList<>();
        String sql = "SELECT * FROM areas;";

        LOGGER.info("Reading all areas from DB");
        try (ResultSet resultSet = getResultSet(sql)){

            while (resultSet.next()){
                areas.add(resultSet.getString("area"));
            }

        } catch (SQLException e) {
            LOGGER.warn("Exception was thrown!", e);
        }

        return areas;
    }

    /**
     * @param object area for adding
     * @return true if area was added or false if not
     */
    @Override
    public boolean add(String object) {
        if (object == null) return false;

        String sql = "INSERT INTO areas VALUES ('" + object + "');";
        try (Statement statement = getStatement()){
            int result = statement.executeUpdate(sql);
            if (result > 0) LOGGER.info("Area = {} was added successfully", object);
            return true;
        }catch (SQLException e){
            LOGGER.warn("Exception was thrown!", e);
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
        if (oldObject.equals(newObject)) return true;

        String sql = "UPDATE areas SET area = '" + newObject + "' WHERE area = '" + oldObject + "';";
        try (Statement statement = getStatement()){
            int result = statement.executeUpdate(sql);
            if (result > 0) LOGGER.info("Area = {} was replaced by area = {} successfully", oldObject, newObject);
            return true;
        }catch (SQLException e){
            LOGGER.warn("Exception was thrown!", e);
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

        String sql = "DELETE FROM areas WHERE area = '" + object + "';";
        try (Statement statement = getStatement()){
            int result = statement.executeUpdate(sql);
            if (result > 0) LOGGER.info("Area = {} was removed successfully", object);
            return true;
        }catch (SQLException e){
            LOGGER.warn("Exception was thrown!", e);
            return false;
        }
    }

    /**
     * Deletes all areas from DB
     * @return true if delete was successful or false if not
     */
    @Override
    public boolean clear() {
        String sql = "DELETE FROM areas;";
        try (Statement statement = getStatement()){
            statement.execute(sql);
            LOGGER.info("Areas list in DB was cleared successfully");
            return true;
        }catch (SQLException e){
            LOGGER.warn("Exception was thrown!", e);
            return false;
        }
    }

    /**
     * Rewrites all areas in DB
     * @param newList for rewriting
     * @return true if rewrite was successful or false if not
     */
    @Override
    public boolean rewrite(List<String> newList) {
        if (newList == null) return false;

        String sql = "DELETE FROM areas;";
        try (Statement statement = getStatement()){
            statement.execute(sql);
            LOGGER.info("Areas list in DB was cleared successfully");

            if (!newList.isEmpty()) {
                StringBuilder sqlBuilder = new StringBuilder();
                sqlBuilder.append("INSERT INTO areas VALUES ");
                for (String a : newList) {
                    sqlBuilder.append("('").append(a).append("'),");
                }
                sqlBuilder.setCharAt(sqlBuilder.length() - 1, ';');

                statement.execute(sqlBuilder.toString());
            }

            LOGGER.info("The list of old areas has been rewritten to the new one:\n{}", newList);
            return true;
        }catch (SQLException e){
            LOGGER.warn("Exception was thrown!", e);
            return false;
        }
    }
}