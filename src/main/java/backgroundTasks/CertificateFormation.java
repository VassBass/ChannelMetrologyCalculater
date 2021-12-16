package backgroundTasks;

import constants.Strings;
import measurements.calculation.Calculation;
import measurements.certificates.*;
import constants.Value;
import model.Channel;
import support.Lists;
import support.Values;
import ui.LoadDialog;
import ui.calculate.end.CalculateEndDialog;
import ui.main.MainScreen;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Calendar;

public class CertificateFormation extends SwingWorker<Void, Void> {
    private final MainScreen mainScreen;
    private final Channel channel;
    private final Values values;
    private final Calculation calculation;

    private final LoadDialog loadDialog;
    private Certificate certificate;

    public CertificateFormation(MainScreen mainScreen, Channel channel, Values values, Calculation calculation){
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
                if (this.channel.getSensor().getType().contains(Strings.SENSOR_YOKOGAWA)) {
                    this.certificate = new ConsumptionCertificate_YOKOGAWA();
                }else if (this.channel.getSensor().getType().contains(Strings.SENSOR_ROSEMOUNT)){
                    this.certificate = new ConsumptionCertificate_ROSEMOUNT();
            }
                break;
        }

        this.certificate.init(this.calculation, this.values, this.channel);
        this.certificate.formation();
        this.certificate.save();

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

        newChannel.setDate((Calendar) this.values.getValue(Value.CHANNEL_DATE));
        newChannel.setNumberOfProtocol(this.values.getStringValue(Value.CHANNEL_PROTOCOL_NUMBER));
        newChannel.isGood = this.calculation.goodChannel();

        if (newChannel.isGood){
            newChannel.setReference("");
        }else{
            newChannel.setReference(this.values.getStringValue(Value.CHANNEL_REFERENCE));
        }

        ArrayList<Channel>channels = Lists.channels();
        if (channels != null) {
            for (int x = 0; x < channels.size(); x++) {
                if (channels.get(x).getCode().equals(this.channel.getCode())) {
                    channels.set(x, newChannel);
                    Lists.saveChannelsListToFile(channels);
                    break;
                }
            }
        }
    }
}
