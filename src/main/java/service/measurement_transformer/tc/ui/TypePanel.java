package service.measurement_transformer.tc.ui;

import service.measurement_transformer.tc.model.Type;

import javax.annotation.Nullable;
import java.util.Map;

public interface TypePanel {
    @Nullable Map.Entry<Type, Double> getType();
}
