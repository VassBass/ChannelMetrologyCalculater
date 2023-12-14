package model;

import java.util.Locale;

public enum OS {
    LINUX("Linux"), WINDOWS("Windows"), UNKNOWN("Unknown");

    private final String name;

    public static OS getCurrentOS(){
        String os = System.getProperty("os.name").toLowerCase(Locale.ROOT);

        if (os.contains("win")) return WINDOWS;
        if (os.contains("nix") || os.contains("nux")) return LINUX;

        return UNKNOWN;
    }

    OS(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
