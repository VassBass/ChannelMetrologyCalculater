package service.export_import;

import model.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public class ImportRequest {
    private static final Logger logger = LoggerFactory.getLogger(ImportRequest.class);

    private final Collection<Calibrator> calibrators;
    private final Collection<Channel> channels;
    private final Collection<ControlPoints> controlPoints;
    private final Collection<Measurement> measurements;
    private final Collection<MeasurementTransformFactor> measurementTransformFactors;
    private final Collection<Person> people;
    private final Collection<Sensor> sensors;
    private final Collection<SensorError> sensorErrors;

    @SuppressWarnings("unchecked")
    private ImportRequest(@Nonnull Map<Entity, Collection<?>> exportDate){
        calibrators = (Collection<Calibrator>) exportDate.getOrDefault(Entity.CALIBRATOR, Collections.emptyList());
        channels = (Collection<Channel>) exportDate.getOrDefault(Entity.CHANNEL, Collections.emptyList());
        controlPoints = (Collection<ControlPoints>) exportDate.getOrDefault(Entity.CONTROL_POINTS, Collections.emptyList());
        measurements = (Collection<Measurement>) exportDate.getOrDefault(Entity.MEASUREMENT, Collections.emptyList());
        measurementTransformFactors = (Collection<MeasurementTransformFactor>) exportDate.getOrDefault(Entity.MEASUREMENT_TRANSFORM_FACTOR, Collections.emptyList());
        people = (Collection<Person>) exportDate.getOrDefault(Entity.PERSON, Collections.emptyList());
        sensors = (Collection<Sensor>) exportDate.getOrDefault(Entity.SENSOR, Collections.emptyList());
        sensorErrors = (Collection<SensorError>) exportDate.getOrDefault(Entity.SENSOR_ERROR, Collections.emptyList());
    }

    /**
     * If export data is valid returns ImportRequest or null if not valid
     * @param exportDate export data
     * @return ImportRequest or null if data is not valid
     */
    public static ImportRequest create(@Nonnull Map<Entity, Collection<?>> exportDate) {
        if (ExportDataValidator.isValid(exportDate)) {
            return new ImportRequest(exportDate);
        } else return null;
    }

    public Collection<Calibrator> getCalibrators() {
        return calibrators;
    }

    public Collection<Channel> getChannels() {
        return channels;
    }

    public Collection<ControlPoints> getControlPoints() {
        return controlPoints;
    }

    public Collection<Measurement> getMeasurements() {
        return measurements;
    }

    public Collection<MeasurementTransformFactor> getMeasurementTransformFactors() {
        return measurementTransformFactors;
    }

    public Collection<Person> getPeople() {
        return people;
    }

    public Collection<Sensor> getSensors() {
        return sensors;
    }

    public Collection<SensorError> getSensorErrors() {
        return sensorErrors;
    }
}
