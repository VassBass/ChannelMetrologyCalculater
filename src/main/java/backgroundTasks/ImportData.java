package backgroundTasks;

import constants.MeasurementConstants;
import constants.Value;
import measurements.Measurement;
import support.*;
import ui.LoadDialog;
import ui.importData.compareSensors.CompareSensorsDialog;
import ui.main.MainScreen;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

@SuppressWarnings("unchecked")
public class ImportData extends SwingWorker<Void, Void> {
    private final MainScreen mainScreen;
    private final File importDataFile;
    private final LoadDialog loadDialog;

    private ArrayList<ArrayList<Values>>data;

    private final ArrayList<Channel>oldChannels;
    private ArrayList<Channel>importedChannels;
    private final ArrayList<Channel>newChannelsList;
    private final ArrayList<Integer[]>channelsIndexes;

    private ArrayList<Sensor>importedSensors;
    private final ArrayList<Sensor>oldSensors;
    private final ArrayList<Sensor>newSensorsList;
    private final ArrayList<Integer[]>sensorsIndexes;

    private ArrayList<Worker>importedPersons;
    private final ArrayList<Worker>oldPersons;
    private final ArrayList<Worker>newPersonsList;
    private final ArrayList<Integer[]>personsIndexes;

    private ArrayList<Calibrator>importedCalibrators;
    private final ArrayList<Calibrator>oldCalibrators;
    private final ArrayList<Calibrator>newCalibratorsList;
    private final ArrayList<Integer[]>calibratorsIndexes;

    private final ArrayList<String>newDepartmentsList, newAreasList, newProcessesList, newInstallationsList;

    public ImportData(final MainScreen mainScreen, File importDataFile){
        super();
        this.mainScreen = mainScreen;
        this.importDataFile = importDataFile;

        this.oldSensors = Lists.sensors();
        this.newSensorsList = new ArrayList<>();
        this.sensorsIndexes = new ArrayList<>();

        this.oldChannels = Lists.channels();
        this.newChannelsList = new ArrayList<>();
        this.channelsIndexes = new ArrayList<>();

        this.oldPersons = Lists.persons();
        this.newPersonsList = new ArrayList<>();
        this.personsIndexes = new ArrayList<>();

        this.oldCalibrators = Lists.calibrators();
        this.newCalibratorsList = new ArrayList<>();
        this.calibratorsIndexes = new ArrayList<>();

        this.newDepartmentsList = new ArrayList<>();
        this.newAreasList = new ArrayList<>();
        this.newProcessesList = new ArrayList<>();
        this.newInstallationsList = new ArrayList<>();

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
        this.data = this.dataExtraction();
        this.importedSensors = this.sensorsExtraction();
        this.importedChannels = this.channelsExtraction();
        this.importedPersons = this.personsExtraction();
        this.importedCalibrators = this.calibratorsExtraction();

        this.copySensors();
        this.copyChannels();
        this.copyPersons();
        this.copyCalibrators();
        this.copyPathElements();

        return null;
    }

