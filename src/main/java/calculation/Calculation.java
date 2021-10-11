package calculation;

import constants.CalibratorType;
import constants.MeasurementConstants;
import support.Converter;
import calibrators.Calibrator;
import org.apache.commons.lang3.math.NumberUtils;
import sensors.*;
import support.Channel;
import constants.Strings;

public class Calculation {
    private final Channel channel;
    private final Method method;
    private Calibrator calibrator;
    /*
     * double[Quantity of measurements] [control points]
     * control points: for Method.MKMX №5300.01:18, Method.MKMX №5300.02:18
     * [0] = 0%
     * [1] = 5% Up
     * [2] = 5% Down
     * [3] = 50% Up
     * [4] = 50% Down
     * [5] = 95% Up
     * [6] = 95% Down
     * [7] = 100%
     */
    private double[][] in;

    private double rangeChannel = -999999999D;
    private double rangeSensor = -999999999D;

    /*
     * double[error in value] [error in percent]
     */
    private double[] errorSensor = null;
    private double[] errorCalibrator = null;

    /*
     * double[Quantity of measurements] [control points]
     * control points: for Method.MKMX №5300.01:18, Method.MKMX №5300.02:18
     * [0] = 5% Up
     * [1] = 5% Down
     * [2] = 50% Up
     * [3] = 50% Down
     * [4] = 95% Up
     * [5] = 95% Down
     */
    private double[][] errorsAbsolute = null;

    /*
     * The biggest absolute error from double[][]errorsAbsolute
     */
    private double errorMax = -999999999D;

    /*
     * Absolute error with sensor error
     */
    private double error = -999999999D;

    /*
     * Error in range
     */
    private double errorD = -999999999D;

    /*
     * Systematic errors
     * double[control points]
     * control points: for Method.MKMX №5300.01:18, Method.MKMX №5300.02:18
     * [0] = 5%
     * [1] = 50%
     * [2] = 95%
     */
    private double[]errorsS = null;

    /*
     * Standard indeterminacy type "A"
     */
    private double uA = -999999999D;

    /*
     * Standard indeterminacy type "B"
     */
    private double uB = -999999999D;

    /*
     * Total standard indeterminacy
     */
    private double uC = -999999999D;

    /*
     * Extended indeterminacy
     */
    private double u = -999999999D;

    private double maxCalibratorPower = -999999999D;

    public boolean closeToFalse(){
        double result = Math.abs(this.getErrorInRange() - this.channel.getAllowableErrorPercent());
        return result < 0.1;
    }

    public boolean goodChannel(){
        return this.getErrorInRange() <= this.channel.getAllowableErrorPercent();
    }

    public Calculation(Channel channel, Method method){
        this.channel = channel;
        this.method = method;
    }

    public void setIn(double[][]in){
        this.in = in;
    }

    public void setCalibrator(Calibrator calibrator){
        if (calibrator.getName() == CalibratorType.FLUKE718_30G){
            this.maxCalibratorPower = new Converter(MeasurementConstants.KG_SM2, this.channel.getMeasurement().getValueConstant()).get(-0.8);
        }
        this.calibrator = calibrator;
    }

    public double[][]getIn(){
        return this.in;
    }

    public String getName(){
        switch (this.method){
            case MKMX_5300_01_18:
                return Strings.MKMX_5300_01_18;
            case MKMX_5300_02_18:
                return Strings.MKMX_5300_02_18;
            default:
                return null;
        }
    }

    public double getRangeChannel(){
        if (this.rangeChannel == -999999999D){
            this.rangeChannel = this.channel.getRangeMax() - this.channel.getRangeMin();
        }
        return this.rangeChannel;
    }

    public double[][] getErrorsAbsolute() {
        if (this.errorsAbsolute == null) {
            double[]values = this.channel.getSensor().getValues(this.channel);
            if (maxCalibratorPower != 999999999D && values[1] < maxCalibratorPower){
                values[1] = maxCalibratorPower;
            }
            switch (this.method) {
                case MKMX_5300_01_18:
                case MKMX_5300_02_18:
                    this.errorsAbsolute = new double[in.length][6];
                    for (int n = 0; n < in.length; n++) {
                        this.errorsAbsolute[n][0] = in[n][1] - values[1];
                        this.errorsAbsolute[n][1] = in[n][2] - values[1];
                        this.errorsAbsolute[n][2] = in[n][3] - values[2];
                        this.errorsAbsolute[n][3] = in[n][4] - values[2];
                        this.errorsAbsolute[n][4] = in[n][5] - values[3];
                        this.errorsAbsolute[n][5] = in[n][6] - values[3];
                    }
                    return this.errorsAbsolute;
                default: return null;
            }
        }else {
            return this.errorsAbsolute;
        }
    }

