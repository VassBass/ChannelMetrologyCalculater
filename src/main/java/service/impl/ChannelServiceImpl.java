package service.impl;

import model.Channel;
import model.Sensor;
import repository.ChannelRepository;
import repository.impl.ChannelRepositoryImpl;
import service.ChannelService;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.logging.Logger;

public class ChannelServiceImpl implements ChannelService {
    private static final Logger LOGGER = Logger.getLogger(ChannelService.class.getName());

    private static final String ERROR = "Помилка";

    private final String dbUrl;

    private ChannelRepository repository;

    public ChannelServiceImpl(){
        this.dbUrl = null;
    }

    public ChannelServiceImpl(String dbUrl){
        this.dbUrl = dbUrl;
    }

    @Override
    public void init(){
        this.repository = this.dbUrl == null ? new ChannelRepositoryImpl() : new ChannelRepositoryImpl(this.dbUrl);
        LOGGER.info("Initialization SUCCESS");
    }

    @Override
    public Channel get(String code) {
        return this.repository.get(code);
    }

    @Override
    public ArrayList<Channel> getAll() {
        return this.repository.getAll();
    }

    @Override
    public ArrayList<Channel> add(Channel channel) {
        this.repository.add(channel);
        return this.repository.getAll();
    }

    @Override
    public void addInCurrentThread(Channel channel) {
        this.repository.addInCurrentThread(channel);
    }

    @Override
    public ArrayList<Channel> remove(Channel channel) {
        this.repository.remove(channel);
        return this.repository.getAll();
    }

    @Override
    public void removeBySensorInCurrentThread(Sensor sensor){
        this.repository.removeBySensorInCurrentThread(sensor);
    }

    @Override
    public void changeSensorInCurrentThread(Sensor oldSensor, Sensor newSensor, int ... ignored){
        this.repository.changeSensorInCurrentThread(oldSensor, newSensor, ignored);
    }

    @Override
    public void changeSensorsInCurrentThread(ArrayList<Sensor>sensors){
        this.repository.changeSensorsInCurrentThread(sensors);
    }

    @Override
    public ArrayList<Channel> set(Channel oldChannel, Channel newChannel) {
        this.repository.set(oldChannel, newChannel);
        return this.repository.getAll();
    }

    @Override
    public void setInCurrentThread(Channel oldChannel, Channel newChannel) {
        this.repository.setInCurrentThread(oldChannel, newChannel);
    }

    @Override
    public boolean isExist(String code){
        return this.repository.isExist(code);
    }

    @Override
    public boolean isExist(String oldChannelCode, String newChannelCode){
        return this.repository.isExist(oldChannelCode, newChannelCode);
    }

    @Override
    public void clear() {
        this.repository.clear();
    }

    @Override
    public void importDataInCurrentThread(ArrayList<Channel>newChannels, ArrayList<Channel>channelsForChange){
        this.repository.importDataInCurrentThread(newChannels, channelsForChange);
    }

    @Override
    public void rewriteInCurrentThread(ArrayList<Channel>channels){
        this.repository.rewriteInCurrentThread(channels);
    }

    @Override
    public void showExistMessage(Window window) {
        String message = "Канал з данним кодом вже існує в списку каналів. Змініть будь ласка код каналу.";
        JOptionPane.showMessageDialog(window, message, ERROR, JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public boolean backgroundTaskIsRun() {
        return this.repository.backgroundTaskIsRun();
    }
}