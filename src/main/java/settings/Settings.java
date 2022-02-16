package settings;

import constants.MeasurementConstants;
import service.FileBrowser;

import java.io.*;
import java.util.HashMap;

public class Settings {
    private static HashMap<String, String> settings = null;

    private static void setDefaultSettings(){

        String nameTemperatureMethod = "МКМХ №5300.01:18";
        String namePressureMethod = "МКМХ №5300.02:18";
        String nameConsumptionMethod = "МКМХ №5300.07:20";

        setSettingValue(MeasurementConstants.TEMPERATURE.getValue(), nameTemperatureMethod);
        setSettingValue(MeasurementConstants.PRESSURE.getValue(), namePressureMethod);
        setSettingValue(MeasurementConstants.CONSUMPTION.getValue(), nameConsumptionMethod);
    }

    @SuppressWarnings("unchecked")
    public static void checkSettings(){
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(FileBrowser.FILE_SETTINGS))){
            settings = (HashMap<String, String>) in.readObject();
        }catch (Exception ex){
            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FileBrowser.FILE_SETTINGS))){
                out.writeObject(new HashMap<String, String>());
                setDefaultSettings();
            }catch (Exception ignored){}
        }
    }

    public static String getSettingValue(String key){
        return getSettings().get(key);
    }

    public static void setSettingValue(String key, String value){
        getSettings().put(key, value);
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FileBrowser.FILE_SETTINGS))){
            out.writeObject(settings);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private static HashMap<String, String> getSettings(){
        if (settings == null){
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(FileBrowser.FILE_SETTINGS))){
                settings = (HashMap<String, String>) in.readObject();
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
        return settings;
    }
}
