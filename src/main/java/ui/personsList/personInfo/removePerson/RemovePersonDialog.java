package ui.personsList.personInfo.removePerson;

import application.Application;
import converters.ConverterUI;
import model.Person;
import ui.model.DefaultButton;
import ui.personsList.PersonsListDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RemovePersonDialog extends JDialog {
    public static final String REMOVE = "Видалити";
    public static final String CANCEL = "Відміна";

    private final PersonsListDialog parent;
    private final Person person;

    private JLabel message;

    private JButton positiveButton, negativeButton;

    public RemovePersonDialog(PersonsListDialog parent, Person person){
        super(parent, REMOVE, true);
        this.parent = parent;
        this.person = person;

        this.createElements();
        this.setReactions();
        this.build();
    }

    private void createElements() {
        String s = REMOVE
                + " зі списку працівника:\n"
                + this.person.getSurname()
                + " "
                + this.person.getName()
                + " "
                + this.person.getPatronymic()
                + " ("
                + this.person.getPosition()
                + ")?";

        this.message = new JLabel(s);

        this.positiveButton = new DefaultButton(REMOVE);
        this.negativeButton = new DefaultButton(CANCEL);
    }

    private void setReactions() {
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        this.negativeButton.addActionListener(this.clickNegativeButton);
        this.positiveButton.addActionListener(this.clickPositiveButton);
    }

    private void build() {
        this.setSize(700,150);
        this.setLocation(ConverterUI.POINT_CENTER(this.parent, this));

        this.setContentPane(new MainPanel());
    }

    private final ActionListener clickNegativeButton = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            dispose();
        }
    };

    private final ActionListener clickPositiveButton = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (Application.isBusy(RemovePersonDialog.this)) return;
            dispose();
            Application.context.personService.remove(person);
            parent.update();
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