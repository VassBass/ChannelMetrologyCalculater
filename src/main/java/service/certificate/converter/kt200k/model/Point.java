package service.certificate.converter.kt200k.model;

import localization.Labels;

public class Point {
    private String resistance;
    private String temperature;
    private String delta;
    private String voltage;
    private String additionalDelta;

    public Point() {
        setDefaults();
    }

    private void setDefaults() {
        resistance = Labels.DASH;
        temperature = Labels.DASH;
        delta = Labels.DASH;
        voltage = Labels.DASH;
        additionalDelta = Labels.DASH;
    }

    public String getResistance() {
        return resistance;
    }

    public void setResistance(String resistance) {
        this.resistance = resistance;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getDelta() {
        return delta;
    }

    public void setDelta(String delta) {
        this.delta = delta;
    }

    public String getVoltage() {
        return voltage;
    }

    public void setVoltage(String voltage) {
        this.voltage = voltage;
    }

    public String getAdditionalDelta() {
        return additionalDelta;
    }

    public void setAdditionalDelta(String additionalDelta) {
        this.additionalDelta = additionalDelta;
    }

    @Override
    public String toString() {
        return "Point{" +
                "\nresistance='" + resistance + '\'' +
                ",\ntemperature='" + temperature + '\'' +
                ",\ndelta='" + delta + '\'' +
                ",\nvoltage='" + voltage + '\'' +
                ",\nadditionalDelta='" + additionalDelta + '\'' +
                "\n}";
    }
}
