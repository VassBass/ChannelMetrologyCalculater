package ui.calibratorsList.calibratorInfo;

import application.Application;
import constants.CalibratorType;
import constants.MeasurementConstants;
import converters.ConverterUI;
import converters.VariableConverter;
import org.mariuszgromada.math.mxparser.Argument;
import org.mariuszgromada.math.mxparser.Expression;
import org.mariuszgromada.math.mxparser.Function;
import model.Calibrator;
import ui.model.ButtonCell;
import ui.calibratorsList.CalibratorsListDialog;
import ui.calibratorsList.calibratorInfo.complexElements.CalibratorRangePanel;
import ui.calibratorsList.calibratorInfo.complexElements.CertificateDatePanel;
import ui.model.DefaultButton;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Objects;

public class CalibratorInfoDialog extends JDialog {
    private static final String CALIBRATOR = "Калібратор";
    private static final String TYPE_OF_MEASUREMENT = "Вид вимірювання";
    private static final String TYPE = "Тип";
    private static final String NAME = "Назва";
    private static final String PARENT_NUMBER = "Заводський № ";
    private static final String RANGE_OF_CALIBRATOR = "Діапазон калібратора";
    private static final String ERROR_FORMULA = "Формула для розрахунку похибки";
    private static final String CERTIFICATE_OF_CALIBRATION = "Сертифікат калібрування";
    private static final String PERFORMERS = "Виконавці";
    private static final String FROM = "від";
    private static final String TYPE_HINT = "Тип калібратора(Застосовується у протоколі)";
    private static final String NAME_HINT = "Назва калібратора для застосування в данній програмі(Не фігурує в документах)";
    private static final String INSERT = "Вставка";
    private static final String CANCEL = "Відміна";
    private static final String SAVE = "Зберегти";

    private final CalibratorsListDialog parent;
    private final JDialog current;
    private final Calibrator oldCalibrator;

    private ButtonCell labelCalibrator;
    private ButtonCell labelMeasurement;
    private ButtonCell labelType;
    private ButtonCell labelName;
    private ButtonCell labelNumber;
    private ButtonCell labelRange;
    private ButtonCell labelErrorFormula;

    private ButtonCell labelCertificate;
    private ButtonCell labelCertificateName;
    private ButtonCell labelCertificateDate;
    private ButtonCell labelCertificateCompany;

    private ButtonCell helpFormula1, helpFormula2, helpFormula3, helpFormula4, helpFormula5,
            helpFormula6, helpFormula7, helpFormula8, helpFormula9, helpFormula10, helpFormula11, helpFormula12;

    private JComboBox<String>measurementsList;
    private JTextField typeText;
    private JTextField nameText;
    private JTextField numberText;
    private CalibratorRangePanel rangePanel;
    private JTextField errorFormulaText;

    private JTextField certificateNameText;
    private CertificateDatePanel certificateDatePanel;
    private JTextField certificateCompanyText;

    private JPopupMenu namePopupMenu;

    private JButton buttonCancel, buttonSave;

    public CalibratorInfoDialog(CalibratorsListDialog parent, Calibrator oldCalibrator){
        super(parent, CALIBRATOR, true);
        this.current = this;
        this.parent = parent;
        this.oldCalibrator = oldCalibrator;

        this.createElements();
        this.setInfo();
        this.setReactions();
        this.build();
    }

