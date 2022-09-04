package repository;

import model.Sensor;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface SensorRepository extends Repository<Sensor>{
    Collection<Sensor> getAll(@Nonnull String measurement);
    Collection<String> getAllTypes();
    Optional<String> getMeasurement(@Nonnull String sensorType);
    Collection<String> getAllSensorsName(@Nonnull String measurementName);
    Optional<Sensor> get(@Nonnull String sensorName);

    boolean removeMeasurementValue(@Nonnull String measurementValue);

    boolean changeMeasurementValue(@Nonnull String oldValue, @Nonnull String newValue);

    boolean importData(@Nonnull Collection<Sensor>newSensors,
                       @Nonnull Collection<Sensor>sensorsForChange);

    boolean isLastInMeasurement(@Nonnull Sensor sensor);
    boolean isExists(@Nonnull String sensorName);
}
