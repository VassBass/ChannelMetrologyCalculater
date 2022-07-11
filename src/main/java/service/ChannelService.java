package service;

import model.Channel;
import model.Sensor;

import java.util.List;

public interface ChannelService {
    Channel get(String code);
    List<Channel> getAll();

    boolean add(Channel channel);

    boolean remove(Channel channel);
    boolean removeBySensor(Sensor sensor);
    boolean removeByMeasurementValue(String measurementValue);

    boolean changeSensor(Sensor oldSensor, Sensor newSensor, int ... ignored);
    boolean changeSensors(List<Sensor>sensors);

    boolean set(Channel oldChannel, Channel newChannel);
    boolean changeMeasurementValue(String oldValue, String newValue);

    boolean isExist(String code);
    boolean isExist(String oldChannelCode, String newChannelCode);

    boolean clear();

    boolean importData(List<Channel>newChannels, List<Channel>channelsForChange);
    boolean rewrite(List<Channel>channels);
}
