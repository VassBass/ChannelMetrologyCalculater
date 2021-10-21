package ui.importChannels.compareChannels.complexElements;

import converters.VariableConverter;
import support.Channel;
import constants.Strings;
import ui.ButtonCell;
import ui.UI_Container;

import javax.swing.*;
import java.awt.*;
import java.util.Locale;

public class CompareChannels_infoPanel extends JPanel implements UI_Container {
    /*
     * [0] = Название поля для сравнения;
     * [1] = Канал из списка;
     * [2] = Импортируемый канал;
     * [3] = Код;
     * [4] = Технологический номер;
     * [5] = Название;
     * [6] = Тип измерения;
     * [7] = Цех;
     * [8] = Участок;
     * [9] = Линия, секция и т.п;
     * [10] = Установка;
     * [11] = Диапазон канала;
     * [12] = Допустимая погрешность канала;
     * [13] = Пригодность канала;
     * [14] = Первичный измерительный прибор;
     * [15] = Тип ПИП;
     * [16] = Диапазон ПИП;
     * [17] = Заводской номер ПИП;
     * [18] = Выходной сигнал ПИП;
     * [19] = Данные о проверке;
     * [20] = Дата последней проверки;
     * [21] = Периодичность проверки;
     * [22] = Номер протокола;
     * [23] = Справка;
     */
    private JButton[] labels = null;

    /*
     * [0] = Код канала;
     * [1] = Технологический номер канала;
     * [2] = Название канала;
     * [3] = Тип измерения канала;
     * [4] = Цех канала;
     * [5] = Участок канала;
     * [6] = Линия, секция и т.п. канала;
     * [7] = Установка канала;
     * [8] = Диапазон канала;
     * [9] = Допустимая погрешность канала;
     * [10] = Пригодность канала;
     * [11] = Тип ПИП;
     * [12] = Диапазон ПИП;
     * [13] = Заводской номер ПИП;
     * [14] = Выходной сигнал ПИП;
     * [15] = Дата последней проверки;
     * [16] = Периодичность проверки;
     * [17] = Номер протокола;
     * [18] = Справка;
     */
    private JButton[] oldInfo = null;
    private JButton[] newInfo = null;

    private final Channel old;
    private final Channel imported;

    public CompareChannels_infoPanel(Channel old, Channel imported){
        super(new GridBagLayout());

        this.old = old;
        this.imported = imported;

        this.createElements();
        this.setReactions();
        this.build();
    }

