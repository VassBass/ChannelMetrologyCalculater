package service.channel.info;

import model.dto.Channel;

public interface ChannelInfoManager {
    void setChannelInfo(Channel channel);
    void changeMeasurementName();
    void changeMeasurementValue();
    void changeDateOrFrequency();
    void setChannelAndSensorRangesEqual();
}
