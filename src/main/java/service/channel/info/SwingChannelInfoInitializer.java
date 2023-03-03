package service.channel.info;

import repository.repos.channel.ChannelRepository;
import service.channel.info.ui.swing.*;
import service.root.ServiceInitializer;

public class SwingChannelInfoInitializer implements ServiceInitializer {

    private final SwingChannelInfoDialog dialog;
    private final ChannelInfoManager manager;
    private final ChannelRepository channelRepository;

    public SwingChannelInfoInitializer(SwingChannelInfoDialog dialog,
                                       ChannelInfoManager manager,
                                       ChannelRepository channelRepository) {
        this.dialog = dialog;
        this.manager = manager;
        this.channelRepository = channelRepository;
    }

    @Override
    public void init() {
        SwingChannelInfoCodePanel codePanel = new SwingChannelInfoCodePanel(manager, channelRepository);
        SwingChannelInfoNamePanel namePanel = new SwingChannelInfoNamePanel();
        SwingChannelInfoMeasurementPanel measurementPanel = new SwingChannelInfoMeasurementPanel(manager);
        SwingChannelInfoTechnologyNumberPanel technologyNumberPanel = new SwingChannelInfoTechnologyNumberPanel();
        SwingChannelInfoDatePanel datePanel = new SwingChannelInfoDatePanel();
        SwingChannelInfoProtocolNumberPanel protocolNumberPanel = new SwingChannelInfoProtocolNumberPanel();
        SwingChannelInfoFrequencyPanel frequencyPanel = new SwingChannelInfoFrequencyPanel(manager);
        SwingChannelInfoNextDatePanel nextDatePanel = new SwingChannelInfoNextDatePanel();
        SwingChannelInfoPathPanel pathPanel = new SwingChannelInfoPathPanel();
        SwingChannelInfoSensorPanel sensorPanel = new SwingChannelInfoSensorPanel(manager);
        SwingChannelInfoRangePanel rangePanel = new SwingChannelInfoRangePanel(manager);
        SwingChannelInfoAllowableErrorPanel allowableErrorPanel = new SwingChannelInfoAllowableErrorPanel(manager);
        SwingChannelInfoButtonsPanel buttonsPanel = new SwingChannelInfoButtonsPanel(manager);

        dialog.init(
                codePanel,
                namePanel,
                measurementPanel,
                technologyNumberPanel,
                datePanel,
                protocolNumberPanel,
                frequencyPanel,
                nextDatePanel,
                pathPanel,
                sensorPanel,
                rangePanel,
                allowableErrorPanel,
                buttonsPanel);
        manager.init(
                codePanel,
                namePanel,
                measurementPanel,
                technologyNumberPanel,
                datePanel,
                protocolNumberPanel,
                frequencyPanel,
                nextDatePanel,
                pathPanel,
                sensorPanel,
                rangePanel,
                allowableErrorPanel
        );
    }
}
