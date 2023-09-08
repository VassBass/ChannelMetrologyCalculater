package service.export_import;

import model.dto.*;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class ExportDataValidatorTest {

    @Test
    public void testIsValidEmptyLists() {
        Map<Entity, Collection<?>> testData = new HashMap<>();

        testData.put(Entity.CALIBRATOR, Collections.emptyList());
        testData.put(Entity.CHANNEL, Collections.emptyList());
        testData.put(Entity.CONTROL_POINTS, Collections.emptyList());
        testData.put(Entity.MEASUREMENT, Collections.emptyList());
        testData.put(Entity.MEASUREMENT_TRANSFORM_FACTOR, Collections.emptyList());
        testData.put(Entity.PERSON, Collections.emptyList());
        testData.put(Entity.SENSOR, Collections.emptyList());
        testData.put(Entity.SENSOR_ERROR, Collections.emptyList());

        assertTrue(ExportDataValidator.isValid(testData));
    }

    @Test
    public void testIsValid_validCollections() {
        Map<Entity, Collection<?>> testData = new HashMap<>();

        Collection<?> calibrators = Arrays.asList(
                new Calibrator(), new Calibrator()
        );
        testData.put(Entity.CALIBRATOR, calibrators);

        Collection<?> channels = Arrays.asList(
                new Channel(), new Channel()
        );
        testData.put(Entity.CHANNEL, channels);

        Collection<?> controlPoints = Arrays.asList(
                new ControlPoints(), new ControlPoints()
        );
        testData.put(Entity.CONTROL_POINTS, controlPoints);

        Collection<?> measurements = Arrays.asList(
                new Measurement(), new Measurement()
        );
        testData.put(Entity.MEASUREMENT, measurements);

        Collection<?> measurementTransformFactors = Arrays.asList(
                new MeasurementTransformFactor(0, null, null, 0),
                new MeasurementTransformFactor(0, null, null, 0)
        );
        testData.put(Entity.MEASUREMENT_TRANSFORM_FACTOR, measurementTransformFactors);

        Collection<?> people = Arrays.asList(
                new Person(), new Person()
        );
        testData.put(Entity.PERSON, people);

        Collection<?> sensors = Arrays.asList(
                new Sensor(), new Sensor()
        );
        testData.put(Entity.SENSOR, sensors);

        Collection<?> sensorErrors = Arrays.asList(
                SensorError.create(null, 0,0,null, null),
                SensorError.create(null, 0,0,null, null)
        );
        testData.put(Entity.SENSOR_ERROR, sensorErrors);

        assertTrue(ExportDataValidator.isValid(testData));
    }

    @Test
    public void testIsValid_notValidCollections() {
        Map<Entity, Collection<?>> testData = new HashMap<>();

        Collection<?> calibrators = Arrays.asList(
                new Calibrator(), "Not Calibrator"
        );
        testData.put(Entity.CALIBRATOR, calibrators);

        Collection<?> channels = Arrays.asList(
                new Channel(), new Range(0,0,null)
        );
        testData.put(Entity.CHANNEL, channels);

        Collection<?> controlPoints = Arrays.asList(
                new ControlPoints(), new Channel()
        );
        testData.put(Entity.CONTROL_POINTS, controlPoints);

        Collection<?> measurements = Arrays.asList(
                new Measurement(), new Person()
        );
        testData.put(Entity.MEASUREMENT, measurements);

        Collection<?> measurementTransformFactors = Arrays.asList(
                new MeasurementTransformFactor(0, null, null, 0),
                new Measurement()
        );
        testData.put(Entity.MEASUREMENT_TRANSFORM_FACTOR, measurementTransformFactors);

        Collection<?> people = Arrays.asList(
                new Person(), new Calibrator()
        );
        testData.put(Entity.PERSON, people);

        Collection<?> sensors = Arrays.asList(
                new Sensor(), new Person()
        );
        testData.put(Entity.SENSOR, sensors);

        Collection<?> sensorErrors = Arrays.asList(
                SensorError.create(null, 0,0,null, null),
                new Sensor()
        );
        testData.put(Entity.SENSOR_ERROR, sensorErrors);

        assertFalse(ExportDataValidator.isValid(testData));
    }
}