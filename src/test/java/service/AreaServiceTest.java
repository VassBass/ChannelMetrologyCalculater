package service;

import def.DefaultAreas;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sqlite.JDBC;
import service.impl.AreaServiceImpl;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class AreaServiceTest {
    private static final String DB_URL = "jdbc:sqlite:TestData.db";
    private final AreaService service = new AreaServiceImpl(DB_URL);

    private static final String OVSD_1 = "ОВЗД-1";
    private static final String OVSD_2 = "ОВЗД-2";
    private static final String OVSD_3 = "ОВЗД-3";

    private ArrayList<String>getAllFromDB() {
        ArrayList<String>areas = new ArrayList<>();
        String sql = "SELECT * FROM areas";
        try {
            DriverManager.registerDriver(new JDBC());
            try (Connection connection = DriverManager.getConnection(DB_URL);
                 Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(sql)) {
                while (resultSet.next()) {
                    String a = resultSet.getString("area");
                    if (a != null) areas.add(a);
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return areas;
    }

    private long howLong(Date start, Date end){
        return end.getTime() - start.getTime();
    }

    private boolean isBackgroundTaskIsRun() {
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
            return true;
        }
        return this.service.backgroundTaskIsRun();
    }

    @BeforeEach
    void setUp() {
        Date start = new Date();
        this.service.init();
        this.service.clear();
        while (this.isBackgroundTaskIsRun());
        ArrayList<String>testArray = new ArrayList<>();
        testArray.add(OVSD_1);
        testArray.add(OVSD_2);
        this.service.addInCurrentThread(testArray);
        Date end = new Date();
        System.out.println("setUp() duration = " + this.howLong(start, end) + " mills");
    }

    @Test
    void getAll() {
        Date start = new Date();
        ArrayList<String>testArray = new ArrayList<>();
        testArray.add(OVSD_1);
        testArray.add(OVSD_2);
        assertIterableEquals(testArray, this.service.getAll());
        assertIterableEquals(testArray, this.getAllFromDB());
        Date end = new Date();
        System.out.println("getAll() duration = " + this.howLong(start, end) + " mills");
    }

    @Test
    void getAllInStrings() {
        Date start = new Date();
        String[]testArray = new String[]{OVSD_1, OVSD_2};
        assertArrayEquals(testArray, this.service.getAllInStrings());
        assertArrayEquals(testArray, this.getAllFromDB().toArray(new String[0]));
        Date end = new Date();
        System.out.println("getAllInStrings() duration = " + this.howLong(start, end) + " mills");
    }

    @Test
    void add() {
        Date start = new Date();
        ArrayList<String>testArray = new ArrayList<>();
        testArray.add(OVSD_1);
        testArray.add(OVSD_2);
        testArray.add(OVSD_3);
        ArrayList<String>areas = this.service.add(OVSD_3);
        assertIterableEquals(testArray, areas);
        while (this.isBackgroundTaskIsRun());
        areas = this.service.add(null);
        assertIterableEquals(testArray, areas);
        while (this.isBackgroundTaskIsRun());
        assertIterableEquals(testArray, this.getAllFromDB());
        Date end = new Date();
        System.out.println("add() duration = " + this.howLong(start, end) + " mills");
    }

    @Test
    void remove() {
        Date start = new Date();
        ArrayList<String>testArray = new ArrayList<>();
        testArray.add(OVSD_1);
        ArrayList<String>areas = this.service.remove(OVSD_2);
        assertIterableEquals(testArray, areas);
        while (this.isBackgroundTaskIsRun());
        areas = this.service.remove(null);
        assertIterableEquals(testArray, areas);
        while (this.isBackgroundTaskIsRun());
        areas = this.service.remove(OVSD_3);
        assertIterableEquals(testArray, areas);
        while (this.isBackgroundTaskIsRun());
        assertIterableEquals(testArray, this.getAllFromDB());
        Date end = new Date();
        System.out.println("remove() duration = " + this.howLong(start, end) + " mills");
    }

    @Test
    void set() {
        Date start = new Date();
        ArrayList<String>testArray = new ArrayList<>();
        testArray.add(OVSD_1);
        testArray.add(OVSD_3);
        ArrayList<String>areas = this.service.set(OVSD_2, OVSD_3);
        assertIterableEquals(testArray, areas);
        while (this.isBackgroundTaskIsRun());
        areas = this.service.set(null, OVSD_3);
        assertIterableEquals(testArray, areas);
        while (this.isBackgroundTaskIsRun());
        areas = this.service.set(null, null);
        assertIterableEquals(testArray, areas);
        while (this.isBackgroundTaskIsRun());
        areas = this.service.set(OVSD_2, OVSD_3);
        assertIterableEquals(testArray, areas);
        while (this.isBackgroundTaskIsRun());
        areas = this.service.set(OVSD_3, null);
        assertIterableEquals(testArray, areas);
        while (this.isBackgroundTaskIsRun());
        assertIterableEquals(testArray, this.getAllFromDB());
        Date end = new Date();
        System.out.println("set() duration = " + this.howLong(start, end) + " mills");
    }

    @Test
    void get() {
        Date start = new Date();
        String dof = this.service.get(0);
        String cvo = this.service.get(1);
        String nullString = this.service.get(2);
        assertEquals(OVSD_1, dof);
        assertEquals(OVSD_2, cvo);
        assertNull(nullString);
        Date end = new Date();
        System.out.println("get() duration = " + this.howLong(start, end) + " mills");
    }

    @Test
    void rewriteInCurrentThread() {
        Date start = new Date();
        ArrayList<String>testArray = new ArrayList<>();
        testArray.add(OVSD_3);
        testArray.add(OVSD_2);
        testArray.add(OVSD_1);
        this.service.rewriteInCurrentThread(testArray);
        assertIterableEquals(testArray, this.service.getAll());
        this.service.rewriteInCurrentThread(null);
        assertIterableEquals(testArray, this.service.getAll());
        assertIterableEquals(testArray, this.getAllFromDB());
        Date end = new Date();
        System.out.println("rewriteInCurrentThread() duration = " + this.howLong(start, end) + " mills");
    }

    @Test
    void resetToDefaultInCurrentThread() {
        Date start = new Date();
        ArrayList<String>testArray = DefaultAreas.get();
        this.service.resetToDefaultInCurrentThread();
        assertIterableEquals(testArray, this.service.getAll());
        assertIterableEquals(testArray, this.getAllFromDB());
        Date end = new Date();
        System.out.println("resetToDefaultInCurrentThread() duration = " + this.howLong(start, end) + " mills");
    }
}