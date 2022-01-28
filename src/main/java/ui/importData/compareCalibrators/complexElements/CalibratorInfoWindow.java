package ui.importData.compareCalibrators.complexElements;

import model.Calibrator;
import ui.model.ButtonCell;

import javax.swing.*;
import java.awt.*;

public class CalibratorInfoWindow extends JWindow {
    private static final String MEASUREMENT = "Вид вимірювання:";
    private static final String NAME = "Назва:";
    private static final String TYPE = "Тип:";
    private static final String RANGE = "Діапазон: ";
    private static final String NUMBER = "Номер:";
    private static final String VALUE = "Вихідна величина:";
    private static final String CERTIFICATE = "Сертифікат:";
    private static final String ERROR_FORMULA = "Формула розрахунку допустимої похибки:";
    private final String TITLE;

    private final Calibrator calibrator;

    private ButtonCell title;

    private ButtonCell measurementLabel, nameLabel, typeLabel, rangeLabel, numberLabel, valueLabel, certificateLabel, errorFormulaLabel;
    private ButtonCell measurement, name, type, range, number, value, certificate, errorFormula;

    public CalibratorInfoWindow(String title, JDialog parent, Calibrator calibrator){
        super(parent);
        this.TITLE = title;
        this.calibrator = calibrator;

        this.createElements();
        this.build();
    }

    private void createElements(){
        this.title = new ButtonCell(Color.YELLOW, Color.BLACK, this.TITLE);
        this.title.setHorizontalAlignment(SwingConstants.LEFT);

        this.measurementLabel = new ButtonCell(true, MEASUREMENT);
        this.measurementLabel.setHorizontalAlignment(SwingConstants.LEFT);
        this.measurementLabel.setToolTipText(this.calibrator.getMeasurement());
        this.measurement = new ButtonCell(false, this.calibrator.getMeasurement());
        this.measurement.setHorizontalAlignment(SwingConstants.LEFT);
        this.measurement.setToolTipText(this.calibrator.getMeasurement());

        this.nameLabel = new ButtonCell(true, NAME);
        this.nameLabel.setHorizontalAlignment(SwingConstants.LEFT);
        this.nameLabel.setToolTipText(this.calibrator.getName());
        this.name = new ButtonCell(false, this.calibrator.getName());
        this.name.setHorizontalAlignment(SwingConstants.LEFT);
        this.name.setToolTipText(this.calibrator.getName());

        this.typeLabel = new ButtonCell(true, TYPE);
        this.typeLabel.setHorizontalAlignment(SwingConstants.LEFT);
        this.typeLabel.setToolTipText(this.calibrator.getType());
        this.type = new ButtonCell(false, this.calibrator.getType());
        this.type.setHorizontalAlignment(SwingConstants.LEFT);
        this.type.setToolTipText(this.calibrator.getType());

        String r = " - ";
        if (this.calibrator.getRange() > 0) r = this.calibrator.getRangeMin() + " - " + this.calibrator.getRangeMax();
        this.rangeLabel = new ButtonCell(true, RANGE);
        this.rangeLabel.setHorizontalAlignment(SwingConstants.LEFT);
        this.rangeLabel.setToolTipText(r);
        this.range = new ButtonCell(false, r);
        this.range.setHorizontalAlignment(SwingConstants.LEFT);
        this.range.setToolTipText(r);

        String v = this.calibrator.getValue();
        if (v.length() == 0) v = " - ";
        this.valueLabel = new ButtonCell(true, VALUE);
        this.valueLabel.setHorizontalAlignment(SwingConstants.LEFT);
        this.valueLabel.setToolTipText(v);
        this.value = new ButtonCell(false, v);
        this.value.setHorizontalAlignment(SwingConstants.LEFT);
        this.value.setToolTipText(v);

        String n = this.calibrator.getNumber();
        if (n.length() == 0) n = " - ";
        this.numberLabel = new ButtonCell(true, NUMBER);
        this.numberLabel.setHorizontalAlignment(SwingConstants.LEFT);
        this.numberLabel.setToolTipText(n);
        this.number = new ButtonCell(false, n);
        this.number.setHorizontalAlignment(SwingConstants.LEFT);
        this.number.setToolTipText(n);

        this.certificateLabel = new ButtonCell(true, CERTIFICATE);
        this.certificateLabel.setHorizontalAlignment(SwingConstants.LEFT);
        this.certificateLabel.setToolTipText(this.calibrator.getCertificateToString());
        this.certificate = new ButtonCell(false, this.calibrator.getCertificateToString());
        this.certificate.setHorizontalAlignment(SwingConstants.LEFT);
        this.certificate.setToolTipText(this.calibrator.getCertificateToString());

        this.errorFormulaLabel = new ButtonCell(true, ERROR_FORMULA);
        this.errorFormulaLabel.setHorizontalAlignment(SwingConstants.LEFT);
        this.errorFormulaLabel.setToolTipText(this.calibrator.getErrorFormula());
        this.errorFormula = new ButtonCell(false, this.calibrator.getErrorFormula());
        this.errorFormula.setHorizontalAlignment(SwingConstants.LEFT);
        this.errorFormula.setToolTipText(this.calibrator.getErrorFormula());
    }

    private void build(){
        this.setSize(300,400);
        JScrollPane scrollPane = new JScrollPane(new MainPanel());
        this.setContentPane(scrollPane);
    }

    private class MainPanel extends JPanel {
        protected MainPanel(){
            super(new GridBagLayout());

            this.add(title, new Cell(0));
            this.add(measurementLabel, new Cell(1));
            this.add(measurement, new Cell(2));
            this.add(nameLabel, new Cell(3));
            this.add(name, new Cell(4));
            this.add(typeLabel, new Cell(5));
            this.add(type, new Cell(6));
            this.add(rangeLabel, new Cell(7));
            this.add(range, new Cell(8));
            this.add(valueLabel, new Cell(9));
            this.add(value, new Cell(10));
            this.add(numberLabel, new Cell(11));
            this.add(number, new Cell(12));
            this.add(certificateLabel, new Cell(13));
            this.add(certificate, new Cell(14));
            this.add(errorFormulaLabel, new Cell(15));
            this.add(errorFormula, new Cell(16));
        }

        private class Cell extends GridBagConstraints {
            protected Cell(int y){
                super();
                this.fill = BOTH;
                this.weightx = 1.0;

                this.gridy = y;
            }
        }
    }
}