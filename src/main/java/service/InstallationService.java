package service;

public interface InstallationService extends Service<String> {
    String[] getAllInStrings();
    boolean resetToDefault();
}
