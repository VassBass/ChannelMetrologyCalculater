package application;

import controller.FileBrowser;
import support.Settings;
import ui.ApplicationLogo;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class Application extends SwingWorker<Void, String> {
    public static final Dimension sizeOfScreen = new Dimension(Toolkit.getDefaultToolkit().getScreenSize().width, GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().height);

    public static ApplicationContext context;
    private static ApplicationLogo logo;

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
