package repository;

import model.Channel;
import model.Sensor;

import java.util.Collection;
import java.util.List;

public interface ChannelRepository extends Repository<Channel>{
    Channel get(String code);

    boolean removeBySensor(Sensor sensor);
    boolean removeByMeasurementValue(String measurementValue);

    boolean importData(Collection<Channel> newChannels, Collection<Channel>channelsForChange);

    boolean changeSensor(Sensor oldSensor, Sensor newSensor, int ... ignored);
    boolean changeSensors(List<Sensor>sensors);
    boolean changeMeasurementValue(String oldValue, String newValue);

    boolean isExist(String code);
    boolean isExist(String oldChannelCode, String newChannelCode);
}
