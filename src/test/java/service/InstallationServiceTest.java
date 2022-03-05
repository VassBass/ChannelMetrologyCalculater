package service;

import def.DefaultInstallations;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sqlite.JDBC;
import service.impl.InstallationServiceImpl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class InstallationServiceTest {
    private static final String DB_URL = "jdbc:sqlite:TestData.db";
    private final InstallationService service = new InstallationServiceImpl(DB_URL);

    private static final String MEL = "Млин";
    private static final String PICH = "Піч";
    private static final String VENT = "Вентилятор";

    @BeforeEach
    void setUp() {
        try {
            DriverManager.registerDriver(new JDBC());
            Connection connection = DriverManager.getConnection(DB_URL);
            String sql = "DELETE FROM installations;";
            Statement statement = connection.createStatement();
            statement.execute(sql);
            statement.close();
            this.service.rewriteInCurrentThread(new ArrayList<String>());

            sql = "INSERT INTO installations ('installation') "
                    + "VALUES "
                    +       "('" + MEL + "'),"
                    +       "('" + PICH + "');";
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
        testArray.add(MEL);
        testArray.add(PICH);
        assertIterableEquals(testArray, this.service.getAll());
    }

    @Test
    void getAllInStrings() {
        String[]testArray = new String[]{MEL, PICH};
        assertArrayEquals(testArray, this.service.getAllInStrings());
    }

    @Test
    void add() {
        ArrayList<String>testArray = new ArrayList<>();
        testArray.add(MEL);
        testArray.add(PICH);
        testArray.add(VENT);
        ArrayList<String>installations = this.service.add(VENT);
        assertIterableEquals(testArray, installations);
        installations = this.service.add(null);
        assertIterableEquals(testArray, installations);
    }

    @Test
    void remove() {
        ArrayList<String>testArray = new ArrayList<>();
        testArray.add(MEL);
        ArrayList<String>installations = this.service.remove(PICH);
        assertIterableEquals(testArray, installations);
        installations = this.service.remove(null);
        assertIterableEquals(testArray, installations);
        installations = this.service.remove(VENT);
        assertIterableEquals(testArray, installations);
    }

    @Test
    void set() {
        ArrayList<String>testArray = new ArrayList<>();
        testArray.add(MEL);
        testArray.add(VENT);
        ArrayList<String>installations = this.service.set(PICH, VENT);
        assertIterableEquals(testArray, installations);
        installations = this.service.set(null, VENT);
        assertIterableEquals(testArray, installations);
        installations = this.service.set(null, null);
        assertIterableEquals(testArray, installations);
        installations = this.service.set(PICH, VENT);
        assertIterableEquals(testArray, installations);
        installations = this.service.set(VENT, null);
        assertIterableEquals(testArray, installations);
    }

    @Test
    void get() {
        String dof = this.service.get(0);
        String cvo = this.service.get(1);
        String nullString = this.service.get(2);
        assertEquals(MEL, dof);
        assertEquals(PICH, cvo);
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
        testArray.add(VENT);
        testArray.add(PICH);
        testArray.add(MEL);
        this.service.rewriteInCurrentThread(testArray);
        assertIterableEquals(testArray, this.service.getAll());
        this.service.rewriteInCurrentThread(null);
        assertIterableEquals(testArray, this.service.getAll());
    }

    @Test
    void resetToDefault() {
        ArrayList<String>testArray = DefaultInstallations.get();
        this.service.resetToDefault();
        assertIterableEquals(testArray, this.service.getAll());
    }
}