package def;

import model.Measurement;

import java.util.ArrayList;

public class DefaultMeasurements {
    public static ArrayList<Measurement> get(){
        ArrayList<Measurement>measurements = new ArrayList<>();

        measurements.add(new Measurement(Measurement.TEMPERATURE, Measurement.DEGREE_CELSIUS));

        measurements.add(new Measurement(Measurement.PRESSURE, Measurement.KPA));
        measurements.add(new Measurement(Measurement.PRESSURE, Measurement.PA));
        measurements.add(new Measurement(Measurement.PRESSURE, Measurement.MM_ACVA));
        measurements.add(new Measurement(Measurement.PRESSURE, Measurement.KGS_SM2));
        measurements.add(new Measurement(Measurement.PRESSURE, Measurement.KGS_MM2));
        measurements.add(new Measurement(Measurement.PRESSURE, Measurement.BAR));
        measurements.add(new Measurement(Measurement.PRESSURE, Measurement.ML_BAR));

        measurements.add(new Measurement(Measurement.CONSUMPTION, Measurement.M3_HOUR));

        return measurements;
    }
}
