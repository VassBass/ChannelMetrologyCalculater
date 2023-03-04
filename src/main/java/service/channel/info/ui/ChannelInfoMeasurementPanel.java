package service.channel.info.ui;

import java.util.List;

public interface ChannelInfoMeasurementPanel {
    void setMeasurementName(String name);
    void setMeasurementValue(String value);
    void setMeasurementValues(List<String> values);
    void setMeasurementNames(List<String>names);

    String getSelectedMeasurementName();
    String getSelectedMeasurementValue();
}
