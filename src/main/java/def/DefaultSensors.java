package def;

import model.Measurement;
import model.Sensor;

import java.util.ArrayList;

public class DefaultSensors {
    public static ArrayList<Sensor> get() {
        ArrayList<Sensor>sensors = new ArrayList<>();

        Sensor tcm_50m = new Sensor();
        tcm_50m.setType(Sensor.TCM_50M);
        tcm_50m.setName(Sensor.TCM_50M);
        tcm_50m.setRange(-50D,180D);
        tcm_50m.setValue(Measurement.DEGREE_CELSIUS);
        tcm_50m.setMeasurement(Measurement.TEMPERATURE);
        tcm_50m.setErrorFormula("(0.005 * R) + 0.3");
        sensors.add(tcm_50m);

        Sensor tcp_100 = new Sensor();
        tcp_100.setType(Sensor.Pt100);
        tcp_100.setName(Sensor.Pt100);
        tcp_100.setRange(-50D,500D);
        tcp_100.setValue(Measurement.DEGREE_CELSIUS);
        tcp_100.setMeasurement(Measurement.TEMPERATURE);
        tcp_100.setErrorFormula("(0.005 * R) + 0.3");
        sensors.add(tcp_100);

        Sensor txa_2388_typeK = new Sensor();
        txa_2388_typeK.setType("Термопара TXA-2388 (тип К)");
        txa_2388_typeK.setName("Термопара TXA-2388 (тип К) < 333.5" + Measurement.DEGREE_CELSIUS);
        txa_2388_typeK.setRange(-50D,1250D);
        txa_2388_typeK.setValue(Measurement.DEGREE_CELSIUS);
        txa_2388_typeK.setMeasurement(Measurement.TEMPERATURE);
        txa_2388_typeK.setErrorFormula("2.5");
        sensors.add(txa_2388_typeK);

        Sensor txa_2388_typeK_big = new Sensor();
        txa_2388_typeK_big.setType("Термопара TXA-2388 (тип К)");
        txa_2388_typeK_big.setName("Термопара TXA-2388 (тип К) > 333.5" + Measurement.DEGREE_CELSIUS);
        txa_2388_typeK_big.setRange(-50D,1250D);
        txa_2388_typeK_big.setValue(Measurement.DEGREE_CELSIUS);
        txa_2388_typeK_big.setMeasurement(Measurement.TEMPERATURE);
        txa_2388_typeK_big.setErrorFormula("0.0075 * R");
        sensors.add(txa_2388_typeK_big);

        Sensor txa_0395_typeK = new Sensor();
        txa_0395_typeK.setType("Термопара TXA-0395 (тип К)");
        txa_0395_typeK.setName("Термопара TXA-0395 (тип К) < 333.5" + Measurement.DEGREE_CELSIUS);
        txa_0395_typeK.setRange(-50D,1250D);
        txa_0395_typeK.setValue(Measurement.DEGREE_CELSIUS);
        txa_0395_typeK.setMeasurement(Measurement.TEMPERATURE);
        txa_0395_typeK.setErrorFormula("2.5");
        sensors.add(txa_0395_typeK);

        Sensor txa_0395_typeK_big = new Sensor();
        txa_0395_typeK_big.setType("Термопара TXA-0395 (тип К)");
        txa_0395_typeK_big.setName("Термопара TXA-0395 (тип К) > 333.5" + Measurement.DEGREE_CELSIUS);
        txa_0395_typeK_big.setRange(-50D,1250D);
        txa_0395_typeK_big.setValue(Measurement.DEGREE_CELSIUS);
        txa_0395_typeK_big.setMeasurement(Measurement.TEMPERATURE);
        txa_0395_typeK_big.setErrorFormula("0.0075 * R");
        sensors.add(txa_0395_typeK_big);

        Sensor tp0198_2 = new Sensor();
        tp0198_2.setType("ТП 0198/2");
        tp0198_2.setName("ТП 0198/2 < 333.5" + Measurement.DEGREE_CELSIUS);
        tp0198_2.setRange(-40D,1100D);
        tp0198_2.setValue(Measurement.DEGREE_CELSIUS);
        tp0198_2.setMeasurement(Measurement.TEMPERATURE);
        tp0198_2.setErrorFormula("2.5");
        sensors.add(tp0198_2);

        Sensor tp0198_2_big = new Sensor();
        tp0198_2_big.setType("ТП 0198/2");
        tp0198_2_big.setName("ТП 0198/2 > 333.5" + Measurement.DEGREE_CELSIUS);
        tp0198_2_big.setRange(-40D,1100D);
        tp0198_2_big.setValue(Measurement.DEGREE_CELSIUS);
        tp0198_2_big.setMeasurement(Measurement.TEMPERATURE);
        tp0198_2_big.setErrorFormula("0.0075 * R");
        sensors.add(tp0198_2_big);

        Sensor deltabarS = new Sensor();
        deltabarS.setType("Deltabar S");
        deltabarS.setName("Deltabar S");
        deltabarS.setMeasurement(Measurement.PRESSURE);
        deltabarS.setErrorFormula("(convR / 100) * 0.075");
        sensors.add(deltabarS);

        Sensor fisherRosemount3051s = new Sensor();
        fisherRosemount3051s.setType("Fisher-Rosemount 3051S");
        fisherRosemount3051s.setName("Fisher-Rosemount 3051S");
        fisherRosemount3051s.setMeasurement(Measurement.PRESSURE);
        fisherRosemount3051s.setErrorFormula("(convR / 100) * 0.055");
        sensors.add(fisherRosemount3051s);

        Sensor yokogawa = new Sensor();
        yokogawa.setType("Yokogawa");
        yokogawa.setName("Yokogawa");
        yokogawa.setMeasurement(Measurement.PRESSURE);
        yokogawa.setErrorFormula("(convR / 100) * 0.2");
        sensors.add(yokogawa);

        Sensor jumoDTransP02 = new Sensor();
        jumoDTransP02.setType("JUMO dTRANS p02");
        jumoDTransP02.setName("JUMO dTRANS p02");
        jumoDTransP02.setMeasurement(Measurement.PRESSURE);
        jumoDTransP02.setErrorFormula("(convR / 100) * 0.1");
        sensors.add(jumoDTransP02);

        Sensor yokogawa_axf050g = new Sensor();
        yokogawa_axf050g.setType("YOKOGAWA AXF050G");
        yokogawa_axf050g.setName("YOKOGAWA AXF050G");
        yokogawa_axf050g.setMeasurement(Measurement.CONSUMPTION);
        yokogawa_axf050g.setErrorFormula("(R / 100) * 0.35");
        sensors.add(yokogawa_axf050g);

        Sensor rosemount_8750 = new Sensor();
        rosemount_8750.setType("ROSEMOUNT 8750");
        rosemount_8750.setName("ROSEMOUNT 8750");
        rosemount_8750.setMeasurement(Measurement.CONSUMPTION);
        rosemount_8750.setErrorFormula("(R / 100) * 0.5");
        sensors.add(rosemount_8750);

        return sensors;
    }
}
