package repository.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.DepartmentRepository;
import repository.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DepartmentRepositoryImpl extends Repository implements DepartmentRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(DepartmentRepository.class);

    public DepartmentRepositoryImpl(){
        setPropertiesFromFile();
        createTable();
    }

    public DepartmentRepositoryImpl(String dbUrl, String dbUser, String dbPassword){
        setProperties(dbUrl, dbUser, dbPassword);
        createTable();
    }

    /**
     * Creates table "departments" if it not exists
     */
    @Override
    protected void createTable(){
        String sql = "CREATE TABLE IF NOT EXISTS departments (department text NOT NULL UNIQUE, PRIMARY KEY (\"department\"));";
        try (Statement statement = getStatement()){
            statement.execute(sql);
        } catch (SQLException e) {
            LOGGER.warn("Exception was thrown!", e);
        }
    }

    /**
     * @return List of departments or empty list if something go wrong
     */
    @Override
    public List<String> getAll() {
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
    public boolean add(String object) {
        if (object == null) return false;

        String sql = "INSERT INTO departments (department) VALUES ('" + object + "');";
        try (Statement statement = getStatement()){
            int result = statement.executeUpdate(sql);
            if (result > 0) LOGGER.info("Department = {} was added successfully", object);
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

        String sql = "UPDATE departments SET department = '" + newObject + "' WHERE department = '" + oldObject + "';";
        try (Statement statement = getStatement()){
            int result = statement.executeUpdate(sql);
            if (result > 0) LOGGER.info("Department = {} was replaced by department = {} successfully", oldObject, newObject);
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

        String sql = "DELETE FROM departments WHERE department = '" + object + "';";
        try (Statement statement = getStatement()){
            int result = statement.executeUpdate(sql);
            if (result > 0) LOGGER.info("Department = {} was removed successfully", object);
            return true;
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
    public boolean rewrite(List<String> newList) {
        if (newList == null) return false;

        String sql = "DELETE FROM departments;";
        try (Statement statement = getStatement()){
            statement.execute(sql);
            LOGGER.info("Departments list in DB was cleared successfully");

            if (!newList.isEmpty()) {
                StringBuilder sqlBuilder = new StringBuilder();
                sqlBuilder.append("INSERT INTO departments(department) VALUES ");
                for (String a : newList) {
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