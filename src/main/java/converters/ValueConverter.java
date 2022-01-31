package converters;

import constants.MeasurementConstants;

public class ValueConverter {

    private final MeasurementConstants inValue;
    private final MeasurementConstants outValue;

    public ValueConverter(MeasurementConstants inValue, MeasurementConstants outValue){
        this.inValue = inValue;
        this.outValue = outValue;
    }

    public Double get(double from){

        switch (this.outValue){

            case MM_ACVA:
                switch (this.inValue){
                    case KPA:
                        return from * 101.9716;
                    case PA:
                        return from * 0.1019716;
                    case KG_SM2:
                        return from * 10000D;
                    case BAR:
                        return from * 10197.16;
                    case KG_MM2:
                        return from * 1000027D;
                    case ML_BAR:
                        return from * 10.19744288922;
                    default:
                        return from;
                }

            case KPA:
                switch (this.inValue){
                    case MM_ACVA:
                        return from * (9.807750 * 0.001);
                    case PA:
                        return from * 0.001;
                    case KG_SM2:
                        return from * 98.0665;
                    case BAR:
                        return from * 100D;
                    case KG_MM2:
                        return from * 9806.65;
                    case ML_BAR:
                        return from * 0.1;
                    default:
                        return from;
                }

            case PA:
                switch (this.inValue){
                    case MM_ACVA:
                        return from * 9.80665;
                    case KPA:
                        return from * 1000D;
                    case KG_SM2:
                        return from * 98066.5;
                    case BAR:
                        return from * 100000D;
                    case KG_MM2:
                        return from * 9806650D;
                    case ML_BAR:
                        return from * 100;
                    default:
                        return from;
                }

            case KG_SM2:
                switch (this.inValue){
                    case MM_ACVA:
                        return from * 0.0001;
                    case KPA:
                        return from * 0.01019716;
                    case PA:
                        return from * (10.19716 * 0.000001);
                    case BAR:
                        return from * 1.019716;
                    case KG_MM2:
                        return from * 100;
                    case ML_BAR:
                        return from * 0.001019716212978;
                    default:
                        return from;
                }

            case BAR:
                switch (this.inValue){
                    case KG_SM2:
                        return from * 0.980665;
                    case MM_ACVA:
                        return from * (98.0665 * 0.000001);
                    case KPA:
                        return from * 0.01;
                    case PA:
                        return from * 0.00001;
                    case KG_MM2:
                        return from * 98.0665;
                    case ML_BAR:
                        return from * 0.001;
                    default:
                        return from;
                }

            case KG_MM2:
                switch (this.inValue){
                    case BAR:
                        return from * 0.01019716212978;
                    case KG_SM2:
                        return from * 0.01;
                    case MM_ACVA:
                        return from * (9.999724776623 * 0.0000001);
                    case KPA:
                        return from * 0.0001019716212978;
                    case PA:
                        return from * (1.019716212978 * 0.0000001);
                    case ML_BAR:
                        return from * 0.00001019716212978;
                    default:
                        return from;
                }

            case ML_BAR:
                switch (this.inValue){
                    case BAR:
                        return from * 1000;
                    case KG_MM2:
                        return from * 98066.5;
                    case KG_SM2:
                        return from * 980.665;
                    case MM_ACVA:
                        return from * 0.0980638;
                    case KPA:
                        return from * 10;
                    case PA:
                        return from * 0.01;
                    default:
                        return from;
                }

            default:
                return from;
        }
    }
}
