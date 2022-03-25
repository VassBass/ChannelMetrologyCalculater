package def;

import model.Measurement;

import java.util.ArrayList;

import static constants.MeasurementConstants.*;

public class DefaultMeasurements {
    public static ArrayList<Measurement> get(){
        ArrayList<Measurement>measurements = new ArrayList<>();

        measurements.add(new Measurement(TEMPERATURE, DEGREE_CELSIUS));

        measurements.add(new Measurement(PRESSURE, KPA));
        measurements.add(new Measurement(PRESSURE, PA));
        measurements.add(new Measurement(PRESSURE, MM_ACVA));
        measurements.add(new Measurement(PRESSURE, KGS_SM2));
        measurements.add(new Measurement(PRESSURE, KGS_MM2));
        measurements.add(new Measurement(PRESSURE, BAR));
        measurements.add(new Measurement(PRESSURE, ML_BAR));

        measurements.add(new Measurement(CONSUMPTION, M3_HOUR));

        return measurements;
    }
}
