package support;

import calibrators.*;

import java.util.ArrayList;

import constants.MeasurementConstants;
import constants.SensorType;
import constants.WorkPositions;
import measurements.Measurement;
import measurements.Pressure;
import measurements.Temperature;

public class Default {
    
    public static void loadForms(){
        UnZipper unzipper = new UnZipper();
        unzipper.unzip();
    }

    public static void loadSensors(){
        ArrayList<Sensor> sensors = new ArrayList<>();

        Sensor tcm_50m = new Sensor();
        tcm_50m.setType(SensorType.TCM_50M);
        tcm_50m.setName(SensorType.TCM_50M.getType());
        tcm_50m.setRange(-50D,180D);
        tcm_50m.setValue(MeasurementConstants.OM.getValue());
        tcm_50m.setMeasurement(MeasurementConstants.TEMPERATURE.getValue());
        tcm_50m.setErrorFormula("(0.005 * R) + 0.3");
        sensors.add(tcm_50m);

        Sensor tcp_100 = new Sensor();
        tcp_100.setType(SensorType.TCP_100);
        tcp_100.setName(SensorType.TCP_100.getType());
        tcp_100.setRange(-50D,500D);
        tcp_100.setValue(MeasurementConstants.OM.getValue());
        tcp_100.setMeasurement(MeasurementConstants.TEMPERATURE.getValue());
        tcp_100.setErrorFormula("(0.005 * R) + 0.3");
        sensors.add(tcp_100);

        Sensor txa_2388_typeK = new Sensor();
        txa_2388_typeK.setType(SensorType.TXA_2388_typeK);
        txa_2388_typeK.setName(SensorType.TXA_2388_typeK.getType() + " < 333.5" + MeasurementConstants.DEGREE_CELSIUS.getValue());
        txa_2388_typeK.setRange(-50D,1250D);
        txa_2388_typeK.setValue(MeasurementConstants.MV.getValue());
        txa_2388_typeK.setMeasurement(MeasurementConstants.TEMPERATURE.getValue());
        txa_2388_typeK.setErrorFormula("2.5");
        sensors.add(txa_2388_typeK);

        Sensor txa_2388_typeK_big = new Sensor();
        txa_2388_typeK_big.setType(SensorType.TXA_2388_typeK);
        txa_2388_typeK_big.setName(SensorType.TXA_2388_typeK.getType() + " > 333.5" + MeasurementConstants.DEGREE_CELSIUS.getValue());
        txa_2388_typeK_big.setRange(-50D,1250D);
        txa_2388_typeK_big.setValue(MeasurementConstants.MV.getValue());
        txa_2388_typeK_big.setMeasurement(MeasurementConstants.TEMPERATURE.getValue());
        txa_2388_typeK_big.setErrorFormula("0.0075 * R");
        sensors.add(txa_2388_typeK_big);

        Sensor txa_0395_typeK = new Sensor();
        txa_0395_typeK.setType(SensorType.TXA_2388_typeK);
        txa_0395_typeK.setName(SensorType.TXA_2388_typeK.getType() + " < 333.5" + MeasurementConstants.DEGREE_CELSIUS.getValue());
        txa_0395_typeK.setRange(-50D,1250D);
        txa_0395_typeK.setValue(MeasurementConstants.MV.getValue());
        txa_0395_typeK.setMeasurement(MeasurementConstants.TEMPERATURE.getValue());
        txa_0395_typeK.setErrorFormula("2.5");
        sensors.add(txa_0395_typeK);

        Sensor txa_0395_typeK_big = new Sensor();
        txa_0395_typeK_big.setType(SensorType.TXA_0395_typeK);
        txa_0395_typeK_big.setName(SensorType.TXA_0395_typeK.getType() + " > 333.5" + MeasurementConstants.DEGREE_CELSIUS.getValue());
        txa_0395_typeK_big.setRange(-50D,1250D);
        txa_0395_typeK_big.setValue(MeasurementConstants.MV.getValue());
        txa_0395_typeK_big.setMeasurement(MeasurementConstants.TEMPERATURE.getValue());
        txa_0395_typeK_big.setErrorFormula("0.0075 * R");
        sensors.add(txa_0395_typeK_big);

        Sensor tp0198_2 = new Sensor();
        tp0198_2.setType(SensorType.TP0198_2);
        tp0198_2.setName(SensorType.TP0198_2.getType() + " < 333.5" + MeasurementConstants.DEGREE_CELSIUS.getValue());
        tp0198_2.setRange(-40D,1100D);
        tp0198_2.setValue(MeasurementConstants.MV.getValue());
        tp0198_2.setMeasurement(MeasurementConstants.TEMPERATURE.getValue());
        tp0198_2.setErrorFormula("2.5");
        sensors.add(tp0198_2);

        Sensor tp0198_2_big = new Sensor();
        tp0198_2_big.setType(SensorType.TP0198_2);
        tp0198_2_big.setName(SensorType.TP0198_2.getType() + " > 333.5" + MeasurementConstants.DEGREE_CELSIUS.getValue());
        tp0198_2_big.setRange(-40D,1100D);
        tp0198_2_big.setValue(MeasurementConstants.MV.getValue());
        tp0198_2_big.setMeasurement(MeasurementConstants.TEMPERATURE.getValue());
        tp0198_2_big.setErrorFormula("0.0075 * R");
        sensors.add(tp0198_2_big);

        Sensor deltabarS = new Sensor();
        deltabarS.setType(SensorType.DELTABAR_S);
        deltabarS.setName(SensorType.DELTABAR_S.getType());
        deltabarS.setMeasurement(MeasurementConstants.PRESSURE.getValue());
        deltabarS.setErrorFormula("(convR / 100) * 0.075");
        sensors.add(deltabarS);

        Sensor fisherRosemount3051s = new Sensor();
        fisherRosemount3051s.setType(SensorType.FISHER_ROSEMOUNT_3051S);
        fisherRosemount3051s.setName(SensorType.FISHER_ROSEMOUNT_3051S.getType());
        fisherRosemount3051s.setMeasurement(MeasurementConstants.PRESSURE.getValue());
        fisherRosemount3051s.setErrorFormula("(convR / 100) * 0.055");
        sensors.add(fisherRosemount3051s);

        Sensor yokogawa = new Sensor();
        yokogawa.setType(SensorType.YOKOGAWA);
        yokogawa.setName(SensorType.YOKOGAWA.getType());
        yokogawa.setMeasurement(MeasurementConstants.PRESSURE.getValue());
        yokogawa.setErrorFormula("(convR / 100) * 0.2");
        sensors.add(yokogawa);

        Sensor jumoDTransP02 = new Sensor();
        jumoDTransP02.setType(SensorType.JUMO_dTRANS_p02);
        jumoDTransP02.setName(SensorType.JUMO_dTRANS_p02.getType());
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

        calibrators.add(new Fluke725());
        calibrators.add(new Fluke724());
        calibrators.add(new Prova_123());
        calibrators.add(new Fluke718_30G());
        calibrators.add(new Fluke750PD2());
        calibrators.add(new Fluke750PD2_small());

        Lists.saveCalibratorsListToFile(calibrators);
    }

    public static void loadMeasurements(){
        ArrayList<Measurement>measurements = new ArrayList<>();

        measurements.add(new Temperature(MeasurementConstants.DEGREE_CELSIUS));
        measurements.add(new Temperature(MeasurementConstants.KELVIN));
        measurements.add(new Pressure(MeasurementConstants.KPA));
        measurements.add(new Pressure(MeasurementConstants.PA));
        measurements.add(new Pressure(MeasurementConstants.MM_ACVA));
        measurements.add(new Pressure(MeasurementConstants.KG_SM2));
        measurements.add(new Pressure(MeasurementConstants.KG_MM2));
        measurements.add(new Pressure(MeasurementConstants.BAR));
        measurements.add(new Pressure(MeasurementConstants.ML_BAR));

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