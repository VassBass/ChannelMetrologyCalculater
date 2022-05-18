package def;

import model.Measurement;

import java.util.ArrayList;
import java.util.HashMap;

public class DefaultMeasurements {
    public static ArrayList<Measurement> get(){
        ArrayList<Measurement>measurements = new ArrayList<>();

        measurements.add(new Measurement(Measurement.TEMPERATURE, Measurement.DEGREE_CELSIUS));

        HashMap<String, Double>kPaFactors = new HashMap<>();
        kPaFactors.put(Measurement.PA, 1000D);
        kPaFactors.put(Measurement.MM_ACVA, 101.9716);
        kPaFactors.put(Measurement.KGS_SM2, 0.01019716);
        kPaFactors.put(Measurement.KGS_MM2, 0.0001019716212978);
        kPaFactors.put(Measurement.BAR, 0.01);
        kPaFactors.put(Measurement.ML_BAR, 10D);
        Measurement kPa = new Measurement(Measurement.PRESSURE, Measurement.KPA);
        kPa.setFactors(kPaFactors);
        measurements.add(kPa);

        HashMap<String, Double>paFactors = new HashMap<>();
        paFactors.put(Measurement.KPA, 0.001);
        paFactors.put(Measurement.MM_ACVA, 0.1019716);
        paFactors.put(Measurement.KGS_SM2, 0.000001);
        paFactors.put(Measurement.KGS_MM2, 0.000000102);
        paFactors.put(Measurement.BAR, 0.00001);
        paFactors.put(Measurement.ML_BAR, 0.01);
        Measurement pa = new Measurement(Measurement.PRESSURE, Measurement.PA);
        pa.setFactors(paFactors);
        measurements.add(pa);

        HashMap<String, Double>mmAcvaFactors = new HashMap<>();
        mmAcvaFactors.put(Measurement.KPA, 0.00980775);
        mmAcvaFactors.put(Measurement.PA, 9.80665);
        mmAcvaFactors.put(Measurement.KGS_SM2, 0.0001);
        mmAcvaFactors.put(Measurement.KGS_MM2, 0.000001);
        mmAcvaFactors.put(Measurement.BAR, 0.000098067);
        mmAcvaFactors.put(Measurement.ML_BAR, 0.0980638);
        Measurement mmAcva = new Measurement(Measurement.PRESSURE, Measurement.MM_ACVA);
        mmAcva.setFactors(mmAcvaFactors);
        measurements.add(mmAcva);

        HashMap<String, Double>kgsSm2Factors = new HashMap<>();
        kgsSm2Factors.put(Measurement.KPA, 98.0665);
        kgsSm2Factors.put(Measurement.PA, 98066.5);
        kgsSm2Factors.put(Measurement.MM_ACVA, 10000D);
        kgsSm2Factors.put(Measurement.KGS_MM2, 0.01);
        kgsSm2Factors.put(Measurement.BAR, 0.980665);
        kgsSm2Factors.put(Measurement.ML_BAR, 980.665);
        Measurement kgsSm2 = new Measurement(Measurement.PRESSURE, Measurement.KGS_SM2);
        kgsSm2.setFactors(kgsSm2Factors);
        measurements.add(kgsSm2);

        HashMap<String, Double>kgsMm2Factors = new HashMap<>();
        kgsMm2Factors.put(Measurement.KPA, 9806.65);
        kgsMm2Factors.put(Measurement.PA, 9806650D);
        kgsMm2Factors.put(Measurement.MM_ACVA, 1000027D);
        kgsMm2Factors.put(Measurement.KGS_SM2, 100D);
        kgsMm2Factors.put(Measurement.BAR, 98.0665);
        kgsMm2Factors.put(Measurement.ML_BAR, 98066.5);
        Measurement kgsMm2 = new Measurement(Measurement.PRESSURE, Measurement.KGS_MM2);
        kgsMm2.setFactors(kgsMm2Factors);
        measurements.add(kgsMm2);

        HashMap<String, Double>barFactors = new HashMap<>();
        barFactors.put(Measurement.KPA, 100D);
        barFactors.put(Measurement.PA, 100000D);
        barFactors.put(Measurement.MM_ACVA, 10197.16);
        barFactors.put(Measurement.KGS_SM2, 1.019716);
        barFactors.put(Measurement.KGS_MM2, 0.01019716212978);
        barFactors.put(Measurement.ML_BAR, 1000D);
        Measurement bar = new Measurement(Measurement.PRESSURE, Measurement.BAR);
        bar.setFactors(barFactors);
        measurements.add(bar);

        HashMap<String, Double>mlBarFactors = new HashMap<>();
        mlBarFactors.put(Measurement.KPA, 0.1);
        mlBarFactors.put(Measurement.PA, 100D);
        mlBarFactors.put(Measurement.MM_ACVA, 10.19744288922);
        mlBarFactors.put(Measurement.KGS_SM2, 0.001019716212978);
        mlBarFactors.put(Measurement.KGS_MM2, 0.00001019716212978);
        mlBarFactors.put(Measurement.BAR, 0.001);
        Measurement mlBar = new Measurement(Measurement.PRESSURE, Measurement.ML_BAR);
        mlBar.setFactors(mlBarFactors);
        measurements.add(mlBar);

        measurements.add(new Measurement(Measurement.CONSUMPTION, Measurement.M3_HOUR));

        return measurements;
    }
}
