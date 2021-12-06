package support;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import constants.MeasurementConstants;
import constants.WorkPositions;
import measurements.Consumption;
import measurements.Measurement;
import measurements.Pressure;
import measurements.Temperature;

public class Default {
    
    public static void loadForms(){
        UnZipper unzipper = new UnZipper();
        unzipper.unzip();
    }

    public static void loadSettings(){

        String nameTemperatureMethod = "МКМХ №5300.01:18";
        String namePressureMethod = "МКМХ №5300.02:18";
        String nameConsumptionMethod = "МКМХ №5300.07:20";

        Settings.setSettingValue(MeasurementConstants.TEMPERATURE.getValue(), nameTemperatureMethod);
        Settings.setSettingValue(MeasurementConstants.PRESSURE.getValue(), namePressureMethod);
        Settings.setSettingValue(MeasurementConstants.CONSUMPTION.getValue(), nameConsumptionMethod);
    }

    public static void loadSensors(){
        ArrayList<Sensor> sensors = new ArrayList<>();

        Sensor tcm_50m = new Sensor();
        tcm_50m.setType("ТСМ-50М");
        tcm_50m.setName("ТСМ-50М");
        tcm_50m.setRange(-50D,180D);
        tcm_50m.setValue(MeasurementConstants.DEGREE_CELSIUS.getValue());
        tcm_50m.setMeasurement(MeasurementConstants.TEMPERATURE.getValue());
        tcm_50m.setErrorFormula("(0.005 * R) + 0.3");
        sensors.add(tcm_50m);

        Sensor tcp_100 = new Sensor();
        tcp_100.setType("ТОП  Pt 100");
        tcp_100.setName("ТОП  Pt 100");
        tcp_100.setRange(-50D,500D);
        tcp_100.setValue(MeasurementConstants.DEGREE_CELSIUS.getValue());
        tcp_100.setMeasurement(MeasurementConstants.TEMPERATURE.getValue());
        tcp_100.setErrorFormula("(0.005 * R) + 0.3");
        sensors.add(tcp_100);

        Sensor txa_2388_typeK = new Sensor();
        txa_2388_typeK.setType("Термопара TXA-2388 (тип К)");
        txa_2388_typeK.setName("Термопара TXA-2388 (тип К) < 333.5" + MeasurementConstants.DEGREE_CELSIUS.getValue());
        txa_2388_typeK.setRange(-50D,1250D);
        txa_2388_typeK.setValue(MeasurementConstants.DEGREE_CELSIUS.getValue());
        txa_2388_typeK.setMeasurement(MeasurementConstants.TEMPERATURE.getValue());
        txa_2388_typeK.setErrorFormula("2.5");
        sensors.add(txa_2388_typeK);

        Sensor txa_2388_typeK_big = new Sensor();
        txa_2388_typeK_big.setType("Термопара TXA-2388 (тип К)");
        txa_2388_typeK_big.setName("Термопара TXA-2388 (тип К) > 333.5" + MeasurementConstants.DEGREE_CELSIUS.getValue());
        txa_2388_typeK_big.setRange(-50D,1250D);
        txa_2388_typeK_big.setValue(MeasurementConstants.DEGREE_CELSIUS.getValue());
        txa_2388_typeK_big.setMeasurement(MeasurementConstants.TEMPERATURE.getValue());
        txa_2388_typeK_big.setErrorFormula("0.0075 * R");
        sensors.add(txa_2388_typeK_big);

        Sensor txa_0395_typeK = new Sensor();
        txa_0395_typeK.setType("Термопара TXA-0395 (тип К)");
        txa_0395_typeK.setName("Термопара TXA-0395 (тип К) < 333.5" + MeasurementConstants.DEGREE_CELSIUS.getValue());
        txa_0395_typeK.setRange(-50D,1250D);
        txa_0395_typeK.setValue(MeasurementConstants.DEGREE_CELSIUS.getValue());
        txa_0395_typeK.setMeasurement(MeasurementConstants.TEMPERATURE.getValue());
        txa_0395_typeK.setErrorFormula("2.5");
        sensors.add(txa_0395_typeK);

        Sensor txa_0395_typeK_big = new Sensor();
        txa_0395_typeK_big.setType("Термопара TXA-0395 (тип К)");
        txa_0395_typeK_big.setName("Термопара TXA-0395 (тип К) > 333.5" + MeasurementConstants.DEGREE_CELSIUS.getValue());
        txa_0395_typeK_big.setRange(-50D,1250D);
        txa_0395_typeK_big.setValue(MeasurementConstants.DEGREE_CELSIUS.getValue());
        txa_0395_typeK_big.setMeasurement(MeasurementConstants.TEMPERATURE.getValue());
        txa_0395_typeK_big.setErrorFormula("0.0075 * R");
        sensors.add(txa_0395_typeK_big);

        Sensor tp0198_2 = new Sensor();
        tp0198_2.setType("ТП 0198/2");
        tp0198_2.setName("ТП 0198/2 < 333.5" + MeasurementConstants.DEGREE_CELSIUS.getValue());
        tp0198_2.setRange(-40D,1100D);
        tp0198_2.setValue(MeasurementConstants.DEGREE_CELSIUS.getValue());
        tp0198_2.setMeasurement(MeasurementConstants.TEMPERATURE.getValue());
        tp0198_2.setErrorFormula("2.5");
        sensors.add(tp0198_2);

        Sensor tp0198_2_big = new Sensor();
        tp0198_2_big.setType("ТП 0198/2");
        tp0198_2_big.setName("ТП 0198/2 > 333.5" + MeasurementConstants.DEGREE_CELSIUS.getValue());
        tp0198_2_big.setRange(-40D,1100D);
        tp0198_2_big.setValue(MeasurementConstants.DEGREE_CELSIUS.getValue());
        tp0198_2_big.setMeasurement(MeasurementConstants.TEMPERATURE.getValue());
        tp0198_2_big.setErrorFormula("0.0075 * R");
        sensors.add(tp0198_2_big);

        Sensor deltabarS = new Sensor();
        deltabarS.setType("Deltabar S");
        deltabarS.setName("Deltabar S");
        deltabarS.setMeasurement(MeasurementConstants.PRESSURE.getValue());
        deltabarS.setErrorFormula("(convR / 100) * 0.075");
        sensors.add(deltabarS);

        Sensor fisherRosemount3051s = new Sensor();
        fisherRosemount3051s.setType("Fisher-Rosemount 3051S");
        fisherRosemount3051s.setName("Fisher-Rosemount 3051S");
        fisherRosemount3051s.setMeasurement(MeasurementConstants.PRESSURE.getValue());
        fisherRosemount3051s.setErrorFormula("(convR / 100) * 0.055");
        sensors.add(fisherRosemount3051s);

        Sensor yokogawa = new Sensor();
        yokogawa.setType("Yokogawa");
        yokogawa.setName("Yokogawa");
        yokogawa.setMeasurement(MeasurementConstants.PRESSURE.getValue());
        yokogawa.setErrorFormula("(convR / 100) * 0.2");
        sensors.add(yokogawa);

        Sensor jumoDTransP02 = new Sensor();
        jumoDTransP02.setType("JUMO dTRANS p02");
        jumoDTransP02.setName("JUMO dTRANS p02");
        jumoDTransP02.setMeasurement(MeasurementConstants.PRESSURE.getValue());
        jumoDTransP02.setErrorFormula("(convR / 100) * 0.1");
        sensors.add(jumoDTransP02);

        Lists.saveSensorsListToFile(sensors);
    }

