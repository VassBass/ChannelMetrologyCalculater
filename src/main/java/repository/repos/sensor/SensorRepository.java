package repository.repos.sensor;

import model.dto.Sensor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;

public interface SensorRepository {
    Collection<Sensor> getAll();
    Collection<Sensor> getAllByMeasurementName(@Nonnull String measurement);
    Collection<String> getAllTypes();
    Collection<String> getAllSensorsTypesByMeasurementName(@Nonnull String measurementName);
    @Nullable Sensor get(@Nonnull String channelCode);
    String getErrorFormula(@Nonnull String sensorType);

    boolean add(@Nonnull Sensor sensor);

    boolean remove(@Nonnull Sensor sensor);
    boolean removeByChannelCode(@Nonnull String channelCode);
    boolean clear();

    boolean set(@Nonnull Sensor oldSensor, @Nonnull Sensor newSensor);
    boolean setErrorFormula(@Nonnull String sensorType, @Nonnull String errorFormula);
    boolean changeMeasurementValue(@Nonnull String oldValue, @Nonnull String newValue);
    boolean rewrite(@Nonnull Collection<Sensor> sensors);

    boolean importData(@Nonnull Collection<Sensor>newSensors,
                       @Nonnull Collection<Sensor>sensorsForChange);

    boolean isExists(@Nonnull String channelCode);
}