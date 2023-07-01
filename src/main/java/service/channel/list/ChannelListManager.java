package service.channel.list;

import model.dto.Channel;

import javax.annotation.Nonnull;

public interface ChannelListManager {
    void channelSelected(String channelCode);
    void addChannel();
    void showChannelInfo();
    void removeChannel();
    void calculateChannel();
    void calculateChannel(@Nonnull Channel channel);
    void openChannelCertificateFolder();
    void search();
    void advancedSearch();
    void revaluateChannelTable();
}
