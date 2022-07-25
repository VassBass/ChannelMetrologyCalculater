package service;

public interface ProcessService extends Service<String> {
    String[] getAllInStrings();
    boolean resetToDefault();
}
