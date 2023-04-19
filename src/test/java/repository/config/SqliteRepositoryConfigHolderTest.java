package repository.config;

import org.junit.Before;
import org.junit.Test;
import repository.repos.area.AreaRepository;
import repository.repos.calibrator.CalibratorRepository;
import repository.repos.channel.ChannelRepository;
import repository.repos.department.DepartmentRepository;
import repository.repos.installation.InstallationRepository;
import repository.repos.measurement.MeasurementRepository;
import repository.repos.person.PersonRepository;
import repository.repos.process.ProcessRepository;
import repository.repos.sensor.SensorRepository;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.junit.Assert.*;

public class SqliteRepositoryConfigHolderTest {
    private static final String TEST_REPOSITORY_PROPERTIES_FILE = "properties/repository_test.properties";

    private SqliteRepositoryConfigHolder configHolder;
    private SqliteRepositoryConfigHolder configHolderPropertiesFileNotExists;

    @Before
    public void setUp() {
        configHolder = new SqliteRepositoryConfigHolder(TEST_REPOSITORY_PROPERTIES_FILE);
        configHolderPropertiesFileNotExists = new SqliteRepositoryConfigHolder(EMPTY);
    }

    @Test
    public void testGetDBUrl() {
        String testDbUrl = "jdbc:sqlite:TestData.db";

        assertEquals(testDbUrl, configHolder.getDBUrl());
        assertNotNull(configHolderPropertiesFileNotExists.getDBUrl());
    }

    @Test
    public void testGetUser() {
        assertTrue(configHolder.getUser().isEmpty());
        assertNotNull(configHolderPropertiesFileNotExists.getUser());
    }

    @Test
    public void testGetPassword() {
        assertTrue(configHolder.getPassword().isEmpty());
        assertNotNull(configHolderPropertiesFileNotExists.getPassword());
    }

    @Test
    public void testGetDBFile() {
        String testDataFile = "TestData.db";

        assertEquals(testDataFile, configHolder.getDBFile());
        assertNotNull(configHolderPropertiesFileNotExists.getDBFile());
    }

    @Test
    public void testGetMeasurementTableName() {
        String measurementTableName = "measurements";

        assertEquals(measurementTableName, configHolder.getTableName(MeasurementRepository.class));
        assertEquals(measurementTableName, configHolderPropertiesFileNotExists.getTableName(MeasurementRepository.class));
    }

    @Test
    public void testGetSensorTableName() {
        String sensorTableName = "sensors";

        assertEquals(sensorTableName, configHolder.getTableName(SensorRepository.class));
        assertEquals(sensorTableName, configHolderPropertiesFileNotExists.getTableName(SensorRepository.class));
    }

    @Test
    public void testGetChannelTableName() {
        String channelTableName = "channels";

        assertEquals(channelTableName, configHolder.getTableName(ChannelRepository.class));
        assertEquals(channelTableName, configHolderPropertiesFileNotExists.getTableName(ChannelRepository.class));
    }

    @Test
    public void testGetDepartmentTableName() {
        String departmentTableName = "departments";

        assertEquals(departmentTableName, configHolder.getTableName(DepartmentRepository.class));
        assertEquals(departmentTableName, configHolderPropertiesFileNotExists.getTableName(DepartmentRepository.class));
    }

    @Test
    public void testGetAreaTableName() {
        String areaTableName = "areas";

        assertEquals(areaTableName, configHolder.getTableName(AreaRepository.class));
        assertEquals(areaTableName, configHolderPropertiesFileNotExists.getTableName(AreaRepository.class));
    }

    @Test
    public void testGetProcessTableName() {
        String processTableName = "processes";

        assertEquals(processTableName, configHolder.getTableName(ProcessRepository.class));
        assertEquals(processTableName, configHolderPropertiesFileNotExists.getTableName(ProcessRepository.class));
    }

    @Test
    public void testGetInstallationTableName() {
        String installationTableName = "installations";

        assertEquals(installationTableName, configHolder.getTableName(InstallationRepository.class));
        assertEquals(installationTableName, configHolderPropertiesFileNotExists.getTableName(InstallationRepository.class));
    }

    @Test
    public void testGetCalibratorTableName() {
        String calibratorTableName = "calibrators";

        assertEquals(calibratorTableName, configHolder.getTableName(CalibratorRepository.class));
        assertEquals(calibratorTableName, configHolderPropertiesFileNotExists.getTableName(CalibratorRepository.class));
    }

    @Test
    public void testGetPersonTableName() {
        String personTableName = "persons";

        assertEquals(personTableName, configHolder.getTableName(PersonRepository.class));
        assertEquals(personTableName, configHolderPropertiesFileNotExists.getTableName(PersonRepository.class));
    }

    @Test
    public void testGetUnknownTableName() {
        assertNotNull(configHolder.getTableName(SqliteRepositoryConfigHolderTest.class));
        assertNotNull(configHolderPropertiesFileNotExists.getTableName(SqliteRepositoryConfigHolderTest.class));
    }
}