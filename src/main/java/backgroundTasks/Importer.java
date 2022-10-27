package backgroundTasks;

import model.*;
import repository.*;
import repository.impl.*;
import ui.importData.compareCalibrators.CompareCalibratorsDialog;
import ui.importData.compareChannels.CompareChannelsDialog;
import ui.importData.compareSensors.CompareSensorsDialog;
import ui.mainScreen.MainScreen;
import ui.model.DialogLoading;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class Importer extends SwingWorker<Boolean, Void> {

    private final DialogLoading loadDialog = new DialogLoading(MainScreen.getInstance());
    private final Model model;
    private int stage = -1;
    private final File importFile;

    private final PathElementRepository departmentRepository = DepartmentRepositorySQLite.getInstance();
    private final PathElementRepository areaRepository = AreaRepositorySQLite.getInstance();
    private final PathElementRepository processRepository = ProcessRepositorySQLite.getInstance();
    private final PathElementRepository installationRepository = InstallationRepositorySQLite.getInstance();
    private final PersonRepository personRepository = PersonRepositorySQLite.getInstance();
    private final ControlPointsValuesRepository controlPointsValuesRepository = ControlPointsValuesRepositorySQLite.getInstance();
    private final CalibratorRepository calibratorRepository = CalibratorRepositorySQLite.getInstance();
    private final SensorRepository sensorRepository = SensorRepositorySQLite.getInstance();
    private final ChannelRepository channelRepository = ChannelRepositorySQLite.getInstance();

    private PathElementRepository importDepartmentRepository;
    private PathElementRepository importAreaRepository;
    private PathElementRepository importProcessRepository;
    private PathElementRepository importInstallationRepository;
    private PersonRepository importPersonRepository;
    private ControlPointsValuesRepository importControlPointsValuesRepository;
    private CalibratorRepository importCalibratorRepository;
    private SensorRepository importSensorRepository;
    private ChannelRepository importChannelRepository;

    private List<Calibrator>newCalibrators, calibratorsForChange, changedCalibrators;
    private List<Channel>newChannels, channelsForChange, changedChannels;
    private List<Sensor>newSensors, sensorsForChange, changedSensors;

    public Importer(File importFile, Model model) {
        super();
        this.importFile = importFile;
        String dbUrl = "jdbc:sqlite:" + importFile.getAbsolutePath();
        this.model = model;
        if (model == Model.ALL) this.stage = 0;
        initImportRepositories(dbUrl);
        EventQueue.invokeLater(() -> loadDialog.setVisible(true));
    }

    /*
    stage 0 - import departments, areas, processes, installations, persons, controlPointsValues, calibrators
    stage 1 - import sensors
    stage 2 - import channels
     */
    public Importer(File importFile, int stage) throws SQLException {
        super();
        this.importFile = importFile;
        this.stage = stage;
        String dbUrl = "jdbc:sqlite:" + importFile.getAbsolutePath();
        this.model = Model.ALL;
        initImportRepositories(dbUrl);
        EventQueue.invokeLater(() -> loadDialog.setVisible(true));
    }

    private void initImportRepositories(String dbUrl){
        importDepartmentRepository = new DepartmentRepositorySQLite(dbUrl, null, null);
        importAreaRepository = new AreaRepositorySQLite(dbUrl, null, null);
        importProcessRepository = new ProcessRepositorySQLite(dbUrl, null, null);
        importInstallationRepository = new InstallationRepositorySQLite(dbUrl, null, null);
        importPersonRepository = new PersonRepositorySQLite(dbUrl, null, null);
        importControlPointsValuesRepository = new ControlPointsValuesRepositorySQLite(dbUrl, null, null);
        importCalibratorRepository = new CalibratorRepositorySQLite(dbUrl, null, null);
        importSensorRepository = new SensorRepositorySQLite(dbUrl, null, null);
        importChannelRepository = new ChannelRepositorySQLite(dbUrl, null, null);
    }

    @Override
    protected Boolean doInBackground() throws Exception {
        switch (this.model) {
            case ALL:
                switch (stage){
                    case 0:
                        departmentRepository.add(importDepartmentRepository.getAll());
                        areaRepository.add(importAreaRepository.getAll());
                        processRepository.add(importProcessRepository.getAll());
                        installationRepository.add(importInstallationRepository.getAll());
                        List<Person> p = new ArrayList<>(importPersonRepository.getAll());
                        for (Person person : p) {
                            personRepository.set(person, person);
                            personRepository.add(person);
                        }
                        List<ControlPointsValues>points = new ArrayList<>(importControlPointsValuesRepository.getAll());
                        for (ControlPointsValues cpv : points){
                            controlPointsValuesRepository.add(cpv);
                        }
                        this.fuelingCalibratorsLists(importCalibratorRepository.getAll());
                        break;

                    case 1:
                        this.fuelingSensorsLists(importSensorRepository.getAll());
                        break;
                    case 2:
                        this.fuelingChannelsLists(importChannelRepository.getAll(), importSensorRepository.getAll());
                        break;
                }
                return true;

            case DEPARTMENT:
                departmentRepository.add(importDepartmentRepository.getAll());
                return true;

            case AREA:
                areaRepository.add(importAreaRepository.getAll());
                return true;

            case PROCESS:
                processRepository.add(importProcessRepository.getAll());
                return true;

            case INSTALLATION:
                installationRepository.add(importInstallationRepository.getAll());
                return true;

            case CALIBRATOR:
                this.fuelingCalibratorsLists(importCalibratorRepository.getAll());
                return true;

            case CHANNEL:
                this.fuelingChannelsLists(importChannelRepository.getAll(), importSensorRepository.getAll());
                return true;

            case PERSON:
                List<Person>persons = new ArrayList<>(importPersonRepository.getAll());
                for (Person person : persons){
                    personRepository.set(person, person);
                    personRepository.add(person);
                }
                return true;

            case SENSOR:
                this.fuelingSensorsLists(importSensorRepository.getAll());
                return true;

            case SENSOR_VALUE:
                List<ControlPointsValues>points = new ArrayList<>(importControlPointsValuesRepository.getAll());
                for (ControlPointsValues cpv : points){
                    controlPointsValuesRepository.add(cpv);
                }
                return true;
        }
        return false;
    }

    @Override
    protected void done() {
        this.loadDialog.dispose();
        try {
            if (this.get()){
                switch (this.model){
                    case DEPARTMENT:
                    case AREA:
                    case PROCESS:
                    case INSTALLATION:
                    case PERSON:
                    case SENSOR_VALUE:
                        JOptionPane.showMessageDialog(MainScreen.getInstance(), "Імпорт виконано успішно","Успіх", JOptionPane.INFORMATION_MESSAGE);
                        break;
                    case CALIBRATOR:
                        EventQueue.invokeLater(() -> new CompareCalibratorsDialog(newCalibrators, calibratorsForChange, changedCalibrators).setVisible(true));
                        break;
                    case CHANNEL:
                        EventQueue.invokeLater(() -> new CompareChannelsDialog(newChannels, channelsForChange, changedChannels,
                                newSensors, sensorsForChange).setVisible(true));
                        break;
                    case SENSOR:
                        EventQueue.invokeLater(() -> new CompareSensorsDialog(newSensors, sensorsForChange, changedSensors).setVisible(true));
                        break;
                    case ALL:
                        switch (stage){
                            case 0:
                                EventQueue.invokeLater(() -> new CompareCalibratorsDialog(newCalibrators, calibratorsForChange, changedCalibrators, importFile).setVisible(true));
                                break;
                            case 1:
                                EventQueue.invokeLater(() -> new CompareSensorsDialog(newSensors, sensorsForChange, changedSensors, importFile).setVisible(true));
                                break;
                            case 2:
                                EventQueue.invokeLater(() -> new CompareChannelsDialog(newChannels, channelsForChange, changedChannels,
                                        newSensors, sensorsForChange).setVisible(true));
                                break;
                        }
                        break;
                }
            }else {
                JOptionPane.showMessageDialog(MainScreen.getInstance(),
                        "Помилка при імпорті. Будь-ласка спробуйте ще.", "Помилка", JOptionPane.ERROR_MESSAGE);
            }
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(MainScreen.getInstance(),
                    "Помилка при імпорті. Будь-ласка спробуйте ще.", "Помилка", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void fuelingCalibratorsLists(Collection<Calibrator> importedCalibrators){
        ArrayList<Calibrator>oldList = new ArrayList<>(calibratorRepository.getAll());
        if (oldList.isEmpty()) {
            this.newCalibrators = new ArrayList<>(importedCalibrators);
        }else {
            ArrayList<Calibrator>newList = new ArrayList<>();
            ArrayList<Calibrator>changedList = new ArrayList<>();
            ArrayList<Calibrator>calibratorsForChange = new ArrayList<>();
            for (Calibrator newCalibrator : importedCalibrators){
                boolean exist = false;
                for (Calibrator oldCalibrator : oldList){
                    if (oldCalibrator.getName().equals(newCalibrator.getName())){
                        exist = true;
                        if (!oldCalibrator.isMatch(newCalibrator)) {
                            calibratorsForChange.add(newCalibrator);
                            changedList.add(oldCalibrator);
                        }
                        break;
                    }
                }
                if (!exist){
                    newList.add(newCalibrator);
                }
            }
            this.newCalibrators = newList;
            this.calibratorsForChange = calibratorsForChange;
            this.changedCalibrators = changedList;
        }
    }

    private void fuelingChannelsLists(Collection<Channel>importedChannels, Collection<Sensor>importedSensors){
        ArrayList<Sensor> oldSensors = new ArrayList<>(sensorRepository.getAll());
        if (oldSensors.isEmpty()) {
            this.newSensors = new ArrayList<>(importedSensors);
        } else {
            ArrayList<Sensor> newSensorsList = new ArrayList<>();
            ArrayList<Sensor> sensorsForChange = new ArrayList<>();
            for (Sensor newSensor : importedSensors) {
                boolean exist = false;
                for (Sensor oldSensor : oldSensors) {
                    if (oldSensor.getName().equals(newSensor.getName())) {
                        exist = true;
                        if (!oldSensor.isMatch(newSensor)){
                            sensorsForChange.add(newSensor);
                        }
                        break;
                    }
                }
                if (!exist) {
                    newSensorsList.add(newSensor);
                }
            }
            this.newSensors = newSensorsList;
            this.sensorsForChange = sensorsForChange;
        }

        ArrayList<Channel> oldList = new ArrayList<>(channelRepository.getAll());
        if (oldList.isEmpty()) {
            this.newChannels = new ArrayList<>(importedChannels);
            this.channelsForChange = new ArrayList<>();
        } else {
            ArrayList<Channel> newList = new ArrayList<>();
            ArrayList<Channel> changedList = new ArrayList<>();
            ArrayList<Channel> channelsForChange = new ArrayList<>();
            for (Channel newChannel : importedChannels) {
                boolean exist = false;
                for (Channel oldChannel : oldList) {
                    if (oldChannel.getCode().equals(newChannel.getCode())) {
                        exist = true;
                        if (!oldChannel.isMatch(newChannel)) {
                            changedList.add(oldChannel);
                            channelsForChange.add(newChannel);
                        }
                        break;
                    }
                }
                if (!exist) {
                    newList.add(newChannel);
                }
            }
            this.newChannels = newList;
            this.channelsForChange = channelsForChange;
            this.changedChannels = changedList;
        }
    }

    void fuelingSensorsLists(Collection<Sensor>importedSensors){
        ArrayList<Sensor>oldList = new ArrayList<>(sensorRepository.getAll());
        if (oldList.isEmpty()) {
            this.newSensors = new ArrayList<>(importedSensors);
        }else {
            ArrayList<Sensor>newList = new ArrayList<>();
            ArrayList<Sensor>changedList = new ArrayList<>();
            ArrayList<Sensor>sensorsForChange = new ArrayList<>();
            for (Sensor newSensor : importedSensors){
                boolean exist = false;
                for (Sensor oldSensor : oldList){
                    if (oldSensor.getName().equals(newSensor.getName())){
                        exist = true;

                        if (!oldSensor.isMatch(newSensor)){
                            sensorsForChange.add(newSensor);
                            changedList.add(oldSensor);
                        }
                        break;
                    }
                }
                if (!exist){
                    newList.add(newSensor);
                }
            }
            this.newSensors = newList;
            this.sensorsForChange = sensorsForChange;
            this.changedSensors = changedList;
        }
    }
}