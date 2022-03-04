package service;

import def.DefaultDepartments;
import def.DefaultProcesses;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sqlite.JDBC;
import service.impl.ProcessServiceImpl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ProcessServiceTest {
    private static final String DB_URL = "jdbc:sqlite:TestData.db";
    private final ProcessService service = new ProcessServiceImpl(DB_URL);

    private static final String SECTION = "Секція";
    private static final String TECHNOLOGY_LINE = "Технологічна лінія";
    private static final String TRAKT = "Тракт";

    @BeforeEach
    void setUp() {
        try {
            DriverManager.registerDriver(new JDBC());
            Connection connection = DriverManager.getConnection(DB_URL);
            String sql = "DELETE FROM processes;";
            Statement statement = connection.createStatement();
            statement.execute(sql);
            statement.close();
            this.service.rewriteInCurrentThread(new ArrayList<String>());

            sql = "INSERT INTO processes ('process') "
                    + "VALUES "
                    +       "('" + SECTION + "'),"
                    +       "('" + TECHNOLOGY_LINE + "');";
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
        testArray.add(SECTION);
        testArray.add(TECHNOLOGY_LINE);
        assertIterableEquals(testArray, this.service.getAll());
    }

    @Test
    void getAllInStrings() {
        String[]testArray = new String[]{SECTION, TECHNOLOGY_LINE};
        assertArrayEquals(testArray, this.service.getAllInStrings());
    }

    @Test
    void add() {
        ArrayList<String>testArray = new ArrayList<>();
        testArray.add(SECTION);
        testArray.add(TECHNOLOGY_LINE);
        testArray.add(TRAKT);
        ArrayList<String>processes = this.service.add(TRAKT);
        assertIterableEquals(testArray, processes);
        processes = this.service.add(null);
        assertIterableEquals(testArray, processes);
    }

    @Test
    void remove() {
        ArrayList<String>testArray = new ArrayList<>();
        testArray.add(SECTION);
        ArrayList<String>processes = this.service.remove(TECHNOLOGY_LINE);
        assertIterableEquals(testArray, processes);
        processes = this.service.remove(null);
        assertIterableEquals(testArray, processes);
        processes = this.service.remove(TRAKT);
        assertIterableEquals(testArray, processes);
    }

    @Test
    void set() {
        ArrayList<String>testArray = new ArrayList<>();
        testArray.add(SECTION);
        testArray.add(TRAKT);
        ArrayList<String>processes = this.service.set(TECHNOLOGY_LINE, TRAKT);
        assertIterableEquals(testArray, processes);
        processes = this.service.set(null, TRAKT);
        assertIterableEquals(testArray, processes);
        processes = this.service.set(null, null);
        assertIterableEquals(testArray, processes);
        processes = this.service.set(TECHNOLOGY_LINE, TRAKT);
        assertIterableEquals(testArray, processes);
        processes = this.service.set(TRAKT, null);
        assertIterableEquals(testArray, processes);
    }

    @Test
    void get() {
        String dof = this.service.get(0);
        String cvo = this.service.get(1);
        String nullString = this.service.get(2);
        assertEquals(SECTION, dof);
        assertEquals(TECHNOLOGY_LINE, cvo);
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
        testArray.add(TRAKT);
        testArray.add(TECHNOLOGY_LINE);
        testArray.add(SECTION);
        this.service.rewriteInCurrentThread(testArray);
        assertIterableEquals(testArray, this.service.getAll());
        this.service.rewriteInCurrentThread(null);
        assertIterableEquals(testArray, this.service.getAll());
    }

    @Test
    void resetToDefault() {
        ArrayList<String>testArray = DefaultProcesses.get();
        this.service.resetToDefault();
        assertIterableEquals(testArray, this.service.getAll());
    }
}