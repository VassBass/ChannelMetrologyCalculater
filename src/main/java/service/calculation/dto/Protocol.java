package service.calculation.dto;

import model.dto.Calibrator;
import model.dto.Channel;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;

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
    private double alarm;
    private TreeMap<Double, Double> input;
    private TreeMap<Double, double[]> output;
    private double extendedIndeterminacy;
    private double absoluteError;
    private double relativeError;
    private Map<Double, Double> systematicErrors;

    public double getExtendedIndeterminacy() {
        return extendedIndeterminacy;
    }

    public void setExtendedIndeterminacy(double extendedIndeterminacy) {
        this.extendedIndeterminacy = extendedIndeterminacy;
    }

    public double getAbsoluteError() {
        return absoluteError;
    }

    public void setAbsoluteError(double absoluteError) {
        this.absoluteError = absoluteError;
    }

    public double getRelativeError() {
        return relativeError;
    }

    public void setRelativeError(double relativeError) {
        this.relativeError = relativeError;
    }

    public Map<Double, Double> getSystematicErrors() {
        return systematicErrors;
    }

    public void setSystematicErrors(Map<Double, Double> systematicErrors) {
        this.systematicErrors = systematicErrors;
    }

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

    public double getAlarm() {
        return alarm;
    }

    public void setAlarm(double alarm) {
        this.alarm = alarm;
    }

    public TreeMap<Double, Double> getInput() {
        return input;
    }

    public void setInput(TreeMap<Double, Double> input) {
        this.input = input;
    }

    public TreeMap<Double, double[]> getOutput() {
        return output;
    }

    public void setOutput(TreeMap<Double, double[]> output) {
        this.output = output;
    }
}
