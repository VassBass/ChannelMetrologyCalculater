package converters;

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
}
