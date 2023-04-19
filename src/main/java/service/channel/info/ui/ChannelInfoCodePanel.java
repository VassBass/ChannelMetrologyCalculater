package service.channel.info.ui;

import javax.annotation.Nullable;

public interface ChannelInfoCodePanel {
    String getCode();
    void setCode(String code);
    boolean isCodeValid(@Nullable String oldChannelCode);
}
