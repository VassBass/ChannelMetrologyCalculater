package service.calibrator.info.ui.swing;

import localization.Labels;
import localization.RootLabelName;
import model.ui.ButtonCell;
import model.ui.DefaultTextField;
import model.ui.TitledPanel;
import model.ui.builder.CellBuilder;
import service.calibrator.info.ui.CalibratorInfoRangePanel;
import util.StringHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import static javax.swing.SwingConstants.RIGHT;
import static model.ui.ButtonCell.SIMPLE;
import static util.StringHelper.FOR_LAST_ZERO;

public class SwingCalibratorInfoRangePanel extends TitledPanel implements CalibratorInfoRangePanel {
    private final DefaultTextField rangeMin;
    private final DefaultTextField rangeMax;
    private final ButtonCell measurementValue;

    public SwingCalibratorInfoRangePanel() {
        super(Labels.getRootLabels().get(RootLabelName.RANGE));

        rangeMin = new DefaultTextField(6);
        rangeMin.setText("0.00");
        rangeMin.setHorizontalAlignment(RIGHT);
        rangeMin.setFocusListener(focusListener);

        rangeMax = new DefaultTextField(6);
        rangeMax.setText("100.00");
        rangeMax.setFocusListener(focusListener);

        measurementValue = new ButtonCell(SIMPLE);

        this.add(rangeMin, new CellBuilder().x(0).build());
        this.add(new ButtonCell(SIMPLE, "..."), new CellBuilder().x(1).build());
        this.add(rangeMax, new CellBuilder().x(2).build());
        this.add(measurementValue, new CellBuilder().x(3).build());
    }

    @Override
    public double getRangeMin() {
        String min = rangeMin.getText();
        if (StringHelper.isDouble(min)) {
            this.setTitleColor(Color.BLACK);
            return Double.parseDouble(min);
        } else {
            this.setTitleColor(Color.RED);
            return Double.NaN;
        }
    }

    @Override
    public double getRangeMax() {
        String max = rangeMax.getText();
        if (StringHelper.isDouble(max)) {
            this.setTitleColor(Color.BLACK);
            return Double.parseDouble(max);
        } else {
            this.setTitleColor(Color.RED);
            return Double.NaN;
        }
    }

    @Override
    public String getMeasurementValue() {
        return measurementValue.getText();
    }

    @Override
    public void setRangeMin(double value) {
        rangeMin.setText(StringHelper.roundingDouble(value, FOR_LAST_ZERO));
    }

    @Override
    public void setRangeMax(double value) {
        rangeMax.setText(StringHelper.roundingDouble(value, FOR_LAST_ZERO));
    }

    @Override
    public void setMeasurementValue(String value) {
        measurementValue.setText(value);
    }

    private final FocusListener focusListener = new FocusListener() {
        @Override
        public void focusGained(FocusEvent e) {
            ((JTextField) e.getSource()).selectAll();
        }

        @Override
        public void focusLost(FocusEvent e) {
            String rMin = rangeMin.getText();
            String rMax = rangeMax.getText();
            if (StringHelper.isDouble(rMin, rMax)) {
                SwingCalibratorInfoRangePanel.this.setTitleColor(Color.BLACK);
                DefaultTextField source = (DefaultTextField) e.getSource();
                double min = Double.parseDouble(rMin);
                double max = Double.parseDouble(rMax);
                if (source.equals(rangeMin) && min > max) rangeMax.setText(rMin);
                if (source.equals(rangeMax) && min > max) rangeMin.setText(rMax);
            } else {
                SwingCalibratorInfoRangePanel.this.setTitleColor(Color.RED);
            }
        }
    };
}
