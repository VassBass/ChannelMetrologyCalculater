package backgroundTasks.data_import;

import application.Application;
import model.Channel;
import model.Sensor;
import support.Comparator;
import ui.importData.compareChannels.CompareChannelsDialog;
import ui.mainScreen.MainScreen;
import ui.model.ImportLoadWindow;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

public class ImportChannels extends SwingWorker<Integer, Integer> {
    private static final String ERROR = "Помилка";
    private static final String IMPORT = "Імпорт";

    private final MainScreen mainScreen;
    private final File exportDataFile;
    private final ImportLoadWindow loadWindow;

    private ArrayList<Sensor>importedSensors, newSensors, sensorsForChange;
    private ArrayList<Channel>importedChannels, newChannels, channelsForChange, changedChannels;

    public ImportChannels(File exportDataFile){
        super();
        this.mainScreen = Application.context.mainScreen;
        this.exportDataFile = exportDataFile;
        this.loadWindow = new ImportLoadWindow();
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                loadWindow.setVisible(true);
            }
        });
    }

    private void setProgress(double progress){
        int p = (int)progress;
        if (p != 0) {
            double r = progress - p;
            if (r == 0D) {
                double pr1 = this.importedChannels.size() / 100D;
                double pr = p / pr1;
                this.publish((int)pr);
            }
        }
    }

    @Override
    protected void process(List<Integer> chunks) {
        this.loadWindow.setValue(chunks.get(chunks.size() - 1));
    }

    // return 0: Импорт прошел успешно
    // return 1: В файле отсутствуют калибраторы
    // return -1: Во время импорта произошла ошибка
    @Override
    protected Integer doInBackground() throws Exception {
        try {
            this.dataExtraction();
        }catch (Exception e){
            e.printStackTrace();
            return -1;
        }
        if (this.importedChannels == null){
            return 1;
        }else {
            fueling();
            return 0;
        }
    }

    @Override
    protected void done() {
        this.loadWindow.dispose();
        try {
            String message;
            switch (this.get()) {
                case 1:
                    message = "У обраному файлі відсутні данні калібраторів";
                    JOptionPane.showMessageDialog(mainScreen, message, ERROR, JOptionPane.ERROR_MESSAGE);
                    break;
                case 0:
                    if (newChannels.isEmpty() && channelsForChange.isEmpty()) {
                        message = "Нових або змінених калібраторів в файлі імпорту не знайдено";
                        JOptionPane.showMessageDialog(mainScreen,message,IMPORT, JOptionPane.INFORMATION_MESSAGE);
                    }else {
                        EventQueue.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                new CompareChannelsDialog(newChannels, channelsForChange, changedChannels, newSensors, sensorsForChange).setVisible(true);
                            }
                        });
                    }
                    break;
                case -1:
                    message = "Помилка при виконанні імпорту";
                    JOptionPane.showMessageDialog(mainScreen, message, ERROR, JOptionPane.ERROR_MESSAGE);
                    break;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private void dataExtraction() throws Exception {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(this.exportDataFile));
        ArrayList<?>[]data = (ArrayList<?>[]) ois.readObject();
        ArrayList<Channel>channels = (ArrayList<Channel>) data[0];
        if (channels.isEmpty()){
            this.importedChannels = null;
        }else {
            this.importedChannels =  channels;
            this.importedSensors = (ArrayList<Sensor>) data[1];
        }
    }

    private void fueling() {
        double progress = 0D;

        ArrayList<Sensor> oldSensors = Application.context.sensorsController.getAll();
        if (oldSensors.isEmpty()) {
            this.newSensors = this.importedSensors;
        } else {
            ArrayList<Sensor> newSensorsList = new ArrayList<>();
            ArrayList<Sensor> sensorsForChange = new ArrayList<>();
            for (Sensor newSensor : this.importedSensors) {
                boolean exist = false;
                for (Sensor oldSensor : oldSensors) {
                    if (oldSensor.getName().equals(newSensor.getName())) {
                        exist = true;
                        if (!Comparator.sensorsMatch(newSensor, oldSensor)) {
                            sensorsForChange.add(newSensor);
                        }
                        break;
                    }
                }
                if (!exist) {
                    newSensorsList.add(newSensor);
                }
                progress = progress + 0.5;
                this.setProgress(progress);
            }
            this.newSensors = newSensorsList;
            this.sensorsForChange = sensorsForChange;
        }

        ArrayList<Channel> oldList = Application.context.channelsController.getAll();
        if (oldList == null || oldList.isEmpty()) {
            this.newChannels = this.importedChannels;
            this.channelsForChange = new ArrayList<>();
        } else {
            ArrayList<Channel> newList = new ArrayList<>();
            ArrayList<Channel> changedList = new ArrayList<>();
            ArrayList<Channel> channelsForChange = new ArrayList<>();
            for (Channel newChannel : this.importedChannels) {
                boolean exist = false;
                for (Channel oldChannel : oldList) {
                    if (oldChannel.getCode().equals(newChannel.getCode())) {
                        exist = true;
                        if (!Comparator.channelsMatch(newChannel, oldChannel)) {
                            changedList.add(oldChannel);
                            channelsForChange.add(newChannel);
                        }
                        break;
                    }
                }
                if (!exist) {
                    newList.add(newChannel);
                }
                progress = progress + 0.5;
                this.setProgress(progress);
            }
            this.newChannels = newList;
            this.channelsForChange = channelsForChange;
            this.changedChannels = changedList;
        }
    }
}