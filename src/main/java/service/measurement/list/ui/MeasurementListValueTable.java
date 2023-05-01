package service.measurement.list.ui;

import javax.annotation.Nullable;
import java.util.List;

public interface MeasurementListValueTable {
    @Nullable String getSelectedValue();
    void setValueList(List<String> valueList);
}
