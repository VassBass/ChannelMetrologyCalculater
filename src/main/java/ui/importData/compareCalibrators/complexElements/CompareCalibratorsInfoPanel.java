package ui.importData.compareCalibrators.complexElements;

import converters.VariableConverter;
import model.Calibrator;
import ui.model.ButtonCell;

import javax.swing.*;
import java.awt.*;

public class CompareCalibratorsInfoPanel extends JPanel {
    private static final String FIELD = "Поле";
    private static final String CALIBRATOR_IN_LIST = "Калібратор зі списку";
    private static final String IMPORTED_CALIBRATOR = "Імпортуємий калібратор";
    private static final String TYPE = "Тип";
    private static final String NAME = "Назва";
    private static final String PARENT_NUMBER = "Заводський № ";
    private static final String TYPE_OF_MEASUREMENT = "Вид вимірювання";
    private static final String RANGE_OF_CALIBRATOR = "Діапазон калібратора";
    private static final String ERROR_FORMULA = "Формула для розрахунку похибки";
    private static final String CERTIFICATE_NAME = "Назва сертифікату";
    private static final String CERTIFICATE_DATE = "Дата повірки";
    private static final String CERTIFICATE_COMPANY = "Компанія яка проводила повірку";

    /*
     * [0] = Название поля для сравнения;
     * [1] = Калибратор из списка;
     * [2] = Импортируемый калибратор;
     * [3] = Тип;
     * [4] = Название;
     * [5] = Номер;
     * [6] = Тип измерения;
     * [7] = Диапазон;
     * [8] = Формула вычисления погрешности;
     * [9] = Название сертификата о поверке;
     * [10] = Дата поверки в сертификате;
     * [11] = Кампания проводившая поверку(из сертификата);
     */
    private JButton[] labels = null;

    /*
     * [0] = Тип;
     * [1] = Название;
     * [2] = Номер;
     * [3] = Тип измерения;
     * [4] = Диапазон;
     * [5] = Формула вычисления погрешности;
     * [6] = Название сертификата о поверке;
     * [7] = Дата поверки в сертификате;
     * [8] = Кампания проводившая поверку(из сертификата);
     */
    private JButton[] oldInfo = null;
    private JButton[] newInfo = null;

    private final Calibrator old;
    private final Calibrator imported;

    public CompareCalibratorsInfoPanel(Calibrator old, Calibrator imported){
        super(new GridBagLayout());

        this.old = old;
        this.imported = imported;

        this.createElements();
        this.build();
    }

