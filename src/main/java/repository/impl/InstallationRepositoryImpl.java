package repository.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.InstallationRepository;
import repository.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class InstallationRepositoryImpl extends Repository implements InstallationRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(InstallationRepository.class);

    public InstallationRepositoryImpl(){
        setPropertiesFromFile();
        createTable();
    }

    public InstallationRepositoryImpl(String dbUrl, String dbUser, String dbPassword){
        setProperties(dbUrl, dbUser, dbPassword);
        createTable();
    }

    /**
     * Creates table "installations" if it not exists
     */
    @Override
    public void createTable(){
        String sql = "CREATE TABLE IF NOT EXISTS installations (installation text NOT NULL UNIQUE, PRIMARY KEY (\"installation\"));";
        try (Statement statement = getStatement()){
            statement.execute(sql);
        } catch (SQLException e) {
            LOGGER.warn("Exception was thrown!", e);
        }
    }

    /**
     * @return List of installations or empty list if something go wrong
     */
    @Override
    public ArrayList<String> getAll() {
        ArrayList<String>installations = new ArrayList<>();
        String sql = "SELECT * FROM installations;";

        LOGGER.info("Reading all installations from DB");
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
    public boolean add(String object) {
        if (object == null) return false;

        String sql = "INSERT INTO installations (installation) VALUES ('" + object + "');";
        try (Statement statement = getStatement()){
            int result = statement.executeUpdate(sql);
            if (result > 0) LOGGER.info("Installation = {} was added successfully", object);
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

        String sql = "UPDATE installations SET installation = '" + newObject + "' WHERE installation = '" + oldObject + "';";
        try (Statement statement = getStatement()){
            int result = statement.executeUpdate(sql);
            if (result > 0) LOGGER.info("Installation = {} was replaced by installation = {} successfully", oldObject, newObject);
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

        String sql = "DELETE FROM installations WHERE installation = '" + object + "';";
        try (Statement statement = getStatement()){
            int result = statement.executeUpdate(sql);
            if (result > 0) LOGGER.info("Installation = {} was removed successfully", object);
            return true;
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
            LOGGER.info("Installations list in DB was cleared successfully");
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
    public boolean rewrite(ArrayList<String> newList) {
        if (newList == null) return false;

        String sql = "DELETE FROM installations;";
        try (Statement statement = getStatement()){
            statement.execute(sql);
            LOGGER.info("Installations list in DB was cleared successfully");

            if (!newList.isEmpty()) {
                StringBuilder sqlBuilder = new StringBuilder();
                sqlBuilder.append("INSERT INTO installations(installation) VALUES ");
                for (String a : newList) {
                    sqlBuilder.append("('").append(a).append("'),");
                }
                sqlBuilder.setCharAt(sqlBuilder.length() - 1, ';');

                statement.execute(sqlBuilder.toString());
            }

            LOGGER.info("The list of old installations has been rewritten to the new one:\n{}", newList);
            return true;
        }catch (SQLException e){
            LOGGER.warn("Exception was thrown!", e);
            return false;
        }
    }
}