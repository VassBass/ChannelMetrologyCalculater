package service.impl;

import def.DefaultCalibrators;
import model.Calibrator;
import model.Measurement;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import repository.impl.CalibratorRepositorySQLite;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

public class CalibratorServiceImplTest {

    private static final String DB_URL = "jdbc:sqlite:TestData.db";
    private static final CalibratorServiceImpl service = new CalibratorServiceImpl(new CalibratorRepositorySQLite(DB_URL, null, null));

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

    @Before
    public void setUp() {
        testCalibrators = new Calibrator[7];
        for (int i=0;i<7;i++){
            testCalibrators[i] = createCalibrator(i);
            service.add(createCalibrator(i));
        }
    }

    @After
    public void tearDown() {
        testCalibrators = null;
        service.clear();
    }

    @Test
    public void testGetInstance() {
        assertSame(service, CalibratorServiceImpl.getInstance());
    }

    @Test
    public void testGetAll() {
        assertArrayEquals(testCalibrators, service.getAll().toArray(new Calibrator[0]));
    }

    @Test
    public void testGetAllNames() {
        String[] expectedTemperature = new String[3];
        String[] expectedPressure = new String[2];
        String[] expectedConsumption = new String[2];
        for (int i=0;i< testCalibrators.length;i++){
            if (i < 3) {
                expectedTemperature[i] = testCalibrators[i].getName();
            }else if (i < 5){
                expectedPressure[i-3] = testCalibrators[i].getName();
            }else {
                expectedConsumption[i-5] = testCalibrators[i].getName();
            }
        }

        assertArrayEquals(expectedTemperature, service.getAllNames(new Measurement(Measurement.TEMPERATURE, Measurement.DEGREE_CELSIUS)));
        assertArrayEquals(expectedPressure, service.getAllNames(new Measurement(Measurement.PRESSURE, Measurement.KPA)));
        assertArrayEquals(expectedConsumption, service.getAllNames(new Measurement(Measurement.CONSUMPTION, Measurement.M3_HOUR)));
    }

    @Test
    public void testAddNotExisted() {
        Calibrator[] expected = Arrays.copyOf(testCalibrators, 8);
        expected[expected.length-1] = createCalibrator(7);

        assertTrue(service.add(createCalibrator(7)));
        assertArrayEquals(expected, service.getAll().toArray(new Calibrator[0]));
    }

    @Test
    public void testAddExisted() {
        assertFalse(service.add(createCalibrator(2)));
        assertArrayEquals(testCalibrators, service.getAll().toArray(new Calibrator[0]));
    }

    @Test
    public void testRemoveExisted() {
        Calibrator[] expected = Arrays.copyOf(testCalibrators, 6);

        assertTrue(service.remove(createCalibrator(6)));
        assertArrayEquals(expected, service.getAll().toArray(new Calibrator[0]));
    }

    @Test
    public void testRemoveNotExisted() {
        assertFalse(service.remove(createCalibrator(8)));
        assertArrayEquals(testCalibrators, service.getAll().toArray(new Calibrator[0]));
    }

    @Test
    public void testRemoveByMeasurementValueExisted() {
        Calibrator[]expected = Arrays.copyOf(testCalibrators, 5);

        assertTrue(service.removeByMeasurementValue(Measurement.M3_HOUR));
        assertArrayEquals(expected, service.getAll().toArray(new Calibrator[0]));
    }

    @Test
    public void testRemoveByMeasurementValueNotExisted() {
        assertFalse(service.removeByMeasurementValue("Not Existed"));
        assertArrayEquals(testCalibrators, service.getAll().toArray(new Calibrator[0]));
    }

    @Test
    public void testSetSame() {
        assertTrue(service.set(createCalibrator(2), createCalibrator(2)));
        assertArrayEquals(testCalibrators, service.getAll().toArray(new Calibrator[0]));
    }

    @Test
    public void testSetNew() {
        testCalibrators[2] = createCalibrator(8);

        assertTrue(service.set(createCalibrator(2), createCalibrator(8)));
        assertArrayEquals(testCalibrators, service.getAll().toArray(new Calibrator[0]));
    }

    @Test
    public void testSetExisted() {
        assertFalse(service.set(createCalibrator(2), createCalibrator(0)));
        assertArrayEquals(testCalibrators, service.getAll().toArray(new Calibrator[0]));
    }

    @Test
    public void testSetInsteadNotExisted() {
        assertFalse(service.set(createCalibrator(8), createCalibrator(0)));
        assertArrayEquals(testCalibrators, service.getAll().toArray(new Calibrator[0]));
    }

    @Test
    public void testChangeToSameMeasurementValue() {
        assertTrue(service.changeMeasurementValue(Measurement.DEGREE_CELSIUS, Measurement.DEGREE_CELSIUS));
        assertArrayEquals(testCalibrators, service.getAll().toArray(new Calibrator[0]));
    }

