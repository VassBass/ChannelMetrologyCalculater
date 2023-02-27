package service.channel.list;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.channel.list.ui.ChannelListInfoTable;
import service.channel.list.ui.ChannelListTable;
import service.channel.list.ui.ChannelListSearchPanel;
import service.repository.repos.channel.ChannelRepository;

import java.awt.*;
import java.io.File;

public class DefaultChannelListManager implements ChannelListManager {
    private static final Logger logger = LoggerFactory.getLogger(DefaultChannelListManager.class);

    private final ChannelRepository repository;
    private final ChannelListConfigHolder configHolder;

    private ChannelListInfoTable channelInfoTable;
    private ChannelListSearchPanel searchPanel;
    private ChannelListTable channelTable;

    public DefaultChannelListManager(ChannelRepository repository, ChannelListConfigHolder configHolder) {
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
    public void init(ChannelListInfoTable infoTable,
                     ChannelListSearchPanel searchPanel,
                     ChannelListTable channelTable) {
        this.channelInfoTable = infoTable;
        this.searchPanel = searchPanel;
        this.channelTable = channelTable;
    }
}
