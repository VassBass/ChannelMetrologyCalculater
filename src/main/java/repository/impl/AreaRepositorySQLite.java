package repository.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.AreaRepository;
import repository.RepositoryJDBC;

import javax.annotation.Nonnull;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class AreaRepositorySQLite extends RepositoryJDBC implements AreaRepository {
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
    public Collection<String> getAll() {
        List<String>areas = new ArrayList<>();
        String sql = "SELECT * FROM areas;";

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
    public boolean add(@Nonnull String object) {
        String sql = "INSERT INTO areas VALUES ('" + object + "');";
        try (Statement statement = getStatement()){
            return statement.executeUpdate(sql) > 0;
        }catch (SQLException e){
            return false;
        }
    }

    @Override
    public boolean add(@Nonnull Collection<String> areas) {
        Set<String> old = new LinkedHashSet<>(getAll());
        old.addAll(areas);
        return rewrite(old);
    }

    /**
     * @param oldObject to be replaced
     * @param newObject for replace
     * @return true if replace was successful or false if not
     */
    @Override
    public boolean set(@Nonnull String oldObject, @Nonnull String newObject) {
        String sql = "UPDATE areas SET area = '" + newObject + "' WHERE area = '" + oldObject + "';";
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
        String sql = "DELETE FROM areas WHERE area = '" + object + "';";
        try (Statement statement = getStatement()){
            return statement.executeUpdate(sql) > 0;
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
    public boolean rewrite(@Nonnull Collection<String> newList) {
        String sql = "DELETE FROM areas;";
        try (Statement statement = getStatement()){
            statement.execute(sql);

            if (!newList.isEmpty()) {
                StringBuilder sqlBuilder = new StringBuilder();
                sqlBuilder.append("INSERT INTO areas VALUES ");
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
}