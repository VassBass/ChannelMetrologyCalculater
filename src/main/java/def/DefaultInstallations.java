package def;

import java.util.ArrayList;

public class DefaultInstallations {
    public static ArrayList<String> get() {
        ArrayList<String>installations = new ArrayList<>();

        String conveyor = "Конвеєр";
        String KKD = "ККД (Конусна крупного дроблення)";
        String KSD = "КСД (Конусна среднього дроблення)";
        String KMD = "КМД (Конусна мілкого дроблення)";
        String grohot = "Грохот";
        String bunker = "Бункер";
        String mill = "Млин";
        String klasifikator1 = "Класифікатор односпіральний";
        String klasifikator2 = "Класифікатор двухспіральний";
        String ZUMPF = "ЗУМПФ";
        String gidrociklon710 = "Гідроциклон 710мм";
        String gidrociklon350 = "Гідроциклон 350мм";
        String pump = "Насос";
        String MGS5 = "МГС-5";
        String MGS9 = "МГС-9";
        String MS = "Магнітний сепаратор";
        String FM = "Флотомашина";
        String ventilator = "Вентилятор";
        String pich = "Піч";
        String reshotka = "Решітка";
        String cooller = "Охолоджувач";

        installations.add(conveyor);
        installations.add(KKD);
        installations.add(KSD);
        installations.add(KMD);
        installations.add(grohot);
        installations.add(bunker);
        installations.add(mill);
        installations.add(klasifikator1);
        installations.add(klasifikator2);
        installations.add(ZUMPF);
        installations.add(gidrociklon710);
        installations.add(gidrociklon350);
        installations.add(pump);
        installations.add(MGS5);
        installations.add(MGS9);
        installations.add(MS);
        installations.add(FM);
        installations.add(ventilator);
        installations.add(pich);
        installations.add(reshotka);
        installations.add(cooller);

        return installations;
    }
}
