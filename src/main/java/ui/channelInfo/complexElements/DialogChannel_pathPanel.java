package ui.channelInfo.complexElements;

import application.Application;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class DialogChannel_pathPanel extends JPanel {

    private JComboBox<String>departments;
    private JComboBox<String>areas;
    private JComboBox<String>processes;
    private JComboBox<String>installations;

    public DialogChannel_pathPanel(){
        super();

        this.createElements();
        this.build();
    }

    private void createElements() {
        this.departments = new JComboBox<>(Application.context.departmentsController.getAllInStrings());
        this.departments.setEditable(true);
        this.departments.setBackground(Color.WHITE);

        this.areas = new JComboBox<>(Application.context.areasController.getAllInStrings());
        this.areas.setEditable(true);
        this.areas.setBackground(Color.WHITE);

        this.processes = new JComboBox<>(Application.context.processesController.getAllInStrings());
        this.processes.setEditable(true);
        this.processes.setBackground(Color.WHITE);

        this.installations = new JComboBox<>(Application.context.installationsController.getAllInStrings());
        this.installations.setEditable(true);
        this.installations.setBackground(Color.WHITE);
    }

    private void build() {
        this.add(this.departments);
        this.add(this.areas);
        this.add(this.processes);
        this.add(this.installations);

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    }

    public void update(String department, String area, String process, String installation){
        if (department != null){
            this.departments.setSelectedItem(department);
        }
        if (area != null){
            this.areas.setSelectedItem(area);
        }
        if (process != null){
            this.processes.setSelectedItem(process);
        }
        if (installation != null){
            this.installations.setSelectedItem(installation);
        }
    }

    public String getDepartment(){
        return Objects.requireNonNull(this.departments.getSelectedItem()).toString();
    }
    public String getArea(){
        return Objects.requireNonNull(this.areas.getSelectedItem()).toString();
    }
    public String getProcess(){
        return Objects.requireNonNull(this.processes.getSelectedItem()).toString();
    }
    public String getInstallation(){
        return Objects.requireNonNull(this.installations.getSelectedItem()).toString();
    }
}