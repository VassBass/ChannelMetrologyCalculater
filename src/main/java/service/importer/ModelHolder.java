package service.importer;

import java.util.HashMap;
import java.util.Map;

public class ModelHolder {
    private final  Model model;

    private final Map<ModelField, String> fields;

    public ModelHolder(Model model) {
        this.model = model;
        fields = new HashMap<>();
    }
}
