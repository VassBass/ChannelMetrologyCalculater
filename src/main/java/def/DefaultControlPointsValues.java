package def;

import constants.SensorType;
import model.ControlPointsValues;

import java.util.ArrayList;

public class DefaultControlPointsValues {

    public static ArrayList<ControlPointsValues> get(){
        ArrayList<ControlPointsValues>controlPointsValues = new ArrayList<>();

        double[] values_tcm50m_0_100 = new double[]{ 0D, 5.14, 50D, 94.86, 100D };
        double[] values_tcm50m_m50_180 = new double[]{ -50.13, -39.59, 64.95, 168.22, 179.9 };
        double[] values_tcm50m_0_125 = new double[]{ 0D, 6.08, 62.15, 118.69, 124.77 };
        double[] values_tcm50m_0_150 = new double[]{ 0D, 7.48, 74.77, 142.52, 150D };
        double[] values_tcm50m_m20_125 = new double[]{ -20D, -12.6, 52.34, 118.69, 124.77 };
        double[] values_tcm50m_m20_150 = new double[]{ -20D, -11.67, 64.95, 141.59, 150D };
        ControlPointsValues TCM50M_0_100 = new ControlPointsValues(SensorType.TCM_50M,0D,100D, values_tcm50m_0_100);
        ControlPointsValues TCM50M_m50_180 = new ControlPointsValues(SensorType.TCM_50M,-50D, 180D, values_tcm50m_m50_180);
        ControlPointsValues TCM50M_0_125 = new ControlPointsValues(SensorType.TCM_50M, 0D,125D, values_tcm50m_0_125);
        ControlPointsValues TCM50M_0_150 = new ControlPointsValues(SensorType.TCM_50M, 0D,150D, values_tcm50m_0_150);
        ControlPointsValues TCM50M_m20_125 = new ControlPointsValues(SensorType.TCM_50M, -20D, 125D, values_tcm50m_m20_125);
        ControlPointsValues TCM50M_m20_150 = new ControlPointsValues(SensorType.TCM_50M, -20D, 150D, values_tcm50m_m20_150);

        TCM50M_0_100.setId(1);
        TCM50M_m50_180.setId(2);
        TCM50M_0_125.setId(3);
        TCM50M_0_150.setId(4);
        TCM50M_m20_125.setId(5);
        TCM50M_m20_150.setId(6);

        double[] values_Pt100_0_100 = new double[]{ 0D, 5.12, 50D, 94.98, 100D };
        double[] values_Pt100_0_125 = new double[]{ 0D, 6.15, 62.5, 118.76, 125.13 };
        double[] values_Pt100_0_150 = new double[]{ 0D, 7.43, 75.03, 142.4, 149.93 };
        double[] values_Pt100_m20_125 = new double[]{ -20.15, -13.02, 52.61, 117.96, 125.13 };
        double[] values_Pt100_m20_150 = new double[]{ -20.15, -11.67, 64.84, 141.38, 149.93 };
        ControlPointsValues Pt100_0_100 = new ControlPointsValues(SensorType.Pt100, 0D, 100D, values_Pt100_0_100);
        ControlPointsValues Pt100_0_125 = new ControlPointsValues(SensorType.Pt100, 0D, 125D, values_Pt100_0_125);
        ControlPointsValues Pt100_0_150 = new ControlPointsValues(SensorType.Pt100, 0D, 150D, values_Pt100_0_150);
        ControlPointsValues Pt100_m20_125 = new ControlPointsValues(SensorType.Pt100, -20D, 125D, values_Pt100_m20_125);
        ControlPointsValues Pt100_m20_150 = new ControlPointsValues(SensorType.Pt100, -20D, 150D, values_Pt100_m20_150);

        Pt100_0_100.setId(7);
        Pt100_0_125.setId(8);
        Pt100_0_150.setId(9);
        Pt100_m20_125.setId(10);
        Pt100_m20_150.setId(11);

        controlPointsValues.add(TCM50M_0_100);
        controlPointsValues.add(TCM50M_m50_180);
        controlPointsValues.add(TCM50M_0_125);
        controlPointsValues.add(TCM50M_0_150);
        controlPointsValues.add(TCM50M_m20_125);
        controlPointsValues.add(TCM50M_m20_150);
        controlPointsValues.add(Pt100_0_100);
        controlPointsValues.add(Pt100_0_125);
        controlPointsValues.add(Pt100_0_150);
        controlPointsValues.add(Pt100_m20_125);
        controlPointsValues.add(Pt100_m20_150);

        return controlPointsValues;
    }
}
