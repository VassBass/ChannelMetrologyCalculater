package service.application;

import model.ui.ApplicationLogo;
import service.repository.RepositoryServiceExecuter;

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

        public Starter(ApplicationConfigHolder configHolder) {
            super();
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
        protected void done() {
            logo.setMessage("Starting application");
        }

        @Override
        protected Void doInBackground() {
            FactoryImplementationHolder factoryImplementationHolder = new HashMapFactoryImplementationHolder();

            String serviceInitMessage = "Service initialization ... ";

            publish(serviceInitMessage + "RepositoryService");
            new RepositoryServiceExecuter(factoryImplementationHolder).execute();

            return null;
        }
    }
}
