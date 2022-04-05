package ui.calculate.performers;

import application.Application;
import backgroundTasks.CertificateFormation;
import calculation.Calculation;
import constants.Key;
import converters.ConverterUI;
import model.Channel;
import model.Person;
import ui.calculate.reference.CalculateReferenceDialog;
import ui.calculate.verification.CalculateVerificationDialog;
import ui.mainScreen.MainScreen;
import ui.model.DefaultButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.HashMap;
import java.util.Objects;

@SuppressWarnings("unchecked")
public class CalculatePerformersDialog extends JDialog {
    private static final String WORKERS = "Робітники";
    private static final String PERFORMERS = "Виконавці:";
    private static final String CALCULATER = "Працівник який виконував розрахунки:";
    private static final String HEADS = "Керівники";
    private static final String EMPTY_ARRAY = "<Порожньо>";
    private static final String BACK = "Назад";
    private static final String SAVE = "Зберегти";

    private final MainScreen mainScreen;
    private final Channel channel;
    private final HashMap<Integer, Object> values;
    private final Calculation calculation;

    private JLabel performersLabel;
    private JLabel calculaterLabel;
    private JLabel headsLabel;
    private JLabel headOfMetrologyLabel;
    private JLabel headOfDepartmentLabel;

    private JTextField performer1Position, performer2Position;
    private JTextField calculaterPosition;
    private JLabel headOfAreaPosition;

    private JComboBox<String>performer1Name, performer2Name;
    private JComboBox<String>calculaterName;
    private JComboBox<String>headOfMetrology, headOfArea, headOfDepartment;

    private JButton buttonBack, buttonSave;

    public CalculatePerformersDialog(MainScreen mainScreen, Channel channel, HashMap<Integer, Object> values, Calculation calculation){
        super(mainScreen, WORKERS, true);
        this.mainScreen = mainScreen;
        this.channel = channel;
        this.values = values;
        this.calculation = calculation;

        this.createElements();
        this.setValues(values);
        this.setReactions();
        this.build();
    }

    private void createElements() {
        this.performersLabel = new JLabel(PERFORMERS);
        this.performersLabel.setForeground(Color.RED.darker());

        this.calculaterLabel = new JLabel(CALCULATER);
        this.calculaterLabel.setForeground(Color.RED.darker());

        this.headsLabel = new JLabel(HEADS);
        this.headsLabel.setForeground(Color.RED.darker());

        this.headOfMetrologyLabel = new JLabel(Person.HEAD_OF_AREA + " МЗтаП");
        this.headOfAreaPosition = new JLabel(Person.HEAD_OF_AREA + " АСУТП " + this.channel.getArea());
        this.headOfDepartmentLabel = new JLabel(Person.HEAD_OF_DEPARTMENT_ASUTP);

        this.performer1Name = new JComboBox<>(personsNames());
        this.performer1Name.setEditable(true);
        this.performer1Name.setBackground(Color.WHITE);

        this.performer2Name = new JComboBox<>(personsNames());
        this.performer2Name.setEditable(true);
        this.performer2Name.setBackground(Color.WHITE);

        this.calculaterName = new JComboBox<>(personsNames());
        this.calculaterName.setEditable(true);
        this.calculaterName.setBackground(Color.WHITE);

        this.headOfMetrology = new JComboBox<>(personsNames());
        this.headOfMetrology.setEditable(true);
        this.headOfMetrology.setBackground(Color.WHITE);

        this.headOfArea = new JComboBox<>(personsNames());
        this.headOfArea.setEditable(true);
        this.headOfArea.setBackground(Color.WHITE);

        this.headOfDepartment = new JComboBox<>(headsOfDepartment());
        this.headOfDepartment.setEditable(true);
        this.headOfDepartment.setBackground(Color.WHITE);

        this.performer1Position = new JTextField(EMPTY_ARRAY,10);
        this.performer2Position = new JTextField(EMPTY_ARRAY,10);
        this.calculaterPosition = new JTextField(EMPTY_ARRAY,10);

        this.buttonBack = new DefaultButton(BACK);
        this.buttonSave = new DefaultButton(SAVE);
    }

    private void setReactions() {
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        this.performer1Name.addItemListener(this.selectPerformer1);
        this.performer2Name.addItemListener(this.selectPerformer2);
        this.calculaterName.addItemListener(this.selectCalculater);

        this.buttonBack.addActionListener(this.clickBack);
        this.buttonSave.addActionListener(this.clickSave);
    }

