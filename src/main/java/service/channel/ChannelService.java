package service.channel;

import model.dto.Channel;

import java.util.Calendar;
import java.util.Collection;

public interface ChannelService {
    Collection<String> getCodesOfExpiredChannels();
    Collection<String> getCodesOfChannelsCloseToExpired();
    String getFullPath(Channel channel);
    Calendar getDateOfNextCheck(Channel channel);
}
