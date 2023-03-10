package service.importer;

import service.importer.model.Model;
import service.importer.model.ModelHolder;

import javax.annotation.Nonnull;
import java.io.File;
import java.util.List;

public interface Reader {
    List<ModelHolder> read(@Nonnull File file);
}
