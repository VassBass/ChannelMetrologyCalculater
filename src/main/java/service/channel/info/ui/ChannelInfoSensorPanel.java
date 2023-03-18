package service.channel.info.ui;

import java.util.List;

public interface ChannelInfoSensorPanel {
    void setSensorsTypes(List<String> sensorsTypes);
    void setMeasurementValues(List<String> values);
    void setErrorFormulas(List<String> errors);

    void setSensorType(String type);
    void setMeasurementValue(String value);
    void setRange(String min, String max);
    void setRangeMin(String min);
    void setRangeMax(String max);
    void setRangePanelEnabled(boolean enabled);
    void setSerialNumber(String serialNumber);
    void setErrorFormula(String error);

    String getSelectedSensorType();
    String getSelectedMeasurementValue();
    String getRangeMin();
    String getRangeMax();
    String getSerialNumber();
    String getErrorFormula();

    boolean isRangeValid();
    boolean isEqualsRangesCheckboxAreSelected();
}
