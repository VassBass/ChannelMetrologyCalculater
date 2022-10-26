package ui.channelInfo;

import application.Application;
import converters.ConverterUI;
import converters.VariableConverter;
import developer.calculating.OS_Chooser;
import model.Channel;
import model.Measurement;
import model.Sensor;
import repository.ChannelRepository;
import repository.impl.ChannelRepositorySQLite;
import service.ChannelSorter;
import ui.channelInfo.button.*;
import ui.channelInfo.panel.*;
import ui.mainScreen.MainScreen;
import ui.specialCharacters.SpecialCharactersPanel;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Optional;

public class DialogChannel extends JDialog {
    private static final String INFORMATION_ABOUT_CHANNEL = "Інформація вимірювального каналу";

    public final Channel oldChannel;

    private final ChannelRepository channelRepository = ChannelRepositorySQLite.getInstance();

    @Nonnull public final PanelCode panelCode;
    @Nonnull public final PanelName panelName;
    @Nonnull public final PanelMeasurement panelMeasurement;
    @Nonnull public final PanelTechnologyNumber panelTechnologyNumber;
    @Nonnull public final PanelDate panelDate;
    @Nonnull public final PanelProtocolNumber panelProtocolNumber;
    @Nonnull public final PanelFrequency panelFrequency;
    @Nonnull public final PanelPath panelPath;
    @Nonnull public final PanelSensor panelSensor;
    @Nonnull public final PanelChannelRange panelChannelRange;
    @Nonnull public final PanelAllowableError panelAllowableError;

    @Nonnull public final SpecialCharactersPanel specialCharactersPanel;

    public final ButtonSave buttonSave;
    public final ButtonSaveAndCalculate buttonSaveAndCalculate;
    public final ButtonRemove buttonRemove;
    public final ButtonReset buttonReset;
    public final ButtonClose buttonClose;

    public DialogChannel(@Nullable Channel oldChannel){
        super(MainScreen.getInstance(), INFORMATION_ABOUT_CHANNEL, true);
        this.oldChannel = oldChannel;

        panelCode = new PanelCode(this);
        panelName = new PanelName(this);
        panelMeasurement = new PanelMeasurement(this);
        panelTechnologyNumber = new PanelTechnologyNumber(this);
        panelDate = new PanelDate(this);
        panelProtocolNumber = new PanelProtocolNumber(this);
        panelFrequency = new PanelFrequency(this);
        panelPath = new PanelPath(this);
        panelSensor = new PanelSensor(this);
        panelChannelRange = new PanelChannelRange(this);
        panelAllowableError = new PanelAllowableError(this);

        specialCharactersPanel = new SpecialCharactersPanel();

        buttonSave = new ButtonSave(this);
        buttonSaveAndCalculate = new ButtonSaveAndCalculate(this);
        buttonRemove = new ButtonRemove(this);
        buttonReset = new ButtonReset(this);
        buttonClose = new ButtonClose(this);

        this.addKeyListener();
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        int width = Math.min(Application.sizeOfScreen.width, 850);
        int height = Math.min(Application.sizeOfScreen.height, 850);
        this.setSize(width, height);

        this.setChannelInfo(oldChannel);
        this.setLocation(ConverterUI.POINT_CENTER(MainScreen.getInstance(),this));
        this.setContentPane(new MainPanel());
    }

