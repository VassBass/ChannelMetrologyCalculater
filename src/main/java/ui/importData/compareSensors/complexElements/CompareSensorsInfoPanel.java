package ui.importData.compareSensors.complexElements;

import constants.Strings;
import support.Sensor;
import ui.ButtonCell;
import ui.UI_Container;

import javax.swing.*;
import java.awt.*;

public class CompareSensorsInfoPanel extends JPanel implements UI_Container {
    /*
     * [0] = Название поля для сравнения;
     * [1] = ПИП из списка;
     * [2] = Импортируемый ПИП;
     * [3] = Тип ПИП;
     * [4] = Название ПИП
     * [5] = Диапазон ПИП;
     * [6] = Выходной сигнал ПИП;
     * [7] = Формула вычисления погрешности ПИП;
     */
    private JButton[] labels = null;

    /*
     * [0] = Тип ПИП;
     * [1] = Название ПИП;
     * [2] = Диапазон ПИП;
     * [3] = Выходной сигнал ПИП;
     * [4] = Формула вычисления погрешности ПИП;
     */
    private JButton[] oldInfo = null;
    private JButton[] newInfo = null;

    private final Sensor old;
    private final Sensor imported;

    public CompareSensorsInfoPanel(Sensor old, Sensor imported){
        super(new GridBagLayout());

        this.old = old;
        this.imported = imported;

        this.createElements();
        this.setReactions();
        this.build();
    }

    @Override
    public void createElements() {
        labels = new ButtonCell[8];
        oldInfo = new ButtonCell[5];
        newInfo = new ButtonCell[5];

        labels[0] = new ButtonCell(Color.white, Color.black, Strings.FIELD);
        labels[1] = new ButtonCell(Color.white, Color.black, Strings.SENSOR_IN_LIST);
        labels[2] = new ButtonCell(Color.white, Color.black, Strings.IMPORTED_SENSOR);

        if (old.getType().equals(imported.getType())){
            labels[3] = new ButtonCell(Color.green.darker(), Color.white, Strings.TYPE);
            oldInfo[0] = new ButtonCell(Color.green.darker(), Color.white, old.getType());
            newInfo[0] = new ButtonCell(Color.green.darker(), Color.white, imported.getType());
        }else {
            labels[3] = new ButtonCell(Color.red.darker(), Color.white, Strings.TYPE);
            oldInfo[0] = new ButtonCell(Color.red.darker(), Color.white, old.getType());
            newInfo[0] = new ButtonCell(Color.red.darker(), Color.white, imported.getType());
        }

        if (old.getName().equals(imported.getName())){
            labels[4] = new ButtonCell(Color.green.darker(), Color.white, Strings._NAME);
            oldInfo[1] = new ButtonCell(Color.green.darker(), Color.white, old.getName());
            newInfo[1] = new ButtonCell(Color.green.darker(), Color.white, imported.getName());
        }else {
            labels[4] = new ButtonCell(Color.red.darker(), Color.white, Strings._NAME);
            oldInfo[1] = new ButtonCell(Color.red.darker(), Color.white, old.getName());
            newInfo[1] = new ButtonCell(Color.red.darker(), Color.white, imported.getName());
        }

        if (old.getRangeMin() == imported.getRangeMin() && old.getRangeMax() == imported.getRangeMax()){
            labels[5] = new ButtonCell(Color.green.darker(), Color.white, Strings.RANGE_OF_SENSOR);
            String o = String.valueOf(old.getRangeMin()).concat(" - ").concat(String.valueOf(old.getRangeMax()));
            oldInfo[2] = new ButtonCell(Color.green.darker(), Color.white, o);
            String i = String.valueOf(imported.getRangeMin()).concat(" - ").concat(String.valueOf(imported.getRangeMax()));
            newInfo[2] = new ButtonCell(Color.green.darker(), Color.white, i);
        }else {
            labels[5] = new ButtonCell(Color.red.darker(), Color.white, Strings.RANGE_OF_SENSOR);
            String o = String.valueOf(old.getRangeMin()).concat(" - ").concat(String.valueOf(old.getRangeMax()));
            oldInfo[2] = new ButtonCell(Color.red.darker(), Color.white, o);
            String i = String.valueOf(imported.getRangeMin()).concat(" - ").concat(String.valueOf(imported.getRangeMax()));
            newInfo[2] = new ButtonCell(Color.red.darker(), Color.white, i);
        }

        if (old.getValue().equals(imported.getValue())){
            labels[6] = new ButtonCell(Color.green.darker(), Color.white, Strings.OUT);
            oldInfo[3] = new ButtonCell(Color.green.darker(), Color.white, old.getValue());
            newInfo[3] = new ButtonCell(Color.green.darker(), Color.white, imported.getValue());
        }else {
            labels[6] = new ButtonCell(Color.red.darker(), Color.white, Strings.OUT);
            oldInfo[3] = new ButtonCell(Color.red.darker(), Color.white, old.getValue());
            newInfo[3] = new ButtonCell(Color.red.darker(), Color.white, imported.getValue());
        }

        if (old.getErrorFormula().equals(imported.getErrorFormula())){
            labels[7] = new ButtonCell(Color.green.darker(), Color.white, Strings.ERROR_FORMULA);
            oldInfo[4] = new ButtonCell(Color.green.darker(), Color.white, old.getErrorFormula());
            newInfo[4] = new ButtonCell(Color.green.darker(), Color.white, imported.getErrorFormula());
        }else {
            labels[7] = new ButtonCell(Color.red.darker(), Color.white, Strings.ERROR_FORMULA);
            oldInfo[4] = new ButtonCell(Color.red.darker(), Color.white, old.getErrorFormula());
            newInfo[4] = new ButtonCell(Color.red.darker(), Color.white, imported.getErrorFormula());
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

        this.add(this.labels[7], new Cell(0, 5));
        this.add(this.oldInfo[4], new Cell(1, 5));
        this.add(this.newInfo[4], new Cell(2, 5));
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
