package def;

import java.util.ArrayList;

public class DefaultAreas {
    public static ArrayList<String> get() {
        ArrayList<String>areas = new ArrayList<>();

        String OPU1 = "ОВДЗ-1";
        String OPU2 = "ОВДЗ-2";
        String OPU3 = "ОВДЗ-3";
        String OPU4 = "ОВДЗ-4";
        String CPO1 = "ЦВО-1";
        String CPO2 = "ЦВО-2";

        areas.add(OPU1);
        areas.add(OPU2);
        areas.add(OPU3);
        areas.add(OPU4);
        areas.add(CPO1);
        areas.add(CPO2);

        return areas;
    }
}
