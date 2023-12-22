package repository.repos.person;

import localization.Messages;
import model.dto.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.config.RepositoryConfigHolder;
import repository.connection.RepositoryDBConnector;
import repository.init.PersonRepositoryInitializer;

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
        new PersonRepositoryInitializer(configHolder, connector).init();
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
            LOGGER.warn(Messages.Log.EXCEPTION_THROWN, e);
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
            LOGGER.warn(Messages.Log.EXCEPTION_THROWN, e);
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
            LOGGER.warn(Messages.Log.EXCEPTION_THROWN, e);
            return false;
        }
    }

    /**
     * Adds a persons to the DB and sets the generated id for them
     * @param persons to add
     * @return true if persons added successful or false if something go wrong
     */
    @Override
    public boolean addAll(@Nonnull Collection<Person> persons) {
        if (persons.isEmpty()) {
            return true;
        } else {
            Collection<Person> all = getAll();
            for (Person p : persons) {
                if (p == null || all.contains(p)) continue;
                if (p.getId() < 0) p.setId(PersonIdGenerator.generateForCollection(all));
                all.add(p);
            }
            return rewrite(all);
        }
    }

    @Override
    public boolean remove(@Nonnull Person person) {
        String sql = String.format("DELETE FROM %s WHERE id = %s;", tableName, person.getId());
        try (Statement statement = connector.getStatement()){
            return statement.executeUpdate(sql) > 0;
        }catch (SQLException e){
            LOGGER.warn(Messages.Log.EXCEPTION_THROWN, e);
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
            LOGGER.warn(Messages.Log.EXCEPTION_THROWN, e);
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
            LOGGER.warn(Messages.Log.EXCEPTION_THROWN, e);
            return false;
        }
    }

    @Override
    public Person findMostSimilarByLastName(String lastName) {
        List<Person> result = new ArrayList<>();
        for (int i = lastName.length(); i > 0; i--) {
            String s = lastName.substring(0, i);
            for (Person p : getAll()) {
                if (!result.contains(p) && p.getSurname().contains(s)) {
                    result.add(p);
                }
            }
        }
        return result.isEmpty() ? null : result.get(0);
    }

    @Override
    public boolean rewrite(@Nonnull Collection<Person> persons) {
        String sql = String.format("DELETE FROM %s;", tableName);
        try (Statement statement = connector.getStatement()) {
            statement.execute(sql);

            if (!persons.isEmpty()) {
                sql = String.format("INSERT INTO %s (id, name, surname, patronymic, position) VALUES ", tableName);
                StringBuilder sqlBuilder = new StringBuilder(sql);

                int id = 0;
                for (Person person : persons) {
                    if (person == null) continue;
                    if (person.getId() < 0) person.setId(id++);

                    String values = String.format("(%s, '%s', '%s', '%s', '%s'),",
                            person.getId(), person.getName(), person.getSurname(), person.getPatronymic(), person.getPosition());
                    sqlBuilder.append(values);
                }
                sqlBuilder.setCharAt(sqlBuilder.length()-1, ';');
                statement.execute(sqlBuilder.toString());
            }

            return true;
        } catch (SQLException e) {
            LOGGER.warn(Messages.Log.EXCEPTION_THROWN, e);
            return false;
        }
    }
}