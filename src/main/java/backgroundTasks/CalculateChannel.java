package backgroundTasks;


import constants.Strings;
import measurements.calculation.Calculation;
import model.Calibrator;
import model.Channel;
import ui.model.LoadDialog;
import ui.calculate.verification.CalculateVerificationDialog;
import ui.mainScreen.MainScreen;

import javax.swing.*;
import java.awt.*;

public class CalculateChannel extends SwingWorker<Void, Void> {
    private final MainScreen mainScreen;
    private final Channel channel;
    private final Values values;

    private Calculation calculation;

    private final LoadDialog loadDialog;

    public CalculateChannel(MainScreen mainScreen, Channel channel, Values values){
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
        Calibrator calibrator = (Calibrator) this.values.getValue(Value.CALIBRATOR);
        double[][]measurements = new double[5][8];
        switch (this.channel.getMeasurement().getNameConstant()){
            case TEMPERATURE:
            case PRESSURE:
                measurements = new double[5][8];
                break;
            case CONSUMPTION:
                if (calibrator.getName().equals(Strings.CALIBRATOR_ROSEMOUNT_8714DQ4)){
                    measurements = new double[5][8];
                }else {
                    measurements = new double[5][10];
                }
        }
        double[]measurement1 = (double[]) values.getValue(Value.MEASUREMENT_1);
        double[]measurement2 = (double[]) values.getValue(Value.MEASUREMENT_2);
        double[]measurement3 = (double[]) values.getValue(Value.MEASUREMENT_3);
        double[]measurement4 = (double[]) values.getValue(Value.MEASUREMENT_4);
        double[]measurement5 = (double[]) values.getValue(Value.MEASUREMENT_5);

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

        this.calculation = new Calculation(this.channel);
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
