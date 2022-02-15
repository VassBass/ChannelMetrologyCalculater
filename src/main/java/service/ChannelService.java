package service;

import model.Channel;
import model.Sensor;

import java.awt.*;
import java.util.ArrayList;

public interface ChannelService {
    void init(Window window);
    ArrayList<Channel> getAll();
    ArrayList<Channel> add(Channel channel);
    ArrayList<Channel> remove(Channel channel);
    void removeBySensor(Sensor sensor);
    void changeSensor(Sensor oldSensor, Sensor newSensor);
    void changeSensors(ArrayList<Sensor>sensors);
    ArrayList<Channel> set(Channel oldChannel, Channel newChannel);
    int getIndex(String code);
    Channel get(String code);
    Channel get(int index);
    boolean isExist(String code);
    boolean isExist(String oldChannelCode, String newChannelCode);
    void clear();
    void save();
    boolean exportData();
    void importData(ArrayList<Channel>newChannels, ArrayList<Channel>channelsForChange);
    void rewriteInCurrentThread(ArrayList<Channel>channels);
    void showExistMessage(Window window);
}
