package service.channel.list;

import model.dto.Channel;
import model.dto.Sensor;
import repository.repos.channel.ChannelRepository;
import repository.repos.sensor.SensorRepository;
import util.DateHelper;

import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.HashSet;

public class DefaultChannelListService implements ChannelListService {
    private final ChannelRepository channelRepository;
    private final SensorRepository sensorRepository;

    public DefaultChannelListService(ChannelRepository channelRepository,
                                     SensorRepository sensorRepository ) {
        this.channelRepository = channelRepository;
        this.sensorRepository = sensorRepository;
    }

    @Override
    public Collection<String> getCodesOfExpiredChannels() {
        Collection<String> result = new HashSet<>();

        for (Channel channel : channelRepository.getAll()) {
            Calendar checkDate = DateHelper.stringToDate(channel.getDate());
            if (checkDate == null) {
                result.add(channel.getCode());
            }else {
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
        Collection<String> result = new HashSet<>();

        for (Channel channel : channelRepository.getAll()) {
            Calendar checkDate = DateHelper.stringToDate(channel.getDate());
            if (checkDate != null) {
                long checkDateInMills = checkDate.getTimeInMillis();
                long nextDate = DateHelper.yearsToMills(channel.getFrequency()) + checkDateInMills;
                long toNextCheck = nextDate - Calendar.getInstance().getTimeInMillis();
                long days90 = 7776000000L;

                if (toNextCheck <= days90) result.add(channel.getCode());
            }
        }

        return result;
    }

    @Override
    public String getFullPath(Channel channel) {
        return String.format("%s/%s/%s/%s",
                channel.getDepartment(),
                channel.getArea(),
                channel.getProcess(),
                channel.getInstallation());
    }

    @Override
    public Calendar getDateOfNextCheck(Channel channel) {
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
    public Sensor getSensor(Channel channel) {
        return sensorRepository.get(channel.getCode());
    }
}
