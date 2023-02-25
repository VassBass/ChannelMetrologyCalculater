package service.channel.ui;

import model.dto.Channel;

import java.util.List;

public interface ChannelTable {
    void setChannelList(List<Channel> list);
    String getSelectedChannelCode();
}
