package service;

import def.DefaultDepartments;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sqlite.JDBC;
import service.impl.DepartmentServiceImpl;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class DepartmentServiceTest {
    private static final String DB_URL = "jdbc:sqlite:TestData.db";
    private final DepartmentService service = new DepartmentServiceImpl(DB_URL);

    private static final String DOF = "ДОФ";
    private static final String CVO = "ЦВО";
    private static final String VC = "ВЦ";

    private ArrayList<String>getAllFromDB() {
        ArrayList<String>departments = new ArrayList<>();
        String sql = "SELECT * FROM departments";
        try {
            DriverManager.registerDriver(new JDBC());
            try (Connection connection = DriverManager.getConnection(DB_URL);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql)) {
                while (resultSet.next()) {
                    String d = resultSet.getString("department");
                    if (d != null) departments.add(d);
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return departments;
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
        ArrayList<String>testArray = new ArrayList<>();
        testArray.add(DOF);
        testArray.add(CVO);
        while (this.isBackgroundTaskIsRun());
        this.service.addInCurrentThread(testArray);
        Date end = new Date();
        System.out.println("setUp() duration = " + this.howLong(start, end) + " mills");
    }

    @Test
    void getAll() {
        Date start = new Date();
        ArrayList<String>testArray = new ArrayList<>();
        testArray.add(DOF);
        testArray.add(CVO);
        assertIterableEquals(testArray, this.service.getAll());
        assertIterableEquals(testArray, this.getAllFromDB());
        Date end = new Date();
        System.out.println("getAll() duration = " + this.howLong(start, end) + " mills");
    }

    @Test
    void getAllInStrings() {
        Date start = new Date();
        String[]testArray = new String[]{DOF, CVO};
        assertArrayEquals(testArray, this.service.getAllInStrings());
        assertArrayEquals(testArray, this.getAllFromDB().toArray(new String[0]));
        Date end = new Date();
        System.out.println("getAllInStrings() duration = " + this.howLong(start, end) + " mills");
    }

    @Test
    void add() {
        Date start = new Date();
        ArrayList<String>testArray = new ArrayList<>();
        testArray.add(DOF);
        testArray.add(CVO);
        testArray.add(VC);
        ArrayList<String>departments = this.service.add(VC);
        assertIterableEquals(testArray, departments);
        while (this.isBackgroundTaskIsRun());
        departments = this.service.add(null);
        assertIterableEquals(testArray, departments);
        while (this.isBackgroundTaskIsRun());
        assertIterableEquals(testArray, this.getAllFromDB());
        Date end = new Date();
        System.out.println("add() duration = " + this.howLong(start, end) + " mills");
    }

    @Test
    void remove() {
        Date start = new Date();
        ArrayList<String>testArray = new ArrayList<>();
        testArray.add(DOF);
        ArrayList<String>departments = this.service.remove(CVO);
        assertIterableEquals(testArray, departments);
        while (this.isBackgroundTaskIsRun());
        departments = this.service.remove(null);
        assertIterableEquals(testArray, departments);
        while (this.isBackgroundTaskIsRun());
        departments = this.service.remove(VC);
        assertIterableEquals(testArray, departments);
        while (this.isBackgroundTaskIsRun());
        assertIterableEquals(testArray, this.getAllFromDB());
        Date end = new Date();
        System.out.println("remove() duration = " + this.howLong(start, end) + " mills");
    }

    @Test
    void set() {
        Date start = new Date();
        ArrayList<String>testArray = new ArrayList<>();
        testArray.add(DOF);
        testArray.add(VC);
        ArrayList<String>departments = this.service.set(CVO, VC);
        assertIterableEquals(testArray, departments);
        while (this.isBackgroundTaskIsRun());
        departments = this.service.set(null, VC);
        assertIterableEquals(testArray, departments);
        while (this.isBackgroundTaskIsRun());
        departments = this.service.set(null, null);
        assertIterableEquals(testArray, departments);
        while (this.isBackgroundTaskIsRun());
        departments = this.service.set(CVO, VC);
        assertIterableEquals(testArray, departments);
        while (this.isBackgroundTaskIsRun());
        departments = this.service.set(VC, null);
        assertIterableEquals(testArray, departments);
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
        assertEquals(DOF, dof);
        assertEquals(CVO, cvo);
        assertNull(nullString);
        Date end = new Date();
        System.out.println("get() duration = " + this.howLong(start, end) + " mills");
    }

    @Test
    void rewriteInCurrentThread() {
        Date start = new Date();
        ArrayList<String>testArray = new ArrayList<>();
        testArray.add(VC);
        testArray.add(CVO);
        testArray.add(DOF);
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
        ArrayList<String>testArray = DefaultDepartments.get();
        this.service.resetToDefaultInCurrentThread();
        assertIterableEquals(testArray, this.service.getAll());
        assertIterableEquals(testArray, this.getAllFromDB());
        Date end = new Date();
        System.out.println("resetToDefaultInCurrentThread() duration = " + this.howLong(start, end) + " mills");
    }
}