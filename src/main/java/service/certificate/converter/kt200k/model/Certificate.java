package service.certificate.converter.kt200k.model;

import model.dto.Person;

import java.util.List;

public class Certificate {
    private String protocolNumber;
    private String channelName;
    private String environmentTemperature;
    private String environmentPressure;
    private String environmentHumidity;
    private Sensor benchmarkSensor;
    private List<Sensor> sensors;
    private Person worker;
    private Person headOfMetrologyDepartment;
    private Person sensorOwner;

    public String getProtocolNumber() {
        return protocolNumber;
    }

    public void setProtocolNumber(String protocolNumber) {
        this.protocolNumber = protocolNumber;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getEnvironmentTemperature() {
        return environmentTemperature;
    }

    public void setEnvironmentTemperature(String environmentTemperature) {
        this.environmentTemperature = environmentTemperature;
    }

    public String getEnvironmentPressure() {
        return environmentPressure;
    }

    public void setEnvironmentPressure(String environmentPressure) {
        this.environmentPressure = environmentPressure;
    }

    public String getEnvironmentHumidity() {
        return environmentHumidity;
    }

    public void setEnvironmentHumidity(String environmentHumidity) {
        this.environmentHumidity = environmentHumidity;
    }

    public Sensor getBenchmarkSensor() {
        return benchmarkSensor;
    }

    public void setBenchmarkSensor(Sensor benchmarkSensor) {
        this.benchmarkSensor = benchmarkSensor;
    }

    public List<Sensor> getSensors() {
        return sensors;
    }

    public void setSensors(List<Sensor> sensors) {
        this.sensors = sensors;
    }

    public Person getWorker() {
        return worker;
    }

    public void setWorker(Person worker) {
        this.worker = worker;
    }

    public Person getHeadOfMetrologyDepartment() {
        return headOfMetrologyDepartment;
    }

    public void setHeadOfMetrologyDepartment(Person headOfMetrologyDepartment) {
        this.headOfMetrologyDepartment = headOfMetrologyDepartment;
    }

    public Person getSensorOwner() {
        return sensorOwner;
    }

    public void setSensorOwner(Person sensorOwner) {
        this.sensorOwner = sensorOwner;
    }

    @Override
    public String toString() {
        return "Certificate{" +
                "\n\tprotocolNumber=" + protocolNumber +
                ",\n\tchannelName=" + channelName +
                ",\n\tenvironmentTemperature=" + environmentTemperature +
                ",\n\tenvironmentPressure=" + environmentPressure +
                ",\n\tenvironmentHumidity=" + environmentHumidity +
                ",\n\tbenchmarkSensor=" + benchmarkSensor +
                ",\n\tsensors=" + sensors +
                ",\n\tworker=" + worker +
                ",\n\theadOfMetrologyDepartment=" + headOfMetrologyDepartment +
                ",\n\tsensorOwner=" + sensorOwner +
                "\n}";
    }
}
