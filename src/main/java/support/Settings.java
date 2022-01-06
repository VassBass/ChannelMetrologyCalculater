package support;

import constants.Files;
import constants.MeasurementConstants;

import java.io.*;

public class Settings {

    private static Values settings = null;

    private static void setDefaultSettings(){

        String nameTemperatureMethod = "МКМХ №5300.01:18";
        String namePressureMethod = "МКМХ №5300.02:18";
        String nameConsumptionMethod = "МКМХ №5300.07:20";

        setSettingValue(MeasurementConstants.TEMPERATURE.getValue(), nameTemperatureMethod);
        setSettingValue(MeasurementConstants.PRESSURE.getValue(), namePressureMethod);
        setSettingValue(MeasurementConstants.CONSUMPTION.getValue(), nameConsumptionMethod);
    }

    public static void checkSettings(){
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(Files.FILE_SETTINGS))){
            settings = (Values) in.readObject();
        }catch (Exception ex){
            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(Files.FILE_SETTINGS))){
                out.writeObject(new Values());
                setDefaultSettings();
            }catch (Exception ignored){}
        }
    }

    public static String getSettingValue(String key){
        return getSettings().getStringValue(key);
    }

    public static void setSettingValue(String key, String value){
        getSettings().putValue(key, value);
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(Files.FILE_SETTINGS))){
            out.writeObject(settings);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private static Values getSettings(){
        if (settings == null){
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(Files.FILE_SETTINGS))){
                settings = (Values) in.readObject();
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
        return settings;
    }
}
