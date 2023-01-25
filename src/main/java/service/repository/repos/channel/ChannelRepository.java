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
    boolean removeBySensorName(@Nonnull String sensorName);
    boolean removeByMeasurementValue(@Nonnull String measurementValue);
    boolean clear();

    boolean set(@Nonnull Channel oldChannel, @Nonnull Channel newChannel);
    boolean changeSensorName(@Nonnull String oldSensor, @Nonnull String newSensor);
    boolean changeMeasurementValue(@Nonnull String oldValue, @Nonnull String newValue);
    boolean rewrite(@Nonnull Collection<Channel> channels);
    boolean importData(@Nonnull Collection<Channel> newChannels, @Nonnull Collection<Channel>channelsForChange);

    boolean isExist(@Nonnull String code);
    boolean isExist(@Nonnull String oldChannelCode, @Nonnull String newChannelCode);
}
