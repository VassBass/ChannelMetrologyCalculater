package application;

import def.*;
import repository.impl.*;
import service.FileBrowser;
import settings.Settings;
import ui.mainScreen.MainScreen;
import ui.model.ApplicationLogo;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Application extends SwingWorker<Void, String> {
    public static final String appVersion = "v5.4";
    public static final Dimension sizeOfScreen = new Dimension(Toolkit.getDefaultToolkit().getScreenSize().width, GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().height);
    private static final Logger LOGGER = Logger.getLogger(Application.class.getName());
    public static ApplicationContext context;
    private static ApplicationLogo logo;
    private static boolean firstStart = true;

    private static final String[] bufferNamesOfChannels = new String[10];

    public static void main(String[] args){
        LOGGER.info("Application starts from main!");
        try {
            new Application().start();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Exception in main: ", e);
        }
    }

    public static String[] getHints(){
        return bufferNamesOfChannels;
    }
    public static void putHint(String hint){
        if (hint == null) return;
        if (bufferNamesOfChannels[0] == null){
            bufferNamesOfChannels[0] = hint;
            return;
        }
        int index = -1;
        for (int i=0;i<bufferNamesOfChannels.length;i++){
            String buffer = bufferNamesOfChannels[i];
            if (buffer == null) break;
            if (buffer.equals(hint)){
                index = i;
                break;
            }
        }
        if (index > 0){
            String b = bufferNamesOfChannels[index - 1];
            bufferNamesOfChannels[index - 1] = bufferNamesOfChannels[index];
            bufferNamesOfChannels[index] = b;
        }else if (index < 0){
            for (index = 0;index<bufferNamesOfChannels.length;index++){
                if (bufferNamesOfChannels[index] == null || index == (bufferNamesOfChannels.length - 1)){
                    bufferNamesOfChannels[index] = hint;
                    break;
                }
            }
        }
    }

    public static void setNotFirstRun(){firstStart = false;}

    public void start() throws IOException {
        FileBrowser.init();
        logo = new ApplicationLogo();
        EventQueue.invokeLater(() -> logo.setVisible(true));

        this.execute();
    }
    @Override
    protected Void doInBackground() throws Exception {
        publish("Завантаження налаштуваннь користувача");
        Settings.checkSettings();
        publish("Встановлення налаштуваннь мови");
        ApplicationContext.setUkraineLocalization();
        publish("Завантаження списків");
        if (firstStart){
            DepartmentRepositorySQLite.getInstance().rewrite(DefaultDepartments.get());
            AreaRepositorySQLite.getInstance().rewrite(DefaultAreas.get());
            InstallationRepositorySQLite.getInstance().rewrite(DefaultInstallations.get());
            ProcessRepositorySQLite.getInstance().rewrite(DefaultProcesses.get());
            PersonRepositorySQLite.getInstance().rewrite(DefaultPersons.get());
            MeasurementRepositorySQLite.getInstance().rewrite(DefaultMeasurements.get());
            CalibratorRepositorySQLite.getInstance().rewrite(DefaultCalibrators.get());
            SensorRepositorySQLite.getInstance().rewrite(DefaultSensors.get());
            ControlPointsValuesRepositorySQLite.getInstance().rewrite(DefaultControlPointsValues.get());
        }
        publish("Архівування сертифікатів/протоколів");
        FileBrowser.createArchive();
        publish("Завантаження головного вікна");
        return null;
    }

    @Override
    protected void process(List<String> chunks) {
        logo.setMessage(chunks.get(chunks.size() - 1));
    }

    @Override
    protected void done() {
        MainScreen.getInstance().setVisible(true);
        logo.dispose();
    }
}