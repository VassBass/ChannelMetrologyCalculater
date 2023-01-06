package repository.impl;

import model.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.PersonRepository;
import repository.RepositoryJDBC;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class PersonRepositorySQLite extends RepositoryJDBC implements PersonRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(PersonRepositorySQLite.class);

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
    public Collection<Person> getAll() {
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
    public Optional<Person> getById(@Nonnegative int id) {
        String sql = "SELECT * FROM persons WHERE id = " + id + " LIMIT 1;";
        try (ResultSet resultSet = getResultSet(sql)){
            if (resultSet.next()){
                Person person = new Person(resultSet.getInt("id"));
                person.setName(resultSet.getString("name"));
                person.setSurname(resultSet.getString("surname"));
                person.setPatronymic(resultSet.getString("patronymic"));
                person.setPosition(resultSet.getString("position"));

                return Optional.of(person);
            }
        }catch (SQLException e){
            LOGGER.warn("Exception was thrown!", e);
        }

        return Optional.empty();
    }

    @Override
    public boolean add(@Nonnull Person person) {
        String sql = "INSERT INTO persons (name, surname, patronymic, position) VALUES (?, ?, ?, ?);";
        try (PreparedStatement statement = getPreparedStatement(sql)){
            statement.setString(1, person.getName());
            statement.setString(2, person.getSurname());
            statement.setString(3, person.getPatronymic());
            statement.setString(4, person.getPosition());

            return statement.executeUpdate() > 0;
        }catch (SQLException e){
            LOGGER.warn("Exception was thrown!", e);
            return false;
        }
    }

    @Override
    public boolean set(@Nonnull Person person, @Nullable Person ignored) {
        String sql = "UPDATE persons SET name = ?, surname = ?, patronymic = ?, position = ? WHERE id = ?;";
        try (PreparedStatement statement = getPreparedStatement(sql)){
            statement.setString(1, person.getName());
            statement.setString(2, person.getSurname());
            statement.setString(3, person.getPatronymic());
            statement.setString(4, person.getPosition());
            statement.setInt(5, person.getId());

            return statement.executeUpdate() > 0;
        }catch (SQLException e){
            LOGGER.warn("Exception was thrown!", e);
            return false;
        }
    }

    @Override
    public boolean add(@Nonnull Collection<Person> persons) {
        if (persons.isEmpty()) return true;

        String sql = "INSERT INTO persons (name, surname, patronymic, position) "
                + "VALUES ";
        StringBuilder sqlBuilder = new StringBuilder(sql);

        for (Person person : persons) {
            if (person == null) continue;

            sqlBuilder.append("('").append(person.getName()).append("', ")
                    .append("'").append(person.getSurname()).append("', ")
                    .append("'").append(person.getPatronymic()).append("', ")
                    .append("'").append(person.getPosition()).append("'),");
        }
        sqlBuilder.setCharAt(sqlBuilder.length() - 1, ';');

        try (Statement statement = getStatement()) {
            return statement.executeUpdate(sqlBuilder.toString()) > 0;
        }catch (SQLException e){
            LOGGER.warn("Exception was thrown!", e);
            return false;
        }
    }

    @Override
    public boolean remove(@Nonnull Person person) {
        String sql = "DELETE FROM persons WHERE id = " + person.getId();
        try (Statement statement = getStatement()){
            return statement.executeUpdate(sql) > 0;
        }catch (SQLException e){
            LOGGER.warn("Exception was thrown!", e);
            return false;
        }
    }

    @Override
    public boolean set(@Nonnull Person person) {
        String sql = "UPDATE persons SET name = ?, surname = ?, patronymic = ?, position = ? WHERE id = ?;";
        try (PreparedStatement statement = getPreparedStatement(sql)){
            statement.setString(1, person.getName());
            statement.setString(2, person.getSurname());
            statement.setString(3, person.getPatronymic());
            statement.setString(4, person.getPosition());
            statement.setInt(5, person.getId());

            return statement.executeUpdate() > 0;
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
            return true;
        }catch (SQLException e){
            LOGGER.warn("Exception was thrown!", e);
            return false;
        }
    }

    @Override
    public boolean rewrite(@Nonnull Collection<Person> persons) {
        String sql = "DELETE FROM persons;";
        try (Statement statement = getStatement()) {
            statement.execute(sql);

            if (!persons.isEmpty()) {
                sql = "INSERT INTO persons (name, surname, patronymic, position) VALUES ";
                StringBuilder sqlBuilder = new StringBuilder(sql);

                for (Person person : persons) {
                    if (person == null) continue;

                    sqlBuilder.append("('").append(person.getName()).append("', ")
                            .append("'").append(person.getSurname()).append("', ")
                            .append("'").append(person.getPatronymic()).append("', ")
                            .append("'").append(person.getPosition()).append("'),");
                }
                sqlBuilder.setCharAt(sqlBuilder.length()-1, ';');
                statement.execute(sqlBuilder.toString());
            }

            return true;
        } catch (SQLException e) {
            LOGGER.warn("Exception was thrown!", e);
            return false;
        }
    }
}