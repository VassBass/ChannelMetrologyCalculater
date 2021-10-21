package sensors;

import constants.MeasurementConstants;
import constants.SensorType;

public class TCP_100 extends Sensor {

    public TCP_100(){
        this.type = SensorType.TCP_100;
        this.rangeMin = -50D;
        this.rangeMax = 500D;
        this.value = MeasurementConstants.OM.getValue();
        this.measurement = MeasurementConstants.TEMPERATURE.getValue();
    }
}
