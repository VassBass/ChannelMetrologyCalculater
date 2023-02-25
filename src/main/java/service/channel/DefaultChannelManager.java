package service.channel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.channel.ui.ChannelInfoTable;
import service.channel.ui.ChannelTable;
import service.channel.ui.SearchPanel;
import service.repository.repos.channel.ChannelRepository;

import java.awt.*;
import java.io.File;

public class DefaultChannelManager implements ChannelManager {
    private static final Logger logger = LoggerFactory.getLogger(DefaultChannelManager.class);

    private final ChannelRepository repository;
    private final ChannelConfigHolder configHolder;

    private ChannelInfoTable channelInfoTable;
    private SearchPanel searchPanel;
    private ChannelTable channelTable;

    public DefaultChannelManager(ChannelRepository repository, ChannelConfigHolder configHolder) {
        this.repository = repository;
        this.configHolder = configHolder;
    }

    @Override
    public void channelSelected(String channelCode) {
        if (channelInfoTable != null) channelInfoTable.updateInfo(repository.get(channelCode));
    }

    @Override
    public void addChannel() {

    }

    @Override
    public void showChannelInfo() {

    }

    @Override
    public void removeChannel() {

    }

    @Override
    public void calculateChannel() {

    }

    @Override
    public void chooseOSBeforeChannelCalculate() {

    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public void openChannelCertificateFolder() {
        Desktop desktop;
        if (Desktop.isDesktopSupported()) {
            desktop = Desktop.getDesktop();
            try {
                File certificateFolder = new File(configHolder.getChannelsCertificatesFolder());
                if (!certificateFolder.exists()) certificateFolder.mkdirs();
                desktop.open(certificateFolder);
            } catch (Exception ex) {
                logger.warn("Exception was thrown!", ex);
            }
        }
    }

    @Override
    public void search() {

    }

    @Override
    public void advancedSearch() {

    }

    @Override
    public void shutdownSearch() {

    }

    @Override
    public void init(ChannelInfoTable infoTable,
                     SearchPanel searchPanel,
                     ChannelTable channelTable) {
        this.channelInfoTable = infoTable;
        this.searchPanel = searchPanel;
        this.channelTable = channelTable;
    }
}
