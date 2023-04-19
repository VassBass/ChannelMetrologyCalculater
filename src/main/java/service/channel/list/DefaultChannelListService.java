package service.channel.list;

import model.dto.Channel;
import model.dto.Sensor;
import repository.RepositoryFactory;
import repository.repos.channel.ChannelRepository;
import repository.repos.sensor.SensorRepository;
import util.DateHelper;

import javax.annotation.Nonnull;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.HashSet;

public class DefaultChannelListService implements ChannelListService {
    private final RepositoryFactory repositoryFactory;

    public DefaultChannelListService(RepositoryFactory repositoryFactory) {
        this.repositoryFactory = repositoryFactory;
    }

    @Override
    public Collection<String> getCodesOfExpiredChannels() {
        ChannelRepository channelRepository = repositoryFactory.getImplementation(ChannelRepository.class);
        Collection<String> result = new HashSet<>();

        for (Channel channel : channelRepository.getAll()) {
            Calendar checkDate = DateHelper.stringToDate(channel.getDate());
            if (checkDate == null) {
                result.add(channel.getCode());
            } else {
                long checkDateInMills = checkDate.getTimeInMillis();
                long nextDate = DateHelper.yearsToMills(channel.getFrequency()) + checkDateInMills;
                long toNextCheck = nextDate - Calendar.getInstance().getTimeInMillis();

                if (toNextCheck < 0) result.add(channel.getCode());
            }
        }
        return result;
    }

    @Override
    public Collection<String> getCodesOfChannelsCloseToExpired() {
        ChannelRepository channelRepository = repositoryFactory.getImplementation(ChannelRepository.class);
        Collection<String> result = new HashSet<>();

        for (Channel channel : channelRepository.getAll()) {
            Calendar checkDate = DateHelper.stringToDate(channel.getDate());
            if (checkDate != null) {
                long checkDateInMills = checkDate.getTimeInMillis();
                long nextDate = DateHelper.yearsToMills(channel.getFrequency()) + checkDateInMills;
                long toNextCheck = nextDate - Calendar.getInstance().getTimeInMillis();
                long days90 = 7_776_000_000L;

                if (toNextCheck > 0 && toNextCheck <= days90) result.add(channel.getCode());
            }
        }
        return result;
    }

    @Override
    public String getFullPath(@Nonnull Channel channel) {
        return String.format("%s/%s/%s/%s",
                channel.getDepartment(),
                channel.getArea(),
                channel.getProcess(),
                channel.getInstallation());
    }

    @Override
    public Calendar getDateOfNextCheck(@Nonnull Channel channel) {
        Calendar nextDate = null;
        Calendar checkDate = DateHelper.stringToDate(channel.getDate());
        if (channel.isSuitability() && checkDate != null) {
            nextDate = new GregorianCalendar();
            nextDate.setTimeInMillis(
                    checkDate.getTimeInMillis() + DateHelper.yearsToMills(channel.getFrequency()));

        }
        return nextDate;
    }

    @Override
    public Sensor getSensor(@Nonnull Channel channel) {
        SensorRepository sensorRepository = repositoryFactory.getImplementation(SensorRepository.class);
        return sensorRepository.get(channel.getCode());
    }
}
