package service.calculation.result.worker;

import mock.RepositoryConfigHolderMock;
import mock.RepositoryMockFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class DefaultCalculationWorkerTest {

    private RepositoryMockFactory repositoryMockFactory;
    private DefaultCalculationWorker worker;

    @Before
    public void init() {
        repositoryMockFactory = new RepositoryMockFactory(new RepositoryConfigHolderMock());
        worker = new DefaultCalculationWorker(repositoryMockFactory);
    }

    @After
    public void dispose() {
        repositoryMockFactory.dispose();
    }

    @Test
    public void testCalculateAbsoluteErrors() {
        Map<Double, double[]> measurementValues = new HashMap<>();
        measurementValues.put(-23.456, new double[] {
                -23.456, -22.789, -22.123, -21.567, -22.876, -23.009, -21.345, -24.555, -23.223, -24.004
        });
        measurementValues.put(0.123, new double[] {
                1.234, -1.789, 0.123, -0.987, 1.345, -1.456, -0.234, 0.456, -1.123, 1.876
        });
        measurementValues.put(12.789, new double[] {
                11.234, 10.789, 12.123, 13.987, 11.345, 13.456, 12.234, 10.456, 14.123, 13.876
        });

        Map<Double, double[]> expected = new HashMap<>();
        expected.put(-23.456, new double[] {
                0.0, 0.667, 1.333, 1.889, 0.58, 0.447, 2.111, -1.099, 0.233, -0.548
        });
        expected.put(0.123, new double[] {
                1.111, -1.912, 0.0, -1.11, 1.222, -1.579, -0.357, 0.333, -1.246, 1.753
        });
        expected.put(12.789, new double[] {
                -1.555, -2.0, -0.666, 1.198, -1.444, 0.667, -0.555, -2.333, 1.334, 1.087
        });

        Map<Double, double[]> actual = worker.calculateAbsoluteErrors(measurementValues);
        for (Map.Entry<Double, double[]> expectedEntry : expected.entrySet()) {
            double[] expectedValues = expectedEntry.getValue();
            double[] actualValues = actual.get(expectedEntry.getKey());
            assertEquals(expectedValues.length, actualValues.length);
            for (int i = 0; i < expectedValues.length; i++) {
                assertEquals(expectedValues[i], actualValues[i], 0.001);
            }
        }
    }

    @Test
    public void testCalculateMaxAbsoluteError() {
        Map<Double, double[]> absoluteErrors = new HashMap<>();
        absoluteErrors.put(-23.456, new double[] {
                0.0, 0.667, 1.333, 1.889, 0.58, 0.447, 2.111, -1.099, 0.233, -0.548
        });
        absoluteErrors.put(0.123, new double[] {
                1.111, -1.912, 0.0, -1.11, 1.222, -1.579, -0.357, 0.333, -1.246, 1.753
        });
        absoluteErrors.put(12.789, new double[] {
                -1.555, -2.0, -0.666, 1.198, -1.444, 0.667, -0.555, -2.333, 1.334, 1.087
        });

        assertEquals(2.333, worker.calculateMaxAbsoluteError(absoluteErrors), 0.001);
    }

    @Test
    public void testCalculateAbsoluteErrorWithEquipment() {
        double maxAbsoluteError = 2.333;
        double sensorError = 0.8;
        double calibratorError = 0.3;

        double actual = worker.calculateAbsoluteErrorWithEquipment(maxAbsoluteError, sensorError, calibratorError);

        assertEquals(2.484529935, actual, 0.000000001);
    }

    @Test
    public void testCalculateRelativeErrorWithEquipment() {
        double absoluteErrorWithEquipment = 2.484529935;
        double channelRangeMin = -20.0;
        double channelRangeMax = 5.0;
        double channelRange = channelRangeMax - channelRangeMin;

        double actual = worker.calculateRelativeErrorWithEquipment(absoluteErrorWithEquipment, channelRange);
        assertEquals(9.93811974, actual, 0.00000001);
    }

    @Test
    public void testCalculateSystematicErrors() {
        Map<Double, double[]> absoluteErrors = new HashMap<>();
        absoluteErrors.put(-23.456, new double[] {
                0.0, 0.667, 1.333, 1.889, 0.58, 0.447, 2.111, -1.099, 0.233, -0.548
        });
        absoluteErrors.put(0.123, new double[] {
                1.111, -1.912, 0.0, -1.11, 1.222, -1.579, -0.357, 0.333, -1.246, 1.753
        });
        absoluteErrors.put(12.789, new double[] {
                -1.555, -2.0, -0.666, 1.198, -1.444, 0.667, -0.555, -2.333, 1.334, 1.087
        });

        Map<Double, Double> expected = new HashMap<>();
        expected.put(-23.456, 0.5613);
        expected.put(0.123, -0.1785);
        expected.put(12.789, -0.4267);

        Map<Double, Double> actual = worker.calculateSystematicErrors(absoluteErrors);
        assertEquals(expected.size(), actual.size());
        for (Map.Entry<Double, Double> expectedEntry : expected.entrySet()) {
            assertEquals(expectedEntry.getValue(), actual.get(expectedEntry.getKey()), 0.0001);
        }
    }

    @Test
    public void testCalculateStandardIndeterminacyA() {
        Map<Double, double[]> absoluteErrors = new HashMap<>();
        absoluteErrors.put(-23.456, new double[] {
                0.0, 0.667, 1.333, 1.889, 0.58, 0.447, 2.111, -1.099, 0.233, -0.548
        });
        absoluteErrors.put(0.123, new double[] {
                1.111, -1.912, 0.0, -1.11, 1.222, -1.579, -0.357, 0.333, -1.246, 1.753
        });
        absoluteErrors.put(12.789, new double[] {
                -1.555, -2.0, -0.666, 1.198, -1.444, 0.667, -0.555, -2.333, 1.334, 1.087
        });

        assertEquals(1.530627730703975, worker.calculateStandardIndeterminacyA(absoluteErrors), 0.000000001);
    }

    @Test
    public void testCalculateStandardIndeterminacyB() {
        double sensorError = 0.8;
        double calibratorError = 0.3;

        assertEquals(0.854400375, worker.calculateStandardIndeterminacyB(sensorError, calibratorError), 0.000000001);
    }

    @Test
    public void testCalculateTotalStandardIndeterminacy() {
        double a = 0.232073178;
        double b = 0.854400375;

        assertEquals(0.885357532, worker.calculateTotalStandardIndeterminacy(a, b), 0.000000001);
    }

    @Test
    public void testCalculateExtendedIndeterminacy() {
        double total = 0.885357532;

        assertEquals(1.770715065, worker.calculateExtendedIndeterminacy(total, 2.0), 0.00000001);
    }
}