package model;

import java.util.Locale;

public enum OS {
    LINUX, WINDOWS, UNKNOWN;

    public static OS getCurrentOS(){
        String os = System.getProperty("os.name").toLowerCase(Locale.ROOT);

        if (os.contains("win")) return WINDOWS;
        if (os.contains("nix") || os.contains("nux")) return LINUX;

        return UNKNOWN;
    }
}
