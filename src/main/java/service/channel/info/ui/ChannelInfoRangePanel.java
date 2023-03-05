package service.channel.info.ui;

public interface ChannelInfoRangePanel {
    void setMeasurementValue(String value);
    void setRangeMin(String min);
    void setRangeMax(String max);

    String getRangeMin();
    String getRangeMax();
    String getMeasurementValue();

    boolean isRangeValid();
}
