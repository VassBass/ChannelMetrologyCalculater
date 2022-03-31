package application;

import service.FileBrowser;
import settings.Settings;
import ui.model.ApplicationLogo;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Application extends SwingWorker<Void, String> {
    public static final String appVersion = "v5.3";
    public static final String pathToDB = "jdbc:sqlite:Support/Data.db";
    public static final Dimension sizeOfScreen = new Dimension(Toolkit.getDefaultToolkit().getScreenSize().width, GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().height);
    private static final Logger LOGGER = Logger.getLogger(Application.class.getName());
    public static ApplicationContext context;
    private static ApplicationLogo logo;
    private static boolean busy = false;
    private static boolean firstStart = true;

    private static final String[] bufferNamesOfChannels = new String[10];

    public Application(){
        context = new ApplicationContext();
    }

    public static void main(String[] args){
        LOGGER.info("Application starts from main!");
        try {
            Application application = new Application();

            FileBrowser.init();

            logo = new ApplicationLogo();
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    logo.setVisible(true);
                }
            });

            application.start();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Exception in main: ", e);
        }
    }

    public static void setBusy(boolean b){busy = b;}
    public static boolean isBusy(Window window){
        if (busy){
            String title = "Зачекайте";
            String message = "Виконується запис данних, спробуйте після того як пропаде індикатор " +
                    "\"Запис даних\" в лівому верхньому куту головного вікна";
            JOptionPane.showMessageDialog(window,message,title,JOptionPane.ERROR_MESSAGE);
        }
        return busy;
    }
    public static boolean isBusy(){return busy;}

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

    public void start(){
        this.execute();
    }
    @Override
    protected Void doInBackground() throws Exception {
        publish("Завантаження налаштуваннь користувача");
        Settings.checkSettings();
        publish("Завантаження списку цехів");
        context.departmentService.init();
        if (firstStart) context.departmentService.resetToDefaultInCurrentThread();
        publish("Завантаження списку ділянок");
        context.areaService.init();
        if (firstStart) context.areaService.resetToDefaultInCurrentThread();
        publish("Завантаження списку процесів");
        context.processService.init();
        if (firstStart) context.processService.resetToDefaultInCurrentThread();
        publish("Завантаження списку установок");
        context.installationService.init();
        if (firstStart) context.installationService.resetToDefaultInCurrentThread();
        publish("Завантаження списку працівників");
        context.personService.init();
        if (firstStart) context.personService.resetToDefaultInCurrentThread();
        publish("Завантаження списку вимірюваннь");
        context.measurementService.init();
        if (firstStart) context.measurementService.resetToDefaultInCurrentThread();
        publish("Завантаження списку калібраторів");
        context.calibratorService.init();
        if (firstStart) context.calibratorService.resetToDefaultInCurrentThread();
        publish("Завантаження списку ПВП");
        context.sensorService.init();
        if (firstStart) context.sensorService.resetToDefault();
        context.controlPointsValuesService.init();
        if (firstStart) context.controlPointsValuesService.resetToDefault();
        publish("Завантаження списку каналів");
        context.channelService.init();
        publish("Завантаження головного вікна");
        context.mainScreen.init(context.channelService.getAll());
        return null;
    }

    @Override
    protected void process(List<String> chunks) {
        logo.setMessage(chunks.get(chunks.size() - 1));
    }

    @Override
    protected void done() {
        logo.dispose();
        context.mainScreen.setVisible(true);
    }
}