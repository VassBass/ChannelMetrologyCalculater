package service.measurement_transformer.tc.transformer;

import service.measurement_transformer.tc.model.Type;

public class Transformer_GOST6651_2009 implements Transformer {
    @Override
    public double transformFromTemperature(Type type, double r0, double temperatureValue) {
        double a, b, c;
        double t = temperatureValue;

        switch (type) {
            case Cu:
                a = 0.00_428;
                b = -0.000_000_62032;
                c = 0.000_000_000_85154;

                return t < 0 ?
                        r0 * (1 + (a * t) + (b * t * (t + 6.7)) + (c * Math.pow(t, 3))) :
                        r0 * (1 + (a * t));
            case Pt:
                a = 0.00_39083;
                b = -0.000_000_5775;
                c = -0.000_000_000_00_4183;

                return t < 0 ?
                        r0 * (1 + (a * t) + (b * Math.pow(t, 2)) + (c * (t - 100) * Math.pow(t, 3))) :
                        r0 * (1 + (a * t) + (b * Math.pow(t, 2)));
            case Pl:
                a = 0.00_3969;
                b = -0.000_000_5841;
                c = -0.000_000_000_00_433;

                return t < 0 ?
                        r0 * (1 + (a * t) + (b * Math.pow(t, 2)) + (c * (t - 100) * Math.pow(t, 3))) :
                        r0 * (1 + (a * t) + (b * Math.pow(t, 2)));
            case Ni:
                a = 0.00_54963;
                b = 0.000_00_67556;
                c = 0.000_000_00_92004;

                return t < 100 ?
                        r0 * (1 + (a * t) + (b * Math.pow(t, 2))) :
                        r0 * (1 + (a * t) + (b * Math.pow(t, 2)) + (c * (t - 100) * Math.pow(t, 2)));

            default: return Double.NaN;
        }
    }

    @Override
    public double transformFromResistance(Type type, double r0, double resistanceValue) {
        double a, b, c;
        double r = resistanceValue;

        switch (type) {
            case Cu:
                a = 0.00_428;
                c = 239.4055;

                return r < 50 ?
                        c * ((r / r0) - 1) :
                        ((r / r0) - 1) / a;
            case Pt:
                a = 0.00_39083;
                b = -0.000_000_5775;
                c = 263.83177;

                return r < 100 ?
                        c * ((r / r0) - 1) :
                        (Math.sqrt(Math.pow(a, 2) - (4 * b * (1 - (r / r0)))) - a) / (2 * b);
            case Pl:
                a = 0.00_3969;
                b = -0.000_000_5841;
                c = 259.4644;

                return r < 100 ?
                        c * ((r / r0) - 1) :
                        (Math.sqrt(Math.pow(a, 2) - (4 * b * (1 - (r / r0)))) - a) / (2 * b);
            case Ni:
                a = 0.00_54963;
                b = 0.000_00_67556;
                c = 123.0816;

                return r < transformFromTemperature(Type.Ni, r0, 100) ?
                        (Math.sqrt(Math.pow(a, 2) - (4 * b * (1 - (r / r0)))) - a) / (2 * b) :
                        100 + (c * ((r / r0) - 1.6172));

            default:
                return Double.NaN;
        }
    }
}
