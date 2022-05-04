package service;

import java.util.Locale;

public enum SystemData {
    SYS_UNIX, SYS_WINDOWS, SYS_UNKNOWN;

    public static SystemData osName(){
        String os = System.getProperty("os.name").toLowerCase(Locale.ROOT);

        if (os.contains("win")) return SYS_WINDOWS;
        if (os.contains("nix") || os.contains("nux")) return SYS_UNIX;

        return SYS_UNKNOWN;
    }
}