    private void createElements() {
        this.labelCalibrator = new ButtonCell(true, CALIBRATOR);
        this.labelMeasurement = new ButtonCell(true, TYPE_OF_MEASUREMENT);
        this.labelType = new ButtonCell(true, TYPE);
        this.labelName = new ButtonCell(true, NAME);
        this.labelNumber = new ButtonCell(true, PARENT_NUMBER);
        this.labelRange = new ButtonCell(true, RANGE_OF_CALIBRATOR);
        this.labelErrorFormula = new ButtonCell(true, ERROR_FORMULA);

        this.labelCertificate = new ButtonCell(true, CERTIFICATE_OF_CALIBRATION);
        this.labelCertificateName = new ButtonCell(true, NAME);
        this.labelCertificateCompany = new ButtonCell(true, PERFORMERS);
        this.labelCertificateDate = new ButtonCell(true, FROM);

        this.measurementsList = new JComboBox<>(Application.context.measurementsController.getAllNames());

        this.typeText = new JTextField(10);
        this.typeText.setToolTipText(TYPE_HINT);

        this.nameText = new JTextField(10);
        this.nameText.setToolTipText(NAME_HINT);
        this.namePopupMenu = new JPopupMenu(INSERT);
        this.nameText.setComponentPopupMenu(this.namePopupMenu);

        this.numberText = new JTextField(10);
        this.rangePanel = new CalibratorRangePanel();
        this.errorFormulaText = new JTextField(10);
        this.certificateNameText = new JTextField(10);
        this.certificateDatePanel = new CertificateDatePanel();
        this.certificateCompanyText = new JTextField(10);

        String toolTipText = "Приклад існує лише для ознайомлення з формою запису і не є реальною формулою.";

        String help1 = "Щоб написати формулу користуйтеся:";
        this.helpFormula1 = new ButtonCell(false, help1);
        this.helpFormula1.setVerticalAlignment(SwingConstants.CENTER);
        this.helpFormula1.setHorizontalAlignment(SwingConstants.LEFT);
        this.helpFormula1.setBorderPainted(false);

        String help2 = "0...9, 0.1, 0,1 - Натуральні та дробні числа";
        this.helpFormula2 = new ButtonCell(false, help2);
        this.helpFormula2.setVerticalAlignment(SwingConstants.CENTER);
        this.helpFormula2.setHorizontalAlignment(SwingConstants.LEFT);
        this.helpFormula2.setBorderPainted(false);

        String help3 = "() - Дужки, для розстановки послідовності дій";
        this.helpFormula3 = new ButtonCell(false, help3);
        this.helpFormula3.setVerticalAlignment(SwingConstants.CENTER);
        this.helpFormula3.setHorizontalAlignment(SwingConstants.LEFT);
        this.helpFormula3.setBorderPainted(false);

        String help4 = "+, -, *, / - сума, різниця, множення, ділення";
        this.helpFormula4 = new ButtonCell(false, help4);
        this.helpFormula4.setVerticalAlignment(SwingConstants.CENTER);
        this.helpFormula4.setHorizontalAlignment(SwingConstants.LEFT);
        this.helpFormula4.setBorderPainted(false);

        String help5 = "R - Діапазон вимірювання вимірювального каналу";
        this.helpFormula5 = new ButtonCell(false, help5);
        this.helpFormula5.setVerticalAlignment(SwingConstants.CENTER);
        this.helpFormula5.setHorizontalAlignment(SwingConstants.LEFT);
        this.helpFormula5.setBorderPainted(false);

        String help6 = "convR - Діапазон калібратора переконвертований під вимірювальну величину вимірювального каналу";
        this.helpFormula6 = new ButtonCell(false, help6);
        this.helpFormula6.setVerticalAlignment(SwingConstants.CENTER);
        this.helpFormula6.setHorizontalAlignment(SwingConstants.LEFT);
        this.helpFormula6.setBorderPainted(false);

        String help7 = "r - Діапазон калібратора";
        this.helpFormula7 = new ButtonCell(false, help7);
        this.helpFormula7.setVerticalAlignment(SwingConstants.CENTER);
        this.helpFormula7.setHorizontalAlignment(SwingConstants.LEFT);
        this.helpFormula7.setBorderPainted(false);

        String help8 = "Приклад: ((0.005 * R) / r) + convR";
        this.helpFormula8 = new ButtonCell(false, help8);
        this.helpFormula8.setVerticalAlignment(SwingConstants.CENTER);
        this.helpFormula8.setHorizontalAlignment(SwingConstants.CENTER);
        this.helpFormula8.setToolTipText(toolTipText);

        String help9 = "Дія №1 - 0.005 помножено на діапазон вимірювання вимірювального каналу(R)";
        this.helpFormula9 = new ButtonCell(false, help9);
        this.helpFormula9.setVerticalAlignment(SwingConstants.CENTER);
        this.helpFormula9.setHorizontalAlignment(SwingConstants.LEFT);
        this.helpFormula9.setBorderPainted(false);
        this.helpFormula9.setToolTipText(toolTipText);

        String help10 = "Дія №2 - Результат першої дії поділено на діапазон калібратора(r)";
        this.helpFormula10 = new ButtonCell(false, help10);
        this.helpFormula10.setVerticalAlignment(SwingConstants.CENTER);
        this.helpFormula10.setHorizontalAlignment(SwingConstants.LEFT);
        this.helpFormula10.setBorderPainted(false);
        this.helpFormula10.setToolTipText(toolTipText);

        String help11 = "Дія №3 - До результату другої дії додати діапазон калібратора переконвертований під вимірювальну";
        this.helpFormula11 = new ButtonCell(false, help11);
        this.helpFormula11.setVerticalAlignment(SwingConstants.CENTER);
        this.helpFormula11.setHorizontalAlignment(SwingConstants.LEFT);
        this.helpFormula11.setBorderPainted(false);
        this.helpFormula11.setToolTipText(toolTipText);

        String help12 = "величину вимірювального каналу(convR)";
        this.helpFormula12 = new ButtonCell(false, help12);
        this.helpFormula12.setVerticalAlignment(SwingConstants.CENTER);
        this.helpFormula12.setHorizontalAlignment(SwingConstants.LEFT);
        this.helpFormula12.setBorderPainted(false);
        this.helpFormula12.setToolTipText(toolTipText);

        this.buttonCancel = new DefaultButton(CANCEL);
        this.buttonSave = new DefaultButton(SAVE);
    }

