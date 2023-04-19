package service.importer;

import service.importer.model.ModelHolder;

import javax.annotation.Nonnull;
import java.util.List;

public interface Importer {
    boolean importing(@Nonnull List<ModelHolder> in);
}
