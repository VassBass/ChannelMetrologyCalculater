package backgroundTasks.export;

import constants.Files;
import constants.Strings;
import ui.LoadDialog;
import ui.main.MainScreen;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Calendar;

public class ExportAreas extends SwingWorker<Boolean, Void> {
    private final MainScreen mainScreen;
    private final LoadDialog loadDialog;

    private String fileName(Calendar date){
        return "export_areas ["
                + date.get(Calendar.DAY_OF_MONTH)
                + "."
                + (date.get(Calendar.MONTH) + 1)
                + "."
                + date.get(Calendar.YEAR)
                + "].are";
    }

    private File exportFile(){
        return new File(Files.EXPORT_DIR, this.fileName(Calendar.getInstance()));
    }

    public ExportAreas(MainScreen mainScreen){
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
        /*ArrayList<String> areas = Lists.areas();
        File file = this.exportFile();
        if (!file.exists()){
            if (!file.createNewFile()){
                return false;
            }
        }
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(this.exportFile()));
        oos.writeObject(areas);
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
