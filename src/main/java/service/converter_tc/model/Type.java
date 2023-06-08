package service.converter_tc.model;

import javax.annotation.Nullable;

public enum Type {
    Cu("Cu"), Pt("Pt"), Pl("П");

    public final String name;
    Type(String name) {
        this.name = name;
    }

    @Nullable
    public static Type getType(String name) {
        switch (name) {
            case "Cu": return Cu;
            case "Pt": return Pt;
            case "П": return Pl;
            default: return null;
        }
    }
}
