package service.channel.info.ui;

import java.util.Collection;

public interface ChannelInfoPathPanel {
    void setDepartments(Collection<String> departments);
    void setAreas(Collection<String> areas);
    void setProcesses(Collection<String> processes);
    void setInstallations(Collection<String> installations);

    void setDepartment(String department);
    void setArea(String area);
    void setProcess(String process);
    void setInstallation(String installation);

    String getSelectedDepartment();
    String getSelectedArea();
    String getSelectedProcess();
    String getSelectedInstallation();
}
