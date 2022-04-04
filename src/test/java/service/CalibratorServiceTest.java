package service;

import com.fasterxml.jackson.core.JsonProcessingException;
import def.DefaultCalibrators;
import model.Calibrator;
import model.Measurement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sqlite.JDBC;
import service.impl.CalibratorServiceImpl;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class CalibratorServiceTest {
    private static final String DB_URL = "jdbc:sqlite:TestData.db";
    private static final CalibratorService service = new CalibratorServiceImpl(DB_URL);

    private static Calibrator calibratorTemperature(){
        Calibrator calibrator = new Calibrator();
        calibrator.setType("1");
        calibrator.setName("1");
        calibrator.setCertificateName("1");
        calibrator.setCertificateCompany("1");
        calibrator.setCertificateDate("23.03.2022");
        calibrator.setNumber("1");
        calibrator.setMeasurement(Measurement.TEMPERATURE);
        calibrator.setRangeMin(0D);
        calibrator.setRangeMax(100D);
        calibrator.setValue(Measurement.DEGREE_CELSIUS);
        calibrator.setErrorFormula("r");
        return calibrator;
    }

    private static Calibrator calibratorPressureKPA(){
        Calibrator calibrator = new Calibrator();
        calibrator.setType("2");
        calibrator.setName("2");
        calibrator.setCertificateName("2");
        calibrator.setCertificateCompany("2");
        calibrator.setCertificateDate("23.03.2022");
        calibrator.setNumber("2");
        calibrator.setMeasurement(Measurement.PRESSURE);
        calibrator.setRangeMin(0D);
        calibrator.setRangeMax(200D);
        calibrator.setValue(Measurement.KPA);
        calibrator.setErrorFormula("r");
        return calibrator;
    }

    private static Calibrator calibratorPressureBAR(){
        Calibrator calibrator = new Calibrator();
        calibrator.setType("3");
        calibrator.setName("3");
        calibrator.setCertificateName("3");
        calibrator.setCertificateCompany("3");
        calibrator.setCertificateDate("23.03.2022");
        calibrator.setNumber("3");
        calibrator.setMeasurement(Measurement.PRESSURE);
        calibrator.setRangeMin(0D);
        calibrator.setRangeMax(300D);
        calibrator.setValue(Measurement.BAR);
        calibrator.setErrorFormula("r");
        return calibrator;
    }

    private static Calibrator calibratorPressurePA(){
        Calibrator calibrator = new Calibrator();
        calibrator.setType("4");
        calibrator.setName("4");
        calibrator.setCertificateName("4");
        calibrator.setCertificateCompany("4");
        calibrator.setCertificateDate("23.03.2022");
        calibrator.setNumber("4");
        calibrator.setMeasurement(Measurement.PRESSURE);
        calibrator.setRangeMin(0D);
        calibrator.setRangeMax(400D);
        calibrator.setValue(Measurement.PA);
        calibrator.setErrorFormula("r");
        return calibrator;
    }

    private static Calibrator calibratorConsumption(){
        Calibrator calibrator = new Calibrator();
        calibrator.setType("5");
        calibrator.setName("5");
        calibrator.setCertificateName("5");
        calibrator.setCertificateCompany("5");
        calibrator.setCertificateDate("23.03.2022");
        calibrator.setNumber("5");
        calibrator.setMeasurement(Measurement.CONSUMPTION);
        calibrator.setRangeMin(0D);
        calibrator.setRangeMax(500D);
        calibrator.setValue(Measurement.M3_HOUR);
        calibrator.setErrorFormula("r");
        return calibrator;
    }

    private static ArrayList<Calibrator>getTestList(){
        ArrayList<Calibrator>list = new ArrayList<>();
        list.add(calibratorTemperature());
        list.add(calibratorPressureKPA());
        list.add(calibratorPressureBAR());
        return list;
    }

    private static ArrayList<Calibrator> getAllFromDB() {
        ArrayList<Calibrator>calibrators = new ArrayList<>();
        String sql = "SELECT * FROM calibrators";
        try {
            DriverManager.registerDriver(new JDBC());
            try (Connection connection = DriverManager.getConnection(DB_URL);
                 Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(sql)) {
                while (resultSet.next()) {
                    Calibrator calibrator = new Calibrator();
                    calibrator.setType(resultSet.getString("type"));
                    calibrator.setName(resultSet.getString("name"));
                    calibrator.setCertificate(Calibrator.Certificate.fromString(resultSet.getString("certificate")));
                    calibrator.setMeasurement(resultSet.getString("measurement"));
                    calibrator.setRangeMin(resultSet.getDouble("range_min"));
                    calibrator.setRangeMax(resultSet.getDouble("range_max"));
                    calibrator.setValue(resultSet.getString("value"));
                    calibrator.setErrorFormula(resultSet.getString("error_formula"));
                    calibrators.add(calibrator);
                }
            }
        }catch (SQLException | JsonProcessingException e){
            e.printStackTrace();
        }
        return calibrators;
    }

    private static long howLong(java.util.Date start, Date end){
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
        service.addInCurrentThread(calibratorTemperature());
        service.addInCurrentThread(calibratorPressureKPA());
        service.addInCurrentThread(calibratorPressureBAR());

        System.out.println("setUp() duration = " + howLong(start, new Date()) + " mills");
    }

    @Test
    void getAll() {
        Date start = new Date();

        ArrayList<Calibrator>testList = getTestList();
        assertIterableEquals(testList, service.getAll());
        assertIterableEquals(testList, getAllFromDB());

        System.out.println("getAll() duration = " + howLong(start, new Date()) + " mills");
    }

    @Test
    void getAllNames() {
        Date start = new Date();

        Measurement measurementPressure = new Measurement(Measurement.PRESSURE, Measurement.KPA);
        Measurement measurementConsumption = new Measurement(Measurement.CONSUMPTION, Measurement.M3_HOUR);
        String[] testArray = new String[]{
                calibratorPressureKPA().getName(),
                calibratorPressureBAR().getName()
        };
        String[] emptyArray = new String[0];

        assertArrayEquals(testArray, service.getAllNames(measurementPressure));
        assertArrayEquals(emptyArray, service.getAllNames(measurementConsumption));
        assertNull(service.getAllNames(null));

        System.out.println("getAllNames() duration = " + howLong(start, new Date()) + " mills");
    }

    @Test
    void add() {
        Date start = new Date();

        ArrayList<Calibrator>testList = getTestList();
        testList.add(calibratorConsumption());

        ArrayList<Calibrator>calibrators = service.add(calibratorConsumption());
        assertIterableEquals(testList, calibrators);
        while (isBackgroundTaskIsRun());
        calibrators = service.add(calibratorConsumption());
        assertIterableEquals(testList, calibrators);
        while (isBackgroundTaskIsRun());
        calibrators = service.add(null);
        assertIterableEquals(testList, calibrators);
        while (isBackgroundTaskIsRun());
        assertIterableEquals(testList, getAllFromDB());

        System.out.println("add() duration = " + howLong(start, new Date()) + " mills");
    }

    @Test
    void addInCurrentThread() {
        Date start = new Date();

        ArrayList<Calibrator>testList = getTestList();
        testList.add(calibratorConsumption());

        service.addInCurrentThread(calibratorConsumption());
        assertIterableEquals(testList, service.getAll());
        service.addInCurrentThread(calibratorConsumption());
        assertIterableEquals(testList, service.getAll());
        service.addInCurrentThread(null);
        assertIterableEquals(testList, service.getAll());
        assertIterableEquals(testList, getAllFromDB());

        System.out.println("addInCurrentThread() duration = " + howLong(start, new Date()) + " mills");
    }

    @Test
    void remove() {
        Date start = new Date();

        ArrayList<Calibrator>testList = getTestList();
        testList.remove(calibratorTemperature());

        ArrayList<Calibrator>calibrators = service.remove(calibratorTemperature());
        assertIterableEquals(testList, calibrators);
        while (isBackgroundTaskIsRun());
        calibrators = service.remove(calibratorTemperature());
        assertIterableEquals(testList, calibrators);
        while (isBackgroundTaskIsRun());
        calibrators = service.remove(null);
        assertIterableEquals(testList, calibrators);
        while (isBackgroundTaskIsRun());
        assertIterableEquals(testList, getAllFromDB());

        System.out.println("remove(Calibrator) duration = " + howLong(start, new Date()) + " mills");

        start = new Date();

        testList.remove(0);

        calibrators = service.remove(0);
        assertIterableEquals(testList, calibrators);
        while (isBackgroundTaskIsRun());
        calibrators = service.remove(-1);
        assertIterableEquals(testList, calibrators);
        while (isBackgroundTaskIsRun());
        calibrators = service.remove(645);
        assertIterableEquals(testList, calibrators);
        while (isBackgroundTaskIsRun());
        assertIterableEquals(testList, getAllFromDB());

        System.out.println("remove(int) duration = " + howLong(start, new Date()) + " mills");
    }

    @Test
    void set() {
        Date start = new Date();

        ArrayList<Calibrator>testList = getTestList();
        testList.set(0, calibratorConsumption());

        ArrayList<Calibrator>calibrators = service.set(calibratorTemperature(), calibratorConsumption());
        assertIterableEquals(testList, calibrators);
        while (isBackgroundTaskIsRun());
        calibrators = service.set(calibratorTemperature(), calibratorPressurePA());
        assertIterableEquals(testList, calibrators);
        while (isBackgroundTaskIsRun());
        calibrators = service.set(null, calibratorPressurePA());
        assertIterableEquals(testList, calibrators);
        while (isBackgroundTaskIsRun());
        calibrators = service.set(calibratorPressureKPA(), calibratorConsumption());
        assertIterableEquals(testList, calibrators);
        while (isBackgroundTaskIsRun());
        calibrators = service.set(calibratorPressureKPA(), null);
        assertIterableEquals(testList, calibrators);
        while (isBackgroundTaskIsRun());
        calibrators = service.set(calibratorPressurePA(), calibratorPressureKPA());
        assertIterableEquals(testList, calibrators);
        while (isBackgroundTaskIsRun());
        assertIterableEquals(testList, getAllFromDB());

        System.out.println("set() duration = " + howLong(start, new Date()) + " mills");
    }

    @Test
    void setInCurrentThread() {
        Date start = new Date();

        ArrayList<Calibrator>testList = getTestList();
        testList.set(0, calibratorConsumption());

        service.setInCurrentThread(calibratorTemperature(), calibratorConsumption());
        assertIterableEquals(testList, service.getAll());
        service.setInCurrentThread(calibratorTemperature(), calibratorPressurePA());
        assertIterableEquals(testList, service.getAll());
        service.setInCurrentThread(null, calibratorPressurePA());
        assertIterableEquals(testList, service.getAll());
        service.setInCurrentThread(calibratorPressureKPA(), calibratorConsumption());
        assertIterableEquals(testList, service.getAll());
        service.setInCurrentThread(calibratorPressureKPA(), null);
        assertIterableEquals(testList, service.getAll());
        service.setInCurrentThread(calibratorPressurePA(), calibratorPressureKPA());
        assertIterableEquals(testList, service.getAll());
        assertIterableEquals(testList, getAllFromDB());

        System.out.println("set() duration = " + howLong(start, new Date()) + " mills");
    }

    @Test
    void get() {
        Date start = new Date();

        Calibrator testCalibrator = calibratorTemperature();
        String testName = testCalibrator.getName();

        assertEquals(testCalibrator, service.get(testName));
        assertNull(service.get(""));
        assertNull(service.get(null));
        assertNull(service.get(" "));

        System.out.println("get(String) duration = " + howLong(start, new Date()) + " mills");

        start = new Date();

        assertEquals(testCalibrator, service.get(0));
        assertNull(service.get(-1));
        assertNull(service.get(645));

        System.out.println("set(int) duration = " + howLong(start, new Date()) + " mills");
    }

    @Test
    void clear() {
        Date start = new Date();

        ArrayList<Calibrator>emptyList = new ArrayList<>();

        service.clear();
        assertEquals(emptyList, service.getAll());
        while (isBackgroundTaskIsRun());
        assertEquals(emptyList, getAllFromDB());

        System.out.println("clear() duration = " + howLong(start, new Date()) + " mills");
    }

    @Test
    void importDataInCurrentThread() {
        Date start = new Date();

        Calibrator changedCalibrator = calibratorTemperature();
        changedCalibrator.setType("newType");

        ArrayList<Calibrator>newCalibrators = new ArrayList<>();
        newCalibrators.add(calibratorConsumption());

        ArrayList<Calibrator>calibratorsForChange = new ArrayList<>();
        calibratorsForChange.add(changedCalibrator);

        ArrayList<Calibrator>testList = getTestList();
        testList.set(0, changedCalibrator);
        testList.add(calibratorConsumption());

        service.importDataInCurrentThread(newCalibrators, calibratorsForChange);
        assertIterableEquals(testList, service.getAll());
        service.importDataInCurrentThread(null, null);
        assertIterableEquals(testList, service.getAll());

        System.out.println("importDataInCurrentThread() duration = " + howLong(start, new Date()) + " mills");
    }

    @Test
    void rewriteInCurrentThread() {
        Date start = new Date();

        ArrayList<Calibrator>testList = new ArrayList<>();
        testList.add(calibratorPressureBAR());
        testList.add(calibratorPressurePA());
        testList.add(calibratorPressureKPA());

        service.rewriteInCurrentThread(testList);
        assertIterableEquals(testList, service.getAll());
        service.rewriteInCurrentThread(new ArrayList<Calibrator>());
        assertIterableEquals(testList, service.getAll());
        service.rewriteInCurrentThread(null);
        assertIterableEquals(testList, service.getAll());
        assertIterableEquals(testList, getAllFromDB());

        System.out.println("rewriteInCurrentThread() duration = " + howLong(start, new Date()) + " mills");
    }

    @Test
    void resetToDefaultInCurrentThread() {
        Date start = new Date();

        ArrayList<Calibrator>testList = DefaultCalibrators.get();

        service.resetToDefaultInCurrentThread();
        assertIterableEquals(testList, service.getAll());
        assertIterableEquals(testList, getAllFromDB());

        System.out.println("resetToDefaultInCurrentThread() duration = " + howLong(start, new Date()) + " mills");
    }
}