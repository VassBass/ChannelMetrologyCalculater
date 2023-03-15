package repository.repos.person;

import model.dto.Person;
import org.junit.Test;
import org.sqlite.JDBC;
import repository.config.RepositoryConfigHolder;
import repository.config.SqliteRepositoryConfigHolder;
import repository.connection.RepositoryDBConnector;
import repository.connection.SqliteRepositoryDBConnector;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

public class PersonIdGeneratorTest {

    @Test
    public void generateForMap() {
        final Person person1 = new Person(1);
        final Person person2 = new Person(2);
        final Person person3 = new Person(8);
        final Person person4 = new Person(5);
        Map<Integer, Person> personMap = new HashMap<>(
                Stream.of(person1, person2, person3, person4).collect(Collectors.toMap(Person::getId, Function.identity())));
        for (int x = 0; x < 100; x++) {
            assertFalse(personMap.containsKey(PersonIdGenerator.generateForMap(personMap)));
        }
    }

    @Test
    public void generateForCollection() {
        final Person person1 = new Person(1);
        final Person person2 = new Person(2);
        final Person person3 = new Person(8);
        final Person person4 = new Person(5);
        Collection<Person> personCollection = Arrays.asList(person1, person2, person3, person4);
        for (int x = 0; x < 100; x++) {
            int generatedId = PersonIdGenerator.generateForCollection(personCollection);
            assertFalse(personCollection.contains(new Person(generatedId)));
        }
    }

    /**
     * @see PersonRepositorySQLite
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Test
    public void generateForDefaultSqliteRepository() throws IOException, SQLException {
        String repositoryPropertiesFile = "properties/repository_test.properties";
        File dbFile = new File("TestData.db");
        String dbUrl = "jdbc:sqlite:TestData.db";
        String tableName = "persons";

        dbFile.createNewFile();
        String sql = String.format("CREATE TABLE IF NOT EXISTS %s (" +
                "id integer NOT NULL UNIQUE" +
                ", name text NOT NULL" +
                ", surname text NOT NULL" +
                ", patronymic text" +
                ", position text NOT NULL" +
                ", PRIMARY KEY (id AUTOINCREMENT)" +
                ");", tableName);
        try (Connection connection = DriverManager.getConnection(dbUrl);
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
        }

        RepositoryConfigHolder configHolder = new SqliteRepositoryConfigHolder(repositoryPropertiesFile);
        RepositoryDBConnector connector = new SqliteRepositoryDBConnector(configHolder);
        PersonRepository repository = new PersonRepositorySQLite(configHolder, connector);

        final Person person1 = new Person(1);
        final Person person2 = new Person(2);
        final Person person3 = new Person(8);
        final Person person4 = new Person(5);

        repository.addAll(Arrays.asList(person1, person2, person3, person4));

        for (int x = 0; x < 100; x++) {
            int generatedId = PersonIdGenerator.generateForRepository(repository);
            assertNull(repository.getById(generatedId));
        }

        sql = String.format("DROP TABLE IF EXISTS %s", tableName);
        DriverManager.registerDriver(new JDBC());
        try (Connection connection = DriverManager.getConnection(configHolder.getDBUrl());
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
        }
    }

    /**
     * @see BufferedPersonRepositorySQLite
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Test
    public void generateForBufferedSqliteRepository() throws IOException, SQLException {
        String repositoryPropertiesFile = "properties/repository_test.properties";
        File dbFile = new File("TestData.db");
        String dbUrl = "jdbc:sqlite:TestData.db";
        String tableName = "persons";

        dbFile.createNewFile();
        String sql = String.format("CREATE TABLE IF NOT EXISTS %s (" +
                "id integer NOT NULL UNIQUE" +
                ", name text NOT NULL" +
                ", surname text NOT NULL" +
                ", patronymic text" +
                ", position text NOT NULL" +
                ", PRIMARY KEY (id AUTOINCREMENT)" +
                ");", tableName);
        try (Connection connection = DriverManager.getConnection(dbUrl);
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
        }

        RepositoryConfigHolder configHolder = new SqliteRepositoryConfigHolder(repositoryPropertiesFile);
        RepositoryDBConnector connector = new SqliteRepositoryDBConnector(configHolder);

        PersonRepository repository = new BufferedPersonRepositorySQLite(configHolder, connector);

        final Person person1 = new Person(1);
        final Person person2 = new Person(2);
        final Person person3 = new Person(8);
        final Person person4 = new Person(5);

        repository.addAll(Arrays.asList(person1, person2, person3, person4));

        for (int x = 0; x < 100; x++) {
            int generatedId = PersonIdGenerator.generateForRepository(repository);
            assertNull(repository.getById(generatedId));
        }

        sql = String.format("DROP TABLE IF EXISTS %s", tableName);
        DriverManager.registerDriver(new JDBC());
        try (Connection connection = DriverManager.getConnection(configHolder.getDBUrl());
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
        }
    }
}