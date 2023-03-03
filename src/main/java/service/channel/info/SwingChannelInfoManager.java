package service.channel.info;

import model.dto.Channel;
import repository.RepositoryFactory;
import repository.repos.channel.ChannelRepository;
import service.channel.info.ui.*;
import service.channel.info.ui.swing.SwingChannelInfoDialog;

import javax.swing.*;

public class SwingChannelInfoManager implements ChannelInfoManager {
    private static final String SEARCH_TEXT = "Пошук";

    private static final String CHANNEL_NOT_FOUND_MESSAGE = "Канал з данним кодом не знайдений.";
    private static final String CHANNEL_FOUND_MESSAGE = "Канал знайдено. Відкрити інформацію про канал у данному вікні?";

    private final SwingChannelInfoDialog owner;
    private final RepositoryFactory repositoryFactory;

    private ChannelInfoCodePanel codePanel;

    public SwingChannelInfoManager(SwingChannelInfoDialog owner, RepositoryFactory repositoryFactory) {
        this.owner = owner;
        this.repositoryFactory = repositoryFactory;
    }

    @Override
    public void searchChannelByCode() {
        if (codePanel != null) {
            ChannelRepository repository = repositoryFactory.getImplementation(ChannelRepository.class);
            Channel channel = repository.get(codePanel.getCode());
            if (channel == null) {
                JOptionPane.showMessageDialog(owner, CHANNEL_NOT_FOUND_MESSAGE, SEARCH_TEXT, JOptionPane.INFORMATION_MESSAGE);
            } else {
                int result = JOptionPane.showConfirmDialog(owner, CHANNEL_FOUND_MESSAGE, SEARCH_TEXT, JOptionPane.OK_CANCEL_OPTION);
                if (result == 0) setChannelInfo(channel);
            }
        }
    }

    @Override
    public void setChannelInfo(Channel channel) {

    }

    @Override
    public void changeMeasurementName() {

    }

    @Override
    public void changeMeasurementValue() {

    }

    @Override
    public void changeDateOrFrequency() {

    }

    @Override
    public void setChannelAndSensorRangesEqual() {

    }

    @Override
    public void changedChannelRange() {

    }

    @Override
    public void changedAllowableErrorPercent() {

    }

    @Override
    public void changedAllowableErrorValue() {

    }

    @Override
    public void saveChannel() {

    }

    @Override
    public void saveAndCalculateChannel() {

    }

    @Override
    public void resetChannelInfo() {

    }

    @Override
    public void deleteChannel() {

    }

    @Override
    public void init(ChannelInfoCodePanel codePanel,
                     ChannelInfoNamePanel namePanel,
                     ChannelInfoMeasurementPanel measurementPanel,
                     ChannelInfoTechnologyNumberPanel technologyNumberPanel,
                     ChannelInfoDatePanel datePanel,
                     ChannelInfoProtocolNumberPanel protocolNumberPanel,
                     ChannelInfoFrequencyPanel frequencyPanel,
                     ChannelInfoNextDatePanel nextDatePanel,
                     ChannelInfoPathPanel pathPanel,
                     ChannelInfoSensorPanel sensorPanel,
                     ChannelInfoRangePanel rangePanel,
                     ChannelInfoAllowableErrorPanel allowableErrorPanel) {
        this.codePanel = codePanel;
    }
}
