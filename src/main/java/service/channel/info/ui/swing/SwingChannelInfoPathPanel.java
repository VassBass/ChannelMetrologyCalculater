package service.channel.info.ui.swing;

import localization.Labels;
import localization.RootLabelName;
import model.ui.DefaultComboBox;
import model.ui.TitledPanel;
import model.ui.builder.CellBuilder;
import service.channel.info.ui.ChannelInfoPathPanel;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;

public class SwingChannelInfoPathPanel extends TitledPanel implements ChannelInfoPathPanel {
    private final DefaultComboBox departments;
    private final DefaultComboBox areas;
    private final DefaultComboBox processes;
    private final DefaultComboBox installations;

    public SwingChannelInfoPathPanel() {
        super(Labels.getRootLabels().get(RootLabelName.LOCATION), Color.BLACK);

        departments = new DefaultComboBox(true);
        areas = new DefaultComboBox(true);
        processes = new DefaultComboBox(true);
        installations = new DefaultComboBox(true);

        this.add(departments, new CellBuilder().x(0).y(0).build());
        this.add(areas, new CellBuilder().x(1).y(0).build());
        this.add(processes, new CellBuilder().x(0).y(1).build());
        this.add(installations, new CellBuilder().x(1).y(1).build());
    }

    @Override
    public void setDepartments(Collection<String> departments) {
        this.departments.setList(new ArrayList<>(departments));
    }

    @Override
    public void setAreas(Collection<String> areas) {
        this.areas.setList(new ArrayList<>(areas));
    }

    @Override
    public void setProcesses(Collection<String> processes) {
        this.processes.setList(new ArrayList<>(processes));
    }

    @Override
    public void setInstallations(Collection<String> installations) {
        this.installations.setList(new ArrayList<>(installations));
    }

    @Override
    public void setDepartment(String department) {
        departments.setSelectedItem(department);
    }

    @Override
    public void setArea(String area) {
        areas.setSelectedItem(area);
    }

    @Override
    public void setProcess(String process) {
        processes.setSelectedItem(process);
    }

    @Override
    public void setInstallation(String installation) {
        installations.setSelectedItem(installation);
    }

    @Override
    public String getSelectedDepartment() {
        return departments.getSelectedString();
    }

    @Override
    public String getSelectedArea() {
        return areas.getSelectedString();
    }

    @Override
    public String getSelectedProcess() {
        return processes.getSelectedString();
    }

    @Override
    public String getSelectedInstallation() {
        return installations.getSelectedString();
    }
}
