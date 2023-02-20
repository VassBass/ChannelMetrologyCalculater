package service.repository.repos.measurement_factor;

import model.MeasurementTransformFactor;
import org.junit.Test;
import org.sqlite.JDBC;
import service.repository.config.RepositoryConfigHolder;
import service.repository.config.SqliteRepositoryConfigHolder;
import service.repository.connection.RepositoryDBConnector;
import service.repository.connection.SqliteRepositoryDBConnector;
import service.repository.repos.person.BufferedPersonRepositorySQLite;
import service.repository.repos.person.PersonRepositorySQLite;

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

public class MeasurementFactorIdGeneratorTest {
    @Test
    public void generateForMap() {
        final MeasurementTransformFactor mtf1 = new MeasurementTransformFactor(1, null, null, 0);
        final MeasurementTransformFactor mtf2 = new MeasurementTransformFactor(2, null, null, 0);
        final MeasurementTransformFactor mtf8 = new MeasurementTransformFactor(8, null, null, 0);
        final MeasurementTransformFactor mtf5 = new MeasurementTransformFactor(5, null, null, 0);
        Map<Integer, MeasurementTransformFactor> factorMap = new HashMap<>(
                Stream.of(mtf1, mtf2, mtf8, mtf5).collect(Collectors.toMap(MeasurementTransformFactor::getId, Function.identity())));
        for (int x = 0; x < 100; x++) {
            assertFalse(factorMap.containsKey(MeasurementFactorIdGenerator.generateForMap(factorMap)));
        }
    }

    @Test
    public void generateForCollection() {
        final MeasurementTransformFactor mtf1 = new MeasurementTransformFactor(1, null, null, 0);
        final MeasurementTransformFactor mtf2 = new MeasurementTransformFactor(2, null, null, 0);
        final MeasurementTransformFactor mtf8 = new MeasurementTransformFactor(8, null, null, 0);
        final MeasurementTransformFactor mtf5 = new MeasurementTransformFactor(5, null, null, 0);
        Collection<MeasurementTransformFactor> factorCollection = Arrays.asList(mtf1, mtf2, mtf8, mtf5);
        for (int x = 0; x < 100; x++) {
            int generatedId = MeasurementFactorIdGenerator.generateForCollection(factorCollection);
            assertFalse(factorCollection.stream().anyMatch(mtf -> mtf.getId() == generatedId));
        }
    }

    /**
     * @see PersonRepositorySQLite
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Test
    public void generateForDefaultSqliteRepository() throws IOException, SQLException {
        String repositoryPropertiesFile = "properties/test_repository.properties";
        File dbFile = new File("TestData.db");
        String dbUrl = "jdbc:sqlite:TestData.db";
        String tableName = "measurement_factors";

        dbFile.createNewFile();
        String sql = String.format("CREATE TABLE IF NOT EXISTS %s (" +
                "id integer NOT NULL UNIQUE" +
                ", source TEXT NOT NULL" +
                ", result TEXT NOT NULL" +
                ", factor REAL NOT NULL" +
                ", PRIMARY KEY (id)" +
                ");", tableName);
        try (Connection connection = DriverManager.getConnection(dbUrl);
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
        }

        RepositoryConfigHolder configHolder = new SqliteRepositoryConfigHolder(repositoryPropertiesFile);
        RepositoryDBConnector connector = new SqliteRepositoryDBConnector(configHolder);
        MeasurementFactorRepository repository = new MeasurementFactorRepositorySQLite(configHolder, connector);

        repository.add("source1", "result1", 1);
        repository.add("source2", "result2", 2);
        repository.add("source3", "result3", 3);
        repository.add("source4", "result4", 4);

        for (int x = 0; x < 100; x++) {
            int generatedId = MeasurementFactorIdGenerator.generateForRepository(repository);
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
        String repositoryPropertiesFile = "properties/test_repository.properties";
        File dbFile = new File("TestData.db");
        String dbUrl = "jdbc:sqlite:TestData.db";
        String tableName = "measurement_factors";

        dbFile.createNewFile();
        String sql = String.format("CREATE TABLE IF NOT EXISTS %s (" +
                "id integer NOT NULL UNIQUE" +
                ", source TEXT NOT NULL" +
                ", result TEXT NOT NULL" +
                ", factor REAL NOT NULL" +
                ", PRIMARY KEY (id)" +
                ");", tableName);
        try (Connection connection = DriverManager.getConnection(dbUrl);
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
        }

        RepositoryConfigHolder configHolder = new SqliteRepositoryConfigHolder(repositoryPropertiesFile);
        RepositoryDBConnector connector = new SqliteRepositoryDBConnector(configHolder);
        MeasurementFactorRepository repository = new BufferedMeasurementFactorRepositorySQLite(configHolder, connector);

        repository.add("source1", "result1", 1);
        repository.add("source2", "result2", 2);
        repository.add("source3", "result3", 3);
        repository.add("source4", "result4", 4);

        for (int x = 0; x < 100; x++) {
            int generatedId = MeasurementFactorIdGenerator.generateForRepository(repository);
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