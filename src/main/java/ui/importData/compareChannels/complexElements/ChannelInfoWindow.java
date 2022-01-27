package ui.importData.compareChannels.complexElements;

import converters.VariableConverter;
import model.Channel;
import ui.model.ButtonCell;

import javax.swing.*;
import java.awt.*;

public class ChannelInfoWindow extends JWindow {
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
    private static final String CODE = "Код:";
    private static final String NAME = "Назва:";
    private static final String TECHNOLOGY_NUMBER = "Технологічний номер:";
    private static final String MEASUREMENT = "Вид вимірювання:";
    private static final String RANGE = "Діапазон:";
    private static final String ALLOWABLE_ERROR_OF_CHANNEL = "Допустима похибка:";
    private static final String PATH = "Розташування:";
    private static final String DATE = "Дата останньої перевірки:";
    private static final String PROTOCOL_NUMBER = "Номер протоколу:";
    private static final String REFERENCE_NUMBER = "Номер довідки:";
    private static final String FREQUENCY_CONTROL = "Міжконтрольний інтервал:";
    private static final String SENSOR = "ПВП:";
    private static final String TYPE = "Тип:";
    private static final String NUMBER = "Номер:";
    private static final String VALUE = "Вихідна величина:";
    private static final String ERROR_FORMULA = "Формула розрахунку допустимої похибки:";
    private static final String SUITABILITY = "Придатний";
    private static final String NO_SUITABILITY = "Не придатний";
    private final String TITLE;

    private final Channel channel;

    private ButtonCell title, sensor;

    private ButtonCell codeLabel, channelNameLabel, technologyNumberLabel, measurementLabel, channelRangeLabel, allowableErrorLabel, pathLabel,
                        dateLabel, protocolLabel, referenceLabel, frequencyLabel;
    private ButtonCell sensorNameLabel, sensorTypeLabel, sensorRangeLabel, sensorNumberLabel, sensorValueLabel, sensorErrorFormulaLabel;
    private ButtonCell code, channelName, technologyNumber, measurement, channelRange, allowableError, department, area, process, installation,
                        date, protocol, reference, frequency;
    private ButtonCell sensorName, sensorType, sensorRange, sensorNumber, sensorValue, sensorErrorFormula;

    public ChannelInfoWindow(String title, JDialog parent, Channel channel){
        super(parent);
        this.TITLE = title;
        this.channel = channel;

        this.createElements();
        this.build();
    }

    private void createElements(){
        this.title = new ButtonCell(Color.YELLOW, Color.BLACK, this.TITLE);
        String suitability = NO_SUITABILITY;
        if (this.channel.isSuitability()) suitability = SUITABILITY;
        this.title.setToolTipText(suitability);

        this.sensor = new ButtonCell(Color.YELLOW, Color.BLACK, SENSOR);

        this.codeLabel = new ButtonCell(true, CODE);
        this.channelNameLabel = new ButtonCell(true, NAME);
        this.technologyNumberLabel = new ButtonCell(true, TECHNOLOGY_NUMBER);
        this.measurementLabel = new ButtonCell(true, MEASUREMENT);
        this.channelRangeLabel = new ButtonCell(true, RANGE);
        this.allowableErrorLabel = new ButtonCell(true, ALLOWABLE_ERROR_OF_CHANNEL);
        this.pathLabel = new ButtonCell(true, PATH);
        this.dateLabel = new ButtonCell(true, DATE);
        this.protocolLabel = new ButtonCell(true, PROTOCOL_NUMBER);
        this.referenceLabel = new ButtonCell(true, REFERENCE_NUMBER);
        this.frequencyLabel = new ButtonCell(true, FREQUENCY_CONTROL);

        this.sensorNameLabel = new ButtonCell(true, NAME);
        this.sensorTypeLabel = new ButtonCell(true, TYPE);
        this.sensorRangeLabel = new ButtonCell(true, RANGE);
        this.sensorValueLabel = new ButtonCell(true, VALUE);
        this.sensorNumberLabel = new ButtonCell(true, NUMBER);
        this.sensorErrorFormulaLabel = new ButtonCell(true, ERROR_FORMULA);

        this.code = new ButtonCell(false, this.channel.getCode());
        this.codeLabel.setToolTipText(this.channel.getCode());
        this.code.setToolTipText(this.channel.getCode());

        this.channelName = new ButtonCell(false, this.channel.getName());
        this.channelNameLabel.setToolTipText(this.channel.getName());
        this.channelName.setToolTipText(this.channel.getName());

        this.technologyNumber = new ButtonCell(false, this.channel.getTechnologyNumber());
        this.technologyNumberLabel.setToolTipText(this.channel.getTechnologyNumber());
        this.technologyNumber.setToolTipText(this.channel.getTechnologyNumber());

        String m = this.channel.getMeasurement().getName() + " | " + this.channel.getMeasurement().getValue();
        this.measurement = new ButtonCell(false, m);
        this.measurementLabel.setToolTipText(m);
        this.measurement.setToolTipText(m);

        String rc = this.channel.getRangeMin() + " ... " + this.channel.getRangeMax();
        this.channelRange = new ButtonCell(false, rc);
        this.channelRangeLabel.setToolTipText(rc);
        this.channelRange.setToolTipText(rc);

        String e = this.channel.getAllowableErrorPercent() + " %  або " +
                this.channel.getAllowableError() + this.channel.getMeasurement().getValue();
        this.allowableError = new ButtonCell(false, e);
        this.allowableErrorLabel.setToolTipText(e);
        this.allowableError.setToolTipText(e);

        String d = " - ";
        if (this.channel.getDepartment().length() > 0) d = this.channel.getDepartment();
        this.department = new ButtonCell(false, d);
        this.department.setToolTipText(d);

        String a = " - ";
        if (this.channel.getArea().length() > 0) a = this.channel.getArea();
        this.area = new ButtonCell(false, a);
        this.area.setToolTipText(a);

        String p = " - ";
        if (this.channel.getProcess().length() > 0) p = this.channel.getProcess();
        this.process = new ButtonCell(false, p);
        this.process.setToolTipText(p);

        String i = " - ";
        if (this.channel.getInstallation().length() > 0) i = this.channel.getInstallation();
        this.installation = new ButtonCell(false, i);
        this.installation.setToolTipText(i);

        this.date = new ButtonCell(false, VariableConverter.dateToString(this.channel.getDate()));
        this.dateLabel.setToolTipText(VariableConverter.dateToString(this.channel.getDate()));
        this.date.setToolTipText(VariableConverter.dateToString(this.channel.getDate()));

        String pn = " - ";
        if (this.channel.getNumberOfProtocol().length() > 0) pn = this.channel.getNumberOfProtocol();
        this.protocol = new ButtonCell(false, pn);
        this.protocolLabel.setToolTipText(pn);
        this.protocol.setToolTipText(pn);

        String ref = " - ";
        if (this.channel.getReference().length() > 0) ref = this.channel.getReference();
        this.reference = new ButtonCell(false, ref);
        this.referenceLabel.setToolTipText(ref);
        this.reference.setToolTipText(ref);

        String f = this.channel.getFrequency() + " " + YEAR_WORD(this.channel.getFrequency());
        this.frequency = new ButtonCell(false, f);
        this.frequencyLabel.setToolTipText(f);
        this.frequency.setToolTipText(f);

        this.sensorName = new ButtonCell(false, this.channel.getSensor().getName());
        this.sensorNameLabel.setToolTipText(this.channel.getSensor().getName());
        this.sensorName.setToolTipText(this.channel.getSensor().getName());

        this.sensorType = new ButtonCell(false, this.channel.getSensor().getType());
        this.sensorTypeLabel.setToolTipText(this.channel.getSensor().getType());
        this.sensorType.setToolTipText(this.channel.getSensor().getType());

        String r = " - ";
        if (this.channel.getSensor().getRange() > 0) r = this.channel.getSensor().getRangeMin() + " ... " + this.channel.getSensor().getRangeMax();
        this.sensorRange = new ButtonCell(false, r);
        this.sensorRangeLabel.setToolTipText(r);
        this.sensorRange.setToolTipText(r);

        String v = this.channel.getSensor().getValue();
        if (v.length() == 0) v = " - ";
        this.sensorValue = new ButtonCell(false, v);
        this.sensorValueLabel.setToolTipText(v);
        this.sensorValue.setToolTipText(v);

        String n = this.channel.getSensor().getNumber();
        if (n.length() == 0) n = " - ";
        this.sensorNumber = new ButtonCell(false, n);
        this.sensorNumberLabel.setToolTipText(n);
        this.sensorNumber.setToolTipText(n);

        this.sensorErrorFormula = new ButtonCell(false, this.channel.getSensor().getErrorFormula());
        this.sensorErrorFormulaLabel.setToolTipText(this.channel.getSensor().getErrorFormula());
        this.sensorErrorFormula.setToolTipText(this.channel.getSensor().getErrorFormula());
    }