    private void addKeyListener() {
        panelCode.addKeyListener(keyListener);
        panelName.addKeyListener(keyListener);
        panelProtocolNumber.addKeyListener(keyListener);
        panelTechnologyNumber.addKeyListener(keyListener);
        panelMeasurement.addKeyListener(keyListener);
        panelDate.addKeyListener(keyListener);
        panelFrequency.addKeyListener(keyListener);
        panelPath.addKeyListener(keyListener);
        panelSensor.addKeyListener(keyListener);
        panelChannelRange.addKeyListener(keyListener);
        panelAllowableError.addKeyListener(keyListener);

        specialCharactersPanel.addKeyListener(keyListener);

        buttonClose.addKeyListener(keyListener);
        buttonSave.addKeyListener(this.keyListener);
        buttonSaveAndCalculate.addKeyListener(this.keyListener);
        buttonReset.addKeyListener(this.keyListener);
        buttonRemove.addKeyListener(this.keyListener);
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean isFieldsAreFilled(){
        boolean filledCode = panelCode.isCodeAvailable(oldChannel);
        boolean filledName = panelName.isNameAvailable();
        boolean filledTechnologyNumber = panelTechnologyNumber.isTechnologyNumberAvailable();
        boolean filledRanges = panelChannelRange.isRangeAvailable();

        boolean filled = filledCode && filledName && filledTechnologyNumber && filledRanges;

        if (!filled) this.refresh();
        return filled;
    }

    public void setChannelInfo(Channel channel) {
        if (channel == null) {
            panelMeasurement.updateMeasurementName(Measurement.TEMPERATURE);
            panelDate.updateDate(Calendar.getInstance());
            panelFrequency.updateFrequency(2.0);
            panelFrequency.updateNextDate(Calendar.getInstance());
            panelSensor.updateMeasurementName(Measurement.TEMPERATURE);

            buttonReset.setEnabled(false);
            buttonRemove.setEnabled(false);
        } else {
            Measurement measurement = channel.getMeasurement();

            panelCode.setCode(channel.getCode());
            panelName.setChannelName(channel.getName());
            panelMeasurement.updateMeasurement(measurement);
            panelTechnologyNumber.setTechnologyNumber(channel.getTechnologyNumber());
            panelDate.updateDate(VariableConverter.stringToDate(channel.getDate()));
            panelProtocolNumber.setProtocolNumber(channel.getNumberOfProtocol());
            panelFrequency.updateFrequency(channel.getFrequency());
            panelPath.updateDepartment(channel.getDepartment());
            panelPath.updateArea(channel.getArea());
            panelPath.updateProcess(channel.getProcess());
            panelPath.updateInstallation(channel.getInstallation());
            panelSensor.updateMeasurementName(measurement.getName());
            panelSensor.updateSensor(channel.getSensor());
            panelChannelRange.updateRange(channel.getRangeMin(), channel.getRangeMax());
            panelChannelRange.updateMeasurementValue(measurement.getValue());
            panelAllowableError.updateError(channel.getAllowableErrorPercent(), true, channel._getRange());
            panelAllowableError.updateMeasurementValue(measurement.getValue());

            buttonReset.setEnabled(true);
            buttonRemove.setEnabled(true);
        }
    }

    public Optional<Channel> getChannel(){
        Optional<Measurement> m = panelMeasurement.getMeasurement();
        Optional<String> d = panelPath.getDepartment();
        Optional<String> a = panelPath.getArea();
        Optional<String> p = panelPath.getProcess();
        Optional<String> i = panelPath.getInstallation();
        Optional<Sensor> s = panelSensor.getSelectedSensor();

        if (m.isPresent() &&
                d.isPresent() &&
                a.isPresent() &&
                p.isPresent() &&
                i.isPresent() &&
                s.isPresent()
        ) {
            Channel channel = new Channel();
            channel.setCode(panelCode.getCode());
            channel.setName(panelName.getChannelName());
            channel.setMeasurement(m.get());
            channel.setDepartment(d.get());
            channel.setArea(a.get());
            channel.setProcess(p.get());
            channel.setInstallation(i.get());
            channel.setDate(panelDate.getDate());
            channel.setFrequency(panelFrequency.getFrequency());
            channel.setTechnologyNumber(panelTechnologyNumber.getTechnologyNumber());

            Sensor sensor = s.get();
            sensor.setRange(
                    panelSensor.panelSensorRange.getRangeMin(),
                    panelSensor.panelSensorRange.getRangeMax()
            );
            sensor.setValue(panelSensor.panelSensorRange.getValue());
            sensor.setNumber(panelSensor.getSerialNumber());
            channel.setSensor(sensor);

            channel.setNumberOfProtocol(panelProtocolNumber.getProtocolNumber());
            channel.setRange(panelChannelRange.getRangeMin(), panelChannelRange.getRangeMax());
            channel.setAllowableError(
                    panelAllowableError.getAllowableErrorPercent(),
                    panelAllowableError.getAllowableErrorNumber()
            );

            if (oldChannel != null) {
                channel.setReference(oldChannel.getReference());
                channel.setSuitability(oldChannel.isSuitability());
            }

            return Optional.of(channel);
        } else return Optional.empty();
    }

    public final KeyListener keyListener = new KeyAdapter() {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()){
                case KeyEvent.VK_ESCAPE:
                    if (e.isAltDown()) buttonClose.doClick();
                    break;
                case KeyEvent.VK_ENTER:
                    if (e.isAltDown() && !e.isControlDown()) buttonSave.doClick();
                    if (e.isControlDown() && !e.isAltDown()) buttonSaveAndCalculate.doClick();
                    if (e.isAltDown() && e.isControlDown()) {
                        specialCharactersPanel.setFieldForInsert(null);
                        if (!isFieldsAreFilled()) return;

                        Application.putHint(panelName.getChannelName());
                        dispose();

                        Optional<Channel> c = getChannel();
                        if (c.isPresent()) {
                            Channel channel = c.get();
                            if (oldChannel == null) {
                                channelRepository.add(channel);
                            } else {
                                channelRepository.set(oldChannel, channel);
                            }
                            if (ChannelSorter.getInstance().isOn()) {
                                MainScreen.getInstance().setChannelsList(ChannelSorter.getInstance().getCurrent());
                            } else {
                                MainScreen.getInstance().setChannelsList(new ArrayList<>(channelRepository.getAll()));
                            }
                            EventQueue.invokeLater(() -> new OS_Chooser(MainScreen.getInstance(), channel).setVisible(true));
                        }
                    }
                    break;
            }
        }
    };

    private void refresh(){
        this.setVisible(false);
        this.setVisible(true);
    }

    private class MainPanel extends JPanel {

        protected MainPanel() {
            super(new GridBagLayout());
            this.setBackground(Color.WHITE);

            this.add(panelCode, new Cell(0,0));
            this.add(specialCharactersPanel, new Cell(2,0,5));
            this.add(panelName, new Cell(0,1));
            this.add(panelMeasurement, new Cell(0,2));
            this.add(panelTechnologyNumber, new Cell(0,3));
            this.add(panelDate, new Cell(0,4));
            this.add(panelProtocolNumber, new Cell(0,5));
            this.add(panelFrequency, new Cell(0,6));
            this.add(panelPath, new Cell(0,7));
            this.add(panelSensor, new Cell(0,8));
            this.add(panelChannelRange, new Cell(0,9));
            this.add(panelAllowableError, new Cell(0,10));

            Cell buttonCell = new Cell(0,11,new Insets(20,0,0,0), 3);
            buttonCell.fill = Cell.NONE;
            this.add(buttonSaveAndCalculate, buttonCell);

            JPanel buttonsPanel = new JPanel();
            buttonsPanel.setBackground(Color.WHITE);
            buttonsPanel.add(buttonClose);
            buttonsPanel.add(buttonRemove);
            buttonsPanel.add(buttonReset);
            buttonsPanel.add(buttonSave);
            this.add(buttonsPanel, new Cell(0, 12, new Insets(10, 0, 20, 0), 3));
        }

        private class Cell extends GridBagConstraints {

            protected Cell (int x, int y) {
                super();

                this.weightx = 0.95;
                this.fill = HORIZONTAL;
                this.insets = new Insets(5,5,5,5);

                this.gridx = x;
                this.gridy = y;
            }

            protected Cell (int x, int y, Insets insets, int width) {
                this.weightx = 1.0;
                this.fill = HORIZONTAL;
                this.insets = insets;

                this.gridwidth = width;
                this.gridx = x;
                this.gridy = y;
            }

            Cell (int x, int y, int height){
                super();

                this.weightx = 0.05;
                this.fill = HORIZONTAL;
                this.insets = new Insets(5,5,5,5);

                this.gridx = x;
                this.gridy = y;
                this.gridheight = height;
            }
        }
    }
}