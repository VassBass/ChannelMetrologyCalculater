package service;

import def.DefaultProcesses;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sqlite.JDBC;
import service.impl.ProcessServiceImpl;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class ProcessServiceTest {
    private static final String DB_URL = "jdbc:sqlite:TestData.db";
    private final ProcessService service = new ProcessServiceImpl(DB_URL);

    private static final String SECTION = "Секція";
    private static final String TECHNOLOGY_LINE = "Технологічна лінія";
    private static final String TRAKT = "Тракт";

    private ArrayList<String>getAllFromDB() {
        ArrayList<String>areas = new ArrayList<>();
        String sql = "SELECT * FROM processes";
        try {
            DriverManager.registerDriver(new JDBC());
            try (Connection connection = DriverManager.getConnection(DB_URL);
                 Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(sql)) {
                while (resultSet.next()) {
                    String a = resultSet.getString("process");
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
        testArray.add(SECTION);
        testArray.add(TECHNOLOGY_LINE);
        while (this.isBackgroundTaskIsRun());
        this.service.addInCurrentThread(testArray);
        Date end = new Date();
        System.out.println("setUp() duration = " + this.howLong(start, end) + " mills");
    }

    @Test
    void getAll() {
        Date start = new Date();
        ArrayList<String>testArray = new ArrayList<>();
        testArray.add(SECTION);
        testArray.add(TECHNOLOGY_LINE);
        assertIterableEquals(testArray, this.service.getAll());
        assertIterableEquals(testArray, this.getAllFromDB());
        Date end = new Date();
        System.out.println("getAll() duration = " + this.howLong(start, end) + " mills");
    }

    @Test
    void getAllInStrings() {
        Date start = new Date();
        String[]testArray = new String[]{SECTION, TECHNOLOGY_LINE};
        assertArrayEquals(testArray, this.service.getAllInStrings());
        assertArrayEquals(testArray, this.getAllFromDB().toArray(new String[0]));
        System.out.println("getAllInStrings() duration = " + this.howLong(start, new Date()) + " mills");
    }

    @Test
    void add() {
        Date start = new Date();
        ArrayList<String>testArray = new ArrayList<>();
        testArray.add(SECTION);
        testArray.add(TECHNOLOGY_LINE);
        testArray.add(TRAKT);
        ArrayList<String>processes = this.service.add(TRAKT);
        assertIterableEquals(testArray, processes);
        while (this.isBackgroundTaskIsRun());
        processes = this.service.add(null);
        assertIterableEquals(testArray, processes);
        while (this.isBackgroundTaskIsRun());
        assertIterableEquals(testArray, this.getAllFromDB());
        System.out.println("add() duration = " + this.howLong(start, new Date()) + " mills");
    }

    @Test
    void remove() {
        Date start = new Date();
        ArrayList<String>testArray = new ArrayList<>();
        testArray.add(SECTION);
        ArrayList<String>processes = this.service.remove(TECHNOLOGY_LINE);
        assertIterableEquals(testArray, processes);
        while (this.isBackgroundTaskIsRun());
        processes = this.service.remove(null);
        assertIterableEquals(testArray, processes);
        while (this.isBackgroundTaskIsRun());
        processes = this.service.remove(TRAKT);
        assertIterableEquals(testArray, processes);
        while (this.isBackgroundTaskIsRun());
        assertIterableEquals(testArray, this.getAllFromDB());
        System.out.println("remove() duration = " + this.howLong(start, new Date()) + " mills");
    }

    @Test
    void set() {
        Date start = new Date();
        ArrayList<String>testArray = new ArrayList<>();
        testArray.add(SECTION);
        testArray.add(TRAKT);
        ArrayList<String>processes = this.service.set(TECHNOLOGY_LINE, TRAKT);
        assertIterableEquals(testArray, processes);
        while (this.isBackgroundTaskIsRun());
        processes = this.service.set(null, TRAKT);
        assertIterableEquals(testArray, processes);
        while (this.isBackgroundTaskIsRun());
        processes = this.service.set(null, null);
        assertIterableEquals(testArray, processes);
        while (this.isBackgroundTaskIsRun());
        processes = this.service.set(TECHNOLOGY_LINE, TRAKT);
        assertIterableEquals(testArray, processes);
        while (this.isBackgroundTaskIsRun());
        processes = this.service.set(TRAKT, null);
        assertIterableEquals(testArray, processes);
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
        assertEquals(SECTION, dof);
        assertEquals(TECHNOLOGY_LINE, cvo);
        assertNull(nullString);
        System.out.println("get() duration = " + this.howLong(start, new Date()) + " mills");
    }

    @Test
    void rewriteInCurrentThread() {
        Date start = new Date();
        ArrayList<String>testArray = new ArrayList<>();
        testArray.add(TRAKT);
        testArray.add(TECHNOLOGY_LINE);
        testArray.add(SECTION);
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
        ArrayList<String>testArray = DefaultProcesses.get();
        this.service.resetToDefaultInCurrentThread();
        assertIterableEquals(testArray, this.service.getAll());
        assertIterableEquals(testArray, this.getAllFromDB());
        System.out.println("resetToDefaultInCurrentThread() duration = " + this.howLong(start, new Date()) + " mills");
    }
}