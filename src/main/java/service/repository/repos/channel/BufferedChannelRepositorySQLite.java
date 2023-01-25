package service.repository.repos.channel;

import model.Channel;
import service.repository.config.RepositoryConfigHolder;
import service.repository.connection.RepositoryDBConnector;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public class BufferedChannelRepositorySQLite extends ChannelRepositorySQLite {
    private final Map<String, Channel> buffer;

    public BufferedChannelRepositorySQLite(RepositoryConfigHolder configHolder, RepositoryDBConnector connector) {
        super(configHolder, connector);

        buffer = super.getAll().stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(Channel::getCode, Function.identity()));
    }

    @Override
    public Channel get(@Nonnull String code) {
        return buffer.get(code);
    }

    @Override
    public Collection<Channel> getAll() {
        return buffer.values();
    }

    @Override
    public boolean add(@Nonnull Channel channel) {
        if (buffer.containsKey(channel.getCode())) return false;

        buffer.put(channel.getCode(), channel);
        return super.add(channel);
    }

    @Override
    public boolean remove(@Nonnull Channel channel) {
        return buffer.remove(channel.getCode()) != null && super.remove(channel);
    }

    @Override
    public boolean removeBySensorName(@Nonnull String sensorName) {
        Collection<Channel> result = buffer.values().stream()
                .filter(c -> !c.getSensorName().equals(sensorName))
                .collect(Collectors.toSet());
        buffer.clear();

        buffer.putAll(result.stream().collect(Collectors.toMap(Channel::getCode, Function.identity())));
        return super.removeBySensorName(sensorName);
    }

    @Override
    public boolean removeByMeasurementValue(@Nonnull String measurementValue) {
        Collection<Channel> result = buffer.values().stream()
                .filter(c -> !c.getMeasurementValue().equals(measurementValue))
                .collect(Collectors.toSet());
        buffer.clear();

        buffer.putAll(result.stream().collect(Collectors.toMap(Channel::getCode, Function.identity())));
        return super.removeByMeasurementValue(measurementValue);
    }

    @Override
    public boolean rewrite(@Nonnull Collection<Channel> channels) {
        buffer.clear();
        buffer.putAll(channels.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(Channel::getCode, Function.identity())));
        return super.rewrite(channels);
    }

    @Override
    public boolean changeSensorName(@Nonnull String oldSensor, @Nonnull String newSensor) {
        buffer.values().forEach(c -> {
                    if (c.getSensorName().equals(oldSensor)) c.setSensorName(newSensor);
                });
        return super.changeSensorName(oldSensor, newSensor);
    }

    @Override
    public boolean changeMeasurementValue(@Nonnull String oldValue, @Nonnull String newValue) {
        buffer.values().forEach(c -> {
            if (c.getMeasurementValue().equals(oldValue)) c.setMeasurementValue(newValue);
        });
        return super.changeMeasurementValue(oldValue, newValue);
    }

    @Override
    public boolean set(@Nonnull Channel oldChannel, @Nonnull Channel newChannel) {
        if (!oldChannel.equals(newChannel)) {
            if (buffer.containsKey(newChannel.getCode())) return false;

            buffer.remove(oldChannel.getCode());
        }

        buffer.put(newChannel.getCode(), newChannel);
        return super.set(oldChannel, newChannel);
    }

    @Override
    public boolean clear() {
        buffer.clear();
        return super.clear();
    }

    @Override
    public boolean importData(@Nonnull Collection<Channel> newChannels, @Nonnull Collection<Channel> channelsForChange) {
        buffer.putAll(channelsForChange.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(Channel::getCode, Function.identity())));
        buffer.putAll(newChannels.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(Channel::getCode, Function.identity())));
        return super.importData(newChannels, channelsForChange);
    }

    @Override
    public boolean isExist(@Nonnull String code) {
        return buffer.containsKey(code);
    }

    @Override
    public boolean isExist(@Nonnull String oldChannelCode, @Nonnull String newChannelCode) {
        if (oldChannelCode.equals(newChannelCode)) return false;
        return buffer.containsKey(newChannelCode);
    }
}