    public double getMaxAbsoluteError(){
        if (this.errorMax == -999999999D){
            switch (this.method){
                case MKMX_5300_01_18:
                case MKMX_5300_02_18:
                    double[][]errorsAbsolute = this.getErrorsAbsolute();
                    double[] biggest = new double[errorsAbsolute.length];
                    for (int n=0;n<errorsAbsolute.length;n++) {
                        biggest[n] = NumberUtils.max(errorsAbsolute[n]);
                    }
                    this.errorMax = NumberUtils.max(biggest);
                    return this.errorMax;
                default: return 0D;
            }
        }else {
            return this.errorMax;
        }
    }

    public double getRangeSensor(){
        if (this.rangeSensor == -999999999D){
            Sensor sensor = this.channel.getSensor();
            this.rangeSensor = sensor.getRangeMax() - sensor.getRangeMin();
        }
        return this.rangeSensor;
    }

    public double[] getErrorSensor(){
        if (this.errorSensor == null){
            this.errorSensor = new double[2];
            Sensor sensor = this.channel.getSensor();
            double rangeChannel = this.getRangeChannel();
            double rangeSensor = this.getRangeSensor();
            switch (this.method){
                case MKMX_5300_01_18:
                    switch (sensor.getType()){
                        case TCM_50M:
                        case TCP_100:
                            this.errorSensor[0] = (0.005 * rangeChannel) + 0.3;
                            break;
                        case TXA_0395_typeK:
                        case TXA_2388_typeK:
                        case TP0198_2:
                            if (this.channel.getRangeMax() < 333.5){
                                this.errorSensor[0] = 2.5;
                            }else if (this.channel.getRangeMax() > 333.4 && this.channel.getRangeMax() <= 1250D){
                                this.errorSensor[0] = 0.0075 * rangeChannel;
                            }
                            break;
                    }

                    this.errorSensor[1] = (this.errorSensor[0] / rangeSensor) * 100;

                    return this.errorSensor;

                case MKMX_5300_02_18:
                    switch (sensor.getType()){
                        case DELTABAR_S:
                            this.errorSensor[1] = 0.075;
                            break;
                        case JUMO_dTRANS_p02:
                            this.errorSensor[1] = 0.1;
                            break;
                        case FISHER_ROSEMOUNT_3051S:
                            this.errorSensor[1] = 0.055;
                            break;
                        case YOKOGAWA:
                            this.errorSensor[1] = 0.2;
                    }

                    double dSensor_notConverted = this.getRangeSensor();
                    double dSensor = new Converter(MeasurementConstants.getConstantFromString(this.channel.getSensor().getValue()),
                            this.channel.getMeasurement().getValueConstant()).get(dSensor_notConverted);
                    this.errorSensor[0] = (dSensor / 100) * this.errorSensor[1];

                    return this.errorSensor;

                default: return null;
            }
        }else {
            return this.errorSensor;
        }
    }

    public double[] getErrorCalibrator(){
        if (this.errorCalibrator == null){
            this.errorCalibrator = new double[2];
            double rangeChannel = this.getRangeChannel();
            switch (this.method){
                case MKMX_5300_01_18:
                    switch (this.calibrator.getName()){
                        case FLUKE725:
                        case FLUKE724:
                            this.errorCalibrator[0] = 0.7;
                            break;
                        case PROVA123:
                            if (this.channel.getRangeMin() > -200D && this.channel.getRangeMax() <= 0D){
                                this.errorCalibrator[0] = 1.1;
                            }else if (this.channel.getRangeMin() >= 0D && this.channel.getRangeMax() <= 1370D){
                                this.errorCalibrator[0] = 0.8;
                            }
                            break;
                    }

                    this.errorCalibrator[1] = (this.errorCalibrator[0] / rangeChannel) * 100;

                    return this.errorCalibrator;

                case MKMX_5300_02_18:
                    switch (this.calibrator.getName()){
                        case FLUKE750PD2:
                        case FLUKE718_30G:
                        case FLUKE750PD2_small:
                            this.errorCalibrator[1] = 0.05;
                            break;
                    }

                    double dCalibrator = new Converter(MeasurementConstants.getConstantFromString(this.calibrator.getValue()),
                            this.channel.getMeasurement().getValueConstant()).get(this.calibrator.getRange());
                    this.errorCalibrator[0] = (dCalibrator / 100) * this.errorCalibrator[1];

                    return this.errorCalibrator;

                default: return null;
            }
        }else {
            return this.errorCalibrator;
        }
    }

