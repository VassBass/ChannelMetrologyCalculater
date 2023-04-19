package service.calculation;

import model.ui.UI;
import service.calculation.protocol.Protocol;

public interface CalculationCollectDialog extends UI {
    boolean fillProtocol(Protocol protocol);
}
