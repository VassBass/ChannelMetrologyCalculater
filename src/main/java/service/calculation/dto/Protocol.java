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

    private String date;
    private String number;
    private Calibrator calibrator;
    private int temperature;
    private int humidity;
    private int pressure;
    private double alarm;
    private TreeMap<Double, Double> input;
    private TreeMap<Double, double[]> output;
    private double extendedIndeterminacy;
    private double absoluteError;
    private double relativeError;
    private Map<Double, Double> systematicErrors;
    private String conclusion;
    private int valuesDecimalPoint = 2;
    private int percentsDecimalPoint = 2;

    public int getValuesDecimalPoint() {
        return valuesDecimalPoint;
    }

    public void setValuesDecimalPoint(int valuesDecimalPoint) {
        this.valuesDecimalPoint = valuesDecimalPoint;
    }

    public int getPercentsDecimalPoint() {
        return percentsDecimalPoint;
    }

    public void setPercentsDecimalPoint(int percentsDecimalPoint) {
        this.percentsDecimalPoint = percentsDecimalPoint;
    }

    public String getConclusion() {
        return conclusion;
    }

    public void setConclusion(String conclusion) {
        this.conclusion = conclusion;
    }

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

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public int getPressure() {
        return pressure;
    }

    public void setPressure(int pressure) {
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
