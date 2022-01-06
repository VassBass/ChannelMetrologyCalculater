package backgroundTasks.export;

import constants.Files;
import constants.Strings;
import model.Calibrator;
import ui.LoadDialog;
import ui.main.MainScreen;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Calendar;

public class ExportCalibrators extends SwingWorker<Boolean, Void> {
    private final MainScreen mainScreen;
    private final LoadDialog loadDialog;

    private String fileName(Calendar date){
        return "export_calibrators ["
                + date.get(Calendar.DAY_OF_MONTH)
                + "."
                + (date.get(Calendar.MONTH) + 1)
                + "."
                + date.get(Calendar.YEAR)
                + "].cal";
    }

    private File exportFile(){
        return new File(Files.EXPORT_DIR, this.fileName(Calendar.getInstance()));
    }

    public ExportCalibrators(MainScreen mainScreen){
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
        /*ArrayList<Calibrator> calibrators = Lists.calibrators();
        File file = this.exportFile();
        if (!file.exists()){
            if (!file.createNewFile()){
                return false;
            }
        }
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(this.exportFile()));
        oos.writeObject(calibrators);
        oos.close();*/
        return true;
    }

    @Override
    protected void done() {
        loadDialog.dispose();
        try {
            if (this.get()){
                JOptionPane.showMessageDialog(this.mainScreen, Strings.EXPORT_SUCCESS, Strings.EXPORT, JOptionPane.INFORMATION_MESSAGE);
            }else {
                JOptionPane.showMessageDialog(mainScreen, Strings.ERROR, "Файл експорту не вдалось створити", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
