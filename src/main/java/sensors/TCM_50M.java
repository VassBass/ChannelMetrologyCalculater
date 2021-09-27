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

    @Override
    public double[]getValuesElectro(Channel channel){
        double[] valuesElectro = new double[5];
        if (channel.getRangeMin() == 0D && channel.getRangeMax() == 100D){
            valuesElectro[0] = 50D;
            valuesElectro[1] = 51.1;
            valuesElectro[2] = 60.7;
            valuesElectro[3] = 70.3;
            valuesElectro[4] = 71.4;
        }else if (channel.getRangeMin() == 0D && channel.getRangeMax() == 125D){
            valuesElectro[0] = 50D;
            valuesElectro[1] = 51.3;
            valuesElectro[2] = 63.3;
            valuesElectro[3] = 75.4;
            valuesElectro[4] = 76.7;
        }else if (channel.getRangeMin() == 0D && channel.getRangeMax() == 150D){
            valuesElectro[0] = 50D;
            valuesElectro[1] = 51.6;
            valuesElectro[2] = 66D;
            valuesElectro[3] = 80.5;
            valuesElectro[4] = 82.1;
        }else if (channel.getRangeMin() == -20D && channel.getRangeMax() == 125D){
            valuesElectro[0] = 45.7;
            valuesElectro[1] = 47.3;
            valuesElectro[2] = 61.2;
            valuesElectro[3] = 75.4;
            valuesElectro[4] = 76.7;
        }else if (channel.getRangeMin() == -20D && channel.getRangeMax() == 150D){
            valuesElectro[0] = 45.7;
            valuesElectro[1] = 47.5;
            valuesElectro[2] = 63.9;
            valuesElectro[3] = 80.3;
            valuesElectro[4] = 82.1;
        }else if (channel.getRangeMin() == -50D && channel.getRangeMax() == 180D){
            valuesElectro[0] = 39.2;
            valuesElectro[1] = 41.7;
            valuesElectro[2] = 63.9;
            valuesElectro[3] = 86D;
            valuesElectro[4] = 88.5;
        }
        return valuesElectro;
    }

    @Override
    public double[] getValues(Channel channel) {
        double[]values = new double[5];
        if (channel.getRangeMin() == 0D && channel.getRangeMax() == 100D){
            values[0] = 0D;
            values[1] = 5.14;
            values[2] = 50D;
            values[3] = 94.86;
            values[4] = 100.47;
        }else if (channel.getRangeMin() == 0D && channel.getRangeMax() == 125D){
            values[0] = 0D;
            values[1] = 6.08;
            values[2] = 62.15;
            values[3] = 118.69;
            values[4] = 125.35;
        }else if (channel.getRangeMin() == 0D && channel.getRangeMax() == 150D){
            values[0] = 0D;
            values[1] = 7.48;
            values[2] = 74.77;
            values[3] = 142.52;
            values[4] = 150.7;
        }else if (channel.getRangeMin() == -20D && channel.getRangeMax() == 125D){
            values[0] = -20D;
            values[1] = -12.6;
            values[2] = 52.34;
            values[3] = 118.69;
            values[4] = 125.35;
        }else if (channel.getRangeMin() == -20D && channel.getRangeMax() == 150D){
            values[0] = -20D;
            values[1] = -11.67;
            values[2] = 64.95;
            values[3] = 141.59;
            values[4] = 150D;
        }else if (channel.getRangeMin() == -50D && channel.getRangeMax() == 180D){
            values[0] = -50.13;
            values[1] = -39.59;
            values[2] = 64.95;
            values[3] = 168.22;
            values[4] = 179.9;
        }else {
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
