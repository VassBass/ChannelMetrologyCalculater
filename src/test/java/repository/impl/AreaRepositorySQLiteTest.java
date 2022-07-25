package repository.impl;

import org.junit.*;
import org.sqlite.JDBC;
import repository.Repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.Assert.*;

public class AreaRepositorySQLiteTest {

    private static final String DB_URL = "jdbc:sqlite:TestData.db";
    private static final Repository<String>repository = new AreaRepositorySQLite(DB_URL, null, null);

    private static final String AREA_1 = "area1";
    private static final String AREA_2 = "area2";
    private static final String AREA_3 = "area3";
    private static final String AREA_4 = "area4";
    private static final String AREA_5 = "area5";

    @BeforeClass
    public static void testCreateTable(){
        assertTrue(repository.createTable());
    }

    @AfterClass
    public static void removeTable() throws SQLException {
        String sql = "DROP TABLE areas;";
        DriverManager.registerDriver(new JDBC());
        try (Connection connection = DriverManager.getConnection(DB_URL);
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
        }
    }

    @Before
    public void setUp() throws SQLException {
        String sql = "INSERT INTO areas VALUES "
                + "('" + AREA_1 + "')"
                + ", ('" + AREA_2 + "')"
                + ", ('" + AREA_3 + "')"
                + ", ('" + AREA_4 + "')"
                + ", ('" + AREA_5 + "')"
                + ";";

        DriverManager.registerDriver(new JDBC());
        try (Connection connection = DriverManager.getConnection(DB_URL);
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
        }
    }

    @After
    public void tearDown() throws Exception {
        String sql = "DELETE FROM areas;";
        DriverManager.registerDriver(new JDBC());
        try (Connection connection = DriverManager.getConnection(DB_URL);
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
        }
    }

    @Test
    public void createTable() {
    }

    @Test
    public void getAll() {
    }

    @Test
    public void add() {
    }

    @Test
    public void set() {
    }

    @Test
    public void remove() {
    }

    @Test
    public void clear() {
    }

    @Test
    public void rewrite() {
    }
}