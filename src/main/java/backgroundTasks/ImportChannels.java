package backgroundTasks;

import constants.MeasurementConstants;
import constants.SensorType;
import constants.Strings;
import constants.Value;
import measurements.Measurement;
import support.Sensor;
import support.*;
import ui.LoadDialog;
import ui.importChannels.compareChannels.CompareChannelsDialog;
import ui.main.MainScreen;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Calendar;

@SuppressWarnings("unchecked")
public class ImportChannels extends SwingWorker<Void, Void> {
    private final MainScreen mainScreen;
    private final File importDataFile;
    private final LoadDialog loadDialog;

    private final ArrayList<Channel>oldList;
    private ArrayList<Channel>importedList;
    private final ArrayList<Channel>newList;
    private final ArrayList<Integer[]>indexes;

    public ImportChannels(final MainScreen mainScreen, File importDataFile){
        super();
        this.mainScreen = mainScreen;
        this.importDataFile = importDataFile;
        this.oldList = Lists.channels();
        this.newList = new ArrayList<>();
        this.indexes = new ArrayList<>();
        this.loadDialog = new LoadDialog(mainScreen);
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                loadDialog.setVisible(true);
            }
        });
    }

    @Override
    protected Void doInBackground() throws Exception {
        this.importedList = this.dataExtraction();
        this.copyChannels();
        return null;
    }

    @Override
    protected void done() {
        if (this.indexes.size() == 0){
            this.loadDialog.dispose();
            new SaveChannelsToFile(this.mainScreen, this.newList).execute();
            JOptionPane.showMessageDialog(this.mainScreen, Strings.IMPORT_SUCCESS, Strings.SUCCESS, JOptionPane.INFORMATION_MESSAGE);
        }else {
            this.loadDialog.dispose();
            new CompareChannelsDialog(this.mainScreen, this.oldList, this.importedList, this.newList, this.indexes).setVisible(true);
        }
    }

    private ArrayList<Channel>dataExtraction(){
        ArrayList<Channel>channels = new ArrayList<>();
        ArrayList<Values>data = new ArrayList<>();
        try {
            ObjectInputStream oos = new ObjectInputStream(new FileInputStream(this.importDataFile));
            data = (ArrayList<Values>) oos.readObject();
            oos.close();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        for (Values channelData : data) {
            Channel channel = new Channel();

            channel.setCode(channelData.getStringValue(Value.CHANNEL_CODE));
            channel.setName(channelData.getStringValue(Value.CHANNEL_NAME));
            channel.setDepartment(channelData.getStringValue(Value.CHANNEL_DEPARTMENT));
            channel.setArea(channelData.getStringValue(Value.CHANNEL_AREA));
            channel.setProcess(channelData.getStringValue(Value.CHANNEL_PROCESS));
            channel.setInstallation(channelData.getStringValue(Value.CHANNEL_INSTALLATION));
            channel.setDate((Calendar) channelData.getValue(Value.CHANNEL_DATE));
            channel.setFrequency(channelData.getDoubleValue(Value.CHANNEL_FREQUENCY));
            channel.setTechnologyNumber(channelData.getStringValue(Value.CHANNEL_TECHNOLOGY_NUMBER));
            channel.setNumberOfProtocol(channelData.getStringValue(Value.CHANNEL_PROTOCOL_NUMBER));
            channel.setReference(channelData.getStringValue(Value.CHANNEL_REFERENCE));
            channel.setRangeMin(channelData.getDoubleValue(Value.CHANNEL_RANGE_MIN));
            channel.setRangeMax(channelData.getDoubleValue(Value.CHANNEL_RANGE_MAX));
            channel.setAllowableError(channelData.getDoubleValue(Value.CHANNEL_ALLOWABLE_ERROR_PERCENT),
                    channelData.getDoubleValue(Value.CHANNEL_ALLOWABLE_ERROR_VALUE));
            channel.isGood = channelData.getBooleanValue(Value.CHANNEL_IS_GOOD);

            MeasurementConstants measurementName = MeasurementConstants.getConstantFromString(channelData.getStringValue(Value.MEASUREMENT_NAME));
            MeasurementConstants measurementValue = MeasurementConstants.getConstantFromString(channelData.getStringValue(Value.MEASUREMENT_VALUE));
            Measurement measurement = new Measurement(measurementName, measurementValue);
            channel.setMeasurement(measurement);

            Sensor sensor = new Sensor();
            sensor.setType(SensorType.getConstantFromString(channelData.getStringValue(Value.SENSOR_TYPE)));
            ArrayList<Sensor> sensors = Lists.sensors();
            if (sensors != null) {
                for (Sensor s : sensors) {
                    if (s.getType().equals(sensor.getType())) {
                        sensor = s;
                        break;
                    }
                }
            }
            sensor.setRangeMin(channelData.getDoubleValue(Value.SENSOR_RANGE_MIN));
            sensor.setRangeMax(channelData.getDoubleValue(Value.SENSOR_RANGE_MAX));
            sensor.setNumber(channelData.getStringValue(Value.SENSOR_NUMBER));
            sensor.setValue(channelData.getStringValue(Value.SENSOR_VALUE));
            sensor.setMeasurement(channelData.getStringValue(Value.SENSOR_MEASUREMENT));
            channel.setSensor(sensor);

            channels.add(channel);
        }
        return channels;
    }

    private void copyChannels(){
        for (int i=0;i<this.importedList.size();i++){
            boolean exists = false;
            for (int old=0;old<this.oldList.size();old++){
                if (this.oldList.get(old).getCode().equals(this.importedList.get(i).getCode())){
                    exists = true;
                    if (this.oldList.get(old).equals(this.importedList.get(i))){
                        this.newList.add(this.oldList.get(old));
                    }else {
                        this.indexes.add(new Integer[]{old, i});
                    }
                    break;
                }
            }
            if (!exists){
                this.newList.add(this.importedList.get(i));
            }
        }
    }
}
