package backgroundTasks;

import constants.Files;
import constants.Strings;
import constants.Value;
import support.*;
import ui.LoadDialog;
import ui.main.MainScreen;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ExportChannels extends SwingWorker<Void, Integer> {
    private final MainScreen mainScreen;
    private final ArrayList<Channel> channels;
    private final LoadDialog loadDialog;
    private final JProgressBar loadProgress;

    private int channelsNumber = 0;

    public ExportChannels(MainScreen mainScreen){
        super();
        this.mainScreen = mainScreen;
        this.channels = Lists.channels();
        if (this.channels != null) {
            this.channelsNumber = this.channels.size();
        }

        this.loadDialog = new LoadDialog(mainScreen);
        this.loadProgress = this.loadDialog.progressBar;
        this.loadProgress.setIndeterminate(false);
        this.loadProgress.setMaximum(this.channelsNumber);
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                loadDialog.setVisible(true);
            }
        });
    }

    @Override
    protected Void doInBackground() throws Exception {
        this.saveData(this.createExportData());
        return null;
    }

    @Override
    protected void process(List<Integer> chunks){
        int i = chunks.get(chunks.size() - 1);
        this.loadProgress.setValue(i);
        if (i == this.channels.size() - 1){
            this.loadProgress.setString("Створення файлу експорту");
        }else {
            this.loadProgress.setString(Strings.EXPORT
                    + " "
                    + i
                    + "/"
                    + this.channelsNumber);
        }
    }

    @Override
    protected void done() {
        this.loadDialog.dispose();
        JOptionPane.showMessageDialog(this.mainScreen, Strings.EXPORT_SUCCESS, Strings.EXPORT, JOptionPane.INFORMATION_MESSAGE);
    }

    private ArrayList<Values> createExportData(){
        ArrayList<Values>data = new ArrayList<>();
        for (int x=0;x<this.channelsNumber;x++){
            Channel channel = this.channels.get(x);
            Values channelData = new Values();

            //Channel info
            channelData.putValue(Value.CHANNEL_CODE, channel.getCode());//String
            channelData.putValue(Value.CHANNEL_NAME, channel.getName());//String
            channelData.putValue(Value.CHANNEL_DEPARTMENT, channel.getDepartment());//String
            channelData.putValue(Value.CHANNEL_AREA, channel.getArea());//String
            channelData.putValue(Value.CHANNEL_PROCESS, channel.getProcess());//String
            channelData.putValue(Value.CHANNEL_INSTALLATION, channel.getInstallation());//String
            channelData.putValue(Value.CHANNEL_DATE, channel.getDate());//Calendar
            channelData.putValue(Value.CHANNEL_FREQUENCY, channel.getFrequency());//double
            channelData.putValue(Value.CHANNEL_TECHNOLOGY_NUMBER, channel.getTechnologyNumber());//String
            channelData.putValue(Value.CHANNEL_PROTOCOL_NUMBER, channel.getNumberOfProtocol());//String
            channelData.putValue(Value.CHANNEL_REFERENCE, channel.getReference());//String
            channelData.putValue(Value.CHANNEL_RANGE_MIN, channel.getRangeMin());//double
            channelData.putValue(Value.CHANNEL_RANGE_MAX, channel.getRangeMax());//double
            channelData.putValue(Value.CHANNEL_ALLOWABLE_ERROR_PERCENT, channel.getAllowableErrorPercent());//double
            channelData.putValue(Value.CHANNEL_ALLOWABLE_ERROR_VALUE, channel.getAllowableError());//double
            channelData.putValue(Value.CHANNEL_IS_GOOD, channel.isGood);//boolean

            //Measurement info
            channelData.putValue(Value.MEASUREMENT_NAME, channel.getMeasurement().getName());//String
            channelData.putValue(Value.MEASUREMENT_VALUE, channel.getMeasurement().getValue());//String

            //Sensor info
            channelData.putValue(Value.SENSOR_TYPE, channel.getSensor().getType());//String
            channelData.putValue(Value.SENSOR_NAME, channel.getSensor().getName());//String
            channelData.putValue(Value.SENSOR_RANGE_MIN, channel.getSensor().getRangeMin());//double
            channelData.putValue(Value.SENSOR_RANGE_MAX, channel.getSensor().getRangeMax());//double
            channelData.putValue(Value.SENSOR_NUMBER, channel.getSensor().getNumber());//String
            channelData.putValue(Value.SENSOR_VALUE, channel.getSensor().getValue());//String
            channelData.putValue(Value.SENSOR_MEASUREMENT, channel.getSensor().getMeasurement());//String
            channelData.putValue(Value.SENSOR_ERROR_FORMULA, channel.getSensor().getErrorFormula());//String

            data.add(channelData);
            publish(x);
        }
        return data;
    }

    private void saveData(ArrayList<Values>data){
        this.loadProgress.setIndeterminate(true);
        try {
            File file = new File(Files.EXPORT_DIR, Strings.FILE_NAME_EXPORTED_CHANNELS(Calendar.getInstance()));
            if (!file.exists()){
                if (!file.createNewFile()){
                    JOptionPane.showMessageDialog(mainScreen, Strings.ERROR, "Файл експорту не вдалось створити",JOptionPane.ERROR_MESSAGE);
                }
            }
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));
            out.writeObject(data);
            out.close();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
