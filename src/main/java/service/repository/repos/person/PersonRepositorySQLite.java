package service.repository.repos.person;

import model.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.repository.config.RepositoryConfigHolder;
import service.repository.connection.RepositoryDBConnector;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PersonRepositorySQLite implements PersonRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(PersonRepositorySQLite.class);

    private final String tableName;
    private final RepositoryDBConnector connector;

    public PersonRepositorySQLite(RepositoryConfigHolder configHolder, RepositoryDBConnector connector) {
        this.tableName = configHolder.getTableName(PersonRepository.class);
        this.connector = connector;
    }

    @Override
    public Collection<Person> getAll() {
        List<Person>persons = new ArrayList<>();
        String sql = String.format("SELECT * FROM %s;", tableName);
        try (ResultSet resultSet = connector.getResultSet(sql)){
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
    public Person getById(@Nonnegative int id) {
        String sql = String.format("SELECT * FROM %s WHERE id = %s LIMIT 1;", tableName, id);
        try (ResultSet resultSet = connector.getResultSet(sql)){
            if (resultSet.next()){
                Person person = new Person(resultSet.getInt("id"));
                person.setName(resultSet.getString("name"));
                person.setSurname(resultSet.getString("surname"));
                person.setPatronymic(resultSet.getString("patronymic"));
                person.setPosition(resultSet.getString("position"));

                return person;
            }
        }catch (SQLException e){
            LOGGER.warn("Exception was thrown!", e);
        }

        return null;
    }

    /**
     * Adds a person to the DB and sets the generated id for him
     * @param person to add
     * @return true if person added successful or false if something go wrong
     */
    @Override
    public boolean add(@Nonnull Person person) {
        if (person.getId() < 0) person.setId(PersonIdGenerator.generateForRepository(this));

        String sql = String.format("INSERT INTO %s (id, name, surname, patronymic, position) VALUES (?, ?, ?, ?, ?);", tableName);
        try (PreparedStatement statement = connector.getPreparedStatementWithKey(sql)){
            statement.setInt(1, person.getId());
            statement.setString(2, person.getName());
            statement.setString(3, person.getSurname());
            statement.setString(4, person.getPatronymic());
            statement.setString(5, person.getPosition());

            return statement.executeUpdate() > 0;
        }catch (SQLException e){
            LOGGER.warn("Exception was thrown!", e);
            return false;
        }
    }

    /**
     * Adds a persons to the DB and sets the generated id for them
     * @param persons to add
     * @return true if persons added successful or false if something go wrong
     */
    @Override
    public boolean add(@Nonnull Collection<Person> persons) {
        if (persons.isEmpty()) {
            return false;
        } else {
            for (Person p : persons) {
                if (p.getId() < 0) p.setId(PersonIdGenerator.generateForRepository(this));
            }
        }

        String sql = String.format("INSERT INTO %s (id, name, surname, patronymic, position) VALUES ", tableName);
        StringBuilder sqlBuilder = new StringBuilder(sql);

        for (Person person : persons) {
            if (person == null) continue;

            String values = String.format("(%s, '%s', '%s', '%s', '%s'),",
                    person.getId(),
                    person.getName(),
                    person.getSurname(),
                    person.getPatronymic(),
                    person.getPosition());
            sqlBuilder.append(values);
        }
        sqlBuilder.setCharAt(sqlBuilder.length() - 1, ';');

        try (Statement statement = connector.getStatement()) {
            statement.execute(sqlBuilder.toString());
            return true;
        }catch (SQLException e){
            LOGGER.warn("Exception was thrown!", e);
            return false;
        }
    }

    @Override
    public boolean remove(@Nonnull Person person) {
        String sql = String.format("DELETE FROM %s WHERE id = %s;", tableName, person.getId());
        try (Statement statement = connector.getStatement()){
            return statement.executeUpdate(sql) > 0;
        }catch (SQLException e){
            LOGGER.warn("Exception was thrown!", e);
            return false;
        }
    }

    @Override
    public boolean set(@Nonnull Person person) {
        String sql = String.format("UPDATE %s SET name = ?, surname = ?, patronymic = ?, position = ? WHERE id = ?;", tableName);
        try (PreparedStatement statement = connector.getPreparedStatement(sql)){
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
        String sql = String.format("DELETE FROM %s;", tableName);
        try (Statement statement = connector.getStatement()){
            statement.execute(sql);
            return true;
        }catch (SQLException e){
            LOGGER.warn("Exception was thrown!", e);
            return false;
        }
    }

    @Override
    public boolean rewrite(@Nonnull Collection<Person> persons) {
        String sql = String.format("DELETE FROM %s;", tableName);
        try (Statement statement = connector.getStatement()) {
            statement.execute(sql);

            if (!persons.isEmpty()) {
                sql = String.format("INSERT INTO %s (name, surname, patronymic, position) VALUES ", tableName);
                StringBuilder sqlBuilder = new StringBuilder(sql);

                for (Person person : persons) {
                    if (person == null) continue;

                    String values = String.format("('%s', '%s', '%s', '%s'),",
                            person.getName(), person.getSurname(), person.getPatronymic(), person.getPosition());
                    sqlBuilder.append(values);
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