package service.control_points.list.ui;

import model.dto.ControlPoints;

import javax.annotation.Nullable;
import java.util.List;

public interface ControlPointsListTable {
    void setControlPointsList(List<ControlPoints> list);
    @Nullable String getSelectedControlPointsName();
}
