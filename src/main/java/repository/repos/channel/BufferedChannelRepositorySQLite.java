package repository.repos.channel;

import model.dto.Channel;
import repository.config.RepositoryConfigHolder;
import repository.connection.RepositoryDBConnector;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static util.StringHelper.containsIgnoreCaseAndSpaces;

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

    @Override
    public Collection<Channel> search(SearchParams params) {
        return getAll().stream()
                .filter(c -> {
                    boolean match = false;

                    if (params.month > 0 || params.year > 0) match = isDateMatch(c, params.month, params.year);
                    if (params.locationZone > 0) match = isLocationMatch(c, params.locationZone, params.locationValue);

                    return match;
                })
                .collect(Collectors.toSet());
    }

    private static final String MONTH_REGEX = "(?<=\\.)\\d{2}(?=\\.)";
    private static final String YEAR_REGEX = "(?<=\\d{2}\\.)\\d{4}";
    private boolean isDateMatch(Channel channel, int month, int year) {
        boolean match = false;
        Matcher matcher;
        String date = channel.getDate();

        if (month > 0) {
            matcher = Pattern.compile(MONTH_REGEX).matcher(date);
            int m = matcher.find() ? Integer.parseInt(matcher.group()) : 0;
            match = m == month;
        }
        if (year > 0) {
            matcher = Pattern.compile(YEAR_REGEX).matcher(date);
            int y = matcher.find() ? Integer.parseInt(matcher.group()) : 0;
            match = y == year;
        }

        return match;
    }

    private boolean isLocationMatch(Channel channel, int zone, String val) {
        switch (zone) {
            case SearchParams.LOCATION_ZONE_ALL:
                return containsIgnoreCaseAndSpaces(channel.createFullPath(), val);
            case SearchParams.LOCATION_ZONE_DEPARTMENT:
                return containsIgnoreCaseAndSpaces(channel.getDepartment(), val);
            case SearchParams.LOCATION_ZONE_AREA:
                return containsIgnoreCaseAndSpaces(channel.getArea(), val);
            case SearchParams.LOCATION_ZONE_PROCESS:
                return containsIgnoreCaseAndSpaces(channel.getProcess(), val);
            case SearchParams.LOCATION_ZONE_INSTALLATION:
                return containsIgnoreCaseAndSpaces(channel.getInstallation(), val);
        }
        return false;
    }
}
