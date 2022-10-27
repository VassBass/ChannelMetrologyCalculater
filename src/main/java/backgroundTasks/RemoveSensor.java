package backgroundTasks;

import model.Sensor;
import repository.ChannelRepository;
import repository.ControlPointsValuesRepository;
import repository.SensorRepository;
import repository.impl.ChannelRepositorySQLite;
import repository.impl.ControlPointsValuesRepositorySQLite;
import repository.impl.SensorRepositorySQLite;
import service.ChannelSorter;
import ui.model.DialogLoading;
import ui.sensorsList.SensorsListDialog;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class RemoveSensor extends SwingWorker<Boolean, Void> {
    private static final String ALL = "Всі";
    private String lastSensorMessageGenerator(String measurement){
        return "Неможливо видалити останній ПВП для вимірювання параметру \"" + measurement + "\"";
    }

    private static String LAST_SENSOR_MESSAGE;
    private static final String ERROR = "Помилка";
    private static final String ERROR_MESSAGE = "Помилка при видаленні ПВП!";

    private final SensorsListDialog dialog;
    private final DialogLoading loadDialog;

    private final ControlPointsValuesRepository cpvRepository = ControlPointsValuesRepositorySQLite.getInstance();
    private final ChannelRepository channelRepository = ChannelRepositorySQLite.getInstance();
    private final SensorRepository sensorRepository = SensorRepositorySQLite.getInstance();

    public RemoveSensor(SensorsListDialog dialog){
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
        Sensor sensor = this.dialog.getSensor();
        if (sensorRepository.isLastInMeasurement(sensor)) {
            LAST_SENSOR_MESSAGE = this.lastSensorMessageGenerator(sensor.getMeasurement());
            return false;
        }else {
            sensorRepository.remove(sensor);
            channelRepository.removeBySensor(sensor);
            cpvRepository.removeAll(sensor.getType());
            return true;
        }
    }

    @Override
    protected void done() {
        this.loadDialog.dispose();
        try {
            if (this.get()){
                String measurement = dialog.getMeasurement().equals(ALL) ? null : dialog.getMeasurement();
                this.dialog.update(measurement);
                if (ChannelSorter.getInstance().isOn()){
                    this.dialog.updateMain(new ArrayList<>(ChannelSorter.getInstance().getCurrent()));
                }else {
                    this.dialog.updateMain(new ArrayList<>(channelRepository.getAll()));
                }
            }else {
                JOptionPane.showMessageDialog(dialog, LAST_SENSOR_MESSAGE, ERROR, JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(dialog, ERROR_MESSAGE, ERROR,JOptionPane.ERROR_MESSAGE);
        }
    }
}