package service.converter_tc.ui;

import service.converter_tc.model.Type;

import javax.annotation.Nullable;
import java.util.Map;

public interface TypePanel {
    @Nullable Map.Entry<Type, Double> getType();
}