    private void build() {
        this.setSize(600,350);
        this.setLocation(ConverterUI.POINT_CENTER(this.mainScreen, this));

        this.setContentPane(new MainPanel());
    }

    private String[]personsNames(){
        return Application.context.personService.getAllNamesWithFirstEmptyString();
    }

    private String[]headsOfDepartment(){
        return Application.context.personService.getNamesOfHeadsWithFirstEmptyString();
    }

    private void setValues(HashMap<Integer, Object> values){
        if (values.get(Key.PERFORMER1_NAME) != null){
            this.performer1Name.setSelectedItem(values.get(Key.PERFORMER1_NAME));
        }
        if (values.get(Key.PERFORMER2_NAME) != null){
            this.performer2Name.setSelectedItem(values.get(Key.PERFORMER2_NAME));
        }
        if (values.get(Key.PERFORMER1_POSITION) != null){
            this.performer1Position.setText((String) values.get(Key.PERFORMER1_POSITION));
        }
        if (values.get(Key.PERFORMER2_POSITION) != null){
            this.performer2Position.setText((String) values.get(Key.PERFORMER2_POSITION));
        }
        if (values.get(Key.CALCULATER_NAME) != null){
            this.calculaterName.setSelectedItem(values.get(Key.CALCULATER_NAME));
        }
        if (values.get(Key.CALCULATER_POSITION) != null){
            this.calculaterPosition.setText((String) values.get(Key.CALCULATER_POSITION));
        }
        if (values.get(Key.HEAD_OF_METROLOGY_NAME) != null){
            this.headOfMetrology.setSelectedItem(values.get(Key.HEAD_OF_METROLOGY_NAME));
        }
        if (values.get(Key.HEAD_OF_AREA_NAME) != null){
            this.headOfArea.setSelectedItem(values.get(Key.HEAD_OF_AREA_NAME));
        }

        if (!this.calculation.goodChannel()){
            if (values.get(Key.HEAD_OF_DEPARTMENT) != null){
                this.headOfDepartment.setSelectedItem(values.get(Key.HEAD_OF_DEPARTMENT));
            }
        }
    }

    private HashMap<Integer, Object> getValues(){
        if (!Objects.requireNonNull(this.performer1Name.getSelectedItem()).toString().equals(EMPTY_ARRAY)
                && this.performer1Name.getSelectedItem().toString().length() > 0){
            this.values.put(Key.PERFORMER1_NAME, this.performer1Name.getSelectedItem().toString());
        }
        if (!Objects.requireNonNull(this.performer2Name.getSelectedItem()).toString().equals(EMPTY_ARRAY)
                && this.performer2Name.getSelectedItem().toString().length() != 0){
            this.values.put(Key.PERFORMER2_NAME, this.performer2Name.getSelectedItem().toString());
        }

        if (!this.performer1Position.getText().equals(EMPTY_ARRAY)
                && this.performer1Position.getText().length() > 0){
            this.values.put(Key.PERFORMER1_POSITION, this.performer1Position.getText());
        }
        if (!this.performer2Position.getText().equals(EMPTY_ARRAY)
                && this.performer2Position.getText().length() > 0){
            this.values.put(Key.PERFORMER2_POSITION, this.performer2Position.getText());
        }
        if (!Objects.requireNonNull(this.calculaterName.getSelectedItem()).toString().equals(EMPTY_ARRAY)
                && this.calculaterName.getSelectedItem().toString().length() > 0){
            this.values.put(Key.CALCULATER_NAME, this.calculaterName.getSelectedItem().toString());
        }
        if (!this.calculaterPosition.getText().equals(EMPTY_ARRAY)
                && this.calculaterPosition.getText().length() > 0){
            this.values.put(Key.CALCULATER_POSITION, this.calculaterPosition.getText());
        }
        if (!Objects.requireNonNull(this.headOfMetrology.getSelectedItem()).toString().equals(EMPTY_ARRAY)
                && this.headOfMetrology.getSelectedItem().toString().length() > 0){
            this.values.put(Key.HEAD_OF_METROLOGY_NAME, this.headOfMetrology.getSelectedItem().toString());
        }
        if (!Objects.requireNonNull(this.headOfArea.getSelectedItem()).toString().equals(EMPTY_ARRAY)
                && this.headOfArea.getSelectedItem().toString().length() > 0){
            this.values.put(Key.HEAD_OF_AREA_NAME, this.headOfArea.getSelectedItem().toString());
        }

        if (!this.calculation.goodChannel()){
            if (!Objects.requireNonNull(this.headOfDepartment.getSelectedItem()).toString().equals(EMPTY_ARRAY)
                    && this.headOfDepartment.getSelectedItem().toString().length() > 0){
                this.values.put(Key.HEAD_OF_DEPARTMENT, this.headOfDepartment.getSelectedItem().toString());
            }
        }

        return this.values;
    }

