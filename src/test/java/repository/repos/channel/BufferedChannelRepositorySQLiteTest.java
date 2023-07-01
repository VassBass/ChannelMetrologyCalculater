package repository.repos.channel;

import model.dto.Channel;
import model.dto.Measurement;
import model.dto.builder.ChannelBuilder;
import org.junit.*;
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

public class BufferedChannelRepositorySQLiteTest {
    private static final String TEST_REPOSITORY_PROPERTIES_FILE = "properties/repository_test.properties";
    private static final File TEST_DB_FILE = new File("TestData.db");
    private static final String TEST_DB_URL = "jdbc:sqlite:TestData.db";
    private static final String TABLE_NAME = "channels";

    private static List<Channel> expected;

    private static Channel createChannel(int number, String measurementValue){
        return new ChannelBuilder(String.valueOf(number))
                .setName("name" + number)
                .setDate(String.format("0%s.0%s.%s", (number + 1), ((number % 2) + 1), (2000 + number)))
                .setDepartment("department" + number)
                .setArea("area" + number)
                .setProcess("process" + number)
                .setInstallation("installation" + number)
                .setMeasurementValue(measurementValue)
                .build();
    }

    private ChannelRepository repository;

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @BeforeClass
    public static void createDB() throws IOException, SQLException {
        TEST_DB_FILE.createNewFile();
        String sql = String.format("CREATE TABLE IF NOT EXISTS %s ("
                + "code text NOT NULL UNIQUE"
                + ", name text NOT NULL"
                + ", department text"
                + ", area text"
                + ", process text"
                + ", installation text"
                + ", technology_number text NOT NULL"
                + ", protocol_number text"
                + ", reference text"
                + ", date text"
                + ", suitability text NOT NULL"
                + ", measurement_name text NOT NULL"
                + ", measurement_value text NOT NULL"
                + ", frequency real NOT NULL"
                + ", range_min real NOT NULL"
                + ", range_max real NOT NULL"
                + ", allowable_error_percent real NOT NULL"
                + ", allowable_error_value real NOT NULL"
                + ", PRIMARY KEY (\"code\")"
                + ");", TABLE_NAME);
        try (Connection connection = DriverManager.getConnection(TEST_DB_URL);
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
        }
    }

    @AfterClass
    public static void removeDB() throws SQLException {
        String sql = String.format("DROP TABLE IF EXISTS %s", TABLE_NAME);
        try (Connection connection = DriverManager.getConnection(TEST_DB_URL);
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
        }
    }

