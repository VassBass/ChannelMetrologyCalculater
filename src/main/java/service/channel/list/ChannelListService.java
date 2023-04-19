package service.channel.list;

import model.dto.Channel;
import model.dto.Sensor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Calendar;
import java.util.Collection;

public interface ChannelListService {
    Collection<String> getCodesOfExpiredChannels();
    Collection<String> getCodesOfChannelsCloseToExpired();
    String getFullPath(@Nonnull Channel channel);
    @Nullable Calendar getDateOfNextCheck(@Nonnull Channel channel);
    @Nullable Sensor getSensor(@Nonnull Channel channel);
}
