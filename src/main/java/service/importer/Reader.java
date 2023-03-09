package service.importer;

import javax.annotation.Nonnull;
import java.io.File;
import java.util.List;

public interface Reader {
    List<ModelHolder> read(@Nonnull File file, @Nonnull Model ... models);
}
