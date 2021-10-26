package ui.importData.comparePersons.complexElements;

import constants.Strings;
import support.Worker;
import ui.ButtonCell;
import ui.UI_Container;

import javax.swing.*;
import java.awt.*;

public class ComparePersonsInfoPanel extends JPanel implements UI_Container {
    /*
     * [0] = Название поля для сравнения;
     * [1] = Данные о работнике из списка;
     * [2] = Импортируемые данные о работнике;
     * [3] = Имя;
     * [4] = Фамилия;
     * [5] = Отчество;
     * [6] = Должность;
     */
    private JButton[] labels = null;

    /*
     * [0] = Имя;
     * [1] = Фамилия;
     * [2] = Отчество;
     * [3] = Должность;
     */
    private JButton[] oldInfo = null;
    private JButton[] newInfo = null;

    private final Worker old;
    private final Worker imported;

    public ComparePersonsInfoPanel(Worker old, Worker imported){
        super(new GridBagLayout());

        this.old = old;
        this.imported = imported;

        this.createElements();
        this.setReactions();
        this.build();
    }

    @Override
    public void createElements() {
        labels = new ButtonCell[7];
        oldInfo = new ButtonCell[4];
        newInfo = new ButtonCell[4];

        labels[0] = new ButtonCell(Color.white, Color.black, Strings.FIELD);
        labels[1] = new ButtonCell(Color.white, Color.black, Strings.WORKER_IN_LIST);
        labels[2] = new ButtonCell(Color.white, Color.black, Strings.IMPORTED_WORKER);

        if (old.getName().equals(imported.getName())){
            labels[3] = new ButtonCell(Color.green.darker(), Color.white, Strings.NAME);
            oldInfo[0] = new ButtonCell(Color.green.darker(), Color.white, old.getName());
            newInfo[0] = new ButtonCell(Color.green.darker(), Color.white, imported.getName());
        }else {
            labels[3] = new ButtonCell(Color.red.darker(), Color.white, Strings.NAME);
            oldInfo[0] = new ButtonCell(Color.red.darker(), Color.white, old.getName());
            newInfo[0] = new ButtonCell(Color.red.darker(), Color.white, imported.getName());
        }

        if (old.getSurname().equals(imported.getSurname())){
            labels[4] = new ButtonCell(Color.green.darker(), Color.white, Strings.SURNAME);
            oldInfo[1] = new ButtonCell(Color.green.darker(), Color.white, old.getSurname());
            newInfo[1] = new ButtonCell(Color.green.darker(), Color.white, imported.getSurname());
        }else {
            labels[4] = new ButtonCell(Color.red.darker(), Color.white, Strings.SURNAME);
            oldInfo[1] = new ButtonCell(Color.red.darker(), Color.white, old.getSurname());
            newInfo[1] = new ButtonCell(Color.red.darker(), Color.white, imported.getSurname());
        }

        if (old.getPatronymic().equals(imported.getPatronymic())){
            labels[5] = new ButtonCell(Color.green.darker(), Color.white, Strings.PATRONYMIC);
            oldInfo[2] = new ButtonCell(Color.green.darker(), Color.white, old.getPatronymic());
            newInfo[2] = new ButtonCell(Color.green.darker(), Color.white, imported.getPatronymic());
        }else {
            labels[5] = new ButtonCell(Color.red.darker(), Color.white, Strings.PATRONYMIC);
            oldInfo[2] = new ButtonCell(Color.red.darker(), Color.white, old.getPatronymic());
            newInfo[2] = new ButtonCell(Color.red.darker(), Color.white, imported.getPatronymic());
        }

        if (old.getPosition().equals(imported.getPosition())){
            labels[6] = new ButtonCell(Color.green.darker(), Color.white, Strings.POSITION);
            oldInfo[3] = new ButtonCell(Color.green.darker(), Color.white, old.getPosition());
            newInfo[3] = new ButtonCell(Color.green.darker(), Color.white, imported.getPosition());
        }else {
            labels[6] = new ButtonCell(Color.red.darker(), Color.white, Strings.POSITION);
            oldInfo[3] = new ButtonCell(Color.red.darker(), Color.white, old.getPosition());
            newInfo[3] = new ButtonCell(Color.red.darker(), Color.white, imported.getPosition());
        }
    }

    @Override public void setReactions() {}

    @Override
    public void build() {
        this.add(this.labels[0], new Cell(0, 0));
        this.add(this.labels[1], new Cell(1, 0));
        this.add(this.labels[2], new Cell(2, 0));

        this.add(this.labels[3], new Cell(0, 1));
        this.add(this.oldInfo[0], new Cell(1, 1));
        this.add(this.newInfo[0], new Cell(2, 1));

        this.add(this.labels[4], new Cell(0, 2));
        this.add(this.oldInfo[1], new Cell(1, 2));
        this.add(this.newInfo[1], new Cell(2, 2));

        this.add(this.labels[5], new Cell(0, 3));
        this.add(this.oldInfo[2], new Cell(1, 3));
        this.add(this.newInfo[2], new Cell(2, 3));

        this.add(this.labels[6], new Cell(0, 4));
        this.add(this.oldInfo[3], new Cell(1, 4));
        this.add(this.newInfo[3], new Cell(2, 4));
    }

    private static class Cell extends GridBagConstraints {

        protected Cell(int x, int y){
            super();

            this.fill = HORIZONTAL;

            this.gridx = x;
            this.gridy = y;
        }
    }
}
