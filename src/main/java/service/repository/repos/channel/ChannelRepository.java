package service.repository.repos.channel;

import model.Channel;
import model.Sensor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;

public interface ChannelRepository {
    Collection<Channel> getAll();
    @Nullable Channel get(@Nonnull String code);

    boolean add(@Nonnull Channel channel);

    boolean remove(@Nonnull Channel channel);
    boolean removeBySensor(@Nonnull Sensor sensor);
    boolean removeByMeasurementValue(@Nonnull String measurementValue);
    boolean clear();

    boolean set(@Nonnull Channel oldChannel, @Nonnull Channel newChannel);
    boolean changeSensor(@Nonnull Sensor oldSensor, @Nonnull Sensor newSensor);
    boolean changeSensors(@Nonnull List<Sensor>sensors);
    boolean changeMeasurementValue(@Nonnull String oldValue, @Nonnull String newValue);
    boolean rewrite(@Nonnull Collection<Channel> channels);
    boolean importData(@Nonnull Collection<Channel> newChannels, @Nonnull Collection<Channel>channelsForChange);

    boolean isExist(@Nonnull String code);
    boolean isExist(@Nonnull String oldChannelCode, @Nonnull String newChannelCode);
}
