package repository;

import model.Channel;
import model.Sensor;

import java.util.List;

public interface ChannelRepository {
    Channel get(String code);
    List<Channel> getAll();

    boolean add(Channel channel);

    boolean remove(Channel channel);
    boolean removeBySensor(Sensor sensor);
    boolean removeByMeasurementValue(String measurementValue);

    boolean importData(List<Channel>newChannels, List<Channel>channelsForChange);
    boolean rewrite(List<Channel>channels);

    boolean changeSensor(Sensor oldSensor, Sensor newSensor, int ... ignored);
    boolean changeSensors(List<Sensor>sensors);
    boolean changeMeasurementValue(String oldValue, String newValue);

    boolean set(Channel oldChannel, Channel newChannel);

    boolean clear();

    boolean isExist(String code);
    boolean isExist(String oldChannelCode, String newChannelCode);
}
