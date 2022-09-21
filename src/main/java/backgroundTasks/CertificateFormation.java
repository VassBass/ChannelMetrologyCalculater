package backgroundTasks;

import calculation.Calculation;
import certificates.*;
import constants.Key;
import model.Calibrator;
import model.Channel;
import model.Measurement;
import repository.ChannelRepository;
import repository.impl.ChannelRepositorySQLite;
import ui.calculate.end.CalculateEndDialog;
import ui.mainScreen.MainScreen;
import ui.model.LoadDialog;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class CertificateFormation extends SwingWorker<Void, Void> {
    private final MainScreen mainScreen;
    private final Channel channel;
    private final HashMap<Integer, Object> values;
    private final Calculation calculation;

    private final LoadDialog loadDialog;
    private Certificate certificate;

    private final ChannelRepository channelRepository = ChannelRepositorySQLite.getInstance();

    public CertificateFormation(MainScreen mainScreen, Channel channel, HashMap<Integer, Object> values, Calculation calculation){
        super();
        this.mainScreen = mainScreen;
        this.channel = channel;
        this.values = values;
        this.calculation = calculation;

        this.loadDialog = new LoadDialog(mainScreen);
        EventQueue.invokeLater(() -> {
            try {
                loadDialog.setVisible(true);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    @Override
    protected Void doInBackground() throws Exception {
        String measurementName = this.channel.getMeasurement().getName();
        if (measurementName.equals(Measurement.TEMPERATURE)){
            this.certificate = new TemperatureCertificate();
        }else if (measurementName.equals(Measurement.PRESSURE)){
            this.certificate = new PressureCertificate();
        }else if (measurementName.equals(Measurement.CONSUMPTION)){
            if (this.calculation.getCalibrator().getName().equals(Calibrator.ROSEMOUNT_8714DQ4)){
                this.certificate = new ConsumptionCertificate_ROSEMOUNT();
            }else {
                this.certificate = new ConsumptionCertificate();
            }
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

        newChannel.setDate((String) this.values.get(Key.CHANNEL_DATE));
        newChannel.setNumberOfProtocol((String) this.values.get(Key.CHANNEL_PROTOCOL_NUMBER));
        newChannel.setSuitability(this.calculation.goodChannel());

        if (newChannel.isSuitability()){
            newChannel.setReference("");
        }else{
            newChannel.setReference((String) this.values.get(Key.CHANNEL_REFERENCE));
        }
        channelRepository.set(this.channel, newChannel);
    }
}
