package service.channel.info;

import model.dto.Channel;
import service.channel.info.ui.*;

public interface ChannelInfoManager {
    void searchChannelByCode();
    void setChannelInfo(Channel channel);
    void changeMeasurementName();
    void changeMeasurementValue();
    void changeDateOrFrequency();
    void setChannelAndSensorRangesEqual();
    void changedChannelRange();
    void changedAllowableErrorPercent();
    void changedAllowableErrorValue();
    void saveChannel();
    void saveAndCalculateChannel();
    void resetChannelInfo();
    void deleteChannel();
    void setExpectedSensorInfo(Integer ... options);
}
