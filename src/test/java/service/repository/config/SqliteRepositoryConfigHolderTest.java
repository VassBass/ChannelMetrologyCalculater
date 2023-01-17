package service.repository.config;

import org.junit.Before;
import org.junit.Test;
import service.repository.repos.area.AreaRepository;
import service.repository.repos.calibrator.CalibratorRepository;
import service.repository.repos.channel.ChannelRepository;
import service.repository.repos.department.DepartmentRepository;
import service.repository.repos.installation.InstallationRepository;
import service.repository.repos.measurement.MeasurementRepository;
import service.repository.repos.person.PersonRepository;
import service.repository.repos.process.ProcessRepository;
import service.repository.repos.sensor.SensorRepository;

import static org.junit.Assert.*;

public class SqliteRepositoryConfigHolderTest {

    private SqliteRepositoryConfigHolder configHolder;
    private SqliteRepositoryConfigHolder configHolderTestInstance;

    @Before
    public void setUp() {
        configHolder = new SqliteRepositoryConfigHolder();
        configHolderTestInstance = new SqliteRepositoryConfigHolder("properties/test_repository.properties");
    }

    @Test
    public void testGetTestInstance() {
        assertNotSame(configHolder, configHolderTestInstance);
    }

    @Test
    public void testGetDBUrl() {
        String dbUrl = "jdbc:sqlite:Data.db";
        String testDbUrl = "jdbc:sqlite:TestData.db";

        assertEquals(dbUrl, configHolder.getDBUrl());
        assertEquals(testDbUrl, configHolderTestInstance.getDBUrl());
    }

    @Test
    public void testGetUser() {
        assertTrue(configHolder.getUser().isEmpty());
        assertTrue(configHolderTestInstance.getUser().isEmpty());
    }

    @Test
    public void testGetPassword() {
        assertTrue(configHolder.getPassword().isEmpty());
        assertTrue(configHolderTestInstance.getPassword().isEmpty());
    }

    @Test
    public void testGetMeasurementTableName() {
        String measurementTableName = "measurements";

        assertEquals(measurementTableName, configHolder.getTableName(MeasurementRepository.class));
        assertEquals(measurementTableName, configHolderTestInstance.getTableName(MeasurementRepository.class));
    }

    @Test
    public void testGetSensorTableName() {
        String sensorTableName = "sensors";

        assertEquals(sensorTableName, configHolder.getTableName(SensorRepository.class));
        assertEquals(sensorTableName, configHolderTestInstance.getTableName(SensorRepository.class));
    }

    @Test
    public void testGetChannelTableName() {
        String channelTableName = "channels";

        assertEquals(channelTableName, configHolder.getTableName(ChannelRepository.class));
        assertEquals(channelTableName, configHolderTestInstance.getTableName(ChannelRepository.class));
    }

    @Test
    public void testGetDepartmentTableName() {
        String departmentTableName = "departments";

        assertEquals(departmentTableName, configHolder.getTableName(DepartmentRepository.class));
        assertEquals(departmentTableName, configHolderTestInstance.getTableName(DepartmentRepository.class));
    }

    @Test
    public void testGetAreaTableName() {
        String areaTableName = "areas";

        assertEquals(areaTableName, configHolder.getTableName(AreaRepository.class));
        assertEquals(areaTableName, configHolderTestInstance.getTableName(AreaRepository.class));
    }

    @Test
    public void testGetProcessTableName() {
        String processTableName = "processes";

        assertEquals(processTableName, configHolder.getTableName(ProcessRepository.class));
        assertEquals(processTableName, configHolderTestInstance.getTableName(ProcessRepository.class));
    }

    @Test
    public void testGetInstallationTableName() {
        String installationTableName = "installations";

        assertEquals(installationTableName, configHolder.getTableName(InstallationRepository.class));
        assertEquals(installationTableName, configHolderTestInstance.getTableName(InstallationRepository.class));
    }

    @Test
    public void testGetCalibratorTableName() {
        String calibratorTableName = "calibrators";

        assertEquals(calibratorTableName, configHolder.getTableName(CalibratorRepository.class));
        assertEquals(calibratorTableName, configHolderTestInstance.getTableName(CalibratorRepository.class));
    }

    @Test
    public void testGetPersonTableName() {
        String personTableName = "persons";

        assertEquals(personTableName, configHolder.getTableName(PersonRepository.class));
        assertEquals(personTableName, configHolderTestInstance.getTableName(PersonRepository.class));
    }

    @Test
    public void testGetUnknownTableName() {
        assertTrue(configHolder.getTableName(SqliteRepositoryConfigHolderTest.class).isEmpty());
        assertTrue(configHolderTestInstance.getTableName(SqliteRepositoryConfigHolderTest.class).isEmpty());
    }
}