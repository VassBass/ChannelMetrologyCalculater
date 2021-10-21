package ui.personsList.personInfo.removePerson;

import backgroundTasks.RemovePerson;
import constants.Strings;
import converters.ConverterUI;
import ui.UI_Container;
import ui.personsList.PersonsListDialog;
import workers.Worker;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RemovePersonDialog extends JDialog implements UI_Container {
    private final PersonsListDialog parent;
    private final Worker worker;

    private JLabel message;

    private JButton positiveButton, negativeButton;

    private final Color buttonsColor = new Color(51,51,51);

    public RemovePersonDialog(PersonsListDialog parent, Worker worker){
        super(parent, Strings.REMOVE, true);
        this.parent = parent;
        this.worker = worker;

        this.createElements();
        this.setReactions();
        this.build();
    }

    @Override
    public void createElements() {
        String s = Strings.REMOVE
                + " зі списку працівника:\n"
                + worker.getSurname()
                + " "
                + worker.getName()
                + " "
                + worker.getPatronymic()
                + " ("
                + worker.getPosition()
                + ")?";

        this.message = new JLabel(s);

        this.positiveButton = new JButton(Strings.REMOVE);
        this.positiveButton.setBackground(this.buttonsColor);
        this.positiveButton.setForeground(Color.white);
        this.positiveButton.setFocusPainted(false);
        this.positiveButton.setContentAreaFilled(false);
        this.positiveButton.setOpaque(true);

        this.negativeButton = new JButton(Strings.CANCEL);
        this.negativeButton.setBackground(this.buttonsColor);
        this.negativeButton.setForeground(Color.white);
        this.negativeButton.setFocusPainted(false);
        this.negativeButton.setContentAreaFilled(false);
        this.negativeButton.setOpaque(true);
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
        this.setSize(700,150);
        this.setLocation(ConverterUI.POINT_CENTER(this.parent, this));

        this.setContentPane(new MainPanel());
    }

    private final ChangeListener pushButton = new ChangeListener() {
        @Override
        public void stateChanged(ChangeEvent e) {
            JButton button = (JButton) e.getSource();
            if (button.getModel().isPressed()) {
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
            dispose();
            new RemovePerson(parent, worker).execute();
        }
    };

    private class MainPanel extends JPanel{
        protected MainPanel(){
            super(new GridBagLayout());

            this.add(message, new Cell(0,0,2));
            this.add(negativeButton, new Cell(0,1,1));
            this.add(positiveButton, new Cell(1,1,1));
        }

        private class Cell extends GridBagConstraints{
            protected Cell(int x, int y, int width){
                super();

                this.fill = BOTH;
                this.insets = new Insets(10,10,10,10);
                this.weightx = 1.0;

                this.gridx = x;
                this.gridy = y;
                this.gridwidth = width;
            }
        }
    }
}
