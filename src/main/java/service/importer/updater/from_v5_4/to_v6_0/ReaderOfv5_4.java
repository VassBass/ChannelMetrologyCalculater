package service.importer.updater.from_v5_4.to_v6_0;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.RepositoryFactory;
import service.importer.ModelHolder;
import service.importer.Reader;

import java.io.File;
import java.util.List;

public class ReaderOfv5_4 implements Reader {
    private static final Logger logger = LoggerFactory.getLogger(ReaderOfv5_4.class);

    @Override
    public List<ModelHolder> readAll(File file) {
        return null;
    }
}
