package service;

import com.fasterxml.jackson.core.JsonProcessingException;
import def.DefaultSensors;
import model.Calibrator;
import model.Channel;
import model.Measurement;
import model.Sensor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sqlite.JDBC;
import service.impl.ChannelServiceImpl;
import service.impl.SensorServiceImpl;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class SensorServiceTest {
    private static final String DB_URL = "jdbc:sqlite:TestData.db";
    private static final SensorService service = new SensorServiceImpl(DB_URL);

    private static Sensor sensorTemperatureTCM_50M(){
        Sensor sensor = new Sensor();
        sensor.setType(Sensor.TCM_50M);
        sensor.setName(Sensor.TCM_50M);
        sensor.setRange(0D, 100D);
        sensor.setNumber(null);
        sensor.setValue(Measurement.DEGREE_CELSIUS);
        sensor.setMeasurement(Measurement.TEMPERATURE);
        sensor.setErrorFormula("r");
        return sensor;
    }

    private static Sensor sensorTemperaturePt100(){
        Sensor sensor = new Sensor();
        sensor.setType(Sensor.Pt100);
        sensor.setName(Sensor.Pt100);
        sensor.setRange(-20D, 150D);
        sensor.setNumber(null);
        sensor.setValue(Measurement.DEGREE_CELSIUS);
        sensor.setMeasurement(Measurement.TEMPERATURE);
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

    private static Sensor sensorConsumptionROSEMOUNT(){
        Sensor sensor = new Sensor();
        sensor.setType(Sensor.ROSEMOUNT);
        sensor.setName(Sensor.ROSEMOUNT);
        sensor.setRange(0D, 400D);
        sensor.setNumber("123456789");
        sensor.setValue(Measurement.M3_HOUR);
        sensor.setMeasurement(Measurement.CONSUMPTION);
        sensor.setErrorFormula("r");
        return sensor;
    }

    private static Sensor sensorConsumptionYOKOGAVA(){
        Sensor sensor = new Sensor();
        sensor.setType(Sensor.YOKOGAWA);
        sensor.setName(Sensor.YOKOGAWA);
        sensor.setRange(0D, 500D);
        sensor.setNumber("987654321");
        sensor.setValue(Measurement.CM_S);
        sensor.setMeasurement(Measurement.CONSUMPTION);
        sensor.setErrorFormula("r");
        return sensor;
    }

    private static ArrayList<Sensor> getTestList(){
        ArrayList<Sensor>list = new ArrayList<>();
        list.add(sensorTemperatureTCM_50M());
        list.add(sensorConsumptionROSEMOUNT());
        list.add(sensorConsumptionYOKOGAVA());
        return list;
    }

    private static ArrayList<Sensor> getAllFromDB() {
        ArrayList<Sensor>sensors = new ArrayList<>();
        String sql = "SELECT * FROM sensors";
        try {
            DriverManager.registerDriver(new JDBC());
            try (Connection connection = DriverManager.getConnection(DB_URL);
                 Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(sql)) {
                while (resultSet.next()) {
                    Sensor sensor = new Sensor();
                    sensor.setType(resultSet.getString("type"));
                    sensor.setName(resultSet.getString("name"));
                    sensor.setMeasurement(resultSet.getString("measurement"));
                    sensor.setValue(resultSet.getString("value"));
                    double rangeMin = resultSet.getDouble("range_min");
                    double rangeMax = resultSet.getDouble("range_min");
                    sensor.setRange(rangeMin, rangeMax);
                    sensor.setNumber(resultSet.getString("number"));
                    sensor.setErrorFormula(resultSet.getString("error_formula"));

                    sensors.add(sensor);
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return sensors;
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
        service.addInCurrentThread(sensorTemperatureTCM_50M());
        service.addInCurrentThread(sensorConsumptionROSEMOUNT());
        service.addInCurrentThread(sensorConsumptionYOKOGAVA());

        System.out.println("setUp() duration = " + howLong(start, new Date()) + " mills");
    }

    @Test
    void getAllTypes() {
        Date start = new Date();

        String[]testArray = new String[]{Sensor.TCM_50M, Sensor.ROSEMOUNT, Sensor.YOKOGAWA};

        assertArrayEquals(testArray, service.getAllTypes());
        service.clear();
        while (isBackgroundTaskIsRun());
        assertArrayEquals(new String[]{}, service.getAllTypes());

        System.out.println("getAllTypes() duration = " + howLong(start, new Date()) + " mills");
    }

    @Test
    void getAllTypesWithoutROSEMOUNT() {
        Date start = new Date();

        String[]testArray = new String[]{Sensor.TCM_50M, Sensor.YOKOGAWA};

        assertArrayEquals(testArray, service.getAllTypesWithoutROSEMOUNT());
        service.clear();
        while (isBackgroundTaskIsRun());
        assertArrayEquals(new String[]{}, service.getAllTypesWithoutROSEMOUNT());

        System.out.println("getAllTypes() duration = " + howLong(start, new Date()) + " mills");
    }

    @Test
    void getAll() {
        Date start = new Date();

        ArrayList<Sensor>testList = getTestList();

        assertIterableEquals(testList, service.getAll());

        System.out.println("getAll() duration = " + howLong(start, new Date()) + " mills");
    }

    @Test
    void getMeasurement() {
        Date start = new Date();

        String testMeasurement = Measurement.CONSUMPTION;

        assertEquals(testMeasurement, service.getMeasurement(Sensor.ROSEMOUNT));
        assertNull(service.getMeasurement(Sensor.Pt100));
        assertNull(service.getMeasurement(null));

        System.out.println("getMeasurement() duration = " + howLong(start, new Date()) + " mills");
    }

    @Test
    void getAllSensorsName() {
        Date start = new Date();

        String[]testArray = new String[]{Sensor.ROSEMOUNT, Sensor.YOKOGAWA};

        assertArrayEquals(testArray, service.getAllSensorsName(Measurement.CONSUMPTION));
        assertArrayEquals(new String[]{}, service.getAllSensorsName(Measurement.PRESSURE));
        assertArrayEquals(new String[]{}, service.getAllSensorsName(null));

        System.out.println("getAllSensorsName() duration = " + howLong(start, new Date()) + " mills");
    }

    @Test
    void add() {
        Date start = new Date();

        ArrayList<Sensor>testList = getTestList();
        testList.add(sensorTemperaturePt100());

        ArrayList<Sensor>sensors = service.add(sensorTemperaturePt100());
        assertIterableEquals(testList, sensors);
        while (isBackgroundTaskIsRun());
        sensors = service.add(sensorTemperatureTCM_50M());
        assertIterableEquals(testList, sensors);
        while (isBackgroundTaskIsRun());
        sensors = service.add(null);
        assertIterableEquals(testList, sensors);
        while (isBackgroundTaskIsRun());
        assertIterableEquals(testList, getAllFromDB());

        System.out.println("add() duration = " + howLong(start, new Date()) + " mills");
    }

    @Test
    void addInCurrentThread() {
        Date start = new Date();

        ArrayList<Sensor>testList = getTestList();
        testList.add(sensorTemperaturePt100());

        service.addInCurrentThread(sensorTemperaturePt100());
        assertIterableEquals(testList, service.getAll());
        service.addInCurrentThread(sensorTemperatureTCM_50M());
        assertIterableEquals(testList, service.getAll());
        service.addInCurrentThread(null);
        assertIterableEquals(testList, service.getAll());
        assertIterableEquals(testList, getAllFromDB());

        System.out.println("addInCurrentThread() duration = " + howLong(start, new Date()) + " mills");
    }

    @Test
    void removeInCurrentThread() {
        Date start = new Date();

        ArrayList<Sensor>testList = getTestList();
        testList.remove(0);

        service.removeInCurrentThread(sensorTemperatureTCM_50M());
        assertIterableEquals(testList, service.getAll());
        service.removeInCurrentThread(sensorTemperaturePt100());
        assertIterableEquals(testList, service.getAll());
        service.removeInCurrentThread(null);
        assertIterableEquals(testList, service.getAll());

        System.out.println("removeInCurrentThread() duration = " + howLong(start, new Date()) + " mills");
    }

    @Test
    void setInCurrentThread() {
        Date start = new Date();

        ArrayList<Sensor>testList = getTestList();
        testList.set(0, sensorTemperaturePt100());

        service.setInCurrentThread(sensorTemperatureTCM_50M(), sensorTemperaturePt100());
        assertIterableEquals(testList, service.getAll());
        service.setInCurrentThread(sensorTemperatureTCM_50M(), sensorPressureBAR());
        assertIterableEquals(testList, service.getAll());
        service.setInCurrentThread(sensorTemperaturePt100(), sensorConsumptionROSEMOUNT());
        assertIterableEquals(testList, service.getAll());
        service.setInCurrentThread(null, sensorPressureBAR());
        assertIterableEquals(testList, service.getAll());
        service.setInCurrentThread(null, sensorConsumptionROSEMOUNT());
        assertIterableEquals(testList, service.getAll());
        service.setInCurrentThread(sensorConsumptionROSEMOUNT(), null);
        assertIterableEquals(testList, service.getAll());
        service.setInCurrentThread(sensorPressureBAR(), null);
        assertIterableEquals(testList, service.getAll());

        System.out.println("setInCurrentThread() duration = " + howLong(start, new Date()) + " mills");
    }

    @Test
    void importDataInCurrentThread() {
        Date start = new Date();

        Sensor changedSensor = sensorTemperatureTCM_50M();
        changedSensor.setType("newType");

        ArrayList<Sensor>newSensors = new ArrayList<>();
        newSensors.add(sensorTemperaturePt100());
        newSensors.add(sensorPressureBAR());

        ArrayList<Sensor>sensorsForChange = new ArrayList<>();
        sensorsForChange.add(changedSensor);

        ArrayList<Sensor>testList = getTestList();
        testList.set(0, changedSensor);
        testList.add(sensorTemperaturePt100());
        testList.add(sensorPressureBAR());

        service.importDataInCurrentThread(newSensors, sensorsForChange);
        assertIterableEquals(testList, service.getAll());
        service.importDataInCurrentThread(null, null);
        assertIterableEquals(testList, service.getAll());
        assertIterableEquals(testList, getAllFromDB());

        System.out.println("importDataInCurrentThread() duration = " + howLong(start, new Date()) + " mills");
    }

    @Test
    void get() {
        Date start = new Date();

        Sensor testSensor = sensorTemperatureTCM_50M();

        assertEquals(testSensor, service.get(Sensor.TCM_50M));
        assertNull(service.get("newSensor"));
        assertNull(service.get(null));

        System.out.println("get(String) duration = " + howLong(start, new Date()) + " mills");

        assertEquals(testSensor, service.get(0));
        assertNull(service.get(-2));
        assertNull(service.get(645));

        System.out.println("get(int) duration = " + howLong(start, new Date()) + " mills");
    }

    @Test
    void clear() {
        Date start = new Date();

        service.clear();
        assertIterableEquals(new ArrayList<Sensor>(), service.getAll());
        while (isBackgroundTaskIsRun());
        assertIterableEquals(new ArrayList<Sensor>(), getAllFromDB());

        System.out.println("clear() duration = " + howLong(start, new Date()) + " mills");
    }

    @Test
    void isLastInMeasurement() {
        Date start = new Date();

        assertEquals(true, service.isLastInMeasurement(sensorTemperatureTCM_50M()));
        assertEquals(false, service.isLastInMeasurement(sensorConsumptionROSEMOUNT()));
        assertEquals(false, service.isLastInMeasurement(sensorPressureBAR()));
        assertEquals(false, service.isLastInMeasurement(null));

        System.out.println("isLastInMeasurement() duration = " + howLong(start, new Date()) + " mills");
    }

    @Test
    void rewriteInCurrentThread() {
        Date start = new Date();

        ArrayList<Sensor>testList = new ArrayList<>();
        testList.add(sensorTemperaturePt100());
        testList.add(sensorConsumptionYOKOGAVA());
        testList.add(sensorPressureBAR());
        testList.add(sensorTemperatureTCM_50M());

        service.rewriteInCurrentThread(testList);
        assertIterableEquals(testList, service.getAll());
        service.rewriteInCurrentThread(null);
        assertIterableEquals(testList, service.getAll());
        service.rewriteInCurrentThread(new ArrayList<Sensor>());
        assertIterableEquals(testList, service.getAll());
        assertIterableEquals(testList, getAllFromDB());

        System.out.println("rewriteInCurrentThread() duration = " + howLong(start, new Date()) + " mills");
    }

    @Test
    void resetToDefaultInCurrentThread() {
        Date start = new Date();

        ArrayList<Sensor>testList = DefaultSensors.get();

        service.resetToDefaultInCurrentThread();
        assertIterableEquals(testList, service.getAll());
        assertIterableEquals(testList, getAllFromDB());

        System.out.println("resetToDefaultInCurrentThread() duration = " + howLong(start, new Date()) + " mills");
    }
}