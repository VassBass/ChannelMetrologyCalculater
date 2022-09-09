package service.impl;

import model.Measurement;
import model.Sensor;
import org.junit.*;
import org.sqlite.JDBC;
import repository.SensorRepository;
import repository.impl.SensorRepositorySQLite;
import service.SensorService;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class SensorServiceImplTest {

    private static final String DB_URL = "jdbc:sqlite:TestData.db";
    private static final SensorRepository repository = new SensorRepositorySQLite(DB_URL, null, null);
    private static final SensorService service = new SensorServiceImpl(repository);

    private static Sensor[] testSensors;

    private static Sensor createSensor(int number, Measurement measurement){
        Sensor sensor = new Sensor("sensor" + number);
        sensor.setType("type" + number);
        sensor.setName("name" + number);
        sensor.setRange(number, number+100);
        sensor.setNumber(String.valueOf(number));
        sensor.setValue(measurement.getValue());
        sensor.setMeasurement(measurement.getName());
        sensor.setErrorFormula("2 * " + number);
        return sensor;
    }

    @BeforeClass
    public static void testCreateTable(){
        assertTrue(repository.createTable());
    }

    @AfterClass
    public static void removeTable() throws SQLException {
        String sql = "DROP TABLE sensors;";
        DriverManager.registerDriver(new JDBC());
        try (Connection connection = DriverManager.getConnection(DB_URL);
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
        }
    }

    @Before
    public void fuelingTestSensorsList() {
        testSensors = new Sensor[7];
        for (int n = 0;n < 7;n++){
            if (n < 3) {
                Sensor temperature = createSensor(n, new Measurement(Measurement.TEMPERATURE, Measurement.DEGREE_CELSIUS));
                testSensors[n] = temperature;
                repository.add(temperature);
            }else if (n < 5) {
                Sensor pressure = createSensor(n, new Measurement(Measurement.PRESSURE, Measurement.KPA));
                testSensors[n] = pressure;
                repository.add(pressure);
            }else {
                Sensor consumption = createSensor(n, new Measurement(Measurement.CONSUMPTION, Measurement.M3_HOUR));
                consumption.setType("type " + Sensor.ROSEMOUNT);
                testSensors[n] = consumption;
                repository.add(consumption);
            }
        }
    }

    @After
    public void clearTestSensorsList() {
        testSensors = null;
        repository.clear();
    }

    @Test
    public void testGetAllTypesWithoutROSEMOUNT() {
        String[] expected = Arrays.stream(Arrays.copyOf(testSensors, 5))
                .map(Sensor::getType)
                .toArray(String[]::new);

        List<String> actual = service.getAllTypesWithoutROSEMOUNT();

        assertFalse(actual.stream().anyMatch(t -> t.contains(Sensor.ROSEMOUNT)));
        assertArrayEquals(expected, actual.toArray(new String[0]));
    }
}