    @Before
    public void init() throws SQLException {
        expected = new ArrayList<>(7);
        expected.add(createChannel(0, Measurement.DEGREE_CELSIUS));
        expected.add(createChannel(1, Measurement.DEGREE_CELSIUS));
        expected.add(createChannel(2, Measurement.DEGREE_CELSIUS));
        expected.add(createChannel(3, Measurement.KPA));
        expected.add(createChannel(4, Measurement.KPA));
        expected.add(createChannel(5, Measurement.M3_HOUR));
        expected.add(createChannel(6, Measurement.M3_HOUR));

        String sql = String.format("INSERT INTO %s (code, name, department, area, process, installation, technology_number" +
                ", protocol_number, reference, date, suitability, measurement_name, measurement_value, frequency" +
                ", range_min, range_max, allowable_error_percent, allowable_error_value) "
                + "VALUES ", TABLE_NAME);
        StringBuilder sqlBuilder = new StringBuilder(sql);

        for (Channel channel : expected) {
            String values = String.format(
                    "('%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', %s, %s, %s, %s, %s),",
                    channel.getCode(),
                    channel.getName(),
                    channel.getDepartment(),
                    channel.getArea(),
                    channel.getProcess(),
                    channel.getInstallation(),
                    channel.getTechnologyNumber(),
                    channel.getNumberOfProtocol(),
                    channel.getReference(),
                    channel.getDate(),
                    channel.isSuitability(),
                    channel.getMeasurementName(),
                    channel.getMeasurementValue(),
                    channel.getFrequency(),
                    channel.getRangeMin(),
                    channel.getRangeMax(),
                    channel.getAllowableErrorPercent(),
                    channel.getAllowableErrorValue()
            );
            sqlBuilder.append(values);
        }
        sqlBuilder.setCharAt(sqlBuilder.length()-1, ';');

        try (Connection connection = DriverManager.getConnection(TEST_DB_URL);
             Statement statement = connection.createStatement()) {
            statement.execute(sqlBuilder.toString());
        }

        RepositoryConfigHolder configHolder = new SqliteRepositoryConfigHolder(TEST_REPOSITORY_PROPERTIES_FILE);
        RepositoryDBConnector connector = new SqliteRepositoryDBConnector(configHolder);
        repository = new BufferedChannelRepositorySQLite(configHolder, connector);
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
        Collection<Channel> actual = repository.getAll();
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testGetExisted() {
        assertEquals(expected.get(1), repository.get("1"));
    }

    @Test
    public void testGetNotExisted() {
        assertNull(repository.get("Not Existed"));
    }

    @Test
    public void testAddNotExisted() {
        Channel channel7 = createChannel(7, Measurement.DEGREE_CELSIUS);
        expected.add(channel7);

        assertTrue(repository.add(channel7));
        assertEquals(channel7, repository.get("7"));

        Collection<Channel> actual = repository.getAll();
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testAddExisted() {
        Channel channel2 = createChannel(2, Measurement.DEGREE_CELSIUS);

        assertFalse(repository.add(channel2));
        assertEquals(channel2, repository.get("2"));

        Collection<Channel> actual = repository.getAll();
        assertEquals(1L, actual.stream().filter(ch -> ch.equals(channel2)).count());
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testRemoveExisted() {
        Channel channel2 = createChannel(2, Measurement.DEGREE_CELSIUS);
        expected.remove(channel2);

        assertTrue(repository.remove(channel2));
        assertNull(repository.get(channel2.getCode()));

        Collection<Channel> actual = repository.getAll();
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testRemoveNotExisted() {
        Channel channel7 = createChannel(7, Measurement.DEGREE_CELSIUS);

        assertFalse(repository.remove(channel7));
        assertNull(repository.get(channel7.getCode()));

        Collection<Channel> actual = repository.getAll();
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testRemoveByExistedMeasurementValue(){
        expected.removeIf(ch -> ch.getMeasurementValue().equals(Measurement.DEGREE_CELSIUS));

        assertTrue(repository.removeByMeasurementValue(Measurement.DEGREE_CELSIUS));
        assertNull(repository.get("0"));
        assertNull(repository.get("1"));
        assertNull(repository.get("2"));

        Collection<Channel> actual = repository.getAll();
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testRemoveByNotExistedMeasurementValue(){
        assertTrue(repository.removeByMeasurementValue(Measurement.MM_ACVA));

        Collection<Channel> actual = repository.getAll();
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }


    @Test
    public void testRewriteNotEmpty() {
        Channel channel0 = createChannel(0, Measurement.DEGREE_CELSIUS);
        Channel channel8 = createChannel(8, Measurement.CM_S);
        Channel channel9 = createChannel(9, Measurement.KPA);
        Channel channel5 = createChannel(5, Measurement.DEGREE_CELSIUS);
        Collection<Channel> toRewrite = Arrays.asList(channel0, channel8, null, channel9, channel5);
        expected = toRewrite.stream().filter(Objects::nonNull).collect(Collectors.toList());

        assertTrue(repository.rewrite(toRewrite));

        Collection<Channel> actual = repository.getAll();
        assertFalse(actual.stream().anyMatch(Objects::isNull));
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testRewriteEmpty() {
        assertTrue(repository.rewrite(new ArrayList<>()));
        assertEquals(0, repository.getAll().size());
    }

    @Test
    public void testChangeExistedMeasurementValue(){
        expected.forEach(ch -> {
            if (ch.getMeasurementValue().equals(Measurement.DEGREE_CELSIUS)) {
                ch.setMeasurementValue(Measurement.M3_HOUR);
            }
        });

        assertTrue(repository.changeMeasurementValue(Measurement.DEGREE_CELSIUS, Measurement.M3_HOUR));

        Collection<Channel> actual = repository.getAll();
        assertFalse(actual.stream().anyMatch(ch -> ch.getMeasurementValue().equals(Measurement.DEGREE_CELSIUS)));
        assertEquals(5, actual.stream().filter(ch -> ch.getMeasurementValue().equals(Measurement.M3_HOUR)).count());

        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testChangeNotExistedMeasurementValue(){
        assertTrue(repository.changeMeasurementValue(Measurement.PA, Measurement.DEGREE_CELSIUS));

        Collection<Channel> actual = repository.getAll();
        assertFalse(actual.stream().anyMatch(ch -> ch.getMeasurementValue().equals(Measurement.PA)));
        assertEquals(3, actual.stream().filter(ch -> ch.getMeasurementValue().equals(Measurement.DEGREE_CELSIUS)).count());

        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }


    @Test
    public void testSetSameExisted() {
        Channel channel2 = createChannel(2, Measurement.DEGREE_CELSIUS);
        assertTrue(repository.set(channel2, channel2));

        assertEquals(channel2, repository.get(channel2.getCode()));

        Collection<Channel> actual = repository.getAll();
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testSetSameNotExisted() {
        Channel channel8 = createChannel(8, Measurement.DEGREE_CELSIUS);
        assertFalse(repository.set(channel8, channel8));

        assertNull(repository.get(channel8.getCode()));

        Collection<Channel> actual = repository.getAll();
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testSetNew() {
        Channel channel2 = createChannel(2, Measurement.DEGREE_CELSIUS);
        Channel channel8 = createChannel(8, Measurement.DEGREE_CELSIUS);
        expected.set(2, channel8);

        assertTrue(repository.set(channel2, channel8));
        assertNull(repository.get(channel2.getCode()));
        assertEquals(channel8, repository.get(channel8.getCode()));

        Collection<Channel> actual = repository.getAll();
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testSetExisted() {
        Channel channel0 = createChannel(0, Measurement.DEGREE_CELSIUS);
        Channel channel2 = createChannel(2, Measurement.DEGREE_CELSIUS);

        assertFalse(repository.set(channel2, channel0));

        Collection<Channel> actual = repository.getAll();
        assertEquals(1, actual.stream().filter(ch -> ch.equals(channel0)).count());
        assertEquals(1, actual.stream().filter(ch -> ch.equals(channel2)).count());

        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testSetInsteadNotExisted() {
        Channel channel2 = createChannel(2, Measurement.DEGREE_CELSIUS);
        Channel channel8 = createChannel(8, Measurement.DEGREE_CELSIUS);

        assertFalse(repository.set(channel8, channel2));

        Collection<Channel> actual = repository.getAll();
        assertEquals(channel2, repository.get(channel2.getCode()));
        assertEquals(1, actual.stream().filter(ch -> ch.equals(channel2)).count());
        assertFalse(actual.stream().anyMatch(ch -> ch.equals(channel8)));

        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testClear() {
        assertTrue(repository.clear());
        assertEquals(0, repository.getAll().size());
    }

    @Test
    public void testImportDataWithNewAndChanging() {
        Channel channel0 = createChannel(0, Measurement.MM_ACVA);
        Channel channel1 = createChannel(1, Measurement.MM_ACVA);
        Channel channel2 = createChannel(2, Measurement.MM_ACVA);
        Channel channel8 = createChannel(8, Measurement.CM_S);
        Channel channel9 = createChannel(9, Measurement.CM_S);

        expected.forEach(ch -> {
            if (ch.getMeasurementValue().equals(Measurement.DEGREE_CELSIUS)) {
                ch.setMeasurementValue(Measurement.MM_ACVA);
            }
        });
        expected.addAll(Arrays.asList(channel8, channel9));

        assertTrue(repository.importData(Arrays.asList(channel8, null, channel9), Arrays.asList(channel0, channel1, null, channel2)));
        assertFalse(repository.getAll().stream().anyMatch(Objects::isNull));

        Channel actualChannel0 = repository.get(channel0.getCode());
        assertNotNull(actualChannel0);
        assertEquals(channel0, actualChannel0);
        assertEquals(channel0.getMeasurementValue(), actualChannel0.getMeasurementValue());

        Channel actualChannel1 = repository.get(channel1.getCode());
        assertNotNull(actualChannel1);
        assertEquals(channel1, actualChannel1);
        assertEquals(channel1.getMeasurementValue(), actualChannel1.getMeasurementValue());

        Channel actualChannel2 = repository.get(channel2.getCode());
        assertNotNull(actualChannel2);
        assertEquals(channel2, actualChannel2);
        assertEquals(channel2.getMeasurementValue(), actualChannel2.getMeasurementValue());

        assertEquals(channel8, repository.get(channel8.getCode()));
        assertEquals(channel9, repository.get(channel9.getCode()));

        Collection<Channel> actual = repository.getAll();
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testImportDataOnlyWithNew() {
        Channel channel8 = createChannel(8, Measurement.CM_S);
        Channel channel9 = createChannel(9, Measurement.CM_S);
        expected.addAll(Arrays.asList(channel8, channel9));

        assertTrue(repository.importData(Arrays.asList(channel8, null, channel9), new ArrayList<>()));
        assertFalse(repository.getAll().stream().anyMatch(Objects::isNull));

        assertEquals(channel8, repository.get(channel8.getCode()));
        assertEquals(channel9, repository.get(channel9.getCode()));

        Collection<Channel> actual = repository.getAll();
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testImportDataOnlyWithChanging() {
        Channel channel0 = createChannel(0, Measurement.MM_ACVA);
        Channel channel1 = createChannel(1, Measurement.MM_ACVA);
        Channel channel2 = createChannel(2, Measurement.MM_ACVA);
        expected.forEach(ch -> {
            if (ch.getMeasurementValue().equals(Measurement.DEGREE_CELSIUS)) {
                ch.setMeasurementValue(Measurement.MM_ACVA);
            }
        });

        assertTrue(repository.importData(new ArrayList<>(), Arrays.asList(channel0, channel1, null, channel2)));
        assertFalse(repository.getAll().stream().anyMatch(Objects::isNull));

        Channel actualChannel0 = repository.get(channel0.getCode());
        assertNotNull(actualChannel0);
        assertEquals(channel0, actualChannel0);
        assertEquals(channel0.getMeasurementValue(), actualChannel0.getMeasurementValue());

        Channel actualChannel1 = repository.get(channel1.getCode());
        assertNotNull(actualChannel1);
        assertEquals(channel1, actualChannel1);
        assertEquals(channel1.getMeasurementValue(), actualChannel1.getMeasurementValue());

        Channel actualChannel2 = repository.get(channel2.getCode());
        assertNotNull(actualChannel2);
        assertEquals(channel2, actualChannel2);
        assertEquals(channel2.getMeasurementValue(), actualChannel2.getMeasurementValue());

        Collection<Channel> actual = repository.getAll();
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testImportDataWithoutNewAndChanging() {
        assertTrue(repository.importData(new ArrayList<>(), new ArrayList<>()));

        Collection<Channel> actual = repository.getAll();
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testIsExists() {
        assertTrue(repository.isExist("0"));
        assertFalse(repository.isExist("9"));

        assertFalse(repository.isExist("1", "1"));
        assertFalse(repository.isExist("1", "9"));
        assertFalse(repository.isExist("9", "9"));
        assertTrue(repository.isExist("1", "2"));
        assertTrue(repository.isExist("9", "2"));
    }

    @Test
    public void testSearchByMonth() {
        SearchParams params = new SearchParams();
        params.month = 1;

        Collection<Channel> expected = Arrays.asList(
                createChannel(0, Measurement.DEGREE_CELSIUS),
                createChannel(2, Measurement.DEGREE_CELSIUS),
                createChannel(4, Measurement.KPA),
                createChannel(6, Measurement.M3_HOUR)
        );

        Collection<Channel> actual = repository.search(params);
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testSearchByMonthAndYear() {
        SearchParams params = new SearchParams();
        params.month = 1;
        params.year = 2004;

        Collection<Channel> expected = Collections.singletonList(createChannel(4, Measurement.KPA));

        Collection<Channel> actual = repository.search(params);
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testSearchByYear() {
        SearchParams params = new SearchParams();
        params.year = 2004;

        Collection<Channel> expected = Collections.singletonList(createChannel(4, Measurement.KPA));

        Collection<Channel> actual = repository.search(params);
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testSearchByAllPath() {
        SearchParams params = new SearchParams();
        params.locationZone = SearchParams.LOCATION_ZONE_ALL;
        params.locationValue = "4";

        Collection<Channel> expected = Collections.singletonList(createChannel(4, Measurement.KPA));

        Collection<Channel> actual = repository.search(params);
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testSearchByDepartments() {
        SearchParams params = new SearchParams();
        params.locationZone = SearchParams.LOCATION_ZONE_DEPARTMENT;
        params.locationValue = "Department4";

        Collection<Channel> expected = Collections.singletonList(createChannel(4, Measurement.KPA));

        Collection<Channel> actual = repository.search(params);
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testSearchByArea() {
        SearchParams params = new SearchParams();
        params.locationZone = SearchParams.LOCATION_ZONE_AREA;
        params.locationValue = "Area4";

        Collection<Channel> expected = Collections.singletonList(createChannel(4, Measurement.KPA));

        Collection<Channel> actual = repository.search(params);
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testSearchByProcess() {
        SearchParams params = new SearchParams();
        params.locationZone = SearchParams.LOCATION_ZONE_PROCESS;
        params.locationValue = "Process4";

        Collection<Channel> expected = Collections.singletonList(createChannel(4, Measurement.KPA));

        Collection<Channel> actual = repository.search(params);
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testSearchByInstallation() {
        SearchParams params = new SearchParams();
        params.locationZone = SearchParams.LOCATION_ZONE_INSTALLATION;
        params.locationValue = "Installation4";

        Collection<Channel> expected = Collections.singletonList(createChannel(4, Measurement.KPA));

        Collection<Channel> actual = repository.search(params);
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testSearchByAllPathAndMonth() {
        SearchParams params = new SearchParams();
        params.locationZone = SearchParams.LOCATION_ZONE_ALL;
        params.month = 2;
        params.locationValue = "4";

        Collection<Channel> actual = repository.search(params);
        assertEquals(0, actual.size());
    }
}