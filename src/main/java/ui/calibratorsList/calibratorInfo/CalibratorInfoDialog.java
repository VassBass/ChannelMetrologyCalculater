package ui.calibratorsList.calibratorInfo;

import backgroundTasks.PutCalibratorInList;
import constants.Strings;
import converters.ConverterUI;
import converters.VariableConverter;
import measurements.Measurement;
import org.mariuszgromada.math.mxparser.Argument;
import org.mariuszgromada.math.mxparser.Expression;
import org.mariuszgromada.math.mxparser.Function;
import support.Calibrator;
import support.Lists;
import ui.ButtonCell;
import ui.UI_Container;
import ui.calibratorsList.CalibratorsListDialog;
import ui.calibratorsList.calibratorInfo.complexElements.CalibratorRangePanel;
import ui.calibratorsList.calibratorInfo.complexElements.CertificateDatePanel;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Objects;

public class CalibratorInfoDialog extends JDialog implements UI_Container {
    private final CalibratorsListDialog parent;
    private final CalibratorInfoDialog current;
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

    private final Color buttonsColor = new Color(51,51,51);

    public CalibratorInfoDialog(CalibratorsListDialog parent, Calibrator oldCalibrator){
        super(parent, Strings.CALIBRATOR, true);
        this.parent = parent;
        this.current = this;
        this.oldCalibrator = oldCalibrator;

        this.createElements();
        this.setInfo();
        this.setReactions();
        this.build();
    }

    @Override
    public void createElements() {
        this.labelCalibrator = new ButtonCell(true, Strings.CALIBRATOR);
        this.labelMeasurement = new ButtonCell(true, Strings.TYPE_OF_MEASUREMENT);
        this.labelType = new ButtonCell(true, Strings.TYPE);
        this.labelName = new ButtonCell(true, Strings._NAME);
        this.labelNumber = new ButtonCell(true, Strings.PARENT_NUMBER);
        this.labelRange = new ButtonCell(true, Strings.RANGE_OF_CALIBRATOR);
        this.labelErrorFormula = new ButtonCell(true, Strings.ERROR_FORMULA);

        this.labelCertificate = new ButtonCell(true, Strings.CERTIFICATE_OF_CALIBRATION);
        this.labelCertificateName = new ButtonCell(true, Strings._NAME);
        this.labelCertificateCompany = new ButtonCell(true, Strings.PERFORMERS);
        this.labelCertificateDate = new ButtonCell(true, Strings.FROM);

        ArrayList<String> measurementsNames = new ArrayList<>();
        ArrayList<Measurement>measurements = Lists.measurements();
        for (Measurement measurement : Objects.requireNonNull(measurements)) {
            boolean exist = false;
            for (String measurementsName : measurementsNames) {
                if (measurementsName.equals(measurement.getName())) {
                    exist = true;
                }
            }
            if (!exist) {
                measurementsNames.add(measurement.getName());
            }
        }
        this.measurementsList = new JComboBox<>(measurementsNames.toArray(new String[0]));

        String typeHint = "Тип калібратора(Застосовується у протоколі)";
        this.typeText = new JTextField(10);
        this.typeText.setToolTipText(typeHint);

        String nameHint = "Назва калібратора для застосування в данній програмі(Не фігурує в документах)";
        this.nameText = new JTextField(10);
        this.nameText.setToolTipText(nameHint);
        this.namePopupMenu = new JPopupMenu("Вставка");
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

        this.buttonCancel = new JButton(Strings.CANCEL);
        this.buttonCancel.setBackground(buttonsColor);
        this.buttonCancel.setForeground(Color.white);
        this.buttonCancel.setFocusPainted(false);
        this.buttonCancel.setContentAreaFilled(false);
        this.buttonCancel.setOpaque(true);

        this.buttonSave = new JButton(Strings.SAVE);
        this.buttonSave.setBackground(buttonsColor);
        this.buttonSave.setForeground(Color.white);
        this.buttonSave.setFocusPainted(false);
        this.buttonSave.setContentAreaFilled(false);
        this.buttonSave.setOpaque(true);
    }

