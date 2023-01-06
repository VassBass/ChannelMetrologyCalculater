package application;

import def.*;
import factory.AbstractFactory;
import repository.*;
import repository.impl.*;
import service.FileBrowser;
import settings.Settings;
import ui.factory.WindowFactory;
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
    private static final Logger LOGGER = Logger.getLogger(Application.class.getName());
    public static ApplicationContext context;
    private static ApplicationLogo logo;
    private static boolean firstStart = true;

    private static final String[] bufferNamesOfChannels = new String[10];

    private final DepartmentRepository departmentRepository;
    private final AreaRepository areaRepository;
    private final ProcessRepository processRepository;
    private final InstallationRepository installationRepository;
    private final PersonRepository personRepository;
    private final MeasurementRepository measurementRepository;
    private final CalibratorRepository calibratorRepository;
    private final SensorRepository sensorRepository;
    private final ControlPointsValuesRepository controlPointsValuesRepository;

    private final MainScreen mainScreen;

    public Application() {
        AbstractFactory repositoryFactory = new SQLiteRepositoryFactory();
        this.departmentRepository = repositoryFactory.create(DepartmentRepository.class);
        this.areaRepository = repositoryFactory.create(AreaRepository.class);
        this.processRepository = repositoryFactory.create(ProcessRepository.class);
        this.installationRepository = repositoryFactory.create(InstallationRepository.class);
        this.personRepository = repositoryFactory.create(PersonRepository.class);
        this.measurementRepository = repositoryFactory.create(MeasurementRepository.class);
        this.calibratorRepository = repositoryFactory.create(CalibratorRepository.class);
        this.sensorRepository = repositoryFactory.create(SensorRepository.class);
        this.controlPointsValuesRepository = repositoryFactory.create(ControlPointsValuesRepository.class);

        AbstractFactory windowFactory = new WindowFactory(repositoryFactory);
        this.mainScreen = windowFactory.create(MainScreen.class);
    }

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
            departmentRepository.rewrite(DefaultDepartments.get());
            areaRepository.rewrite(DefaultAreas.get());
            installationRepository.rewrite(DefaultInstallations.get());
            processRepository.rewrite(DefaultProcesses.get());
            personRepository.rewrite(DefaultPersons.get());
            measurementRepository.rewrite(DefaultMeasurements.get());
            calibratorRepository.rewrite(DefaultCalibrators.get());
            sensorRepository.rewrite(DefaultSensors.get());
            controlPointsValuesRepository.rewrite(DefaultControlPointsValues.get());
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