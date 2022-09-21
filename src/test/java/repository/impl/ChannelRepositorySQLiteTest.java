package repository.impl;

import model.Channel;
import model.Measurement;
import model.Sensor;
import org.junit.*;
import org.sqlite.JDBC;
import repository.ChannelRepository;
import repository.MeasurementRepository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

@SuppressWarnings("OptionalGetWithoutIsPresent")
public class ChannelRepositorySQLiteTest {

    private static final String DB_URL = "jdbc:sqlite:TestData.db";
    private static final MeasurementRepository measurementRepository = new MeasurementRepositorySQLite(DB_URL, null, null);
    private static final ChannelRepository repository = new ChannelRepositorySQLite(DB_URL, null, null, measurementRepository);

    private static Channel[] testChannels;

    private static Channel createChannel(int number, Sensor sensor, Measurement measurement){
        Channel channel = new Channel(String.valueOf(number));
        channel.setName("name" + number);
        channel.setMeasurement(measurement);
        channel.setSensor(sensor);
        return channel;
    }

    private static Sensor createSensor(int number, Measurement measurement){
        Sensor sensor = new Sensor("sensor" + number);
        sensor.setType("type" + number);
        sensor.setName("name" + number);
        sensor.setRange(number, number+100);
        sensor.setNumber(String.valueOf(number));
        sensor.setValue(measurement.getValue());
        sensor.setMeasurement(measurement.getName());
        sensor.setErrorFormula("2 * " + number);
        return sensor;
    }

    @BeforeClass
    public static void testCreateTable(){
        assertTrue(repository.createTable());
    }

    @AfterClass
    public static void removeTable() throws SQLException {
        String sql = "DROP TABLE channels;";
        DriverManager.registerDriver(new JDBC());
        try (Connection connection = DriverManager.getConnection(DB_URL);
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
        }
    }

    @Before
    public void fuelingTestDB() throws SQLException {
        Measurement temperature = new Measurement(Measurement.TEMPERATURE, Measurement.DEGREE_CELSIUS);
        Measurement consumption = new Measurement(Measurement.CONSUMPTION, Measurement.M3_HOUR);

        measurementRepository.add(temperature);
        measurementRepository.add(consumption);
        measurementRepository.add(new Measurement(Measurement.PRESSURE, Measurement.KPA));
        measurementRepository.add(new Measurement(Measurement.PRESSURE, Measurement.PA));

        testChannels = new Channel[7];
        for (int n = 0;n < 7;n++){
            if (n < 3) {
                Sensor temperatureS = createSensor(1, temperature);
                testChannels[n] = createChannel(n, temperatureS, temperature);
            }else if (n < 5) {
                Sensor pressureS = createSensor(2, new Measurement(Measurement.PRESSURE, Measurement.KPA));
                testChannels[n] = createChannel(n, pressureS, new Measurement(Measurement.PRESSURE, Measurement.PA));
            }else {
                Sensor consumptionS = createSensor(3, consumption);
                testChannels[n] = createChannel(n, consumptionS, consumption);
            }
        }

        String insertSql = "INSERT INTO channels (code, name, measurement_value, department, area, process, installation, date, frequency, " +
                "technology_number, sensor, protocol_number, reference, range_min, range_max, allowable_error_percent, allowable_error_value, " +
                "suitability) "
                + "VALUES ";
        StringBuilder sql = new StringBuilder(insertSql);

        for (Channel channel : testChannels) {
            sql.append("('").append(channel.getCode()).append("',")
                    .append("'").append(channel.getName()).append("',")
                    .append("'").append(channel.getMeasurement().getValue()).append("',")
                    .append("'").append(channel.getDepartment()).append("',")
                    .append("'").append(channel.getArea()).append("',")
                    .append("'").append(channel.getProcess()).append("',")
                    .append("'").append(channel.getInstallation()).append("',")
                    .append("'").append(channel.getDate()).append("',")
                    .append(channel.getFrequency()).append(",")
                    .append("'").append(channel.getTechnologyNumber()).append("',")
                    .append("'").append(channel.getSensor()).append("',")
                    .append("'").append(channel.getNumberOfProtocol()).append("',")
                    .append("'").append(channel.getReference()).append("',")
                    .append(channel.getRangeMin()).append(",")
                    .append(channel.getRangeMax()).append(",")
                    .append(channel.getAllowableErrorPercent()).append(",")
                    .append(channel.getAllowableError()).append(",")
                    .append("'").append(channel.isSuitability()).append("'),");
        }
        sql.setCharAt(sql.length()-1, ';');


        DriverManager.registerDriver(new JDBC());
        try (Connection connection = DriverManager.getConnection(DB_URL);
             Statement statement = connection.createStatement()) {
            statement.execute(sql.toString());
        }
    }

