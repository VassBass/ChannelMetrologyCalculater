package service;

import com.fasterxml.jackson.core.JsonProcessingException;
import model.Channel;
import model.Measurement;
import model.Sensor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sqlite.JDBC;
import service.impl.ChannelServiceImpl;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class ChannelServiceTest {
    private static final String DB_URL = "jdbc:sqlite:TestData.db";
    private static final ChannelService service = new ChannelServiceImpl(DB_URL);

    private static Sensor sensorTemperature(){
        Sensor sensor = new Sensor();
        sensor.setType("T");
        sensor.setName("T");
        sensor.setRange(0D, 100D);
        sensor.setNumber("T");
        sensor.setValue(Measurement.DEGREE_CELSIUS);
        sensor.setMeasurement(Measurement.TEMPERATURE);
        sensor.setErrorFormula("r");
        return sensor;
    }

    private static Sensor sensorPressureKPA(){
        Sensor sensor = new Sensor();
        sensor.setType("P");
        sensor.setName("P");
        sensor.setRange(0D, 200D);
        sensor.setNumber("P");
        sensor.setValue(Measurement.KPA);
        sensor.setMeasurement(Measurement.PRESSURE);
        sensor.setErrorFormula("r");
        return sensor;
    }

    private static Sensor sensorPressureBAR(){
        Sensor sensor = new Sensor();
        sensor.setType("P");
        sensor.setName("P");
        sensor.setRange(0D, 300D);
        sensor.setNumber("P");
        sensor.setValue(Measurement.BAR);
        sensor.setMeasurement(Measurement.PRESSURE);
        sensor.setErrorFormula("r");
        return sensor;
    }

    private static Sensor sensorConsumptionM3(){
        Sensor sensor = new Sensor();
        sensor.setType("C");
        sensor.setName("C");
        sensor.setRange(0D, 400D);
        sensor.setNumber("C");
        sensor.setValue(Measurement.M3_HOUR);
        sensor.setMeasurement(Measurement.CONSUMPTION);
        sensor.setErrorFormula("r");
        return sensor;
    }

    private static Sensor sensorConsumptionCM(){
        Sensor sensor = new Sensor();
        sensor.setType("C");
        sensor.setName("C");
        sensor.setRange(0D, 500D);
        sensor.setNumber("C");
        sensor.setValue(Measurement.CM_S);
        sensor.setMeasurement(Measurement.CONSUMPTION);
        sensor.setErrorFormula("r");
        return sensor;
    }

    private static Channel channelTemperature(){
        Channel channel = new Channel();
        channel.setCode("1");
        channel.setName("t");
        channel.setMeasurement(new Measurement(Measurement.TEMPERATURE, Measurement.DEGREE_CELSIUS));
        channel.setDepartment("t");
        channel.setArea("t");
        channel.setProcess("t");
        channel.setInstallation("t");
        channel.setDate("23.02.2022");
        channel.setFrequency(2D);
        channel.setTechnologyNumber("t");
        channel.setSensor(sensorTemperature());
        channel.setNumberOfProtocol("t");
        channel.setReference(null);
        channel.setRange(0D, 1000D);
        channel.setAllowableError(2D, 20D);
        channel.setSuitability(true);
        return channel;
    }

    private static Channel channelPressureKPA(){
        Channel channel = new Channel();
        channel.setCode("2");
        channel.setName("p");
        channel.setMeasurement(new Measurement(Measurement.PRESSURE, Measurement.KPA));
        channel.setDepartment("p");
        channel.setArea("p");
        channel.setProcess("p");
        channel.setInstallation("p");
        channel.setDate("23.02.2022");
        channel.setFrequency(2D);
        channel.setTechnologyNumber("p");
        channel.setSensor(sensorPressureKPA());
        channel.setNumberOfProtocol("p");
        channel.setReference(null);
        channel.setRange(0D, 1000D);
        channel.setAllowableError(2D, 20D);
        channel.setSuitability(true);
        return channel;
    }

    private static Channel channelPressureBAR(){
        Channel channel = new Channel();
        channel.setCode("3");
        channel.setName("p");
        channel.setMeasurement(new Measurement(Measurement.PRESSURE, Measurement.BAR));
        channel.setDepartment("p");
        channel.setArea("p");
        channel.setProcess("p");
        channel.setInstallation("p");
        channel.setDate("23.02.2022");
        channel.setFrequency(2D);
        channel.setTechnologyNumber("p");
        channel.setSensor(sensorPressureBAR());
        channel.setNumberOfProtocol("p");
        channel.setReference(null);
        channel.setRange(0D, 1000D);
        channel.setAllowableError(2D, 20D);
        channel.setSuitability(true);
        return channel;
    }

    private static Channel channelConsumptionM3(){
        Channel channel = new Channel();
        channel.setCode("4");
        channel.setName("c");
        channel.setMeasurement(new Measurement(Measurement.CONSUMPTION, Measurement.M3_HOUR));
        channel.setDepartment("c");
        channel.setArea("c");
        channel.setProcess("c");
        channel.setInstallation("c");
        channel.setDate("23.02.2022");
        channel.setFrequency(2D);
        channel.setTechnologyNumber("c");
        channel.setSensor(sensorConsumptionM3());
        channel.setNumberOfProtocol("c");
        channel.setReference(null);
        channel.setRange(0D, 1000D);
        channel.setAllowableError(2D, 20D);
        channel.setSuitability(true);
        return channel;
    }

    private static Channel channelConsumptionCM(){
        Channel channel = new Channel();
        channel.setCode("5");
        channel.setName("c");
        channel.setMeasurement(new Measurement(Measurement.CONSUMPTION, Measurement.CM_S));
        channel.setDepartment("c");
        channel.setArea("c");
        channel.setProcess("c");
        channel.setInstallation("c");
        channel.setDate("23.02.2022");
        channel.setFrequency(2D);
        channel.setTechnologyNumber("c");
        channel.setSensor(sensorConsumptionCM());
        channel.setNumberOfProtocol("c");
        channel.setReference(null);
        channel.setRange(0D, 1000D);
        channel.setAllowableError(2D, 20D);
        channel.setSuitability(true);
        return channel;
    }

    private static ArrayList<Channel> getTestList(){
        ArrayList<Channel>list = new ArrayList<>();
        list.add(channelTemperature());
        list.add(channelPressureKPA());
        list.add(channelPressureBAR());
        return list;
    }

    private static ArrayList<Channel> getAllFromDB() {
        ArrayList<Channel>channels = new ArrayList<>();
        String sql = "SELECT * FROM channels";
        try {
            DriverManager.registerDriver(new JDBC());
            try (Connection connection = DriverManager.getConnection(DB_URL);
                 Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(sql)) {
                while (resultSet.next()) {
                    Channel channel = new Channel();
                    channel.setCode(resultSet.getString("code"));
                    channel.setName(resultSet.getString("name"));
                    channel.setMeasurement(Measurement.fromString(resultSet.getString("measurement")));
                    channel.setDepartment(resultSet.getString("department"));
                    channel.setArea(resultSet.getString("area"));
                    channel.setProcess(resultSet.getString("process"));
                    channel.setInstallation(resultSet.getString("installation"));
                    channel.setDate(resultSet.getString("date"));
                    channel.setFrequency(resultSet.getDouble("frequency"));
                    channel.setTechnologyNumber(resultSet.getString("technology_number"));
                    channel.setSensor(Sensor.fromString(resultSet.getString("sensor")));
                    channel.setNumberOfProtocol(resultSet.getString("protocol_number"));
                    channel.setReference(resultSet.getString("reference"));
                    channel.setRangeMin(resultSet.getDouble("range_min"));
                    channel.setRangeMax(resultSet.getDouble("range_max"));
                    double allowableErrorPercent = resultSet.getDouble("allowable_error_percent");
                    double allowableErrorValue = resultSet.getDouble("allowable_error_value");
                    channel.setAllowableError(allowableErrorPercent, allowableErrorValue);
                    channel.setSuitability(Boolean.parseBoolean(resultSet.getString("suitability")));

                    channels.add(channel);
                }
            }
        }catch (SQLException | JsonProcessingException e){
            e.printStackTrace();
        }
        return channels;
    }

    private static long howLong(Date start, Date end){
        return end.getTime() - start.getTime();
    }

    private static boolean isBackgroundTaskIsRun() {
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
            return true;
        }
        return service.backgroundTaskIsRun();
    }

    @BeforeEach
    void setUp() {
        Date start = new Date();

        service.init();
        service.clear();
        while (isBackgroundTaskIsRun());
        service.addInCurrentThread(channelTemperature());
        service.addInCurrentThread(channelPressureKPA());
        service.addInCurrentThread(channelPressureBAR());

        System.out.println("setUp() duration = " + howLong(start, new Date()) + " mills");
    }

    @Test
    void getAll() {
        Date start = new Date();

        ArrayList<Channel>testList = getTestList();

        assertIterableEquals(testList, service.getAll());
        assertIterableEquals(testList, getAllFromDB());

        System.out.println("getAll() duration = " + howLong(start, new Date()) + " mills");
    }

    @Test
    void get() {
        Date start = new Date();

        String notExistsTestCode = "N";
        Channel testChannel = channelTemperature();

        assertEquals(testChannel, service.get(testChannel.getCode()));
        assertNull(service.get(notExistsTestCode));
        assertNull(service.get(null));

        System.out.println("get() duration = " + howLong(start, new Date()) + " mills");
    }

    @Test
    void add() {
        Date start = new Date();

        ArrayList<Channel>testList = getTestList();
        testList.add(channelConsumptionM3());

        ArrayList<Channel> channels = service.add(channelConsumptionM3());
        assertIterableEquals(testList, channels);
        while (isBackgroundTaskIsRun());
        channels = service.add(null);
        assertIterableEquals(testList, channels);
        while (isBackgroundTaskIsRun());
        assertIterableEquals(testList, getAllFromDB());

        System.out.println("add() duration = " + howLong(start, new Date()) + " mills");
    }

    @Test
    void addInCurrentThread() {
        Date start = new Date();

        ArrayList<Channel>testList = getTestList();
        testList.add(channelConsumptionM3());

        service.addInCurrentThread(channelConsumptionM3());
        assertIterableEquals(testList, service.getAll());
        service.addInCurrentThread(null);
        assertIterableEquals(testList, service.getAll());
        assertIterableEquals(testList, getAllFromDB());

        System.out.println("addInCurrentThread() duration = " + howLong(start, new Date()) + " mills");
    }

    @Test
    void remove() {
        Date start = new Date();

        ArrayList<Channel>testList = getTestList();
        testList.remove(channelTemperature());

        ArrayList<Channel>channels = service.remove(channelTemperature());
        assertIterableEquals(testList, channels);
        while (isBackgroundTaskIsRun());
        channels = service.remove(channelConsumptionM3());
        assertIterableEquals(testList, channels);
        while (isBackgroundTaskIsRun());
        channels = service.remove(null);
        assertIterableEquals(testList, channels);
        while (isBackgroundTaskIsRun());
        assertIterableEquals(testList, getAllFromDB());

        System.out.println("remove() duration = " + howLong(start, new Date()) + " mills");
    }

    @Test
    void removeBySensorInCurrentThread() {
        Date start = new Date();

        ArrayList<Channel>testList = getTestList();
        testList.remove(channelPressureKPA());
        testList.remove(channelPressureBAR());

        service.removeBySensorInCurrentThread(sensorPressureKPA());
        assertIterableEquals(testList, service.getAll());
        service.removeBySensorInCurrentThread(sensorConsumptionM3());
        assertIterableEquals(testList, service.getAll());
        service.removeBySensorInCurrentThread(null);
        assertIterableEquals(testList, service.getAll());
        assertIterableEquals(testList, getAllFromDB());

        System.out.println("removeBySensorInCurrentThread() duration = " + howLong(start, new Date()) + " mills");
    }

    @Test
    void changeSensorInCurrentThread() {
        Date start = new Date();

        ArrayList<Channel>testList = getTestList();
        int index = testList.indexOf(channelPressureKPA());
        testList.get(index).setSensor(sensorConsumptionM3());
        index = testList.indexOf(channelPressureBAR());
        testList.get(index).setSensor(sensorConsumptionM3());

        service.changeSensorInCurrentThread(sensorPressureKPA(), sensorConsumptionM3());
        assertIterableEquals(testList, service.getAll());
        service.changeSensorInCurrentThread(sensorPressureKPA(), sensorTemperature());
        assertIterableEquals(testList, service.getAll());
        service.changeSensorInCurrentThread(null, sensorTemperature());
        assertIterableEquals(testList, service.getAll());
        service.changeSensorInCurrentThread(sensorConsumptionM3(), null);
        assertIterableEquals(testList, service.getAll());
        service.changeSensorInCurrentThread(null, null);
        assertIterableEquals(testList, service.getAll());
        assertIterableEquals(testList, getAllFromDB());

        System.out.println("changeSensorInCurrentThread() duration = " + howLong(start, new Date()) + " mills");
    }

    @Test
    void set() {
        Date start = new Date();

        ArrayList<Channel>testList = getTestList();
        testList.set(0, channelConsumptionM3());

        ArrayList<Channel>channels = service.set(channelTemperature(), channelConsumptionM3());
        assertIterableEquals(testList, channels);
        while (isBackgroundTaskIsRun());
        channels = service.set(channelTemperature(), channelConsumptionCM());
        assertIterableEquals(testList, channels);
        while (isBackgroundTaskIsRun());
        channels = service.set(channelPressureKPA(), channelPressureBAR());
        assertIterableEquals(testList, channels);
        while (isBackgroundTaskIsRun());
        channels = service.set(channelTemperature(), channelPressureBAR());
        assertIterableEquals(testList, channels);
        while (isBackgroundTaskIsRun());
        channels = service.set(null, channelTemperature());
        assertIterableEquals(testList, channels);
        while (isBackgroundTaskIsRun());
        channels = service.set(null, channelPressureBAR());
        assertIterableEquals(testList, channels);
        while (isBackgroundTaskIsRun());
        channels = service.set(channelPressureKPA(), null);
        assertIterableEquals(testList, channels);
        while (isBackgroundTaskIsRun());
        channels = service.set(channelTemperature(), null);
        assertIterableEquals(testList, channels);
        while (isBackgroundTaskIsRun());
        channels = service.set(null, null);
        assertIterableEquals(testList, channels);
        while (isBackgroundTaskIsRun());
        assertIterableEquals(testList, getAllFromDB());

        System.out.println("set() duration = " + howLong(start, new Date()) + " mills");
    }

    @Test
    void setInCurrentThread() {
        Date start = new Date();

        ArrayList<Channel>testList = getTestList();
        testList.set(0, channelConsumptionM3());

        service.setInCurrentThread(channelTemperature(), channelConsumptionM3());
        assertIterableEquals(testList, service.getAll());
        service.setInCurrentThread(channelTemperature(), channelConsumptionCM());
        assertIterableEquals(testList, service.getAll());
        service.setInCurrentThread(channelPressureKPA(), channelPressureBAR());
        assertIterableEquals(testList, service.getAll());
        service.setInCurrentThread(channelTemperature(), channelPressureBAR());
        assertIterableEquals(testList, service.getAll());
        service.setInCurrentThread(null, channelTemperature());
        assertIterableEquals(testList, service.getAll());
        service.setInCurrentThread(null, channelPressureBAR());
        assertIterableEquals(testList, service.getAll());
        service.setInCurrentThread(channelPressureKPA(), null);
        assertIterableEquals(testList, service.getAll());
        service.setInCurrentThread(channelTemperature(), null);
        assertIterableEquals(testList, service.getAll());
        service.setInCurrentThread(null, null);
        assertIterableEquals(testList, service.getAll());
        assertIterableEquals(testList, getAllFromDB());

        System.out.println("setInCurrentThread() duration = " + howLong(start, new Date()) + " mills");
    }

    @Test
    void isExist() {
        Date start = new Date();

        String existsTestCode1 = "1";
        String existsTestCode2 = "2";
        String notExistsTestCode = "N";

        assertEquals(true, service.isExist(existsTestCode1));
        assertEquals(false, service.isExist(notExistsTestCode));
        assertEquals(false, service.isExist(null));
        assertEquals(false, service.isExist(""));

        System.out.println("isExist(String) duration = " + howLong(start, new Date()) + " mills");

        start = new Date();

        assertEquals(true, service.isExist(existsTestCode1, existsTestCode2));
        assertEquals(true, service.isExist(notExistsTestCode, "B"));
        assertEquals(true, service.isExist(notExistsTestCode, existsTestCode1));
        assertEquals(true, service.isExist(notExistsTestCode, null));
        assertEquals(true, service.isExist(existsTestCode1, null));
        assertEquals(true, service.isExist(null, notExistsTestCode));
        assertEquals(true, service.isExist(null, existsTestCode1));
        assertEquals(true, service.isExist(null, null));
        assertEquals(false, service.isExist(existsTestCode1, existsTestCode1));
        assertEquals(false, service.isExist(existsTestCode1, notExistsTestCode));

        System.out.println("isExist(String, String) duration = " + howLong(start, new Date()) + " mills");
    }

    @Test
    void clear() {
        Date start = new Date();

        service.clear();
        assertIterableEquals(new ArrayList<Channel>(), service.getAll());
        while (isBackgroundTaskIsRun());
        assertIterableEquals(new ArrayList<Channel>(), getAllFromDB());

        System.out.println("clear() duration = " + howLong(start, new Date()) + " mills");
    }

    @Test
    void importDataInCurrentThread() {
        Date start = new Date();

        Channel changedChannel = channelTemperature();
        changedChannel.setName("newName");

        ArrayList<Channel>newChannels = new ArrayList<>();
        newChannels.add(channelConsumptionM3());

        ArrayList<Channel>channelsForChange = new ArrayList<>();
        channelsForChange.add(changedChannel);

        ArrayList<Channel>testList = getTestList();
        testList.set(0, changedChannel);
        testList.add(channelConsumptionM3());

        service.importDataInCurrentThread(newChannels, channelsForChange);
        assertIterableEquals(testList, service.getAll());
        service.importDataInCurrentThread(null, null);
        assertIterableEquals(testList, service.getAll());

        newChannels.set(0, channelConsumptionCM());
        channelsForChange.set(0, channelTemperature());
        testList.add(channelConsumptionCM());

        service.importDataInCurrentThread(newChannels, null);
        assertIterableEquals(testList, service.getAll());

        testList.set(0, channelTemperature());
        service.importDataInCurrentThread(null, channelsForChange);
        assertIterableEquals(testList, service.getAll());

        assertIterableEquals(testList, getAllFromDB());

        System.out.println("importDataInCurrentThread() duration = " + howLong(start, new Date()) + " mills");
    }

    @Test
    void rewriteInCurrentThread() {
        Date start = new Date();

        ArrayList<Channel>testList = new ArrayList<>();
        testList.add(channelConsumptionM3());
        testList.add(channelConsumptionCM());
        testList.add(channelTemperature());

        service.rewriteInCurrentThread(testList);
        assertIterableEquals(testList, service.getAll());
        assertIterableEquals(testList, getAllFromDB());

        System.out.println("rewriteInCurrentThread() duration = " + howLong(start, new Date()) + " mills");
    }
}