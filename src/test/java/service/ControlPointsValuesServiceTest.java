package service;

import converters.VariableConverter;
import def.DefaultControlPointsValues;
import model.ControlPointsValues;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sqlite.JDBC;
import service.impl.ControlPointsValuesServiceImpl;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class ControlPointsValuesServiceTest {
    private static final String DB_URL = "jdbc:sqlite:TestData.db";
    private static final ControlPointsValuesService service = new ControlPointsValuesServiceImpl(DB_URL);

    private static ArrayList<ControlPointsValues> getAllFromDB(){
        ArrayList<ControlPointsValues>controlPoints = new ArrayList<>();
        String sql = "SELECT * FROM control_points;";
        try {
            DriverManager.registerDriver(new JDBC());
            try (Connection connection = DriverManager.getConnection(DB_URL);
                 Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(sql)) {
                while (resultSet.next()) {
                    ControlPointsValues cpv = new ControlPointsValues();
                    cpv.setSensorType(resultSet.getString("sensor_type"));
                    cpv.setRangeMin(resultSet.getDouble("range_min"));
                    cpv.setRangeMax(resultSet.getDouble("range_max"));
                    cpv.setValues(VariableConverter.stringToArray(resultSet.getString("points")));
                    controlPoints.add(cpv);
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return controlPoints;
    }

    private static ArrayList<ControlPointsValues> getBySensorTypeFromDB(String sensorType) {
        ArrayList<ControlPointsValues>controlPoints = new ArrayList<>();
        String sql = "SELECT * FROM control_points WHERE sensor_type = '" + sensorType + "';";
        try {
            DriverManager.registerDriver(new JDBC());
            try (Connection connection = DriverManager.getConnection(DB_URL);
                 Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(sql)) {
                while (resultSet.next()) {
                    ControlPointsValues cpv = new ControlPointsValues();
                    cpv.setSensorType(resultSet.getString("sensor_type"));
                    cpv.setRangeMin(resultSet.getDouble("range_min"));
                    cpv.setRangeMax(resultSet.getDouble("range_max"));
                    cpv.setValues(VariableConverter.stringToArray(resultSet.getString("points")));
                    controlPoints.add(cpv);
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return controlPoints;
    }

    private static final String sensorTypeTCM = "TCM";
    private static final String sensorTypePT = "PT";
    private static final String sensorTypeTYPE = "TYPE";

    private static ControlPointsValues cpvTCM_0_100(){
        double[]values = new double[]{0D,25D,50D,75D,100D};
        return new ControlPointsValues(sensorTypeTCM, 0D, 100D, values);
    }

    private static ControlPointsValues cpvTCM_m20_120(){
        double[]values = new double[]{-20D, 35D, 70D, 85D, 120D};
        return new ControlPointsValues(sensorTypeTCM, -20D, 120D, values);
    }

    private static ControlPointsValues cpvPT_0_100(){
        double[]values = new double[]{0D,5D,50D,95D,100D};
        return new ControlPointsValues(sensorTypePT, 0D, 100D, values);
    }

    private static ControlPointsValues cpvTYPE_m20_120(){
        double[]values = new double[]{-20D, -13D, 70D, 113D, 120D};
        return new ControlPointsValues(sensorTypeTYPE, -20D, 120D, values);
    }

    private static ControlPointsValues cpvTYPE_m20_120_another(){
        double[]values = new double[]{-20D, 15D, 50D, 85D, 120D};
        return new ControlPointsValues(sensorTypeTYPE, -20D, 120D, values);
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
        service.clear(null);
        while (isBackgroundTaskIsRun());
        service.putInCurrentThread(cpvTCM_0_100());
        service.putInCurrentThread(cpvTCM_m20_120());
        service.putInCurrentThread(cpvPT_0_100());

        System.out.println("setUp() duration = " + howLong(start, new Date()) + " mills");
    }

    @Test
    void getBySensorType() {
        Date start = new Date();

        ArrayList<ControlPointsValues>testList = new ArrayList<>();
        testList.add(cpvTCM_0_100());
        testList.add(cpvTCM_m20_120());

        assertIterableEquals(testList, service.getBySensorType(sensorTypeTCM));
        assertIterableEquals(testList, getBySensorTypeFromDB(sensorTypeTCM));
        assertIterableEquals(new ArrayList<ControlPointsValues>(), service.getBySensorType(null));
        assertIterableEquals(new ArrayList<ControlPointsValues>(), service.getBySensorType(sensorTypeTYPE));

        System.out.println("getBySensorType() duration = " + howLong(start, new Date()) + " mills");
    }

    @Test
    void getValues() {
        Date start = new Date();

        double[]testValues = new double[]{0D,25D,50D,75D,100D};

        assertArrayEquals(testValues, service.getValues(sensorTypeTCM, 0D, 100D));
        assertNull(service.getValues(sensorTypeTCM, 500D, 1000D));
        assertNull(service.getValues(null, 0D,100D));
        assertNull(service.getValues(sensorTypeTYPE, -20D, 120D));

        System.out.println("getValues() duration = " + howLong(start, new Date()) + " mills");
    }

    @Test
    void getControlPointsValues() {
        Date start = new Date();

        ControlPointsValues testPoints = cpvTCM_m20_120();

        assertEquals(testPoints, service.getControlPointsValues(testPoints.getSensorType(), 1));
        assertNull(service.getControlPointsValues(null,0));
        assertNull(service.getControlPointsValues(testPoints.getSensorType(), -1));
        assertNull(service.getControlPointsValues(testPoints.getSensorType(), 564));
        assertNull(service.getControlPointsValues(null,-5));

        System.out.println("getControlPointsValues() duration = " + howLong(start, new Date()) + " mills");
    }

    @Test
    void put() {
        Date start = new Date();

        ArrayList<ControlPointsValues>testList = new ArrayList<>();
        testList.add(cpvTCM_0_100());
        testList.add(cpvTCM_m20_120());
        testList.add(cpvPT_0_100());
        testList.add(cpvTYPE_m20_120());

        service.put(cpvTYPE_m20_120());
        assertIterableEquals(testList, service.getAll());
        while (isBackgroundTaskIsRun());
        service.put(cpvTCM_0_100());
        assertIterableEquals(testList, service.getAll());
        while (isBackgroundTaskIsRun());
        service.put(null);
        assertIterableEquals(testList, service.getAll());
        while (isBackgroundTaskIsRun());
        assertIterableEquals(testList, getAllFromDB());

        testList.set(3, cpvTYPE_m20_120_another());
        service.put(cpvTYPE_m20_120_another());
        assertIterableEquals(testList, service.getAll());
        while (isBackgroundTaskIsRun());
        assertIterableEquals(testList, getAllFromDB());
        double[]testValues = testList.get(3).getValues();
        double[]values = service.getValues(cpvTYPE_m20_120_another().getSensorType(), cpvTYPE_m20_120_another().getRangeMin(), cpvTYPE_m20_120_another().getRangeMax());
        assertArrayEquals(testValues, values);

        System.out.println("put() duration = " + howLong(start, new Date()) + " mills");
    }

    @Test
    void putInCurrentThread() {
        Date start = new Date();

        ArrayList<ControlPointsValues>testList = new ArrayList<>();
        testList.add(cpvTCM_0_100());
        testList.add(cpvTCM_m20_120());
        testList.add(cpvPT_0_100());
        testList.add(cpvTYPE_m20_120());

        service.putInCurrentThread(cpvTYPE_m20_120());
        assertIterableEquals(testList, service.getAll());
        service.putInCurrentThread(cpvTCM_0_100());
        assertIterableEquals(testList, service.getAll());
        service.putInCurrentThread(null);
        assertIterableEquals(testList, service.getAll());
        assertIterableEquals(testList, getAllFromDB());

        testList.set(3, cpvTYPE_m20_120_another());
        service.putInCurrentThread(cpvTYPE_m20_120_another());
        assertIterableEquals(testList, service.getAll());
        assertIterableEquals(testList, getAllFromDB());
        double[]testValues = testList.get(3).getValues();
        double[]values = service.getValues(cpvTYPE_m20_120_another().getSensorType(), cpvTYPE_m20_120_another().getRangeMin(), cpvTYPE_m20_120_another().getRangeMax());
        assertArrayEquals(testValues, values);

        System.out.println("putInCurrentThread() duration = " + howLong(start, new Date()) + " mills");
    }

    @Test
    void remove() {
        Date start = new Date();

        ArrayList<ControlPointsValues>testList = new ArrayList<>();
        testList.add(cpvTCM_0_100());
        testList.add(cpvPT_0_100());

        service.remove(cpvTCM_m20_120());
        assertIterableEquals(testList, service.getAll());
        while (isBackgroundTaskIsRun());
        service.remove(cpvTYPE_m20_120());
        assertIterableEquals(testList, service.getAll());
        while (isBackgroundTaskIsRun());
        service.remove(null);
        assertIterableEquals(testList, service.getAll());
        while (isBackgroundTaskIsRun());
        assertIterableEquals(testList, getAllFromDB());

        System.out.println("remove() duration = " + howLong(start, new Date()) + " mills");
    }

    @Test
    void removeAllInCurrentThread() {
        Date start = new Date();

        ArrayList<ControlPointsValues>testList = new ArrayList<>();
        testList.add(cpvTCM_0_100());
        testList.add(cpvTCM_m20_120());

        service.removeAllInCurrentThread(sensorTypePT);
        assertIterableEquals(testList, service.getAll());
        service.removeAllInCurrentThread(sensorTypeTYPE);
        assertIterableEquals(testList, service.getAll());
        assertIterableEquals(testList, getAllFromDB());
        service.removeAllInCurrentThread(null);
        assertIterableEquals(new ArrayList<ControlPointsValues>(), service.getAll());
        assertIterableEquals(new ArrayList<ControlPointsValues>(), getAllFromDB());

        System.out.println("removeAllInCurrentThread() duration = " + howLong(start, new Date()) + " mills");
    }

    @Test
    void clear() {
        Date start = new Date();

        ArrayList<ControlPointsValues>testList = new ArrayList<>();
        testList.add(cpvTCM_0_100());
        testList.add(cpvTCM_m20_120());

        service.clear(sensorTypePT);
        assertIterableEquals(testList, service.getAll());
        while (isBackgroundTaskIsRun());
        service.clear(sensorTypeTYPE);
        assertIterableEquals(testList, service.getAll());
        while (isBackgroundTaskIsRun());
        assertIterableEquals(testList, getAllFromDB());
        service.clear(null);
        assertIterableEquals(new ArrayList<ControlPointsValues>(), service.getAll());
        while (isBackgroundTaskIsRun());
        assertIterableEquals(new ArrayList<ControlPointsValues>(), getAllFromDB());

        System.out.println("clear() duration = " + howLong(start, new Date()) + " mills");
    }

    @Test
    void resetToDefaultInCurrentThread() {
        Date start = new Date();

        ArrayList<ControlPointsValues>testList = DefaultControlPointsValues.get();

        service.resetToDefaultInCurrentThread();
        assertIterableEquals(testList, service.getAll());
        assertIterableEquals(testList, getAllFromDB());

        System.out.println("resetToDefaultInCurrentThread() duration = " + howLong(start, new Date()) + " mills");
    }
}