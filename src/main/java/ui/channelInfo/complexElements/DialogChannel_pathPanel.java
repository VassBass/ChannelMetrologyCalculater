package ui.channelInfo.complexElements;

import ui.UI_Container;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class DialogChannel_pathPanel /*extends JPanel implements UI_Container*/ {/*

    private JComboBox<String>departments;
    private JComboBox<String>areas;
    private JComboBox<String>processes;
    private JComboBox<String>installations;

    public DialogChannel_pathPanel(){
        super();

        this.createElements();
        this.setReactions();
        this.build();
    }

    @Override
    public void createElements() {
        this.departments = new JComboBox<>(Objects.requireNonNull(Lists.departments()).toArray(new String[0]));
        this.departments.setEditable(true);
        this.departments.setBackground(Color.white);

        this.areas = new JComboBox<>(Objects.requireNonNull(Lists.areas()).toArray(new String[0]));
        this.areas.setEditable(true);
        this.areas.setBackground(Color.white);

        this.processes = new JComboBox<>(Objects.requireNonNull(Lists.processes()).toArray(new String[0]));
        this.processes.setEditable(true);
        this.processes.setBackground(Color.white);

        this.installations = new JComboBox<>(Objects.requireNonNull(Lists.installations()).toArray(new String[0]));
        this.installations.setEditable(true);
        this.installations.setBackground(Color.white);
    }

    @Override
    public void setReactions() {}

    @Override
    public void build() {
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
*/}
