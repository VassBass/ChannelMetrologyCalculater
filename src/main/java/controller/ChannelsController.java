package controller;

import application.Application;
import model.Channel;
import model.Model;
import model.Sensor;
import repository.Repository;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

public class ChannelsController {
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

    public void init(Window window){
        try {
            this.channels = new Repository<Channel>(null, Model.CHANNEL).readList();
        }catch (Exception e){
            System.out.println("File \"" + FileBrowser.FILE_CHANNELS.getName() + "\" is empty");
            this.channels = new ArrayList<>();
        }
        this.window = window;
    }

    public ArrayList<Channel> getAll() {
        return this.channels;
    }

    public ArrayList<Channel> add(Channel channel) {
        boolean exist = false;
        for (Channel cha : this.channels){
            if (cha.getCode().equals(channel.getCode())){
                exist = true;
                break;
            }
        }
        if (exist){
            this.showExistMessage();
        }else {
            this.channels.add(channel);
            this.save();
        }
        return this.channels;
    }

    public ArrayList<Channel> remove(Channel channel) {
        boolean removed = false;

        for (Channel cha : this.channels){
            if (cha.getCode().equals(channel.getCode())){
                this.channels.remove(cha);
                removed = true;
                break;
            }
        }

        if (removed){
            this.save();
        }else {
            this.showNotFoundMessage();
        }
        return this.channels;
    }

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

    public void changeSensor(Sensor oldSensor, Sensor newSensor){
        for (Channel channel : this.channels){
            if (channel.getSensor().getName().equals(oldSensor.getName())){
                channel.setSensor(newSensor);
            }
        }
    }

    public ArrayList<Channel> set(Channel oldChannel, Channel newChannel) {
        if (oldChannel != null){
            if (newChannel == null){
                this.remove(oldChannel);
            }else {
                for (int c=0;c<this.channels.size();c++){
                    String channelCode = this.channels.get(c).getCode();
                    if (channelCode.equals(oldChannel.getCode())){
                        this.channels.set(c, newChannel);
                        break;
                    }
                }
            }
            this.save();
        }
        return this.channels;
    }

    public int getIndex(String channelCode) {
        for (int index=0;index<this.channels.size();index++) {
            Channel channel = this.channels.get(index);
            if (channel.getCode().equals(channelCode)) {
                return index;
            }
        }
        this.showNotFoundMessage();
        return -1;
    }

    public Channel get(String channelCode) {
        for (Channel channel : this.channels) {
            if (channel.getCode().equals(channelCode)) {
                return channel;
            }
        }
        this.showNotFoundMessage();
        return null;
    }

    public Channel get(int index) {
        if (index >= 0) {
            return this.channels.get(index);
        }else {
            return null;
        }
    }

    public boolean isExist(String channelCode){
        for (Channel c : this.channels){
            if (c.getCode().equals(channelCode)){
                return true;
            }
        }
        return false;
    }

    public boolean isExist(String oldChannelCode, String newChannelCode){
        int oldIndex = this.getIndex(oldChannelCode);
        for (int index=0;index<this.channels.size();index++){
            String channelCode = this.channels.get(index).getCode();
            if (channelCode.equals(newChannelCode) && index != oldIndex){
                return true;
            }
        }
        return false;
    }

    public void clear() {
        this.channels.clear();
        this.save();
    }

    private void save() {
        new Repository<Channel>(this.window, Model.CHANNEL).writeList(this.channels);
    }

    public boolean exportData() {
        String fileName = this.exportFileName(Calendar.getInstance());
        ArrayList<Sensor>sensors = Application.context.sensorsController.getAll();
        ArrayList<?>[]list = new ArrayList<?>[]{this.channels, sensors};
        try {
            FileBrowser.saveToFile(FileBrowser.exportFile(fileName), list);
            return true;
        }catch (IOException e){
            e.printStackTrace();
            return false;
        }
    }

    public void rewriteAll(ArrayList<Channel>channels){
        this.channels = channels;
        this.save();
    }

    private void showNotFoundMessage() {
        String message = "Канал з данним кодом не знайдено в списку каналів.";
        JOptionPane.showMessageDialog(this.window, message, ERROR, JOptionPane.ERROR_MESSAGE);
    }

    private void showExistMessage() {
        String message = "Канал з данним кодом вже існує в списку каналів. Змініть будь ласка код каналу.";
        JOptionPane.showMessageDialog(this.window, message, ERROR, JOptionPane.ERROR_MESSAGE);
    }

    public void showExistMessage(Window window) {
        String message = "Канал з данним кодом вже існує в списку каналів. Змініть будь ласка код каналу.";
        JOptionPane.showMessageDialog(window, message, ERROR, JOptionPane.ERROR_MESSAGE);
    }
}
