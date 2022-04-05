package service;

import def.DefaultMeasurements;
import model.Measurement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sqlite.JDBC;
import service.impl.MeasurementServiceImpl;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class MeasurementServiceTest {
    private static final String DB_URL = "jdbc:sqlite:TestData.db";
    private static final MeasurementService service = new MeasurementServiceImpl(DB_URL);

    private static final Measurement temperature = new Measurement(Measurement.TEMPERATURE, Measurement.DEGREE_CELSIUS);
    private static final Measurement pressureKPA = new Measurement(Measurement.PRESSURE, Measurement.KPA);
    private static final Measurement pressureBAR = new Measurement(Measurement.PRESSURE, Measurement.BAR);
    private static final Measurement pressurePA = new Measurement(Measurement.PRESSURE, Measurement.PA);
    private static final Measurement consumption = new Measurement(Measurement.CONSUMPTION, Measurement.M3_HOUR);

    private ArrayList<Measurement> getAllFromDB() {
        ArrayList<Measurement>measurements = new ArrayList<>();
        String sql = "SELECT * FROM measurements";
        try {
            DriverManager.registerDriver(new JDBC());
            try (Connection connection = DriverManager.getConnection(DB_URL);
                 Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(sql)) {
                while (resultSet.next()) {
                    String name = resultSet.getString("name");
                    String value = resultSet.getString("value");
                    measurements.add(new Measurement(name, value));
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return measurements;
    }

    private static long howLong(Date start, Date end){
        return end.getTime() - start.getTime();
    }

    @BeforeEach
    void setUp() {
        Date start = new Date();

        service.init();
        service.clear();
        service.add(temperature);
        service.add(pressureKPA);
        service.add(pressureBAR);
        service.add(consumption);

        System.out.println("setUp() duration = " + howLong(start, new Date()) + " mills");
    }

    @Test
    void getAllNames() {
        Date start = new Date();

        String[]testArray = new String[]{
                Measurement.TEMPERATURE,
                Measurement.PRESSURE,
                Measurement.CONSUMPTION
        };

        assertArrayEquals(testArray, service.getAllNames());

        System.out.println("getAllNames() duration = " + howLong(start, new Date()) + " mills");
    }

    @Test
    void getAllValues() {
        Date start = new Date();

        String[]testArray = new String[]{
                Measurement.DEGREE_CELSIUS,
                Measurement.KPA,
                Measurement.BAR,
                Measurement.M3_HOUR
        };

        assertArrayEquals(testArray, service.getAllValues());

        System.out.println("getAllValues() duration = " + howLong(start, new Date()) + " mills");
    }

    @Test
    void getValues() {
        Date start = new Date();

        String[]temperatureValues = new String[]{Measurement.DEGREE_CELSIUS};
        String[]pressureValues = new String[]{Measurement.KPA, Measurement.BAR};
        String[]consumptionValues = new String[]{Measurement.M3_HOUR};
        String[]emptyArray = new String[0];

        assertArrayEquals(temperatureValues, service.getValues(temperature));
        assertArrayEquals(pressureValues, service.getValues(pressureKPA));
        assertArrayEquals(consumptionValues, service.getValues(consumption));
        assertArrayEquals(emptyArray, service.getValues(new Measurement()));
        assertNull(service.getValues((Measurement) null));

        System.out.println("getValues(Measurement) duration = " + howLong(start, new Date()) + " mills");

        start = new Date();

        assertArrayEquals(temperatureValues, service.getValues(Measurement.TEMPERATURE));
        assertArrayEquals(pressureValues, service.getValues(Measurement.PRESSURE));
        assertArrayEquals(consumptionValues, service.getValues(Measurement.CONSUMPTION));
        assertArrayEquals(emptyArray, service.getValues(" "));
        assertNull(service.getValues((String) null));
        assertNull(service.getValues(""));

        System.out.println("getValues(String) duration = " + howLong(start, new Date()) + " mills");
    }

    @Test
    void getAll() {
        Date start = new Date();

        ArrayList<Measurement>testList = new ArrayList<>();
        testList.add(temperature);
        testList.add(pressureKPA);
        testList.add(pressureBAR);
        testList.add(consumption);

        assertIterableEquals(testList, service.getAll());

        System.out.println("getAll() duration = " + howLong(start, new Date()) + " mills");
    }

    @Test
    void get() {
        Date start = new Date();

        assertEquals(temperature, service.get(Measurement.DEGREE_CELSIUS));
        assertEquals(pressureKPA, service.get(Measurement.KPA));
        assertNotEquals(pressureBAR, service.get(Measurement.KPA));
        assertEquals(consumption, service.get(Measurement.M3_HOUR));
        assertNull(service.get(null));
        assertNull(service.get(Measurement.CM_S));

        System.out.println("get(String) duration = " + howLong(start, new Date()) + " mills");

        start = new Date();
        assertEquals(temperature, service.get(0));
        assertEquals(consumption, service.get(3));
        assertNull(service.get(-1));
        assertNull(service.get(564));

        System.out.println("get(int) duration = " + howLong(start, new Date()) + " mills");
    }

    @Test
    void getMeasurements() {
        Date start = new Date();

        ArrayList<Measurement>testList = new ArrayList<>();
        testList.add(pressureKPA);
        testList.add(pressureBAR);

        assertIterableEquals(testList, service.getMeasurements(Measurement.PRESSURE));
        assertIterableEquals(new ArrayList<Measurement>(), service.getMeasurements(" "));
        assertNull(service.getMeasurements(null));
        assertNull(service.getMeasurements(""));

        System.out.println("getMeasurements() duration = " + howLong(start, new Date()) + " mills");
    }

    @Test
    void resetToDefaultInCurrentThread() {
        Date start = new Date();

        ArrayList<Measurement>testList = DefaultMeasurements.get();

        service.resetToDefaultInCurrentThread();
        assertIterableEquals(testList, service.getAll());
        assertIterableEquals(testList, this.getAllFromDB());

        System.out.println("resetToDefaultInCurrentThread() duration = " + howLong(start, new Date()) + " mills");
    }

    @Test
    void add() {
        Date start = new Date();

        ArrayList<Measurement>testList = new ArrayList<>();
        testList.add(temperature);
        testList.add(pressureKPA);
        testList.add(pressureBAR);
        testList.add(consumption);
        testList.add(pressurePA);

        service.add(pressurePA);
        assertIterableEquals(testList, service.getAll());
        service.add(null);
        assertIterableEquals(testList, service.getAll());
        service.add(temperature);
        assertIterableEquals(testList, service.getAll());
        assertIterableEquals(testList, this.getAllFromDB());

        System.out.println("add() duration = " + howLong(start, new Date()) + " mills");
    }

    @Test
    void delete() {
        Date start = new Date();

        ArrayList<Measurement>testList = new ArrayList<>();
        testList.add(temperature);
        testList.add(pressureKPA);
        testList.add(consumption);

        service.delete(pressureBAR);
        assertIterableEquals(testList, service.getAll());
        service.delete(null);
        assertIterableEquals(testList, service.getAll());
        service.delete(pressurePA);
        assertIterableEquals(testList, service.getAll());
        assertIterableEquals(testList, this.getAllFromDB());

        System.out.println("delete() duration = " + howLong(start, new Date()) + " mills");
    }

    @Test
    void clear() {
        Date start = new Date();

        service.clear();
        assertIterableEquals(new ArrayList<Measurement>(), service.getAll());
        assertIterableEquals(new ArrayList<Measurement>(), this.getAllFromDB());

        System.out.println("clear() duration = " + howLong(start, new Date()) + " mills");
    }
}