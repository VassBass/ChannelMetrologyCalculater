package sensors;


import constants.MeasurementConstants;
import constants.SensorType;
import support.Channel;

public class Yokogawa extends Sensor {

    public Yokogawa(){
        this.type = SensorType.YOKOGAWA;
        this.measurement = MeasurementConstants.PRESSURE.getValue();
    }
}
