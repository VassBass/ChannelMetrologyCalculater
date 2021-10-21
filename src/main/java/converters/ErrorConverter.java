package converters;

import org.mariuszgromada.math.mxparser.Argument;
import org.mariuszgromada.math.mxparser.Expression;
import org.mariuszgromada.math.mxparser.Function;

public class ErrorConverter {
    public static double getErrorFrom(double error, boolean outInPercent, double range){
        if (error == 0D || range == 0D){
            return 0D;
        }else {
            if (error < 0){
                error = -1 * error;
            }
            if (outInPercent) {
                return (range / 100) * error;
            } else {
                return (error / range) * 100;
            }
        }
    }

    public static double getSensorError(String formula, double channelRange, double sensorRange){
        Function f = new Function("At(R,r) = " + formula);
        Argument R = new Argument("R = " + channelRange);
        Argument r = new Argument("r = " + sensorRange);
        Expression expression = new Expression("At(R,r)", f,R,r);
        return expression.calculate();
    }
}
