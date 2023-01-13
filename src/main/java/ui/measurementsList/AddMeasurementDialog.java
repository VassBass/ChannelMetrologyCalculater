package ui.measurementsList;

import backgroundTasks.AddMeasurement;
import converters.VariableConverter;
import model.Measurement;
import service.repository.repos.measurement.MeasurementRepositorySQLite;
import ui.model.ButtonCell;
import ui.model.DefaultButton;
import ui.specialCharacters.PanelSpecialCharacters;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;

import static ui.UI_Constants.POINT_CENTER;

public class AddMeasurementDialog extends JDialog {
    private static final String ADD = "Додати";
    private static final String CANCEL = "Відміна";

    private String valuesBorderTitle(String value){
        return value == null ? "1? = " : "1" + value + " = ";
    }

    private final MeasurementsListDialog parentDialog;
    private final String measurementName;

    private JTextField measurementValue;
    private JTextField[] factors;
    private JButton[] values;
    private JPanel valuesPanel;
    private JButton btn_positive, btn_negative;
    private PanelSpecialCharacters specialCharactersPanel;

    public AddMeasurementDialog(MeasurementsListDialog parentDialog){
        super(parentDialog, ADD, true);
        this.parentDialog = parentDialog;
        this.measurementName = parentDialog.getSelectedMeasurementName();

        createElements();
        build();
        setReactions();
    }

    private void createElements(){
        measurementValue = new JTextField(10);
        measurementValue.setHorizontalAlignment(SwingConstants.CENTER);

        String[] measurements = MeasurementRepositorySQLite.getInstance().getValues(measurementName);

        factors = new JTextField[measurements.length];

        values = new JButton[measurements.length];

        valuesPanel = new JPanel(new GridBagLayout());
        valuesPanel.setBorder(BorderFactory.createTitledBorder(valuesBorderTitle(null)));
        for (int index = 0;index<measurements.length;index++){
            String value = measurements[index];
            factors[index] = new JTextField("1.0",10);
            factors[index].setHorizontalAlignment(SwingConstants.CENTER);
            factors[index].addFocusListener(factorFocus);
            values[index] = new ButtonCell(false, value);
            valuesPanel.add(factors[index], new Cell(0,index));
            valuesPanel.add(values[index], new Cell(1,index));
        }

        btn_positive = new DefaultButton(ADD);
        btn_negative = new DefaultButton(CANCEL);

        specialCharactersPanel = new PanelSpecialCharacters();
        specialCharactersPanel.setFieldForInsert(measurementValue);
    }

    private void build(){
        this.setSize(650,300);
        this.setLocation(POINT_CENTER(parentDialog, this));

        JScrollPane scroll = new JScrollPane(valuesPanel);
        scroll.setPreferredSize(new Dimension(400,200));
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.add(btn_negative);
        buttonsPanel.add(btn_positive);

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));

        leftPanel.add(measurementValue);
        leftPanel.add(scroll);
        leftPanel.add(buttonsPanel);

        JPanel mainPanel = new JPanel();
        mainPanel.add(leftPanel);
        mainPanel.add(specialCharactersPanel);

        this.setContentPane(mainPanel);
    }

    private void setReactions(){
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        measurementValue.getDocument().addDocumentListener(changeValueName);
        btn_negative.addActionListener(clickNegative);
        btn_positive.addActionListener(clickPositive);
    }

    private final DocumentListener changeValueName = new DocumentListener() {
        @Override
        public void insertUpdate(DocumentEvent e) {
            String t = measurementValue.getText().length() > 0 ? valuesBorderTitle(measurementValue.getText()) : null;
            valuesPanel.setBorder(BorderFactory.createTitledBorder(t));
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            String t = measurementValue.getText().length() > 0 ? valuesBorderTitle(measurementValue.getText()) : null;
            valuesPanel.setBorder(BorderFactory.createTitledBorder(t));
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

    private final ActionListener clickNegative = e -> dispose();

    private final ActionListener clickPositive = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            String value = measurementValue.getText();
            if (value.length() == 0){
                JOptionPane.showMessageDialog(AddMeasurementDialog.this,
                        "Ви не ввели назву величини","Помилка", JOptionPane.ERROR_MESSAGE);
            }else if (MeasurementRepositorySQLite.getInstance().exists(value)){
                JOptionPane.showMessageDialog(AddMeasurementDialog.this,
                        "Така вимірювальна величина вже існує в списку величин цього або іншого виду вимірюваннь.",
                        "Помилка", JOptionPane.ERROR_MESSAGE);
            }else {
                Measurement measurement = new Measurement(measurementName, value);
                HashMap<String, Double>measurementFactors = new HashMap<>();
                for (int index = 0;index<values.length;index++){
                    JTextField factor = factors[index];
                    JButton val = values[index];
                    measurementFactors.put(val.getText(), Double.parseDouble(factor.getText()));
                }
                measurement.setFactors(measurementFactors);

                new AddMeasurement(AddMeasurementDialog.this, parentDialog, measurement).start();
            }
        }
    };

    private static class Cell extends GridBagConstraints {
        Cell(int x, int y){
            super();

            this.fill = BOTH;
            this.weightx = 1D;

            this.gridx = x;
            this.gridy = y;
        }
    }
}
