package repository.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.AreaRepository;
import repository.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class AreaRepositoryImpl extends Repository implements AreaRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(AreaRepository.class);

    public AreaRepositoryImpl(){
        setPropertiesFromFile();
    }

    public AreaRepositoryImpl(String dbUrl, String dbUser, String dbPassword){
        setProperties(dbUrl, dbUser, dbPassword);
    }

    private void createTable(){
        String sql = "CREATE TABLE IF NOT EXISTS areas (area text NOT NULL UNIQUE, PRIMARY KEY (\"area\"));";
        try (Statement statement = getStatement()){
            statement.execute(sql);
        } catch (SQLException e) {
            LOGGER.warn("Exception was thrown!", e);
        }
    }


    /**
     * @return List of areas or empty list if something go wrong
     */
    @Override
    public ArrayList<String> getAll() {
        ArrayList<String>areas = new ArrayList<>();
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
        String sql = "INSERT INTO areas (area) VALUES ('" + object + "');";
        try (Statement statement = getStatement()){
            statement.execute(sql);
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
        String sql = "UPDATE areas SET area = '" + newObject + "' WHERE area = '" + oldObject + "';";
        try (Statement statement = getStatement()){
            statement.execute(sql);
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
        String sql = "DELETE FROM areas WHERE area = '" + object + "';";
        try (Statement statement = getStatement()){
            statement.execute(sql);
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
    public boolean rewrite(ArrayList<String> newList) {
        String sql = "DELETE FROM areas;";
        StringBuilder sqlBuilder = new StringBuilder();
        try (Statement statement = getStatement()){
            statement.execute(sql);

            sqlBuilder.append("INSERT INTO areas(area) VALUES ");
            for (String a : newList){
                sqlBuilder.append("('").append(a).append("'),");
            }
            sqlBuilder.setCharAt(sqlBuilder.length()-1, ';');

            statement.execute(sqlBuilder.toString());
            return true;
        }catch (SQLException e){
            LOGGER.warn("Exception was thrown!", e);
            return false;
        }
    }
}