package ui.calibratorsList.calibratorInfo.complexElements;

import converters.VariableConverter;
import model.Measurement;
import service.repository.repos.measurement.MeasurementRepositorySQLite;
import ui.calibratorsList.calibratorInfo.CalibratorInfoDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.Objects;

public class CalibratorRangePanel extends JPanel {
    private static final String DEFAULT_VALUE = "0.00";
    private static final String DASH = " - ";

    private final CalibratorInfoDialog parent;

    private JTextField rangeMin, rangeMax;
    private JComboBox<String>value;
    private JLabel t;
    private String rMin, rMax;

    private String measurement;
    private ValueModel valuesModel;

    public CalibratorRangePanel(CalibratorInfoDialog parent){
        super(new GridBagLayout());
        this.parent = parent;

        this.createElements();
        this.setReactions();
        this.build();
    }

    private void createElements() {
        this.rangeMin = new JTextField(DEFAULT_VALUE,4);
        this.rangeMin.setHorizontalAlignment(SwingConstants.CENTER);

        this.rangeMax = new JTextField(DEFAULT_VALUE,4);
        this.rangeMax.setHorizontalAlignment(SwingConstants.CENTER);

        this.t = new JLabel(DASH);
        this.t.setHorizontalAlignment(SwingConstants.CENTER);

        this.valuesModel = new ValueModel();
        this.valuesModel.setList(this.measurement);
        this.value = new JComboBox<>(this.valuesModel);
        this.value.setBackground(Color.WHITE);
    }

    private static class ValueModel extends AbstractListModel<String> implements ComboBoxModel<String>{
        private final ArrayList<String>list;
        private String selected;

        public ValueModel(){
            this.list = new ArrayList<>();
        }

        public void setList(String measurement){
            this.list.clear();
            if (measurement != null && measurement.equals(Measurement.PRESSURE)) {
                ArrayList<Measurement> measurements = new ArrayList<>(MeasurementRepositorySQLite.getInstance().getAll());
                for (Measurement m : Objects.requireNonNull(measurements)) {
                    if (m.getName().equals(measurement)) {
                        this.list.add(m.getValue());
                    }
                }
            }else {
                this.list.add(DASH);
            }
        }

        public void add(String item){
            this.list.add(item);
            fireContentsChanged(this,0,list.size());
        }

        @Override
        public void setSelectedItem(Object anItem) {
            this.selected = (String) anItem;
        }

        public void setSelectedIndex(int index){
            if (index >= 0 && index < this.list.size()){
                this.selected = list.get(index);
            }
            fireContentsChanged(this,0,list.size());
        }

        @Override
        public Object getSelectedItem() {
            return this.selected;
        }

        @Override
        public int getSize() {
            return list.size();
        }

        @Override
        public String getElementAt(int index) {
            return list.get(index);
        }
    }

    private void setReactions() {
        this.rangeMin.addFocusListener(this.rangeFocus);
        this.rangeMax.addFocusListener(this.rangeFocus);

        this.value.addFocusListener(focusListener);
    }

    private void build() {
        this.setBackground(Color.WHITE);

        this.add(this.rangeMin, new Cell(0));
        this.add(this.t, new Cell(1));
        this.add(this.rangeMax, new Cell(2));
        this.add(this.value, new Cell(3));
    }

    public void setRange(double r1, double r2){
        if (r1 >= r2){
            this.rMax = String.valueOf(r1);
            this.rMin = String.valueOf(r2);
        }else {
            this.rMax = String.valueOf(r2);
            this.rMin = String.valueOf(r1);
        }
        this.rangeMax.setText(this.rMax);
        this.rangeMin.setText(this.rMin);
    }

    @Override
    public void setEnabled(boolean enabled) {
        if (enabled){
            this.value.setModel(this.valuesModel);
            this.rangeMin.setText(this.rMin);
            this.rangeMax.setText(this.rMax);
        }else {
            this.rMin = this.rangeMin.getText();
            this.rMax = this.rangeMax.getText();
        }
        this.value.setEnabled(enabled);
        this.rangeMin.setEnabled(enabled);
        this.rangeMax.setEnabled(enabled);
    }

    private void setDisabledAndEmpty(boolean disabledAndEmpty){
        if (disabledAndEmpty){
            this.setEnabled(false);
            this.value.setModel(new DefaultComboBoxModel<>(new String[]{DASH}));
            this.rangeMin.setText(DEFAULT_VALUE);
            this.rangeMax.setText(DEFAULT_VALUE);
        }else {
            this.setEnabled(true);
        }
    }

    public void setValues(String measurement, String selected){
        this.measurement = measurement;
        this.valuesModel.setList(measurement);
        if (measurement.equals(Measurement.PRESSURE)) {
            setDisabledAndEmpty(false);
            if (selected == null) {
                this.valuesModel.setSelectedIndex(0);
            } else {
                this.valuesModel.setSelectedItem(selected);
            }
        } else {
            this.setDisabledAndEmpty(true);
        }
    }

    public String getValue(){
        return Objects.requireNonNull(this.value.getSelectedItem()).toString();
    }

    public double getRangeMin(){
        return Double.parseDouble(VariableConverter.doubleString(rangeMin.getText()));
    }

    public double getRangeMax(){
        return Double.parseDouble(VariableConverter.doubleString(rangeMax.getText()));
    }

    private final FocusListener rangeFocus = new FocusListener() {
        @Override
        public void focusGained(FocusEvent e) {
            JTextField field = (JTextField) e.getSource();
            field.selectAll();
            parent.resetSpecialCharactersPanel();
        }

        @Override
        public void focusLost(FocusEvent e) {
            if (rangeMin.getText().length() == 0){
                rangeMin.setText(DEFAULT_VALUE);
            }
            if (rangeMax.getText().length() == 0){
                rangeMax.setText(DEFAULT_VALUE);
            }
            double r1 = Double.parseDouble(VariableConverter.doubleString(rangeMin.getText()));
            double r2 = Double.parseDouble(VariableConverter.doubleString(rangeMax.getText()));
            setRange(r1, r2);
        }
    };

    private final FocusListener focusListener = new FocusAdapter() {
        @Override
        public void focusGained(FocusEvent e) {
            parent.resetSpecialCharactersPanel();
        }
    };

    private static class Cell extends GridBagConstraints {
        protected Cell(int x){
            super();

            this.fill = BOTH;
            this.weightx = 1D;
            this.weighty = 1D;

            this.gridx = x;
            this.gridy = 0;
        }
    }
}