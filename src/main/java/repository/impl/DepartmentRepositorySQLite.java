package repository.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.Repository;
import repository.RepositoryJDBC;

import javax.annotation.Nonnull;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DepartmentRepositorySQLite extends RepositoryJDBC implements Repository<String> {
    private static final Logger LOGGER = LoggerFactory.getLogger(DepartmentRepositorySQLite.class);

    public DepartmentRepositorySQLite(){
        setPropertiesFromFile();
        createTable();
    }

    public DepartmentRepositorySQLite(String dbUrl, String dbUser, String dbPassword){
        setProperties(dbUrl, dbUser, dbPassword);
        createTable();
    }

    /**
     * Creates table "departments" if it not exists
     */
    @Override
    public boolean createTable(){
        String sql = "CREATE TABLE IF NOT EXISTS departments (department text NOT NULL UNIQUE, PRIMARY KEY (\"department\"));";
        try (Statement statement = getStatement()){
            statement.execute(sql);
            return isTableExists("departments");
        } catch (SQLException e) {
            LOGGER.warn("Exception was thrown!", e);
            return false;
        }
    }

    /**
     * @return List of departments or empty list if something go wrong
     */
    @Override
    public Collection<String> getAll() {
        List<String>departments = new ArrayList<>();
        String sql = "SELECT * FROM departments;";

        LOGGER.info("Reading all departments from DB");
        try (ResultSet resultSet = getResultSet(sql)){

            while (resultSet.next()){
                departments.add(resultSet.getString("department"));
            }

        } catch (SQLException e) {
            LOGGER.warn("Exception was thrown!", e);
        }

        return departments;
    }

    /**
     * @param object department for adding
     * @return true if department was added or false if not
     */
    @Override
    public boolean add(@Nonnull String object) {
        String sql = "INSERT INTO departments VALUES ('" + object + "');";
        try (Statement statement = getStatement()){
            int result = statement.executeUpdate(sql);
            if (result > 0){
                LOGGER.info("Department = {} was added successfully", object);
                return true;
            } else return false;
        }catch (SQLException e){
            LOGGER.info("Department = {} is already exists", object);
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
        String sql = "UPDATE departments SET department = '" + newObject + "' WHERE department = '" + oldObject + "';";
        try (Statement statement = getStatement()){
            int result = statement.executeUpdate(sql);
            if (result > 0) {
                LOGGER.info("Department = {} was replaced by department = {} successfully", oldObject, newObject);
                return true;
            }else {
                LOGGER.info("Department = {} was not found", oldObject);
                return false;
            }
        }catch (SQLException e){
            LOGGER.info("Department = {} is already exists", newObject);
            return false;
        }
    }

    /**
     * @param object for delete
     * @return true if delete was successful or false if not
     */
    @Override
    public boolean remove(@Nonnull String object) {
        String sql = "DELETE FROM departments WHERE department = '" + object + "';";
        try (Statement statement = getStatement()){
            int result = statement.executeUpdate(sql);
            if (result > 0) {
                LOGGER.info("Department = {} was removed successfully", object);
                return true;
            } else {
                LOGGER.info("Department = {} was not found", object);
                return false;
            }
        }catch (SQLException e){
            LOGGER.warn("Exception was thrown!", e);
            return false;
        }
    }

    /**
     * Deletes all departments from DB
     * @return true if delete was successful or false if not
     */
    @Override
    public boolean clear() {
        String sql = "DELETE FROM departments;";
        try (Statement statement = getStatement()){
            statement.execute(sql);
            LOGGER.info("Departments list in DB was cleared successfully");
            return true;
        }catch (SQLException e){
            LOGGER.warn("Exception was thrown!", e);
            return false;
        }
    }

    @Override
    public boolean rewrite(@Nonnull Collection<String> newList) {
        String sql = "DELETE FROM departments;";
        try (Statement statement = getStatement()){
            statement.execute(sql);
            LOGGER.info("Departments list in DB was cleared successfully");

            if (!newList.isEmpty()) {
                StringBuilder sqlBuilder = new StringBuilder();
                sqlBuilder.append("INSERT INTO departments VALUES ");
                for (String a : newList) {
                    if (a == null) continue;

                    sqlBuilder.append("('").append(a).append("'),");
                }
                sqlBuilder.setCharAt(sqlBuilder.length() - 1, ';');

                statement.execute(sqlBuilder.toString());
            }

            LOGGER.info("The list of old departments has been rewritten to the new one:\n{}", newList);
            return true;
        }catch (SQLException e){
            LOGGER.warn("Exception was thrown!", e);
            return false;
        }
    }
}