    @After
    public void clearTestDB() throws Exception {
        String sql = "DELETE FROM channels;";
        DriverManager.registerDriver(new JDBC());
        try (Connection connection = DriverManager.getConnection(DB_URL);
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
        }
        measurementRepository.clear();
    }

    @Test
    public void testGetAll() {
        assertArrayEquals(testChannels, repository.getAll().toArray(new Channel[0]));
        assertEquals("name0", repository.getAll().toArray(new Channel[0])[0].getName());
        assertEquals("name3", repository.getAll().toArray(new Channel[0])[3].getName());
        assertEquals("name6", repository.getAll().toArray(new Channel[0])[6].getName());
    }

    @Test
    public void testGetExisted() {
        assertEquals(testChannels[1], repository.get("1").get());
    }

    @Test
    public void testGetNotExisted() {
        assertFalse(repository.get("Not Existed").isPresent());
    }

    @Test
    public void testAddNotExisted() {
        Measurement temperature = new Measurement(Measurement.TEMPERATURE, Measurement.DEGREE_CELSIUS);
        Sensor sensor = createSensor(7, temperature);
        Channel toAdd = createChannel(7, sensor, temperature);
        Channel[] expected = Arrays.copyOf(testChannels, 8);
        expected[expected.length-1] = toAdd;

        assertTrue(repository.add(toAdd));
        assertTrue(repository.get("7").isPresent());
        assertArrayEquals(expected, repository.getAll().toArray(new Channel[0]));
    }

    @Test
    public void testAddExisted() {
        assertFalse(repository.add(testChannels[2]));
        assertArrayEquals(testChannels, repository.getAll().toArray(new Channel[0]));
    }

    @Test
    public void testRemoveExisted() {
        Channel[] expected = Arrays.copyOf(testChannels, 6);

        assertTrue(repository.remove(testChannels[6]));
        assertFalse(repository.get("6").isPresent());
        assertArrayEquals(expected, repository.getAll().toArray(new Channel[0]));
    }

    @Test
    public void testRemoveNotExisted() {
        assertFalse(repository.remove(createChannel(8, new Sensor(), null)));
        assertArrayEquals(testChannels, repository.getAll().toArray(new Channel[0]));
    }

    @Test
    public void testRemoveByExistedSensor(){
        Channel[] expected = Arrays.copyOf(testChannels, 5);

        assertTrue(repository.removeBySensor(createSensor(3, new Measurement(Measurement.CONSUMPTION, Measurement.M3_HOUR))));
        assertFalse(repository.get("6").isPresent());
        assertArrayEquals(expected, repository.getAll().toArray(new Channel[0]));
    }

    @Test
    public void testRemoveByNotExistedSensor(){
        assertTrue(repository.removeBySensor(createSensor(4, new Measurement())));
        assertArrayEquals(testChannels, repository.getAll().toArray(new Channel[0]));
    }

    @Test
    public void testRemoveByExistedMeasurementValue(){
        Channel[] expected = Arrays.copyOf(testChannels, 5);

        assertTrue(repository.removeByMeasurementValue(Measurement.M3_HOUR));
        assertFalse(repository.get("6").isPresent());
        assertArrayEquals(expected, repository.getAll().toArray(new Channel[0]));
    }

    @Test
    public void testRemoveByNotExistedMeasurementValue(){
        assertTrue(repository.removeByMeasurementValue(Measurement.KPA));
        assertArrayEquals(testChannels, repository.getAll().toArray(new Channel[0]));
    }


    @Test
    public void testRewriteNotEmpty() {
        Channel[] expected = new Channel[]{
                createChannel(8, new Sensor(), new Measurement()),
                createChannel(9, new Sensor(), new Measurement()),
                testChannels[0]
        };
        Channel[] toRewrite = new Channel[]{
                createChannel(8, new Sensor(), new Measurement()),
                null,
                createChannel(9, new Sensor(), new Measurement()),
                testChannels[0]
        };

        assertTrue(repository.rewrite(Arrays.asList(toRewrite)));
        assertArrayEquals(expected, repository.getAll().toArray(new Channel[0]));
    }

    @Test
    public void testRewriteEmpty() {
        assertTrue(repository.rewrite(new ArrayList<>()));
        assertArrayEquals(new Channel[0], repository.getAll().toArray(new Channel[0]));
    }

