package backgroundTasks.data_export;

import application.Application;
import ui.model.LoadDialog;
import ui.mainScreen.MainScreen;

import javax.swing.*;
import java.awt.*;

public class ExportInstallations extends SwingWorker<Boolean, Void> {
    private static final String EXPORT_SUCCESS = "Дані вдало експортовані";
    private static final String EXPORT = "Експорт";
    private static final String ERROR = "Помилка";
    private static final String ERROR_MESSAGE = "Файл експорту не вдалось створити";

    private final MainScreen mainScreen;
    private final LoadDialog loadDialog;

    public ExportInstallations(MainScreen mainScreen){
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
        return Application.context.installationService.exportData();
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
