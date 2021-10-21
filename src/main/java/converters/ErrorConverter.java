package converters;

public class ErrorConverter {
    public static double getErrorFrom(double error, boolean inPercent, double range){
        if (error == 0D || range == 0D){
            return 0D;
        }else {
            if (error < 0){
                error = -1 * error;
            }
            if (inPercent) {
                return (range / 100) * error;
            } else {
                return (error / range) * 100;
            }
        }
    }
}
