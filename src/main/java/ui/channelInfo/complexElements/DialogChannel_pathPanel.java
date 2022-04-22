package ui.channelInfo.complexElements;

import application.Application;
import ui.channelInfo.DialogChannel;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Objects;

public class DialogChannel_pathPanel extends JPanel {
    private static final String PATH = "Розташування";

    private final DialogChannel parent;

    private JComboBox<String>departments;
    private JComboBox<String>areas;
    private JComboBox<String>processes;
    private JComboBox<String>installations;

    public DialogChannel_pathPanel(DialogChannel parent){
        super();
        this.parent = parent;

        this.createElements();
        this.setReactions();
        this.build();
    }

    private void createElements() {
        this.departments = new JComboBox<>(Application.context.departmentService.getAllInStrings());
        this.departments.setEditable(true);
        this.departments.setBackground(Color.WHITE);

        this.areas = new JComboBox<>(Application.context.areaService.getAllInStrings());
        this.areas.setEditable(true);
        this.areas.setBackground(Color.WHITE);

        this.processes = new JComboBox<>(Application.context.processService.getAllInStrings());
        this.processes.setEditable(true);
        this.processes.setBackground(Color.WHITE);

        this.installations = new JComboBox<>(Application.context.installationService.getAllInStrings());
        this.installations.setEditable(true);
        this.installations.setBackground(Color.WHITE);
    }

    private void setReactions(){
        this.departments.addFocusListener(focus);
        this.areas.addFocusListener(focus);
        this.processes.addFocusListener(focus);
        this.installations.addFocusListener(focus);
    }

    private void build() {
        this.setBackground(Color.WHITE);

        this.add(this.departments);
        this.add(this.areas);
        this.add(this.processes);
        this.add(this.installations);

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        TitledBorder border = BorderFactory.createTitledBorder(PATH);
        this.setBorder(border);
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

    private final FocusListener focus = new FocusAdapter() {
        @Override
        public void focusGained(FocusEvent e) {
            parent.resetSpecialCharactersPanel();
        }
    };
}