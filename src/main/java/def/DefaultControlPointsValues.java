package def;

import constants.SensorType;
import model.ControlPointsValues;

import java.util.ArrayList;

public class DefaultControlPointsValues {

    public static ArrayList<ControlPointsValues> get(){
        ArrayList<ControlPointsValues>controlPointsValues = new ArrayList<>();

        double[] values_tcm50m_0_100 = new double[]{ 0D, 5.14, 50D, 94.86, 100D };
        double[] values_tcm50m_m50_180 = new double[]{ -50.13, -39.59, 64.95, 168.22, 179.9 };
        ControlPointsValues TCM50M_0_100 = new ControlPointsValues(SensorType.TCM_50M,0D,100D, values_tcm50m_0_100);
        ControlPointsValues TCM50M_m50_180 = new ControlPointsValues(SensorType.TCM_50M,-50D, 180D, values_tcm50m_m50_180);

        controlPointsValues.add(TCM50M_0_100);
        controlPointsValues.add(TCM50M_m50_180);

        return controlPointsValues;
    }
}
