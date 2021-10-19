package sensors;

import constants.MeasurementConstants;
import constants.SensorType;
import support.Channel;

public class TXA_0395_typeK extends Sensor {

    public TXA_0395_typeK(){
        this.type = SensorType.TXA_0395_typeK;
        this.rangeMin = -50D;
        this.rangeMax = 1250D;
        this.value = MeasurementConstants.MV.getValue();
        this.measurement = MeasurementConstants.TEMPERATURE.getValue();
    }
}
