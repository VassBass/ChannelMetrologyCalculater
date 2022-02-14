package repository.impl;

import controller.FileBrowser;
import model.Sensor;
import model.SensorValues;
import repository.SensorsValuesRepository;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SensorsValuesRepositoryImpl implements SensorsValuesRepository {
    private static final Logger LOGGER = Logger.getLogger(SensorsValuesRepository.class.getName());

    private Map<String, ArrayList<SensorValues>> values;

    @Override
    @SuppressWarnings("unchecked")
    public void init() {
        LOGGER.fine("Start initialization of SensorsValues repository...");
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FileBrowser.FILE_SENSORS_VALUES))){
            this.values = (HashMap<String, ArrayList<SensorValues>>) ois.readObject();
            LOGGER.fine("Initialization was successful!");
        }catch (Exception ex){
            this.values = new HashMap<>();
            LOGGER.log(Level.SEVERE, "Error: ", ex);
        }
    }

    @Override
    public SensorValues getValues(Sensor sensor) {
        LOGGER.fine("Get from repository sensors values of sensor with name: " + sensor.getName());
        if (this.values == null || this.values.isEmpty()){
            LOGGER.fine("Sensors values list is empty");
            return null;
        }

        ArrayList<SensorValues>values = this.values.get(sensor.getName());
        if (values == null || values.isEmpty()){
            LOGGER.fine("Sensors values of sensor [" + sensor.getName() + "] is missing");
            return null;
        }

        for (SensorValues val : values){
            if (val.isMatch(sensor.getRangeMin(), sensor.getRangeMax())){
                LOGGER.fine("Sensor values was found");
                return val;
            }
        }

        LOGGER.fine("Sensor values for range "
                + sensor.getRangeMin() + "..." + sensor.getRangeMax()
                +  "was not found");
        return null;
    }

    @Override
    public void setValues(Sensor sensor, SensorValues values) {
        LOGGER.fine("Set values to sensor: " +
                "name[" + sensor.getName() + "]; " +
                "range[" + sensor.getRangeMin() + "..." + sensor.getRangeMax() + "]");
        if (this.values == null) this.values = new HashMap<>();
        ArrayList<SensorValues>val = this.values.get(sensor.getName());

        if (val == null) val = new ArrayList<>();

        int index = -1;
        for (int i=0;i<val.size();i++){
            if (val.get(i).isMatch(values)){
                index = i;
                break;
            }
        }

        String toLog;
        if (index >= 0){
            val.set(index, values);
            toLog = "Values for sensor: "
                    + "name[" + sensor.getName() + "]; "
                    + "range[" + sensor.getRangeMin() + "..." + sensor.getRangeMax() + "] "
                    + "was ReWrite successful";
        }else {
            val.add(values);
            toLog = "Values for sensor: "
                    + "name[" + sensor.getName() + "]; "
                    + "range[" + sensor.getRangeMin() + "..." + sensor.getRangeMax() + "] "
                    + "was write successful";
        }
        this.values.put(sensor.getName(), val);

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FileBrowser.FILE_SENSORS_VALUES))){
            oos.writeObject(this.values);
            LOGGER.fine(toLog);
        }catch (Exception ex){
            LOGGER.log(Level.SEVERE, "Error: ", ex);
        }
    }
}
