package service.sensor_types.list.ui;

import javax.annotation.Nullable;
import java.util.List;

public interface SensorTypesListTable {
    @Nullable String getSelectedType();
    void setTypeList(List<String> typeList);
}
