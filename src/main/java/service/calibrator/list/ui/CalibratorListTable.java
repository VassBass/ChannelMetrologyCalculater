package service.calibrator.list.ui;

import model.dto.Calibrator;

import javax.annotation.Nullable;
import java.util.List;

public interface CalibratorListTable {
    void setCalibratorList(List<Calibrator> calibratorList);
    @Nullable String getSelectedCalibratorName();
}
