package updater;

import constants.Key;
import settings.Settings;

public class Updater {
    private static final String V5_2 = "v5.2";
    private static final String V5_3 = "v5.3";

    public static void update(){
        String version = Settings.getSettingValue(Key.VERSION);
        if (version == null || version.equals(V5_2)){
            ToVersion5_3 upTo5_3 = new ToVersion5_3();
            upTo5_3.update();
            Settings.setSettingValue(Key.VERSION, V5_3);
        }
    }
}
