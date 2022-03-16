package def;

import constants.MeasurementConstants;
import measurements.Consumption;
import model.Measurement;
import measurements.Pressure;
import measurements.Temperature;

import java.util.ArrayList;

public class DefaultMeasurements {
    public static ArrayList<Measurement> get(){
        ArrayList<Measurement>measurements = new ArrayList<>();

        measurements.add(new Temperature(MeasurementConstants.DEGREE_CELSIUS));

        measurements.add(new Pressure(MeasurementConstants.KPA));
        measurements.add(new Pressure(MeasurementConstants.PA));
        measurements.add(new Pressure(MeasurementConstants.MM_ACVA));
        measurements.add(new Pressure(MeasurementConstants.KGS_SM2));
        measurements.add(new Pressure(MeasurementConstants.KGS_MM2));
        measurements.add(new Pressure(MeasurementConstants.BAR));
        measurements.add(new Pressure(MeasurementConstants.ML_BAR));

        measurements.add(new Consumption(MeasurementConstants.M3_HOUR));

        return measurements;
    }
}
