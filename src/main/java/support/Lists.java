package support;

import calibrators.Calibrator;

import java.io.*;
import java.util.ArrayList;

import constants.Files;
import measurements.Measurement;
import sensors.Sensor;


@SuppressWarnings("unchecked")
public class Lists {

    private static ArrayList<Sensor> mainSensorsList = null;
    private static ArrayList<Channel> mainChannelsList = null;
    private static ArrayList<String> mainDepartmentsList = null;
    private static ArrayList<String> mainAreasList = null;
    private static ArrayList<String> mainProcessesList = null;
    private static ArrayList<String> mainInstallationsList = null;
    private static ArrayList<Calibrator>mainCalibratorsList = null;
    private static ArrayList<Measurement>mainMeasurementsList = null;
    private static ArrayList<Worker>mainPersonsList = null;

    public static void create() {
        mainSensorsList = sensors();
        mainDepartmentsList = departments();
        mainAreasList = areas();
        mainProcessesList = processes();
        mainInstallationsList = installations();
        mainChannelsList = channels();
        mainCalibratorsList = calibrators();
        mainMeasurementsList = measurements();
    }


    //Getters
    public static ArrayList<Sensor> sensors() {
        ObjectInputStream loader = null;
        if (mainSensorsList==null) {
            try {
                loader = new ObjectInputStream(new FileInputStream(Files.FILE_LIST_SENSORS));
                mainSensorsList = (ArrayList<Sensor>)loader.readObject();
                loader.close();
                return mainSensorsList;

            }catch(Exception ex) {
                Default.loadSensors();
                try {
                    if (loader != null) {
                        loader.close();
                    }
                    loader = new ObjectInputStream(new FileInputStream(Files.FILE_LIST_SENSORS));
                    mainSensorsList = (ArrayList<Sensor>)loader.readObject();
                    loader.close();
                    return mainSensorsList;
                }catch(Exception e) {
                    try {
                        if (loader != null) {
                            loader.close();
                        }
                    }catch (Exception ignored){}
                    e.printStackTrace();
                    return null;
                }
            }
        }else {
            return mainSensorsList;
        }
    }

    public static ArrayList<Channel> channels() {
        ObjectInputStream loader = null;
        if (mainChannelsList==null) {
            try {
                loader = new ObjectInputStream(new FileInputStream(Files.FILE_LIST_CHANNELS));
                mainChannelsList = (ArrayList<Channel>)loader.readObject();
                loader.close();
                return mainChannelsList;
            }catch(Exception ex) {
                Default.loadChannels();
                try {
                    if (loader != null) {
                        loader.close();
                    }
                    loader = new ObjectInputStream(new FileInputStream(Files.FILE_LIST_CHANNELS));
                    mainChannelsList = (ArrayList<Channel>)loader.readObject();
                    loader.close();
                    return mainChannelsList;
                }catch(Exception e) {
                    try {
                        if (loader != null) {
                            loader.close();
                        }
                    }catch (Exception ignored){}
                    e.printStackTrace();
                    return null;
                }
            }
        }else {
            return mainChannelsList;
        }
    }

    public static ArrayList<String>departments(){
        ObjectInputStream loader = null;
        if (mainDepartmentsList==null) {
            try {
                loader = new ObjectInputStream(new FileInputStream(Files.FILE_LIST_DEPARTMENTS));
                mainDepartmentsList = (ArrayList<String>)loader.readObject();
                loader.close();
                return mainDepartmentsList;

            }catch(Exception ex) {
                Default.loadDepartments();
                try {
                    if (loader != null) {
                        loader.close();
                    }
                    loader = new ObjectInputStream(new FileInputStream(Files.FILE_LIST_DEPARTMENTS));
                    mainDepartmentsList = (ArrayList<String>)loader.readObject();
                    loader.close();
                    return mainDepartmentsList;

                }catch(Exception e) {
                    try {
                        if (loader != null) {
                            loader.close();
                        }
                    }catch (Exception ignored){}
                    e.printStackTrace();
                    return null;
                }
            }
        }else {
            return mainDepartmentsList;
        }
    }

