package service.calculation.condition.ui;

public interface CalculationControlConditionEnvironmentPanel {
    void setTemperature(int temperature);
    int getTemperature();

    void setPressure(int pressure);
    int getPressure();

    void setHumidity(int humidity);
    int getHumidity();
}
