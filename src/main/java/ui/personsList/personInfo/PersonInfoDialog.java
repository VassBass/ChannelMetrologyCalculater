package ui.personsList.personInfo;

import backgroundTasks.PutPersonInList;
import constants.Strings;
import converters.ConverterUI;
import ui.UI_Container;
import ui.personsList.PersonsListDialog;
import ui.personsList.personInfo.complexElements.PersonInfoPanel;
import workers.Worker;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PersonInfoDialog extends JDialog implements UI_Container {
    private final PersonsListDialog parent;
    private final Worker worker;
    private final JDialog current;

    private PersonInfoPanel infoPanel;

    private JButton positiveButton, negativeButton;

    private final Color buttonsColor = new Color(51,51,51);

    public PersonInfoDialog(PersonsListDialog parent, Worker worker){
        super(parent, title(worker), true);
        this.parent = parent;
        this.worker = worker;
        this.current = this;

        this.createElements();
        this.setReactions();
        this.build();
    }

    @Override
    public void createElements() {
        this.infoPanel = new PersonInfoPanel(this.worker);

        this.negativeButton = new JButton(Strings.CANCEL);
        this.negativeButton.setBackground(this.buttonsColor);
        this.negativeButton.setForeground(Color.white);
        this.negativeButton.setFocusPainted(false);
        this.negativeButton.setContentAreaFilled(false);
        this.negativeButton.setOpaque(true);

        this.positiveButton = new JButton(Strings.SAVE);
        this.positiveButton.setBackground(this.buttonsColor);
        this.positiveButton.setForeground(Color.white);
        this.positiveButton.setFocusPainted(false);
        this.positiveButton.setContentAreaFilled(false);
        this.positiveButton.setOpaque(true);
    }

    @Override
    public void setReactions() {
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        this.positiveButton.addChangeListener(this.pushButton);
        this.negativeButton.addChangeListener(this.pushButton);

        this.negativeButton.addActionListener(this.clickNegativeButton);
        this.positiveButton.addActionListener(this.clickPositiveButton);
    }

    @Override
    public void build() {
        this.setSize(500,300);
        this.setLocation(ConverterUI.POINT_CENTER(this.parent, this));

        this.setContentPane(new MainPanel());
    }

    private static String title(Worker worker){
        if (worker == null){
            return Strings.ADD;
        }else {
            return Strings.CHANGE
                    + "\""
                    + worker.getFullName()
                    + " - "
                    + worker.getPosition()
                    + "\"";
        }
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

    private final ActionListener clickNegativeButton = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            dispose();
        }
    };

    private final ActionListener clickPositiveButton = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (infoPanel.allTextsFull()){
                dispose();
                new PutPersonInList(parent, infoPanel.getWorker(), worker).execute();
            }else {
                JOptionPane.showMessageDialog(current, "Всі поля повинні бути заповнені", Strings.ERROR, JOptionPane.ERROR_MESSAGE);
            }
        }
    };

    private class MainPanel extends JPanel {
        protected MainPanel(){
            super(new GridBagLayout());

            this.add(infoPanel, new Cell(0,0,2,true));
            this.add(negativeButton, new Cell(0,1,1,false));
            this.add(positiveButton, new Cell(1,1,1,false));
        }

        private class Cell extends GridBagConstraints{
            protected Cell(int x, int y, int width, boolean panel){
                super();

                this.fill = BOTH;
                this.insets = new Insets(5,5,5,5);
                this.weightx = 1.0;
                if (panel){
                    this.weighty = 0.9;
                }else {
                    this.weighty = 0.1;
                }

                this.gridx = x;
                this.gridy = y;
                this.gridwidth = width;
            }
        }
    }
}
