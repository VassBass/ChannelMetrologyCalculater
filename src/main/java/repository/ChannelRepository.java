package repository;

import model.Channel;
import model.Sensor;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ChannelRepository extends Repository<Channel>{
    Optional<Channel> get(@Nonnull String code);

    boolean removeBySensor(@Nonnull Sensor sensor);
    boolean removeByMeasurementValue(@Nonnull String measurementValue);

    boolean importData(@Nonnull Collection<Channel> newChannels,
                       @Nonnull Collection<Channel>channelsForChange);

    boolean changeSensor(@Nonnull Sensor oldSensor, @Nonnull Sensor newSensor, int ... ignored);
    boolean changeSensors(@Nonnull List<Sensor>sensors);
    boolean changeMeasurementValue(@Nonnull String oldValue, @Nonnull String newValue);

    boolean isExist(@Nonnull String code);
    boolean isExist(@Nonnull String oldChannelCode, @Nonnull String newChannelCode);
}
