package service.impl;

import application.Application;
import service.FileBrowser;
import model.Channel;
import model.Model;
import model.Sensor;
import repository.Repository;
import service.ChannelService;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

public class ChannelServiceImpl implements ChannelService {
    private static final String ERROR = "Помилка";

    private Window window;
    private ArrayList<Channel> channels;

    private String exportFileName(Calendar date){
        return "export_channels ["
                + date.get(Calendar.DAY_OF_MONTH)
                + "."
                + (date.get(Calendar.MONTH) + 1)
                + "."
                + date.get(Calendar.YEAR)
                + "].chn";
    }

    @Override
    public void init(Window window){
        try {
            this.channels = new Repository<Channel>(null, Model.CHANNEL).readList();
        }catch (Exception e){
            System.out.println("File \"" + FileBrowser.FILE_CHANNELS.getName() + "\" is empty");
            this.channels = new ArrayList<>();
        }
        this.window = window;
    }

    @Override
    public ArrayList<Channel> getAll() {
        return this.channels;
    }

    @Override
    public ArrayList<Channel> add(Channel channel) {
        int index = this.channels.indexOf(channel);
        if (index < 0){
            this.channels.add(channel);
            this.save();
        }else {
            this.showExistMessage();
        }
        return this.channels;
    }

    @Override
    public ArrayList<Channel> remove(Channel channel) {
        int index = this.channels.indexOf(channel);
        if (index < 0){
            this.showNotFoundMessage();
        }else {
            this.channels.remove(index);
            this.save();
        }
        return this.channels;
    }

    @Override
    public void removeBySensor(Sensor sensor){
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
    }

    @Override
    public void changeSensor(Sensor oldSensor, Sensor newSensor){
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
        new Repository<Channel>(null,Model.CHANNEL).writeListInCurrentThread(this.channels);
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
        new Repository<Channel>(null, Model.CHANNEL).writeListInCurrentThread(this.channels);
    }

    @Override
    public ArrayList<Channel> set(Channel oldChannel, Channel newChannel) {
        if (oldChannel != null){
            int index = this.channels.indexOf(oldChannel);
            if (index >= 0) {
                if (newChannel == null) {
                    this.channels.remove(index);
                } else {
                    this.channels.set(index, newChannel);
                }
                this.save();
            }
        }
        return this.channels;
    }

    @Override
    public boolean isExist(String code){
        for (Channel c : this.channels){
            if (c.getCode().equals(code)){
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isExist(String oldChannelCode, String newChannelCode){
        Channel oldChannel = new Channel();
        oldChannel.setCode(oldChannelCode);

        int oldIndex = this.channels.indexOf(oldChannel);
        for (int index=0;index<this.channels.size();index++){
            String channelCode = this.channels.get(index).getCode();
            if (channelCode.equals(newChannelCode) && index != oldIndex){
                return true;
            }
        }
        return false;
    }

    @Override
    public void clear() {
        this.channels.clear();
        this.save();
    }

    @Override
    public void save() {
        new Repository<Channel>(this.window, Model.CHANNEL).writeList(this.channels);
    }

    @Override
    public boolean exportData() {
        String fileName = this.exportFileName(Calendar.getInstance());
        ArrayList<Sensor>sensors = Application.context.sensorService.getAll();
        ArrayList<?>[]list = new ArrayList<?>[]{this.channels, sensors};
        try {
            FileBrowser.saveToFile(FileBrowser.exportFile(fileName), list);
            return true;
        }catch (IOException e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void importData(ArrayList<Channel>newChannels, ArrayList<Channel>channelsForChange){
        for (Channel channel : channelsForChange){
            int index = this.channels.indexOf(channel);
            if (index >= 0) this.channels.set(index, channel);
        }
        this.channels.addAll(newChannels);
        new Repository<Channel>(null,Model.CHANNEL).writeListInCurrentThread(this.channels);
    }

    @Override
    public void rewriteInCurrentThread(ArrayList<Channel>channels){
        this.channels = channels;
        new Repository<Channel>(null,Model.CHANNEL).writeListInCurrentThread(channels);
    }

    private void showNotFoundMessage() {
        String message = "Канал з данним кодом не знайдено в списку каналів.";
        JOptionPane.showMessageDialog(this.window, message, ERROR, JOptionPane.ERROR_MESSAGE);
    }

    private void showExistMessage() {
        String message = "Канал з данним кодом вже існує в списку каналів. Змініть будь ласка код каналу.";
        JOptionPane.showMessageDialog(this.window, message, ERROR, JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public void showExistMessage(Window window) {
        String message = "Канал з данним кодом вже існує в списку каналів. Змініть будь ласка код каналу.";
        JOptionPane.showMessageDialog(window, message, ERROR, JOptionPane.ERROR_MESSAGE);
    }
}