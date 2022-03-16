package measurements;

import constants.MeasurementConstants;
import model.Measurement;

public class Pressure extends Measurement {

    public Pressure(MeasurementConstants value) {
        super(MeasurementConstants.PRESSURE, value);
    }
}
