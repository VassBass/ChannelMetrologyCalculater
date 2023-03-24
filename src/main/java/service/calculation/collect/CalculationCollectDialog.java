package service.calculation.collect;

import model.ui.UI;
import service.calculation.dto.Protocol;

import javax.annotation.Nullable;

public interface CalculationCollectDialog extends UI {
    boolean fillProtocol(Protocol protocol);
}
