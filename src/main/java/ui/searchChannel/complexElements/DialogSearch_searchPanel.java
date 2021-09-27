package ui.searchChannel.complexElements;

import support.Converter;
import constants.ChannelConstants;
import support.Channel;
import support.Lists;
import ui.searchChannel.DialogSearch;

import javax.swing.*;
import java.awt.*;
import java.util.Calendar;
import java.util.Objects;

public class DialogSearch_searchPanel extends JPanel {
    private final DialogSearch parent;

    private JTextField userText;
    private JComboBox<String> searchCombo;
    private JComboBox<String> departments;
    private JComboBox<String> areas;
    private JComboBox<String> processes;
    private JComboBox<String> installations;
    private DialogSearch_datePanel datePanel;

    private ChannelConstants field;

    public DialogSearch_searchPanel(DialogSearch parent){
        super(new FlowLayout());
        this.parent = parent;

        this.field = ChannelConstants.CODE;
        this.build(this.field);
    }

    public void update(ChannelConstants field){
        this.build(field);
        this.parent.setVisible(false);
        this.parent.setVisible(true);
    }

    private void build(ChannelConstants field) {
        this.removeAll();
        this.field = field;
        switch (field) {
            case CODE:
            case NAME:
            case TECHNOLOGY_NUMBER:
            case PROTOCOL_NUMBER:
                this.setLayout(new FlowLayout());
                this.userText = new JTextField(10);
                this.add(this.userText);
                parent.setSize(700, 120);
                break;
            case MEASUREMENT:
            case DEPARTMENT:
            case AREA:
            case PROCESS:
            case INSTALLATION:
            case SENSOR:
                this.setLayout(new FlowLayout());
                this.searchCombo = new JComboBox<>(items(field));
                this.searchCombo.setBackground(Color.white);
                this.add(searchCombo);
                parent.setSize(900, 120);
                break;
            case FULL_PATH:
                this.setLayout(new GridBagLayout());
                String[] toComboDepartment = Objects.requireNonNull(Lists.departments()).toArray(new String[0]);
                String[] toComboArea = Objects.requireNonNull(Lists.areas()).toArray(new String[0]);
                String[] toComboProcess = Objects.requireNonNull(Lists.processes()).toArray(new String[0]);
                String[] toComboInstallation = Objects.requireNonNull(Lists.installations()).toArray(new String[0]);
                this.departments = new JComboBox<>(toComboDepartment);
                this.departments.setEditable(true);
                this.departments.setBackground(Color.white);
                this.areas = new JComboBox<>(toComboArea);
                this.areas.setEditable(true);
                this.areas.setBackground(Color.white);
                this.processes = new JComboBox<>(toComboProcess);
                this.processes.setEditable(true);
                this.processes.setBackground(Color.white);
                this.installations = new JComboBox<>(toComboInstallation);
                this.installations.setEditable(true);

                this.add(this.departments, new Cell(0, 0));
                this.add(this.areas, new Cell(0, 1));
                this.add(this.processes, new Cell(1, 0));
                this.add(this.installations, new Cell(1, 1));
                this.installations.setBackground(Color.white);
                parent.setSize(1100, 120);
                break;
            case THIS_DATE:
            case NEXT_DATE:
                this.field = ChannelConstants.NEXT_DATE;
                this.setLayout(new FlowLayout());
                this.datePanel = new DialogSearch_datePanel();
                this.datePanel.update(Calendar.getInstance());
                this.add(this.datePanel);
                parent.setSize(700, 120);
                break;
        }
    }

    private static String[]items(ChannelConstants field) {
        String[]i = null;

        switch (field) {
            case MEASUREMENT:
                i = new String[Objects.requireNonNull(Lists.measurements()).size()];
                for (int x=0;x<i.length;x++) {
                    i[x] = Objects.requireNonNull(Lists.measurements()).get(x).getName();
                }
                break;
            case DEPARTMENT:
                i = Objects.requireNonNull(Lists.departments()).toArray(new String[0]);
                break;
            case AREA:
                i = Objects.requireNonNull(Lists.areas()).toArray(new String[0]);
                break;
            case PROCESS:
                i = Objects.requireNonNull(Lists.processes()).toArray(new String[0]);
                break;
            case INSTALLATION:
                i = Objects.requireNonNull(Lists.installations()).toArray(new String[0]);
                break;
            case SENSOR:
                i = new String[Objects.requireNonNull(Lists.sensors()).size()];
                for (int x=0;x<i.length;x++) {
                    i[x] = Objects.requireNonNull(Lists.sensors()).get(x).getType().getType();
                }
                break;
        }

        return i;
    }

    public String getValue(){
        switch (this.field) {
            case CODE:
            case NAME:
            case TECHNOLOGY_NUMBER:
            case PROTOCOL_NUMBER:
                return this.userText.getText();
            case MEASUREMENT:
            case DEPARTMENT:
            case AREA:
            case PROCESS:
            case INSTALLATION:
            case SENSOR:
                return Objects.requireNonNull(this.searchCombo.getSelectedItem()).toString();
            case FULL_PATH:
                Channel channel = new Channel();
                channel.setDepartment(Objects.requireNonNull(this.departments.getSelectedItem()).toString());
                channel.setArea(Objects.requireNonNull(this.areas.getSelectedItem()).toString());
                channel.setProcess(Objects.requireNonNull(this.processes.getSelectedItem()).toString());
                channel.setInstallation(Objects.requireNonNull(this.installations.getSelectedItem()).toString());
                return channel.getFullPath();
            case THIS_DATE:
            case NEXT_DATE:
                return Converter.dateToString(this.datePanel.getDate());
            default:
                return null;
        }
    }

    private static class Cell extends GridBagConstraints {

        private static final long serialVersionUID = 1L;

        protected Cell(int x, int y) {
            super();

            this.fill = HORIZONTAL;

            this.gridx = x;
            this.gridy = y;
        }

    }
}
