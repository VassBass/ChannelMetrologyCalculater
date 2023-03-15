package service.importer.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ModelHolder that = (ModelHolder) o;
        return model == that.model && fields.equals(that.fields);
    }

    @Override
    public int hashCode() {
        return Objects.hash(model, fields);
    }
}
