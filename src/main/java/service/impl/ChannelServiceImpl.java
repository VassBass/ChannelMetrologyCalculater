package service.impl;

import application.Application;
import model.Channel;
import model.Sensor;
import repository.ChannelRepository;
import repository.impl.ChannelRepositoryImpl;
import service.ChannelService;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Logger;

public class ChannelServiceImpl implements ChannelService {
    private static final Logger LOGGER = Logger.getLogger(ChannelService.class.getName());

    private static final String ERROR = "Помилка";

    private final ChannelRepository repository;
    private ArrayList<Channel> channels;

    public ChannelServiceImpl(){
        this.repository = new ChannelRepositoryImpl();
    }

    public ChannelServiceImpl(String dbUrl){
        this.repository = new ChannelRepositoryImpl(dbUrl);
    }

    @Override
    public void init(){
        this.channels = this.repository.getAll();
        LOGGER.info("Initialization SUCCESS");
    }

    @Override
    public ArrayList<Channel> getAll() {
        return this.channels;
    }

    @Override
    public ArrayList<Channel> add(Channel channel) {
        if (channel != null) {
            if (this.channels.contains(channel)) {
                this.showExistMessage();
            } else {
                this.channels.add(channel);
                this.repository.add(channel);
            }
        }
        return this.channels;
    }

    @Override
    public ArrayList<Channel> remove(Channel channel) {
        if (channel != null) {
            int index = this.channels.indexOf(channel);
            if (index < 0) {
                this.showNotFoundMessage();
            } else {
                this.channels.remove(index);
                this.repository.remove(channel.getCode());
            }
        }
        return this.channels;
    }

    @Override
    public void removeBySensorInCurrentThread(Sensor sensor){
        ArrayList<Integer>indexes = new ArrayList<>();
        String sensorName = sensor.getName();
        for (int c=0;c<this.channels.size();c++){
            String channelSensor = this.channels.get(c).getSensor().getName();
            if (channelSensor.equals(sensorName)){
                indexes.add(c);
            }
        }
        Collections.reverse(indexes);
        for (int index : indexes){
            this.channels.remove(index);
        }
        this.repository.rewriteInCurrentThread(this.channels);
    }

    @Override
    public void changeSensorInCurrentThread(Sensor oldSensor, Sensor newSensor){
        for (Channel channel : this.channels){
            if (channel.getSensor().getName().equals(oldSensor.getName())){
                double minRange = channel.getSensor().getRangeMin();
                double maxRange = channel.getSensor().getRangeMax();
                String value = channel.getSensor().getValue();
                newSensor.setRange(minRange, maxRange);
                newSensor.setValue(value);
                channel.setSensor(newSensor);
            }
        }
        this.repository.rewriteInCurrentThread(this.channels);
    }

    @Override
    public void changeSensorsInCurrentThread(ArrayList<Sensor>sensors){
        for (Sensor sensor : sensors){
            for (Channel channel : this.channels){
                if (channel.getSensor().getName().equals(sensor.getName())){
                    channel.setSensor(sensor);
                }
            }
        }
        this.repository.rewriteInCurrentThread(this.channels);
    }

    @Override
    public ArrayList<Channel> set(Channel oldChannel, Channel newChannel) {
        if (oldChannel != null && newChannel != null){
            int index = this.channels.indexOf(oldChannel);
            if (index >= 0) {
                this.channels.set(index, newChannel);
                this.repository.set(oldChannel, newChannel);
            }
        }
        return this.channels;
    }

    @Override
    public boolean isExist(String code){
        for (Channel c : this.channels){
            if (c.getCode().equals(code)) return true;
        }
        return false;
    }

    @Override
    public boolean isExist(String oldChannelCode, String newChannelCode){
        if (oldChannelCode != null && newChannelCode != null) {
            Channel oldChannel = new Channel();
            oldChannel.setCode(oldChannelCode);

            int oldIndex = this.channels.indexOf(oldChannel);
            for (int index = 0; index < this.channels.size(); index++) {
                String channelCode = this.channels.get(index).getCode();
                if (channelCode.equals(newChannelCode) && index != oldIndex) return true;
            }
        }
        return false;
    }

    @Override
    public void clear() {
        this.channels.clear();
        this.repository.clear();
    }

    @Override
    public void exportData() {
        this.repository.export(this.channels);
    }

    @Override
    public void importData(ArrayList<Channel>newChannels, ArrayList<Channel>channelsForChange){
        for (Channel channel : channelsForChange){
            int index = this.channels.indexOf(channel);
            if (index >= 0) this.channels.set(index, channel);
        }
        this.channels.addAll(newChannels);
        this.repository.rewriteInCurrentThread(this.channels);
    }

    @Override
    public void rewriteInCurrentThread(ArrayList<Channel>channels){
        this.channels = channels;
        this.repository.rewriteInCurrentThread(channels);
    }

    private void showNotFoundMessage() {
        if (Application.context != null) {
            String message = "Канал з данним кодом не знайдено в списку каналів.";
            JOptionPane.showMessageDialog(Application.context.mainScreen, message, ERROR, JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showExistMessage() {
        if (Application.context != null) {
            String message = "Канал з данним кодом вже існує в списку каналів. Змініть будь ласка код каналу.";
            JOptionPane.showMessageDialog(Application.context.mainScreen, message, ERROR, JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void showExistMessage(Window window) {
        String message = "Канал з данним кодом вже існує в списку каналів. Змініть будь ласка код каналу.";
        JOptionPane.showMessageDialog(window, message, ERROR, JOptionPane.ERROR_MESSAGE);
    }
}