    private void createElements() {
        labels = new ButtonCell[12];
        oldInfo = new ButtonCell[9];
        newInfo = new ButtonCell[9];

        labels[0] = new ButtonCell(Color.WHITE, Color.BLACK, FIELD);
        labels[1] = new ButtonCell(Color.WHITE, Color.BLACK, CALIBRATOR_IN_LIST);
        labels[2] = new ButtonCell(Color.WHITE, Color.BLACK, IMPORTED_CALIBRATOR);

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

        if (old.getNumber().equals(imported.getNumber())){
            labels[5] = new ButtonCell(Color.GREEN.darker(), Color.WHITE, PARENT_NUMBER);
            oldInfo[2] = new ButtonCell(Color.GREEN.darker(), Color.WHITE, old.getNumber());
            newInfo[2] = new ButtonCell(Color.GREEN.darker(), Color.WHITE, imported.getNumber());
        }else {
            labels[5] = new ButtonCell(Color.RED.darker(), Color.WHITE, PARENT_NUMBER);
            oldInfo[2] = new ButtonCell(Color.RED.darker(), Color.WHITE, old.getNumber());
            newInfo[2] = new ButtonCell(Color.RED.darker(), Color.WHITE, imported.getNumber());
        }

        if (old.getMeasurement().equals(imported.getMeasurement())){
            labels[6] = new ButtonCell(Color.GREEN.darker(), Color.WHITE, TYPE_OF_MEASUREMENT);
            oldInfo[3] = new ButtonCell(Color.GREEN.darker(), Color.WHITE, old.getMeasurement());
            newInfo[3] = new ButtonCell(Color.GREEN.darker(), Color.WHITE, imported.getMeasurement());
        }else {
            labels[6] = new ButtonCell(Color.RED.darker(), Color.WHITE, TYPE_OF_MEASUREMENT);
            oldInfo[3] = new ButtonCell(Color.RED.darker(), Color.WHITE, old.getMeasurement());
            newInfo[3] = new ButtonCell(Color.RED.darker(), Color.WHITE, imported.getMeasurement());
        }

        if (old.getRangeMin() == imported.getRangeMin() && old.getRangeMax() == imported.getRangeMax()){
            labels[7] = new ButtonCell(Color.GREEN.darker(), Color.WHITE, RANGE_OF_CALIBRATOR);
            String o = old.getRangeMin() + " - " + old.getRangeMax();
            oldInfo[4] = new ButtonCell(Color.GREEN.darker(), Color.WHITE, o);
            String i = imported.getRangeMin() + " - " + imported.getRangeMax();
            newInfo[4] = new ButtonCell(Color.GREEN.darker(), Color.WHITE, i);
        }else {
            labels[7] = new ButtonCell(Color.RED.darker(), Color.WHITE, RANGE_OF_CALIBRATOR);
            String o = old.getRangeMin() + " - " + old.getRangeMax();
            oldInfo[4] = new ButtonCell(Color.RED.darker(), Color.WHITE, o);
            String i = imported.getRangeMin() + " - " + imported.getRangeMax();
            newInfo[4] = new ButtonCell(Color.RED.darker(), Color.WHITE, i);
        }

        if (old.getErrorFormula().equals(imported.getErrorFormula())){
            labels[8] = new ButtonCell(Color.GREEN.darker(), Color.WHITE, ERROR_FORMULA);
            oldInfo[5] = new ButtonCell(Color.GREEN.darker(), Color.WHITE, old.getErrorFormula());
            newInfo[5] = new ButtonCell(Color.GREEN.darker(), Color.WHITE, imported.getErrorFormula());
        }else {
            labels[8] = new ButtonCell(Color.RED.darker(), Color.WHITE, ERROR_FORMULA);
            oldInfo[5] = new ButtonCell(Color.RED.darker(), Color.WHITE, old.getErrorFormula());
            newInfo[5] = new ButtonCell(Color.RED.darker(), Color.WHITE, imported.getErrorFormula());
        }

        if (old.getCertificateName().equals(imported.getCertificateName())){
            labels[9] = new ButtonCell(Color.GREEN.darker(), Color.WHITE, CERTIFICATE_NAME);
            oldInfo[6] = new ButtonCell(Color.GREEN.darker(), Color.WHITE, old.getCertificateName());
            newInfo[6] = new ButtonCell(Color.GREEN.darker(), Color.WHITE, imported.getCertificateName());
        }else {
            labels[9] = new ButtonCell(Color.RED.darker(), Color.WHITE, CERTIFICATE_NAME);
            oldInfo[6] = new ButtonCell(Color.RED.darker(), Color.WHITE, old.getCertificateName());
            newInfo[6] = new ButtonCell(Color.RED.darker(), Color.WHITE, imported.getCertificateName());
        }

        if (VariableConverter.dateToString(old.getCertificateDate()).equals(VariableConverter.dateToString(imported.getCertificateDate()))){
            labels[10] = new ButtonCell(Color.GREEN.darker(), Color.WHITE, CERTIFICATE_DATE);
            oldInfo[7] = new ButtonCell(Color.GREEN.darker(), Color.WHITE, VariableConverter.dateToString(old.getCertificateDate()));
            newInfo[7] = new ButtonCell(Color.GREEN.darker(), Color.WHITE, VariableConverter.dateToString(imported.getCertificateDate()));
        }else {
            labels[10] = new ButtonCell(Color.RED.darker(), Color.WHITE, CERTIFICATE_DATE);
            oldInfo[7] = new ButtonCell(Color.RED.darker(), Color.WHITE, VariableConverter.dateToString(old.getCertificateDate()));
            newInfo[7] = new ButtonCell(Color.RED.darker(), Color.WHITE, VariableConverter.dateToString(imported.getCertificateDate()));
        }

        if (old.getCertificateCompany().equals(imported.getCertificateCompany())){
            labels[11] = new ButtonCell(Color.GREEN.darker(), Color.WHITE, CERTIFICATE_COMPANY);
            oldInfo[8] = new ButtonCell(Color.GREEN.darker(), Color.WHITE, old.getCertificateCompany());
            newInfo[8] = new ButtonCell(Color.GREEN.darker(), Color.WHITE, imported.getCertificateCompany());
        }else {
            labels[11] = new ButtonCell(Color.RED.darker(), Color.WHITE, CERTIFICATE_COMPANY);
            oldInfo[8] = new ButtonCell(Color.RED.darker(), Color.WHITE, old.getCertificateCompany());
            newInfo[8] = new ButtonCell(Color.RED.darker(), Color.WHITE, imported.getCertificateCompany());
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

        this.add(this.labels[8], new Cell(0, 6));
        this.add(this.oldInfo[5], new Cell(1, 6));
        this.add(this.newInfo[5], new Cell(2, 6));

        this.add(this.labels[9], new Cell(0, 7));
        this.add(this.oldInfo[6], new Cell(1, 7));
        this.add(this.newInfo[6], new Cell(2, 7));

        this.add(this.labels[10], new Cell(0, 8));
        this.add(this.oldInfo[7], new Cell(1, 8));
        this.add(this.newInfo[7], new Cell(2, 8));

        this.add(this.labels[11], new Cell(0, 9));
        this.add(this.oldInfo[8], new Cell(1, 9));
        this.add(this.newInfo[8], new Cell(2, 9));
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