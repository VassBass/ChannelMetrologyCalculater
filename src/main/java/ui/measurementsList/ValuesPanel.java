package ui.measurementsList;

import converters.VariableConverter;
import model.Measurement;
import service.impl.MeasurementServiceImpl;
import ui.model.ButtonCell;
import ui.model.DefaultButton;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;

public class ValuesPanel extends JPanel{
    private static final String SAVE_CHANGES = "Зберегти зміни";
    private static final String CLEAR = "Скинути";
    private static final String VALUES = "Величини";

    private final MeasurementsListDialog parentDialog;
    private String currentMeasurementValue;

    private String title(String measurementValue){
        return "1" + measurementValue + " = ";
    }

    private JButton btn_saveValues, btn_clear;
    private JButton[] valuesLabels;
    private JTextField[] valuesTexts;
    private JPanel valuesPanel;
    private JScrollPane scroll;
    private HashMap<String, Double>currentFactors;

    public ValuesPanel(MeasurementsListDialog parentDialog){
        super(new GridBagLayout());
        this.parentDialog = parentDialog;

        createElements();
        build();
        setReactions();
    }

    private void createElements(){
        btn_saveValues = new DefaultButton(SAVE_CHANGES);
        btn_clear = new DefaultButton(CLEAR);
        btn_saveValues.setEnabled(false);
        btn_clear.setEnabled(false);
        valuesPanel = new JPanel();
        valuesPanel.setLayout(new GridBagLayout());
    }

    private void build(){
        this.setBorder(BorderFactory.createTitledBorder(VALUES));

        scroll = new JScrollPane(valuesPanel);
        scroll.setPreferredSize(new Dimension(300,400));
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        this.add(scroll, new Cell(0, 0.95));

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.add(btn_saveValues);
        buttonsPanel.add(btn_clear);
        this.add(buttonsPanel, new Cell(1, 0.05));
    }

    private void setReactions(){
        btn_clear.addActionListener(clickClear);
        btn_saveValues.addActionListener(clickSave);
    }

    public void updateValues(String measurementValue){
        valuesPanel.removeAll();
        this.currentMeasurementValue = measurementValue == null ? VALUES : title(measurementValue);
        this.setBorder(BorderFactory.createTitledBorder(this.currentMeasurementValue));

        if (measurementValue != null) {
            Measurement measurement = MeasurementServiceImpl.getInstance().get(measurementValue).get();

            currentFactors = measurement.getFactors();
            String[] values = currentFactors.keySet().toArray(new String[0]);
            valuesLabels = new JButton[values.length];
            valuesTexts = new JTextField[values.length];

            for (int index = 0; index < values.length; index++) {
                String value = values[index];
                valuesLabels[index] = new ButtonCell(false, value);
                valuesTexts[index] = new JTextField(10);
                valuesTexts[index].setText(String.valueOf(currentFactors.get(value)));
                valuesTexts[index].getDocument().addDocumentListener(documentListener);
                valuesTexts[index].addFocusListener(factorFocus);
                valuesPanel.add(valuesTexts[index], new Cell(0, index));
                valuesPanel.add(valuesLabels[index], new Cell(1, index));
            }
        }
        btn_saveValues.setEnabled(false);
        btn_clear.setEnabled(false);
        scroll.revalidate();
    }

    private final DocumentListener documentListener = new DocumentListener() {
        @Override
        public void insertUpdate(DocumentEvent e) {
            btn_saveValues.setEnabled(true);
            btn_clear.setEnabled(true);
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            btn_saveValues.setEnabled(true);
            btn_clear.setEnabled(true);
        }

        @Override public void changedUpdate(DocumentEvent e) {}
    };

    private final FocusListener factorFocus = new FocusAdapter() {
        @Override
        public void focusLost(FocusEvent e) {
            JTextField source = (JTextField) e.getSource();
            source.setText(VariableConverter.doubleString(source.getText()));
        }
    };

    private HashMap<String, Double>getFactors(){
        HashMap<String, Double>result = new HashMap<>();
        for (int index = 0;index < valuesTexts.length;index++){
            JTextField field = valuesTexts[index];
            double d = Double.parseDouble(field.getText());
            result.put(valuesLabels[index].getText(), d);
        }
        return result;
    }

    private final ActionListener clickClear = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            for (int index = 0;index < valuesTexts.length;index++){
                valuesTexts[index].setText(String.valueOf(currentFactors.get(valuesLabels[index].getText())));
            }
            btn_clear.setEnabled(false);
            btn_saveValues.setEnabled(false);
        }
    };

    private final ActionListener clickSave = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            HashMap<String, Double> factors = getFactors();
            MeasurementServiceImpl.getInstance().changeFactors(currentMeasurementValue, factors);
            btn_clear.setEnabled(false);
            btn_saveValues.setEnabled(false);
            JOptionPane.showMessageDialog(parentDialog, "Збережено","Успіх",JOptionPane.INFORMATION_MESSAGE);
        }
    };

    private static class Cell extends GridBagConstraints {
        Cell(int y, double weight){
            super();

            this.fill = BOTH;
            this.gridx = 0;

            this.weighty = weight;
            this.gridy = y;
        }

        Cell(int x, int y){
            super();

            this.fill = BOTH;
            this.weightx = 1D;

            this.gridx = x;
            this.gridy = y;
        }
    }
}
