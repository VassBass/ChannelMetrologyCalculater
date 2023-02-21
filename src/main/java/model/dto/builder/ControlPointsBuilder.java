package model.dto.builder;

import model.dto.ControlPoints;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;

public class ControlPointsBuilder {
    private final ControlPoints controlPoints;

    public ControlPointsBuilder(){
        this.controlPoints = new ControlPoints();
    }

    public ControlPointsBuilder(@Nonnull String name) {
        this.controlPoints = new ControlPoints(name);
    }

    public ControlPointsBuilder setName(@Nonnull String name) {
        controlPoints.setName(name);
        return this;
    }

    public ControlPointsBuilder setSensorType(@Nonnull String sensorType) {
        controlPoints.setSensorType(sensorType);
        return this;
    }

    public ControlPointsBuilder setPoints(Map<Double, Double> points) {
        controlPoints.setValues(points);
        return this;
    }

    public ControlPointsBuilder addPercentValues(Double ... percentValues) {
        Map<Double, Double> values = controlPoints.getValues();
        for (Double v : percentValues) {
            if (v == null) continue;
            values.put(v, null);
        }
        controlPoints.setValues(values);
        return this;
    }

    public ControlPointsBuilder addControlPoint(@Nonnull Double percentPoint, @Nullable Double valuePoint) {
        Map<Double, Double> values = controlPoints.getValues();
        values.put(percentPoint, valuePoint);
        controlPoints.setValues(values);
        return this;
    }

    public ControlPoints build() {
        return this.controlPoints;
    }
}
