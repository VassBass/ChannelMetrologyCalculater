package service.repository.repos.sensor;

import model.Sensor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;

public interface SensorRepository {
    Collection<Sensor> getAll();
    Collection<Sensor> getAll(@Nonnull String measurement);
    Collection<String> getAllTypes();
    String getMeasurement(@Nonnull String sensorType);
    Collection<String> getAllSensorsName(@Nonnull String measurementName);
    @Nullable Sensor get(@Nonnull String sensorName);

    boolean add(@Nonnull Sensor sensor);

    boolean remove(@Nonnull Sensor sensor);
    boolean removeMeasurementValue(@Nonnull String measurementValue);
    boolean clear();

    boolean set(@Nonnull Sensor oldSensor, @Nonnull Sensor newSensor);
    boolean changeMeasurementValue(@Nonnull String oldValue, @Nonnull String newValue);
    boolean rewrite(@Nonnull Collection<Sensor> sensors);

    boolean importData(@Nonnull Collection<Sensor>newSensors,
                       @Nonnull Collection<Sensor>sensorsForChange);

    boolean isLastInMeasurement(@Nonnull Sensor sensor);
    boolean isExists(@Nonnull String sensorName);
}
