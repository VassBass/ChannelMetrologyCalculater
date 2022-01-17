package backgroundTasks.data_export;

import constants.Strings;
import ui.model.LoadDialog;
import ui.mainScreen.MainScreen;

import javax.swing.*;
import java.awt.*;
import java.util.Calendar;

public class ExportData extends SwingWorker<Void, Void>{
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
    public static final int ALL_DATA = 0;
    public static final int CHANNELS = 1;
    public static final int SENSORS = 2;
    public static final int CALIBRATORS = 3;
    public static final int DEPARTMENTS = 4;
    public static final int AREAS = 5;
    public static final int PROCESSES = 6;
    public static final int INSTALLATIONS = 7;
    public static final int ALL_PATH_ELEMENTS = 8;
    public static final int PERSONS = 9;

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
    protected Void doInBackground() throws Exception {
        /*ArrayList<?>[]data = new ArrayList<?>[]{
                Lists.sensors(), Lists.channels(), Lists.calibrators(), Lists.persons(),
                Lists.departments(), Lists.areas(), Lists.processes(), Lists.installations()
        };
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(this.fileName(Calendar.getInstance())));
        oos.writeObject(data);
        oos.close();*/
        return null;
    }

    @Override
    protected void done() {
        this.loadDialog.dispose();
        JOptionPane.showMessageDialog(this.mainScreen, Strings.EXPORT_SUCCESS, Strings.EXPORT, JOptionPane.INFORMATION_MESSAGE);
    }
}
