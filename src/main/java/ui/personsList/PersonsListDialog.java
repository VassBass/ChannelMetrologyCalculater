package ui.personsList;

import service.repository.repos.person.PersonRepository;
import service.repository.repos.person.PersonRepositorySQLite;
import ui.mainScreen.MainScreen;
import ui.model.DefaultButton;
import ui.personsList.personInfo.PersonInfoDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static ui.UI_Constants.POINT_CENTER;

public class PersonsListDialog extends JDialog {
    private static final String WORKERS = "Робітники";
    private static final String ADD = "Додати";
    private static final String CHANGE = "Змінити";
    private static final String REMOVE = "Видалити";

    private final MainScreen mainScreen;

    private PersonsListTable mainTable;

    private JButton buttonAdd, buttonRemove, buttonChange;

    private final PersonRepository personRepository = PersonRepositorySQLite.getInstance();

    public PersonsListDialog(MainScreen mainScreen){
        super(mainScreen, WORKERS, true);
        this.mainScreen = mainScreen;

        this.createElements();
        this.setReactions();
        this.build();
    }

    private void createElements() {
        this.mainTable = new PersonsListTable(this);

        this.buttonAdd = new DefaultButton(ADD);
        this.buttonChange = new DefaultButton(CHANGE);
        this.buttonRemove = new DefaultButton(REMOVE);
    }

    private void setReactions() {
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        this.buttonAdd.addActionListener(this.clickAdd);
        this.buttonChange.addActionListener(this.clickChange);
        this.buttonRemove.addActionListener(this.clickRemove);
    }

    private void build() {
        this.setSize(800,600);
        this.setLocation(POINT_CENTER(this.mainScreen, this));
        this.setEnabledButtons(false);

        this.setContentPane(new MainPanel());
    }

    public void update(){
        this.mainTable = new PersonsListTable(this);
        this.setContentPane(new MainPanel());
        this.setEnabledButtons(false);
        this.setVisible(false);
        this.setVisible(true);
    }

    public void setEnabledButtons(boolean enabled){
        this.buttonChange.setEnabled(enabled);
        this.buttonRemove.setEnabled(enabled);
    }

    private final ActionListener clickAdd = e -> EventQueue.invokeLater(() -> new PersonInfoDialog(PersonsListDialog.this, null).setVisible(true));

    private final ActionListener clickChange = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            EventQueue.invokeLater(() -> {
                int index = mainTable.getSelectedRow();
                //new PersonInfoDialog(PersonsListDialog.this, personRepository.getById(index).get()).setVisible(true);
            });
        }
    };

    private final ActionListener clickRemove = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            EventQueue.invokeLater(() -> {
                int index = mainTable.getSelectedRow();
                //new RemovePersonDialog(PersonsListDialog.this, Application.context.personService.get(index)).setVisible(true);
            });
        }
    };

    private class MainPanel extends JPanel {
        protected MainPanel(){
            super(new GridBagLayout());

            this.add(new JScrollPane(mainTable), new Cell(0,0.9));
            JPanel buttonsPanel = new JPanel();
            buttonsPanel.add(buttonRemove);
            buttonsPanel.add(buttonChange);
            buttonsPanel.add(buttonAdd);
            this.add(buttonsPanel, new Cell(1, 0.1));
        }

        private class Cell extends GridBagConstraints {
            protected Cell(int y, double weighty){
                super();

                this.weightx = 1.0;
                this.weighty = weighty;
                this.gridx = 0;
                this.fill = BOTH;

                this.gridy = y;
            }
        }
    }
}