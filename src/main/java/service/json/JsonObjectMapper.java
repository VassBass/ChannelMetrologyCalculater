package service.json;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;

public interface JsonObjectMapper {
    <O> String objectToJson(@Nonnull O object);
    @Nullable <O> O jsonToObject(@Nonnull String json, Class<O> clazz);
    Map<Double, Double> jsonToDoubleMap(@Nonnull String json);
    String doubleMapToJson(Map<Double, Double> doubleMap);
}
