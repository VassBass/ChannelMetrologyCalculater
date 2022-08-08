package ui.personsList.personInfo;

import converters.ConverterUI;
import model.Person;
import service.impl.PersonServiceImpl;
import ui.model.DefaultButton;
import ui.personsList.PersonsListDialog;
import ui.personsList.personInfo.complexElements.PersonInfoPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PersonInfoDialog extends JDialog {
    private static final String ADD = "Додати";
    private static final String CHANGE = "Змінити";
    private static final String CANCEL = "Відміна";
    private static final String SAVE = "Зберегти";
    private static final String ERROR_MESSAGE = "Всі поля повинні бути заповнені";
    public static final String ERROR = "Помилка";

    private final PersonsListDialog parent;
    private final Person worker;

    private PersonInfoPanel infoPanel;

    private JButton positiveButton, negativeButton;

    public PersonInfoDialog(PersonsListDialog parent, Person worker){
        super(parent, title(worker), true);
        this.parent = parent;
        this.worker = worker;

        this.createElements();
        this.setReactions();
        this.build();
    }

    private void createElements() {
        this.infoPanel = new PersonInfoPanel(this.worker);

        this.negativeButton = new DefaultButton(CANCEL);
        this.positiveButton = new DefaultButton(SAVE);
    }

    private void setReactions() {
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        this.negativeButton.addActionListener(this.clickNegativeButton);
        this.positiveButton.addActionListener(this.clickPositiveButton);
    }

    private void build() {
        this.setSize(500,300);
        this.setLocation(ConverterUI.POINT_CENTER(this.parent, this));

        this.setContentPane(new MainPanel());
    }

    private static String title(Person worker){
        if (worker == null){
            return ADD;
        }else {
            return CHANGE + "\"" + worker._getFullName() + " - " + worker.getPosition() + "\"";
        }
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
            if (infoPanel.allTextsFull()){
                dispose();
                Person newPerson = infoPanel.getPerson();
                if (worker == null){
                    PersonServiceImpl.getInstance().add(newPerson);
                }else {
                    if (!worker.equals(newPerson)){
                        PersonServiceImpl.getInstance().set(worker, newPerson);
                    }
                }
                parent.update();
            }else {
                JOptionPane.showMessageDialog(PersonInfoDialog.this, ERROR_MESSAGE, ERROR, JOptionPane.ERROR_MESSAGE);
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