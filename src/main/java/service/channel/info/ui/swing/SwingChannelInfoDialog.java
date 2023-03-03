package service.channel.info.ui.swing;

import model.ui.UI;
import service.application.ApplicationScreen;

import javax.swing.*;
import java.awt.*;

public class SwingChannelInfoDialog extends JDialog implements UI {
    private static final ApplicationScreen APPLICATION_SCREEN = ApplicationScreen.getInstance();

    private static final String TITLE_TEXT = "Інформація вимірювального каналу";


    public SwingChannelInfoDialog(SwingChannelInfoCodePanel codePanel,
                                  SwingChannelInfoNamePanel namePanel,
                                  SwingChannelInfoMeasurementPanel measurementPanel,
                                  SwingChannelInfoTechnologyNumberPanel technologyNumberPanel,
                                  SwingChannelInfoDatePanel datePanel,
                                  SwingChannelInfoProtocolNumberPanel protocolNumberPanel,
                                  SwingChannelInfoFrequencyPanel frequencyPanel,
                                  SwingChannelInfoNextDatePanel nextDatePanel,
                                  SwingChannelInfoPathPanel pathPanel,
                                  SwingChannelInfoSensorPanel sensorPanel) {
        super(APPLICATION_SCREEN, TITLE_TEXT, true);


    }

    @Override
    public void showing() {
        EventQueue.invokeLater(() -> this.setVisible(true));
    }

    @Override
    public void hiding() {
        EventQueue.invokeLater(() -> this.setVisible(false));
    }

    @Override
    public void shutdown() {
        this.dispose();
    }
}
