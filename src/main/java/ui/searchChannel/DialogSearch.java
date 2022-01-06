package ui.searchChannel;

import backgroundTasks.controllers.SearchChannels;
import constants.ChannelConstants;
import constants.Strings;
import converters.ConverterUI;
import ui.UI_Container;
import ui.main.MainScreen;
import ui.searchChannel.complexElements.DialogSearch_searchPanel;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class DialogSearch extends JDialog implements UI_Container {
    private final MainScreen mainScreen;

    private JLabel label;
    private JComboBox<String>searchField;
    private JButton positiveButton, negativeButton;

    private DialogSearch_searchPanel searchPanel;

    public DialogSearch(MainScreen mainScreen){
        super(mainScreen, Strings.SEARCH, true);
        this.mainScreen = mainScreen;

        this.createElements();
        this.setReactions();
        this.build();
    }

    @Override
    public void createElements() {
        this.label = new JLabel(Strings.SEARCH_FIELD + ": ");

        this.searchField = new JComboBox<>(searchFields());
        this.searchField.setBackground(Color.white);

        //this.searchPanel = new DialogSearch_searchPanel(this);

        this.positiveButton = new JButton(Strings.SEARCH);
        this.positiveButton.setBackground(Color.white);
        this.positiveButton.setFocusPainted(false);
        this.positiveButton.setContentAreaFilled(false);
        this.positiveButton.setOpaque(true);

        this.negativeButton = new JButton(Strings.CANCEL);
        this.negativeButton.setBackground(Color.white);
        this.negativeButton.setFocusPainted(false);
        this.negativeButton.setContentAreaFilled(false);
        this.negativeButton.setOpaque(true);
    }

    @Override
    public void setReactions() {
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        this.searchField.addItemListener(this.selectField);

        this.positiveButton.addChangeListener(pushButton);
        this.negativeButton.addChangeListener(pushButton);

        this.positiveButton.addActionListener(clickPositiveButton);
        this.negativeButton.addActionListener(clickNegativeButton);
    }

    @Override
    public void build() {
        this.setSize(700, 120);
        this.setLocation(ConverterUI.POINT_CENTER(this.mainScreen, this));

        this.setContentPane(new MainPanel());
    }

    private static String[]searchFields(){
        String[]toComboBox = new String[13];

        toComboBox[0] = Strings.CODE;
        toComboBox[1] = Strings._NAME;
        toComboBox[2] = Strings.TYPE_OF_MEASUREMENT;
        toComboBox[3] = Strings.DEPARTMENT;
        toComboBox[4] = Strings.AREA;
        toComboBox[5] = Strings.PROCESS;
        toComboBox[6] = Strings.INSTALLATION;
        toComboBox[7] = Strings.FULL_PATH;
        toComboBox[8] = Strings.THIS_DATE;
        toComboBox[9] = Strings.NEXT_DATE;
        toComboBox[10] = Strings.TECHNOLOGY_NUMBER;
        toComboBox[11] = Strings.SENSOR;
        toComboBox[12] = Strings.PROTOCOL_NUMBER;

        return toComboBox;
    }

    private final ItemListener selectField = new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                //searchPanel.update(ChannelConstants.getConstantFromInt(searchField.getSelectedIndex()));
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

    private final ActionListener clickPositiveButton = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            ChannelConstants field = ChannelConstants.getConstantFromInt(searchField.getSelectedIndex());
            //new SearchChannels(mainScreen, field, searchPanel.getValue()).execute();
            dispose();
        }
    };

    private final ActionListener clickNegativeButton = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            dispose();
        }
    };

    private class MainPanel extends JPanel {

        protected MainPanel() {
            super(new GridBagLayout());

            this.add(label, new Cell(0,0));
            this.add(searchField, new Cell(1,0));
            this.add(searchPanel, new Cell(2,0));
            JPanel buttonsPanel = new JPanel();
            buttonsPanel.add(positiveButton);
            buttonsPanel.add(negativeButton);
            this.add(buttonsPanel, new Cell(1,1));
        }

        private class Cell extends GridBagConstraints {

            private static final long serialVersionUID = 1L;

            protected Cell(int x, int y) {
                super();

                this.fill = HORIZONTAL;

                this.gridx = x;
                this.gridy = y;
            }
        }
    }
}
