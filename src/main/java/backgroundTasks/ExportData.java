package backgroundTasks;

import constants.Files;
import constants.Strings;
import constants.Value;
import support.*;
import ui.LoadDialog;
import ui.main.MainScreen;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

public class ExportData extends SwingWorker<Void, Void>{
    private final MainScreen mainScreen;
    private final ArrayList<Channel> channels;
    private final LoadDialog loadDialog;

    private int channelsNumber = 0;

    public ExportData(MainScreen mainScreen){
        super();
        this.mainScreen = mainScreen;
        this.channels = Lists.channels();
        if (this.channels != null) {
            this.channelsNumber = this.channels.size();
        }

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
        this.saveData(this.createExportData());
        return null;
    }

    @Override
    protected void done() {
        this.loadDialog.dispose();
        JOptionPane.showMessageDialog(this.mainScreen, Strings.EXPORT_SUCCESS, Strings.EXPORT, JOptionPane.INFORMATION_MESSAGE);
    }

    /*
     * [0] = Channels list
     * [1] = Sensors list
     * [2] = Persons list
     * [3] = Departments list
     * [4] = Areas list
     * [5] = Processes list
     * [6] = Installations list
     * [7] = Calibrators list
     */
    private ArrayList<ArrayList<Values>> createExportData(){
        ArrayList<ArrayList<Values>>data = new ArrayList<>();

        ArrayList<Values>channels = new ArrayList<>();
        for (int x=0;x<this.channelsNumber;x++){
            Channel channel = this.channels.get(x);
            Values channelData = new Values();

            //Channel info
            channelData.putValue(Value.CHANNEL_CODE, channel.getCode());//String
            channelData.putValue(Value.CHANNEL_NAME, channel.getName());//String
            channelData.putValue(Value.CHANNEL_DEPARTMENT, channel.getDepartment());//String
            channelData.putValue(Value.CHANNEL_AREA, channel.getArea());//String
            channelData.putValue(Value.CHANNEL_PROCESS, channel.getProcess());//String
            channelData.putValue(Value.CHANNEL_INSTALLATION, channel.getInstallation());//String
            channelData.putValue(Value.CHANNEL_DATE, channel.getDate());//Calendar
            channelData.putValue(Value.CHANNEL_FREQUENCY, channel.getFrequency());//double
            channelData.putValue(Value.CHANNEL_TECHNOLOGY_NUMBER, channel.getTechnologyNumber());//String
            channelData.putValue(Value.CHANNEL_PROTOCOL_NUMBER, channel.getNumberOfProtocol());//String
            channelData.putValue(Value.CHANNEL_REFERENCE, channel.getReference());//String
            channelData.putValue(Value.CHANNEL_RANGE_MIN, channel.getRangeMin());//double
            channelData.putValue(Value.CHANNEL_RANGE_MAX, channel.getRangeMax());//double
            channelData.putValue(Value.CHANNEL_ALLOWABLE_ERROR_PERCENT, channel.getAllowableErrorPercent());//double
            channelData.putValue(Value.CHANNEL_ALLOWABLE_ERROR_VALUE, channel.getAllowableError());//double
            channelData.putValue(Value.CHANNEL_IS_GOOD, channel.isGood);//boolean

            //Measurement info
            channelData.putValue(Value.MEASUREMENT_NAME, channel.getMeasurement().getName());//String
            channelData.putValue(Value.MEASUREMENT_VALUE, channel.getMeasurement().getValue());//String

            //Sensor info
            channelData.putValue(Value.SENSOR_TYPE, channel.getSensor().getType());//String
            channelData.putValue(Value.SENSOR_NAME, channel.getSensor().getName());//String
            channelData.putValue(Value.SENSOR_RANGE_MIN, channel.getSensor().getRangeMin());//double
            channelData.putValue(Value.SENSOR_RANGE_MAX, channel.getSensor().getRangeMax());//double
            channelData.putValue(Value.SENSOR_NUMBER, channel.getSensor().getNumber());//String
            channelData.putValue(Value.SENSOR_VALUE, channel.getSensor().getValue());//String
            channelData.putValue(Value.SENSOR_MEASUREMENT, channel.getSensor().getMeasurement());//String
            channelData.putValue(Value.SENSOR_ERROR_FORMULA, channel.getSensor().getErrorFormula());//String

            channels.add(channelData);
        }

        ArrayList<Values>sensors = new ArrayList<>();
        ArrayList<Sensor>sensorsList = Lists.sensors();
        for (Sensor sensor : Objects.requireNonNull(sensorsList)){
            Values sensorData = new Values();

            sensorData.putValue(Value.SENSOR_TYPE, sensor.getType());//String
            sensorData.putValue(Value.SENSOR_NAME, sensor.getName());//String
            sensorData.putValue(Value.SENSOR_RANGE_MIN, sensor.getRangeMin());//double
            sensorData.putValue(Value.SENSOR_RANGE_MAX, sensor.getRangeMax());//double
            sensorData.putValue(Value.SENSOR_NUMBER, sensor.getNumber());//String
            sensorData.putValue(Value.SENSOR_VALUE, sensor.getValue());//String
            sensorData.putValue(Value.SENSOR_MEASUREMENT, sensor.getMeasurement());//String
            sensorData.putValue(Value.SENSOR_ERROR_FORMULA, sensor.getErrorFormula());//String

            sensors.add(sensorData);
        }

        ArrayList<Values>persons = new ArrayList<>();
        ArrayList<Worker>personsList = Lists.persons();
        for (Worker person : Objects.requireNonNull(personsList)){
            Values personData = new Values();

            personData.putValue(Value.PERSON_NAME, person.getName());//String
            personData.putValue(Value.PERSON_SURNAME, person.getSurname());//String
            personData.putValue(Value.PERSON_PATRONYMIC, person.getPatronymic());//String
            personData.putValue(Value.PERSON_POSITION, person.getPosition());//String

            persons.add(personData);
        }

        ArrayList<Values>departments = new ArrayList<>();
        ArrayList<String>departmentsList = Lists.departments();
        for (String department : Objects.requireNonNull(departmentsList)){
            Values departmentData = new Values();
            departmentData.putValue(Value.CHANNEL_DEPARTMENT, department);//String
            departments.add(departmentData);
        }

        ArrayList<Values>areas = new ArrayList<>();
        ArrayList<String>areasList = Lists.areas();
        for (String area : Objects.requireNonNull(areasList)){
            Values areaData = new Values();
            areaData.putValue(Value.CHANNEL_AREA, area);//String
            areas.add(areaData);
        }

        ArrayList<Values>processes = new ArrayList<>();
        ArrayList<String>processesList = Lists.processes();
        for (String process : Objects.requireNonNull(processesList)){
            Values processData = new Values();
            processData.putValue(Value.CHANNEL_PROCESS, process);//String
            processes.add(processData);
        }

        ArrayList<Values>installations = new ArrayList<>();
        ArrayList<String>installationsList = Lists.installations();
        for (String installation : Objects.requireNonNull(installationsList)){
            Values installationData = new Values();
            installationData.putValue(Value.CHANNEL_INSTALLATION, installation);//String
            installations.add(installationData);
        }

        ArrayList<Values>calibrators = new ArrayList<>();
        ArrayList<Calibrator>calibratorsList = Lists.calibrators();
        for (Calibrator calibrator : Objects.requireNonNull(calibratorsList)){
            Values calibratorData = new Values();

            calibratorData.putValue(Value.CALIBRATOR_TYPE, calibrator.getType());//String
            calibratorData.putValue(Value.CALIBRATOR_NAME, calibrator.getName());//String
            calibratorData.putValue(Value.CALIBRATOR_NUMBER, calibrator.getNumber());//String
            calibratorData.putValue(Value.CALIBRATOR_MEASUREMENT, calibrator.getMeasurement());//String
            calibratorData.putValue(Value.CALIBRATOR_RANGE_MIN, calibrator.getRangeMin());//double
            calibratorData.putValue(Value.CALIBRATOR_RANGE_MAX, calibrator.getRangeMax());//double
            calibratorData.putValue(Value.CALIBRATOR_VALUE, calibrator.getValue());//String
            calibratorData.putValue(Value.CALIBRATOR_ERROR, calibrator.getErrorFormula());//String
            calibratorData.putValue(Value.CALIBRATOR_CERTIFICATE_NAME, calibrator.getCertificateName());//String
            calibratorData.putValue(Value.CALIBRATOR_CERTIFICATE_DATE, calibrator.getCertificateDate());//String
            calibratorData.putValue(Value.CALIBRATOR_CERTIFICATE_COMPANY, calibrator.getCertificateCompany());//String

            calibrators.add(calibratorData);
        }

        data.add(channels);     // 0
        data.add(sensors);      // 1
        data.add(persons);      // 2
        data.add(departments);  // 3
        data.add(areas);        // 4
        data.add(processes);    // 5
        data.add(installations);// 6
        data.add(calibrators);  // 7
        return data;
    }

    private void saveData(ArrayList<ArrayList<Values>>data){
        try {
            File file = new File(Files.EXPORT_DIR, Strings.FILE_NAME_EXPORTED_DATA(Calendar.getInstance()));
            if (!file.exists()){
                if (!file.createNewFile()){
                    JOptionPane.showMessageDialog(mainScreen, Strings.ERROR, "Файл експорту не вдалось створити", JOptionPane.ERROR_MESSAGE);
                }
            }
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));
            out.writeObject(data);
            out.close();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
