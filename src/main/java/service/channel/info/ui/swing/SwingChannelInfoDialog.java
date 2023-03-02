package service.channel.info.ui.swing;

import model.ui.UI;
import repository.RepositoryFactory;
import repository.repos.channel.ChannelRepository;
import service.application.ApplicationScreen;
import service.channel.info.ChannelInfoManager;
import service.channel.info.ui.*;

import javax.swing.*;
import java.awt.*;

public class SwingChannelInfoDialog extends JDialog implements UI {
    private static final ApplicationScreen APPLICATION_SCREEN = ApplicationScreen.getInstance();

    private static final String TITLE_TEXT = "Інформація вимірювального каналу";


    public SwingChannelInfoDialog(ChannelInfoCodePanel codePanel,
                                  ChannelInfoNamePanel namePanel,
                                  ChannelInfoMeasurementPanel measurementPanel,
                                  ChannelInfoTechnologyNumberPanel technologyNumberPanel,
                                  ChannelInfoDatePanel datePanel) {
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
