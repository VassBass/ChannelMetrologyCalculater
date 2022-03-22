package backgroundTasks;

import application.Application;
import com.fasterxml.jackson.core.JsonProcessingException;
import converters.VariableConverter;
import model.*;
import org.sqlite.JDBC;
import support.Comparator;
import ui.importData.compareCalibrators.CompareCalibratorsDialog;
import ui.importData.compareChannels.CompareChannelsDialog;
import ui.importData.compareSensors.CompareSensorsDialog;
import ui.model.LoadDialog;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class Importer extends SwingWorker<Boolean, Void> {

    private final Connection connection;
    private final LoadDialog loadDialog = new LoadDialog(Application.context.mainScreen);
    private Model model;

    private ArrayList<Calibrator>newCalibrators, calibratorsForChange, changedCalibrators;
    private ArrayList<Channel>newChannels, channelsForChange, changedChannels;
    private ArrayList<Sensor>newSensors, sensorsForChange, changedSensors;

    public Importer(File importFile) throws SQLException {
        super();
        String dbUrl = "jdbc:sqlite:Support/" + importFile.getAbsolutePath();
        DriverManager.registerDriver(new JDBC());
        this.connection = DriverManager.getConnection(dbUrl);
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                loadDialog.setVisible(true);
            }
        });
    }

    public void importDepartment(){
        this.model = Model.DEPARTMENT;
        this.execute();
    }

    @Override
    protected Boolean doInBackground() throws Exception {
        try {
            switch (this.model) {
                default:
                    return true;
                case DEPARTMENT:
                    Application.context.departmentService.addInCurrentThread(this.getDepartments());
                    return true;
                case AREA:
                    Application.context.areaService.addInCurrentThread(this.getAreas());
                    return true;
                case PROCESS:
                    Application.context.processService.addInCurrentThread(this.getProcesses());
                    return true;
                case INSTALLATION:
                    Application.context.installationService.addInCurrentThread(this.getInstallations());
                    return true;
                case CALIBRATOR:
                    this.fuelingCalibratorsLists(this.getCalibrators());
                    return true;
                case CHANNEL:
                    this.fuelingChannelsLists(this.getChannels(), this.getSensors());
                    return true;
                case PERSON:
                    ArrayList<Person>persons = this.getPersons();
                    for (Person person : persons){
                        Application.context.personService.setInCurrentThread(person, person);
                        Application.context.personService.addInCurrentThread(person);
                    }
                    return true;
                case SENSOR:
                    this.fuelingSensorsLists(this.getSensors());
                    return true;
                case SENSOR_VALUE:
                    ArrayList<ControlPointsValues>points = this.getControlPoints();
                    for (ControlPointsValues cpv : points){
                        Application.context.controlPointsValuesService.putInCurrentThread(cpv);
                    }
                    return true;
            }
        }catch (SQLException | JsonProcessingException ex){
            ex.printStackTrace();
            return false;
        }
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
                        JOptionPane.showMessageDialog(Application.context.mainScreen,
                                "Імпорт виконано успішно","Успіх", JOptionPane.INFORMATION_MESSAGE);
                        break;
                    case CALIBRATOR:
                        EventQueue.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                new CompareCalibratorsDialog(newCalibrators, calibratorsForChange, changedCalibrators).setVisible(true);
                            }
                        });
                        break;
                    case CHANNEL:
                        EventQueue.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                new CompareChannelsDialog(newChannels, channelsForChange, changedChannels,
                                        newSensors, sensorsForChange).setVisible(true);
                            }
                        });
                        break;
                    case SENSOR:
                        EventQueue.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                new CompareSensorsDialog(newSensors, sensorsForChange, changedSensors).setVisible(true);
                            }
                        });
                }
            }else {
                JOptionPane.showMessageDialog(Application.context.mainScreen,
                        "Помилка при імпорті. Будь-ласка спробуйте ще.", "Помилка", JOptionPane.ERROR_MESSAGE);
            }
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(Application.context.mainScreen,
                    "Помилка при імпорті. Будь-ласка спробуйте ще.", "Помилка", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void fuelingCalibratorsLists(ArrayList<Calibrator>importedCalibrators){
        ArrayList<Calibrator>oldList = Application.context.calibratorService.getAll();
        if (oldList == null || oldList.isEmpty()) {
            this.newCalibrators = importedCalibrators;
        }else {
            ArrayList<Calibrator>newList = new ArrayList<>();
            ArrayList<Calibrator>changedList = new ArrayList<>();
            ArrayList<Calibrator>calibratorsForChange = new ArrayList<>();
            for (Calibrator newCalibrator : importedCalibrators){
                boolean exist = false;
                for (Calibrator oldCalibrator : oldList){
                    if (oldCalibrator.getName().equals(newCalibrator.getName())){
                        exist = true;
                        if (!Comparator.calibratorsMatch(oldCalibrator, newCalibrator)) {
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

    private void fuelingChannelsLists(ArrayList<Channel>importedChannels, ArrayList<Sensor>importedSensors){
        ArrayList<Sensor> oldSensors = Application.context.sensorService.getAll();
        if (oldSensors.isEmpty()) {
            this.newSensors = importedSensors;
        } else {
            ArrayList<Sensor> newSensorsList = new ArrayList<>();
            ArrayList<Sensor> sensorsForChange = new ArrayList<>();
            for (Sensor newSensor : importedSensors) {
                boolean exist = false;
                for (Sensor oldSensor : oldSensors) {
                    if (oldSensor.getName().equals(newSensor.getName())) {
                        exist = true;
                        if (!Comparator.sensorsMatch(newSensor, oldSensor)) {
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

        ArrayList<Channel> oldList = Application.context.channelService.getAll();
        if (oldList == null || oldList.isEmpty()) {
            this.newChannels = importedChannels;
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
                        if (!Comparator.channelsMatch(newChannel, oldChannel)) {
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

    void fuelingSensorsLists(ArrayList<Sensor>importedSensors){
        ArrayList<Sensor>oldList = Application.context.sensorService.getAll();
        if (oldList == null || oldList.isEmpty()) {
            this.newSensors = importedSensors;
        }else {
            ArrayList<Sensor>newList = new ArrayList<>();
            ArrayList<Sensor>changedList = new ArrayList<>();
            ArrayList<Sensor>sensorsForChange = new ArrayList<>();
            for (Sensor newSensor : importedSensors){
                boolean exist = false;
                for (Sensor oldSensor : oldList){
                    if (oldSensor.getName().equals(newSensor.getName())){
                        exist = true;
                        if (!Comparator.sensorsMatch(oldSensor, newSensor)) {
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

    private ArrayList<String> getDepartments() throws SQLException {
        ArrayList<String>list = new ArrayList<>();
        Statement statement = this.connection.createStatement();
        String sql = "SELECT * FROM departments";
        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next()){
            list.add(resultSet.getString("department"));
        }
        return list;
    }

    private ArrayList<String> getAreas() throws SQLException {
        ArrayList<String>list = new ArrayList<>();
        Statement statement = this.connection.createStatement();
        String sql = "SELECT * FROM areas";
        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next()){
            list.add(resultSet.getString("area"));
        }
        return list;
    }

    private ArrayList<String> getProcesses() throws SQLException {
        ArrayList<String>list = new ArrayList<>();
        Statement statement = this.connection.createStatement();
        String sql = "SELECT * FROM processes";
        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next()){
            list.add(resultSet.getString("process"));
        }
        return list;
    }

    private ArrayList<String> getInstallations() throws SQLException {
        ArrayList<String>list = new ArrayList<>();
        Statement statement = this.connection.createStatement();
        String sql = "SELECT * FROM installations";
        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next()){
            list.add(resultSet.getString("installation"));
        }
        return list;
    }

    private ArrayList<Calibrator> getCalibrators() throws SQLException, JsonProcessingException {
        ArrayList<Calibrator>list = new ArrayList<>();
        Statement statement = this.connection.createStatement();
        String sql = "SELECT * FROM calibrators";
        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next()){
            Calibrator calibrator = new Calibrator();
            calibrator.setName(resultSet.getString("name"));
            calibrator.setType(resultSet.getString("type"));
            calibrator.setNumber(resultSet.getString("number"));
            calibrator.setMeasurement(resultSet.getString("measurement"));
            calibrator.setValue(resultSet.getString("value"));
            calibrator.setErrorFormula(resultSet.getString("error_formula"));
            String certificateString = resultSet.getString("certificate");
            calibrator.setCertificate(Calibrator.Certificate.fromString(certificateString));
            calibrator.setRangeMin(resultSet.getDouble("range_min"));
            calibrator.setRangeMax(resultSet.getDouble("range_max"));
            list.add(calibrator);
        }
        return list;
    }

    private ArrayList<Channel> getChannels() throws SQLException, JsonProcessingException {
        ArrayList<Channel>list = new ArrayList<>();
        Statement statement = this.connection.createStatement();
        String sql = "SELECT * FROM channels";
        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next()){
            Channel channel = new Channel();
            channel.setCode(resultSet.getString("code"));
            channel.setName(resultSet.getString("name"));
            channel.setDepartment(resultSet.getString("department"));
            channel.setArea(resultSet.getString("area"));
            channel.setProcess(resultSet.getString("process"));
            channel.setInstallation(resultSet.getString("installation"));
            channel.setTechnologyNumber(resultSet.getString("technology_number"));
            channel.setNumberOfProtocol(resultSet.getString("protocol_number"));
            channel.setReference(resultSet.getString("reference"));
            channel.setDate(VariableConverter.stringToDate(resultSet.getString("date")));
            channel.setSuitability(Boolean.parseBoolean(resultSet.getString("suitability")));
            channel.setMeasurement(Measurement.fromString(resultSet.getString("measurement")));
            channel.setSensor(Sensor.fromString(resultSet.getString("sensor")));
            channel.setFrequency(resultSet.getDouble("frequency"));
            channel.setRangeMin(resultSet.getDouble("range_min"));
            channel.setRangeMax(resultSet.getDouble("range_max"));
            double allowableErrorPercent = resultSet.getDouble("allowable_error_percent");
            double allowableErrorValue = resultSet.getDouble("allowable_error_value");
            channel.setAllowableError(allowableErrorPercent, allowableErrorValue);
            list.add(channel);
        }
        return list;
    }

    private ArrayList<ControlPointsValues> getControlPoints() throws SQLException {
        ArrayList<ControlPointsValues>list = new ArrayList<>();
        Statement statement = this.connection.createStatement();
        String sql = "SELECT * FROM control_points";
        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next()){
            ControlPointsValues cpv = new ControlPointsValues();
            cpv.setId(resultSet.getInt("id"));
            cpv.setSensorType(resultSet.getString("sensor_type"));
            cpv.setValues(VariableConverter.stringToArray(resultSet.getString("points")));
            cpv.setRangeMin(resultSet.getDouble("range_min"));
            cpv.setRangeMax(resultSet.getDouble("range_max"));
            list.add(cpv);
        }
        return list;
    }

    private ArrayList<Person> getPersons() throws SQLException {
        ArrayList<Person>list = new ArrayList<>();
        Statement statement = this.connection.createStatement();
        String sql = "SELECT * FROM persons";
        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next()){
            Person person = new Person();
            person.setId(resultSet.getInt("id"));
            person.setName(resultSet.getString("name"));
            person.setSurname(resultSet.getString("surname"));
            person.setPatronymic(resultSet.getString("patronymic"));
            person.setPosition(resultSet.getString("position"));
            list.add(person);
        }
        return list;
    }

    private ArrayList<Sensor> getSensors() throws SQLException {
        ArrayList<Sensor>list = new ArrayList<>();
        Statement statement = this.connection.createStatement();
        String sql = "SELECT * FROM sensors";
        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next()){
            Sensor sensor = new Sensor();
            sensor.setName(resultSet.getString("name"));
            sensor.setType(resultSet.getString("type"));
            sensor.setNumber(resultSet.getString("number"));
            sensor.setMeasurement(resultSet.getString("measurement"));
            sensor.setValue(resultSet.getString("value"));
            sensor.setErrorFormula(resultSet.getString("error_formula"));
            sensor.setRangeMin(resultSet.getDouble("range_min"));
            sensor.setRangeMax(resultSet.getDouble("range_max"));
            list.add(sensor);
        }
        return list;
    }
}