package backgroundTasks.tasks_for_import;

import backgroundTasks.export.ExportData;
import constants.Strings;
import support.Calibrator;
import support.Comparator;
import support.Lists;
import ui.LoadDialog;
import ui.importData.compareCalibrators.CompareCalibratorsDialog;
import ui.main.MainScreen;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Objects;

public class ImportCalibrators extends SwingWorker<Integer, Void> {
    private final MainScreen mainScreen;
    private final File exportDataFile;
    private final LoadDialog loadDialog;

    private ArrayList<Calibrator> importedCalibrators, newCalibratorsList;
    private ArrayList<Integer[]>calibratorsIndexes;

    public ImportCalibrators(MainScreen mainScreen, File exportDataFile){
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
            this.copyCalibrators();
            return 0;
        }
    }

    @Override
    protected void done() {
        loadDialog.dispose();
        try {
            switch (this.get()) {
                case 1:
                    JOptionPane.showMessageDialog(mainScreen, "У обраному файлі відсутні данні калібраторів", Strings.ERROR, JOptionPane.ERROR_MESSAGE);
                    break;
                case 0:
                    EventQueue.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            new CompareCalibratorsDialog(mainScreen, ExportData.CALIBRATORS, newCalibratorsList, importedCalibrators, calibratorsIndexes);
                        }
                    });
                    break;
                case -1:
                    JOptionPane.showMessageDialog(mainScreen, "Помилка при виконанні імпорту", Strings.ERROR, JOptionPane.ERROR_MESSAGE);
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

    private void copyCalibrators(){
        ArrayList<Calibrator>oldCalibratorsList = Lists.calibrators();
        ArrayList<Integer[]>indexes = new ArrayList<>();
        ArrayList<Calibrator>newList = new ArrayList<>();

        for (int o = 0; o< Objects.requireNonNull(oldCalibratorsList).size(); o++){
            boolean exist = false;
            Calibrator old = oldCalibratorsList.get(o);
            for (int i=0;i<this.importedCalibrators.size();i++){
                Calibrator imp = this.importedCalibrators.get(i);
                if (old.getName().equals(imp.getName())){
                    exist = true;
                    if (Comparator.calibratorsMatch(old, imp)){
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
        for (Calibrator imp : this.importedCalibrators) {
            boolean exist = false;
            for (Calibrator old : oldCalibratorsList) {
                if (imp.getName().equals(old.getName())) {
                    exist = true;
                    break;
                }
            }
            if (!exist) {
                newList.add(imp);
            }
        }

        if (newList.isEmpty()){
            this.newCalibratorsList = null;
        }else {
            this.newCalibratorsList = newList;
        }
        if (indexes.isEmpty()){
            this.calibratorsIndexes = null;
        }else {
            this.calibratorsIndexes = indexes;
        }
    }
}
