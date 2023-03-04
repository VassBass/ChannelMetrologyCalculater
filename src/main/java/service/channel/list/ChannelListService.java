package service.channel.list;

import model.dto.Channel;
import model.dto.Sensor;

import java.util.Calendar;
import java.util.Collection;

public interface ChannelListService {
    Collection<String> getCodesOfExpiredChannels();
    Collection<String> getCodesOfChannelsCloseToExpired();
    String getFullPath(Channel channel);
    Calendar getDateOfNextCheck(Channel channel);
    Sensor getSensor(Channel channel);
}
