package repository;

import model.Sensor;

import javax.annotation.Nonnull;
import java.util.Collection;

public interface SensorRepository extends Repository<Sensor>{
    Collection<Sensor> getAll(@Nonnull String measurement);
    String[]getAllTypes();
    String[]getAllTypesWithoutROSEMOUNT();
    String getMeasurement(@Nonnull String sensorType);
    String[] getAllSensorsName(@Nonnull String measurementName);
    Sensor get(@Nonnull String sensorName);

    boolean removeMeasurementValue(@Nonnull String measurementValue);

    boolean changeMeasurementValue(@Nonnull String oldValue, @Nonnull String newValue);

    boolean importData(@Nonnull Collection<Sensor>newSensors,
                       @Nonnull Collection<Sensor>sensorsForChange);

    boolean isLastInMeasurement(@Nonnull Sensor sensor);
    boolean isExists(@Nonnull String sensorName);
}
