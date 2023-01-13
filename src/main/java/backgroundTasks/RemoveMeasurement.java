package backgroundTasks;

import model.Measurement;
import repository.CalibratorRepository;
import service.repository.repos.channel.ChannelRepository;
import service.repository.repos.measurement.MeasurementRepository;
import service.repository.repos.sensor.SensorRepository;
import repository.impl.CalibratorRepositorySQLite;
import service.repository.repos.channel.ChannelRepositorySQLite;
import service.repository.repos.measurement.MeasurementRepositorySQLite;
import service.repository.repos.sensor.SensorRepositorySQLite;
import service.ChannelSorter;
import ui.measurementsList.MeasurementsListDialog;
import ui.model.DialogLoading;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class RemoveMeasurement extends SwingWorker<Boolean, Void> {
    private String lastSensorMessageGenerator(String measurement){
        return "Неможливо видалити останню величину для вимірювання параметру \"" + measurement + "\"";
    }

    private static String LAST_MEASUREMENT_MESSAGE;
    private static final String ERROR = "Помилка";
    private static final String ERROR_MESSAGE = "Помилка при видаленні величини!";

    private final MeasurementsListDialog dialog;
    private final DialogLoading loadDialog;
    private Measurement measurement;

    private final CalibratorRepository calibratorRepository = CalibratorRepositorySQLite.getInstance();
    private final MeasurementRepository measurementRepository = MeasurementRepositorySQLite.getInstance();
    private final ChannelRepository channelRepository = ChannelRepositorySQLite.getInstance();
    private final SensorRepository sensorRepository = SensorRepositorySQLite.getInstance();

    public RemoveMeasurement(MeasurementsListDialog dialog){
        super();
        this.dialog = dialog;
        this.loadDialog = new DialogLoading(dialog);
    }

    public void start(){
        EventQueue.invokeLater(() -> loadDialog.setVisible(true));
        this.execute();
    }

    @Override
    protected Boolean doInBackground() throws Exception {
        String measurementValue = this.dialog.getSelectedMeasurementValue();
        measurement = measurementRepository.get(measurementValue).get();
        if (measurementRepository.isLastInMeasurement(measurementValue)) {
            LAST_MEASUREMENT_MESSAGE = this.lastSensorMessageGenerator(measurement.getName());
            return false;
        }else {
            measurementRepository.remove(measurement);
            channelRepository.removeByMeasurementValue(measurementValue);
            calibratorRepository.removeByMeasurementValue(measurementValue);
            sensorRepository.removeMeasurementValue(measurementValue);
            return true;
        }
    }

    @Override
    protected void done() {
        this.loadDialog.dispose();
        try {
            if (this.get()){
                this.dialog.updateMeasurementsList(measurement.getName());
                if (ChannelSorter.getInstance().isOn()){
                    this.dialog.updateMain(new ArrayList<>(ChannelSorter.getInstance().getCurrent()));
                }else {
                    this.dialog.updateMain(new ArrayList<>(channelRepository.getAll()));
                }
            }else {
                JOptionPane.showMessageDialog(dialog, LAST_MEASUREMENT_MESSAGE, ERROR, JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(dialog, ERROR_MESSAGE, ERROR,JOptionPane.ERROR_MESSAGE);
        }
    }
}
