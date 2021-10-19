package sensors;

import constants.MeasurementConstants;
import constants.SensorType;
import support.Channel;

public class JUMO_dTRANS_p02 extends Sensor {

    public JUMO_dTRANS_p02(){
        this.type = SensorType.JUMO_dTRANS_p02;
        this.measurement = MeasurementConstants.PRESSURE.getValue();
    }
}
