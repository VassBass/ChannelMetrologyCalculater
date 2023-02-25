package service.channel.ui;

import java.util.Map;

public interface SearchPanel {
    String getChannelCode();
    void setSearchInfo(Map<String, String> info);
    void resetPanel();
}
