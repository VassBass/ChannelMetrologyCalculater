package service.channel.info.ui;

import java.util.List;

public interface ChannelInfoPathPanel {
    void setDepartments(List<String> departments);
    void setAreas(List<String> areas);
    void setProcesses(List<String> processes);
    void setInstallations(List<String> installations);

    void setDepartment(String department);
    void setArea(String area);
    void setProcess(String process);
    void setInstallation(String installation);

    String getSelectedDepartment();
    String getSelectedArea();
    String getSelectedProcess();
    String getSelectedInstallation();
}
