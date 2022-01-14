package calculation;

import constants.CalibratorType;
import constants.MeasurementConstants;
import model.Calibrator;
import model.Channel;
import org.apache.commons.lang3.math.NumberUtils;

public class CalculationConsumption extends Calculation {

    public CalculationConsumption(Channel channel) {
        super(channel);
    }

    @Override
    public boolean goodChannel() {
        return this.getErrorInRange() <= this.channel.getAllowableErrorPercent();
    }

    @Override
    public void setCalibrator(Calibrator calibrator) {
        super.setCalibrator(calibrator);
        this.calibrator.setValue(this.channel.getMeasurement().getValue());
    }

    @Override
    public boolean closeToFalse() {
        double result = Math.abs(this.getErrorInRange() - this.channel.getAllowableErrorPercent());
        return result < 0.1;
    }

    @Override
    public double[][] getErrorsAbsolute() {
        if (this.calibrator.getName().equals(CalibratorType.ROSEMOUNT_8714DQ4)){
            String value = this.channel.getMeasurement().getValue();
            double value0 = 0D;
            double value91 = 0.91;
            double value305 = 3.05;
            double value914 = 9.14;
            if (value.equals(MeasurementConstants.CM_S.getValue())){
                value91 = value91 * 100;
                value305 = value305 * 100;
                value914 = value914 * 100;
            }
            this.errorsAbsolute = new double[in.length][8];
            for (int n = 0; n < in.length; n++) {
                this.errorsAbsolute[n][0] = in[n][0] - value0;
                this.errorsAbsolute[n][1] = in[n][1] - value0;
                this.errorsAbsolute[n][2] = in[n][2] - value91;
                this.errorsAbsolute[n][3] = in[n][3] - value91;
                this.errorsAbsolute[n][4] = in[n][4] - value305;
                this.errorsAbsolute[n][5] = in[n][5] - value305;
                this.errorsAbsolute[n][6] = in[n][6] - value914;
                this.errorsAbsolute[n][7] = in[n][7] - value914;
            }
        }else {
            double value0 = this.channel.getRangeMin();
            double value25 = ((this.channel.getRange() / 100) * 25) + this.channel.getRangeMin();
            double value50 = ((this.channel.getRange() / 100) * 50) + this.channel.getRangeMin();
            double value75 = ((this.channel.getRange() / 100) * 75) + this.channel.getRangeMin();
            double value100 = this.channel.getRangeMax();
            this.errorsAbsolute = new double[in.length][10];
            for (int n = 0; n < in.length; n++) {
                this.errorsAbsolute[n][0] = in[n][0] - value0;
                this.errorsAbsolute[n][1] = in[n][1] - value0;
                this.errorsAbsolute[n][2] = in[n][2] - value25;
                this.errorsAbsolute[n][3] = in[n][3] - value25;
                this.errorsAbsolute[n][4] = in[n][4] - value50;
                this.errorsAbsolute[n][5] = in[n][5] - value50;
                this.errorsAbsolute[n][6] = in[n][6] - value75;
                this.errorsAbsolute[n][7] = in[n][7] - value75;
                this.errorsAbsolute[n][8] = in[n][8] - value100;
                this.errorsAbsolute[n][9] = in[n][9] - value100;
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
            double rangeChannel = this.channel.getRange();
            this.errorD = (error * 100D) / rangeChannel;
        }
        return this.errorD;
    }

    @Override
    public double getErrorInRangeWidthSensorError(){
        if (this.errorD == -999999999D){
            double error = this.getAbsoluteErrorWithSensorError();
            double rangeChannel = this.channel.getRange();
            this.errorD = (error * 100D) / rangeChannel;
        }
        return this.errorD;
    }

    public double[] getSystematicErrors(){
        if (this.errorsS == null){
            double[][]errorsAbsolute = this.getErrorsAbsolute();
            if (this.calibrator.getName().equals(CalibratorType.ROSEMOUNT_8714DQ4)){
                this.errorsS = new double[4];

                double s0 = 0D;
                for (double[] doubles : errorsAbsolute) {
                    s0 = s0 + (doubles[0] + doubles[1]);
                }
                double S0 = s0 / (errorsAbsolute.length * 2);
                if (S0 < 0.0005 && S0 > -0.0005) {
                    this.errorsS[0] = Math.abs(S0);
                } else {
                    this.errorsS[0] = S0;
                }

                double s91 = 0D;
                for (double[] doubles : errorsAbsolute) {
                    s91 = s91 + (doubles[2] + doubles[3]);
                }
                double S91 = s91 / (errorsAbsolute.length * 2);
                if (S91 < 0.0005 && S91 > -0.0005) {
                    this.errorsS[1] = Math.abs(S91);
                } else {
                    this.errorsS[1] = S91;
                }

                double s305 = 0D;
                for (double[] doubles : errorsAbsolute) {
                    s305 = s305 + (doubles[4] + doubles[5]);
                }
                double S305 = s305 / (errorsAbsolute.length * 2);
                if (S305 < 0.0005 && S305 > -0.0005) {
                    this.errorsS[2] = Math.abs(S305);
                } else {
                    this.errorsS[2] = S305;
                }

                double s914 = 0D;
                for (double[] doubles : errorsAbsolute) {
                    s914 = s914 + (doubles[6] + doubles[7]);
                }
                double S914 = s914 / (errorsAbsolute.length * 2);
                if (S914 < 0.0005 && S914 > -0.0005) {
                    this.errorsS[3] = Math.abs(S914);
                } else {
                    this.errorsS[3] = S914;
                }
            }else {
                this.errorsS = new double[5];

                double s0 = 0D;
                for (double[] doubles : errorsAbsolute) {
                    s0 = s0 + (doubles[0] + doubles[1]);
                }
                double S0 = s0 / (errorsAbsolute.length * 2);
                if (S0 < 0.0005 && S0 > -0.0005) {
                    this.errorsS[0] = Math.abs(S0);
                } else {
                    this.errorsS[0] = S0;
                }

                double s25 = 0D;
                for (double[] doubles : errorsAbsolute) {
                    s25 = s25 + (doubles[2] + doubles[3]);
                }
                double S25 = s25 / (errorsAbsolute.length * 2);
                if (S25 < 0.0005 && S25 > -0.0005) {
                    this.errorsS[1] = Math.abs(S25);
                } else {
                    this.errorsS[1] = S25;
                }

                double s50 = 0D;
                for (double[] doubles : errorsAbsolute) {
                    s50 = s50 + (doubles[4] + doubles[5]);
                }
                double S50 = s50 / (errorsAbsolute.length * 2);
                if (S50 < 0.0005 && S50 > -0.0005) {
                    this.errorsS[2] = Math.abs(S50);
                } else {
                    this.errorsS[2] = S50;
                }

                double s75 = 0D;
                for (double[] doubles : errorsAbsolute) {
                    s75 = s75 + (doubles[6] + doubles[7]);
                }
                double S75 = s75 / (errorsAbsolute.length * 2);
                if (S75 < 0.0005 && S75 > -0.0005) {
                    this.errorsS[3] = Math.abs(S75);
                } else {
                    this.errorsS[3] = S75;
                }

                double s100 = 0D;
                for (double[] doubles : errorsAbsolute) {
                    s100 = s100 + (doubles[8] + doubles[9]);
                }
                double S100 = s100 / (errorsAbsolute.length * 2);
                if (S100 < 0.0005 && S100 > -0.0005) {
                    this.errorsS[4] = Math.abs(S100);
                } else {
                    this.errorsS[4] = S100;
                }
            }
        }
        return this.errorsS;
    }

    @Override
    public double getStandardIndeterminacyA(){
        if (this.uA == -999999999D){
            double[][]errorsAbsolute = this.getErrorsAbsolute();
            double[] sum;
            if (this.calibrator.getName().equals(CalibratorType.ROSEMOUNT_8714DQ4)){
                sum = new double[4];
                for (double[] doubles : errorsAbsolute) {
                    sum[0] += Math.pow(doubles[0], 2D) + Math.pow(doubles[1], 2D);
                    sum[1] += Math.pow(doubles[2], 2D) + Math.pow(doubles[3], 2D);
                    sum[2] += Math.pow(doubles[4], 2D) + Math.pow(doubles[5], 2D);
                    sum[3] += Math.pow(doubles[6], 2D) + Math.pow(doubles[7], 2D);
                }
                sum[0] = Math.sqrt(sum[0]/20);
                sum[1] = Math.sqrt(sum[1]/20);
                sum[2] = Math.sqrt(sum[2]/20);
                sum[3] = Math.sqrt(sum[3]/20);
            }else {
                sum = new double[5];
                for (double[] doubles : errorsAbsolute) {
                    sum[0] += Math.pow(doubles[0], 2D) + Math.pow(doubles[1], 2D);
                    sum[1] += Math.pow(doubles[2], 2D) + Math.pow(doubles[3], 2D);
                    sum[2] += Math.pow(doubles[4], 2D) + Math.pow(doubles[5], 2D);
                    sum[3] += Math.pow(doubles[6], 2D) + Math.pow(doubles[7], 2D);
                    sum[4] += Math.pow(doubles[8], 2D) + Math.pow(doubles[9], 2D);
                }
                sum[0] = Math.sqrt(sum[0] / 20);
                sum[1] = Math.sqrt(sum[1] / 20);
                sum[2] = Math.sqrt(sum[2] / 20);
                sum[3] = Math.sqrt(sum[3] / 20);
                sum[4] = Math.sqrt(sum[4] / 20);
            }
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