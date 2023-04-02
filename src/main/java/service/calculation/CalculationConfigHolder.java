package service.calculation;

public interface CalculationConfigHolder {
    int getControlConditionDialogWidth();
    int getControlConditionDialogHeight();
    int getInputDialogWidth();
    int getInputDialogHeight();
    int getResultDialogWidth();
    int getResultDialogHeight();
    int getPersonsDialogWidth();
    int getPersonsDialogHeight();
    String getProtocolFolderPath();
    String getTemperatureCalculationMethodName();
    String getPressureCalculationMethodName();
    String getConsumptionCalculationMethodName();
}
