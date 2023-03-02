package service.channel.info.ui.swing;

import model.ui.UI;
import repository.RepositoryFactory;
import repository.repos.channel.ChannelRepository;
import service.application.ApplicationScreen;
import service.channel.info.ChannelInfoManager;
import service.channel.info.ui.ChannelInfoCodePanel;
import service.channel.info.ui.ChannelInfoMeasurementPanel;
import service.channel.info.ui.ChannelInfoNamePanel;

import javax.swing.*;
import java.awt.*;

public class SwingChannelInfoDialog extends JDialog implements UI {
    private static final ApplicationScreen APPLICATION_SCREEN = ApplicationScreen.getInstance();

    private static final String TITLE_TEXT = "Інформація вимірювального каналу";

    private final ChannelInfoCodePanel codePanel;
    private final ChannelInfoNamePanel namePanel;
    private final SwingChannelInfoMeasurementPanel measurementPanel;

    public SwingChannelInfoDialog(ChannelInfoCodePanel codePanel,
                                  ChannelInfoNamePanel namePanel,
                                  ChannelInfoMeasurementPanel measurementPanel,

                                  ChannelInfoManager manager,
                                  RepositoryFactory repositoryFactory) {
        super(APPLICATION_SCREEN, TITLE_TEXT, true);
        ChannelRepository channelRepository = repositoryFactory.getImplementation(ChannelRepository.class);

        this.codePanel = new SwingChannelInfoCodePanel(this, manager, channelRepository);
        this.namePanel = new SwingChannelInfoNamePanel();
        this.measurementPanel = new SwingChannelInfoMeasurementPanel(manager);

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
