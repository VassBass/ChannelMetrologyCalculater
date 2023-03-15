package repository.repos.measurement_factor;

import model.dto.Measurement;
import model.dto.MeasurementTransformFactor;
import org.junit.*;
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
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class MeasurementFactorRepositorySQLiteTest {
    private static final String TEST_REPOSITORY_PROPERTIES_FILE = "properties/repository_test.properties";
    private static final File TEST_DB_FILE = new File("TestData.db");
    private static final String TEST_DB_URL = "jdbc:sqlite:TestData.db";
    private static final String TABLE_NAME = "measurement_factors";

    private List<MeasurementTransformFactor> expected;
    private MeasurementFactorRepository repository;

    private MeasurementTransformFactor createFactor(int id){
        String source = id < 3 ?
                Measurement.KPA :
                id < 5 ? Measurement.KGS_SM2 :
                Measurement.M3_HOUR;
        String result = id < 3 ?
                Measurement.PA :
                id < 5 ? Measurement.KGS_MM2 :
                Measurement.CM_S;
        double factor = id * 0.1;
        return new MeasurementTransformFactor(id, source, result, factor);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @BeforeClass
    public static void createDB() throws IOException, SQLException {
        TEST_DB_FILE.createNewFile();
        String sql = String.format("CREATE TABLE IF NOT EXISTS %s (" +
                "id INTEGER NOT NULL UNIQUE" +
                ", source TEXT NOT NULL" +
                ", result TEXT NOT NULL" +
                ", factor REAL NOT NULL" +
                ", PRIMARY KEY (id)" +
                ");", TABLE_NAME);
        try (Connection connection = DriverManager.getConnection(TEST_DB_URL);
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
        }
    }

    @AfterClass
    public static void removeDB() throws SQLException {
        String sql = String.format("DROP TABLE IF EXISTS %s", TABLE_NAME);
        DriverManager.registerDriver(new JDBC());
        try (Connection connection = DriverManager.getConnection(TEST_DB_URL);
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
        }
    }

    @Before
    public void init() throws SQLException {
        expected = new ArrayList<>(7);
        for (int i = 0; i < 7; i++) expected.add(createFactor(i));

        String sql = String.format("INSERT INTO %s (id, source, result, factor) VALUES ", TABLE_NAME);
        StringBuilder sqlBuilder = new StringBuilder(sql);
        for (MeasurementTransformFactor mtf : expected) {
            String values = String.format("(%s, '%s', '%s', %s),",
                    mtf.getId(),
                    mtf.getTransformFrom(),
                    mtf.getTransformTo(),
                    mtf.getTransformFactor());
            sqlBuilder.append(values);
        }
        sqlBuilder.setCharAt(sqlBuilder.length() - 1, ';');

        try (Connection connection = DriverManager.getConnection(TEST_DB_URL);
             Statement statement = connection.createStatement()) {
            statement.execute(sqlBuilder.toString());
        }

        RepositoryConfigHolder configHolder = new SqliteRepositoryConfigHolder(TEST_REPOSITORY_PROPERTIES_FILE);
        RepositoryDBConnector connector = new SqliteRepositoryDBConnector(configHolder);
        repository = new MeasurementFactorRepositorySQLite(configHolder, connector);
    }

    @After
    public void clearTestDB() throws Exception {
        String sql = String.format("DELETE FROM %s;", TABLE_NAME);
        try (Connection connection = DriverManager.getConnection(TEST_DB_URL);
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
        }
    }

    @Test
    public void testGetAll() {
        Collection<MeasurementTransformFactor> actual = repository.getAll();
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testGetExistedById() {
        MeasurementTransformFactor mtf0 = createFactor(0);

        assertEquals(mtf0, repository.getById(mtf0.getId()));
    }

    @Test
    public void testGetNotExistedById() {
        assertNull(repository.getById(888));
    }

    @Test
    public void testGetByExistedSource() {
        expected.removeIf(mtf -> !mtf.getTransformFrom().equals(Measurement.KPA));

        Collection<MeasurementTransformFactor> actual = repository.getBySource(Measurement.KPA);
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testGetByNotExistedSource() {
        assertTrue(repository.getBySource("Not existed").isEmpty());
    }

    @Test
    public void testGetByExistedResult() {
        expected.removeIf(mtf -> !mtf.getTransformTo().equals(Measurement.PA));

        Collection<MeasurementTransformFactor> actual = repository.getByResult(Measurement.PA);
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testGetByNotExistedResult() {
        assertTrue(repository.getByResult("Not existed").isEmpty());
    }

    @Test
    public void testAdd() {
        MeasurementTransformFactor mtf7 = createFactor(7);
        expected.add(mtf7);

        assertEquals(7, repository.add(mtf7.getTransformFrom(), mtf7.getTransformTo(), mtf7.getTransformFactor()));
        assertEquals(mtf7, repository.getById(7));

        Collection<MeasurementTransformFactor> actual = repository.getAll();
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testSet() {
        MeasurementTransformFactor mtf8 = createFactor(8);
        MeasurementTransformFactor changedMtf0 = new MeasurementTransformFactor(
                0,
                mtf8.getTransformFrom(),
                mtf8.getTransformTo(),
                mtf8.getTransformFactor()
        );
        expected.set(0, changedMtf0);

        assertTrue(repository.set(changedMtf0));

        MeasurementTransformFactor changedFromRepo = repository.getById(0);
        assertEquals(changedMtf0, changedFromRepo);
        assertEquals(changedMtf0.getId(), changedFromRepo.getId());
        assertEquals(changedMtf0.getTransformFrom(), changedFromRepo.getTransformFrom());
        assertEquals(changedMtf0.getTransformTo(), changedFromRepo.getTransformTo());
        assertEquals(0, Double.compare(changedMtf0.getTransformFactor(), changedFromRepo.getTransformFactor()));

        Collection<MeasurementTransformFactor> actual = repository.getAll();
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testRewrite() {
        MeasurementTransformFactor mtf8 = createFactor(8);
        Collection<MeasurementTransformFactor> toRewrite = Arrays.asList(createFactor(2), createFactor(1), null, mtf8);
        List<MeasurementTransformFactor> expected = toRewrite.stream().filter(Objects::nonNull).collect(Collectors.toList());

        assertTrue(repository.rewrite(toRewrite));

        assertNull(repository.getById(0));
        assertEquals(mtf8, repository.getById(8));

        Collection<MeasurementTransformFactor> actual = repository.getAll();
        assertFalse(actual.stream().anyMatch(Objects::isNull));
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testChangeAllExistedSourcesToNew() {
        expected.forEach(mtf -> {
            if (mtf.getTransformFrom().equals(Measurement.KPA)) mtf.setTransformFrom(Measurement.DEGREE_CELSIUS);
        });

        assertTrue(repository.changeAllSources(Measurement.KPA, Measurement.DEGREE_CELSIUS));

        Collection<MeasurementTransformFactor> actual = repository.getAll();
        assertEquals(0, actual.stream().filter(mtf -> mtf.getTransformFrom().equals(Measurement.KPA)).count());
        assertEquals(3, actual.stream().filter(mtf -> mtf.getTransformFrom().equals(Measurement.DEGREE_CELSIUS)).count());

        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testChangeAllExistedSourcesToExisted() {
        expected.forEach(mtf -> {
            if (mtf.getTransformFrom().equals(Measurement.KPA)) mtf.setTransformFrom(Measurement.M3_HOUR);
        });

        assertTrue(repository.changeAllSources(Measurement.KPA, Measurement.M3_HOUR));

        Collection<MeasurementTransformFactor> actual = repository.getAll();
        assertEquals(0, actual.stream().filter(mtf -> mtf.getTransformFrom().equals(Measurement.KPA)).count());
        assertEquals(5, actual.stream().filter(mtf -> mtf.getTransformFrom().equals(Measurement.M3_HOUR)).count());

        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testChangeAllNotExistedSourcesToNew() {
        assertTrue(repository.changeAllSources(Measurement.DEGREE_CELSIUS, Measurement.MM_ACVA));

        Collection<MeasurementTransformFactor> actual = repository.getAll();
        assertEquals(0, actual.stream().filter(mtf -> mtf.getTransformFrom().equals(Measurement.DEGREE_CELSIUS)).count());
        assertEquals(0, actual.stream().filter(mtf -> mtf.getTransformFrom().equals(Measurement.MM_ACVA)).count());

        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testChangeAllNotExistedSourcesToExisted() {
        assertTrue(repository.changeAllSources(Measurement.DEGREE_CELSIUS, Measurement.KPA));

        Collection<MeasurementTransformFactor> actual = repository.getAll();
        assertEquals(0, actual.stream().filter(mtf -> mtf.getTransformFrom().equals(Measurement.DEGREE_CELSIUS)).count());
        assertEquals(3, actual.stream().filter(mtf -> mtf.getTransformFrom().equals(Measurement.KPA)).count());

        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testChangeAllExistedResultsToNew() {
        expected.forEach(mtf -> {
            if (mtf.getTransformTo().equals(Measurement.PA)) mtf.setTransformTo(Measurement.DEGREE_CELSIUS);
        });

        assertTrue(repository.changeAllResults(Measurement.PA, Measurement.DEGREE_CELSIUS));

        Collection<MeasurementTransformFactor> actual = repository.getAll();
        assertEquals(0, actual.stream().filter(mtf -> mtf.getTransformTo().equals(Measurement.PA)).count());
        assertEquals(3, actual.stream().filter(mtf -> mtf.getTransformTo().equals(Measurement.DEGREE_CELSIUS)).count());

        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testChangeAllExistedResultsToExisted() {
        expected.forEach(mtf -> {
            if (mtf.getTransformTo().equals(Measurement.PA)) mtf.setTransformTo(Measurement.CM_S);
        });

        assertTrue(repository.changeAllResults(Measurement.PA, Measurement.CM_S));

        Collection<MeasurementTransformFactor> actual = repository.getAll();
        assertEquals(0, actual.stream().filter(mtf -> mtf.getTransformTo().equals(Measurement.PA)).count());
        assertEquals(5, actual.stream().filter(mtf -> mtf.getTransformTo().equals(Measurement.CM_S)).count());

        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testChangeAllNotExistedResultsToNew() {
        assertTrue(repository.changeAllResults(Measurement.DEGREE_CELSIUS, Measurement.MM_ACVA));

        Collection<MeasurementTransformFactor> actual = repository.getAll();
        assertEquals(0, actual.stream().filter(mtf -> mtf.getTransformTo().equals(Measurement.DEGREE_CELSIUS)).count());
        assertEquals(0, actual.stream().filter(mtf -> mtf.getTransformTo().equals(Measurement.MM_ACVA)).count());

        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testChangeAllNotExistedResultsToExisted() {
        assertTrue(repository.changeAllResults(Measurement.DEGREE_CELSIUS, Measurement.KPA));

        Collection<MeasurementTransformFactor> actual = repository.getAll();
        assertEquals(0, actual.stream().filter(mtf -> mtf.getTransformTo().equals(Measurement.DEGREE_CELSIUS)).count());
        assertEquals(3, actual.stream().filter(mtf -> mtf.getTransformTo().equals(Measurement.PA)).count());

        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testChangeFactorForExisted() {
        assertNotEquals(0, Double.compare(0.888, repository.getById(0).getTransformFactor()));
        assertTrue(repository.changeFactor(0, 0.888));
        assertEquals(0, Double.compare(0.888, repository.getById(0).getTransformFactor()));
    }

    @Test
    public void testChangeFactorForNotExisted() {
        assertFalse(repository.changeFactor(8, 0.888));
    }

    @Test
    public void testRemoveByIdExisted() {
        expected.remove(0);

        assertTrue(repository.removeById(0));
        assertNull(repository.getById(0));

        Collection<MeasurementTransformFactor> actual = repository.getAll();
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testRemoveByIdNotExisted() {
        assertFalse(repository.removeById(888));
        assertNull(repository.getById(888));

        Collection<MeasurementTransformFactor> actual = repository.getAll();
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testRemoveByExistedSource() {
        expected.removeIf(mtf -> mtf.getTransformFrom().equals(Measurement.KPA));

        assertTrue(repository.removeBySource(Measurement.KPA));

        Collection<MeasurementTransformFactor> actual = repository.getAll();
        assertFalse(actual.stream().anyMatch(mtf -> mtf.getTransformFrom().equals(Measurement.KPA)));

        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testRemoveByNotExistedSource() {
        assertTrue(repository.removeBySource(Measurement.DEGREE_CELSIUS));

        Collection<MeasurementTransformFactor> actual = repository.getAll();
        assertFalse(actual.stream().anyMatch(mtf -> mtf.getTransformFrom().equals(Measurement.DEGREE_CELSIUS)));

        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testRemoveByExistedResult() {
        expected.removeIf(mtf -> mtf.getTransformTo().equals(Measurement.PA));

        assertTrue(repository.removeByResult(Measurement.PA));

        Collection<MeasurementTransformFactor> actual = repository.getAll();
        assertFalse(actual.stream().anyMatch(mtf -> mtf.getTransformTo().equals(Measurement.PA)));

        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testRemoveByNotExistedResult() {
        assertTrue(repository.removeBySource(Measurement.DEGREE_CELSIUS));

        Collection<MeasurementTransformFactor> actual = repository.getAll();
        assertFalse(actual.stream().anyMatch(mtf -> mtf.getTransformFrom().equals(Measurement.DEGREE_CELSIUS)));

        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }
}