    public static ArrayList<String>areas(){
        ObjectInputStream loader = null;
        if (mainAreasList==null) {
            try {
                loader = new ObjectInputStream(new FileInputStream(Files.FILE_LIST_AREAS));
                mainAreasList = (ArrayList<String>)loader.readObject();
                loader.close();
                return mainAreasList;
            }catch(Exception ex) {
                Default.loadAreas();
                try {
                    if (loader != null) {
                        loader.close();
                    }
                    loader = new ObjectInputStream(new FileInputStream(Files.FILE_LIST_AREAS));
                    mainAreasList = (ArrayList<String>)loader.readObject();
                    loader.close();
                    return mainAreasList;

                }catch(Exception e) {
                    try {
                        if (loader != null) {
                            loader.close();
                        }
                    }catch (Exception ignored){}
                    e.printStackTrace();
                    return null;
                }
            }
        }else {
            return mainAreasList;
        }
    }

    public static ArrayList<String>processes(){
        ObjectInputStream loader = null;
        if (mainProcessesList==null) {
            try {
                loader = new ObjectInputStream(new FileInputStream(Files.FILE_LIST_PROCESSES));
                mainProcessesList = (ArrayList<String>)loader.readObject();
                loader.close();
                return mainProcessesList;

            }catch(Exception ex) {
                Default.loadProcesses();
                try {
                    if (loader != null) {
                        loader.close();
                    }
                    loader = new ObjectInputStream(new FileInputStream(Files.FILE_LIST_PROCESSES));
                    mainProcessesList = (ArrayList<String>)loader.readObject();
                    loader.close();
                    return mainProcessesList;

                }catch(Exception e) {
                    try {
                        if (loader != null) {
                            loader.close();
                        }
                    }catch (Exception ignored){}
                    e.printStackTrace();
                    return null;
                }
            }
        }else {
            return mainProcessesList;
        }
    }

    public static ArrayList<String>installations(){
        ObjectInputStream loader = null;
        if (mainInstallationsList==null) {
            try {
                loader = new ObjectInputStream(new FileInputStream(Files.FILE_LIST_INSTALLATIONS));
                mainInstallationsList = (ArrayList<String>)loader.readObject();
                loader.close();
                return mainInstallationsList;

            }catch(Exception ex) {
                Default.loadInstallations();
                try {
                    if (loader != null) {
                        loader.close();
                    }
                    loader = new ObjectInputStream(new FileInputStream(Files.FILE_LIST_INSTALLATIONS));
                    mainInstallationsList = (ArrayList<String>)loader.readObject();
                    loader.close();
                    return mainInstallationsList;

                }catch(Exception e) {
                    try {
                        if (loader != null) {
                            loader.close();
                        }
                    }catch (Exception ignored){}
                    e.printStackTrace();
                    return null;
                }
            }
        }else {
            return mainInstallationsList;
        }
    }

    public static ArrayList<Calibrator>calibrators(){
        ObjectInputStream loader = null;
        if (mainCalibratorsList==null) {
            try {
                loader = new ObjectInputStream(new FileInputStream(Files.FILE_LIST_CALIBRATORS));
                mainCalibratorsList = (ArrayList<Calibrator>)loader.readObject();
                loader.close();
                return mainCalibratorsList;

            }catch(Exception ex) {
                Default.loadCalibrators();
                try {
                    if (loader != null) {
                        loader.close();
                    }
                    loader = new ObjectInputStream(new FileInputStream(Files.FILE_LIST_CALIBRATORS));
                    mainCalibratorsList = (ArrayList<Calibrator>)loader.readObject();
                    loader.close();
                    return mainCalibratorsList;

                }catch(Exception e) {
                    try {
                        if (loader != null) {
                            loader.close();
                        }
                    }catch (Exception ignored){}
                    e.printStackTrace();
                    return null;
                }
            }
        }else {
            return mainCalibratorsList;
        }
    }

    public static ArrayList<Measurement>measurements(){
        ObjectInputStream loader = null;
        if (mainMeasurementsList==null) {
            try {
                loader = new ObjectInputStream(new FileInputStream(Files.FILE_LIST_MEASUREMENTS));
                mainMeasurementsList = (ArrayList<Measurement>)loader.readObject();
                loader.close();
                return mainMeasurementsList;

            }catch(Exception ex) {
                Default.loadMeasurements();
                try {
                    if (loader != null) {
                        loader.close();
                    }
                    loader = new ObjectInputStream(new FileInputStream(Files.FILE_LIST_MEASUREMENTS));
                    mainMeasurementsList = (ArrayList<Measurement>)loader.readObject();
                    loader.close();
                    return mainMeasurementsList;

                }catch(Exception e) {
                    try {
                        if (loader != null) {
                            loader.close();
                        }
                    }catch (Exception ignored){}
                    e.printStackTrace();
                    return null;
                }
            }
        }else {
            return mainMeasurementsList;
        }
    }

