package def;

import java.util.ArrayList;

public class DefaultDepartments {
    public static ArrayList<String> get() {
        ArrayList<String>departments = new ArrayList<>();

        String CPO = "ЦВО";
        String DOF = "ДЗФ";

        departments.add(CPO);
        departments.add(DOF);

        return departments;
    }
}
