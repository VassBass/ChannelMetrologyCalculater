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

public class InstallationRepositorySQLite extends RepositoryJDBC implements PathElementRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(InstallationRepositorySQLite.class);
    private static InstallationRepositorySQLite instance;

    public InstallationRepositorySQLite(){
        setPropertiesFromFile();
        createTable();
    }

    public InstallationRepositorySQLite(String dbUrl, String dbUser, String dbPassword){
        setProperties(dbUrl, dbUser, dbPassword);
        createTable();
    }

    public static InstallationRepositorySQLite getInstance(){
        if (instance == null) instance = new InstallationRepositorySQLite();
        return instance;
    }

    /**
     * Creates table "installations" if it not exists
     */
    @Override
    public boolean createTable(){
        String sql = "CREATE TABLE IF NOT EXISTS installations (installation text NOT NULL UNIQUE, PRIMARY KEY (\"installation\"));";
        try (Statement statement = getStatement()){
            statement.execute(sql);
            return isTableExists("installations");
        } catch (SQLException e) {
            LOGGER.warn("Exception was thrown!", e);
            return false;
        }
    }

    /**
     * @return List of installations or empty list if something go wrong
     */
    @Override
    public Collection<String> getAll() {
        List<String>installations = new ArrayList<>();
        String sql = "SELECT * FROM installations;";

        try (ResultSet resultSet = getResultSet(sql)){

            while (resultSet.next()){
                installations.add(resultSet.getString("installation"));
            }

        } catch (SQLException e) {
            LOGGER.warn("Exception was thrown!", e);
        }

        return installations;
    }

    /**
     * @param object installation for adding
     * @return true if installation was added or false if not
     */
    @Override
    public boolean add(@Nonnull String object) {
        String sql = "INSERT INTO installations VALUES ('" + object + "');";
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
        String sql = "UPDATE installations SET installation = '" + newObject + "' WHERE installation = '" + oldObject + "';";
        try (Statement statement = getStatement()){
            return statement.executeUpdate(sql) > 0;
        }catch (SQLException e){
            LOGGER.info("Installation = {} is already exists", newObject);
            return false;
        }
    }

    /**
     * @param object for delete
     * @return true if delete was successful or false if not
     */
    @Override
    public boolean remove(@Nonnull String object) {
        String sql = "DELETE FROM installations WHERE installation = '" + object + "';";
        try (Statement statement = getStatement()){
            return statement.executeUpdate(sql) > 0;
        }catch (SQLException e){
            LOGGER.warn("Exception was thrown!", e);
            return false;
        }
    }

    /**
     * Deletes all installations from DB
     * @return true if delete was successful or false if not
     */
    @Override
    public boolean clear() {
        String sql = "DELETE FROM installations;";
        try (Statement statement = getStatement()){
            statement.execute(sql);
            return true;
        }catch (SQLException e){
            LOGGER.warn("Exception was thrown!", e);
            return false;
        }
    }

    /**
     * Rewrites all installations in DB
     * @param newList for rewriting
     * @return true if rewrite was successful or false if not
     */
    @Override
    public boolean rewrite(@Nonnull Collection<String> newList) {
        String sql = "DELETE FROM installations;";
        try (Statement statement = getStatement()){
            statement.execute(sql);

            if (!newList.isEmpty()) {
                StringBuilder sqlBuilder = new StringBuilder();
                sqlBuilder.append("INSERT INTO installations VALUES ");
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