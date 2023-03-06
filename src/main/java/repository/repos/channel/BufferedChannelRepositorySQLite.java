package repository.repos.channel;

import model.dto.Channel;
import repository.config.RepositoryConfigHolder;
import repository.connection.RepositoryDBConnector;

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
    public boolean removeByMeasurementValue(@Nonnull String measurementValue) {
        Collection<Channel> result = buffer.values();
        result.removeIf(c -> c.getMeasurementValue().equals(measurementValue));

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
    public boolean changeMeasurementValue(@Nonnull String oldValue, @Nonnull String newValue) {
        buffer.values().forEach(c -> {
            if (c.getMeasurementValue().equals(oldValue)) c.setMeasurementValue(newValue);
        });
        return super.changeMeasurementValue(oldValue, newValue);
    }

    @Override
    public boolean set(@Nonnull Channel oldChannel, @Nonnull Channel newChannel) {
        if (!buffer.containsKey(oldChannel.getCode())) return false;

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