    private void setInfo(){
        if (this.oldCalibrator != null){
            this.measurementsList.setSelectedItem(this.oldCalibrator.getMeasurement());
            this.rangePanel.setValues(Objects.requireNonNull(this.measurementsList.getSelectedItem()).toString(),
                    this.oldCalibrator.getValue());
            this.typeText.setText(this.oldCalibrator.getType());
            this.nameText.setText(this.oldCalibrator.getName());
            this.rangePanel.setRange(this.oldCalibrator.getRangeMax(), this.oldCalibrator.getRangeMin());
            if (this.oldCalibrator.getName().equals(Strings.CALIBRATOR_FLUKE718_30G)){
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
        }else {
            this.rangePanel.setValues(Objects.requireNonNull(this.measurementsList.getSelectedItem()).toString(),null);
        }
    }

    @Override
    public void setReactions() {
        this.buttonCancel.addChangeListener(pushButton);
        this.buttonSave.addChangeListener(pushButton);

        this.buttonCancel.addActionListener(clickCancel);
        this.buttonSave.addActionListener(clickSave);

        this.measurementsList.addItemListener(changeMeasurement);

        this.typeText.getDocument().addDocumentListener(typeChange);
    }

    @Override
    public void build() {
        this.setSize(1050,550);
        this.setLocation(ConverterUI.POINT_CENTER(this.parent, this));

        this.setContentPane(new MainPanel());
    }

    private final ChangeListener pushButton = new ChangeListener() {
        @Override
        public void stateChanged(ChangeEvent e) {
            JButton button = (JButton) e.getSource();
            if (button.getModel().isPressed()){
                button.setBackground(buttonsColor.darker());
            }else {
                button.setBackground(buttonsColor);
            }
        }
    };

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
                Calibrator calibrator = new Calibrator();
                calibrator.setType(typeText.getText());
                calibrator.setName(nameText.getText());
                calibrator.setNumber(numberText.getText());
                calibrator.setMeasurement(Objects.requireNonNull(measurementsList.getSelectedItem()).toString());
                calibrator.setRangeMin(rangePanel.getRangeMin());
                calibrator.setRangeMax(rangePanel.getRangeMax());
                calibrator.setValue(rangePanel.getValue());
                calibrator.setErrorFormula(errorFormulaText.getText());
                calibrator.setCertificateName(certificateNameText.getText());
                calibrator.setCertificateDate(certificateDatePanel.getDate());
                calibrator.setCertificateCompany(certificateCompanyText.getText());
                new PutCalibratorInList(parent, current, calibrator, oldCalibrator).execute();
            }
        }
    };

    private final ItemListener changeMeasurement = new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED){
                rangePanel.setValues(Objects.requireNonNull(measurementsList.getSelectedItem()).toString(), null);
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
        if (this.typeText.getText().length() == 0){
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
            this.add(labelType, new Cell(0, 2,1));
            this.add(typeText, new Cell(1, 2,1));
            this.add(labelCertificateCompany, new Cell(2,2,1));
            this.add(certificateCompanyText, new Cell(3,2,1));
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

            this.add(buttonCancel, new Cell(1,7,1));
            this.add(buttonSave, new Cell(2,7,1));

            this.add(helpFormula1, new Cell(0,8,4));
            this.add(helpFormula2, new Cell(0,9,4));
            this.add(helpFormula3, new Cell(0,10,4));
            this.add(helpFormula4, new Cell(0,11,4));
            this.add(helpFormula5, new Cell(0,12,4));
            this.add(helpFormula6, new Cell(0,13,4));
            this.add(helpFormula7, new Cell(0,14,4));
            this.add(helpFormula8, new Cell(0,15,4));
            this.add(helpFormula9, new Cell(0,16,4));
            this.add(helpFormula10, new Cell(0,17,4));
            this.add(helpFormula11, new Cell(0,18,4));
            this.add(helpFormula12, new Cell(0,19,4));
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