    public double getAbsoluteErrorWithSensorError(){
        if (this.error == -999999999D){
            double errorMax = this.getMaxAbsoluteError();
            double errorSensor = this.getErrorSensor()[0];
            double errorCalibrator = this.getErrorCalibrator()[0];
            switch (this.method){
                case MKMX_5300_01_18:
                case MKMX_5300_02_18:
                    double e = (errorMax * errorMax) + (errorSensor * errorSensor) + (errorCalibrator * errorCalibrator);
                    this.error = Math.sqrt(e);

                    return this.error;

                default: return 0D;
            }
        }else {
            return this.error;
        }
    }

    public double getErrorInRange(){
        if (this.errorD == -999999999D){
            double error = this.getAbsoluteErrorWithSensorError();
            double rangeChannel = this.getRangeChannel();
            switch (this.method){
                case MKMX_5300_01_18:
                case MKMX_5300_02_18:
                    this.errorD = (error * 100D) / rangeChannel;

                    return this.errorD;

                default: return 0D;
            }
        }else {
            return this.errorD;
        }
    }

    public double[] getSystematicErrors(){
        if (this.errorsS == null){
            double[][]errorsAbsolute = this.getErrorsAbsolute();
            switch (this.method){
                case MKMX_5300_01_18:
                case MKMX_5300_02_18:
                    this.errorsS = new double[3];

                    double s5 = 0D;
                    for (double[] doubles : errorsAbsolute) {
                        s5 = s5 + (doubles[0] + doubles[1]);
                    }
                    double S5 = s5 / (errorsAbsolute.length * 2);
                    if (S5 < 0.0005 && S5 > -0.0005){
                        this.errorsS[0] = Math.abs(S5);
                    }else {
                        this.errorsS[0] = S5;
                    }
                    double s50 = 0D;
                    for (double[] doubles : errorsAbsolute) {
                        s50 = s50 + (doubles[2] + doubles[3]);
                    }
                    double S50 = s50 / (errorsAbsolute.length * 2);
                    if (S50 < 0.0005 && S50 > -0.0005){
                        this.errorsS[1] = Math.abs(S50);
                    }else {
                        this.errorsS[1] = S50;
                    }
                    double s95 = 0D;
                    for (double[] doubles : errorsAbsolute) {
                        s95 = s95 + (doubles[4] + doubles[5]);
                    }
                    double S95 = s95 / (errorsAbsolute.length * 2);
                    if (S95 < 0.0005 && S95 > -0.0005){
                        this.errorsS[2] = Math.abs(S95);
                    }else {
                        this.errorsS[2] = S95;
                    }
                    return this.errorsS;

                default: return null;
            }
        }else {
            return this.errorsS;
        }
    }

    public double getStandardIndeterminacyA(){
        if (this.uA == -999999999D){
            double[][]errorsAbsolute = this.getErrorsAbsolute();
            switch (this.method){
                case MKMX_5300_01_18:
                case MKMX_5300_02_18:
                    double sum = 0D;
                    for (double[] doubles : errorsAbsolute) {
                        sum = sum + (doubles[0] + doubles[1] + doubles[2] + doubles[3] + doubles[4] + doubles[5]);
                    }
                    double numerator = sum * sum;
                    int num = (6 * errorsAbsolute.length) * ((6 * errorsAbsolute.length) - 1);
                    double a = numerator/ (double) num;
                    this.uA = Math.sqrt(a);

                    return this.uA;

                default: return 0D;
            }
        }else {
            return this.uA;
        }
    }

    public double getStandardIndeterminacyB(){
        if (this.uB == -999999999D){
            double errorSensor = this.getErrorSensor()[0];
            double errorCalibrator = this.getErrorCalibrator()[0];
            switch (this.method){
                case MKMX_5300_01_18:
                case MKMX_5300_02_18:
                    double sum = (errorSensor * errorSensor) + (errorCalibrator * errorCalibrator);
                    this.uB = Math.sqrt(sum);

                    return this.uB;

                default: return 0D;
            }
        }else {
            return this.uB;
        }
    }


    public double getStandardIndeterminacyTotal(){
        if (this.uC == -999999999D){
            double uA = this.getStandardIndeterminacyA();
            double uB = this.getStandardIndeterminacyB();
            switch (this.method){
                case MKMX_5300_01_18:
                case MKMX_5300_02_18:
                    double sum = (uA * uA) + (uB * uB);
                    this.uC = Math.sqrt(sum);

                    return this.uC;

                default: return 0D;
            }
        }else {
            return this.uC;
        }
    }

    public double getExtendedIndeterminacy(){
        if (this.u == -999999999D){
            double uC = this.getStandardIndeterminacyTotal();
            double k = 2D;
            switch (this.method){
                case MKMX_5300_01_18:
                case MKMX_5300_02_18:
                    this.u = k * uC;
                    break;
                default: return 0D;
            }
        }
        return this.u;
    }
}