    @Test
    public void testChangeNewSensorWithoutIgnored(){
        Sensor toChange = createSensor(5, new Measurement());
        testChannels[0].setSensor(toChange);
        testChannels[1].setSensor(toChange);
        testChannels[2].setSensor(toChange);

        assertTrue(repository.changeSensor(createSensor(1, new Measurement(Measurement.TEMPERATURE, Measurement.DEGREE_CELSIUS)), toChange));
        assertEquals(toChange, repository.get("1").get().getSensor());
        assertEquals(toChange.getType(), repository.get("2").get().getSensor().getType());
        assertArrayEquals(testChannels, repository.getAll().toArray(new Channel[0]));
    }

    @Test
    public void testChangeNewSensorWithIgnored(){
        Sensor toChange = createSensor(5, new Measurement());

        Sensor toChangeExpected = createSensor(5, new Measurement());
        toChangeExpected.setType("type1");

        testChannels[0].setSensor(toChangeExpected);
        testChannels[1].setSensor(toChangeExpected);
        testChannels[2].setSensor(toChangeExpected);

        assertTrue(repository.changeSensor(
                createSensor(1, new Measurement(Measurement.TEMPERATURE, Measurement.DEGREE_CELSIUS)),
                toChange, Sensor.TYPE));

        assertEquals(toChangeExpected, repository.get("1").get().getSensor());
        assertEquals(toChangeExpected.getType(), repository.get("2").get().getSensor().getType());
        assertArrayEquals(testChannels, repository.getAll().toArray(new Channel[0]));
    }

    @Test
    public void testChangeExistedSensor(){
        assertTrue(repository.changeSensor(testChannels[0].getSensor(), testChannels[1].getSensor()));
        assertEquals(testChannels[0].getSensor(), repository.get("0").get().getSensor());
        assertEquals(testChannels[1].getSensor().getType(), repository.get("1").get().getSensor().getType());
        assertArrayEquals(testChannels, repository.getAll().toArray(new Channel[0]));
    }

    @Test
    public void testChangeSensors(){
        Sensor toChange012 = createSensor(5, new Measurement());
        toChange012.setName("name1");
        Sensor toChange345 = createSensor(6, new Measurement());
        toChange345.setName("name2");
        List<Sensor> toChange = Arrays.asList(toChange012, null, toChange345, testChannels[6].getSensor());

        testChannels[0].setSensor(toChange012);
        testChannels[1].setSensor(toChange012);
        testChannels[2].setSensor(toChange012);
        testChannels[3].setSensor(toChange345);
        testChannels[4].setSensor(toChange345);
        testChannels[5].setSensor(toChange345);

        assertTrue(repository.changeSensors(toChange));
        assertEquals(toChange012, repository.get("1").get().getSensor());
        assertEquals(toChange345.getType(), repository.get("4").get().getSensor().getType());
        assertArrayEquals(testChannels, repository.getAll().toArray(new Channel[0]));
    }

    @Test
    public void testChangeExistedMeasurementValueToNew(){
        measurementRepository.add(new Measurement(Measurement.PRESSURE, Measurement.ML_BAR));
        testChannels[3].getMeasurement().setValue(Measurement.ML_BAR);
        testChannels[4].getMeasurement().setValue(Measurement.ML_BAR);
        testChannels[5].getMeasurement().setValue(Measurement.ML_BAR);

        assertTrue(repository.changeMeasurementValue(Measurement.PA, Measurement.ML_BAR));
        assertEquals(Measurement.ML_BAR, repository.get("4").get().getMeasurement().getValue());
        assertArrayEquals(testChannels, repository.getAll().toArray(new Channel[0]));
    }

    @Test
    public void testChangeExistedMeasurementValueToExisted(){
        assertTrue(repository.changeMeasurementValue(Measurement.PA, Measurement.DEGREE_CELSIUS));
        assertEquals(Measurement.DEGREE_CELSIUS, repository.get("1").get().getMeasurement().getValue());
        assertEquals(Measurement.DEGREE_CELSIUS, repository.get("4").get().getMeasurement().getValue());
        assertArrayEquals(testChannels, repository.getAll().toArray(new Channel[0]));
    }

    @Test
    public void testChangeNotExistedMeasurementValueToNew(){
        assertTrue(repository.changeMeasurementValue(Measurement.KPA, Measurement.ML_BAR));
        assertEquals(Measurement.PA, repository.get("4").get().getMeasurement().getValue());
        assertArrayEquals(testChannels, repository.getAll().toArray(new Channel[0]));
    }

