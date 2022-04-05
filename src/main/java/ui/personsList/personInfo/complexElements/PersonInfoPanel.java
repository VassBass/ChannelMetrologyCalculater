package ui.personsList.personInfo.complexElements;

import model.Person;

import javax.swing.*;
import java.awt.*;

public class PersonInfoPanel extends JPanel {
    private static final String NAME = "Ім'я";
    private static final String SURNAME = "Прізвище";
    private static final String PATRONYMIC = "Побатькові";
    private static final String POSITION = "Посада";

    private JLabel nameLabel;
    private JLabel surnameLabel;
    private JLabel patronymicLabel;
    private JLabel positionLabel;

    private JTextField name;
    private JTextField surname;
    private JTextField patronymic;
    private JTextField position;

    public PersonInfoPanel(Person worker){
        super(new GridBagLayout());

        this.createElements();
        this.setInfo(worker);
        this.build();
    }

    private void createElements() {
        this.nameLabel = new JLabel(NAME);
        this.surnameLabel = new JLabel(SURNAME);
        this.patronymicLabel = new JLabel(PATRONYMIC);
        this.positionLabel = new JLabel(POSITION);

        this.name = new JTextField(10);
        this.surname = new JTextField(10);
        this.patronymic = new JTextField(10);
        this.position = new JTextField(10);
    }

    private void build() {
        this.add(this.nameLabel, new Cell(0, 0, true));
        this.add(this.name, new Cell(1, 0, false));

        this.add(this.surnameLabel, new Cell(0, 1, true));
        this.add(this.surname, new Cell(1, 1, false));

        this.add(this.patronymicLabel, new Cell(0, 2, true));
        this.add(this.patronymic, new Cell(1, 2, false));

        this.add(this.positionLabel, new Cell(0, 3, true));
        this.add(this.position, new Cell(1, 3, false));
    }

    private void setInfo(Person worker){
        if (worker!=null){
            this.name.setText(worker.getName());
            this.surname.setText(worker.getSurname());
            this.patronymic.setText(worker.getPatronymic());
            this.position.setText(worker.getPosition());
        }
    }

    public Person getPerson(){
        Person worker = new Person();
        worker.setName(this.name.getText());
        worker.setSurname(this.surname.getText());
        worker.setPatronymic(this.patronymic.getText());
        worker.setPosition(this.position.getText());
        return worker;
    }

    public boolean allTextsFull(){
        if (this.name.getText().length() == 0){
            this.nameLabel.setForeground(Color.RED);
            return false;
        }else if (this.surname.getText().length() == 0){
            this.surnameLabel.setForeground(Color.RED);
            return false;
        }else if (this.patronymic.getText().length() == 0){
            this.patronymicLabel.setForeground(Color.RED);
            return false;
        }else if (this.position.getText().length() == 0){
            this.positionLabel.setForeground(Color.RED);
            return false;
        }else {
            return true;
        }
    }

    private static class Cell extends GridBagConstraints{
        protected Cell(int x, int y, boolean label){
            super();

            this.fill = BOTH;
            this.weightx = 1.0;

            if (label){
                this.insets = new Insets(10,10,10,0);
            }else {
                this.insets = new Insets(10,0,10,10);
            }

            this.gridx = x;
            this.gridy = y;
        }
    }
}