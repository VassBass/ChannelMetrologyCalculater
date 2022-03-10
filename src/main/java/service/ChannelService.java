package service;

import model.Channel;
import model.Sensor;

import java.awt.*;
import java.util.ArrayList;

public interface ChannelService {
    void init();
    ArrayList<Channel> getAll();
    ArrayList<Channel> add(Channel channel);
    ArrayList<Channel> remove(Channel channel);
    void removeBySensorInCurrentThread(Sensor sensor);
    void changeSensorInCurrentThread(Sensor oldSensor, Sensor newSensor);
    void changeSensorsInCurrentThread(ArrayList<Sensor>sensors);
    ArrayList<Channel> set(Channel oldChannel, Channel newChannel);
    boolean isExist(String code);
    boolean isExist(String oldChannelCode, String newChannelCode);
    void clear();
    void exportData();
    void importData(ArrayList<Channel>newChannels, ArrayList<Channel>channelsForChange);
    void rewriteInCurrentThread(ArrayList<Channel>channels);
    void showExistMessage(Window window);
}
