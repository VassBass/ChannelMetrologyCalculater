package model.dto.builder;

import model.dto.Channel;
import model.dto.Measurement;

import javax.annotation.Nonnull;

import static org.apache.commons.lang3.StringUtils.EMPTY;

public class ChannelBuilder {
    private final Channel channel;

    public ChannelBuilder() {
        channel = new Channel(EMPTY);
    }

    public ChannelBuilder(@Nonnull String code) {
        channel = new Channel(code);
    }

    public ChannelBuilder(@Nonnull Channel channel) {
        this.channel = new Channel(channel.getCode());
        this.channel.setName(channel.getName());
        this.channel.setMeasurementName(channel.getMeasurementName());
        this.channel.setMeasurementValue(channel.getMeasurementValue());
        this.channel.setDepartment(channel.getDepartment());
        this.channel.setArea(channel.getArea());
        this.channel.setProcess(channel.getProcess());
        this.channel.setInstallation(channel.getInstallation());
        this.channel.setDate(channel.getDate());
        this.channel.setFrequency(channel.getFrequency());
        this.channel.setTechnologyNumber(channel.getTechnologyNumber());
        this.channel.setNumberOfProtocol(channel.getNumberOfProtocol());
        this.channel.setReference(channel.getReference());
        this.channel.setRange(channel.getRangeMin(), channel.getRangeMax());
        this.channel.setAllowableError(channel.getAllowableErrorPercent(), channel.getAllowableErrorValue());
        this.channel.setSuitability(channel.isSuitability());
    }

    public ChannelBuilder setCode(@Nonnull String code) {
        channel.setCode(code);
        return this;
    }

    public ChannelBuilder setName(@Nonnull String name) {
        channel.setName(name);
        return this;
    }

    public ChannelBuilder setMeasurementName(String measurementName) {
        channel.setMeasurementName(measurementName == null ? EMPTY : measurementName);
        return this;
    }

    public ChannelBuilder setMeasurementValue(@Nonnull String measurementValue) {
        channel.setMeasurementValue(measurementValue);
        return this;
    }

    public ChannelBuilder setMeasurement(Measurement measurement) {
        channel.setMeasurementName(measurement.getName());
        channel.setMeasurementValue(measurement.getValue());
        return this;
    }

    public ChannelBuilder setDepartment(@Nonnull String department) {
        channel.setDepartment(department);
        return this;
    }

    public ChannelBuilder setArea(@Nonnull String area) {
        channel.setArea(area);
        return this;
    }

    public ChannelBuilder setProcess(@Nonnull String process) {
        channel.setProcess(process);
        return this;
    }

    public ChannelBuilder setInstallation(@Nonnull String installation) {
        channel.setInstallation(installation);
        return this;
    }

    public ChannelBuilder setDate(@Nonnull String date) {
        channel.setDate(date);
        return this;
    }

    public ChannelBuilder setFrequency(double frequency) {
        channel.setFrequency(frequency);
        return this;
    }

    public ChannelBuilder setTechnologyNumber(@Nonnull String number) {
        channel.setTechnologyNumber(number);
        return this;
    }

    public ChannelBuilder setNumberOfProtocol(@Nonnull String number) {
        channel.setNumberOfProtocol(number);
        return this;
    }

    public ChannelBuilder setRangeMin(double rangeMin) {
        channel.setRangeMin(rangeMin);
        return this;
    }

    public ChannelBuilder setRangeMax(double rangeMax) {
        channel.setRangeMax(rangeMax);
        return this;
    }

    public ChannelBuilder setRange(double r1, double r2) {
        if (r1 > r2) {
            r1 = r1 + r2;
            r2 = r1 - r2;
            r1 -= r2;
        }
        channel.setRangeMin(r1);
        channel.setRangeMax(r2);

        return this;
    }

    public ChannelBuilder setReference(@Nonnull String reference){
        channel.setReference(reference);
        return this;
    }

    public ChannelBuilder setSuitability(boolean suitability){
        channel.setSuitability(suitability);
        return this;
    }

    public ChannelBuilder setAllowableErrorInPercent(double value) {
        channel.setAllowableError(value, channel.getAllowableErrorValue());
        return this;
    }

    public ChannelBuilder setAllowableErrorInValue(double value) {
        channel.setAllowableError(channel.getAllowableErrorPercent(), value);
        return this;
    }

    public Channel build() {
        double rangeMin = channel.getRangeMin();
        double rangeMax = channel.getRangeMax();
        if (rangeMin > rangeMax) {
            rangeMin = rangeMin + rangeMax;
            rangeMax = rangeMin - rangeMax;
            rangeMin = rangeMin - rangeMax;
            channel.setRangeMin(rangeMin);
            channel.setRangeMax(rangeMax);
        }

        double allowableErrorPercent = channel.getAllowableErrorPercent();
        double allowableErrorValue = channel.getAllowableErrorValue();
        if (allowableErrorValue == 0D && allowableErrorPercent != 0D) {
            allowableErrorValue = (channel.calculateRange() / 100) * allowableErrorPercent;
            channel.setAllowableError(allowableErrorPercent, allowableErrorValue);
        }
        if (allowableErrorPercent == 0D && allowableErrorValue != 0D) {
            allowableErrorPercent = allowableErrorValue / (channel.calculateRange() / 100);
            channel.setAllowableError(allowableErrorPercent, allowableErrorValue);
        }

        return channel;
    }
}
