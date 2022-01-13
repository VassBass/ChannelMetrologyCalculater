package backgroundTasks.export;

import constants.Files;
import constants.Strings;
import ui.model.LoadDialog;
import ui.mainScreen.MainScreen;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Calendar;

public class ExportChannels extends SwingWorker<Boolean, Void> {
    private final MainScreen mainScreen;
    private final LoadDialog loadDialog;

    private String fileName(Calendar date){
        return "export_channels ["
                + date.get(Calendar.DAY_OF_MONTH)
                + "."
                + (date.get(Calendar.MONTH) + 1)
                + "."
                + date.get(Calendar.YEAR)
                + "].chn";
    }

    private File exportFile(){
        return new File(Files.EXPORT_DIR, this.fileName(Calendar.getInstance()));
    }

    public ExportChannels(MainScreen mainScreen){
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
        /*ArrayList<Channel>channels = Lists.channels();
        ArrayList<Sensor> sensors = Lists.sensors();
        ArrayList<?>[]data = new ArrayList<?>[]{channels, sensors};
        File file = this.exportFile();
        if (!file.exists()){
            if (!file.createNewFile()){
                return false;
            }
        }
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(this.exportFile()));
        oos.writeObject(data);
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
