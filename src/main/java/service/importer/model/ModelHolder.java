package service.importer.model;

import java.util.HashMap;
import java.util.Map;

public class ModelHolder {
    private final  Model model;

    private final Map<ModelField, String> fields;

    public ModelHolder(Model model) {
        this.model = model;
        fields = new HashMap<>();
    }

    public void setField(ModelField field, String value) {
        fields.put(field, value);
    }

    public String getValue(ModelField field) {
        return fields.get(field);
    }

    public Model getModel() {
        return model;
    }
}