    @Override
    protected void done() {
        this.loadDialog.dispose();
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new CompareSensorsDialog(mainScreen,
                        newSensorsList, importedSensors, sensorsIndexes,
                        newChannelsList,importedChannels, channelsIndexes,
                        newPersonsList, importedPersons, personsIndexes,
                        newCalibratorsList, importedCalibrators, calibratorsIndexes,
                        newDepartmentsList, newAreasList, newProcessesList, newInstallationsList);
            }
        });
    }

    private ArrayList<ArrayList<Values>>dataExtraction(){
        ArrayList<ArrayList<Values>>data;
        try {
            ObjectInputStream oos = new ObjectInputStream(new FileInputStream(this.importDataFile));
            data = (ArrayList<ArrayList<Values>>) oos.readObject();
            oos.close();
        }catch (Exception ex){
            ex.printStackTrace();
            return null;
        }
        return data;
    }

    private ArrayList<Channel>channelsExtraction(){
        ArrayList<Channel>channels = new ArrayList<>();
        ArrayList<Values>data = new ArrayList<>();
        for (ArrayList<Values> datum : this.data) {
            if (datum.get(0).getStringValue(Value.DATA_TYPE).equals(Value.CHANNEL)) {
                data = datum;
                break;
            }
        }

        for (int x = 1; x< Objects.requireNonNull(data).size(); x++) {
            Values channelData = data.get(x);
            Channel channel = new Channel();

            channel.setCode(channelData.getStringValue(Value.CHANNEL_CODE));
            channel.setName(channelData.getStringValue(Value.CHANNEL_NAME));
            channel.setDepartment(channelData.getStringValue(Value.CHANNEL_DEPARTMENT));
            channel.setArea(channelData.getStringValue(Value.CHANNEL_AREA));
            channel.setProcess(channelData.getStringValue(Value.CHANNEL_PROCESS));
            channel.setInstallation(channelData.getStringValue(Value.CHANNEL_INSTALLATION));
            channel.setDate((Calendar) channelData.getValue(Value.CHANNEL_DATE));
            channel.setFrequency(channelData.getDoubleValue(Value.CHANNEL_FREQUENCY));
            channel.setTechnologyNumber(channelData.getStringValue(Value.CHANNEL_TECHNOLOGY_NUMBER));
            channel.setNumberOfProtocol(channelData.getStringValue(Value.CHANNEL_PROTOCOL_NUMBER));
            channel.setReference(channelData.getStringValue(Value.CHANNEL_REFERENCE));
            channel.setRangeMin(channelData.getDoubleValue(Value.CHANNEL_RANGE_MIN));
            channel.setRangeMax(channelData.getDoubleValue(Value.CHANNEL_RANGE_MAX));
            channel.setAllowableError(channelData.getDoubleValue(Value.CHANNEL_ALLOWABLE_ERROR_PERCENT),
                    channelData.getDoubleValue(Value.CHANNEL_ALLOWABLE_ERROR_VALUE));
            channel.isGood = channelData.getBooleanValue(Value.CHANNEL_IS_GOOD);

            MeasurementConstants measurementName = MeasurementConstants.getConstantFromString(channelData.getStringValue(Value.MEASUREMENT_NAME));
            MeasurementConstants measurementValue = MeasurementConstants.getConstantFromString(channelData.getStringValue(Value.MEASUREMENT_VALUE));
            Measurement measurement = new Measurement(measurementName, measurementValue);
            channel.setMeasurement(measurement);

            Sensor sensor = new Sensor();
            sensor.setType(Value.SENSOR_TYPE);
            sensor.setName(Value.SENSOR_NAME);
            sensor.setRangeMin(channelData.getDoubleValue(Value.SENSOR_RANGE_MIN));
            sensor.setRangeMax(channelData.getDoubleValue(Value.SENSOR_RANGE_MAX));
            sensor.setNumber(channelData.getStringValue(Value.SENSOR_NUMBER));
            sensor.setValue(channelData.getStringValue(Value.SENSOR_VALUE));
            sensor.setMeasurement(channelData.getStringValue(Value.SENSOR_MEASUREMENT));
            sensor.setErrorFormula(Value.SENSOR_ERROR_FORMULA);
            channel.setSensor(sensor);

            channels.add(channel);
        }
        return channels;
    }

    private ArrayList<Sensor>sensorsExtraction(){
        ArrayList<Sensor>sensors = new ArrayList<>();
        ArrayList<Values>data = new ArrayList<>();
        for (ArrayList<Values> datum : this.data) {
            if (datum.get(0).getStringValue(Value.DATA_TYPE).equals(Value.SENSOR)) {
                data = datum;
                break;
            }
        }

        for (int x = 1; x< Objects.requireNonNull(data).size(); x++){
            Values sensorData = data.get(x);
            Sensor sensor = new Sensor();

            sensor.setType(sensorData.getStringValue(Value.SENSOR_TYPE));
            sensor.setName(sensorData.getStringValue(Value.SENSOR_NAME));
            sensor.setRangeMin(sensorData.getDoubleValue(Value.SENSOR_RANGE_MIN));
            sensor.setRangeMax(sensorData.getDoubleValue(Value.SENSOR_RANGE_MAX));
            sensor.setNumber(sensorData.getStringValue(Value.SENSOR_NUMBER));
            sensor.setValue(sensorData.getStringValue(Value.SENSOR_VALUE));
            sensor.setMeasurement(sensorData.getStringValue(Value.SENSOR_MEASUREMENT));
            sensor.setErrorFormula(sensorData.getStringValue(Value.SENSOR_ERROR_FORMULA));

            sensors.add(sensor);
        }
        return sensors;
    }

    private ArrayList<Worker>personsExtraction(){
        ArrayList<Worker>persons = new ArrayList<>();
        ArrayList<Values>data = new ArrayList<>();
        for (ArrayList<Values> datum : this.data) {
            if (datum.get(0).getStringValue(Value.DATA_TYPE).equals(Value.PERSON)) {
                data = datum;
                break;
            }
        }

        for (int x = 1; x< Objects.requireNonNull(data).size(); x++){
            Values personData = data.get(x);
            Worker person = new Worker();

            person.setName(personData.getStringValue(Value.PERSON_NAME));
            person.setSurname(personData.getStringValue(Value.PERSON_SURNAME));
            person.setPatronymic(personData.getStringValue(Value.PERSON_PATRONYMIC));
            person.setPosition(personData.getStringValue(Value.PERSON_POSITION));

            persons.add(person);
        }
        return persons;
    }

    private ArrayList<Calibrator>calibratorsExtraction(){
        ArrayList<Calibrator>calibrators = new ArrayList<>();
        ArrayList<Values>data = new ArrayList<>();
        for (ArrayList<Values> datum : this.data) {
            if (datum.get(0).getStringValue(Value.DATA_TYPE).equals(Value.CALIBRATOR)) {
                data = datum;
                break;
            }
        }
        for (int x = 1; x< Objects.requireNonNull(data).size(); x++){
            Values calibratorData = data.get(x);
            Calibrator calibrator = new Calibrator();

            calibrator.setType(calibratorData.getStringValue(Value.CALIBRATOR_TYPE));
            calibrator.setName(calibratorData.getStringValue(Value.CALIBRATOR_NAME));
            calibrator.setNumber(calibratorData.getStringValue(Value.CALIBRATOR_NUMBER));
            calibrator.setMeasurement(calibratorData.getStringValue(Value.CALIBRATOR_MEASUREMENT));
            calibrator.setRangeMin(calibratorData.getDoubleValue(Value.CALIBRATOR_RANGE_MIN));
            calibrator.setRangeMax(calibratorData.getDoubleValue(Value.CALIBRATOR_RANGE_MAX));
            calibrator.setValue(calibratorData.getStringValue(Value.CALIBRATOR_VALUE));
            calibrator.setErrorFormula(calibratorData.getStringValue(Value.CALIBRATOR_ERROR));
            calibrator.setCertificateName(calibratorData.getStringValue(Value.CALIBRATOR_CERTIFICATE_NAME));
            calibrator.setCertificateDate((Calendar) calibratorData.getValue(Value.CALIBRATOR_CERTIFICATE_DATE));
            calibrator.setCertificateCompany(calibratorData.getStringValue(Value.CALIBRATOR_CERTIFICATE_COMPANY));

            calibrators.add(calibrator);
        }
        return calibrators;
    }

    private void copySensors(){
        for (int imp=0;imp<this.importedSensors.size();imp++){
            boolean exist = false;
            for (int old=0;old<this.oldSensors.size();old++){
                if (this.importedSensors.get(imp).getName().equals(this.oldSensors.get(old).getName())){
                    exist = true;
                    /*if (this.importedSensors.get(imp).matchingFields(this.oldSensors.get(old))){
                        this.newSensorsList.add(this.importedSensors.get(imp));
                    }else {
                        this.sensorsIndexes.add(new Integer[]{old, imp});
                    }*/
                    break;
                }
            }
            if (!exist){
                this.newSensorsList.add(this.importedSensors.get(imp));
            }
        }
    }

    private void copyChannels(){
        for (int imp=0;imp<this.importedChannels.size();imp++){
            boolean exist = false;
            for (int old=0;old<this.oldChannels.size();old++){
                if (this.oldChannels.get(old).getCode().equals(this.importedChannels.get(imp).getCode())){
                    exist = true;
                    if (this.oldChannels.get(old).equals(this.importedChannels.get(imp))){
                        this.newChannelsList.add(this.oldChannels.get(old));
                    }else {
                        this.channelsIndexes.add(new Integer[]{old, imp});
                    }
                    break;
                }
            }
            if (!exist){
                this.newChannelsList.add(this.importedChannels.get(imp));
            }
        }
    }

    private void copyPersons(){
        for (int imp=0;imp<this.importedPersons.size();imp++){
            boolean exist = false;
            for (int old=0;old<this.oldPersons.size();old++){
                if (this.importedPersons.get(imp).equalsPerson(this.oldPersons.get(old))){
                    exist = true;
                    if (this.importedPersons.get(imp).getPosition().equals(this.oldPersons.get(old).getPosition())){
                        this.newPersonsList.add(this.oldPersons.get(old));
                    }else {
                        this.personsIndexes.add(new Integer[]{old, imp});
                    }
                    break;
                }
            }
            if (!exist){
                this.newPersonsList.add(this.importedPersons.get(imp));
            }
        }
    }

    private void copyCalibrators(){
        for (int imp=0;imp<this.importedCalibrators.size();imp++){
            boolean exist = false;
            for (int old=0;old<this.oldCalibrators.size();old++){
                if (this.oldCalibrators.get(old).getName().equals(this.importedCalibrators.get(imp).getName())){
                    exist = true;
                    if (this.oldCalibrators.get(old).equals(this.importedCalibrators.get(imp))){
                        this.newCalibratorsList.add(this.oldCalibrators.get(old));
                    }else {
                        this.calibratorsIndexes.add(new Integer[]{old, imp});
                    }
                    break;
                }
            }
            if (!exist){
                this.newCalibratorsList.add(this.importedCalibrators.get(imp));
            }
        }
    }

    private void copyPathElements(){
        ArrayList<String>old = Lists.departments();
        ArrayList<String>imported = new ArrayList<>();
        ArrayList<Values>data = new ArrayList<>();
        for (ArrayList<Values> datum : this.data) {
            if (datum.get(0).getStringValue(Value.DATA_TYPE).equals(Value.DEPARTMENT)) {
                data = datum;
                break;
            }
        }

        for (int x = 1; x< Objects.requireNonNull(data).size(); x++){
            Values department = data.get(x);
            imported.add(department.getStringValue(Value.CHANNEL_DEPARTMENT));
        }
        for (String imp : imported){
            boolean exist = false;
            for (String o : Objects.requireNonNull(old)){
                if (imp.equals(o)){
                    exist = true;
                    this.newDepartmentsList.add(o);
                }
            }
            if (!exist){
                this.newDepartmentsList.add(imp);
            }
        }

        imported.clear();
        old = Lists.areas();
        for (ArrayList<Values> datum : this.data) {
            if (datum.get(0).getStringValue(Value.DATA_TYPE).equals(Value.AREA)) {
                data = datum;
                break;
            }
        }
        for (int x=1;x<data.size();x++){
            Values area = data.get(x);
            imported.add(area.getStringValue(Value.CHANNEL_AREA));
        }
        for (String imp : imported){
            boolean exist = false;
            for (String o : Objects.requireNonNull(old)){
                if (imp.equals(o)){
                    exist = true;
                    this.newAreasList.add(o);
                }
            }
            if (!exist){
                this.newAreasList.add(imp);
            }
        }

        imported.clear();
        old = Lists.processes();
        for (ArrayList<Values> datum : this.data) {
            if (datum.get(0).getStringValue(Value.DATA_TYPE).equals(Value.PROCESS)) {
                data = datum;
                break;
            }
        }
        for (int x=1;x<data.size();x++){
            Values process = data.get(x);
            imported.add(process.getStringValue(Value.CHANNEL_PROCESS));
        }
        for (String imp : imported){
            boolean exist = false;
            for (String o : Objects.requireNonNull(old)){
                if (imp.equals(o)){
                    exist = true;
                    this.newProcessesList.add(o);
                }
            }
            if (!exist){
                this.newProcessesList.add(imp);
            }
        }

        imported.clear();
        old = Lists.installations();
        for (ArrayList<Values> datum : this.data) {
            if (datum.get(0).getStringValue(Value.DATA_TYPE).equals(Value.INSTALLATION)) {
                data = datum;
                break;
            }
        }
        for (int x=1;x<data.size();x++){
            Values installation = data.get(x);
            imported.add(installation.getStringValue(Value.CHANNEL_INSTALLATION));
        }
        for (String imp : imported){
            boolean exist = false;
            for (String o : Objects.requireNonNull(old)){
                if (imp.equals(o)){
                    exist = true;
                    this.newInstallationsList.add(o);
                }
            }
            if (!exist){
                this.newInstallationsList.add(imp);
            }
        }
    }
}
