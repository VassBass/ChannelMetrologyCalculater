package backgroundTasks;

import application.Application;
import constants.CalibratorType;
import calculation.Calculation;
import certificates.*;
import constants.Key;
import model.Channel;
import ui.model.LoadDialog;
import ui.calculate.end.CalculateEndDialog;
import ui.mainScreen.MainScreen;

import javax.swing.*;
import java.awt.*;
import java.util.Calendar;
import java.util.HashMap;

public class CertificateFormation extends SwingWorker<Void, Void> {
    private final MainScreen mainScreen;
    private final Channel channel;
    private final HashMap<Integer, Object> values;
    private final Calculation calculation;

    private final LoadDialog loadDialog;
    private Certificate certificate;

    public CertificateFormation(MainScreen mainScreen, Channel channel, HashMap<Integer, Object> values, Calculation calculation){
        super();
        this.mainScreen = mainScreen;
        this.channel = channel;
        this.values = values;
        this.calculation = calculation;

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
        switch (this.channel.getMeasurement().getNameConstant()){
            case TEMPERATURE:
                this.certificate = new TemperatureCertificate();
                break;
            case PRESSURE:
                this.certificate = new PressureCertificate();
                break;
            case CONSUMPTION:
                if (this.calculation.getCalibrator().getName().equals(CalibratorType.ROSEMOUNT_8714DQ4)){
                    this.certificate = new ConsumptionCertificate_ROSEMOUNT();
                }else {
                    this.certificate = new ConsumptionCertificate();
                }
                break;
        }

        try {
            this.certificate.init(this.calculation, this.values, this.channel);
            this.certificate.formation();
            this.certificate.save();
        }catch (Exception e){
            e.printStackTrace();
        }


        this.setChannel();
        return null;
    }

    @Override
    protected void done() {
        this.loadDialog.dispose();
        new CalculateEndDialog(this.mainScreen, this.channel, this.values, this.calculation, this.certificate).setVisible(true);
    }

    private void setChannel(){
        Channel newChannel = new Channel().copyFrom(this.channel);

        newChannel.setDate((Calendar) this.values.get(Key.CHANNEL_DATE));
        newChannel.setNumberOfProtocol((String) this.values.get(Key.CHANNEL_PROTOCOL_NUMBER));
        newChannel.setSuitability(this.calculation.goodChannel());

        if (newChannel.isSuitability()){
            newChannel.setReference("");
        }else{
            newChannel.setReference((String) this.values.get(Key.CHANNEL_REFERENCE));
        }
        Application.context.channelsController.set(this.channel, newChannel);
    }
}
