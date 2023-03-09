package service.importer;

import repository.RepositoryFactory;

import javax.annotation.Nonnull;
import java.util.List;

public interface Importer {
    boolean importing(@Nonnull List<ModelHolder> in, @Nonnull RepositoryFactory repositoryFactory);
}
