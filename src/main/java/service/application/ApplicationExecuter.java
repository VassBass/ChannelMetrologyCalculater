package service.application;

import model.ui.ApplicationLogo;
import service.root.ServiceExecuter;

import javax.swing.*;
import java.util.List;

public class ApplicationExecuter implements ServiceExecuter {

    @Override
    public void execute() {
        ApplicationConfigHolder configHolder = new PropertiesApplicationConfigHolder();
        new Starter(configHolder).start();
    }

    private static class Starter extends SwingWorker<Void, String> {
        private final ApplicationLogo logo;
        private final ApplicationConfigHolder configHolder;

        public Starter(ApplicationConfigHolder configHolder) {
            super();
            this.configHolder = configHolder;
            logo = new ApplicationLogo(configHolder);
        }

        public void start(){
            beforeStart();
            execute();
        }

        private void beforeStart() {
            logo.showing();
        }

        @Override
        protected void process(List<String> chunks) {
            String message = chunks.get(chunks.size() - 1);
            logo.setMessage(message == null ? "" : message);
        }

        @Override
        protected Void doInBackground() {
            String message = "Initialization of services";
            publish(message);
            new ApplicationInitializer(configHolder).init();

            return null;
        }

        @Override
        protected void done() {
            logo.dispose();
            if (ApplicationScreen.getInstance() == null) {
                String message = "Виникла помилка при ініціалізації. Спробуйте ще." +
                        "\nЯкщо помилка не зникне перевстановіть програму або зверніться до розробника." +
                        "\nvassbassapp@gmail.com";
                JOptionPane.showMessageDialog(logo, message, "ERROR!", JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            } else {
                ApplicationScreen.getInstance().showing();
            }
        }
    }
}
