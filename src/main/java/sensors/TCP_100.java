package sensors;

import constants.MeasurementConstants;
import constants.SensorType;
import support.Channel;

public class TCP_100 extends Sensor {

    public TCP_100(){
        this.type = SensorType.TCP_100;
        this.rangeMin = -50D;
        this.rangeMax = 500D;
        this.value = MeasurementConstants.OM.getValue();
        this.measurement = MeasurementConstants.TEMPERATURE.getValue();
    }

    @Override
    public double[] getValuesElectro(Channel channel) {
        double[] valuesElectro = new double[5];
        if (channel.getRangeMin() == 0D && channel.getRangeMax() == 100D) {
            valuesElectro[0] = 100D;
            valuesElectro[1] = 102D;
            valuesElectro[2] = 119.4;
            valuesElectro[3] = 136.6;
            valuesElectro[4] = 138.5;
        } else if (channel.getRangeMin() == 0D && channel.getRangeMax() == 125D) {
            valuesElectro[0] = 100D;
            valuesElectro[1] = 102.4;
            valuesElectro[2] = 124.2;
            valuesElectro[3] = 145.6;
            valuesElectro[4] = 148D;
        } else if (channel.getRangeMin() == 0D && channel.getRangeMax() == 150D) {
            valuesElectro[0] = 100D;
            valuesElectro[1] = 102.9;
            valuesElectro[2] = 129D;
            valuesElectro[3] = 154.5;
            valuesElectro[4] = 157.3;
        } else if (channel.getRangeMin() == -20D && channel.getRangeMax() == 125D) {
            valuesElectro[0] = 92.1;
            valuesElectro[1] = 94.9;
            valuesElectro[2] = 120.4;
            valuesElectro[3] = 145.3;
            valuesElectro[4] = 148D;
        } else if (channel.getRangeMin() == -20D && channel.getRangeMax() == 150D) {
            valuesElectro[0] = 92.1;
            valuesElectro[1] = 95.5;
            valuesElectro[2] = 125.1;
            valuesElectro[3] = 154.1;
            valuesElectro[4] = 157.3;
        } else if (channel.getRangeMin() == -50D && channel.getRangeMax() == 100D) {
            valuesElectro[0] = 80.3;
            valuesElectro[1] = 83D;
            valuesElectro[2] = 109.7;
            valuesElectro[3] = 135.7;
            valuesElectro[4] = 138.5;
        }else if (channel.getRangeMin() == 0D && channel.getRangeMax() == 160D){
            valuesElectro[0] = 100D;
            valuesElectro[1] = 112.5;
            valuesElectro[2] = 130.9;
            valuesElectro[3] = 149.1;
            valuesElectro[4] = 161D;
        }
        return valuesElectro;
    }

    @Override
    public double[] getValues(Channel channel) {
        double[]values = new double[5];
        if (channel.getRangeMin() == 0D && channel.getRangeMax() == 100D) {
            values[0] = 0D;
            values[1] = 5.12;
            values[2] = 50D;
            values[3] = 94.98;
            values[4] = 100D;
        } else if (channel.getRangeMin() == 0D && channel.getRangeMax() == 125D) {
            values[0] = 0D;
            values[1] = 6.15;
            values[2] = 62.5;
            values[3] = 118.76;
            values[4] = 125.13;
        } else if (channel.getRangeMin() == 0D && channel.getRangeMax() == 150D) {
            values[0] = 0D;
            values[1] = 7.43;
            values[2] = 75.03;
            values[3] = 142.4;
            values[4] = 149.93;
        } else if (channel.getRangeMin() == -20D && channel.getRangeMax() == 125D) {
            values[0] = -20.15;
            values[1] = -13.02;
            values[2] = 52.61;
            values[3] = 117.96;
            values[4] = 125.13;
        } else if (channel.getRangeMin() == -20D && channel.getRangeMax() == 150D) {
            values[0] = -20.15;
            values[1] = -11.67;
            values[2] = 64.84;
            values[3] = 141.38;
            values[4] = 149.93;
        } else if (channel.getRangeMin() == -50D && channel.getRangeMax() == 100D) {
            values[0] = -50D;
            values[1] = -42.2;
            values[2] = 25D;
            values[3] = 92.5;
            values[4] = 100D;
        }else if (channel.getRangeMin() == 0D && channel.getRangeMax() == 160D){
            values[0] = 0D;
            values[1] = 32.14;
            values[2] = 80.03;
            values[3] = 128.09;
            values[4] = 159.89;
        } else {
            double rangeChannel = channel.getRangeMax() - channel.getRangeMin();
            double percent = rangeChannel / 100;

            values[0] = channel.getRangeMin();
            double v1 = percent * 5D;
            values[1] = channel.getRangeMin() + v1;
            double v2 = percent * 50D;
            values[2] = channel.getRangeMin() + v2;
            double v3 = percent * 95D;
            values[3] = channel.getRangeMin() + v3;
            values[4] = channel.getRangeMax();
        }
        return values;
    }
}
