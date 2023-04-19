package service.channel.info.ui;

public interface ChannelInfoAllowableErrorPanel {
    void setAllowableErrorPercent(String allowableErrorPercent);
    void setAllowableErrorValue(String allowableErrorValue);
    void setMeasurementValue(String value);

    String getAllowableErrorPercent();
    String getAllowableErrorValue();

    boolean isAllowableErrorPercentValid();
    boolean isAllowableErrorValueValid();
}
