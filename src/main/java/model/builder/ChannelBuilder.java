package model.builder;

import model.Channel;

import javax.annotation.Nonnull;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.EMPTY;

public class ChannelBuilder {
    private final Channel channel;

    public ChannelBuilder() {
        channel = new Channel(EMPTY);
    }

    public ChannelBuilder(@Nonnull String code) {
        channel = new Channel(code);
    }

    public ChannelBuilder setCode(@Nonnull String code) {
        channel.setCode(code);
        return this;
    }

    public ChannelBuilder setName(@Nonnull String name) {
        this.channel.setName(name);
        return this;
    }

    public ChannelBuilder setMeasurementValue(@Nonnull String measurementValue) {
        this.channel.setMeasurementValue(measurementValue);
        return this;
    }

    public ChannelBuilder setDepartment(@Nonnull String department) {
        this.channel.setDepartment(department);
        return this;
    }

    public ChannelBuilder setArea(@Nonnull String area) {
        this.channel.setArea(area);
        return this;
    }

    public ChannelBuilder setProcess(@Nonnull String process) {
        this.channel.setProcess(process);
        return this;
    }

    public ChannelBuilder setInstallation(@Nonnull String installation) {
        this.channel.setInstallation(installation);
        return this;
    }

    public ChannelBuilder setDate(@Nonnull String date) {
        this.channel.setDate(date);
        return this;
    }

    public ChannelBuilder setFrequency(double frequency) {
        this.channel.setFrequency(frequency);
        return this;
    }

    public ChannelBuilder setTechnologyNumber(@Nonnull String number) {
        this.channel.setTechnologyNumber(number);
        return this;
    }

    public ChannelBuilder setSensorName(@Nonnull String sensorName) {
        this.channel.setSensorName(sensorName);
        return this;
    }

    public ChannelBuilder setNumberOfProtocol(@Nonnull String number) {
        this.channel.setNumberOfProtocol(number);
        return this;
    }

    public ChannelBuilder setRangeMin(double rangeMin) {
        this.channel.setRangeMin(rangeMin);
        return this;
    }

    public ChannelBuilder setRangeMax(double rangeMax) {
        this.channel.setRangeMax(rangeMax);
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
        this.channel.setReference(reference);
        return this;
    }

    public ChannelBuilder setSuitability(boolean suitability){
        this.channel.setSuitability(suitability);
        return this;
    }

    public ChannelBuilder setControlPoints(@Nonnull Map<Double, Double> controlPoints){
        this.channel.setControlPoints(controlPoints);
        return this;
    }

    public ChannelBuilder setAllowableErrorInPercent(double value) {
        this.channel.setAllowableError(value, channel.getAllowableError());
        return this;
    }

    public ChannelBuilder setAllowableErrorInValue(double value) {
        this.channel.setAllowableError(channel.getAllowableErrorPercent(), value);
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
        double allowableErrorValue = channel.getAllowableError();
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
