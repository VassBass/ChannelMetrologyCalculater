package service.channel.info.ui.swing;

import model.ui.DefaultComboBox;
import model.ui.TitledPanel;
import model.ui.builder.CellBuilder;
import service.channel.info.ui.ChannelInfoPathPanel;

import java.awt.*;
import java.util.List;

public class SwingChannelInfoPathPanel extends TitledPanel implements ChannelInfoPathPanel {
    private static final String TITLE_TEXT = "Розташування";

    private final DefaultComboBox departments;
    private final DefaultComboBox areas;
    private final DefaultComboBox processes;
    private final DefaultComboBox installations;

    public SwingChannelInfoPathPanel() {
        super(TITLE_TEXT, Color.BLACK);

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
    public void setDepartments(List<String> departments) {
        this.departments.setList(departments);
    }

    @Override
    public void setAreas(List<String> areas) {
        this.areas.setList(areas);
    }

    @Override
    public void setProcesses(List<String> processes) {
        this.processes.setList(processes);
    }

    @Override
    public void setInstallations(List<String> installations) {
        this.installations.setList(installations);
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
        return departments.getSelectedItem();
    }

    @Override
    public String getSelectedArea() {
        return areas.getSelectedItem();
    }

    @Override
    public String getSelectedProcess() {
        return processes.getSelectedItem();
    }

    @Override
    public String getSelectedInstallation() {
        return installations.getSelectedItem();
    }
}
