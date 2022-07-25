package service;

public interface AreaService extends Service<String> {
    String[] getAllInStrings();
    boolean resetToDefault();
}
