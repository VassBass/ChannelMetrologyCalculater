package service;

import model.Channel;
import model.Sensor;

import java.util.Collection;
import java.util.List;

public interface ChannelService extends Service<Channel> {
    Channel get(String code);

    boolean removeBySensor(Sensor sensor);
    boolean removeByMeasurementValue(String measurementValue);

    boolean changeSensor(Sensor oldSensor, Sensor newSensor, int ... ignored);
    boolean changeSensors(List<Sensor>sensors);

    boolean changeMeasurementValue(String oldValue, String newValue);

    boolean isExist(String code);
    boolean isExist(String oldChannelCode, String newChannelCode);


    boolean importData(Collection<Channel> newChannels, Collection<Channel>channelsForChange);
}
