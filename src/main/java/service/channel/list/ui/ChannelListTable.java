package service.channel.list.ui;

import model.dto.Channel;

import java.util.List;

public interface ChannelListTable {
    void setChannelList(List<Channel> list);
    String getSelectedChannelCode();
}