    @Override
    public void createElements() {
        labels = new ButtonCell[24];
        oldInfo = new ButtonCell[19];
        newInfo = new ButtonCell[19];

        labels[0] = new ButtonCell(Color.white, Color.black, Strings.FIELD);
        labels[1] = new ButtonCell(Color.white, Color.black, Strings.CHANNEL_IN_LIST);
        labels[2] = new ButtonCell(Color.white, Color.black, Strings.IMPORTED_CHANNEL);

        if (old.getCode().equals(imported.getCode())){
            labels[3] = new ButtonCell(Color.green.darker(), Color.white, Strings.CODE);
            oldInfo[0] = new ButtonCell(Color.green.darker(), Color.white, old.getCode());
            newInfo[0] = new ButtonCell(Color.green.darker(), Color.white, imported.getCode());
        }else {
            labels[3] = new ButtonCell(Color.red.darker(), Color.white, Strings.CODE);
            oldInfo[0] = new ButtonCell(Color.red.darker(), Color.white, old.getCode());
            newInfo[0] = new ButtonCell(Color.red.darker(), Color.white, imported.getCode());
        }

        if (old.getTechnologyNumber().equals(imported.getTechnologyNumber())){
            labels[4] = new ButtonCell(Color.green.darker(), Color.white, Strings.TECHNOLOGY_NUMBER);
            oldInfo[1] = new ButtonCell(Color.green.darker(), Color.white, old.getTechnologyNumber());
            newInfo[1] = new ButtonCell(Color.green.darker(), Color.white, imported.getTechnologyNumber());
        }else {
            labels[4] = new ButtonCell(Color.red.darker(), Color.white, Strings.TECHNOLOGY_NUMBER);
            oldInfo[1] = new ButtonCell(Color.red.darker(), Color.white, old.getTechnologyNumber());
            newInfo[1] = new ButtonCell(Color.red.darker(), Color.white, imported.getTechnologyNumber());
        }

        if (old.getName().equals(imported.getName())){
            labels[5] = new ButtonCell(Color.green.darker(), Color.white, Strings._NAME);
            oldInfo[2] = new ButtonCell(Color.green.darker(), Color.white, old.getName());
            newInfo[2] = new ButtonCell(Color.green.darker(), Color.white, imported.getName());
        }else {
            labels[5] = new ButtonCell(Color.red.darker(), Color.white, Strings._NAME);
            oldInfo[2] = new ButtonCell(Color.red.darker(), Color.white, old.getName());
            newInfo[2] = new ButtonCell(Color.red.darker(), Color.white, imported.getName());
        }

        String vo;
        String vi;

        vo = old.getMeasurement().getValue();
        vi = imported.getMeasurement().getValue();
        String mo = old.getMeasurement().getName().concat(" [").concat(vo).concat("]");
        String mi = imported.getMeasurement().getName().concat("[").concat(vi).concat("]");
        if (old.getMeasurement().equals(imported.getMeasurement())){
            labels[6] = new ButtonCell(Color.green.darker(), Color.white, Strings.TYPE_OF_MEASUREMENT);
            oldInfo[3] = new ButtonCell(Color.green.darker(), Color.white, mo);
            newInfo[3] = new ButtonCell(Color.green.darker(), Color.white, mi);
        }else {
            labels[6] = new ButtonCell(Color.red.darker(), Color.white, Strings.TYPE_OF_MEASUREMENT);
            oldInfo[3] = new ButtonCell(Color.red.darker(), Color.white, mo);
            newInfo[3] = new ButtonCell(Color.red.darker(), Color.white, mi);
        }

        if (old.getDepartment().equals(imported.getDepartment())){
            labels[7] = new ButtonCell(Color.green.darker(), Color.white, Strings.DEPARTMENT);
            oldInfo[4] = new ButtonCell(Color.green.darker(), Color.white, old.getDepartment());
            newInfo[4] = new ButtonCell(Color.green.darker(), Color.white, imported.getDepartment());
        }else {
            labels[7] = new ButtonCell(Color.red.darker(), Color.white, Strings.DEPARTMENT);
            oldInfo[4] = new ButtonCell(Color.red.darker(), Color.white, old.getDepartment());
            newInfo[4] = new ButtonCell(Color.red.darker(), Color.white, imported.getDepartment());
        }

        if (old.getArea().equals(imported.getArea())){
            labels[8] = new ButtonCell(Color.green.darker(), Color.white, Strings.AREA);
            oldInfo[5] = new ButtonCell(Color.green.darker(), Color.white, old.getArea());
            newInfo[5] = new ButtonCell(Color.green.darker(), Color.white, imported.getArea());
        }else {
            labels[8] = new ButtonCell(Color.red.darker(), Color.white, Strings.AREA);
            oldInfo[5] = new ButtonCell(Color.red.darker(), Color.white, old.getArea());
            newInfo[5] = new ButtonCell(Color.red.darker(), Color.white, imported.getArea());
        }

        if (old.getProcess().equals(imported.getProcess())){
            labels[9] = new ButtonCell(Color.green.darker(), Color.white, Strings.PROCESS);
            oldInfo[6] = new ButtonCell(Color.green.darker(), Color.white, old.getProcess());
            newInfo[6] = new ButtonCell(Color.green.darker(), Color.white, imported.getProcess());
        }else {
            labels[9] = new ButtonCell(Color.red.darker(), Color.white, Strings.PROCESS);
            oldInfo[6] = new ButtonCell(Color.red.darker(), Color.white, old.getProcess());
            newInfo[6] = new ButtonCell(Color.red.darker(), Color.white, imported.getProcess());
        }

        if (old.getInstallation().equals(imported.getInstallation())){
            labels[10] = new ButtonCell(Color.green.darker(), Color.white, Strings.INSTALLATION);
            oldInfo[7] = new ButtonCell(Color.green.darker(), Color.white, old.getInstallation());
            newInfo[7] = new ButtonCell(Color.green.darker(), Color.white, imported.getInstallation());
        }else {
            labels[10] = new ButtonCell(Color.red.darker(), Color.white, Strings.INSTALLATION);
            oldInfo[7] = new ButtonCell(Color.red.darker(), Color.white, old.getInstallation());
            newInfo[7] = new ButtonCell(Color.red.darker(), Color.white, imported.getInstallation());
        }

        if (old.getRangeMin() == imported.getRangeMin() && old.getRangeMax() == imported.getRangeMax()){
            labels[11] = new ButtonCell(Color.green.darker(), Color.white, Strings.RANGE_OF_CHANNEL);
            String o = String.valueOf(old.getRangeMin()).concat(" - ").concat(String.valueOf(old.getRangeMax()));
            oldInfo[8] = new ButtonCell(Color.green.darker(), Color.white, o);
            String i = String.valueOf(imported.getRangeMin()).concat(" - ").concat(String.valueOf(imported.getRangeMax()));
            newInfo[8] = new ButtonCell(Color.green.darker(), Color.white, i);
        }else {
            labels[11] = new ButtonCell(Color.red.darker(), Color.white, Strings.RANGE_OF_CHANNEL);
            String o = String.valueOf(old.getRangeMin()).concat(" - ").concat(String.valueOf(old.getRangeMax()));
            oldInfo[8] = new ButtonCell(Color.red.darker(), Color.white, o);
            String i = String.valueOf(imported.getRangeMin()).concat(" - ").concat(String.valueOf(imported.getRangeMax()));
            newInfo[8] = new ButtonCell(Color.red.darker(), Color.white, i);
        }

        if (old.getAllowableErrorPercent() == imported.getAllowableErrorPercent() && old.getAllowableError() == imported.getAllowableError()){
            labels[12] = new ButtonCell(Color.green.darker(), Color.white, Strings.ALLOWABLE_ERROR_OF_CHANNEL);
            String o = String.valueOf(old.getAllowableErrorPercent()).concat("% або ").concat(String.valueOf(old.getAllowableError())).
                    concat(old.getMeasurement().getValue());
            oldInfo[9] = new ButtonCell(Color.green.darker(), Color.white, o);
            String i = String.valueOf(imported.getAllowableErrorPercent()).concat("% або ").concat(String.valueOf(imported.getAllowableError())).
                    concat(old.getMeasurement().getValue());
            newInfo[9] = new ButtonCell(Color.green.darker(), Color.white, i);
        }else {
            labels[12] = new ButtonCell(Color.red.darker(), Color.white, Strings.ALLOWABLE_ERROR_OF_CHANNEL);
            String o = String.valueOf(old.getAllowableErrorPercent()).concat("% або ").concat(String.valueOf(old.getAllowableError())).
                    concat(old.getMeasurement().getValue());
            oldInfo[9] = new ButtonCell(Color.red.darker(), Color.white, o);
            String i = String.valueOf(imported.getAllowableErrorPercent()).concat("% або ").concat(String.valueOf(imported.getAllowableError())).
                    concat(old.getMeasurement().getValue());
            newInfo[9] = new ButtonCell(Color.red.darker(), Color.white, i);
        }

        String so;
        String si;
        if (old.isGood){
            so = Strings.CHANNEL_IS_GOOD;
        }else {
            so = Strings.CHANNEL_IS_BAD;
        }
        if (imported.isGood){
            si = Strings.CHANNEL_IS_GOOD;
        }else {
            si = Strings.CHANNEL_IS_BAD;
        }
        if (old.isGood == imported.isGood){
            labels[13] = new ButtonCell(Color.green.darker(), Color.white, Strings.CHANNEL_SUITABILITY);
            oldInfo[10] = new ButtonCell(Color.green.darker(), Color.white, so);
            newInfo[10] = new ButtonCell(Color.green.darker(), Color.white, si);
        }else {
            labels[13] = new ButtonCell(Color.red.darker(), Color.white, Strings.CHANNEL_SUITABILITY);
            oldInfo[10] = new ButtonCell(Color.red.darker(), Color.white, so);
            newInfo[10] = new ButtonCell(Color.red.darker(), Color.white, si);
        }

        labels[14] = new ButtonCell(Color.white, Color.black, Strings.SENSOR);

        if (old.getSensor().getType() == imported.getSensor().getType()){
            labels[15] = new ButtonCell(Color.green.darker(), Color.white, Strings.TYPE);
            oldInfo[11] = new ButtonCell(Color.green.darker(), Color.white, old.getSensor().getType().getType());
            newInfo[11] = new ButtonCell(Color.green.darker(), Color.white, imported.getSensor().getType().getType());
        }else {
            labels[15] = new ButtonCell(Color.red.darker(), Color.white, Strings.TYPE);
            oldInfo[11] = new ButtonCell(Color.red.darker(), Color.white, old.getSensor().getType().getType());
            newInfo[11] = new ButtonCell(Color.red.darker(), Color.white, imported.getSensor().getType().getType());
        }

        String ro = String.valueOf(old.getSensor().getRangeMin()).concat(" - ").concat(String.valueOf(old.getSensor().getRangeMax()));
        String ri = String.valueOf(imported.getSensor().getRangeMin()).concat(" - ").concat(String.valueOf(imported.getSensor().getRangeMax()));
        if (old.getSensor().getRangeMin() == imported.getSensor().getRangeMin() && old.getSensor().getRangeMax() == imported.getSensor().getRangeMax()){
            labels[16] = new ButtonCell(Color.green.darker(), Color.white, Strings.RANGE_OF_SENSOR);
            oldInfo[12] = new ButtonCell(Color.green.darker(), Color.white, ro);
            newInfo[12] = new ButtonCell(Color.green.darker(), Color.white, ri);
        }else {
            labels[16] = new ButtonCell(Color.red.darker(), Color.white, Strings.RANGE_OF_SENSOR);
            oldInfo[12] = new ButtonCell(Color.red.darker(), Color.white, ro);
            newInfo[12] = new ButtonCell(Color.red.darker(), Color.white, ri);
        }

        String no;
        String ni;
        if (old.getSensor().getNumber() == null || old.getSensor().getNumber().length() == 0) {
            no = " - ";
            if (imported.getSensor().getNumber().length() == 0){
                ni = " - ";
                labels[17] = new ButtonCell(Color.green.darker(), Color.white, Strings.PARENT_NUMBER);
                oldInfo[13] = new ButtonCell(Color.green.darker(), Color.white, no);
                newInfo[13] = new ButtonCell(Color.green.darker(), Color.white, ni);
            }else {
                ni = imported.getSensor().getNumber();
                labels[17] = new ButtonCell(Color.red.darker(), Color.white, Strings.PARENT_NUMBER);
                oldInfo[13] = new ButtonCell(Color.red.darker(), Color.white, no);
                newInfo[13] = new ButtonCell(Color.red.darker(), Color.white, ni);
            }
        }else {
            no = old.getSensor().getNumber();
            if (imported.getSensor().getNumber().length() == 0){
                ni = " - ";
                labels[17] = new ButtonCell(Color.red.darker(), Color.white, Strings.PARENT_NUMBER);
                oldInfo[13] = new ButtonCell(Color.red.darker(), Color.white, no);
                newInfo[13] = new ButtonCell(Color.red.darker(), Color.white, ni);
            }else if (old.getSensor().getNumber().equals(imported.getSensor().getNumber())){
                ni = imported.getSensor().getNumber();
                labels[17] = new ButtonCell(Color.green.darker(), Color.white, Strings.PARENT_NUMBER);
                oldInfo[13] = new ButtonCell(Color.green.darker(), Color.white, no);
                newInfo[13] = new ButtonCell(Color.green.darker(), Color.white, ni);
            }else{
                ni = imported.getSensor().getNumber();
                labels[17] = new ButtonCell(Color.red.darker(), Color.white, Strings.PARENT_NUMBER);
                oldInfo[13] = new ButtonCell(Color.red.darker(), Color.white, no);
                newInfo[13] = new ButtonCell(Color.red.darker(), Color.white, ni);
            }
        }

        if (old.getSensor().getValue().equals(imported.getSensor().getValue())){
            labels[18] = new ButtonCell(Color.green.darker(), Color.white, Strings.OUT);
            oldInfo[14] = new ButtonCell(Color.green.darker(), Color.white, old.getSensor().getValue());
            newInfo[14] = new ButtonCell(Color.green.darker(), Color.white, imported.getSensor().getValue());
        }else {
            labels[18] = new ButtonCell(Color.red.darker(), Color.white, Strings.OUT);
            oldInfo[14] = new ButtonCell(Color.red.darker(), Color.white, old.getSensor().getValue());
            newInfo[14] = new ButtonCell(Color.red.darker(), Color.white, imported.getSensor().getValue());
        }

        labels[19] = new ButtonCell(Color.white, Color.black, Strings.CONTROL_INFO);

        if (VariableConverter.dateToString(old.getDate()).equals(VariableConverter.dateToString(imported.getDate()))){
            labels[20] = new ButtonCell(Color.green.darker(), Color.white, Strings.THIS_DATE);
            oldInfo[15] = new ButtonCell(Color.green.darker(), Color.white, VariableConverter.dateToString(old.getDate()));
            newInfo[15] = new ButtonCell(Color.green.darker(), Color.white, VariableConverter.dateToString(imported.getDate()));
        }else {
            labels[20] = new ButtonCell(Color.red.darker(), Color.white, Strings.THIS_DATE);
            oldInfo[15] = new ButtonCell(Color.red.darker(), Color.white, VariableConverter.dateToString(old.getDate()));
            newInfo[15] = new ButtonCell(Color.red.darker(), Color.white, VariableConverter.dateToString(imported.getDate()));
        }

        String fo = String.format(Locale.ENGLISH, "%.0f", old.getFrequency()).concat(Strings.YEAR_WORD(old.getFrequency()));
        String fi = String.format(Locale.ENGLISH, "%.0f", imported.getFrequency()).concat(Strings.YEAR_WORD(imported.getFrequency()));
        if (old.getFrequency() == imported.getFrequency()){
            labels[21] = new ButtonCell(Color.green.darker(), Color.white, Strings.FREQUENCY_CONTROL);
            oldInfo[16] = new ButtonCell(Color.green.darker(), Color.white, fo);
            newInfo[16] = new ButtonCell(Color.green.darker(), Color.white, fi);
        }else {
            labels[21] = new ButtonCell(Color.red.darker(), Color.white, Strings.FREQUENCY_CONTROL);
            oldInfo[16] = new ButtonCell(Color.red.darker(), Color.white, fo);
            newInfo[16] = new ButtonCell(Color.red.darker(), Color.white, fi);
        }

        if (old.getNumberOfProtocol().equals(imported.getNumberOfProtocol())){
            labels[22] = new ButtonCell(Color.green.darker(), Color.white, Strings.PROTOCOL_NUMBER);
            oldInfo[17] = new ButtonCell(Color.green.darker(), Color.white, old.getNumberOfProtocol());
            newInfo[17] = new ButtonCell(Color.green.darker(), Color.white, imported.getNumberOfProtocol());
        }else {
            labels[22] = new ButtonCell(Color.red.darker(), Color.white, Strings.PROTOCOL_NUMBER);
            oldInfo[17] = new ButtonCell(Color.red.darker(), Color.white, old.getNumberOfProtocol());
            newInfo[17] = new ButtonCell(Color.red.darker(), Color.white, imported.getNumberOfProtocol());
        }

        String refo;
        String refi;
        if (old.getReference() == null) {
            refo = " - ";
            if (imported.getReference() == null){
                refi = " - ";
                labels[23] = new ButtonCell(Color.green.darker(), Color.white, Strings.REFERENCE);
                oldInfo[18] = new ButtonCell(Color.green.darker(), Color.white, refo);
                newInfo[18] = new ButtonCell(Color.green.darker(), Color.white, refi);
            }else {
                refi = imported.getReference();
                labels[23] = new ButtonCell(Color.red.darker(), Color.white, Strings.REFERENCE);
                oldInfo[18] = new ButtonCell(Color.red.darker(), Color.white, refo);
                newInfo[18] = new ButtonCell(Color.red.darker(), Color.white, refi);
            }
        }else {
            refo = old.getReference();
            if (imported.getReference() == null){
                refi = " - ";
                labels[23] = new ButtonCell(Color.red.darker(), Color.white, Strings.REFERENCE);
                oldInfo[18] = new ButtonCell(Color.red.darker(), Color.white, refo);
                newInfo[18] = new ButtonCell(Color.red.darker(), Color.white, refi);
            }else if (old.getReference().equals(imported.getReference())){
                refi = imported.getReference();
                labels[23] = new ButtonCell(Color.green.darker(), Color.white, Strings.REFERENCE);
                oldInfo[18] = new ButtonCell(Color.green.darker(), Color.white, refo);
                newInfo[18] = new ButtonCell(Color.green.darker(), Color.white, refi);
            }else{
                refi = imported.getReference();
                labels[23] = new ButtonCell(Color.red.darker(), Color.white, Strings.REFERENCE);
                oldInfo[18] = new ButtonCell(Color.red.darker(), Color.white, refo);
                newInfo[18] = new ButtonCell(Color.red.darker(), Color.white, refi);
            }
        }
    }