    private void setInfo(){
        String measurement = Objects.requireNonNull(this.measurementsList.getSelectedItem()).toString();
        if (this.oldCalibrator != null){
            this.measurementsList.setSelectedItem(this.oldCalibrator.getMeasurement());
            this.rangePanel.setValues(measurement, this.oldCalibrator.getValue());
            this.typeText.setText(this.oldCalibrator.getType());
            this.nameText.setText(this.oldCalibrator.getName());
            this.rangePanel.setRange(this.oldCalibrator.getRangeMax(), this.oldCalibrator.getRangeMin());
            if (this.oldCalibrator.getName().equals(CalibratorType.FLUKE718_30G)
            || this.oldCalibrator.getName().equals(CalibratorType.ROSEMOUNT_8714DQ4)){
                this.typeText.setEnabled(false);
                this.nameText.setEnabled(false);
                this.measurementsList.setEnabled(false);
                this.rangePanel.setEnabled(false);
            }
            this.numberText.setText(this.oldCalibrator.getNumber());
            this.errorFormulaText.setText(this.oldCalibrator.getErrorFormula());
            this.certificateNameText.setText(this.oldCalibrator.getCertificateName());
            this.certificateDatePanel.setDate(this.oldCalibrator.getCertificateDate());
            this.certificateCompanyText.setText(this.oldCalibrator.getCertificateCompany());

            this.measurementsList.setEnabled(false);
        }else {
            this.rangePanel.setValues(measurement,null);
        }
    }

    private void setReactions() {
        this.buttonCancel.addActionListener(this.clickCancel);
        this.buttonSave.addActionListener(this.clickSave);

        this.measurementsList.addItemListener(this.changeMeasurement);

        this.typeText.getDocument().addDocumentListener(this.typeChange);
    }

    private void build() {
        this.setSize(1050,550);
        this.setLocation(ConverterUI.POINT_CENTER(this.parent, this));

        this.setContentPane(new MainPanel());
    }

