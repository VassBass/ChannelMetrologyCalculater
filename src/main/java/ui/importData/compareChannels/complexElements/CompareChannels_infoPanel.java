package ui.importData.compareChannels.complexElements;

import converters.VariableConverter;
import model.Channel;
import ui.model.ButtonCell;

import javax.swing.*;
import java.awt.*;
import java.util.Locale;

public class CompareChannels_infoPanel extends JPanel {
    private static final String FIELD = "Поле";
    private static final String CHANNEL_IN_LIST = "Канал з переліку";
    private static final String IMPORTED_CHANNEL = "Імпортуємий канал";
    private static final String CODE = "Код";
    private static final String TECHNOLOGY_NUMBER = "Технологічний номер";
    private static final String NAME = "Назва";
    private static final String TYPE_OF_MEASUREMENT = "Вид вимірювання";
    private static final String DEPARTMENT = "Цех";
    private static final String AREA = "Ділянка";
    private static final String PROCESS = "Лінія, секція і т.п.";
    private static final String INSTALLATION = "Установка";
    private static final String RANGE_OF_CHANNEL = "Діапазон вимірювального каналу";
    private static final String ALLOWABLE_ERROR_OF_CHANNEL = "Допустима похибка вимірювального каналу";
    private static final String CHANNEL_IS_GOOD = "Канал придатний";
    private static final String CHANNEL_IS_BAD = "Канал не придатний";
    private static final String CHANNEL_SUITABILITY = "Придатність каналу";
    private static final String SENSOR = "Первинний вимірювальний пристрій";
    private static final String TYPE = "Тип";
    private static final String RANGE_OF_SENSOR = "Діапазон вимірювання ПВП";
    private static final String PARENT_NUMBER = "Заводський № ";
    private static final String OUT = "Вихід";
    private static final String ERROR_FORMULA = "Формула для розрахунку похибки";
    private static final String CONTROL_INFO = "Дані перевірки";
    private static final String THIS_DATE = "Дата останньої перевірки";
    private static final String FREQUENCY_CONTROL = "Міжконтрольний інтервал";
    private static final String PROTOCOL_NUMBER = "Номер протоколу";
    private static final String REFERENCE = "Довідка";
    private static String YEAR_WORD(double d){
        if (d == 0D){
            return "років";
        }else if (d > 0 && d < 1){
            return "року";
        }else if (d == 1D){
            return "рік";
        }else if (d > 1 && d < 5){
            return "роки";
        }else {
            return "років";
        }
    }
    /*
     * [0] = Название поля для сравнения;
     * [1] = Канал из списка;
     * [2] = Импортируемый канал;
     * [3] = Код канала;
     * [4] = Технологический номер канала;
     * [5] = Название канала;
     * [6] = Тип измерения канала;
     * [7] = Цех;
     * [8] = Участок;
     * [9] = Линия, секция и т.п;
     * [10] = Установка;
     * [11] = Диапазон канала;
     * [12] = Допустимая погрешность канала;
     * [13] = Пригодность канала;
     * [14] = Первичный измерительный прибор;
     * [15] = Тип ПИП;
     * [16] = Название ПИП
     * [17] = Диапазон ПИП;
     * [18] = Заводской номер ПИП;
     * [19] = Выходной сигнал ПИП;
     * [20] = Формула вычисления погрешности ПИП;
     * [21] = Данные о проверке;
     * [22] = Дата последней проверки;
     * [23] = Периодичность проверки;
     * [24] = Номер протокола;
     * [25] = Справка;
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
     * [12] = Название ПИП;
     * [13] = Диапазон ПИП;
     * [14] = Заводской номер ПИП;
     * [15] = Выходной сигнал ПИП;
     * [16] = Формула вычисления погрешности ПИП;
     * [17] = Дата последней проверки;
     * [18] = Периодичность проверки;
     * [19] = Номер протокола;
     * [20] = Справка;
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
        this.build();
    }

    private void createElements() {
        labels = new ButtonCell[26];
        oldInfo = new ButtonCell[21];
        newInfo = new ButtonCell[21];

        labels[0] = new ButtonCell(Color.WHITE, Color.BLACK, FIELD);
        labels[1] = new ButtonCell(Color.WHITE, Color.BLACK, CHANNEL_IN_LIST);
        labels[2] = new ButtonCell(Color.WHITE, Color.BLACK, IMPORTED_CHANNEL);

        if (old.getCode().equals(imported.getCode())){
            labels[3] = new ButtonCell(Color.GREEN.darker(), Color.WHITE, CODE);
            oldInfo[0] = new ButtonCell(Color.GREEN.darker(), Color.WHITE, old.getCode());
            newInfo[0] = new ButtonCell(Color.GREEN.darker(), Color.WHITE, imported.getCode());
        }else {
            labels[3] = new ButtonCell(Color.RED.darker(), Color.WHITE, CODE);
            oldInfo[0] = new ButtonCell(Color.RED.darker(), Color.WHITE, old.getCode());
            newInfo[0] = new ButtonCell(Color.RED.darker(), Color.WHITE, imported.getCode());
        }

        if (old.getTechnologyNumber().equals(imported.getTechnologyNumber())){
            labels[4] = new ButtonCell(Color.GREEN.darker(), Color.WHITE, TECHNOLOGY_NUMBER);
            oldInfo[1] = new ButtonCell(Color.GREEN.darker(), Color.WHITE, old.getTechnologyNumber());
            newInfo[1] = new ButtonCell(Color.GREEN.darker(), Color.WHITE, imported.getTechnologyNumber());
        }else {
            labels[4] = new ButtonCell(Color.RED.darker(), Color.WHITE, TECHNOLOGY_NUMBER);
            oldInfo[1] = new ButtonCell(Color.RED.darker(), Color.WHITE, old.getTechnologyNumber());
            newInfo[1] = new ButtonCell(Color.RED.darker(), Color.WHITE, imported.getTechnologyNumber());
        }

        if (old.getName().equals(imported.getName())){
            labels[5] = new ButtonCell(Color.GREEN.darker(), Color.WHITE, NAME);
            oldInfo[2] = new ButtonCell(Color.GREEN.darker(), Color.WHITE, old.getName());
            newInfo[2] = new ButtonCell(Color.GREEN.darker(), Color.WHITE, imported.getName());
        }else {
            labels[5] = new ButtonCell(Color.RED.darker(), Color.WHITE, NAME);
            oldInfo[2] = new ButtonCell(Color.RED.darker(), Color.WHITE, old.getName());
            newInfo[2] = new ButtonCell(Color.RED.darker(), Color.WHITE, imported.getName());
        }

        String vo;
        String vi;

        vo = old.getMeasurement().getValue();
        vi = imported.getMeasurement().getValue();
        String mo = old.getMeasurement().getName().concat(" [").concat(vo).concat("]");
        String mi = imported.getMeasurement().getName().concat("[").concat(vi).concat("]");
        if (old.getMeasurement().equals(imported.getMeasurement())){
            labels[6] = new ButtonCell(Color.GREEN.darker(), Color.WHITE, TYPE_OF_MEASUREMENT);
            oldInfo[3] = new ButtonCell(Color.GREEN.darker(), Color.WHITE, mo);
            newInfo[3] = new ButtonCell(Color.GREEN.darker(), Color.WHITE, mi);
        }else {
            labels[6] = new ButtonCell(Color.RED.darker(), Color.WHITE, TYPE_OF_MEASUREMENT);
            oldInfo[3] = new ButtonCell(Color.RED.darker(), Color.WHITE, mo);
            newInfo[3] = new ButtonCell(Color.RED.darker(), Color.WHITE, mi);
        }

        if (old.getDepartment().equals(imported.getDepartment())){
            labels[7] = new ButtonCell(Color.GREEN.darker(), Color.WHITE, DEPARTMENT);
            oldInfo[4] = new ButtonCell(Color.GREEN.darker(), Color.WHITE, old.getDepartment());
            newInfo[4] = new ButtonCell(Color.GREEN.darker(), Color.WHITE, imported.getDepartment());
        }else {
            labels[7] = new ButtonCell(Color.RED.darker(), Color.WHITE, DEPARTMENT);
            oldInfo[4] = new ButtonCell(Color.RED.darker(), Color.WHITE, old.getDepartment());
            newInfo[4] = new ButtonCell(Color.RED.darker(), Color.WHITE, imported.getDepartment());
        }

        if (old.getArea().equals(imported.getArea())){
            labels[8] = new ButtonCell(Color.GREEN.darker(), Color.WHITE, AREA);
            oldInfo[5] = new ButtonCell(Color.GREEN.darker(), Color.WHITE, old.getArea());
            newInfo[5] = new ButtonCell(Color.GREEN.darker(), Color.WHITE, imported.getArea());
        }else {
            labels[8] = new ButtonCell(Color.RED.darker(), Color.WHITE, AREA);
            oldInfo[5] = new ButtonCell(Color.RED.darker(), Color.WHITE, old.getArea());
            newInfo[5] = new ButtonCell(Color.RED.darker(), Color.WHITE, imported.getArea());
        }

        if (old.getProcess().equals(imported.getProcess())){
            labels[9] = new ButtonCell(Color.GREEN.darker(), Color.WHITE, PROCESS);
            oldInfo[6] = new ButtonCell(Color.GREEN.darker(), Color.WHITE, old.getProcess());
            newInfo[6] = new ButtonCell(Color.GREEN.darker(), Color.WHITE, imported.getProcess());
        }else {
            labels[9] = new ButtonCell(Color.RED.darker(), Color.WHITE, PROCESS);
            oldInfo[6] = new ButtonCell(Color.RED.darker(), Color.WHITE, old.getProcess());
            newInfo[6] = new ButtonCell(Color.RED.darker(), Color.WHITE, imported.getProcess());
        }

        if (old.getInstallation().equals(imported.getInstallation())){
            labels[10] = new ButtonCell(Color.GREEN.darker(), Color.WHITE, INSTALLATION);
            oldInfo[7] = new ButtonCell(Color.GREEN.darker(), Color.WHITE, old.getInstallation());
            newInfo[7] = new ButtonCell(Color.GREEN.darker(), Color.WHITE, imported.getInstallation());
        }else {
            labels[10] = new ButtonCell(Color.RED.darker(), Color.WHITE, INSTALLATION);
            oldInfo[7] = new ButtonCell(Color.RED.darker(), Color.WHITE, old.getInstallation());
            newInfo[7] = new ButtonCell(Color.RED.darker(), Color.WHITE, imported.getInstallation());
        }

        if (old.getRangeMin() == imported.getRangeMin() && old.getRangeMax() == imported.getRangeMax()){
            labels[11] = new ButtonCell(Color.GREEN.darker(), Color.WHITE, RANGE_OF_CHANNEL);
            String o = String.valueOf(old.getRangeMin()).concat(" - ").concat(String.valueOf(old.getRangeMax()));
            oldInfo[8] = new ButtonCell(Color.GREEN.darker(), Color.WHITE, o);
            String i = String.valueOf(imported.getRangeMin()).concat(" - ").concat(String.valueOf(imported.getRangeMax()));
            newInfo[8] = new ButtonCell(Color.GREEN.darker(), Color.WHITE, i);
        }else {
            labels[11] = new ButtonCell(Color.RED.darker(), Color.WHITE, RANGE_OF_CHANNEL);
            String o = String.valueOf(old.getRangeMin()).concat(" - ").concat(String.valueOf(old.getRangeMax()));
            oldInfo[8] = new ButtonCell(Color.RED.darker(), Color.WHITE, o);
            String i = String.valueOf(imported.getRangeMin()).concat(" - ").concat(String.valueOf(imported.getRangeMax()));
            newInfo[8] = new ButtonCell(Color.RED.darker(), Color.WHITE, i);
        }

        if (old.getAllowableErrorPercent() == imported.getAllowableErrorPercent() && old.getAllowableError() == imported.getAllowableError()){
            labels[12] = new ButtonCell(Color.GREEN.darker(), Color.WHITE, ALLOWABLE_ERROR_OF_CHANNEL);
            String o = String.valueOf(old.getAllowableErrorPercent()).concat("% або ").concat(String.valueOf(old.getAllowableError())).
                    concat(old.getMeasurement().getValue());
            oldInfo[9] = new ButtonCell(Color.GREEN.darker(), Color.WHITE, o);
            String i = String.valueOf(imported.getAllowableErrorPercent()).concat("% або ").concat(String.valueOf(imported.getAllowableError())).
                    concat(old.getMeasurement().getValue());
            newInfo[9] = new ButtonCell(Color.GREEN.darker(), Color.WHITE, i);
        }else {
            labels[12] = new ButtonCell(Color.RED.darker(), Color.WHITE, ALLOWABLE_ERROR_OF_CHANNEL);
            String o = String.valueOf(old.getAllowableErrorPercent()).concat("% або ").concat(String.valueOf(old.getAllowableError())).
                    concat(old.getMeasurement().getValue());
            oldInfo[9] = new ButtonCell(Color.RED.darker(), Color.WHITE, o);
            String i = String.valueOf(imported.getAllowableErrorPercent()).concat("% або ").concat(String.valueOf(imported.getAllowableError())).
                    concat(old.getMeasurement().getValue());
            newInfo[9] = new ButtonCell(Color.RED.darker(), Color.WHITE, i);
        }

        String so;
        String si;
        if (old.isSuitability()){
            so = CHANNEL_IS_GOOD;
        }else {
            so = CHANNEL_IS_BAD;
        }
        if (imported.isSuitability()){
            si = CHANNEL_IS_GOOD;
        }else {
            si = CHANNEL_IS_BAD;
        }
        if (old.isSuitability() == imported.isSuitability()){
            labels[13] = new ButtonCell(Color.GREEN.darker(), Color.WHITE, CHANNEL_SUITABILITY);
            oldInfo[10] = new ButtonCell(Color.GREEN.darker(), Color.WHITE, so);
            newInfo[10] = new ButtonCell(Color.GREEN.darker(), Color.WHITE, si);
        }else {
            labels[13] = new ButtonCell(Color.RED.darker(), Color.WHITE, CHANNEL_SUITABILITY);
            oldInfo[10] = new ButtonCell(Color.RED.darker(), Color.WHITE, so);
            newInfo[10] = new ButtonCell(Color.RED.darker(), Color.WHITE, si);
        }

        labels[14] = new ButtonCell(Color.WHITE, Color.BLACK, SENSOR);

        if (old.getSensor().getType().equals(imported.getSensor().getType())){
            labels[15] = new ButtonCell(Color.GREEN.darker(), Color.WHITE, TYPE);
            oldInfo[11] = new ButtonCell(Color.GREEN.darker(), Color.WHITE, old.getSensor().getType());
            newInfo[11] = new ButtonCell(Color.GREEN.darker(), Color.WHITE, imported.getSensor().getType());
        }else {
            labels[15] = new ButtonCell(Color.RED.darker(), Color.WHITE, TYPE);
            oldInfo[11] = new ButtonCell(Color.RED.darker(), Color.WHITE, old.getSensor().getType());
            newInfo[11] = new ButtonCell(Color.RED.darker(), Color.WHITE, imported.getSensor().getType());
        }

        if (old.getSensor().getName().equals(imported.getSensor().getName())){
            labels[16] = new ButtonCell(Color.GREEN.darker(), Color.WHITE, NAME);
            oldInfo[12] = new ButtonCell(Color.GREEN.darker(), Color.WHITE, old.getSensor().getName());
            newInfo[12] = new ButtonCell(Color.GREEN.darker(), Color.WHITE, imported.getSensor().getName());
        }else {
            labels[16] = new ButtonCell(Color.RED.darker(), Color.WHITE, NAME);
            oldInfo[12] = new ButtonCell(Color.RED.darker(), Color.WHITE, old.getSensor().getName());
            newInfo[12] = new ButtonCell(Color.RED.darker(), Color.WHITE, imported.getSensor().getName());
        }

        String ro = String.valueOf(old.getSensor().getRangeMin()).concat(" - ").concat(String.valueOf(old.getSensor().getRangeMax()));
        String ri = String.valueOf(imported.getSensor().getRangeMin()).concat(" - ").concat(String.valueOf(imported.getSensor().getRangeMax()));
        if (old.getSensor().getRangeMin() == imported.getSensor().getRangeMin() && old.getSensor().getRangeMax() == imported.getSensor().getRangeMax()){
            labels[17] = new ButtonCell(Color.GREEN.darker(), Color.WHITE, RANGE_OF_SENSOR);
            oldInfo[13] = new ButtonCell(Color.GREEN.darker(), Color.WHITE, ro);
            newInfo[13] = new ButtonCell(Color.GREEN.darker(), Color.WHITE, ri);
        }else {
            labels[17] = new ButtonCell(Color.RED.darker(), Color.WHITE, RANGE_OF_SENSOR);
            oldInfo[13] = new ButtonCell(Color.RED.darker(), Color.WHITE, ro);
            newInfo[13] = new ButtonCell(Color.RED.darker(), Color.WHITE, ri);
        }

        String no;
        String ni;
        if (old.getSensor().getNumber() == null || old.getSensor().getNumber().length() == 0) {
            no = " - ";
            if (imported.getSensor().getNumber().length() == 0){
                ni = " - ";
                labels[18] = new ButtonCell(Color.GREEN.darker(), Color.WHITE, PARENT_NUMBER);
                oldInfo[14] = new ButtonCell(Color.GREEN.darker(), Color.WHITE, no);
                newInfo[14] = new ButtonCell(Color.GREEN.darker(), Color.WHITE, ni);
            }else {
                ni = imported.getSensor().getNumber();
                labels[18] = new ButtonCell(Color.RED.darker(), Color.WHITE, PARENT_NUMBER);
                oldInfo[14] = new ButtonCell(Color.RED.darker(), Color.WHITE, no);
                newInfo[14] = new ButtonCell(Color.RED.darker(), Color.WHITE, ni);
            }
        }else {
            no = old.getSensor().getNumber();
            if (imported.getSensor().getNumber().length() == 0){
                ni = " - ";
                labels[18] = new ButtonCell(Color.RED.darker(), Color.WHITE, PARENT_NUMBER);
                oldInfo[14] = new ButtonCell(Color.RED.darker(), Color.WHITE, no);
                newInfo[14] = new ButtonCell(Color.RED.darker(), Color.WHITE, ni);
            }else if (old.getSensor().getNumber().equals(imported.getSensor().getNumber())){
                ni = imported.getSensor().getNumber();
                labels[18] = new ButtonCell(Color.GREEN.darker(), Color.WHITE, PARENT_NUMBER);
                oldInfo[14] = new ButtonCell(Color.GREEN.darker(), Color.WHITE, no);
                newInfo[14] = new ButtonCell(Color.GREEN.darker(), Color.WHITE, ni);
            }else{
                ni = imported.getSensor().getNumber();
                labels[18] = new ButtonCell(Color.RED.darker(), Color.WHITE, PARENT_NUMBER);
                oldInfo[14] = new ButtonCell(Color.RED.darker(), Color.WHITE, no);
                newInfo[14] = new ButtonCell(Color.RED.darker(), Color.WHITE, ni);
            }
        }

        if (old.getSensor().getValue().equals(imported.getSensor().getValue())){
            labels[19] = new ButtonCell(Color.GREEN.darker(), Color.WHITE, OUT);
            oldInfo[15] = new ButtonCell(Color.GREEN.darker(), Color.WHITE, old.getSensor().getValue());
            newInfo[15] = new ButtonCell(Color.GREEN.darker(), Color.WHITE, imported.getSensor().getValue());
        }else {
            labels[19] = new ButtonCell(Color.RED.darker(), Color.WHITE, OUT);
            oldInfo[15] = new ButtonCell(Color.RED.darker(), Color.WHITE, old.getSensor().getValue());
            newInfo[15] = new ButtonCell(Color.RED.darker(), Color.WHITE, imported.getSensor().getValue());
        }

        if (old.getSensor().getErrorFormula().equals(imported.getSensor().getErrorFormula())){
            labels[20] = new ButtonCell(Color.GREEN.darker(), Color.WHITE, ERROR_FORMULA);
            oldInfo[16] = new ButtonCell(Color.GREEN.darker(), Color.WHITE, old.getSensor().getErrorFormula());
            newInfo[16] = new ButtonCell(Color.GREEN.darker(), Color.WHITE, imported.getSensor().getErrorFormula());
        }else {
            labels[20] = new ButtonCell(Color.RED.darker(), Color.WHITE, ERROR_FORMULA);
            oldInfo[16] = new ButtonCell(Color.RED.darker(), Color.WHITE, old.getSensor().getErrorFormula());
            newInfo[16] = new ButtonCell(Color.RED.darker(), Color.WHITE, imported.getSensor().getErrorFormula());
        }

        labels[21] = new ButtonCell(Color.WHITE, Color.BLACK, CONTROL_INFO);

        if (VariableConverter.dateToString(old.getDate()).equals(VariableConverter.dateToString(imported.getDate()))){
            labels[22] = new ButtonCell(Color.GREEN.darker(), Color.WHITE, THIS_DATE);
            oldInfo[17] = new ButtonCell(Color.GREEN.darker(), Color.WHITE, VariableConverter.dateToString(old.getDate()));
            newInfo[17] = new ButtonCell(Color.GREEN.darker(), Color.WHITE, VariableConverter.dateToString(imported.getDate()));
        }else {
            labels[22] = new ButtonCell(Color.RED.darker(), Color.WHITE, THIS_DATE);
            oldInfo[17] = new ButtonCell(Color.RED.darker(), Color.WHITE, VariableConverter.dateToString(old.getDate()));
            newInfo[17] = new ButtonCell(Color.RED.darker(), Color.WHITE, VariableConverter.dateToString(imported.getDate()));
        }

        String fo = String.format(Locale.ENGLISH, "%.0f", old.getFrequency()).concat(YEAR_WORD(old.getFrequency()));
        String fi = String.format(Locale.ENGLISH, "%.0f", imported.getFrequency()).concat(YEAR_WORD(imported.getFrequency()));
        if (old.getFrequency() == imported.getFrequency()){
            labels[23] = new ButtonCell(Color.GREEN.darker(), Color.WHITE, FREQUENCY_CONTROL);
            oldInfo[18] = new ButtonCell(Color.GREEN.darker(), Color.WHITE, fo);
            newInfo[18] = new ButtonCell(Color.GREEN.darker(), Color.WHITE, fi);
        }else {
            labels[23] = new ButtonCell(Color.RED.darker(), Color.WHITE, FREQUENCY_CONTROL);
            oldInfo[18] = new ButtonCell(Color.RED.darker(), Color.WHITE, fo);
            newInfo[18] = new ButtonCell(Color.RED.darker(), Color.WHITE, fi);
        }

        if (old.getNumberOfProtocol().equals(imported.getNumberOfProtocol())){
            labels[24] = new ButtonCell(Color.GREEN.darker(), Color.WHITE, PROTOCOL_NUMBER);
            oldInfo[19] = new ButtonCell(Color.GREEN.darker(), Color.WHITE, old.getNumberOfProtocol());
            newInfo[19] = new ButtonCell(Color.GREEN.darker(), Color.WHITE, imported.getNumberOfProtocol());
        }else {
            labels[24] = new ButtonCell(Color.RED.darker(), Color.WHITE, PROTOCOL_NUMBER);
            oldInfo[19] = new ButtonCell(Color.RED.darker(), Color.WHITE, old.getNumberOfProtocol());
            newInfo[19] = new ButtonCell(Color.RED.darker(), Color.WHITE, imported.getNumberOfProtocol());
        }

        String refo;
        String refi;
        if (old.getReference() == null) {
            refo = " - ";
            if (imported.getReference() == null){
                refi = " - ";
                labels[25] = new ButtonCell(Color.GREEN.darker(), Color.WHITE, REFERENCE);
                oldInfo[20] = new ButtonCell(Color.GREEN.darker(), Color.WHITE, refo);
                newInfo[20] = new ButtonCell(Color.GREEN.darker(), Color.WHITE, refi);
            }else {
                refi = imported.getReference();
                labels[25] = new ButtonCell(Color.RED.darker(), Color.WHITE, REFERENCE);
                oldInfo[20] = new ButtonCell(Color.RED.darker(), Color.WHITE, refo);
                newInfo[20] = new ButtonCell(Color.RED.darker(), Color.WHITE, refi);
            }
        }else {
            refo = old.getReference();
            if (imported.getReference() == null){
                refi = " - ";
                labels[25] = new ButtonCell(Color.RED.darker(), Color.WHITE, REFERENCE);
                oldInfo[20] = new ButtonCell(Color.RED.darker(), Color.WHITE, refo);
                newInfo[20] = new ButtonCell(Color.RED.darker(), Color.WHITE, refi);
            }else if (old.getReference().equals(imported.getReference())){
                refi = imported.getReference();
                labels[25] = new ButtonCell(Color.GREEN.darker(), Color.WHITE, REFERENCE);
                oldInfo[20] = new ButtonCell(Color.GREEN.darker(), Color.WHITE, refo);
                newInfo[20] = new ButtonCell(Color.GREEN.darker(), Color.WHITE, refi);
            }else{
                refi = imported.getReference();
                labels[25] = new ButtonCell(Color.RED.darker(), Color.WHITE, REFERENCE);
                oldInfo[20] = new ButtonCell(Color.RED.darker(), Color.WHITE, refo);
                newInfo[20] = new ButtonCell(Color.RED.darker(), Color.WHITE, refi);
            }
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

        this.add(this.labels[19], new Cell(0, 17));
        this.add(this.oldInfo[15], new Cell(1, 17));
        this.add(this.newInfo[15], new Cell(2, 17));

        this.add(this.labels[20], new Cell(0, 18));
        this.add(this.oldInfo[16], new Cell(1, 18));
        this.add(this.newInfo[16], new Cell(2, 18));

        this.add(labels[21], new Cell(19));

        this.add(this.labels[22], new Cell(0, 20));
        this.add(this.oldInfo[17], new Cell(1, 20));
        this.add(this.newInfo[17], new Cell(2, 20));

        this.add(this.labels[23], new Cell(0, 21));
        this.add(this.oldInfo[18], new Cell(1, 21));
        this.add(this.newInfo[18], new Cell(2, 21));

        this.add(this.labels[24], new Cell(0, 22));
        this.add(this.oldInfo[19], new Cell(1, 22));
        this.add(this.newInfo[19], new Cell(2, 22));

        this.add(this.labels[25], new Cell(0, 23));
        this.add(this.oldInfo[20], new Cell(1, 23));
        this.add(this.newInfo[20], new Cell(2, 23));
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