package service.calculation.result.worker;

import service.calculation.dto.Protocol;

public interface CalculationWorker {
    boolean calculate(Protocol protocol);
}
