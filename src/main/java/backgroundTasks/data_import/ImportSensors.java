package backgroundTasks.data_import;

import application.Application;
import model.Sensor;
import support.Comparator;
import ui.importData.compareSensors.CompareSensorsDialog;
import ui.mainScreen.MainScreen;
import ui.model.ImportLoadWindow;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

public class ImportSensors extends SwingWorker<Integer, Integer> {
    private static final String ERROR = "Помилка";
    private static final String IMPORT = "Імпорт";

    private final MainScreen mainScreen;
    private final File exportDataFile;
    private final ImportLoadWindow loadWindow;

    private ArrayList<Sensor>importedSensors, newSensors, sensorsForChange, changedSensors = new ArrayList<>();

    public ImportSensors(File exportDataFile){
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

    @Override
    protected void process(List<Integer> chunks) {
        this.loadWindow.setValue(chunks.get(chunks.size() - 1));
    }

    // return 0: Импорт прошел успешно
    // return 1: В файле отсутствуют ПИП
    // return -1: Во время импорта произошла ошибка
    @Override
    protected Integer doInBackground() throws Exception {
        try {
            this.importedSensors = this.sensorsExtraction();
        }catch (Exception e){
            e.printStackTrace();
            return -1;
        }
        if (this.importedSensors == null){
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
                    message = "У обраному файлі відсутні данні ПВП";
                    JOptionPane.showMessageDialog(mainScreen, message, ERROR, JOptionPane.ERROR_MESSAGE);
                    break;
                case 0:
                    if (newSensors.isEmpty() && sensorsForChange.isEmpty()) {
                        message = "Нових або змінених ПВП в файлі імпорту не знайдено";
                        JOptionPane.showMessageDialog(mainScreen,message,IMPORT, JOptionPane.INFORMATION_MESSAGE);
                    }else {
                        EventQueue.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                new CompareSensorsDialog(newSensors, sensorsForChange, changedSensors).setVisible(true);
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
    private ArrayList<Sensor>sensorsExtraction() throws Exception {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(this.exportDataFile));
        ArrayList<Sensor>sensors = (ArrayList<Sensor>) ois.readObject();
        if (sensors.isEmpty()){
            return null;
        }else {
            return sensors;
        }
    }

    private void fueling(){
        int progress = 0;

        ArrayList<Sensor>oldList = Application.context.sensorsController.getAll();
        if (oldList == null || oldList.isEmpty()) {
            this.newSensors = this.importedSensors;
        }else {
            ArrayList<Sensor>newList = new ArrayList<>();
            ArrayList<Sensor>changedList = new ArrayList<>();
            ArrayList<Sensor>sensorsForChange = new ArrayList<>();
            for (Sensor newSensor : this.importedSensors){
                boolean exist = false;
                for (Sensor oldSensor : oldList){
                    if (oldSensor.getName().equals(newSensor.getName())){
                        exist = true;
                        if (!Comparator.sensorsMatch(oldSensor, newSensor)) {
                            sensorsForChange.add(newSensor);
                            changedList.add(oldSensor);
                        }
                        break;
                    }
                }
                if (!exist){
                    newList.add(newSensor);
                }
                this.publish(++progress);
            }
            this.newSensors = newList;
            this.sensorsForChange = sensorsForChange;
            this.changedSensors = changedList;
        }
    }
}