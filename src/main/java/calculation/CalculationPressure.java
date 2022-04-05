package calculation;

import converters.ValueConverter;
import model.Calibrator;
import model.Channel;
import model.Measurement;
import org.apache.commons.lang3.math.NumberUtils;

public class CalculationPressure extends Calculation {

    public CalculationPressure(Channel channel) {
        super(channel);
    }

    @Override
    public void setCalibrator(Calibrator calibrator) {
        super.setCalibrator(calibrator);
        if (calibrator.getType().equals(Calibrator.FLUKE718_30G)){
            this.maxCalibratorPower = new ValueConverter(Measurement.KGS_SM2, this.channel.getMeasurement().getValue()).get(-0.8);
        }
    }

    @Override
    public boolean goodChannel() {
        return this.getErrorInRangeWidthSensorError() <= this.channel.getAllowableErrorPercent();
    }

    @Override
    public boolean closeToFalse() {
        double result = Math.abs(this.getErrorInRangeWidthSensorError() - this.channel.getAllowableErrorPercent());
        return result < 0.1;
    }

    @Override
    public double[] getControlPointsValues() {
        if (this.controlPointsValues == null){
            this.controlPointsValues = new double[5];
            this.controlPointsValues[0] = this.channel.getRangeMin();
            this.controlPointsValues[1] = ((this.channel._getRange() / 100) * 5) + this.channel.getRangeMin();
            this.controlPointsValues[2] = ((this.channel._getRange() / 100) * 50) + this.channel.getRangeMin();
            this.controlPointsValues[3] = ((this.channel._getRange() / 100) * 95) + this.channel.getRangeMin();
            this.controlPointsValues[4] = this.channel.getRangeMax();
        }
        return this.controlPointsValues;
    }

    @Override
    public double[][] getErrorsAbsolute() {
        if (this.errorsAbsolute == null) {
            double[] values = this.getControlPointsValues();
            double value5 = values[1];
            double value50 = values[2];
            double value95 = values[3];
            if (maxCalibratorPower != 999999999D && value5 < maxCalibratorPower){
                value5 = maxCalibratorPower;
            }
            this.errorsAbsolute = new double[in.length][6];
            for (int n = 0; n < in.length; n++) {
                this.errorsAbsolute[n][0] = in[n][1] - value5;
                this.errorsAbsolute[n][1] = in[n][2] - value5;
                this.errorsAbsolute[n][2] = in[n][3] - value50;
                this.errorsAbsolute[n][3] = in[n][4] - value50;
                this.errorsAbsolute[n][4] = in[n][5] - value95;
                this.errorsAbsolute[n][5] = in[n][6] - value95;
            }
        }
        return this.errorsAbsolute;
    }

    @Override
    public double getMaxAbsoluteError(){
        if (this.errorMax == -999999999D){
            double[][]errorsAbsolute = this.getErrorsAbsolute();
            double[] biggest = new double[errorsAbsolute.length];
            double[] smallest = new double[errorsAbsolute.length];
            for (int n=0;n<errorsAbsolute.length;n++) {
                biggest[n] = NumberUtils.max(errorsAbsolute[n]);
                smallest[n] = NumberUtils.min(errorsAbsolute[n]);
            }

            double biggestNum = Math.abs(NumberUtils.max(biggest));
            double smallestNum = Math.abs(NumberUtils.min(smallest));

            this.errorMax = Math.max(biggestNum, smallestNum);

        }
        return this.errorMax;
    }

    @Override
    public double getAbsoluteErrorWithSensorError(){
        if (this.error == -999999999D){
            double errorMax = this.getMaxAbsoluteError();
            double errorSensor = this.channel.getSensor().getError(this.channel);
            double errorCalibrator = this.calibrator.getError(this.channel);
            double e = (errorMax * errorMax) + (errorSensor * errorSensor) + (errorCalibrator * errorCalibrator);
            this.error = Math.sqrt(e);
        }
        return this.error;
    }

    @Override
    public double getErrorInRange(){
        if (this.errorD == -999999999D){
            double error = this.getMaxAbsoluteError();
            double rangeChannel = this.channel._getRange();
            this.errorD = (error * 100D) / rangeChannel;
        }
        return this.errorD;
    }

    @Override
    public double getErrorInRangeWidthSensorError(){
        if (this.errorD == -999999999D){
            double error = this.getAbsoluteErrorWithSensorError();
            double rangeChannel = this.channel._getRange();
            this.errorD = (error * 100D) / rangeChannel;
        }
        return this.errorD;
    }

    @Override
    public double[] getSystematicErrors(){
        if (this.errorsS == null){
            double[][]errorsAbsolute = this.getErrorsAbsolute();
            this.errorsS = new double[3];

            double s5 = 0D;
            for (double[] doubles : errorsAbsolute) {
                s5 += (doubles[0] + doubles[1]);
            }
            double S5 = s5 / (errorsAbsolute.length * 2);
            if (S5 < 0.0005 && S5 > -0.0005){
                this.errorsS[0] = Math.abs(S5);
            }else {
                this.errorsS[0] = S5;
            }

            double s50 = 0D;
            for (double[] doubles : errorsAbsolute) {
                s50 += (doubles[2] + doubles[3]);
            }
            double S50 = s50 / (errorsAbsolute.length * 2);
            if (S50 < 0.0005 && S50 > -0.0005){
                this.errorsS[1] = Math.abs(S50);
            }else {
                this.errorsS[1] = S50;
            }

            double s95 = 0D;
            for (double[] doubles : errorsAbsolute) {
                s95 += (doubles[4] + doubles[5]);
            }
            double S95 = s95 / (errorsAbsolute.length * 2);
            if (S95 < 0.0005 && S95 > -0.0005){
                this.errorsS[2] = Math.abs(S95);
            }else {
                this.errorsS[2] = S95;
            }
        }
        return this.errorsS;
    }

    @Override
    public double getStandardIndeterminacyA(){
        if (this.uA == -999999999D){
            double[][]errorsAbsolute = this.getErrorsAbsolute();
            double[]sum = new double[3];
            for (double[] doubles : errorsAbsolute) {
                sum[0] += Math.pow(doubles[0], 2D) + Math.pow(doubles[1], 2D);
                sum[1] += Math.pow(doubles[2], 2D) + Math.pow(doubles[3], 2D);
                sum[2] += Math.pow(doubles[4], 2D) + Math.pow(doubles[5], 2D);
            }
            sum[0] = Math.sqrt(sum[0]/20);
            sum[1] = Math.sqrt(sum[1]/20);
            sum[2] = Math.sqrt(sum[2]/20);
            this.uA = NumberUtils.max(sum);
        }
        return this.uA;
    }

    @Override
    public double getStandardIndeterminacyB(){
        if (this.uB == -999999999D){
            double errorSensor = this.channel.getSensor().getError(this.channel);
            double errorCalibrator = this.calibrator.getError(this.channel);
            double sum = (errorSensor * errorSensor) + (errorCalibrator * errorCalibrator);
            this.uB = Math.sqrt(sum);
        }
        return this.uB;
    }

    @Override
    public double getStandardIndeterminacyTotal(){
        if (this.uC == -999999999D){
            double uA = this.getStandardIndeterminacyA();
            double uB = this.getStandardIndeterminacyB();
            double sum = (uA * uA) + (uB * uB);
            this.uC = Math.sqrt(sum);
        }
        return this.uC;
    }

    @Override
    public double getExtendedIndeterminacy(){
        if (this.u == -999999999D){
            double uC = this.getStandardIndeterminacyTotal();
            double k = 2D;
            this.u = k * uC;
        }
        return this.u;
    }
}