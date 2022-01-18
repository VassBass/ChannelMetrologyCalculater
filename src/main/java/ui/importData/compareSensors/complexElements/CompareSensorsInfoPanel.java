package ui.importData.compareSensors.complexElements;

import model.Sensor;
import ui.model.ButtonCell;

import javax.swing.*;
import java.awt.*;

public class CompareSensorsInfoPanel extends JPanel {
    private static final String FIELD = "Поле";
    private static final String SENSOR_IN_LIST = "ПВП з переліку";
    private static final String IMPORTED_SENSOR = "Імпортуємий ПВП";
    private static final String TYPE = "Тип";
    private static final String NAME = "Назва";
    private static final String RANGE_OF_SENSOR = "Діапазон вимірювання ПВП";
    private static final String OUT = "Вихід";
    private static final String ERROR_FORMULA = "Формула для розрахунку похибки";

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
        this.build();
    }

    private void createElements() {
        labels = new ButtonCell[8];
        oldInfo = new ButtonCell[5];
        newInfo = new ButtonCell[5];

        labels[0] = new ButtonCell(Color.WHITE, Color.BLACK, FIELD);
        labels[1] = new ButtonCell(Color.WHITE, Color.BLACK, SENSOR_IN_LIST);
        labels[2] = new ButtonCell(Color.WHITE, Color.BLACK, IMPORTED_SENSOR);

        if (old.getType().equals(imported.getType())){
            labels[3] = new ButtonCell(Color.GREEN.darker(), Color.WHITE, TYPE);
            oldInfo[0] = new ButtonCell(Color.GREEN.darker(), Color.WHITE, old.getType());
            newInfo[0] = new ButtonCell(Color.GREEN.darker(), Color.WHITE, imported.getType());
        }else {
            labels[3] = new ButtonCell(Color.RED.darker(), Color.WHITE, TYPE);
            oldInfo[0] = new ButtonCell(Color.RED.darker(), Color.WHITE, old.getType());
            newInfo[0] = new ButtonCell(Color.RED.darker(), Color.WHITE, imported.getType());
        }

        if (old.getName().equals(imported.getName())){
            labels[4] = new ButtonCell(Color.GREEN.darker(), Color.WHITE, NAME);
            oldInfo[1] = new ButtonCell(Color.GREEN.darker(), Color.WHITE, old.getName());
            newInfo[1] = new ButtonCell(Color.GREEN.darker(), Color.WHITE, imported.getName());
        }else {
            labels[4] = new ButtonCell(Color.RED.darker(), Color.WHITE, NAME);
            oldInfo[1] = new ButtonCell(Color.RED.darker(), Color.WHITE, old.getName());
            newInfo[1] = new ButtonCell(Color.RED.darker(), Color.WHITE, imported.getName());
        }

        if (old.getRangeMin() == imported.getRangeMin() && old.getRangeMax() == imported.getRangeMax()){
            labels[5] = new ButtonCell(Color.GREEN.darker(), Color.WHITE, RANGE_OF_SENSOR);
            String o = old.getRangeMin() + " - " + old.getRangeMax();
            oldInfo[2] = new ButtonCell(Color.GREEN.darker(), Color.WHITE, o);
            String i = imported.getRangeMin() + " - " + imported.getRangeMax();
            newInfo[2] = new ButtonCell(Color.GREEN.darker(), Color.WHITE, i);
        }else {
            labels[5] = new ButtonCell(Color.RED.darker(), Color.WHITE, RANGE_OF_SENSOR);
            String o = old.getRangeMin() + " - " + old.getRangeMax();
            oldInfo[2] = new ButtonCell(Color.RED.darker(), Color.WHITE, o);
            String i = imported.getRangeMin() + " - " + imported.getRangeMax();
            newInfo[2] = new ButtonCell(Color.RED.darker(), Color.WHITE, i);
        }

        if (old.getValue().equals(imported.getValue())){
            labels[6] = new ButtonCell(Color.GREEN.darker(), Color.WHITE, OUT);
            oldInfo[3] = new ButtonCell(Color.GREEN.darker(), Color.WHITE, old.getValue());
            newInfo[3] = new ButtonCell(Color.GREEN.darker(), Color.WHITE, imported.getValue());
        }else {
            labels[6] = new ButtonCell(Color.RED.darker(), Color.WHITE, OUT);
            oldInfo[3] = new ButtonCell(Color.RED.darker(), Color.WHITE, old.getValue());
            newInfo[3] = new ButtonCell(Color.RED.darker(), Color.WHITE, imported.getValue());
        }

        if (old.getErrorFormula().equals(imported.getErrorFormula())){
            labels[7] = new ButtonCell(Color.GREEN.darker(), Color.WHITE, ERROR_FORMULA);
            oldInfo[4] = new ButtonCell(Color.GREEN.darker(), Color.WHITE, old.getErrorFormula());
            newInfo[4] = new ButtonCell(Color.GREEN.darker(), Color.WHITE, imported.getErrorFormula());
        }else {
            labels[7] = new ButtonCell(Color.RED.darker(), Color.WHITE, ERROR_FORMULA);
            oldInfo[4] = new ButtonCell(Color.RED.darker(), Color.WHITE, old.getErrorFormula());
            newInfo[4] = new ButtonCell(Color.RED.darker(), Color.WHITE, imported.getErrorFormula());
        }
    }

    private void build() {
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