package service.error_calculater;

import mock.RepositoryConfigHolderMock;
import mock.RepositoryMockFactory;
import model.dto.Calibrator;
import model.dto.Channel;
import model.dto.Measurement;
import model.dto.Sensor;
import model.dto.builder.CalibratorBuilder;
import model.dto.builder.ChannelBuilder;
import model.dto.builder.SensorBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import repository.repos.measurement_factor.MeasurementFactorRepository;

import static org.junit.Assert.*;

public class MxParserErrorCalculaterTest {

    private RepositoryMockFactory repositoryFactory;
    private Channel channel;

    @Before
    public void init() {
        repositoryFactory = new RepositoryMockFactory(new RepositoryConfigHolderMock());

        MeasurementFactorRepository measurementFactorRepository = repositoryFactory.getImplementation(MeasurementFactorRepository.class);
        measurementFactorRepository.add(Measurement.KPA, Measurement.KGS_MM2, 0.0001019716212978);

        channel = new ChannelBuilder().setRange(-774, 876).setMeasurementValue(Measurement.KGS_MM2).build();
    }

    @Test
    public void testIsFormulaValid() {
        String validFormula1 = "R²² - r² + 2.5";
        String validFormula2 = "convR * conv(3.8) + R * r";
        String validFormula3 = "√(convR + conv(1.5)) / (R + r + 0.5)";
        String validFormula4 = "(convR - conv(2.7)) * (r + R) / 2";
        String validFormula5 = "(convR² + conv(0.6)²) / (R² + r²)";
        String invalidFormula1 = "((convR² + conv(0.6)²) / (R² + r²)";
        String invalidFormula2 = "(convR² + conv(0.6)²)( / (R² + r²)";
        String invalidFormula3 = "(convR² + conv(0.6)²) /* (R² + r²)";
        String invalidFormula4 = "(convR² + conv(0.6)²) /s (R² + r²)";

        assertTrue(MxParserErrorCalculater.isFormulaValid(validFormula1));
        assertTrue(MxParserErrorCalculater.isFormulaValid(validFormula2));
        assertTrue(MxParserErrorCalculater.isFormulaValid(validFormula3));
        assertTrue(MxParserErrorCalculater.isFormulaValid(validFormula4));
        assertTrue(MxParserErrorCalculater.isFormulaValid(validFormula5));

        assertFalse(MxParserErrorCalculater.isFormulaValid(invalidFormula1));
        assertFalse(MxParserErrorCalculater.isFormulaValid(invalidFormula2));
        assertFalse(MxParserErrorCalculater.isFormulaValid(invalidFormula3));
        assertFalse(MxParserErrorCalculater.isFormulaValid(invalidFormula4));
    }

    @After
    public void dispose() {
        repositoryFactory.dispose();
    }

    @Test
    public void testCalculateCalibratorError() {
        Calibrator calibrator1 = new CalibratorBuilder()
                .setRange(-589, 876)
                .setMeasurementValue(Measurement.KPA)
                .setErrorFormula("R² - r² + 2.5")
                .build();
        Calibrator calibrator2 = new CalibratorBuilder()
                .setRange(763, -987)
                .setMeasurementValue(Measurement.KPA)
                .setErrorFormula("convR * conv(3.8) + R * r")
                .build();
        Calibrator calibrator3 = new CalibratorBuilder()
                .setRange(-452, 655)
                .setMeasurementValue(Measurement.KPA)
                .setErrorFormula("(convR + conv(1.5)) / (R + r + 0.5)")
                .build();
        Calibrator calibrator4 = new CalibratorBuilder()
                .setRange(946, -69)
                .setMeasurementValue(Measurement.KPA)
                .setErrorFormula("(convR - conv(2.7)) * (r + R) / 2")
                .build();
        Calibrator calibrator5 = new CalibratorBuilder()
                .setRange(542, 816)
                .setMeasurementValue(Measurement.KPA)
                .setErrorFormula("(convR² + conv(0.6)²) / (R² + r²)")
                .build();

        MxParserErrorCalculater errorCalculater = new MxParserErrorCalculater(repositoryFactory, channel);

        assertEquals(576277.5, errorCalculater.calculate(calibrator1), 0.0);
        assertEquals(2887500.000069148, errorCalculater.calculate(calibrator2), 0.0);
        assertEquals(0.00004099203706567952, errorCalculater.calculate(calibrator3), 0.0);
        assertEquals(137.5484747594841, errorCalculater.calculate(calibrator4), 0.0);
        assertEquals(2.790486741706431e-10, errorCalculater.calculate(calibrator5), 0.0);
    }

    @Test
    public void testCalculateSensorError() {
        Sensor sensor1 = new SensorBuilder()
                .setRange(-589, 876)
                .setMeasurementValue(Measurement.KPA)
                .setErrorFormula("(R + 2²²) * (r - 1.5) / (convR + conv(0.8))")
                .build();
        Sensor sensor2 = new SensorBuilder()
                .setRange(763, -987)
                .setMeasurementValue(Measurement.KPA)
                .setErrorFormula("convR * (conv(0.9) - 1) + R * (r + 3.2)")
                .build();
        Sensor sensor3 = new SensorBuilder()
                .setRange(-452, 655)
                .setMeasurementValue(Measurement.KPA)
                .setErrorFormula("(R + r) * (convR - conv(0.7)) / (R * r)")
                .build();
        Sensor sensor4 = new SensorBuilder()
                .setRange(946, -69)
                .setMeasurementValue(Measurement.KPA)
                .setErrorFormula("(convR + conv(0.3)) * (r^2 + R^2)^0.5 - 2 * R * r")
                .build();
        Sensor sensor5 = new SensorBuilder()
                .setRange(542, 816)
                .setMeasurementValue(Measurement.KPA)
                .setErrorFormula("convR * r^2 / (2 * R + conv(0.2))")
                .build();

        MxParserErrorCalculater errorCalculater = new MxParserErrorCalculater(repositoryFactory, channel);

        assertEquals(41083686200.30776, errorCalculater.calculate(sensor1), 0.0);
        assertEquals(2892779.8215660397, errorCalculater.calculate(sensor2), 0.0);
        assertEquals(0.0001702775676534021, errorCalculater.calculate(sensor3), 0.0);
        assertEquals(-3349299.4387254664, errorCalculater.calculate(sensor4), 0.0);
        assertEquals(0.6356485641660826, errorCalculater.calculate(sensor5), 0.0);
    }
}