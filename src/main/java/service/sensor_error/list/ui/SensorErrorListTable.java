package service.sensor_error.list.ui;

import model.dto.SensorError;

import javax.annotation.Nullable;
import java.util.List;

public interface SensorErrorListTable {
    @Nullable String getSelectedId();
    void setSensorErrorsList(List<SensorError> list);
}
