package ui.channelInfo.panel;

import repository.impl.AreaRepositorySQLite;
import repository.impl.DepartmentRepositorySQLite;
import repository.impl.InstallationRepositorySQLite;
import repository.impl.ProcessRepositorySQLite;
import ui.channelInfo.DialogChannel;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyListener;
import java.util.Optional;

public class PanelPath extends JPanel {
    private static final String PATH = "Розташування";

    private final DialogChannel parent;

    private final JComboBox<String>departments;
    private final JComboBox<String>areas;
    private final JComboBox<String>processes;
    private final JComboBox<String>installations;

    public PanelPath(@Nonnull DialogChannel parent){
        super(new GridBagLayout());
        this.parent = parent;

        departments = new DepartmentComboBox();
        areas = new AreaComboBox();
        processes = new ProcessComboBox();
        installations = new InstallationComboBox();

        this.setBackground(Color.WHITE);
        this.setBorder(BorderFactory.createTitledBorder(PATH));

        this.add(departments, new Cell(0,0));
        this.add(areas, new Cell(0,1));
        this.add(processes, new Cell(0,2));
        this.add(installations, new Cell(0,3));
    }

    @Override
    public synchronized void addKeyListener(@Nonnull KeyListener l) {
        departments.getEditor().getEditorComponent().addKeyListener(l);
        areas.getEditor().getEditorComponent().addKeyListener(l);
        processes.getEditor().getEditorComponent().addKeyListener(l);
        installations.getEditor().getEditorComponent().addKeyListener(l);
    }

    public void updateAll(@Nullable String department,
                       @Nullable String area,
                       @Nullable String process,
                       @Nullable String installation){
        if (department != null) departments.setSelectedItem(department);
        if (area != null) areas.setSelectedItem(area);
        if (process != null) processes.setSelectedItem(process);
        if (installation != null) installations.setSelectedItem(installation);
    }

    public void updateDepartment(@Nonnull String department){
        departments.setSelectedItem(department);
    }

    public void updateArea(@Nonnull String area){
        areas.setSelectedItem(area);
    }

    public void updateProcess(@Nonnull String process){
        processes.setSelectedItem(process);
    }

    public void updateInstallation(@Nonnull String installation){
        installations.setSelectedItem(installation);
    }

    public Optional<String> getDepartment(){
        Object selectedDepartment = departments.getSelectedItem();
        return selectedDepartment == null ? Optional.empty() : Optional.of(selectedDepartment.toString());
    }

    public Optional<String> getArea(){
        Object selectedArea = areas.getSelectedItem();
        return selectedArea == null ? Optional.empty() : Optional.of(selectedArea.toString());
    }

    public Optional<String> getProcess(){
        Object selectedProcess = processes.getSelectedItem();
        return selectedProcess == null ? Optional.empty() : Optional.of(selectedProcess.toString());
    }

    public Optional<String> getInstallation(){
        Object selectedInstallation = installations.getSelectedItem();
        return selectedInstallation == null ? Optional.empty() : Optional.of(selectedInstallation.toString());
    }


    /**
     * ComboBox of departments
     */
    private class DepartmentComboBox extends JComboBox<String> {

        private DepartmentComboBox(){
            super(DepartmentRepositorySQLite.getInstance().getAll().toArray(new String[0]));
            this.setEditable(true);
            this.setBackground(Color.WHITE);

            this.addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    parent.specialCharactersPanel.setFieldForInsert(null);
                }
            });
        }

    }

    /**
     * ComboBox of areas
     */
    private class AreaComboBox extends JComboBox<String> {

        private AreaComboBox(){
            super(AreaRepositorySQLite.getInstance().getAll().toArray(new String[0]));
            this.setEditable(true);
            this.setBackground(Color.WHITE);

            this.addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    parent.specialCharactersPanel.setFieldForInsert(null);
                }
            });
        }

    }

    /**
     * ComboBox of processes
     */
    private class ProcessComboBox extends JComboBox<String> {

        private ProcessComboBox(){
            super(ProcessRepositorySQLite.getInstance().getAll().toArray(new String[0]));
            this.setEditable(true);
            this.setBackground(Color.WHITE);

            this.addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    parent.specialCharactersPanel.setFieldForInsert(null);
                }
            });
        }

    }

    /**
     * ComboBox of installations
     */
    private class InstallationComboBox extends JComboBox<String> {

        private InstallationComboBox(){
            super(InstallationRepositorySQLite.getInstance().getAll().toArray(new String[0]));
            this.setEditable(true);
            this.setBackground(Color.WHITE);

            this.addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    parent.specialCharactersPanel.setFieldForInsert(null);
                }
            });
        }

    }

    private static class Cell extends GridBagConstraints {
        private Cell(int x, int y){
            super();
            this.fill = BOTH;
            this.weightx = 1D;
            this.weighty = 1D;

            this.gridx = x;
            this.gridy = y;
        }
    }
}
