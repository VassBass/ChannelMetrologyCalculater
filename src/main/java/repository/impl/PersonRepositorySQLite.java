package repository.impl;

import model.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.PersonRepository;
import repository.RepositoryJDBC;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PersonRepositorySQLite extends RepositoryJDBC implements PersonRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(PersonRepositorySQLite.class);

    private static final String EMPTY_ARRAY = "<Порожньо>";

    public PersonRepositorySQLite(){
        setPropertiesFromFile();
        createTable();
    }
    public PersonRepositorySQLite(String dbUrl, String dbUser, String dbPassword){
        setProperties(dbUrl, dbUser, dbPassword);
        createTable();
    }

    @Override
    public boolean createTable(){
        String sql = "CREATE TABLE IF NOT EXISTS persons ("
                + "id integer NOT NULL UNIQUE"
                + ", name text NOT NULL"
                + ", surname text NOT NULL"
                + ", patronymic text"
                + ", position text NOT NULL"
                + ", PRIMARY KEY (\"id\" AUTOINCREMENT)"
                + ");";
        try (Statement statement = getStatement()){
            statement.execute(sql);
            return isTableExists("persons");
        }catch (SQLException e){
            LOGGER.warn("Exception was thrown!", e);
            return false;
        }
    }

    @Override
    public List<Person> getAll() {
        LOGGER.info("Reading all persons from DB");
        List<Person>persons = new ArrayList<>();
        String sql = "SELECT * FROM persons;";
        try (ResultSet resultSet = getResultSet(sql)){
            while (resultSet.next()){
                Person person = new Person(resultSet.getInt("id"));
                person.setName(resultSet.getString("name"));
                person.setSurname(resultSet.getString("surname"));
                person.setPatronymic(resultSet.getString("patronymic"));
                person.setPosition(resultSet.getString("position"));

                persons.add(person);
            }
        }catch (SQLException e){
            LOGGER.warn("Exception was thrown!", e);
        }

        return persons;
    }

    @Override
    public String[] getAllNamesWithFirstEmptyString() {
        List<String>names = new ArrayList<>();
        names.add(EMPTY_ARRAY);
        String sql = "SELECT name, surname, patronymic FROM persons;";
        try (ResultSet resultSet = getResultSet(sql)){
            while (resultSet.next()){
                Person person = new Person();
                person.setName(resultSet.getString("name"));
                person.setSurname(resultSet.getString("surname"));
                person.setPatronymic(resultSet.getString("patronymic"));

                names.add(person._getFullName());
            }
        }catch (SQLException e){
            LOGGER.warn("Exception was thrown!", e);
        }

        return names.toArray(new String[0]);
    }

    @Override
    public String[] getNamesOfHeadsWithFirstEmptyString() {
        List<String>heads = new ArrayList<>();
        heads.add(EMPTY_ARRAY);
        String sql = "SELECT name, surname. patronymic FROM persons WHERE position = '" + Person.HEAD_OF_DEPARTMENT_ASUTP + "';";
        try (ResultSet resultSet = getResultSet(sql)){
            while (resultSet.next()){
                Person person = new Person();
                person.setName(resultSet.getString("name"));
                person.setSurname(resultSet.getString("surname"));
                person.setPatronymic(resultSet.getString("patronymic"));

                heads.add(person._getFullName());
            }
        }catch (SQLException e){
            LOGGER.warn("Exception was thrown!", e);
        }

        return heads.toArray(new String[0]);
    }

    @Override
    public Person get(int id) {
        if (id < 0) return null;
        LOGGER.info("Reading person with id = {} from DB", id);
        String sql = "SELECT * FROM persons WHERE id = " + id + " LIMIT 1;";
        try (ResultSet resultSet = getResultSet(sql)){
            if (resultSet.next()){
                Person person = new Person(resultSet.getInt("id"));
                person.setName(resultSet.getString("name"));
                person.setSurname(resultSet.getString("surname"));
                person.setPatronymic(resultSet.getString("patronymic"));
                person.setPosition(resultSet.getString("position"));

                return person;
            }else {
                LOGGER.info("Person with id = {} not found", id);
                return null;
            }
        }catch (SQLException e){
            LOGGER.warn("Exception was thrown!", e);
            return null;
        }
    }

    @Override
    public boolean add(Person person) {
        if (person == null) return false;

        String sql = "INSERT INTO persons (name, surname, patronymic, position) VALUES (?, ?, ?, ?);";
        try (PreparedStatement statement = getPreparedStatement(sql)){
            statement.setString(1, person.getName());
            statement.setString(2, person.getSurname());
            statement.setString(3, person.getPatronymic());
            statement.setString(4, person.getPosition());

            int result = statement.executeUpdate();
            if (result > 0) LOGGER.info("Person = {} was added successfully", person._getFullName());
            return true;
        }catch (SQLException e){
            LOGGER.warn("Exception was thrown!", e);
            return false;
        }
    }

    @Override
    public boolean set(Person person, Person ignored) {
        if (person == null) return false;

        String sql = "UPDATE persons SET name = ?, surname = ?, patronymic = ?, position = ? WHERE id = ?;";
        try (PreparedStatement statement = getPreparedStatement(sql)){
            statement.setString(1, person.getName());
            statement.setString(2, person.getSurname());
            statement.setString(3, person.getPatronymic());
            statement.setString(4, person.getPosition());
            statement.setInt(5, person.getId());

            int result = statement.executeUpdate();
            if (result > 0) LOGGER.info("Person was replaced by:\n{}\nsuccessfully", person);
            return true;
        }catch (SQLException e){
            LOGGER.warn("Exception was thrown!", e);
            return false;
        }
    }

    @Override
    public boolean add(List<Person> persons) {
        if (persons == null) return false;
        if (persons.isEmpty()) return true;

        String sql = "INSERT INTO persons (name, surname, patronymic, position) "
                + "VALUES ";
        StringBuilder sqlBuilder = new StringBuilder(sql);

        for (Person person : persons) {
            sqlBuilder.append("('").append(person.getName()).append("', ")
                    .append("'").append(person.getSurname()).append("', ")
                    .append("'").append(person.getPatronymic()).append("', ")
                    .append("'").append(person.getPosition()).append("'),");
        }
        sqlBuilder.setCharAt(sqlBuilder.length() - 1, ';');

        try (Statement statement = getStatement()) {
            int result = statement.executeUpdate(sqlBuilder.toString());
            if (result > 0) LOGGER.info("Persons list:\n{}\nwas added successfully", persons);
            return true;
        }catch (SQLException e){
            LOGGER.warn("Exception was thrown!", e);
            return false;
        }
    }

    @Override
    public boolean remove(Person person) {
        if (person == null) return false;

        String sql = "DELETE FROM persons WHERE id = " + person.getId();
        try (Statement statement = getStatement()){
            int result = statement.executeUpdate(sql);
            if (result > 0){
                LOGGER.info("Person = {} was removed successfully", person._getFullName());
            }else {
                LOGGER.info("Person with id = {} not found", person.getId());
            }
            return true;
        }catch (SQLException e){
            LOGGER.warn("Exception was thrown!", e);
            return false;
        }
    }

    @Override
    public boolean set(Person person) {
        if (person == null) return false;

        String sql = "UPDATE persons SET name = ?, surname = ?, patronymic = ?, position = ? WHERE id = ?;";
        try (PreparedStatement statement = getPreparedStatement(sql)){
            statement.setString(1, person.getName());
            statement.setString(2, person.getSurname());
            statement.setString(3, person.getPatronymic());
            statement.setString(4, person.getPosition());
            statement.setInt(5, person.getId());

            int result = statement.executeUpdate();
            if (result > 0) LOGGER.info("Person was replaced by:\n{}\nsuccessfully", person);
            return true;
        }catch (SQLException e){
            LOGGER.warn("Exception was thrown!", e);
            return false;
        }
    }

    @Override
    public boolean clear() {
        String sql = "DELETE FROM persons;";
        try (Statement statement = getStatement()){
            statement.execute(sql);
            LOGGER.info("Persons list in DB was cleared successfully");
            return true;
        }catch (SQLException e){
            LOGGER.warn("Exception was thrown!", e);
            return false;
        }
    }

    @Override
    public boolean rewrite(List<Person> persons) {
        if (persons == null) return false;

        String sql = "DELETE FROM persons;";
        try (Statement statement = getStatement()) {
            statement.execute(sql);
            LOGGER.info("Persons list in DB was cleared successfully");

            if (!persons.isEmpty()) {
                sql = "INSERT INTO persons (name, surname, patronymic, position) VALUES ";
                StringBuilder sqlBuilder = new StringBuilder(sql);

                for (Person person : persons) {
                    sqlBuilder.append("('").append(person.getName()).append("', ")
                            .append("'").append(person.getSurname()).append("', ")
                            .append("'").append(person.getPatronymic()).append("', ")
                            .append("'").append(person.getPosition()).append("'),");
                }
                sqlBuilder.setCharAt(sqlBuilder.length()-1, ';');
                statement.execute(sqlBuilder.toString());
            }

            LOGGER.info("The list of old persons has been rewritten to the new one:\n{}", persons);
            return true;
        } catch (SQLException e) {
            LOGGER.warn("Exception was thrown!", e);
            return false;
        }
    }
}