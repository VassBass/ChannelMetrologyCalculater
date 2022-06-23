package repository;

import model.Channel;
import model.Sensor;

import java.util.ArrayList;

public interface ChannelRepository {
    void createTable();

    Channel get(String code);
    ArrayList<Channel> getAll();

    boolean add(Channel channel);

    boolean remove(Channel channel);
    boolean removeBySensor(Sensor sensor);
    boolean removeByMeasurementValue(String measurementValue);

    boolean importData(ArrayList<Channel>newChannels, ArrayList<Channel>channelsForChange);
    boolean rewrite(ArrayList<Channel>channels);

    boolean changeSensor(Sensor oldSensor, Sensor newSensor, int ... ignored);
    boolean changeSensors(ArrayList<Sensor>sensors);
    boolean changeMeasurementValue(String oldValue, String newValue);

    boolean set(Channel oldChannel, Channel newChannel);

    boolean clear();

    boolean isExist(String code);
    boolean isExist(String oldChannelCode, String newChannelCode);
}
