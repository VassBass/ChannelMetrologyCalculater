package measurements;

import constants.MeasurementConstants;
import model.Measurement;

public class Temperature extends Measurement {

    public Temperature(MeasurementConstants value){
        super(MeasurementConstants.TEMPERATURE, value);
    }
}
