package ui.measurementsList;

import backgroundTasks.ChangeMeasurementValue;
import converters.ConverterUI;
import model.Measurement;
import service.impl.MeasurementServiceImpl;
import ui.model.DefaultButton;
import ui.specialCharacters.SpecialCharactersPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChangeMeasurementDialog extends JDialog {
    private static final String TITLE = "Змінити назву величини";
    private static final String CHANGE = "Змінити";
    private static final String CLOSE = "Закрити";
    private static final String ERROR = "Помилка";
    private static final String EXIST_MESSAGE = "Така величина вже є у списку";

    private final MeasurementsListDialog parentDialog;
    private final String oldValue;

    private JTextField txt_value;
    private JButton btn_positive, btn_negative;
    private SpecialCharactersPanel specialCharactersPanel;

    public ChangeMeasurementDialog(MeasurementsListDialog parentDialog){
        super(parentDialog, TITLE, true);
        this.parentDialog = parentDialog;
        oldValue = parentDialog.getSelectedMeasurementValue();

        createElements();
        build();
        setReactions();
    }

    private void createElements(){
        String toolTipText = "Стара назва: " + oldValue;
        txt_value = new JTextField(oldValue, 10);
        txt_value.setToolTipText(toolTipText);
        txt_value.selectAll();
        txt_value.setHorizontalAlignment(SwingConstants.CENTER);

        btn_negative = new DefaultButton(CLOSE);
        btn_positive = new DefaultButton(CHANGE);

        specialCharactersPanel = new SpecialCharactersPanel();
        specialCharactersPanel.setFieldForInsert(txt_value);
    }

    private void build(){
        this.setSize(450,200);
        this.setLocation(ConverterUI.POINT_CENTER(parentDialog, this));

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.add(btn_negative);
        buttonsPanel.add(btn_positive);

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.add(txt_value);
        leftPanel.add(buttonsPanel);

        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(Color.WHITE);
        mainPanel.add(leftPanel);
        mainPanel.add(specialCharactersPanel);

        this.setContentPane(mainPanel);
    }

    private void setReactions(){
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        btn_negative.addActionListener(clickNegative);
        btn_positive.addActionListener(clickPositive);
    }

    private final ActionListener clickNegative = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            dispose();
        }
    };

    private final ActionListener clickPositive = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (MeasurementServiceImpl.getInstance().exists(oldValue, txt_value.getText())){
                JOptionPane.showMessageDialog(ChangeMeasurementDialog.this,
                        EXIST_MESSAGE, ERROR, JOptionPane.ERROR_MESSAGE);
            }else {
                Measurement oldMeasurement = MeasurementServiceImpl.getInstance().get(oldValue);
                Measurement newMeasurement = oldMeasurement.copy();
                newMeasurement.setValue(txt_value.getText());
                new ChangeMeasurementValue(ChangeMeasurementDialog.this, parentDialog, oldMeasurement, newMeasurement).start();
            }
        }
    };
}
