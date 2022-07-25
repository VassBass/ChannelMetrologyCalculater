package service.impl;

import model.Channel;
import model.Sensor;
import repository.ChannelRepository;
import repository.impl.ChannelRepositorySQLite;
import service.ChannelService;

import java.util.List;

public class ChannelServiceImpl implements ChannelService {
    private final ChannelRepository repository;

    public ChannelServiceImpl(){
        this.repository = new ChannelRepositorySQLite();
    }

    public ChannelServiceImpl(ChannelRepository repository){
        this.repository = repository;
    }

    @Override
    public Channel get(String code) {
        return this.repository.get(code);
    }

    @Override
    public List<Channel> getAll() {
        return this.repository.getAll();
    }

    @Override
    public boolean add(Channel channel) {
        return this.repository.add(channel);
    }

    @Override
    public boolean remove(Channel channel) {
        return this.repository.remove(channel);
    }

    @Override
    public boolean removeBySensor(Sensor sensor){
        return this.repository.removeBySensor(sensor);
    }

    @Override
    public boolean removeByMeasurementValue(String measurementValue) {
        return this.repository.removeByMeasurementValue(measurementValue);
    }

    @Override
    public boolean changeSensor(Sensor oldSensor, Sensor newSensor, int ... ignored){
        return this.repository.changeSensor(oldSensor, newSensor, ignored);
    }

    @Override
    public boolean changeSensors(List<Sensor>sensors){
        return this.repository.changeSensors(sensors);
    }

    @Override
    public boolean set(Channel oldChannel, Channel newChannel) {
        return this.repository.set(oldChannel, newChannel);
    }

    @Override
    public boolean changeMeasurementValue(String oldValue, String newValue) {
        return this.repository.changeMeasurementValue(oldValue, newValue);
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
    public boolean clear() {
        return this.repository.clear();
    }

    @Override
    public boolean importData(List<Channel>newChannels, List<Channel>channelsForChange){
        return this.repository.importData(newChannels, channelsForChange);
    }

    @Override
    public boolean rewrite(List<Channel>channels){
        return this.repository.rewrite(channels);
    }
}