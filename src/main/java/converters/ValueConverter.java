package converters;

import static model.Measurement.*;

public class ValueConverter {

    private final String inValue;
    private final String outValue;

    public ValueConverter(String inValue, String outValue){
        this.inValue = inValue;
        this.outValue = outValue;
    }

    public Double get(double from){

        if (this.outValue.equals(MM_ACVA)){
            if (this.inValue.equals(KPA)){
                return from * 101.9716;
            }else if (this.inValue.equals(PA)){
                return from * 0.1019716;
            }else if (this.inValue.equals(KGS_SM2)) {
                return from * 10000D;
            }else if (this.inValue.equals(KGS_MM2)){
                return from * 1000027D;
            }else if (this.inValue.equals(BAR)){
                return from * 10197.16;
            }else if (this.inValue.equals(ML_BAR)){
                return from * 10.19744288922;
            }else {
                return from;
            }
        }
        else if (this.outValue.equals(KPA)){
            if (this.inValue.equals(MM_ACVA)){
                return from * (9.807750 * 0.001);
            }else if (this.inValue.equals(PA)){
                return from * 0.001;
            }else if (this.inValue.equals(KGS_SM2)){
                return from * 98.0665;
            }else if (this.inValue.equals(KGS_MM2)){
                return from * 9806.65;
            }else if (this.inValue.equals(BAR)){
                return from * 100D;
            }else if (this.inValue.equals(ML_BAR)){
                return from * 0.1;
            }else {
                return from;
            }
        }
        else if (this.outValue.equals(PA)){
            if (this.inValue.equals(MM_ACVA)){
                return from * 9.80665;
            }else if (this.inValue.equals(KPA)){
                return from * 1000D;
            }else if (this.inValue.equals(KGS_SM2)){
                return from * 98066.5;
            }else if (this.inValue.equals(KGS_MM2)){
                return from * 9806650D;
            }else if (this.inValue.equals(BAR)){
                return from * 100000D;
            }else if (this.inValue.equals(ML_BAR)){
                return from * 100;
            }else {
                return from;
            }
        }
        else if (this.outValue.equals(KGS_SM2)){
            if (this.inValue.equals(MM_ACVA)){
                return from * 0.0001;
            }else if (this.inValue.equals(KPA)){
                return from * 0.01019716;
            }else if (this.inValue.equals(PA)){
                return from * (10.19716 * 0.000001);
            }else if (this.inValue.equals(KGS_MM2)){
                return from * 100;
            }else if (this.inValue.equals(BAR)){
                return from * 1.019716;
            }else if (this.inValue.equals(ML_BAR)){
                return from * 0.001019716212978;
            }else {
                return from;
            }
        }
        else if (this.outValue.equals(BAR)){
            if (this.inValue.equals(MM_ACVA)){
                return from * (98.0665 * 0.000001);
            }else if (this.inValue.equals(KPA)){
                return from * 0.01;
            }else if (this.inValue.equals(PA)){
                return from * 0.00001;
            }else if (this.inValue.equals(KGS_MM2)){
                return from * 98.0665;
            }else if (this.inValue.equals(KGS_SM2)){
                return from * 0.980665;
            }else if (this.inValue.equals(ML_BAR)){
                return from * 0.001;
            }else {
                return from;
            }
        }
        else if (this.outValue.equals(KGS_MM2)){
            if (this.inValue.equals(MM_ACVA)){
                return from * (9.999724776623 * 0.0000001);
            }else if (this.inValue.equals(KPA)){
                return from * 0.0001019716212978;
            }else if (this.inValue.equals(PA)){
                return from * (1.019716212978 * 0.0000001);
            }else if (this.inValue.equals(KGS_SM2)){
                return from * 0.01;
            }else if (this.inValue.equals(BAR)){
                return from * 0.01019716212978;
            }else if (this.inValue.equals(ML_BAR)){
                return from * 0.00001019716212978;
            }else {
                return from;
            }
        }
        else if (this.outValue.equals(ML_BAR)){
            if (this.inValue.equals(MM_ACVA)){
                return from * 0.0980638;
            }else if (this.inValue.equals(KPA)){
                return from * 10;
            }else if (this.inValue.equals(PA)){
                return from * 0.01;
            }else if (this.inValue.equals(KGS_SM2)){
                return from * 980.665;
            }else if (this.inValue.equals(KGS_MM2)){
                return from * 98066.5;
            }else if (this.inValue.equals(BAR)){
                return from * 1000;
            }else {
                return from;
            }
        }
        else return from;
    }
}
