package sensors;

import constants.MeasurementConstants;
import constants.SensorType;
import support.Channel;

public class Deltabar_S extends Sensor {

    public Deltabar_S(){
        this.type = SensorType.DELTABAR_S;
        this.measurement = MeasurementConstants.PRESSURE.getValue();
    }

    @Override
    public double[] getValues(Channel channel) {
        double[]values = new double[5];
        double rangeChannel = channel.getRangeMax() - channel.getRangeMin();

        values[0] = channel.getRangeMin();
        values[1] = ((rangeChannel / 100) * 5) + channel.getRangeMin();
        values[2] = ((rangeChannel / 100) * 50) + channel.getRangeMin();
        values[3] = ((rangeChannel / 100) * 95) + channel.getRangeMin();
        values[4] = channel.getRangeMax();

        return values;
    }
}
