package service.channel.info.ui;

import java.util.List;

public interface ChannelInfoSensorPanel {
    void setSensorsNames(List<String> sensorsNames);
    void setMeasurementValues(List<String> values);

    void setRange(String min, String max);
    void setRangeMin(String min);
    void setRangeMax(String max);
    void setRangePanelEnabled(boolean enabled);
    void setSerialNumber(String serialNumber);

    String getSelectedSensorName();
    String getRangeMin();
    String getRangeMax();
    String getSerialNumber();

    boolean isRangeValid();
}
