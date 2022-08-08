package service;

import javax.annotation.Nonnull;
import java.util.Collection;

public interface DepartmentService extends Service<String> {
    boolean add(@Nonnull Collection<String> objects);
    String[] getAllInStrings();
    boolean resetToDefault();
}
