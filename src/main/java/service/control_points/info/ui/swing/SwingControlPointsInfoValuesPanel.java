package service.control_points.info.ui.swing;

import localization.Labels;
import localization.RootLabelName;
import model.dto.ControlPoints;
import model.ui.*;
import model.ui.builder.CellBuilder;
import service.control_points.info.ControlPointsInfoManager;
import service.control_points.info.ui.ControlPointsInfoValuesPanel;
import util.StringHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.stream.Collectors;

import static javax.swing.SwingConstants.CENTER;
import static model.ui.ButtonCell.SIMPLE;
import static model.ui.builder.CellBuilder.HORIZONTAL;
import static util.StringHelper.FOR_LAST_ZERO;

public class SwingControlPointsInfoValuesPanel extends TitledPanel implements ControlPointsInfoValuesPanel {
    private static final Map<String, String> labels = Labels.getRootLabels();

    private final ControlPointsInfoManager manager;

    private final DefaultPanel valuesPanel;
    private final ArrayList<ValuePanel> values = new ArrayList<>();

    public SwingControlPointsInfoValuesPanel(@Nonnull ControlPointsInfoManager manager, @Nullable ControlPoints cp) {
        super(labels.get(RootLabelName.CONTROL_POINTS), Color.BLACK);
        this.manager = manager;

        valuesPanel = new DefaultPanel();

        DefaultButton addFirstButton = new DefaultButton(labels.get(RootLabelName.ADD_FIRST));
        DefaultButton addIndexButton = new DefaultButton(labels.get(RootLabelName.ADD));
        DefaultButton addLastButton = new DefaultButton(labels.get(RootLabelName.ADD_LAST));
        DefaultButton removeFirstButton = new DefaultButton(labels.get(RootLabelName.DELETE_FIRST));
        DefaultButton removeIndexButton = new DefaultButton(labels.get(RootLabelName.DELETE));
        DefaultButton removeLastButton = new DefaultButton(labels.get(RootLabelName.DELETE_LAST));
        IntegerTextField addIndexField = new IntegerTextField(2);
        addIndexField.setText(Labels.ZERRO);
        IntegerTextField removeIndexField = new IntegerTextField(2);
        removeIndexField.setText(Labels.ZERRO);

        DefaultPanel addIndexPanel = new DefaultPanel();
        addIndexPanel.add(addIndexButton, new CellBuilder().x(0).build());
        addIndexPanel.add(addIndexField, new CellBuilder().x(1).build());

        DefaultPanel removeIndexPanel = new DefaultPanel();
        removeIndexPanel.add(removeIndexButton, new CellBuilder().x(0).build());
        removeIndexPanel.add(removeIndexField, new CellBuilder().x(1).build());

        DefaultPanel buttonsPanel = new DefaultPanel();
        buttonsPanel.add(addFirstButton, new CellBuilder().fill(HORIZONTAL).y(0).build());
        buttonsPanel.add(addIndexPanel, new CellBuilder().fill(HORIZONTAL).y(1).build());
        buttonsPanel.add(addLastButton, new CellBuilder().fill(HORIZONTAL).y(2).build());
        buttonsPanel.add(removeFirstButton, new CellBuilder().fill(HORIZONTAL).y(3).build());
        buttonsPanel.add(removeIndexPanel, new CellBuilder().fill(HORIZONTAL).y(4).build());
        buttonsPanel.add(removeLastButton, new CellBuilder().fill(HORIZONTAL).y(5).build());

        addFirstButton.addActionListener(e -> addValuePanel(0));
        addIndexButton.addActionListener(e -> addValuePanel(Integer.parseInt(addIndexField.getText())));
        addLastButton.addActionListener(e -> addValuePanel(values.size()));
        removeFirstButton.addActionListener(e -> removeValuePanel(0));
        removeIndexButton.addActionListener(e -> removeValuePanel(Integer.parseInt(removeIndexField.getText())));
        removeLastButton.addActionListener(e -> removeValuePanel(values.size() - 1));

        if (Objects.nonNull(cp)) setValues(cp.getValues());

        this.add(new JScrollPane(valuesPanel), new CellBuilder().weightX(0.8).x(0).build());
        this.add(buttonsPanel, new CellBuilder().weightX(0.2).x(1).build());
    }

    @Override
    public void setValues(Map<Double, Double> values) {
        valuesPanel.removeAll();
        TreeMap<Double, Double> sortedMap = new TreeMap<>(values);
        int index = 0;
        for (Map.Entry<Double, Double> entry : sortedMap.entrySet()) {
            ValuePanel vp = new ValuePanel(index);
            vp.setPercentValue(entry.getKey());
            vp.setValue(entry.getValue());
            this.values.add(vp);
            valuesPanel.add(vp, new CellBuilder().fill(HORIZONTAL).y(index++).build());
        }
        manager.updateDialog();
    }

    @Nullable
    @Override
    public Map<Double, Double> getValues() {
        boolean goodInput = values.stream()
                .noneMatch(vp -> Double.isNaN(vp.getPercentValue()) || Double.isNaN(vp.getValue()));
        if (goodInput) {
            this.setTitleColor(Color.BLACK);
            return values.stream().collect(Collectors.toMap(ValuePanel::getPercentValue, ValuePanel::getValue));
        } else {
            this.setTitleColor(Color.RED);
            return null;
        }
    }

    private void addValuePanel(int index) {
        int size = values.size();
        if (index >= size) {
            values.add(new ValuePanel(size));
        } else {
            values.add(index, new ValuePanel(index++));
            for (; index < values.size(); index++) {
                values.get(index).setIndex(index);
            }
        }
        
        valuesPanel.removeAll();
        int y = 0;
        for (ValuePanel p : values) {
            valuesPanel.add(p, new CellBuilder().fill(HORIZONTAL).y(y++).build());
        }
        manager.updateDialog();
    }

    private void removeValuePanel(int index) {
        int size = values.size();
        if (index >= (size - 1)) {
            values.remove(size - 1);
        } else {
            values.remove(index);
            for (;index < values.size(); index++) {
                values.get(index).setIndex(index);
            }
        }

        valuesPanel.removeAll();
        int y = 0;
        for (ValuePanel p : values) {
            valuesPanel.add(p, new CellBuilder().fill(HORIZONTAL).y(y++).build());
        }
        manager.updateDialog();
    }

    private static class ValuePanel extends DefaultPanel {
        private final ButtonCell index;
        private final DefaultTextField percentValue;
        private final DefaultTextField value;

        ValuePanel(int index) {
            super();
            this.index = new ButtonCell(SIMPLE, String.valueOf(index));
            this.percentValue = new DefaultTextField(5, CENTER);
            this.value = new DefaultTextField(5, CENTER);

            this.add(this.index, new CellBuilder().x(0).weightX(0.1).build());
            this.add(percentValue, new CellBuilder().x(1).weightX(0.45).build());
            this.add(value, new CellBuilder().x(2).weightX(0.45).build());
        }

        void setIndex(int index) {
            this.index.setText(String.valueOf(index));
        }

        void setPercentValue(double value) {
            percentValue.setText(StringHelper.roundingDouble(value, FOR_LAST_ZERO));
        }

        void setValue(double value) {
            this.value.setText(StringHelper.roundingDouble(value, FOR_LAST_ZERO));
        }

        double getPercentValue() {
            String val = percentValue.getText();
            return StringHelper.isDouble(val) ? Double.parseDouble(val) : Double.NaN;
        }

        double getValue() {
            String val = value.getText();
            return StringHelper.isDouble(val) ? Double.parseDouble(val) : Double.NaN;
        }
    }
}
