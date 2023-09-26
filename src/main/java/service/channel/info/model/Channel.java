package service.channel.info.model;

import model.dto.Sensor;
import model.dto.builder.ChannelBuilder;
import model.dto.builder.SensorBuilder;

public class Channel {
    private String code;
    private String name;
    private String measurementName;
    private String measurementValue;
    private String department;
    private String area;
    private String process;
    private String installation;
    private String date;
    private String frequency;
    private String technologyNumber;
    private String numberOfProtocol;
    private String reference;
    private String rangeMin;
    private String rangeMax;
    private String allowableErrorValue;
    private String allowableErrorPercent;
    private String suitability;
    private String sensorType;
    private String sensorSerialNumber;
    private String sensorRangeMin;
    private String sensorRangeMax;
    private String sensorMeasurementValue;
    private String sensorErrorFormula;

    public model.dto.Channel createChannel() {
        return new ChannelBuilder()
                .setCode(code)
                .setName(name)
                .setMeasurementName(measurementName)
                .setMeasurementValue(measurementValue)
                .setDepartment(department)
                .setArea(area)
                .setProcess(process)
                .setInstallation(installation)
                .setDate(date)
                .setFrequency(Double.parseDouble(frequency))
                .setTechnologyNumber(technologyNumber)
                .setNumberOfProtocol(numberOfProtocol)
                .setReference(reference)
                .setRangeMin(Double.parseDouble(rangeMin))
                .setRangeMax(Double.parseDouble(rangeMax))
                .setAllowableErrorInValue(Double.parseDouble(allowableErrorValue))
                .setAllowableErrorInPercent(Double.parseDouble(allowableErrorPercent))
                .setSuitability(Boolean.parseBoolean(suitability))
                .build();
    }

    public Sensor createSensor() {
        return new SensorBuilder()
                .setChannelCode(code)
                .setType(sensorType)
                .setMeasurementName(measurementName)
                .setMeasurementValue(sensorMeasurementValue)
                .setSerialNumber(sensorSerialNumber)
                .setRangeMin(Double.parseDouble(sensorRangeMin))
                .setRangeMax(Double.parseDouble(sensorRangeMax))
                .setErrorFormula(sensorErrorFormula)
                .build();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMeasurementName() {
        return measurementName;
    }

    public void setMeasurementName(String measurementName) {
        this.measurementName = measurementName;
    }

    public String getMeasurementValue() {
        return measurementValue;
    }

    public void setMeasurementValue(String measurementValue) {
        this.measurementValue = measurementValue;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public String getInstallation() {
        return installation;
    }

    public void setInstallation(String installation) {
        this.installation = installation;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getTechnologyNumber() {
        return technologyNumber;
    }

    public void setTechnologyNumber(String technologyNumber) {
        this.technologyNumber = technologyNumber;
    }

    public String getNumberOfProtocol() {
        return numberOfProtocol;
    }

    public void setNumberOfProtocol(String numberOfProtocol) {
        this.numberOfProtocol = numberOfProtocol;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getRangeMin() {
        return rangeMin;
    }

    public void setRangeMin(String rangeMin) {
        this.rangeMin = rangeMin;
    }

    public String getRangeMax() {
        return rangeMax;
    }

    public void setRangeMax(String rangeMax) {
        this.rangeMax = rangeMax;
    }

    public String getAllowableErrorValue() {
        return allowableErrorValue;
    }

    public void setAllowableErrorValue(String allowableErrorValue) {
        this.allowableErrorValue = allowableErrorValue;
    }

    public String getAllowableErrorPercent() {
        return allowableErrorPercent;
    }

    public void setAllowableErrorPercent(String allowableErrorPercent) {
        this.allowableErrorPercent = allowableErrorPercent;
    }

    public String getSuitability() {
        return suitability;
    }

    public void setSuitability(String suitability) {
        this.suitability = suitability;
    }

    public String getSensorType() {
        return sensorType;
    }

    public void setSensorType(String sensorType) {
        this.sensorType = sensorType;
    }

    public String getSensorSerialNumber() {
        return sensorSerialNumber;
    }

    public void setSensorSerialNumber(String sensorSerialNumber) {
        this.sensorSerialNumber = sensorSerialNumber;
    }

    public String getSensorRangeMin() {
        return sensorRangeMin;
    }

    public void setSensorRangeMin(String sensorRangeMin) {
        this.sensorRangeMin = sensorRangeMin;
    }

    public String getSensorRangeMax() {
        return sensorRangeMax;
    }

    public void setSensorRangeMax(String sensorRangeMax) {
        this.sensorRangeMax = sensorRangeMax;
    }

    public String getSensorMeasurementValue() {
        return sensorMeasurementValue;
    }

    public void setSensorMeasurementValue(String sensorMeasurementValue) {
        this.sensorMeasurementValue = sensorMeasurementValue;
    }

    public String getSensorErrorFormula() {
        return sensorErrorFormula;
    }

    public void setSensorErrorFormula(String sensorErrorFormula) {
        this.sensorErrorFormula = sensorErrorFormula;
    }
}
