package settings;

import constants.MeasurementConstants;
import service.FileBrowser;

import java.io.*;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Settings {
    private static final Logger LOGGER = Logger.getLogger(Settings.class.getName());

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
        LOGGER.info("Settings: checking the settings...");
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(FileBrowser.FILE_SETTINGS))){
            settings = (HashMap<String, String>) in.readObject();
        }catch (Exception ex){
            LOGGER.info("Settings: file \"" + FileBrowser.FILE_SETTINGS.getName() + "\" is empty.");
            LOGGER.info("Settings: set default settings");
            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FileBrowser.FILE_SETTINGS))){
                out.writeObject(new HashMap<String, String>());
                setDefaultSettings();
            }catch (Exception e){
                LOGGER.log(Level.WARNING, "Settings: Exception during default setup: ", e);
            }
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