    @Test
    public void testChangeNotExistedMeasurementValueToExisted(){
        assertTrue(repository.changeMeasurementValue(Measurement.KPA, Measurement.DEGREE_CELSIUS));
        assertEquals(Measurement.DEGREE_CELSIUS, repository.get("1").get().getMeasurement().getValue());
        assertEquals(Measurement.PA, repository.get("4").get().getMeasurement().getValue());
        assertArrayEquals(testChannels, repository.getAll().toArray(new Channel[0]));
    }

    @Test
    public void testSetSame() {
        assertTrue(repository.set(testChannels[2], testChannels[2]));
        assertArrayEquals(testChannels, repository.getAll().toArray(new Channel[0]));
    }

    @Test
    public void testSetNew() {
        Channel toChange = createChannel(8, new Sensor(), new Measurement());

        assertTrue(repository.set(testChannels[2], toChange));
        testChannels[2] = toChange;

        assertFalse(repository.get("2").isPresent());
        assertTrue(repository.get("8").isPresent());
        assertArrayEquals(testChannels, repository.getAll().toArray(new Channel[0]));
    }

    @Test
    public void testSetExisted() {
        assertFalse(repository.set(testChannels[2], testChannels[0]));
        assertTrue(repository.get("2").isPresent());
        assertTrue(repository.get("0").isPresent());
        assertArrayEquals(testChannels, repository.getAll().toArray(new Channel[0]));
    }

    @Test
    public void testSetInsteadNotExisted() {
        assertFalse(repository.set(createChannel(8, new Sensor(), null), testChannels[0]));
        assertFalse(repository.get("8").isPresent());
        assertTrue(repository.get("0").isPresent());
        assertArrayEquals(testChannels, repository.getAll().toArray(new Channel[0]));
    }

    @Test
    public void testClear() {
        assertTrue(repository.clear());
        assertArrayEquals(new Channel[0], repository.getAll().toArray(new Channel[0]));
    }

    @Test
    public void testImportDataWithNewAndChanging() {
        testChannels[2].setInstallation("New Installation");
        testChannels[4].setReference("New Reference");
        Channel[] expected = Arrays.copyOf(testChannels, 9);
        expected[expected.length-2] = createChannel(8, new Sensor(), new Measurement());
        expected[expected.length-1] = createChannel(9, new Sensor(), new Measurement());

        ArrayList<Channel>forChange = new ArrayList<>();
        forChange.add(testChannels[2]);
        forChange.add(null);
        forChange.add(testChannels[4]);

        ArrayList<Channel>newCh = new ArrayList<>();
        newCh.add(createChannel(8, new Sensor(), new Measurement()));
        newCh.add(null);
        newCh.add(createChannel(9, new Sensor(), new Measurement()));

        assertTrue(repository.importData(newCh, forChange));
        assertEquals(testChannels[2].getInstallation(), repository.get("2").get().getInstallation());
        assertEquals(testChannels[4].getReference(), repository.get("4").get().getReference());
        assertTrue(repository.get("9").isPresent());
        assertArrayEquals(expected, repository.getAll().toArray(new Channel[0]));
    }

    @Test
    public void testImportDataOnlyWithNew() {
        Channel[] expected = Arrays.copyOf(testChannels, 9);
        expected[expected.length-2] = createChannel(8, new Sensor(), new Measurement());
        expected[expected.length-1] = createChannel(9, new Sensor(), new Measurement());

        ArrayList<Channel>newCh = new ArrayList<>();
        newCh.add(createChannel(8, new Sensor(), new Measurement()));
        newCh.add(createChannel(9, new Sensor(), new Measurement()));

        assertTrue(repository.importData(newCh, new ArrayList<>()));
        assertTrue(repository.get("9").isPresent());
        assertArrayEquals(expected, repository.getAll().toArray(new Channel[0]));
    }

    @Test
    public void testImportDataOnlyWithChanging() {
        testChannels[2].setInstallation("New Installation");
        testChannels[4].setReference("New Reference");

        ArrayList<Channel>forChange = new ArrayList<>();
        forChange.add(testChannels[2]);
        forChange.add(testChannels[4]);

        assertTrue(repository.importData(new ArrayList<>(), forChange));
        assertEquals(testChannels[2].getInstallation(), repository.get("2").get().getInstallation());
        assertEquals(testChannels[4].getReference(), repository.get("4").get().getReference());
        assertArrayEquals(testChannels, repository.getAll().toArray(new Channel[0]));
    }

    @Test
    public void testImportDataWithoutNewAndChanging() {
        assertTrue(repository.importData(new ArrayList<>(), new ArrayList<>()));
        assertArrayEquals(testChannels, repository.getAll().toArray(new Channel[0]));
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