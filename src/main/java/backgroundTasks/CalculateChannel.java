package backgroundTasks;


import calculation.Calculation;
import calculation.CalculationConsumption;
import calculation.CalculationPressure;
import calculation.CalculationTemperature;
import constants.Key;
import model.Calibrator;
import model.Channel;
import model.Measurement;
import ui.calculate.verification.CalculateVerificationDialog;
import ui.mainScreen.MainScreen;
import ui.model.LoadDialog;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class CalculateChannel extends SwingWorker<Void, Void> {
    private final MainScreen mainScreen;
    private final Channel channel;
    private final HashMap<Integer, Object> values;

    private Calculation calculation;

    private final LoadDialog loadDialog;

    public CalculateChannel(MainScreen mainScreen, Channel channel, HashMap<Integer, Object> values){
        super();
        this.mainScreen = mainScreen;
        this.channel = channel;
        this.values = values;

        this.loadDialog = new LoadDialog(mainScreen);
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    loadDialog.setVisible(true);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    @Override
    protected Void doInBackground() throws Exception {
        Calibrator calibrator = (Calibrator) this.values.get(Key.CALIBRATOR);
        double[]controlPointsValues = (double[]) this.values.get(Key.CONTROL_POINTS);

        double[][]measurements = new double[5][8];
        String measurementName = this.channel._getMeasurement().getName();
        if (measurementName.equals(Measurement.TEMPERATURE)){
            this.calculation = new CalculationTemperature(this.channel);
            measurements = new double[5][8];
        }else if (measurementName.equals(Measurement.PRESSURE)){
            this.calculation = new CalculationPressure(this.channel);
            measurements = new double[5][8];
        }else if (measurementName.equals(Measurement.CONSUMPTION)){
            this.calculation = new CalculationConsumption(this.channel);
            if (calibrator.getName().equals(Calibrator.ROSEMOUNT_8714DQ4)){
                measurements = new double[5][8];
            }else {
                measurements = new double[5][10];
            }
        }

        double[]measurement1 = (double[]) this.values.get(Key.MEASUREMENT_1);
        double[]measurement2 = (double[]) this.values.get(Key.MEASUREMENT_2);
        double[]measurement3 = (double[]) this.values.get(Key.MEASUREMENT_3);
        double[]measurement4 = (double[]) this.values.get(Key.MEASUREMENT_4);
        double[]measurement5 = (double[]) this.values.get(Key.MEASUREMENT_5);

        if (measurement2 == null){
            measurement2 = measurement1;
        }
        if (measurement3 == null){
            measurement3 = measurement1;
        }
        if (measurement4 == null){
            measurement4 = measurement2;
        }
        if (measurement5 == null){
            measurement5 = measurement3;
        }
        measurements[0] = measurement1;
        measurements[1] = measurement2;
        measurements[2] = measurement3;
        measurements[3] = measurement4;
        measurements[4] = measurement5;

        this.calculation.setControlPointsValues(controlPointsValues);
        this.calculation.setIn(measurements);
        this.calculation.setCalibrator(calibrator);
        return null;
    }

    @Override
    protected void done() {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    loadDialog.dispose();
                    new CalculateVerificationDialog(mainScreen, channel, values, calculation).setVisible(true);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }
}