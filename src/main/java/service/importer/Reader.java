package service.importer;

import java.io.File;
import java.util.List;

public interface Reader {
    List<ModelHolder> readAll(File file);
}
