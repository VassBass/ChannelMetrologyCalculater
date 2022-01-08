package controller;

import constants.Strings;
import model.Channel;
import model.Model;
import repository.Repository;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ChannelsController {
    private Window window;
    private ArrayList<Channel> channels;

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

    public boolean isExist(Channel channel){
        for (Channel c : this.channels){
            if (c.getCode().equals(channel.getCode())){
                return true;
            }
        }
        return false;
    }

    public boolean isExist(String channelCode){
        for (Channel c : this.channels){
            if (c.getCode().equals(channelCode)){
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

    private void showNotFoundMessage() {
        String message = "Канал з данним кодом не знайдено в списку каналів.";
        JOptionPane.showMessageDialog(this.window, message, Strings.ERROR, JOptionPane.ERROR_MESSAGE);
    }

    private void showExistMessage() {
        String message = "Канал з данним кодом вже існує в списку каналів. Змініть будь ласка код каналу.";
        JOptionPane.showMessageDialog(this.window, message, Strings.ERROR, JOptionPane.ERROR_MESSAGE);
    }

    public void showExistMessage(Window window) {
        String message = "Канал з данним кодом вже існує в списку каналів. Змініть будь ласка код каналу.";
        JOptionPane.showMessageDialog(window, message, Strings.ERROR, JOptionPane.ERROR_MESSAGE);
    }
}
