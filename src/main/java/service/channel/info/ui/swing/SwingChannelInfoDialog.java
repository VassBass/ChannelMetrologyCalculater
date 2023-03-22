package service.channel.info.ui.swing;

import application.ApplicationScreen;
import model.ui.DefaultPanel;
import model.ui.UI;
import model.ui.builder.CellBuilder;
import service.channel.info.ChannelInfoConfigHolder;
import service.channel.info.ChannelInfoSwingContext;
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

        if (codePanel != null) panel.add(codePanel, new CellBuilder().x(0).y(0).width(2).height(1).build());
        if (namePanel != null) panel.add(namePanel, new CellBuilder().x(0).y(1).width(2).height(1).build());
        if (measurementPanel != null) panel.add(measurementPanel, new CellBuilder().x(0).y(2).width(2).height(1).build());
        if (technologyNumberPanel != null) panel.add(technologyNumberPanel, new CellBuilder().x(0).y(3).width(2).height(1).build());
        if (datePanel != null) panel.add(datePanel, new CellBuilder().x(0).y(4).width(2).height(1).build());
        if (protocolNumberPanel != null) panel.add(protocolNumberPanel, new CellBuilder().x(0).y(5).width(2).height(1).build());
        if (frequencyPanel != null) panel.add(frequencyPanel, new CellBuilder().x(0).y(6).width(1).height(1).weightX(0.4).build());
        if (nextDatePanel != null) panel.add(nextDatePanel, new CellBuilder().x(1).y(6).width(1).height(1).weightX(0.6).build());
        if (pathPanel != null) panel.add(pathPanel, new CellBuilder().x(0).y(7).width(2).height(4).build());
        if (sensorPanel != null) panel.add(sensorPanel, new CellBuilder().x(0).y(11).width(2).height(2).build());
        if (rangePanel != null) panel.add(rangePanel, new CellBuilder().x(0).y(13).width(2).height(1).build());
        if (allowableErrorPanel != null) panel.add(allowableErrorPanel, new CellBuilder().x(0).y(14).width(2).height(1).build());
        if (buttonsPanel != null) panel.add(buttonsPanel, new CellBuilder().x(0).y(15).width(2).height(2).build());

        this.setSize(configHolder.getDialogWidth(), configHolder.getDialogHeight());
        this.setLocation(ScreenPoint.center(applicationScreen, this));
        this.setContentPane(panel);
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
