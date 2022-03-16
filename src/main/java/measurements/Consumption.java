package measurements;

import constants.MeasurementConstants;
import model.Measurement;

public class Consumption extends Measurement {

    public Consumption(MeasurementConstants value){
        super(MeasurementConstants.CONSUMPTION, value);
    }
}
