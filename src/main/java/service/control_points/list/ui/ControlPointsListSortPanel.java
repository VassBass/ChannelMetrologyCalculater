package service.control_points.list.ui;

import java.util.List;

public interface ControlPointsListSortPanel {
    void setSensorTypeList(List<String> types);
    String getSelectedSensorType();
    String getSelectedMeasurementName();
}
