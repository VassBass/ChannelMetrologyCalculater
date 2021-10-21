package ui.calculate.performers;

import backgroundTasks.CertificateFormation;
import calculation.Calculation;
import constants.Value;
import constants.WorkPositions;
import converters.ConverterUI;
import support.Channel;
import support.Lists;
import constants.Strings;
import support.Values;
import ui.UI_Container;
import ui.calculate.reference.CalculateReferenceDialog;
import ui.calculate.verification.CalculateVerificationDialog;
import ui.main.MainScreen;
import support.Worker;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Objects;

@SuppressWarnings("unchecked")
public class CalculatePerformersDialog extends JDialog implements UI_Container {
    private final MainScreen mainScreen;
    private final Channel channel;
    private final Values values;
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

    public CalculatePerformersDialog(MainScreen mainScreen, Channel channel, Values values, Calculation calculation){
        super(mainScreen, Strings.WORKERS, true);
        this.mainScreen = mainScreen;
        this.channel = channel;
        this.values = values;
        this.calculation = calculation;

        this.createElements();
        this.setValues(values);
        this.setReactions();
        this.build();
    }

    @Override
    public void createElements() {
        this.performersLabel = new JLabel(Strings.PERFORMERS + ":");
        this.performersLabel.setForeground(Color.red.darker());

        this.calculaterLabel = new JLabel(Strings.CALCULATER +  ":");
        this.calculaterLabel.setForeground(Color.red.darker());

        this.headsLabel = new JLabel(Strings.HEADS + ":");
        this.headsLabel.setForeground(Color.red.darker());

        this.headOfMetrologyLabel = new JLabel(WorkPositions.HEAD_OF_AREA + " МЗтаП");
        this.headOfAreaPosition = new JLabel(WorkPositions.HEAD_OF_AREA + " АСУТП " + this.channel.getArea());
        this.headOfDepartmentLabel = new JLabel(WorkPositions.HEAD_OF_DEPARTMENT_ASUTP);

        this.performer1Name = new JComboBox<>(personsNames());
        this.performer1Name.setEditable(true);
        this.performer1Name.setBackground(Color.white);

        this.performer2Name = new JComboBox<>(personsNames());
        this.performer2Name.setEditable(true);
        this.performer2Name.setBackground(Color.white);

        this.calculaterName = new JComboBox<>(personsNames());
        this.calculaterName.setEditable(true);
        this.calculaterName.setBackground(Color.white);

        this.headOfMetrology = new JComboBox<>(personsNames());
        this.headOfMetrology.setEditable(true);
        this.headOfMetrology.setBackground(Color.white);

        this.headOfArea = new JComboBox<>(personsNames());
        this.headOfArea.setEditable(true);
        this.headOfArea.setBackground(Color.white);

        this.headOfDepartment = new JComboBox<>(headsOfDepartment());
        this.headOfDepartment.setEditable(true);
        this.headOfDepartment.setBackground(Color.white);

        this.performer1Position = new JTextField(Strings.EMPTY_ARRAY,10);
        this.performer2Position = new JTextField(Strings.EMPTY_ARRAY,10);
        this.calculaterPosition = new JTextField(Strings.EMPTY_ARRAY,10);

        this.buttonBack = new JButton(Strings.BACK);
        this.buttonBack.setBackground(Color.white);
        this.buttonBack.setFocusPainted(false);
        this.buttonBack.setContentAreaFilled(false);
        this.buttonBack.setOpaque(true);

        this.buttonSave = new JButton(Strings.SAVE);
        this.buttonSave.setBackground(Color.white);
        this.buttonSave.setFocusPainted(false);
        this.buttonSave.setContentAreaFilled(false);
        this.buttonSave.setOpaque(true);
    }

    @Override
    public void setReactions() {
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        this.performer1Name.addItemListener(selectPerformer1);
        this.performer2Name.addItemListener(selectPerformer2);
        this.calculaterName.addItemListener(selectCalculater);

        this.buttonBack.addChangeListener(pushButton);
        this.buttonSave.addChangeListener(pushButton);

        this.buttonBack.addActionListener(clickBack);
        this.buttonSave.addActionListener(clickSave);
    }

    @Override
    public void build() {
        this.setSize(600,350);
        this.setLocation(ConverterUI.POINT_CENTER(this.mainScreen, this));

        this.setContentPane(new MainPanel());
    }

    private String[]personsNames(){
        int length = Objects.requireNonNull(Lists.persons()).size() + 1;
        String[] persons = new String[length];
        persons[0] = Strings.EMPTY_ARRAY;
        for (int x = 0; x< Objects.requireNonNull(Lists.persons()).size(); x++){
            int y = x+1;
            persons[y] = Objects.requireNonNull(Lists.persons()).get(x).getFullName();
        }
        return persons;
    }

    private String[]headsOfDepartment(){
        ArrayList<Worker>persons = Lists.persons();
        ArrayList<String>heads = new ArrayList<>();
        heads.add(Strings.EMPTY_ARRAY);
        for (Worker worker : Objects.requireNonNull(persons)){
            if (worker.getPosition().equals(WorkPositions.HEAD_OF_DEPARTMENT_ASUTP)){
                heads.add(worker.getFullName());
            }
        }
        return heads.toArray(new String[0]);
    }

