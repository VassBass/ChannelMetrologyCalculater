package backgroundTasks.data_export;

import application.Application;
import controller.FileBrowser;
import ui.model.LoadDialog;
import ui.mainScreen.MainScreen;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

public class ExportData extends SwingWorker<Boolean, Void>{
    private static final String EXPORT_SUCCESS = "Дані вдало експортовані";
    private static final String EXPORT = "Експорт";
    private static final String ERROR = "Помилка";
    private static final String ERROR_MESSAGE = "Файл експорту не вдалось створити";

    private final MainScreen mainScreen;
    private final LoadDialog loadDialog;

    private String fileName(Calendar date){
        return "export_data ["
                + date.get(Calendar.DAY_OF_MONTH)
                + "."
                + (date.get(Calendar.MONTH) + 1)
                + "."
                + date.get(Calendar.YEAR)
                + "].exp";
    }

    private File exportFile(){
        return new File(FileBrowser.DIR_EXPORT, this.fileName(Calendar.getInstance()));
    }

    public ExportData(MainScreen mainScreen){
        super();
        this.mainScreen = mainScreen;
        this.loadDialog = new LoadDialog(mainScreen);
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                loadDialog.setVisible(true);
            }
        });
    }

    @Override
    protected Boolean doInBackground() throws Exception {
        ArrayList<?>[]list = new ArrayList<?>[]{
                Application.context.channelsController.getAll(),
                Application.context.sensorsController.getAll(),
                Application.context.calibratorsController.getAll(),
                Application.context.personsController.getAll(),
                Application.context.departmentsController.getAll(),
                Application.context.areasController.getAll(),
                Application.context.processesController.getAll(),
                Application.context.installationsController.getAll()
        };
        try {
            FileBrowser.saveToFile(this.exportFile(), list);
            return true;
        }catch (IOException e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    protected void done() {
        loadDialog.dispose();
        try {
            if (this.get()){
                JOptionPane.showMessageDialog(this.mainScreen, EXPORT_SUCCESS, EXPORT, JOptionPane.INFORMATION_MESSAGE);
            }else {
                JOptionPane.showMessageDialog(mainScreen, ERROR, ERROR_MESSAGE, JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}