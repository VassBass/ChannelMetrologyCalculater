package sensors;

import constants.MeasurementConstants;
import constants.SensorType;

public class TXA_2388_typeK extends Sensor {

    public TXA_2388_typeK(){
        this.type = SensorType.TXA_2388_typeK;
        this.rangeMin = -50D;
        this.rangeMax = 1250D;
        this.value = MeasurementConstants.MV.getValue();
        this.measurement = MeasurementConstants.TEMPERATURE.getValue();
    }
}