    @Override
    public void setReactions() {

    }

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

        this.add(this.labels[12], new Cell(0, 10));
        this.add(this.oldInfo[9], new Cell(1, 10));
        this.add(this.newInfo[9], new Cell(2, 10));

        this.add(this.labels[13], new Cell(0, 11));
        this.add(this.oldInfo[10], new Cell(1, 11));
        this.add(this.newInfo[10], new Cell(2, 11));

        this.add(labels[14], new Cell(12));

        this.add(this.labels[15], new Cell(0, 13));
        this.add(this.oldInfo[11], new Cell(1, 13));
        this.add(this.newInfo[11], new Cell(2, 13));

        this.add(this.labels[16], new Cell(0, 14));
        this.add(this.oldInfo[12], new Cell(1, 14));
        this.add(this.newInfo[12], new Cell(2, 14));

        this.add(this.labels[17], new Cell(0, 15));
        this.add(this.oldInfo[13], new Cell(1, 15));
        this.add(this.newInfo[13], new Cell(2, 15));

        this.add(this.labels[18], new Cell(0, 16));
        this.add(this.oldInfo[14], new Cell(1, 16));
        this.add(this.newInfo[14], new Cell(2, 16));

        this.add(labels[19], new Cell(17));

        this.add(this.labels[20], new Cell(0, 18));
        this.add(this.oldInfo[15], new Cell(1, 18));
        this.add(this.newInfo[15], new Cell(2, 18));

        this.add(this.labels[21], new Cell(0, 19));
        this.add(this.oldInfo[16], new Cell(1, 19));
        this.add(this.newInfo[16], new Cell(2, 19));

        this.add(this.labels[22], new Cell(0, 20));
        this.add(this.oldInfo[17], new Cell(1, 20));
        this.add(this.newInfo[17], new Cell(2, 20));

        this.add(this.labels[23], new Cell(0, 21));
        this.add(this.oldInfo[18], new Cell(1, 21));
        this.add(this.newInfo[18], new Cell(2, 21));
    }

    private static class Cell extends GridBagConstraints {

        protected Cell(int x, int y){
            super();

            this.fill = HORIZONTAL;

            this.gridx = x;
            this.gridy = y;
        }

        protected Cell(int y){
            super();

            this.fill = HORIZONTAL;
            this.gridwidth = 3;

            this.gridx = 0;
            this.gridy = y;
        }
    }
}
