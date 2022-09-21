package calculation;

import model.Calibrator;
import model.Channel;
import settings.Settings;

public abstract class Calculation {
    protected final Channel channel;

    protected Calibrator calibrator;
    protected double maxCalibratorPower = -999999999D;

    protected double[]controlPointsValues;

    /*
     * double[Quantity of measurements] [control points]
     */
    protected double[][] in;

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
    protected double[][] errorsAbsolute = null;

    /*
     * The biggest absolute error from double[][]errorsAbsolute
     */
    protected double errorMax = -999999999D;

    /*
     * Absolute error with sensor error
     */
    protected double error = -999999999D;

    /*
     * Error in range
     */
    protected double errorD = -999999999D;

    /*
     * Systematic errors
     * double[control points]
     * control points: for temperature, pressure
     * [0] = 5%
     * [1] = 50%
     * [2] = 95%
     */
    protected double[]errorsS = null;

    /*
     * Standard indeterminacy type "A"
     */
    protected double uA = -999999999D;

    /*
     * Standard indeterminacy type "B"
     */
    protected double uB = -999999999D;

    /*
     * Total standard indeterminacy
     */
    protected double uC = -999999999D;

    /*
     * Extended indeterminacy
     */
    protected double u = -999999999D;

    public Calculation(Channel channel){
        this.channel = channel;
    }

    public void setIn(double[][]in){
        this.in = in;
    }

    public void setCalibrator(Calibrator calibrator){
        this.calibrator = calibrator;
    }

    public Calibrator getCalibrator(){return this.calibrator;}

    public double[][]getIn(){
        return this.in;
    }

    public String getName(){
        return Settings.getSettingValue(this.channel.getMeasurement().getName());
    }

    public double[] getControlPointsValues(){
        return this.controlPointsValues;
    }

    public void setControlPointsValues(double[]values){
        this.controlPointsValues = values;
    }

    public abstract boolean closeToFalse();
    public abstract boolean goodChannel();
    public abstract double[][] getErrorsAbsolute();
    public abstract double getMaxAbsoluteError();
    public abstract double getAbsoluteErrorWithSensorError();
    public abstract double getErrorInRange();
    public abstract double getErrorInRangeWidthSensorError();
    public abstract double[] getSystematicErrors();
    public abstract double getStandardIndeterminacyA();
    public abstract double getStandardIndeterminacyB();
    public abstract double getStandardIndeterminacyTotal();
    public abstract double getExtendedIndeterminacy();
}