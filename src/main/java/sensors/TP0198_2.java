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

    @Override
    public double[] getValuesElectro(Channel channel) {
        double[] valuesElectro = new double[5];
        if (channel.getRangeMin() == 0D && channel.getRangeMax() == 250D){
            valuesElectro[0] = 0D;
            valuesElectro[1] = 0.517;
            valuesElectro[2] = 5.124;
            valuesElectro[3] = 9.666;
            valuesElectro[4] = 10.153;
        } else if (channel.getRangeMin() == 0D && channel.getRangeMax() == 450D){
            valuesElectro[0] = 0D;
            valuesElectro[1] = 0.879;
            valuesElectro[2] = 9.141;
            valuesElectro[3] = 17.582;
            valuesElectro[4] = 18.516;
        } else if (channel.getRangeMin() == 0D && channel.getRangeMax() == 600D){
            valuesElectro[0] = 0D;
            valuesElectro[1] = 1.203;
            valuesElectro[2] = 12.209;
            valuesElectro[3] = 23.629;
            valuesElectro[4] = 24.905;
        }else if (channel.getRangeMin() == 0D && channel.getRangeMax() == 800D){
            valuesElectro[0] = 0D;
            valuesElectro[1] = 1.612;
            valuesElectro[2] = 16.397;
            valuesElectro[3] = 31.628;
            valuesElectro[4] = 33.275;
        } else if (channel.getRangeMin() == 0D && channel.getRangeMax() == 1250D){
            valuesElectro[0] = 0D;
            valuesElectro[1] = 2.561;
            valuesElectro[2] = 25.967;
            valuesElectro[3] = 48.399;
            valuesElectro[4] = 50.644;
        } else if (channel.getRangeMin() == 40D && channel.getRangeMax() == 1100D){
            valuesElectro[0] = 1.612;
            valuesElectro[1] = 3.806;
            valuesElectro[2] = 23.629;
            valuesElectro[3] = 43.096;
            valuesElectro[4] = 45.119;
        }
        return valuesElectro;
    }

    @Override
    public double[] getValues(Channel channel) {
        double[]values = new double[5];
        if (channel.getRangeMin() == 0D && channel.getRangeMax() == 250D){
            values[0] = 0D;
            values[1] = 13D;
            values[2] = 125D;
            values[3] = 238D;
            values[4] = 250D;
        } else if (channel.getRangeMin() == 0D && channel.getRangeMax() == 450D){
            values[0] = 0D;
            values[1] = 22;
            values[2] = 225D;
            values[3] = 428D;
            values[4] = 450D;
        } else if (channel.getRangeMin() == 0D && channel.getRangeMax() == 600D){
            values[0] = 0D;
            values[1] = 30D;
            values[2] = 300D;
            values[3] = 570D;
            values[4] = 600D;
        }else if (channel.getRangeMin() == 0D && channel.getRangeMax() == 800D){
            values[0] = 0D;
            values[1] = 40D;
            values[2] = 400D;
            values[3] = 760D;
            values[4] = 800D;
        } else if (channel.getRangeMin() == 0D && channel.getRangeMax() == 1250D){
            values[0] = 0D;
            values[1] = 63D;
            values[2] = 625D;
            values[3] = 1188D;
            values[4] = 1250D;
        } else if (channel.getRangeMin() == 40D && channel.getRangeMax() == 1100D){
            values[0] = 40D;
            values[1] = 93D;
            values[2] = 570D;
            values[3] = 1047D;
            values[4] = 1100D;
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
