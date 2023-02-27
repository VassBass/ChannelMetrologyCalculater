package service.channel_list;

import service.channel_list.ui.ChannelListInfoTable;
import service.channel_list.ui.ChannelListTable;
import service.channel_list.ui.ChannelListSearchPanel;

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
