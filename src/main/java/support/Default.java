package support;

import calibrators.*;

import java.util.ArrayList;

import constants.MeasurementConstants;
import constants.WorkPositions;
import measurements.Measurement;
import measurements.Pressure;
import measurements.Temperature;
import sensors.*;

public class Default {
    
    public static void loadForms(){
        UnZipper unzipper = new UnZipper();
        unzipper.unzip();
    }

    public static void loadSensors(){
        ArrayList<Sensor> sensors = new ArrayList<>();

        sensors.add(new TCM_50M());
        sensors.add(new TCP_100());
        sensors.add(new TXA_2388_typeK());
        sensors.add(new TXA_0395_typeK());
        sensors.add(new Deltabar_S());
        sensors.add(new Yokogawa());
        sensors.add(new JUMO_dTRANS_p02());
        sensors.add(new Fisher_Rosemount_3051S());
        sensors.add(new TP0198_2());

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