package application;

import controller.FileBrowser;
import support.Settings;
import ui.model.ApplicationLogo;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class Application extends SwingWorker<Void, String> {
    public static final String appVersion = "v5.1";
    public static final Dimension sizeOfScreen = new Dimension(Toolkit.getDefaultToolkit().getScreenSize().width, GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().height);

    public static ApplicationContext context;
    private static ApplicationLogo logo;
    private static boolean busy = false;

    private static final String[] bufferNamesOfChannels = new String[10];

    public Application(){
        context = new ApplicationContext();
    }

    public static void main(String[] args){
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
            e.printStackTrace();
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

    public void start(){
        this.execute();
    }
    @Override
    protected Void doInBackground() throws Exception {
        publish("Завантаження списку цехів");
        context.departmentsController.init(context.mainScreen);
        publish("Завантаження списку ділянок");
        context.areasController.init(context.mainScreen);
        publish("Завантаження списку процесів");
        context.processesController.init(context.mainScreen);
        publish("Завантаження списку установок");
        context.installationsController.init(context.mainScreen);
        publish("Завантаження списку працівників");
        context.personsController.init(context.mainScreen);
        publish("Завантаження списку вимірюваннь");
        context.measurementsController.init();
        publish("Завантаження списку калібраторів");
        context.calibratorsController.init(context.mainScreen);
        publish("Завантаження списку ПВП");
        context.sensorsController.init(context.mainScreen);
        publish("Завантаження списку каналів");
        context.channelsController.init(context.mainScreen);
        publish("Завантаження налаштуваннь користувача");
        Settings.checkSettings();
        publish("Завантаження головного вікна");
        context.mainScreen.init(context.channelsController.getAll());
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