package service.channel.list;

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
    void revaluateChannelTable();
}
