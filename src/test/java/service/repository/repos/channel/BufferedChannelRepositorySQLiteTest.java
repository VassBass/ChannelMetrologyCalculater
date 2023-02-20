package service.repository.repos.channel;

import model.Channel;
import model.Measurement;
import model.Sensor;
import model.builder.ChannelBuilder;
import org.junit.*;
import org.sqlite.JDBC;
import service.json.JacksonJsonObjectMapper;
import service.json.JsonObjectMapper;
import service.repository.config.RepositoryConfigHolder;
import service.repository.config.SqliteRepositoryConfigHolder;
import service.repository.connection.RepositoryDBConnector;
import service.repository.connection.SqliteRepositoryDBConnector;

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
    private static final String TEST_REPOSITORY_PROPERTIES_FILE = "properties/test_repository.properties";
    private static final File TEST_DB_FILE = new File("TestData.db");
    private static final String TEST_DB_URL = "jdbc:sqlite:TestData.db";
    private static final String TABLE_NAME = "channels";

    private static List<Channel> expected;

    private static Channel createChannel(int number, String sensorName, String measurementValue){
        return new ChannelBuilder(String.valueOf(number))
                .setName("name" + number)
                .setMeasurementValue(measurementValue)
                .setSensorName(sensorName)
                .build();
    }

    private final JsonObjectMapper jsonMapper = JacksonJsonObjectMapper.getInstance();
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
                + ", sensor_name text NOT NULL"
                + ", frequency real NOT NULL"
                + ", range_min real NOT NULL"
                + ", range_max real NOT NULL"
                + ", allowable_error_percent real NOT NULL"
                + ", allowable_error_value real NOT NULL"
                + ", control_points text NOT NULL"
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
        DriverManager.registerDriver(new JDBC());
        try (Connection connection = DriverManager.getConnection(TEST_DB_URL);
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
        }
    }

    @Before
    public void init() throws SQLException {
        expected = new ArrayList<>(7);
        expected.add(createChannel(0, Sensor.TCM_50M, Measurement.DEGREE_CELSIUS));
        expected.add(createChannel(1, Sensor.TCM_50M, Measurement.DEGREE_CELSIUS));
        expected.add(createChannel(2, Sensor.TCM_50M, Measurement.DEGREE_CELSIUS));
        expected.add(createChannel(3, Sensor.YOKOGAWA, Measurement.KPA));
        expected.add(createChannel(4, Sensor.YOKOGAWA, Measurement.KPA));
        expected.add(createChannel(5, Sensor.ROSEMOUNT, Measurement.M3_HOUR));
        expected.add(createChannel(6, Sensor.ROSEMOUNT, Measurement.M3_HOUR));

        String sql = String.format("INSERT INTO %s (code, name, department, area, process, installation, technology_number" +
                ", protocol_number, reference, date, suitability, measurement_name, measurement_value, sensor_name, frequency" +
                ", range_min, range_max, allowable_error_percent, allowable_error_value, control_points) "
                + "VALUES ", TABLE_NAME);
        StringBuilder sqlBuilder = new StringBuilder(sql);

        for (Channel channel : expected) {
            String values = String.format(
                    "('%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', %s, %s, %s, %s, %s, '%s'),",
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
                    channel.getSensorName(),
                    channel.getFrequency(),
                    channel.getRangeMin(),
                    channel.getRangeMax(),
                    channel.getAllowableErrorPercent(),
                    channel.getAllowableError(),
                    jsonMapper.objectToJson(channel.getControlPoints())
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
        Channel channel7 = createChannel(7, Sensor.Pt100, Measurement.DEGREE_CELSIUS);
        expected.add(channel7);

        assertTrue(repository.add(channel7));
        assertEquals(channel7, repository.get("7"));

        Collection<Channel> actual = repository.getAll();
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testAddExisted() {
        Channel channel2 = createChannel(2, Sensor.TCM_50M, Measurement.DEGREE_CELSIUS);

        assertFalse(repository.add(channel2));
        assertEquals(channel2, repository.get("2"));

        Collection<Channel> actual = repository.getAll();
        assertEquals(1L, actual.stream().filter(ch -> ch.equals(channel2)).count());
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testRemoveExisted() {
        Channel channel2 = createChannel(2, Sensor.TCM_50M, Measurement.DEGREE_CELSIUS);
        expected.remove(channel2);

        assertTrue(repository.remove(channel2));
        assertNull(repository.get(channel2.getCode()));

        Collection<Channel> actual = repository.getAll();
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testRemoveNotExisted() {
        Channel channel7 = createChannel(7, Sensor.Pt100, Measurement.DEGREE_CELSIUS);

        assertFalse(repository.remove(channel7));
        assertNull(repository.get(channel7.getCode()));

        Collection<Channel> actual = repository.getAll();
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testRemoveByExistedSensorName(){
        expected.removeIf(ch -> ch.getSensorName().equals(Sensor.TCM_50M));

        assertTrue(repository.removeBySensorName(Sensor.TCM_50M));
        assertNull(repository.get("0"));
        assertNull(repository.get("1"));
        assertNull(repository.get("2"));

        Collection<Channel> actual = repository.getAll();
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testRemoveByNotExistedSensorName(){
        assertTrue(repository.removeBySensorName(Sensor.Pt100));

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
        Channel channel0 = createChannel(0, Sensor.TCM_50M, Measurement.DEGREE_CELSIUS);
        Channel channel8 = createChannel(8, Sensor.ROSEMOUNT, Measurement.CM_S);
        Channel channel9 = createChannel(9, Sensor.YOKOGAWA, Measurement.KPA);
        Channel channel5 = createChannel(5, Sensor.Pt100, Measurement.DEGREE_CELSIUS);
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
    public void testChangeExistedSensorName(){
        expected.forEach(ch -> {
            if (ch.getSensorName().equals(Sensor.TCM_50M)) ch.setSensorName(Sensor.YOKOGAWA);
        });

        assertTrue(repository.changeSensorName(Sensor.TCM_50M, Sensor.YOKOGAWA));

        Collection<Channel> actual = repository.getAll();
        assertFalse(actual.stream().anyMatch(ch -> ch.getSensorName().equals(Sensor.TCM_50M)));
        assertEquals(5, actual.stream().filter(ch -> ch.getSensorName().equals(Sensor.YOKOGAWA)).count());

        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testChangeNotExistedSensorName(){
        assertTrue(repository.changeSensorName(Sensor.Pt100, Sensor.TCM_50M));

        Collection<Channel> actual = repository.getAll();
        assertFalse(actual.stream().anyMatch(ch -> ch.getSensorName().equals(Sensor.Pt100)));
        assertEquals(3, actual.stream().filter(ch -> ch.getSensorName().equals(Sensor.TCM_50M)).count());

        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
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
        Channel channel2 = createChannel(2, Sensor.TCM_50M, Measurement.DEGREE_CELSIUS);
        assertTrue(repository.set(channel2, channel2));

        assertEquals(channel2, repository.get(channel2.getCode()));

        Collection<Channel> actual = repository.getAll();
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testSetSameNotExisted() {
        Channel channel8 = createChannel(8, Sensor.Pt100, Measurement.DEGREE_CELSIUS);
        assertFalse(repository.set(channel8, channel8));

        assertNull(repository.get(channel8.getCode()));

        Collection<Channel> actual = repository.getAll();
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testSetNew() {
        Channel channel2 = createChannel(2, Sensor.TCM_50M, Measurement.DEGREE_CELSIUS);
        Channel channel8 = createChannel(8, Sensor.Pt100, Measurement.DEGREE_CELSIUS);
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
        Channel channel0 = createChannel(0, Sensor.TCM_50M, Measurement.DEGREE_CELSIUS);
        Channel channel2 = createChannel(2, Sensor.TCM_50M, Measurement.DEGREE_CELSIUS);

        assertFalse(repository.set(channel2, channel0));

        Collection<Channel> actual = repository.getAll();
        assertEquals(1, actual.stream().filter(ch -> ch.equals(channel0)).count());
        assertEquals(1, actual.stream().filter(ch -> ch.equals(channel2)).count());

        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testSetInsteadNotExisted() {
        Channel channel2 = createChannel(2, Sensor.TCM_50M, Measurement.DEGREE_CELSIUS);
        Channel channel8 = createChannel(8, Sensor.Pt100, Measurement.DEGREE_CELSIUS);

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
        Channel channel0 = createChannel(0, Sensor.Pt100, Measurement.DEGREE_CELSIUS);
        Channel channel1 = createChannel(1, Sensor.Pt100, Measurement.DEGREE_CELSIUS);
        Channel channel2 = createChannel(2, Sensor.Pt100, Measurement.DEGREE_CELSIUS);
        Channel channel8 = createChannel(8, Sensor.ROSEMOUNT, Measurement.CM_S);
        Channel channel9 = createChannel(9, Sensor.ROSEMOUNT, Measurement.CM_S);

        expected.forEach(ch -> {
            if (ch.getSensorName().equals(Sensor.TCM_50M)) {
                ch.setSensorName(Sensor.Pt100);
            }
        });
        expected.addAll(Arrays.asList(channel8, channel9));

        assertTrue(repository.importData(Arrays.asList(channel8, null, channel9), Arrays.asList(channel0, channel1, null, channel2)));
        assertFalse(repository.getAll().stream().anyMatch(Objects::isNull));

        Channel actualChannel0 = repository.get(channel0.getCode());
        assertNotNull(actualChannel0);
        assertEquals(channel0, actualChannel0);
        assertEquals(channel0.getSensorName(), actualChannel0.getSensorName());

        Channel actualChannel1 = repository.get(channel1.getCode());
        assertNotNull(actualChannel1);
        assertEquals(channel1, actualChannel1);
        assertEquals(channel1.getSensorName(), actualChannel1.getSensorName());

        Channel actualChannel2 = repository.get(channel2.getCode());
        assertNotNull(actualChannel2);
        assertEquals(channel2, actualChannel2);
        assertEquals(channel2.getSensorName(), actualChannel2.getSensorName());

        assertEquals(channel8, repository.get(channel8.getCode()));
        assertEquals(channel9, repository.get(channel9.getCode()));

        Collection<Channel> actual = repository.getAll();
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void testImportDataOnlyWithNew() {
        Channel channel8 = createChannel(8, Sensor.ROSEMOUNT, Measurement.CM_S);
        Channel channel9 = createChannel(9, Sensor.ROSEMOUNT, Measurement.CM_S);
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
        Channel channel0 = createChannel(0, Sensor.Pt100, Measurement.DEGREE_CELSIUS);
        Channel channel1 = createChannel(1, Sensor.Pt100, Measurement.DEGREE_CELSIUS);
        Channel channel2 = createChannel(2, Sensor.Pt100, Measurement.DEGREE_CELSIUS);
        expected.forEach(ch -> {
            if (ch.getSensorName().equals(Sensor.TCM_50M)) {
                ch.setSensorName(Sensor.Pt100);
            }
        });

        assertTrue(repository.importData(new ArrayList<>(), Arrays.asList(channel0, channel1, null, channel2)));
        assertFalse(repository.getAll().stream().anyMatch(Objects::isNull));

        Channel actualChannel0 = repository.get(channel0.getCode());
        assertNotNull(actualChannel0);
        assertEquals(channel0, actualChannel0);
        assertEquals(channel0.getSensorName(), actualChannel0.getSensorName());

        Channel actualChannel1 = repository.get(channel1.getCode());
        assertNotNull(actualChannel1);
        assertEquals(channel1, actualChannel1);
        assertEquals(channel1.getSensorName(), actualChannel1.getSensorName());

        Channel actualChannel2 = repository.get(channel2.getCode());
        assertNotNull(actualChannel2);
        assertEquals(channel2, actualChannel2);
        assertEquals(channel2.getSensorName(), actualChannel2.getSensorName());

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
}