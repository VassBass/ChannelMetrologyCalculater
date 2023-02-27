package service.channel.list;

import service.channel.list.ui.ChannelListInfoTable;
import service.channel.list.ui.ChannelListTable;
import service.channel.list.ui.ChannelListSearchPanel;

public interface ChannelListManager {
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
    void init(ChannelListInfoTable infoTable,
              ChannelListSearchPanel searchPanel,
              ChannelListTable channelTable);
}
