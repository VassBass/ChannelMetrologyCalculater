package sensors;

import constants.MeasurementConstants;
import constants.SensorType;
import support.Channel;

public class TP0198_2 extends Sensor {

    public TP0198_2(){
        this.type = SensorType.TP0198_2;
        this.rangeMin = -40D;
        this.rangeMax = 1100D;
        this.value = MeasurementConstants.MV.getValue();
        this.measurement = MeasurementConstants.TEMPERATURE.getValue();
    }
}
