package backgroundTasks.data_import;

import application.Application;
import model.Calibrator;
import support.Comparator;
import ui.importData.compareCalibrators.CompareCalibratorsDialog;
import ui.mainScreen.MainScreen;
import ui.model.LoadWindow;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

public class ImportCalibrators extends SwingWorker<Integer, Integer> {
    private static final String ERROR = "Помилка";
    private static final String IMPORT = "Імпорт";

    private final MainScreen mainScreen;
    private final File exportDataFile;
    private final LoadWindow loadWindow;

    private ArrayList<Calibrator>importedCalibrators, newCalibrators, calibratorsForChange, changedCalibrators;

    public ImportCalibrators(File exportDataFile){
        super();
        this.mainScreen = Application.context.mainScreen;
        this.exportDataFile = exportDataFile;
        this.loadWindow = new LoadWindow();
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
    // return 1: В файле отсутствуют калибраторы
    // return -1: Во время импорта произошла ошибка
    @Override
    protected Integer doInBackground() throws Exception {
        try {
            this.importedCalibrators = this.calibratorsExtraction();
        }catch (Exception e){
            e.printStackTrace();
            return -1;
        }
        if (this.importedCalibrators == null){
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
                    if (newCalibrators.isEmpty() && calibratorsForChange.isEmpty()) {
                        message = "Нових або змінених калібраторів в файлі імпорту не знайдено";
                        JOptionPane.showMessageDialog(mainScreen,message,IMPORT, JOptionPane.INFORMATION_MESSAGE);
                    }else {
                        EventQueue.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                new CompareCalibratorsDialog(newCalibrators, calibratorsForChange, changedCalibrators).setVisible(true);
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
    private ArrayList<Calibrator>calibratorsExtraction() throws Exception {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(this.exportDataFile));
        ArrayList<Calibrator>calibrators = (ArrayList<Calibrator>) ois.readObject();
        if (calibrators.isEmpty()){
            return null;
        }else {
            return calibrators;
        }
    }

    private void fueling(){
        int progress = 0;

        ArrayList<Calibrator>oldList = Application.context.calibratorsController.getAll();
        if (oldList == null || oldList.isEmpty()) {
            this.newCalibrators = this.importedCalibrators;
        }else {
            ArrayList<Calibrator>newList = new ArrayList<>();
            ArrayList<Calibrator>changedList = new ArrayList<>();
            ArrayList<Calibrator>calibratorsForChange = new ArrayList<>();
            for (Calibrator newCalibrator : this.importedCalibrators){
                boolean exist = false;
                for (Calibrator oldCalibrator : oldList){
                    if (oldCalibrator.getName().equals(newCalibrator.getName())){
                        exist = true;
                        if (!Comparator.calibratorsMatch(oldCalibrator, newCalibrator)) {
                            calibratorsForChange.add(newCalibrator);
                            changedList.add(oldCalibrator);
                        }
                        break;
                    }
                }
                if (!exist){
                    newList.add(newCalibrator);
                }
                this.publish(++progress);
            }
            this.newCalibrators = newList;
            this.calibratorsForChange = calibratorsForChange;
            this.changedCalibrators = changedList;
        }
    }
}