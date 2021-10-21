package sensors;

import constants.MeasurementConstants;
import constants.SensorType;

public class Deltabar_S extends Sensor {

    public Deltabar_S(){
        this.type = SensorType.DELTABAR_S;
        this.measurement = MeasurementConstants.PRESSURE.getValue();
    }
}
