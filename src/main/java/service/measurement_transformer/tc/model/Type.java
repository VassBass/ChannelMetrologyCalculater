package service.measurement_transformer.tc.model;

import javax.annotation.Nullable;

public enum Type {
    Cu("Cu"), Pt("Pt"), Pl("П"), Ni("Ni");

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
            case "Ni": return Ni;
            default: return null;
        }
    }
}
