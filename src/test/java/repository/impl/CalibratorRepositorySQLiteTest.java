package repository.impl;

import model.Calibrator;
import model.Measurement;
import org.junit.*;
import org.sqlite.JDBC;
import repository.CalibratorRepository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.Assert.assertTrue;

public class CalibratorRepositorySQLiteTest {

    private static final String DB_URL = "jdbc:sqlite:TestData.db";
    private static final CalibratorRepository repository = new CalibratorRepositorySQLite(DB_URL, null, null);

    private static Calibrator[] testCalibrators;

    private static Calibrator createCalibrator(int number){
        Calibrator calibrator = new Calibrator("calibrator" + number);
        calibrator.setType("type" + number);
        calibrator._setCertificateName("certificate" + number);
        calibrator._setCertificateType("certificateType" + number);
        calibrator._setCertificateDate("23.03.2022");
        calibrator._setCertificateCompany("company" + number);
        calibrator.setNumber(String.valueOf(number));
        String measurement = number < 3 ? Measurement.TEMPERATURE : number < 5 ? Measurement.PRESSURE : Measurement.CONSUMPTION;
        String value = number < 3 ? Measurement.DEGREE_CELSIUS : number < 5 ? Measurement.KPA : Measurement.M3_HOUR;
        calibrator.setMeasurement(measurement);
        calibrator.setValue(value);
        calibrator.setRangeMin(0D);
        calibrator.setRangeMax(100D);
        calibrator.setErrorFormula(number + "+" + number);
        return calibrator;
    }



    @BeforeClass
    public static void testCreateTable(){
        testCalibrators = new Calibrator[7];
        for (int n = 0;n < 7;n++){
            testCalibrators[n] = createCalibrator(n);
        }

        assertTrue(repository.createTable());
    }

    @AfterClass
    public static void removeTable() throws SQLException {
        String sql = "DROP TABLE calibrators;";
        DriverManager.registerDriver(new JDBC());
        try (Connection connection = DriverManager.getConnection(DB_URL);
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
        }
    }

    @Before
    public void fuelingTestDB() throws SQLException {
        String insertSql = "INSERT INTO calibrators (name, type, number, measurement, value, error_formula, certificate, range_min, range_max) "
                + "VALUES ";
        StringBuilder sql = new StringBuilder(insertSql);

        for (Calibrator calibrator : testCalibrators) {
            sql.append("('").append(calibrator.getName()).append("', ")
                    .append("'").append(calibrator.getType()).append("', ")
                    .append("'").append(calibrator.getNumber()).append("', ")
                    .append("'").append(calibrator.getMeasurement()).append("', ")
                    .append("'").append(calibrator.getValue()).append("', ")
                    .append("'").append(calibrator.getErrorFormula()).append("', ")
                    .append("'").append(calibrator.getCertificate()).append("', ")
                    .append(calibrator.getRangeMin()).append(", ")
                    .append(calibrator.getRangeMax()).append("),");
        }
        sql.setCharAt(sql.length()-1, ';');


        DriverManager.registerDriver(new JDBC());
        try (Connection connection = DriverManager.getConnection(DB_URL);
             Statement statement = connection.createStatement()) {
            statement.execute(sql.toString());
        }
    }

    @After
    public void clearTestDB() throws Exception {
        String sql = "DELETE FROM calibrators;";
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
    public void getAllNames() {
    }

    @Test
    public void get() {
    }

    @Test
    public void add() {
    }

    @Test
    public void remove() {
    }

    @Test
    public void removeByMeasurementValue() {
    }

    @Test
    public void set() {
    }

    @Test
    public void changeMeasurementValue() {
    }

    @Test
    public void clear() {
    }

    @Test
    public void rewrite() {
    }

    @Test
    public void importData() {
    }

    @Test
    public void isExists() {
    }
}