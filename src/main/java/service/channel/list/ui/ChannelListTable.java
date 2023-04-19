package service.channel.list.ui;

import model.dto.Channel;

import javax.annotation.Nullable;
import java.util.List;

public interface ChannelListTable {
    void setChannelList(List<Channel> list);
    @Nullable String getSelectedChannelCode();
}
