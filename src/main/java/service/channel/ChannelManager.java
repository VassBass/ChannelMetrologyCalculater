package service.channel;

import service.channel.ui.ChannelInfoTable;
import service.channel.ui.ChannelTable;
import service.channel.ui.SearchPanel;

public interface ChannelManager {
    void channelSelected(String channelCode);
    void addChannel();
    void showChannelInfo();
    void removeChannel();
    void calculateChannel();
    void chooseOSBeforeChannelCalculate();
    void openChannelCertificateFolder();
    void search();
    void advancedSearch();
    void shutdownSearch();
    void init(ChannelInfoTable infoTable,
              SearchPanel searchPanel,
              ChannelTable channelTable);
}