    private final ItemListener selectPerformer1 = new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent e) {
            JComboBox<String>comboBox = (JComboBox<String>) e.getSource();
            int index = comboBox.getSelectedIndex();
            if (index == 0){
                performer1Position.setText(EMPTY_ARRAY);
            }else {
                try {
                    String position = Application.context.personService.get(index - 1).getPosition();
                    performer1Position.setText(position);
                }catch (IndexOutOfBoundsException ignored){}
            }
        }
    };

    private final ItemListener selectPerformer2 = new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent e) {
            JComboBox<String>comboBox = (JComboBox<String>) e.getSource();
            int index = comboBox.getSelectedIndex();
            if (index == 0){
                performer2Position.setText(EMPTY_ARRAY);
            }else {
                try {
                    String position = Application.context.personService.get(index - 1).getPosition();
                    performer2Position.setText(position);
                }catch (IndexOutOfBoundsException ignored){}
            }
        }
    };

    private final ItemListener selectCalculater = new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent e) {
            JComboBox<String>comboBox = (JComboBox<String>) e.getSource();
            int index = comboBox.getSelectedIndex();
            if (index == 0){
                calculaterPosition.setText(EMPTY_ARRAY);
            }else {
                try {
                    String position = Application.context.personService.get(index - 1).getPosition();
                    calculaterPosition.setText(position);
                }catch (IndexOutOfBoundsException ignored){}
            }
        }
    };

    private final ActionListener clickBack = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    if (calculation.goodChannel()){
                        dispose();
                        new CalculateVerificationDialog(mainScreen, channel, getValues(), calculation).setVisible(true);
                    }else {
                        dispose();
                        new CalculateReferenceDialog(mainScreen, channel, getValues(), calculation).setVisible(true);
                    }
                }
            });
        }
    };

    private final ActionListener clickSave = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (Application.isBusy(CalculatePerformersDialog.this)) return;
            dispose();
            new CertificateFormation(mainScreen, channel, getValues(), calculation).execute();
        }
    };
    private class MainPanel extends JPanel{

        protected MainPanel(){
            super(new GridBagLayout());

            this.add(performersLabel, new Cell(0,0, true));
            this.add(performer1Name, new Cell(0,1, false));
            this.add(performer1Position, new Cell(1,1, false));
            this.add(performer2Name, new Cell(0,2, false));
            this.add(performer2Position, new Cell(1,2, false));

            this.add(calculaterLabel, new Cell(0,3, true));
            this.add(calculaterName, new Cell(0,4, false));
            this.add(calculaterPosition, new Cell(1,4, false));

            this.add(headsLabel, new Cell(0,5, true));
            this.add(headOfMetrologyLabel, new Cell(0,6, false));
            this.add(headOfMetrology, new Cell(1,6, false));
            this.add(headOfAreaPosition, new Cell(0,7, false));
            this.add(headOfArea, new Cell(1,7, false));

            JPanel buttonsPanel = new JPanel();
            buttonsPanel.add(buttonBack);
            buttonsPanel.add(buttonSave);

            if (calculation.goodChannel()){
                this.add(buttonsPanel, new Cell(1,8,true));
            }else {
                this.add(headOfDepartmentLabel, new Cell(0,8, false));
                this.add(headOfDepartment, new Cell(1,8, false));
                this.add(buttonsPanel, new Cell(1,9, true));
            }
        }

        private class Cell extends GridBagConstraints{

            protected Cell(int x, int y, boolean withInsets){
                super();

                this.fill = BOTH;
                if (withInsets) {
                    this.insets = new Insets(10, 0, 10, 0);
                }

                this.gridx = x;
                this.gridy = y;
            }
        }
    }
}