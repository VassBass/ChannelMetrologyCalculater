package service.channel_list.ui;

import java.util.Map;

public interface ChannelListSearchPanel {
    String getChannelCode();
    void setSearchInfo(Map<String, String> info);
    void resetPanel();
}