    private void build(){
        this.setSize(300,400);
        JScrollPane scrollPane = new JScrollPane(new MainPanel());
        int middleScroll = scrollPane.getHorizontalScrollBar().getMaximum() / 2;
        scrollPane.getHorizontalScrollBar().setValue(middleScroll);
        this.setContentPane(scrollPane);
    }

    private class MainPanel extends JPanel {
        protected MainPanel(){
            super(new GridBagLayout());

            this.add(title, new Cell(0));
            this.add(codeLabel, new Cell(1));
            this.add(code, new Cell(2));
            this.add(channelNameLabel, new Cell(3));
            this.add(channelName, new Cell(4));
            this.add(technologyNumberLabel, new Cell(5));
            this.add(technologyNumber, new Cell(6));
            this.add(measurementLabel, new Cell(7));
            this.add(measurement, new Cell(8));
            this.add(channelRangeLabel, new Cell(9));
            this.add(channelRange, new Cell(10));
            this.add(allowableErrorLabel, new Cell(11));
            this.add(allowableError, new Cell(12));
            this.add(pathLabel, new Cell(13));
            this.add(department, new Cell(14));
            this.add(area, new Cell(15));
            this.add(process, new Cell(16));
            this.add(installation, new Cell(17));
            this.add(dateLabel, new Cell(18));
            this.add(date, new Cell(19));
            this.add(protocolLabel, new Cell(20));
            this.add(protocol, new Cell(21));
            this.add(referenceLabel, new Cell(22));
            this.add(reference, new Cell(23));
            this.add(frequencyLabel, new Cell(24));
            this.add(frequency, new Cell(25));
            this.add(sensor, new Cell(26));
            this.add(sensorNameLabel, new Cell(27));
            this.add(sensorName, new Cell(28));
            this.add(sensorTypeLabel, new Cell(29));
            this.add(sensorType, new Cell(30));
            this.add(sensorRangeLabel, new Cell(31));
            this.add(sensorRange, new Cell(32));
            this.add(sensorValueLabel, new Cell(33));
            this.add(sensorValue, new Cell(34));
            this.add(sensorNumberLabel, new Cell(35));
            this.add(sensorNumber, new Cell(36));
            this.add(sensorErrorFormulaLabel, new Cell(37));
            this.add(sensorErrorFormula, new Cell(38));
        }

        private class Cell extends GridBagConstraints {
            protected Cell(int y){
                super();
                this.fill = BOTH;
                this.weightx = 1.0;

                this.gridy = y;
            }
        }
    }
}