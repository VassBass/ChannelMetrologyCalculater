package service.calculation.condition;

import util.DateHelper;

import java.util.Calendar;
import java.util.Objects;

public class CalculationControlConditionValuesBuffer {
    private static volatile CalculationControlConditionValuesBuffer instance;
    public static CalculationControlConditionValuesBuffer getInstance() {
        if (Objects.isNull(instance)) {
            synchronized (CalculationControlConditionValuesBuffer.class) {
                if (Objects.isNull(instance)) instance = new CalculationControlConditionValuesBuffer();
            }
        }
        return instance;
    }

    private String date;
    private int temperature;
    private int pressure;
    private int humidity;

    private CalculationControlConditionValuesBuffer() {
        date = DateHelper.dateToString(Calendar.getInstance());
        temperature = 21;
        pressure = 750;
        humidity = 70;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public int getPressure() {
        return pressure;
    }

    public void setPressure(int pressure) {
        this.pressure = pressure;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }
}
