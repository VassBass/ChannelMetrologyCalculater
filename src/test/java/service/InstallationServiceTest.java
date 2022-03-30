package service;

import def.DefaultInstallations;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sqlite.JDBC;
import service.impl.InstallationServiceImpl;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class InstallationServiceTest {
    private static final String DB_URL = "jdbc:sqlite:TestData.db";
    private final InstallationService service = new InstallationServiceImpl(DB_URL);

    private static final String MEL = "Млин";
    private static final String PICH = "Піч";
    private static final String VENT = "Вентилятор";

    private ArrayList<String>getAllFromDB() {
        ArrayList<String>areas = new ArrayList<>();
        String sql = "SELECT * FROM installations";
        try {
            DriverManager.registerDriver(new JDBC());
            try (Connection connection = DriverManager.getConnection(DB_URL);
                 Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(sql)) {
                while (resultSet.next()) {
                    String a = resultSet.getString("installation");
                    if (a != null) areas.add(a);
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return areas;
    }

    private long howLong(java.util.Date start, Date end){
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
        ArrayList<String>testArray = new ArrayList<>();
        testArray.add(MEL);
        testArray.add(PICH);
        while (this.isBackgroundTaskIsRun());
        this.service.addInCurrentThread(testArray);
        System.out.println("setUp() duration = " + this.howLong(start, new Date()) + " mills");
    }

    @Test
    void getAll() {
        Date start = new Date();
        ArrayList<String>testArray = new ArrayList<>();
        testArray.add(MEL);
        testArray.add(PICH);
        assertIterableEquals(testArray, this.service.getAll());
        assertIterableEquals(testArray, this.getAllFromDB());
        System.out.println("getAll() duration = " + this.howLong(start, new Date()) + " mills");
    }

    @Test
    void getAllInStrings() {
        Date start = new Date();
        String[]testArray = new String[]{MEL, PICH};
        assertArrayEquals(testArray, this.service.getAllInStrings());
        assertArrayEquals(testArray, this.getAllFromDB().toArray(new String[0]));
        System.out.println("getAllInStrings() duration = " + this.howLong(start, new Date()) + " mills");
    }

    @Test
    void add() {
        Date start = new Date();
        ArrayList<String>testArray = new ArrayList<>();
        testArray.add(MEL);
        testArray.add(PICH);
        testArray.add(VENT);
        ArrayList<String>installations = this.service.add(VENT);
        assertIterableEquals(testArray, installations);
        while (this.isBackgroundTaskIsRun());
        installations = this.service.add(null);
        assertIterableEquals(testArray, installations);
        while (this.isBackgroundTaskIsRun());
        assertIterableEquals(testArray, this.getAllFromDB());
        System.out.println("add() duration = " + this.howLong(start, new Date()) + " mills");
    }

    @Test
    void remove() {
        Date start = new Date();
        ArrayList<String>testArray = new ArrayList<>();
        testArray.add(MEL);
        ArrayList<String>installations = this.service.remove(PICH);
        assertIterableEquals(testArray, installations);
        while (this.isBackgroundTaskIsRun());
        installations = this.service.remove(null);
        assertIterableEquals(testArray, installations);
        while (this.isBackgroundTaskIsRun());
        installations = this.service.remove(VENT);
        assertIterableEquals(testArray, installations);
        while (this.isBackgroundTaskIsRun());
        assertIterableEquals(testArray, this.getAllFromDB());
        System.out.println("remove() duration = " + this.howLong(start, new Date()) + " mills");
    }

    @Test
    void set() {
        Date start = new Date();
        ArrayList<String>testArray = new ArrayList<>();
        testArray.add(MEL);
        testArray.add(VENT);
        ArrayList<String>installations = this.service.set(PICH, VENT);
        assertIterableEquals(testArray, installations);
        while (this.isBackgroundTaskIsRun());
        installations = this.service.set(null, VENT);
        assertIterableEquals(testArray, installations);
        while (this.isBackgroundTaskIsRun());
        installations = this.service.set(null, null);
        assertIterableEquals(testArray, installations);
        while (this.isBackgroundTaskIsRun());
        installations = this.service.set(PICH, VENT);
        assertIterableEquals(testArray, installations);
        while (this.isBackgroundTaskIsRun());
        installations = this.service.set(VENT, null);
        assertIterableEquals(testArray, installations);
        while (this.isBackgroundTaskIsRun());
        assertIterableEquals(testArray, this.getAllFromDB());
        System.out.println("set() duration = " + this.howLong(start, new Date()) + " mills");
    }

    @Test
    void get() {
        Date start = new Date();
        String dof = this.service.get(0);
        String cvo = this.service.get(1);
        String nullString = this.service.get(2);
        assertEquals(MEL, dof);
        assertEquals(PICH, cvo);
        assertNull(nullString);
        System.out.println("get() duration = " + this.howLong(start, new Date()) + " mills");
    }

    @Test
    void rewriteInCurrentThread() {
        Date start = new Date();
        ArrayList<String>testArray = new ArrayList<>();
        testArray.add(VENT);
        testArray.add(PICH);
        testArray.add(MEL);
        this.service.rewriteInCurrentThread(testArray);
        assertIterableEquals(testArray, this.service.getAll());
        this.service.rewriteInCurrentThread(null);
        assertIterableEquals(testArray, this.service.getAll());
        assertIterableEquals(testArray, this.getAllFromDB());
        System.out.println("rewriteInCurrentThread() duration = " + this.howLong(start, new Date()) + " mills");
    }

    @Test
    void resetToDefaultInCurrentThread() {
        Date start = new Date();
        ArrayList<String>testArray = DefaultInstallations.get();
        this.service.resetToDefaultInCurrentThread();
        assertIterableEquals(testArray, this.service.getAll());
        assertIterableEquals(testArray, this.getAllFromDB());
        System.out.println("resetToDefaultInCurrentThread() duration = " + this.howLong(start, new Date()) + " mills");
    }
}