    @Test
    public void testChangeToNewMeasurementValue() {
        testCalibrators[0].setValue(Measurement.PA);
        testCalibrators[1].setValue(Measurement.PA);
        testCalibrators[2].setValue(Measurement.PA);

        assertTrue(service.changeMeasurementValue(Measurement.DEGREE_CELSIUS, Measurement.PA));
        assertArrayEquals(testCalibrators, service.getAll().toArray(new Calibrator[0]));
    }

    @Test
    public void testChangeToExistedMeasurementValue() {
        testCalibrators[0].setValue(Measurement.KPA);
        testCalibrators[1].setValue(Measurement.KPA);
        testCalibrators[2].setValue(Measurement.KPA);

        assertTrue(service.changeMeasurementValue(Measurement.DEGREE_CELSIUS, Measurement.KPA));
        assertArrayEquals(testCalibrators, service.getAll().toArray(new Calibrator[0]));
    }

    @Test
    public void testChangeInsteadNotExistedMeasurementValue() {
        assertFalse(service.changeMeasurementValue(Measurement.PA, Measurement.KPA));
        assertArrayEquals(testCalibrators, service.getAll().toArray(new Calibrator[0]));
    }

    @Test
    public void testGetExisted() {
        assertEquals(createCalibrator(1), service.get("calibrator1"));
    }

    @Test
    public void testGetNotExisted() {
        assertNull(service.get("Not Existed"));
    }

    @Test
    public void testClear() {
        assertTrue(service.clear());
        assertArrayEquals(new Calibrator[0], service.getAll().toArray(new Calibrator[0]));
    }

    @Test
    public void testImportDataWithNewAndChanging() {
        testCalibrators[2].setErrorFormula("New Error-Formula");
        testCalibrators[4].setNumber("New Number");
        Calibrator[] expected = Arrays.copyOf(testCalibrators, 9);
        expected[expected.length-2] = createCalibrator(8);
        expected[expected.length-1] = createCalibrator(9);

        ArrayList<Calibrator>forChange = new ArrayList<>();
        forChange.add(testCalibrators[2]);
        forChange.add(testCalibrators[4]);

        ArrayList<Calibrator>newCal = new ArrayList<>();
        newCal.add(createCalibrator(8));
        newCal.add(createCalibrator(9));

        assertTrue(service.importData(newCal, forChange));
        assertArrayEquals(expected, service.getAll().toArray(new Calibrator[0]));
    }

    @Test
    public void testImportDataOnlyWithNew() {
        Calibrator[] expected = Arrays.copyOf(testCalibrators, 9);
        expected[expected.length-2] = createCalibrator(8);
        expected[expected.length-1] = createCalibrator(9);

        ArrayList<Calibrator>newCal = new ArrayList<>();
        newCal.add(createCalibrator(8));
        newCal.add(createCalibrator(9));

        assertTrue(service.importData(newCal, new ArrayList<Calibrator>()));
        assertArrayEquals(expected, service.getAll().toArray(new Calibrator[0]));
    }

    @Test
    public void testImportDataOnlyWithChanging() {
        testCalibrators[2].setErrorFormula("New Error-Formula");
        testCalibrators[4].setNumber("New Number");

        ArrayList<Calibrator>forChange = new ArrayList<>();
        forChange.add(testCalibrators[2]);
        forChange.add(testCalibrators[4]);

        assertTrue(service.importData(new ArrayList<Calibrator>(), forChange));
        assertArrayEquals(testCalibrators, service.getAll().toArray(new Calibrator[0]));
    }

    @Test
    public void testImportDataWithoutNewAndChanging() {
        assertTrue(service.importData(new ArrayList<Calibrator>(), new ArrayList<Calibrator>()));
        assertArrayEquals(testCalibrators, service.getAll().toArray(new Calibrator[0]));
    }

    @Test
    public void testRewriteNotEmpty() {
        Calibrator[] expected = new Calibrator[]{createCalibrator(8), createCalibrator(9), createCalibrator(0)};

        assertTrue(service.rewrite(Arrays.asList(expected)));
        assertArrayEquals(expected, service.getAll().toArray(new Calibrator[0]));
    }

    @Test
    public void testRewriteEmpty() {
        assertTrue(service.rewrite(new ArrayList<Calibrator>()));
        assertArrayEquals(new Calibrator[0], service.getAll().toArray(new Calibrator[0]));
    }

    @Test
    public void testResetToDefault() {
        assertTrue(service.resetToDefault());
        assertArrayEquals(DefaultCalibrators.get().toArray(new Calibrator[0]), service.getAll().toArray(new Calibrator[0]));
    }

    @Test
    public void testIsExists() {
        assertTrue(service.isExists(createCalibrator(0)));
        assertFalse(service.isExists(createCalibrator(8)));
    }
}