package service;

public interface DepartmentService extends Service<String> {
    String[] getAllInStrings();
    boolean resetToDefault();
}
