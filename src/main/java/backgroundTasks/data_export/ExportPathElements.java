package backgroundTasks.data_export;

import application.Application;
import service.FileBrowser;
import ui.model.LoadDialog;
import ui.mainScreen.MainScreen;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

public class ExportPathElements extends SwingWorker<Boolean, Void> {
    private static final String EXPORT_SUCCESS = "Дані вдало експортовані";
    private static final String EXPORT = "Експорт";
    private static final String ERROR = "Помилка";
    private static final String ERROR_MESSAGE = "Файл експорту не вдалось створити";

    private final MainScreen mainScreen;
    private final LoadDialog loadDialog;

    private String fileName(Calendar date){
        return "export_path_elements ["
                + date.get(Calendar.DAY_OF_MONTH)
                + "."
                + (date.get(Calendar.MONTH) + 1)
                + "."
                + date.get(Calendar.YEAR)
                + "].pat";
    }

    private File exportFile(){
        return new File(FileBrowser.DIR_EXPORT, this.fileName(Calendar.getInstance()));
    }

    public ExportPathElements(MainScreen mainScreen){
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
        ArrayList<?>[]elements = new ArrayList[]{
                Application.context.departmentService.getAll(),
                Application.context.areaService.getAll(),
                Application.context.processService.getAll(),
                Application.context.installationService.getAll()
        };
        try {
            FileBrowser.saveToFile(this.exportFile(), elements);
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