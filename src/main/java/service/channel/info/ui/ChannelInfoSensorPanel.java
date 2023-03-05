package service.channel.info.ui;

import java.util.List;

public interface ChannelInfoSensorPanel {
    void setSensorsTypes(List<String> sensorsTypes);
    void setMeasurementValues(List<String> values);

    void setSensorType(String type);
    void setMeasurementValue(String value);
    void setRange(String min, String max);
    void setRangeMin(String min);
    void setRangeMax(String max);
    void setRangePanelEnabled(boolean enabled);
    void setSerialNumber(String serialNumber);

    String getSelectedSensorType();
    String getSelectedMeasurementValue();
    String getRangeMin();
    String getRangeMax();
    String getSerialNumber();

    boolean isRangeValid();
    boolean isEqualsRangesCheckboxAreSelected();
}