    private void setValues(Values values){
        if (values.getStringValue(Value.PERFORMER1_NAME) != null){
            this.performer1Name.setSelectedItem(values.getStringValue(Value.PERFORMER1_NAME));
        }
        if (values.getStringValue(Value.PERFORMER2_NAME) != null){
            this.performer2Name.setSelectedItem(values.getStringValue(Value.PERFORMER2_NAME));
        }
        if (values.getStringValue(Value.PERFORMER1_POSITION) != null){
            this.performer1Position.setText(values.getStringValue(Value.PERFORMER1_POSITION));
        }
        if (values.getStringValue(Value.PERFORMER2_POSITION) != null){
            this.performer2Position.setText(values.getStringValue(Value.PERFORMER2_POSITION));
        }
        if (values.getStringValue(Value.CALCULATER_NAME) != null){
            this.calculaterName.setSelectedItem(values.getStringValue(Value.CALCULATER_NAME));
        }
        if (values.getStringValue(Value.CALCULATER_POSITION) != null){
            this.calculaterPosition.setText(values.getStringValue(Value.CALCULATER_POSITION));
        }
        if (values.getStringValue(Value.HEAD_OF_METROLOGY_NAME) != null){
            this.headOfMetrology.setSelectedItem(values.getStringValue(Value.HEAD_OF_METROLOGY_NAME));
        }
        if (values.getStringValue(Value.HEAD_OF_AREA_NAME) != null){
            this.headOfArea.setSelectedItem(values.getStringValue(Value.HEAD_OF_AREA_NAME));
        }

        if (!this.calculation.goodChannel()){
            if (values.getStringValue(Value.HEAD_OF_DEPARTMENT) != null){
                this.headOfDepartment.setSelectedItem(values.getStringValue(Value.HEAD_OF_DEPARTMENT));
            }
        }
    }

    private Values getValues(){
        if (!Objects.requireNonNull(this.performer1Name.getSelectedItem()).toString().equals(Strings.EMPTY_ARRAY)
                && this.performer1Name.getSelectedItem().toString().length() > 0){
            this.values.putValue(Value.PERFORMER1_NAME, this.performer1Name.getSelectedItem().toString());
        }
        if (!Objects.requireNonNull(this.performer2Name.getSelectedItem()).toString().equals(Strings.EMPTY_ARRAY)
                && this.performer2Name.getSelectedItem().toString().length() != 0){
            this.values.putValue(Value.PERFORMER2_NAME, this.performer2Name.getSelectedItem().toString());
        }

        if (!this.performer1Position.getText().equals(Strings.EMPTY_ARRAY)
                && this.performer1Position.getText().length() > 0){
            this.values.putValue(Value.PERFORMER1_POSITION, this.performer1Position.getText());
        }
        if (!this.performer2Position.getText().equals(Strings.EMPTY_ARRAY)
                && this.performer2Position.getText().length() > 0){
            this.values.putValue(Value.PERFORMER2_POSITION, this.performer2Position.getText());
        }
        if (!Objects.requireNonNull(this.calculaterName.getSelectedItem()).toString().equals(Strings.EMPTY_ARRAY)
                && this.calculaterName.getSelectedItem().toString().length() > 0){
            this.values.putValue(Value.CALCULATER_NAME, this.calculaterName.getSelectedItem().toString());
        }
        if (!this.calculaterPosition.getText().equals(Strings.EMPTY_ARRAY)
                && this.calculaterPosition.getText().length() > 0){
            this.values.putValue(Value.CALCULATER_POSITION, this.calculaterPosition.getText());
        }
        if (!Objects.requireNonNull(this.headOfMetrology.getSelectedItem()).toString().equals(Strings.EMPTY_ARRAY)
                && this.headOfMetrology.getSelectedItem().toString().length() > 0){
            this.values.putValue(Value.HEAD_OF_METROLOGY_NAME, this.headOfMetrology.getSelectedItem().toString());
        }
        if (!Objects.requireNonNull(this.headOfArea.getSelectedItem()).toString().equals(Strings.EMPTY_ARRAY)
                && this.headOfArea.getSelectedItem().toString().length() > 0){
            this.values.putValue(Value.HEAD_OF_AREA_NAME, this.headOfArea.getSelectedItem().toString());
        }

        if (!this.calculation.goodChannel()){
            if (!Objects.requireNonNull(this.headOfDepartment.getSelectedItem()).toString().equals(Strings.EMPTY_ARRAY)
                    && this.headOfDepartment.getSelectedItem().toString().length() > 0){
                this.values.putValue(Value.HEAD_OF_DEPARTMENT, this.headOfDepartment.getSelectedItem().toString());
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
                performer1Position.setText(Strings.EMPTY_ARRAY);
            }else {
                try {
                    performer1Position.setText(Objects.requireNonNull(Lists.persons()).get(index - 1).getPosition());
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
                performer2Position.setText(Strings.EMPTY_ARRAY);
            }else {
                try {
                    performer2Position.setText(Objects.requireNonNull(Lists.persons()).get(index - 1).getPosition());
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
                calculaterPosition.setText(Strings.EMPTY_ARRAY);
            }else {
                try {
                    calculaterPosition.setText(Objects.requireNonNull(Lists.persons()).get(index - 1).getPosition());
                }catch (IndexOutOfBoundsException ignored){}
            }
        }
    };

    private final ChangeListener pushButton = new ChangeListener() {
        @Override
        public void stateChanged(ChangeEvent e) {
            JButton button = (JButton) e.getSource();
            if (button.getModel().isPressed()) {
                button.setBackground(Color.white.darker());
            }else {
                button.setBackground(Color.white);
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