    private final ActionListener clickCancel = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            dispose();
        }
    };

    private final ActionListener clickSave = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (checkCalibrator()) {
                String measurement = Objects.requireNonNull(measurementsList.getSelectedItem()).toString();
                Calibrator calibrator = new Calibrator();
                calibrator.setType(typeText.getText());
                calibrator.setName(nameText.getText());
                calibrator.setNumber(numberText.getText());
                calibrator.setMeasurement(measurement);
                if (measurement.equals(MeasurementConstants.PRESSURE.getValue())) {
                    calibrator.setRangeMin(rangePanel.getRangeMin());
                    calibrator.setRangeMax(rangePanel.getRangeMax());
                }
                calibrator.setValue(rangePanel.getValue());
                calibrator.setErrorFormula(errorFormulaText.getText());
                calibrator.setCertificateName(certificateNameText.getText());
                calibrator.setCertificateDate(certificateDatePanel.getDate());
                calibrator.setCertificateCompany(certificateCompanyText.getText());

                if (Application.isBusy(current)) return;
                if (oldCalibrator == null){
                    Application.context.calibratorsController.add(calibrator);
                }else {
                    Application.context.calibratorsController.set(oldCalibrator, calibrator);
                }
                dispose();
                parent.mainTable.update();
            }
        }
    };

    private final ItemListener changeMeasurement = new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED){
                String measurement = Objects.requireNonNull(measurementsList.getSelectedItem()).toString();
                rangePanel.setValues(measurement, null);
            }
        }
    };

    private final DocumentListener typeChange = new DocumentListener() {
        @Override
        public void insertUpdate(DocumentEvent e) {
            namePopupMenu.removeAll();
            JMenuItem type = new JMenuItem(typeText.getText());
            type.addActionListener(clickPaste);
            namePopupMenu.add(type);
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            namePopupMenu.removeAll();
            JMenuItem type = new JMenuItem(typeText.getText());
            type.addActionListener(clickPaste);
            namePopupMenu.add(type);
        }

        @Override
        public void changedUpdate(DocumentEvent e) {}
    };

    private final ActionListener clickPaste = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            nameText.setText(typeText.getText());
            nameText.requestFocus();
        }
    };

    private boolean checkCalibrator(){
        if (this.typeText.getText().length() == 0 &&
                !Objects.requireNonNull(measurementsList.getSelectedItem()).toString().equals(MeasurementConstants.CONSUMPTION.getValue())){
            JOptionPane.showMessageDialog(this, "Ви не ввели тип калібратора");
            return false;
        }else if (this.nameText.getText().length() == 0){
            JOptionPane.showMessageDialog(this, "Ви не ввели назву калібратора");
            return false;
        }else if (this.errorFormulaText.getText().length() == 0) {
            JOptionPane.showMessageDialog(this, "Ви не ввели похибку калібратора");
            return false;
        }else if (!checkFormula(this.errorFormulaText.getText())){
            JOptionPane.showMessageDialog(this, "Для запису формули використовуйте тільки дозволені символи.");
            return false;
        }else {
            return true;
        }
    }

    private boolean checkFormula(String formula){
        String fo = VariableConverter.commasToDots(formula);
        Function f = new Function("At(R,r,convR) = " + fo);
        Argument R = new Argument("R = 1");
        Argument r = new Argument("r = 1");
        Argument convR = new Argument("convR = 1");
        Expression expression = new Expression("At(R,r,convR)", f,R,r,convR);
        return !Double.isNaN(expression.calculate());
    }

    private class MainPanel extends JPanel {
        protected MainPanel(){
            super(new GridBagLayout());

            this.add(labelCalibrator, new Cell(0,0,2));
            this.add(labelCertificate, new Cell(2,0,2));
            this.add(labelMeasurement, new Cell(0, 1,1));
            this.add(measurementsList, new Cell(1, 1,1));
            this.add(labelCertificateName, new Cell(2,1,1));
            this.add(certificateNameText, new Cell(3,1,1));
            this.add(labelType, new Cell(0, 2, 1));
            this.add(typeText, new Cell(1, 2, 1));
            this.add(labelCertificateCompany, new Cell(2, 2, 1));
            this.add(certificateCompanyText, new Cell(3, 2, 1));
            this.add(labelName, new Cell(0, 3,1));
            this.add(nameText, new Cell(1, 3,1));
            this.add(labelCertificateDate, new Cell(2,3,1));
            this.add(certificateDatePanel, new Cell(3,3,1));
            this.add(labelNumber, new Cell(0,4,1));
            this.add(numberText, new Cell(1,4,1));
            this.add(labelRange, new Cell(0, 5,1));
            this.add(rangePanel, new Cell(1, 5,1));
            this.add(labelErrorFormula, new Cell(0, 6,1));
            this.add(errorFormulaText, new Cell(1, 6,1));

            this.add(helpFormula1, new Cell(0,7,4));
            this.add(helpFormula2, new Cell(0,8,4));
            this.add(helpFormula3, new Cell(0,9,4));
            this.add(helpFormula4, new Cell(0,10,4));
            this.add(helpFormula5, new Cell(0,11,4));
            this.add(helpFormula6, new Cell(0,12,4));
            this.add(helpFormula7, new Cell(0,13,4));
            this.add(helpFormula8, new Cell(0,14,4));
            this.add(helpFormula9, new Cell(0,15,4));
            this.add(helpFormula10, new Cell(0,16,4));
            this.add(helpFormula11, new Cell(0,17,4));
            this.add(helpFormula12, new Cell(0,18,4));

            this.add(buttonCancel, new Cell(2,19,1));
            this.add(buttonSave, new Cell(3,19,1));
        }

        private class Cell extends GridBagConstraints {
            protected Cell(int x, int y, int width){
                super();

                this.weightx = 1D;
                this.weighty = 1D;
                this.fill = BOTH;

                this.gridx = x;
                this.gridy = y;
                this.gridwidth = width;
            }
        }
    }
}