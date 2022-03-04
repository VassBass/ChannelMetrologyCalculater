package service;

import def.DefaultAreas;
import def.DefaultDepartments;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sqlite.JDBC;
import service.impl.AreaServiceImpl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class AreaServiceTest {
    private static final String DB_URL = "jdbc:sqlite:TestData.db";
    private final AreaService service = new AreaServiceImpl(DB_URL);

    private static final String OVSD_1 = "ОВЗД-1";
    private static final String OVSD_2 = "ОВЗД-2";
    private static final String OVSD_3 = "ОВЗД-3";

    @BeforeEach
    void setUp() {
        try {
            DriverManager.registerDriver(new JDBC());
            Connection connection = DriverManager.getConnection(DB_URL);
            String sql = "DELETE FROM areas;";
            Statement statement = connection.createStatement();
            statement.execute(sql);
            statement.close();
            this.service.rewriteInCurrentThread(new ArrayList<String>());

            sql = "INSERT INTO areas ('area') "
                    + "VALUES "
                    +       "('" + OVSD_1 + "'),"
                    +       "('" + OVSD_2 + "');";
            statement = connection.createStatement();
            statement.execute(sql);
            statement.close();
            this.service.init();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    void getAll() {
        ArrayList<String>testArray = new ArrayList<>();
        testArray.add(OVSD_1);
        testArray.add(OVSD_2);
        assertIterableEquals(testArray, this.service.getAll());
    }

    @Test
    void getAllInStrings() {
        String[]testArray = new String[]{OVSD_1, OVSD_2};
        assertArrayEquals(testArray, this.service.getAllInStrings());
    }

    @Test
    void add() {
        ArrayList<String>testArray = new ArrayList<>();
        testArray.add(OVSD_1);
        testArray.add(OVSD_2);
        testArray.add(OVSD_3);
        ArrayList<String>areas = this.service.add(OVSD_3);
        assertIterableEquals(testArray, areas);
        areas = this.service.add(null);
        assertIterableEquals(testArray, areas);
    }

    @Test
    void remove() {
        ArrayList<String>testArray = new ArrayList<>();
        testArray.add(OVSD_1);
        ArrayList<String>areas = this.service.remove(OVSD_2);
        assertIterableEquals(testArray, areas);
        areas = this.service.remove(null);
        assertIterableEquals(testArray, areas);
        areas = this.service.remove(OVSD_3);
        assertIterableEquals(testArray, areas);
    }

    @Test
    void set() {
        ArrayList<String>testArray = new ArrayList<>();
        testArray.add(OVSD_1);
        testArray.add(OVSD_3);
        ArrayList<String>areas = this.service.set(OVSD_2, OVSD_3);
        assertIterableEquals(testArray, areas);
        areas = this.service.set(null, OVSD_3);
        assertIterableEquals(testArray, areas);
        areas = this.service.set(null, null);
        assertIterableEquals(testArray, areas);
        areas = this.service.set(OVSD_2, OVSD_3);
        assertIterableEquals(testArray, areas);
        areas = this.service.set(OVSD_3, null);
        assertIterableEquals(testArray, areas);
    }

    @Test
    void get() {
        String dof = this.service.get(0);
        String cvo = this.service.get(1);
        String nullString = this.service.get(2);
        assertEquals(OVSD_1, dof);
        assertEquals(OVSD_2, cvo);
        assertNull(nullString);
    }

    @Test
    void clear() {
        this.service.clear();
        assertEquals(0, this.service.getAll().size());
    }

    @Test
    void rewriteInCurrentThread() {
        ArrayList<String>testArray = new ArrayList<>();
        testArray.add(OVSD_3);
        testArray.add(OVSD_2);
        testArray.add(OVSD_1);
        this.service.rewriteInCurrentThread(testArray);
        assertIterableEquals(testArray, this.service.getAll());
        this.service.rewriteInCurrentThread(null);
        assertIterableEquals(testArray, this.service.getAll());
    }

    @Test
    void resetToDefault() {
        ArrayList<String>testArray = DefaultAreas.get();
        this.service.resetToDefault();
        assertIterableEquals(testArray, this.service.getAll());
    }
}