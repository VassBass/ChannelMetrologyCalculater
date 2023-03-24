package service.calculation.dto;

import model.dto.Calibrator;
import model.dto.Channel;

import javax.annotation.Nonnull;
import java.io.Serializable;

public class Protocol implements Serializable {
    private static final long serialVersionUID = 6L;

    private final Channel channel;

    /**
     * Control conditions
     */
    private String date;
    private String number;
    private Calibrator calibrator;
    private String temperature;
    private String humidity;
    private String pressure;

    public Protocol(@Nonnull Channel channel) {
        this.channel = channel;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Calibrator getCalibrator() {
        return calibrator;
    }

    public void setCalibrator(Calibrator calibrator) {
        this.calibrator = calibrator;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getPressure() {
        return pressure;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

    public Channel getChannel() {
        return channel;
    }
}
