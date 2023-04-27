package service.channel.info.ui.swing;

import application.ApplicationScreen;
import model.ui.DefaultPanel;
import model.ui.UI;
import model.ui.builder.CellBuilder;
import service.channel.info.ChannelInfoConfigHolder;
import service.channel.info.ui.ChannelInfoSwingContext;
import util.ScreenPoint;

import javax.swing.*;
import java.awt.*;

public class SwingChannelInfoDialog extends JDialog implements UI {
    private static final String TITLE_TEXT = "Інформація вимірювального каналу";


    public SwingChannelInfoDialog(ApplicationScreen applicationScreen, ChannelInfoConfigHolder configHolder, ChannelInfoSwingContext context) {
        super(applicationScreen, TITLE_TEXT, true);

        DefaultPanel panel = new DefaultPanel();

        SwingChannelInfoCodePanel codePanel = context.getElement(SwingChannelInfoCodePanel.class);
        SwingChannelInfoNamePanel namePanel = context.getElement(SwingChannelInfoNamePanel.class);
        SwingChannelInfoMeasurementPanel measurementPanel = context.getElement(SwingChannelInfoMeasurementPanel.class);
        SwingChannelInfoTechnologyNumberPanel technologyNumberPanel = context.getElement(SwingChannelInfoTechnologyNumberPanel.class);
        SwingChannelInfoDatePanel datePanel = context.getElement(SwingChannelInfoDatePanel.class);
        SwingChannelInfoFrequencyPanel frequencyPanel = context.getElement(SwingChannelInfoFrequencyPanel.class);
        SwingChannelInfoRangePanel rangePanel = context.getElement(SwingChannelInfoRangePanel.class);
        SwingChannelInfoAllowableErrorPanel allowableErrorPanel = context.getElement(SwingChannelInfoAllowableErrorPanel.class);
        SwingChannelInfoProtocolNumberPanel protocolNumberPanel = context.getElement(SwingChannelInfoProtocolNumberPanel.class);
        SwingChannelInfoPathPanel pathPanel = context.getElement(SwingChannelInfoPathPanel.class);
        SwingChannelInfoNextDatePanel nextDatePanel = context.getElement(SwingChannelInfoNextDatePanel.class);
        SwingChannelInfoSensorPanel sensorPanel = context.getElement(SwingChannelInfoSensorPanel.class);
        SwingChannelInfoButtonsPanel buttonsPanel = context.getElement(SwingChannelInfoButtonsPanel.class);

        panel.add(codePanel, new CellBuilder().x(0).y(0).width(2).height(1).build());
        panel.add(namePanel, new CellBuilder().x(0).y(1).width(2).height(1).build());
        panel.add(measurementPanel, new CellBuilder().x(0).y(2).width(2).height(1).build());
        panel.add(technologyNumberPanel, new CellBuilder().x(0).y(3).width(2).height(1).build());
        panel.add(datePanel, new CellBuilder().x(0).y(4).width(2).height(1).build());
        panel.add(protocolNumberPanel, new CellBuilder().x(0).y(5).width(2).height(1).build());
        panel.add(frequencyPanel, new CellBuilder().x(0).y(6).width(1).height(1).weightX(0.4).build());
        panel.add(nextDatePanel, new CellBuilder().x(1).y(6).width(1).height(1).weightX(0.6).build());
        panel.add(pathPanel, new CellBuilder().x(0).y(7).width(2).height(4).build());
        panel.add(sensorPanel, new CellBuilder().x(0).y(11).width(2).height(2).build());
        panel.add(rangePanel, new CellBuilder().x(0).y(13).width(2).height(1).build());
        panel.add(allowableErrorPanel, new CellBuilder().x(0).y(14).width(2).height(1).build());
        panel.add(buttonsPanel, new CellBuilder().x(0).y(15).width(2).height(2).build());

        this.setSize(configHolder.getDialogWidth(), configHolder.getDialogHeight());
        this.setLocation(ScreenPoint.center(applicationScreen, this));
        this.setContentPane(panel);
    }

    @Override
    public void refresh() {
        EventQueue.invokeLater(() -> {
            this.setVisible(false);
            this.setVisible(true);
        });
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

    @Override
    public Object getSource() {
        return this;
    }
}
