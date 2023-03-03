package service.channel.info.ui.swing;

import model.ui.DefaultPanel;
import model.ui.UI;
import model.ui.builder.CellBuilder;

import javax.swing.*;
import java.awt.*;

public class SwingChannelInfoDialog extends JDialog implements UI {
    private static final String TITLE_TEXT = "Інформація вимірювального каналу";

    private SwingChannelInfoCodePanel codePanel;
    private SwingChannelInfoNamePanel namePanel;
    private SwingChannelInfoMeasurementPanel measurementPanel;
    private SwingChannelInfoTechnologyNumberPanel technologyNumberPanel;
    private SwingChannelInfoDatePanel datePanel;
    private SwingChannelInfoProtocolNumberPanel protocolNumberPanel;
    private SwingChannelInfoFrequencyPanel frequencyPanel;
    private SwingChannelInfoNextDatePanel nextDatePanel;
    private SwingChannelInfoPathPanel pathPanel;
    private SwingChannelInfoSensorPanel sensorPanel;
    private SwingChannelInfoRangePanel rangePanel;
    private SwingChannelInfoAllowableErrorPanel allowableErrorPanel;
    private SwingChannelInfoButtonsPanel buttonsPanel;


    public SwingChannelInfoDialog(Frame owner) {
        super(owner, TITLE_TEXT, true);

        DefaultPanel panel = new DefaultPanel();
        if (codePanel != null) panel.add(codePanel, new CellBuilder().x(0).y(0).width(2).height(1).build());
        if (namePanel != null) panel.add(namePanel, new CellBuilder().x(0).y(1).width(2).height(1).build());
        if (measurementPanel != null) panel.add(measurementPanel, new CellBuilder().x(0).y(2).width(2).height(1).build());
        if (technologyNumberPanel != null) panel.add(technologyNumberPanel, new CellBuilder().x(0).y(3).width(2).height(1).build());
        if (datePanel != null) panel.add(datePanel, new CellBuilder().x(0).y(4).width(2).height(1).build());
        if (protocolNumberPanel != null) panel.add(protocolNumberPanel, new CellBuilder().x(0).y(5).width(2).height(1).build());
        if (frequencyPanel != null) panel.add(frequencyPanel, new CellBuilder().x(0).y(6).width(1).height(1).build());
        if (nextDatePanel != null) panel.add(nextDatePanel, new CellBuilder().x(1).y(6).width(1).height(1).build());
        if (pathPanel != null) panel.add(pathPanel, new CellBuilder().x(0).y(7).width(2).height(4).build());
        if (sensorPanel != null) panel.add(sensorPanel, new CellBuilder().x(0).y(11).width(2).height(2).build());
        if (rangePanel != null) panel.add(rangePanel, new CellBuilder().x(0).y(13).width(2).height(1).build());
        if (allowableErrorPanel != null) panel.add(allowableErrorPanel, new CellBuilder().x(0).y(14).width(2).height(1).build());
        if (buttonsPanel != null) panel.add(buttonsPanel, new CellBuilder().x(0).y(15).width(2).height(2).build());
    }

    public void init(
            SwingChannelInfoCodePanel codePanel,
            SwingChannelInfoNamePanel namePanel,
            SwingChannelInfoMeasurementPanel measurementPanel,
            SwingChannelInfoTechnologyNumberPanel technologyNumberPanel,
            SwingChannelInfoDatePanel datePanel,
            SwingChannelInfoProtocolNumberPanel protocolNumberPanel,
            SwingChannelInfoFrequencyPanel frequencyPanel,
            SwingChannelInfoNextDatePanel nextDatePanel,
            SwingChannelInfoPathPanel pathPanel,
            SwingChannelInfoSensorPanel sensorPanel,
            SwingChannelInfoRangePanel rangePanel,
            SwingChannelInfoAllowableErrorPanel allowableErrorPanel,
            SwingChannelInfoButtonsPanel buttonsPanel
    ) {
        this.codePanel = codePanel;
        this.namePanel = namePanel;
        this.measurementPanel = measurementPanel;
        this.technologyNumberPanel = technologyNumberPanel;
        this.datePanel = datePanel;
        this.protocolNumberPanel = protocolNumberPanel;
        this.frequencyPanel = frequencyPanel;
        this.nextDatePanel = nextDatePanel;
        this.pathPanel = pathPanel;
        this.sensorPanel = sensorPanel;
        this.rangePanel = rangePanel;
        this.allowableErrorPanel = allowableErrorPanel;
        this.buttonsPanel = buttonsPanel;
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
