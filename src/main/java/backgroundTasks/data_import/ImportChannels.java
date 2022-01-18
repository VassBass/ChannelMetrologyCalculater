package backgroundTasks.data_import;

import application.Application;
import model.Channel;
import model.Model;
import model.Sensor;
import support.Comparator;
import ui.importData.compareChannels.CompareChannelsDialog;
import ui.mainScreen.MainScreen;
import ui.model.LoadDialog;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class ImportChannels extends SwingWorker<Integer, Void> {
    private static final String ERROR = "Помилка";

    private final MainScreen mainScreen;
    private final File exportDataFile;
    private final LoadDialog loadDialog;

    private ArrayList<Channel>importedChannels, newChannelsList;
    private ArrayList<Sensor> importedSensors;
    private ArrayList<Integer[]> channelsIndexes;

    public ImportChannels(MainScreen mainScreen, File exportDataFile){
        super();
        this.mainScreen = mainScreen;
        this.exportDataFile = exportDataFile;
        this.loadDialog = new LoadDialog(mainScreen);
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                loadDialog.setVisible(true);
            }
        });
    }

    // return 0: Импорт прошел успешно
    // return 1: В файле отсутствуют каналы
    // return -1: Во время импорта произошла ошибка
    @Override
    protected Integer doInBackground() throws Exception {
        try {
            this.dataExtraction();
        }catch (Exception e){
            return -1;
        }
        if (this.importedChannels == null){
            return 1;
        }else {
            this.copyChannels();
            return 0;
        }
    }

    @Override
    protected void done() {
        loadDialog.dispose();
        try {
            switch (this.get()) {
                case 1:
                    JOptionPane.showMessageDialog(mainScreen, "У обраному файлі відсутні данні каналів", ERROR, JOptionPane.ERROR_MESSAGE);
                    break;
                case 0:
                    EventQueue.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            new CompareChannelsDialog(mainScreen, Model.CHANNEL, importedSensors,
                                    newChannelsList,importedChannels, channelsIndexes);
                        }
                    });
                    break;
                case -1:
                    JOptionPane.showMessageDialog(mainScreen, "Помилка при виконанні імпорту", ERROR, JOptionPane.ERROR_MESSAGE);
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
        ArrayList<Sensor>sensors = (ArrayList<Sensor>) data[1];
        if (channels.isEmpty()){
            this.importedChannels = channels;
            this.importedSensors = sensors;
        }
    }

    private void copyChannels(){
        ArrayList<Channel>oldChannelsList = Application.context.channelsController.getAll();
        ArrayList<Integer[]>indexes = new ArrayList<>();
        ArrayList<Channel>newList = new ArrayList<>();

        for (int o = 0; o< oldChannelsList.size(); o++){
            boolean exist = false;
            Channel old = oldChannelsList.get(o);
            for (int i=0;i<this.importedChannels.size();i++){
                Channel imp = this.importedChannels.get(i);
                if (old.getCode().equals(imp.getCode())){
                    exist = true;
                    if (Comparator.channelsMatch(old, imp)){
                        newList.add(old);
                    }else {
                        indexes.add(new Integer[]{o,i});
                    }
                    break;
                }
            }
            if (!exist){
                newList.add(old);
            }
        }
        for (Channel imp : this.importedChannels) {
            boolean exist = false;
            for (Channel old : oldChannelsList) {
                if (imp.getCode().equals(old.getCode())) {
                    exist = true;
                    break;
                }
            }
            if (!exist) {
                newList.add(imp);
            }
        }

        if (newList.isEmpty()){
            this.newChannelsList = null;
        }else {
            this.newChannelsList = newList;
        }
        if (indexes.isEmpty()){
            this.channelsIndexes = null;
        }else {
            this.channelsIndexes = indexes;
        }
    }
}