    public static ArrayList<Worker>persons(){
        ObjectInputStream loader = null;
        if (mainPersonsList==null){
            try{
                loader = new ObjectInputStream(new FileInputStream(Files.FILE_LIST_PERSONS));
                mainPersonsList = (ArrayList<Worker>) loader.readObject();
                loader.close();
                return mainPersonsList;
            }catch (Exception ex){
                Default.loadPersons();
                try {
                    if (loader != null) {
                        loader.close();
                    }
                    loader = new ObjectInputStream(new FileInputStream(Files.FILE_LIST_PERSONS));
                    mainPersonsList = (ArrayList<Worker>) loader.readObject();
                    loader.close();
                    return mainPersonsList;
                }catch (Exception e){
                    try {
                        if (loader != null) {
                            loader.close();
                        }
                    }catch (Exception ignored){}
                    e.printStackTrace();
                    return null;
                }
            }
        }else {
            return mainPersonsList;
        }
    }

    //Setters
    public static void saveSensorsListToFile(ArrayList<Sensor> sensorsList) {
        try (ObjectOutputStream saver = new ObjectOutputStream(new FileOutputStream(Files.FILE_LIST_SENSORS))){
            saver.writeObject(sensorsList);
            saver.close();
            mainSensorsList = null;
        }catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void saveChannelsListToFile(ArrayList<Channel> channelsList) {
        try (ObjectOutputStream saver = new ObjectOutputStream(new FileOutputStream(Files.FILE_LIST_CHANNELS))){
            saver.writeObject(channelsList);
            saver.close();
            mainChannelsList = null;
        }catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void saveDepartmentsListToFile(ArrayList<String> departmentsList) {
        try (ObjectOutputStream saver = new ObjectOutputStream(new FileOutputStream(Files.FILE_LIST_DEPARTMENTS))){
            saver.writeObject(departmentsList);
            saver.close();
            mainDepartmentsList = null;
        }catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void saveAreasListToFile(ArrayList<String> areasList) {
        try (ObjectOutputStream saver = new ObjectOutputStream(new FileOutputStream(Files.FILE_LIST_AREAS))){
            saver.writeObject(areasList);
            saver.close();
            mainAreasList = null;
        }catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void saveProcessesListToFile(ArrayList<String> processesList) {
        try (ObjectOutputStream saver = new ObjectOutputStream(new FileOutputStream(Files.FILE_LIST_PROCESSES))){
            saver.writeObject(processesList);
            saver.close();
            mainProcessesList = null;
        }catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void saveInstallationsListToFile(ArrayList<String> installationsList) {
        try (ObjectOutputStream saver = new ObjectOutputStream(new FileOutputStream(Files.FILE_LIST_INSTALLATIONS))){
            saver.writeObject(installationsList);
            saver.close();
            mainInstallationsList = null;
        }catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void saveCalibratorsListToFile(ArrayList<Calibrator> calibratorsList) {
        try (ObjectOutputStream saver = new ObjectOutputStream(new FileOutputStream(Files.FILE_LIST_CALIBRATORS))){
            saver.writeObject(calibratorsList);
            saver.close();
            mainCalibratorsList = null;
        }catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void saveMeasurementsListToFile(ArrayList<Measurement> measurementsList) {
        try (ObjectOutputStream saver = new ObjectOutputStream(new FileOutputStream(Files.FILE_LIST_MEASUREMENTS))){
            saver.writeObject(measurementsList);
            saver.close();
            mainMeasurementsList = null;
        }catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void savePersonsListToFile(ArrayList<Worker>workersList) {
        try (ObjectOutputStream saver = new ObjectOutputStream(new FileOutputStream(Files.FILE_LIST_PERSONS))){
            saver.writeObject(workersList);
            saver.close();
            mainPersonsList = null;
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
