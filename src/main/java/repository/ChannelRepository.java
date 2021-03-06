package repository;

import model.Channel;
import model.Sensor;

import java.util.ArrayList;

public interface ChannelRepository {
    Channel get(String code);
    ArrayList<Channel> getAll();

    void add(Channel channel);
    void addInCurrentThread(Channel channel);

    void remove(Channel channel);
    void removeBySensorInCurrentThread(Sensor sensor);
    void removeByMeasurementValueInCurrentThread(String measurementValue);

    void importDataInCurrentThread(ArrayList<Channel>newChannels, ArrayList<Channel>channelsForChange);
    void rewriteInCurrentThread(ArrayList<Channel>channels);

    void changeSensorInCurrentThread(Sensor oldSensor, Sensor newSensor, int ... ignored);
    void changeSensorsInCurrentThread(ArrayList<Sensor>sensors);
    void changeMeasurementValueInCurrentThread(String oldValue, String newValue);

    void set(Channel oldChannel, Channel newChannel);
    void setInCurrentThread(Channel oldChannel, Channel newChannel);

    void clear();

    boolean isExist(String code);
    boolean isExist(String oldChannelCode, String newChannelCode);

    boolean backgroundTaskIsRun();
}
