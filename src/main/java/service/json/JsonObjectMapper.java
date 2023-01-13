package service.json;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface JsonObjectMapper {
    <O> String objectToJson(@Nonnull O object);
    @Nullable <O> O JsonToObject(@Nonnull String json, Class<O> clazz);
}
