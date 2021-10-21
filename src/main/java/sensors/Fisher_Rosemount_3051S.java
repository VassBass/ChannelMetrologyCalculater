package sensors;

import constants.MeasurementConstants;
import constants.SensorType;

public class Fisher_Rosemount_3051S extends Sensor {

    public Fisher_Rosemount_3051S(){
        this.type = SensorType.FISHER_ROSEMOUNT_3051S;
        this.measurement = MeasurementConstants.PRESSURE.getValue();
    }
}
