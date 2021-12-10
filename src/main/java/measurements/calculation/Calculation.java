package measurements.calculation;

import constants.MeasurementConstants;
import constants.Strings;
import model.Calibrator;
import converters.ValueConverter;
import org.apache.commons.lang3.math.NumberUtils;
import model.Channel;
import support.Settings;

public class Calculation {
    private final Channel channel;
    private Calibrator calibrator;
    /*
     * double[Quantity of measurements] [control points]
     * control points: for temperature, pressure
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

    /*
     * double[Quantity of measurements] [control points]
     * control points: for temperature, pressure
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
     * control points: for temperature, pressure
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

    public Calculation(Channel channel){
        this.channel = channel;
    }

    public void setIn(double[][]in){
        this.in = in;
    }

    public void setCalibrator(Calibrator calibrator){
        if (calibrator.getType().equals(Strings.CALIBRATOR_FLUKE718_30G)){
            this.maxCalibratorPower = new ValueConverter(MeasurementConstants.KG_SM2, this.channel.getMeasurement().getValueConstant()).get(-0.8);
        }
        this.calibrator = calibrator;
        if (this.calibrator.getMeasurement().equals(MeasurementConstants.CONSUMPTION.getValue())){
            this.calibrator.setValue(this.channel.getMeasurement().getValue());
        }
    }

    public double[][]getIn(){
        return this.in;
    }

    public String getName(){
        return Settings.getSettingValue(this.channel.getMeasurement().getName());
    }

    public double[][] getErrorsAbsolute() {
        if (this.errorsAbsolute == null) {
            switch (this.channel.getMeasurement().getNameConstant()) {
                case TEMPERATURE:
                case PRESSURE:
                    double value5 = ((this.channel.getRange() / 100) * 5) + this.channel.getRangeMin();
                    double value50 = ((this.channel.getRange() / 100) * 50) + this.channel.getRangeMin();
                    double value95 = ((this.channel.getRange() / 100) * 95) + this.channel.getRangeMin();
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
                    return this.errorsAbsolute;
                case CONSUMPTION:
                    double value0 = this.channel.getRangeMin();
                    double value25 = ((this.channel.getRange() / 100) * 25) + this.channel.getRangeMin();
                    double value50c = ((this.channel.getRange() / 100) * 50) + this.channel.getRangeMin();
                    double value75 = ((this.channel.getRange() / 100) * 75) + this.channel.getRangeMin();
                    double value100 = this.channel.getRangeMax();
                    this.errorsAbsolute = new double[in.length][10];
                    for (int n = 0; n < in.length; n++) {
                        this.errorsAbsolute[n][0] = in[n][0] - value0;
                        this.errorsAbsolute[n][1] = in[n][1] - value0;
                        this.errorsAbsolute[n][2] = in[n][2] - value25;
                        this.errorsAbsolute[n][3] = in[n][3] - value25;
                        this.errorsAbsolute[n][4] = in[n][4] - value50c;
                        this.errorsAbsolute[n][5] = in[n][5] - value50c;
                        this.errorsAbsolute[n][6] = in[n][6] - value75;
                        this.errorsAbsolute[n][7] = in[n][7] - value75;
                        this.errorsAbsolute[n][8] = in[n][8] - value100;
                        this.errorsAbsolute[n][9] = in[n][9] - value100;
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
            switch (this.channel.getMeasurement().getNameConstant()){
                case TEMPERATURE:
                case PRESSURE:
                case CONSUMPTION:
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

    public double getAbsoluteErrorWithSensorError(){
        if (this.error == -999999999D){
            double errorMax = this.getMaxAbsoluteError();
            double errorSensor = this.channel.getSensor().getError(this.channel);
            double errorCalibrator = this.calibrator.getError(this.channel);
            switch (this.channel.getMeasurement().getNameConstant()){
                case TEMPERATURE:
                case PRESSURE:
                case CONSUMPTION:
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
            double rangeChannel = this.channel.getRange();
            switch (this.channel.getMeasurement().getNameConstant()){
                case TEMPERATURE:
                case PRESSURE:
                case CONSUMPTION:
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
            switch (this.channel.getMeasurement().getNameConstant()){
                case TEMPERATURE:
                case PRESSURE:
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

                case CONSUMPTION:
                    this.errorsS = new double[5];

                    double s0 = 0D;
                    for (double[] doubles : errorsAbsolute){
                        s0 = s0 + (doubles[0] + doubles[1]);
                    }
                    double S0 = s0 / (errorsAbsolute.length * 2);
                    if (S0 < 0.0005 && S0 > -0.0005){
                        this.errorsS[0] = Math.abs(S0);
                    }else {
                        this.errorsS[0] = S0;
                    }

                    double s25 = 0D;
                    for (double[] doubles : errorsAbsolute) {
                        s25 = s25 + (doubles[2] + doubles[3]);
                    }
                    double S25 = s25 / (errorsAbsolute.length * 2);
                    if (S25 < 0.0005 && S25 > -0.0005){
                        this.errorsS[1] = Math.abs(S25);
                    }else {
                        this.errorsS[1] = S25;
                    }

                    double s50c = 0D;
                    for (double[] doubles : errorsAbsolute) {
                        s50c = s50c + (doubles[4] + doubles[5]);
                    }
                    double S50c = s50c / (errorsAbsolute.length * 2);
                    if (S50c < 0.0005 && S50c > -0.0005){
                        this.errorsS[2] = Math.abs(S50c);
                    }else {
                        this.errorsS[2] = S50c;
                    }

                    double s75 = 0D;
                    for (double[] doubles : errorsAbsolute) {
                        s75 = s75 + (doubles[6] + doubles[7]);
                    }
                    double S75 = s75 / (errorsAbsolute.length * 2);
                    if (S75 < 0.0005 && S75 > -0.0005){
                        this.errorsS[3] = Math.abs(S75);
                    }else {
                        this.errorsS[3] = S75;
                    }

                    double s100 = 0D;
                    for (double[] doubles : errorsAbsolute) {
                        s100 = s100 + (doubles[8] + doubles[9]);
                    }
                    double S100 = s100 / (errorsAbsolute.length * 2);
                    if (S100 < 0.0005 && S100 > -0.0005){
                        this.errorsS[4] = Math.abs(S100);
                    }else {
                        this.errorsS[4] = S100;
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
            switch (this.channel.getMeasurement().getNameConstant()){
                case TEMPERATURE:
                case PRESSURE:
                    double sum = 0D;
                    for (double[] doubles : errorsAbsolute) {
                        sum = sum + (doubles[0] + doubles[1] + doubles[2] + doubles[3] + doubles[4]
                                + doubles[5] + doubles[6] + doubles[7] + doubles[8] + doubles[9]);
                    }
                    double numerator = sum * sum;
                    int num = (10 * errorsAbsolute.length) * ((10 * errorsAbsolute.length) - 1);
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
            double errorSensor = this.channel.getSensor().getError(this.channel);
            double errorCalibrator = this.calibrator.getError(this.channel);
            switch (this.channel.getMeasurement().getNameConstant()){
                case TEMPERATURE:
                case PRESSURE:
                case CONSUMPTION:
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
            switch (this.channel.getMeasurement().getNameConstant()){
                case TEMPERATURE:
                case PRESSURE:
                case CONSUMPTION:
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
            switch (this.channel.getMeasurement().getNameConstant()){
                case TEMPERATURE:
                case PRESSURE:
                case CONSUMPTION:
                    this.u = k * uC;
                    break;
                default: return 0D;
            }
        }
        return this.u;
    }
}