    public static void loadChannels(){
        ArrayList<Channel> channels = new ArrayList<>();
        Lists.saveChannelsListToFile(channels);
    }

    public static void loadDepartments(){
        ArrayList<String>departments = new ArrayList<>();

        String CPO = "ЦВО";
        String DOF = "ДЗФ";

        departments.add(CPO);
        departments.add(DOF);

        Lists.saveDepartmentsListToFile(departments);
    }

    public static void loadAreas(){
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

        Lists.saveAreasListToFile(areas);
    }

    public static void loadProcesses(){
        ArrayList<String>processes = new ArrayList<>();

        String barmak = "Бармак";
        String section = "Секція";
        String tract = "Тракт";
        String line = "Технологічна лінія";

        processes.add(barmak);
        processes.add(section);
        processes.add(tract);
        processes.add(line);

        Lists.saveProcessesListToFile(processes);
    }

    public static void loadInstallations(){
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

        Lists.saveInstallationsListToFile(installations);
    }

    public static void loadCalibrators(){
        ArrayList<Calibrator>calibrators = new ArrayList<>();

        Calibrator fluke725 = new Calibrator();
        fluke725.setType("Fluke 725");
        fluke725.setName("Fluke 725");
        fluke725.setNumber("1988293");
        fluke725.setMeasurement(MeasurementConstants.TEMPERATURE.getValue());
        fluke725.setValue(MeasurementConstants.DEGREE_CELSIUS.getValue());
        fluke725.setCertificateName("№06/2647К");
        fluke725.setCertificateDate(new GregorianCalendar(2020, Calendar.NOVEMBER, 16));
        fluke725.setCertificateCompany("ДП\"ХарківСтандартМетрологія\"");
        fluke725.setErrorFormula("0.7");
        calibrators.add(fluke725);

        Calibrator fluke724 = new Calibrator();
        fluke724.setType("Fluke 724");
        fluke724.setName("Fluke 724");
        fluke724.setNumber("1988293");
        fluke724.setMeasurement(MeasurementConstants.TEMPERATURE.getValue());
        fluke724.setValue(MeasurementConstants.DEGREE_CELSIUS.getValue());
        fluke724.setCertificateName("№06/1777К");
        fluke724.setCertificateDate(new GregorianCalendar(2019, Calendar.OCTOBER, 22));
        fluke724.setCertificateCompany("ДП\"ХарківСтандартМетрологія\"");
        fluke724.setErrorFormula("0.7");
        calibrators.add(fluke724);

        Calibrator prova123_minus = new Calibrator();
        prova123_minus.setType("Prova-123");
        prova123_minus.setName("Prova-123 t < 0" + MeasurementConstants.DEGREE_CELSIUS.getValue());
        prova123_minus.setNumber("13180302");
        prova123_minus.setMeasurement(MeasurementConstants.TEMPERATURE.getValue());
        prova123_minus.setValue(MeasurementConstants.DEGREE_CELSIUS.getValue());
        prova123_minus.setCertificateName("№06/2315К");
        prova123_minus.setCertificateDate(new GregorianCalendar(2020, Calendar.JULY, 21));
        prova123_minus.setCertificateCompany("ДП\"ХарківСтандартМетрологія\"");
        prova123_minus.setErrorFormula("1.1");
        calibrators.add(prova123_minus);

        Calibrator prova123 = new Calibrator();
        prova123.setType("Prova-123");
        prova123.setName("Prova-123 t > 0" + MeasurementConstants.DEGREE_CELSIUS.getValue());
        prova123.setNumber("13180302");
        prova123.setMeasurement(MeasurementConstants.TEMPERATURE.getValue());
        prova123.setValue(MeasurementConstants.DEGREE_CELSIUS.getValue());
        prova123.setCertificateName("№06/2315К");
        prova123.setCertificateDate(new GregorianCalendar(2020, Calendar.JULY, 21));
        prova123.setCertificateCompany("ДП\"ХарківСтандартМетрологія\"");
        prova123.setErrorFormula("0.8");
        calibrators.add(prova123);

        Calibrator fluke718_30g = new Calibrator();
        fluke718_30g.setType("Fluke 718 30G");
        fluke718_30g.setName("Fluke 718 30G");
        fluke718_30g.setNumber("2427047");
        fluke718_30g.setMeasurement(MeasurementConstants.PRESSURE.getValue());
        fluke718_30g.setRangeMin(-83D);
        fluke718_30g.setRangeMax(207D);
        fluke718_30g.setValue(MeasurementConstants.KPA.getValue());
        fluke718_30g.setCertificateName("№05/3570К");
        fluke718_30g.setCertificateDate(new GregorianCalendar(2020, Calendar.SEPTEMBER, 2));
        fluke718_30g.setCertificateCompany("ДП\"ХарківСтандартМетрологія\"");
        fluke718_30g.setErrorFormula("(convR / 100) * 0.05");
        calibrators.add(fluke718_30g);

        Calibrator fluke750pd2 = new Calibrator();
        fluke750pd2.setType("Fluke 750PD2");
        fluke750pd2.setName("Fluke 750PD2");
        fluke750pd2.setNumber("4043273");
        fluke750pd2.setMeasurement(MeasurementConstants.PRESSURE.getValue());
        fluke750pd2.setRangeMin(-7D);
        fluke750pd2.setRangeMax(7D);
        fluke750pd2.setValue(MeasurementConstants.KPA.getValue());
        fluke750pd2.setCertificateName("№05/3570К");
        fluke750pd2.setCertificateDate(new GregorianCalendar(2020, Calendar.SEPTEMBER, 2));
        fluke750pd2.setCertificateCompany("ДП\"ХарківСтандартМетрологія\"");
        fluke750pd2.setErrorFormula("(convR / 100) * 0.05");
        calibrators.add(fluke750pd2);

        Calibrator fluke750pd2_smallPressure = new Calibrator();
        fluke750pd2_smallPressure.setType("Fluke 750PD2");
        fluke750pd2_smallPressure.setName("Fluke 750PD2 (для Р<60 мм вод ст)");
        fluke750pd2_smallPressure.setNumber("4043273");
        fluke750pd2_smallPressure.setMeasurement(MeasurementConstants.PRESSURE.getValue());
        fluke750pd2_smallPressure.setRangeMin(-4.5);
        fluke750pd2_smallPressure.setRangeMax(4.5);
        fluke750pd2_smallPressure.setValue(MeasurementConstants.KPA.getValue());
        fluke750pd2_smallPressure.setCertificateName("№05/3570К");
        fluke750pd2_smallPressure.setCertificateDate(new GregorianCalendar(2020, Calendar.SEPTEMBER, 2));
        fluke750pd2_smallPressure.setCertificateCompany("ДП\"ХарківСтандартМетрологія\"");
        fluke750pd2_smallPressure.setErrorFormula("(convR / 100) * 0.05");
        calibrators.add(fluke750pd2_smallPressure);

        Lists.saveCalibratorsListToFile(calibrators);
    }

