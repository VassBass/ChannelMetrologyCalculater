package service;

import model.Channel;
import model.Sensor;

import java.awt.*;
import java.util.ArrayList;

public interface ChannelService {
    void init();

    Channel get(String code);
    ArrayList<Channel> getAll();

    ArrayList<Channel> add(Channel channel);
    void addInCurrentThread(Channel channel);

    ArrayList<Channel> remove(Channel channel);
    void removeBySensorInCurrentThread(Sensor sensor);
    void removeByMeasurementValueInCurrentThread(String measurementValue);

    void changeSensorInCurrentThread(Sensor oldSensor, Sensor newSensor, int ... ignored);
    void changeSensorsInCurrentThread(ArrayList<Sensor>sensors);

    ArrayList<Channel> set(Channel oldChannel, Channel newChannel);
    void setInCurrentThread(Channel oldChannel, Channel newChannel);
    void changeMeasurementValueInCurrentThread(String oldValue, String newValue);

    boolean isExist(String code);
    boolean isExist(String oldChannelCode, String newChannelCode);

    void clear();

    void importDataInCurrentThread(ArrayList<Channel>newChannels, ArrayList<Channel>channelsForChange);
    void rewriteInCurrentThread(ArrayList<Channel>channels);

    void showExistMessage(Window window);

    boolean backgroundTaskIsRun();
}
