package service.importer;

import service.importer.model.ModelHolder;

import javax.annotation.Nullable;

public interface Transformer {
    @Nullable
    <T> T transform(ModelHolder source, Class<T> result);
}