    public static void loadMeasurements(){
        ArrayList<Measurement>measurements = new ArrayList<>();

        measurements.add(new Temperature(MeasurementConstants.DEGREE_CELSIUS));

        measurements.add(new Pressure(MeasurementConstants.KPA));
        measurements.add(new Pressure(MeasurementConstants.PA));
        measurements.add(new Pressure(MeasurementConstants.MM_ACVA));
        measurements.add(new Pressure(MeasurementConstants.KG_SM2));
        measurements.add(new Pressure(MeasurementConstants.KG_MM2));
        measurements.add(new Pressure(MeasurementConstants.BAR));
        measurements.add(new Pressure(MeasurementConstants.ML_BAR));

        measurements.add(new Consumption(MeasurementConstants.M3_HOUR));

        Lists.saveMeasurementsListToFile(measurements);
    }

    public static void loadPersons(){
        ArrayList<Worker>workers = new ArrayList<>();

        Worker chekunovTM = new Worker();
        chekunovTM.setName("Тимофій");
        chekunovTM.setSurname("Чекунов");
        chekunovTM.setPatronymic("Миколайович");
        chekunovTM.setPosition(WorkPositions.HEAD_OF_DEPARTMENT_ASUTP);
        workers.add(chekunovTM);

        Worker fesenkoEV = new Worker();
        fesenkoEV.setName("Євген");
        fesenkoEV.setSurname("Фесенко");
        fesenkoEV.setPatronymic("Вітальйович");
        fesenkoEV.setPosition(WorkPositions.HEAD_OF_AREA + " МЗтаП");
        workers.add(fesenkoEV);

        Worker lenTV = new Worker();
        lenTV.setName("Тетяна");
        lenTV.setSurname("Лень");
        lenTV.setPatronymic("Володимирівна");
        lenTV.setPosition(WorkPositions.JUNIOR_ENGINEER);
        workers.add(lenTV);

        Worker pohiliiOO = new Worker();
        pohiliiOO.setName("Олександр");
        pohiliiOO.setSurname("Похилий");
        pohiliiOO.setPatronymic("Олександрович");
        pohiliiOO.setPosition(WorkPositions.HEAD_OF_AREA + " АСУТП");
        workers.add(pohiliiOO);

        Worker sergienkoOV = new Worker();
        sergienkoOV.setName("Олександр");
        sergienkoOV.setSurname("Сергієнко");
        sergienkoOV.setPatronymic("Вікторович");
        sergienkoOV.setPosition(WorkPositions.HEAD_OF_AREA + " АСУТП");
        workers.add(sergienkoOV);

        Worker vasilevIS = new Worker();
        vasilevIS.setName("Ігор");
        vasilevIS.setSurname("Васильєв");
        vasilevIS.setPatronymic("Сергійович");
        vasilevIS.setPosition(WorkPositions.ELECTRONIC_ENGINEER);
        workers.add(vasilevIS);

        Lists.savePersonsListToFile(workers);
    }

}