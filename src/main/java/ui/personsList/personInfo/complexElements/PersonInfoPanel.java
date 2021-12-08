package ui.personsList.personInfo.complexElements;

import constants.Strings;
import ui.UI_Container;
import model.Worker;

import javax.swing.*;
import java.awt.*;

public class PersonInfoPanel extends JPanel implements UI_Container {

    private JLabel nameLabel;
    private JLabel surnameLabel;
    private JLabel patronymicLabel;
    private JLabel positionLabel;

    private JTextField name;
    private JTextField surname;
    private JTextField patronymic;
    private JTextField position;

    public PersonInfoPanel(Worker worker){
        super(new GridBagLayout());

        this.createElements();
        this.setInfo(worker);
        this.setReactions();
        this.build();
    }

    @Override
    public void createElements() {
        this.nameLabel = new JLabel(Strings.NAME);
        this.surnameLabel = new JLabel(Strings.SURNAME);
        this.patronymicLabel = new JLabel(Strings.PATRONYMIC);
        this.positionLabel = new JLabel(Strings.POSITION);

        this.name = new JTextField(10);
        this.surname = new JTextField(10);
        this.patronymic = new JTextField(10);
        this.position = new JTextField(10);
    }

    @Override
    public void setReactions() {

    }

    @Override
    public void build() {
        this.add(this.nameLabel, new Cell(0, 0, true));
        this.add(this.name, new Cell(1, 0, false));

        this.add(this.surnameLabel, new Cell(0, 1, true));
        this.add(this.surname, new Cell(1, 1, false));

        this.add(this.patronymicLabel, new Cell(0, 2, true));
        this.add(this.patronymic, new Cell(1, 2, false));

        this.add(this.positionLabel, new Cell(0, 3, true));
        this.add(this.position, new Cell(1, 3, false));
    }

    private void setInfo(Worker worker){
        if (worker!=null){
            this.name.setText(worker.getName());
            this.surname.setText(worker.getSurname());
            this.patronymic.setText(worker.getPatronymic());
            this.position.setText(worker.getPosition());
        }
    }

    public Worker getWorker(){
        Worker worker = new Worker();
        worker.setName(this.name.getText());
        worker.setSurname(this.surname.getText());
        worker.setPatronymic(this.patronymic.getText());
        worker.setPosition(this.position.getText());
        return worker;
    }

    public boolean allTextsFull(){
        if (this.name.getText().length() == 0){
            this.nameLabel.setForeground(Color.red);
            return false;
        }else if (this.surname.getText().length() == 0){
            this.surnameLabel.setForeground(Color.red);
            return false;
        }else if (this.patronymic.getText().length() == 0){
            this.patronymicLabel.setForeground(Color.red);
            return false;
        }else if (this.position.getText().length() == 0){
            this.positionLabel.setForeground(Color.red);
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
