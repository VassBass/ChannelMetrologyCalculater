package sensors;

import constants.MeasurementConstants;
import constants.SensorType;
import support.Channel;

public class TCM_50M extends Sensor {

    public TCM_50M (){
        this.type = SensorType.TCM_50M;
        this.rangeMin = -50D;
        this.rangeMax = 180D;
        this.value = MeasurementConstants.OM.getValue();
        this.measurement = MeasurementConstants.TEMPERATURE.getValue();
    }
}
