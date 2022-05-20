package ui.channelInfo;

import application.Application;
import backgroundTasks.CheckChannel;
import converters.ConverterUI;
import converters.VariableConverter;
import developer.calculating.OS_Chooser;
import model.Channel;
import model.Measurement;
import model.Sensor;
import ui.calculate.start.CalculateStartDialog;
import ui.channelInfo.complexElements.*;
import ui.mainScreen.MainScreen;
import ui.model.DefaultButton;
import ui.specialCharacters.SpecialCharactersPanel;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class DialogChannel extends JDialog {
    private static final String INFORMATION_ABOUT_CHANNEL = "Інформація вимірювального каналу";
    private static final String CODE = "*Код";
    private static final String NAME = "*Назва";
    private static final String TECHNOLOGY_NUMBER = "*Технологічний номер";
    private static final String PROTOCOL_NUMBER = "Номер протоколу";
    private static final String CLOSE = "Закрити (Alt + Esc)";
    private static final String SAVE = "Зберегти (Alt + Enter)";
    private static final String RANGE_OF_CHANNEL = "Діапазон вимірювального каналу";
    private static final String RANGE_OF_SENSOR = "Діапазон вимірювання ПВП";
    private static final String SET_RANGE_LIKE_CHANNEL = "Однакові діапазони";
    private static final String INSERT = "Вставка";
    private static final String SEARCH = "Пошук";
    private static final String SAVE_AND_CALCULATE = "Зберегти та розрахувати (Ctrl + Enter)";
    private static final String RESET = "Скинути";
    private static final String REMOVE = "Видалити";

    private final MainScreen parent;
    private MainPanel mainPanel;

    private JTextField userCode;
    private JTextField userName;
    private JTextField userTechnologyNumber;
    private JTextField userProtocolNumber;

    private final TitledBorder codeBorder = BorderFactory.createTitledBorder(CODE);
    private final TitledBorder nameBorder = BorderFactory.createTitledBorder(NAME);
    private final TitledBorder technologyNumberBorder = BorderFactory.createTitledBorder(TECHNOLOGY_NUMBER);
    private final TitledBorder protocolNumberBorder = BorderFactory.createTitledBorder(PROTOCOL_NUMBER);
    private final TitledBorder sensorRangeBorder = BorderFactory.createTitledBorder(RANGE_OF_SENSOR);

    public DialogChannel_measurementPanel measurementPanel;
    public DialogChannel_datePanel datePanel;
    public DialogChannel_frequencyPanel frequencyPanel;
    public DialogChannel_pathPanel pathPanel;
    public DialogChannel_sensorPanel sensorPanel;
    public DialogChannel_rangePanel rangePanel;
    public DialogChannel_allowableErrorPanel allowableErrorPanel;
    public DialogChannel_sensorRangePanel sensorRangePanel;
    private SpecialCharactersPanel specialCharactersPanel;

    public JCheckBox rangeLikeChannel;

    private JButton negativeButton, positiveButton, saveAndCalculateButton, resetButton, removeButton;

    public final Channel oldChannel;

    public DialogChannel(MainScreen parent, Channel oldChannel){
        super(parent, INFORMATION_ABOUT_CHANNEL, true);
        this.parent = parent;
        this.oldChannel = oldChannel;

        this.createPrimitiveElements();
        this.createComplexElements();
        this.build();
        this.setReactions();
    }

    private void createPrimitiveElements(){
        this.userCode = new JTextField(10);
        JPopupMenu codePopupMenu = new JPopupMenu(SEARCH);
        JMenuItem check = new JMenuItem(SEARCH);
        check.addActionListener(this.clickCheck);
        codePopupMenu.add(check);
        this.userCode.setComponentPopupMenu(codePopupMenu);
        this.userCode.setBorder(codeBorder);

        this.userName = new JTextField(10);
        JPopupMenu namePopupMenu = new JPopupMenu(INSERT);
        this.userName.setComponentPopupMenu(namePopupMenu);
        String[] hints = Application.getHints();
        for (String hint : hints) {
            if (hint == null) break;

            JMenuItem type = new JMenuItem(hint);
            type.addActionListener(this.clickPaste);
            namePopupMenu.add(type);
        }
        this.userName.setBorder(nameBorder);

        this.userTechnologyNumber = new JTextField(10);
        this.userTechnologyNumber.setBorder(technologyNumberBorder);
        this.userProtocolNumber = new JTextField(10);
        this.userProtocolNumber.setBorder(protocolNumberBorder);

        this.rangeLikeChannel = new JCheckBox(SET_RANGE_LIKE_CHANNEL);
        this.rangeLikeChannel.setBackground(Color.WHITE);

        this.negativeButton = new DefaultButton(CLOSE);
        this.positiveButton = new DefaultButton(SAVE);
        this.saveAndCalculateButton = new DefaultButton(SAVE_AND_CALCULATE);
        this.resetButton = new DefaultButton(RESET);
        this.removeButton = new DefaultButton(REMOVE);
    }

    private void createComplexElements(){
        this.measurementPanel = new DialogChannel_measurementPanel(this);
        this.datePanel = new DialogChannel_datePanel(this);
        this.frequencyPanel = new DialogChannel_frequencyPanel(this);
        this.pathPanel = new DialogChannel_pathPanel(this);
        this.sensorPanel = new DialogChannel_sensorPanel(this);
        this.rangePanel = new DialogChannel_rangePanel(this);
        this.allowableErrorPanel = new DialogChannel_allowableErrorPanel(this);
        this.specialCharactersPanel = new SpecialCharactersPanel();
    }

    private void setReactions() {
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        if (this.userCode != null) this.userCode.addFocusListener(focusOnText);
        if (this.userName != null) this.userName.addFocusListener(focusOnText);
        if (this.userProtocolNumber != null) this.userProtocolNumber.addFocusListener(focusOnText);
        if (this.userTechnologyNumber != null) this.userTechnologyNumber.addFocusListener(focusOnText);
        if (this.rangeLikeChannel != null) this.rangeLikeChannel.addFocusListener(focus);

        if (this.rangeLikeChannel != null) this.rangeLikeChannel.addItemListener(this.clickRangeLikeChannel);
        if (this.negativeButton != null) this.negativeButton.addActionListener(this.clickNegativeButton);
        if (this.positiveButton != null) this.positiveButton.addActionListener(this.clickPositiveButton);
        if (this.removeButton != null) this.resetButton.addActionListener(this.clickReset);
        if (this.saveAndCalculateButton != null) this.saveAndCalculateButton.addActionListener(this.clickSaveAndCalculate);
        if (this.removeButton != null) this.removeButton.addActionListener(this.clickRemove);

        if (this.userCode != null) this.userCode.getDocument().addDocumentListener(this.codeUpdate);
        if (this.userName != null) this.userName.getDocument().addDocumentListener(this.nameUpdate);

        if (this.userCode != null) this.userCode.addKeyListener(this.keyListener);
        if (this.userName != null) this.userName.addKeyListener(this.keyListener);
        if (this.userProtocolNumber != null) this.userProtocolNumber.addKeyListener(this.keyListener);
        if (this.userTechnologyNumber != null) this.userTechnologyNumber.addKeyListener(this.keyListener);
        if (this.measurementPanel != null) this.measurementPanel.addKeyListener(this.keyListener);
        if (this.datePanel != null) this.datePanel.addKeyListener(this.keyListener);
        if (this.frequencyPanel != null) this.frequencyPanel.addKeyListener(this.keyListener);
        if (this.pathPanel != null) this.pathPanel.addKeyListener(this.keyListener);
        if (this.sensorPanel != null) this.sensorPanel.addKeyListener(this.keyListener);
        if (this.rangePanel != null) this.rangePanel.addKeyListener(this.keyListener);
        if (this.allowableErrorPanel != null) this.allowableErrorPanel.addKeyListener(this.keyListener);
        if (this.sensorRangePanel != null) this.sensorRangePanel.addKeyListener(this.keyListener);
        if (this.specialCharactersPanel != null) this.specialCharactersPanel.addKeyListener(this.keyListener);
        if (this.rangeLikeChannel != null) this.rangeLikeChannel.addKeyListener(this.keyListener);
        if (this.negativeButton != null) this.negativeButton.addKeyListener(this.keyListener);
        if (this.positiveButton != null) this.positiveButton.addKeyListener(this.keyListener);
        if (this.saveAndCalculateButton != null) this.saveAndCalculateButton.addKeyListener(this.keyListener);
        if (this.resetButton != null) this.resetButton.addKeyListener(this.keyListener);
        if (this.removeButton != null) this.removeButton.addKeyListener(this.keyListener);
    }

    private void build() {
        this.setSize(850, 850);
        this.setLocation(ConverterUI.POINT_CENTER(this.parent, this));
        if (this.oldChannel == null){
            this.setMeasurement(null);
        }else {
            this.setMeasurement(this.oldChannel.getMeasurement());
            this.userCode.setText(this.oldChannel.getCode());
            this.userName.setText(this.oldChannel.getName());
            this.userName.setToolTipText(this.oldChannel.getName());
            this.measurementPanel.update(this.oldChannel.getMeasurement().getName());
            this.userTechnologyNumber.setText(this.oldChannel.getTechnologyNumber());
            this.datePanel.setDate(VariableConverter.stringToDate(this.oldChannel.getDate()));
            this.frequencyPanel.updateFrequency(this.oldChannel.getFrequency(), VariableConverter.stringToDate(this.oldChannel.getDate()));
            this.pathPanel.update(this.oldChannel.getDepartment(), this.oldChannel.getArea(), this.oldChannel.getProcess(), this.oldChannel.getInstallation());
            this.sensorPanel.update(this.oldChannel.getMeasurement().getName());
            this.sensorPanel.update(this.oldChannel.getSensor());
            this.rangePanel.updateValue(this.oldChannel.getMeasurement().getValue());
            this.rangePanel.updateRange(this.oldChannel.getRangeMin(), this.oldChannel.getRangeMax());
            double range = this.oldChannel.getRangeMax() - this.oldChannel.getRangeMin();
            this.allowableErrorPanel.updateError(this.oldChannel.getAllowableError(), false, range);
            this.userProtocolNumber.setText(this.oldChannel.getNumberOfProtocol());
            if (this.sensorRangePanel != null){
                this.sensorRangePanel.update(this.oldChannel.getSensor());
            }
            this.measurementPanel.setSelectedValue(this.oldChannel.getMeasurement().getValue());
        }

        this.setLocation(ConverterUI.POINT_CENTER(this.parent, this));
        this.mainPanel = new MainPanel();
        this.setContentPane(this.mainPanel);
    }

    public void update(Measurement measurement) {
        if (measurement != null){
            this.setMeasurement(measurement);
        }
    }

    private boolean allFieldsAreNotFilled(){
        boolean good;
        if (this.userCode.getText().length()==0) {
            this.codeBorder.setTitleColor(Color.RED);
            good = true;
        }else if (this.oldChannel == null &&
                Application.context.channelService.isExist(this.userCode.getText())) {
            this.codeBorder.setTitleColor(Color.RED);
            Application.context.channelService.showExistMessage(DialogChannel.this);
            good = true;
        }else if (this.oldChannel != null &&
                Application.context.channelService.isExist(this.oldChannel.getCode(), this.userCode.getText())){
            this.codeBorder.setTitleColor(Color.RED);
            Application.context.channelService.showExistMessage(DialogChannel.this);
            good = true;
        }else {
            this.codeBorder.setTitleColor(Color.BLACK);
            good = false;
        }

        if (this.userName.getText().length()==0) {
            this.nameBorder.setTitleColor(Color.RED);
            good = true;
        }else {
            this.nameBorder.setTitleColor(Color.BLACK);
        }

        if (this.userTechnologyNumber.getText().length()==0) {
            this.technologyNumberBorder.setTitleColor(Color.RED);
            good = true;
        }else {
            this.technologyNumberBorder.setTitleColor(Color.BLACK);
        }

        if (this.rangePanel != null && this.sensorRangePanel != null){
            double sensorRangeMin = this.measurementPanel.getMeasurement().convertFrom(this.sensorRangePanel.getValue(), this.sensorRangePanel.getRangeMin());
            double sensorRangeMax = this.measurementPanel.getMeasurement().convertFrom(this.sensorRangePanel.getValue(), this.sensorRangePanel.getRangeMax());
            if (this.rangePanel.getRangeMin() < sensorRangeMin || this.rangePanel.getRangeMax() > sensorRangeMax){
                this.rangePanel.getBorder().setTitleColor(Color.RED);
                this.sensorRangeBorder.setTitleColor(Color.RED);
                if (this.rangePanel.getRangeMin() < sensorRangeMin){
                    this.rangePanel.getRangeMinField().setForeground(Color.RED);
                    this.sensorRangePanel.getRangeMinField().setForeground(Color.RED);
                }else {
                    this.rangePanel.getRangeMinField().setForeground(Color.BLACK);
                    this.sensorRangePanel.getRangeMinField().setForeground(Color.BLACK);
                }
                if (this.rangePanel.getRangeMax() > sensorRangeMax){
                    this.rangePanel.getRangeMaxField().setForeground(Color.RED);
                    this.sensorRangePanel.getRangeMaxField().setForeground(Color.RED);
                }else {
                    this.rangePanel.getRangeMaxField().setForeground(Color.BLACK);
                    this.sensorRangePanel.getRangeMaxField().setForeground(Color.BLACK);
                }
                good = true;
            }else {
                this.rangePanel.getBorder().setTitleColor(Color.BLACK);
                this.sensorRangeBorder.setTitleColor(Color.BLACK);
                this.rangePanel.getRangeMinField().setForeground(Color.BLACK);
                this.rangePanel.getRangeMaxField().setForeground(Color.BLACK);
                this.sensorRangePanel.getRangeMinField().setForeground(Color.BLACK);
                this.sensorRangePanel.getRangeMaxField().setForeground(Color.BLACK);
            }
        }
        if (good) this.refresh();
        return good;
    }

    public Channel getChannel(){
        Channel channel = new Channel();
        channel.setCode(this.userCode.getText());
        channel.setName(this.userName.getText());
        channel.setMeasurement(this.measurementPanel.getMeasurement());
        channel.setDepartment(this.pathPanel.getDepartment());
        channel.setArea(this.pathPanel.getArea());
        channel.setProcess(this.pathPanel.getProcess());
        channel.setInstallation(this.pathPanel.getInstallation());
        channel.setDate(this.datePanel.getDate());
        channel.setFrequency(this.frequencyPanel.getFrequency());
        channel.setTechnologyNumber(this.userTechnologyNumber.getText());
        Sensor s = this.sensorPanel.getSensor();
        Sensor sensor = new Sensor();
        sensor.setName(s.getName());
        sensor.setType(s.getType());
        sensor.setMeasurement(s.getMeasurement());
        sensor.setErrorFormula(s.getErrorFormula());
        if (this.sensorRangePanel != null){
            sensor.setRange(this.sensorRangePanel.getRangeMin(), this.sensorRangePanel.getRangeMax());
            sensor.setValue(this.sensorRangePanel.getValue());
        }else {
            sensor.setRange(s.getRangeMin(), s.getRangeMax());
            sensor.setValue(s.getValue());
        }
        if (channel.getMeasurement().getName().equals(Measurement.CONSUMPTION)){
            sensor.setNumber(this.sensorPanel.getSerialNumber());
            sensor.setRangeMin(this.rangePanel.getRangeMin());
            sensor.setRangeMax(this.rangePanel.getRangeMax());
            sensor.setValue(this.measurementPanel.getMeasurement().getValue());
        }
        channel.setSensor(sensor);
        channel.setNumberOfProtocol(this.userProtocolNumber.getText());
        channel.setRange(this.rangePanel.getRangeMin(), this.rangePanel.getRangeMax());
        channel.setAllowableError(this.allowableErrorPanel.getAllowableErrorPercent(), this.allowableErrorPanel.getAllowableErrorValue());

        if (this.oldChannel != null){
            channel.setReference(this.oldChannel.getReference());
            channel.setSuitability(this.oldChannel.isSuitability());
        }

        return channel;
    }

    private void setMeasurement(Measurement measurement){
        if (measurement == null){
            measurement = new Measurement(Measurement.TEMPERATURE, Measurement.DEGREE_CELSIUS);
        }
        String measurementName = measurement.getName();
        this.measurementPanel.update(measurementName);
        this.sensorPanel.update(measurementName);
        if (measurementName.equals(Measurement.TEMPERATURE)
            || measurementName.equals(Measurement.PRESSURE)){
            this.sensorRangePanel = new DialogChannel_sensorRangePanel(this, measurement);
            this.sensorRangePanel.update(Application.context.sensorService.get(this.sensorPanel.getSensor().getName()));
            this.rangePanel.setTitle(RANGE_OF_CHANNEL);
            this.allowableErrorPanel.setEnabled(true);
        }else if (measurementName.equals(Measurement.CONSUMPTION)){
            this.sensorRangePanel = null;
            this.rangePanel.setTitle(RANGE_OF_SENSOR);
            Channel channel = new Channel();
            channel.setMeasurement(measurement);
            channel.setRangeMin(this.rangePanel.getRangeMin());
            channel.setRangeMax(this.rangePanel.getRangeMax());
            Sensor sensor = this.sensorPanel.getSensor();
            sensor.setValue(channel.getMeasurement().getValue());
            double errorSensor = sensor.getError(channel);
            this.allowableErrorPanel.updateError(errorSensor, false, channel._getRange());
            this.allowableErrorPanel.setEnabled(false);
        }
        this.mainPanel = new MainPanel();
        this.setContentPane(this.mainPanel);
    }

    public final KeyListener keyListener = new KeyAdapter() {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()){
                case KeyEvent.VK_ESCAPE:
                    if (e.isAltDown()) negativeButton.doClick();
                    break;
                case KeyEvent.VK_ENTER:
                    if (e.isAltDown() && !e.isControlDown()) positiveButton.doClick();
                    if (e.isControlDown() && !e.isAltDown()) saveAndCalculateButton.doClick();
                    if (e.isAltDown() && e.isControlDown()) {
                        resetSpecialCharactersPanel();
                        if (allFieldsAreNotFilled() || Application.isBusy(DialogChannel.this)) return;

                        Application.putHint(userName.getText());
                        dispose();
                        ArrayList<Channel>channels;
                        if (oldChannel == null) {
                            channels = Application.context.channelService.add(getChannel());
                        }else {
                            channels = Application.context.channelService.set(oldChannel, getChannel());
                        }
                        if (Application.context.channelSorter.isOn()){
                            parent.setChannelsList(Application.context.channelSorter.getCurrent());
                        }else {
                            parent.setChannelsList(channels);
                        }
                        EventQueue.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                new OS_Chooser(parent, getChannel()).setVisible(true);
                            }
                        });
                    }
                    break;
            }
        }
    };

    private final ItemListener clickRangeLikeChannel = new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent e) {
            if (sensorRangePanel != null) {
                if (rangeLikeChannel.isSelected()) {
                    sensorRangePanel.setRange(String.valueOf(rangePanel.getRangeMin()),
                            String.valueOf(rangePanel.getRangeMax()),
                            measurementPanel.getMeasurement().getValue());
                    sensorRangePanel.setEnabled(false);
                } else {
                    sensorRangePanel.setEnabled(true);
                    sensorRangePanel.update(Application.context.sensorService.get(sensorPanel.getSensor().getName()));
                }
            }
        }
    };

    private void refresh(){
        this.setVisible(false);
        this.setVisible(true);
    }

    private final ActionListener clickNegativeButton = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            DialogChannel.this.dispose();
        }
    };

    private final ActionListener clickPositiveButton = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            resetSpecialCharactersPanel();
            if (allFieldsAreNotFilled() || Application.isBusy(DialogChannel.this)) return;

            Application.putHint(userName.getText());
            dispose();
            ArrayList<Channel>channels;
            if (oldChannel == null) {
                channels = Application.context.channelService.add(getChannel());
            }else {
                channels = Application.context.channelService.set(oldChannel, getChannel());
            }
            if (Application.context.channelSorter.isOn()){
                parent.setChannelsList(Application.context.channelSorter.getCurrent());
            }else {
                parent.setChannelsList(channels);
            }
        }
    };

    private final ActionListener clickPaste = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            JMenuItem item = (JMenuItem) e.getSource();
            userName.setText(item.getText());
        }
    };

    private final ActionListener clickCheck = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            new CheckChannel(DialogChannel.this, userCode.getText()).start();
        }
    };

    private final ActionListener clickReset = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            build();
            resetSpecialCharactersPanel();
        }
    };

    private final ActionListener clickRemove = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (oldChannel != null){
                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        int result = JOptionPane.showConfirmDialog(DialogChannel.this,
                                oldChannel.getName(), "Видалити канал?", JOptionPane.OK_CANCEL_OPTION);
                        if (result == 0){
                            ArrayList<Channel> channels = Application.context.channelService.remove(oldChannel);
                            DialogChannel.this.dispose();
                            if (Application.context.channelSorter.isOn()){
                                parent.setChannelsList(Application.context.channelSorter.getCurrent());
                            }else {
                                parent.setChannelsList(channels);
                            }
                        }
                    }
                });
            }
        }
    };

    private final ActionListener clickSaveAndCalculate = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            resetSpecialCharactersPanel();
            if (allFieldsAreNotFilled() || Application.isBusy(DialogChannel.this)) return;

            Application.putHint(userName.getText());
            dispose();
            ArrayList<Channel>channels;
            final Channel channel = getChannel();
            if (oldChannel == null) {
                channels = Application.context.channelService.add(channel);
            }else {
                channels = Application.context.channelService.set(oldChannel, channel);
            }
            if (Application.context.channelSorter.isOn()){
                parent.setChannelsList(Application.context.channelSorter.getCurrent());
            }else {
                parent.setChannelsList(channels);
            }

            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    new CalculateStartDialog(Application.context.mainScreen, channel, null).setVisible(true);
                }
            });
        }
    };

    private final DocumentListener nameUpdate = new DocumentListener() {
        @Override
        public void insertUpdate(DocumentEvent e) {
            userName.setToolTipText(userName.getText());
            if (oldChannel != null){
                removeButton.setEnabled(oldChannel.getCode().equals(userCode.getText())
                        && oldChannel.getName().equals(userName.getText()));
            }
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            if (userName.getText().length() > 0) {
                userName.setToolTipText(userName.getText());
            }
            if (oldChannel != null){
                removeButton.setEnabled(oldChannel.getCode().equals(userCode.getText())
                        && oldChannel.getName().equals(userName.getText()));
            }
        }

        @Override public void changedUpdate(DocumentEvent e) {}
    };

    private final DocumentListener codeUpdate = new DocumentListener() {
        @Override
        public void insertUpdate(DocumentEvent e) {
            if (oldChannel != null){
                removeButton.setEnabled(oldChannel.getCode().equals(userCode.getText())
                        && oldChannel.getName().equals(userName.getText()));
            }
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            if (oldChannel != null){
                removeButton.setEnabled(oldChannel.getCode().equals(userCode.getText())
                        && oldChannel.getName().equals(userName.getText()));
            }
        }

        @Override public void changedUpdate(DocumentEvent e) {}
    };

    private final FocusListener focusOnText = new FocusAdapter() {
        @Override
        public void focusGained(FocusEvent e) {
            JTextField source = (JTextField) e.getSource();
            if (source.equals(userCode) || source.equals(userName)
                || source.equals(userProtocolNumber) || source.equals(userTechnologyNumber)) {
                specialCharactersPanel.setFieldForInsert(source);
            }
        }
    };

    private final FocusListener focus = new FocusAdapter() {
        @Override
        public void focusGained(FocusEvent e) {
            resetSpecialCharactersPanel();
        }
    };

    public void resetSpecialCharactersPanel(){
        this.specialCharactersPanel.setFieldForInsert(null);
    }

    private class MainPanel extends JPanel {

        protected MainPanel() {
            super(new GridBagLayout());
            this.setBackground(Color.WHITE);

            this.add(userCode, new Cell(0,0));
            this.add(specialCharactersPanel, new Cell(2,0,5));
            this.add(userName, new Cell(0,1));
            this.add(measurementPanel, new Cell(0,2));
            this.add(userTechnologyNumber, new Cell(0,3));
            this.add(datePanel, new Cell(0,4));
            this.add(userProtocolNumber, new Cell(0,5));
            this.add(frequencyPanel, new Cell(0,6));
            this.add(pathPanel, new Cell(0,7));
            this.add(sensorPanel, new Cell(0,8));

            if (sensorRangePanel != null){
                JPanel srp = new JPanel();
                srp.setBackground(Color.WHITE);
                srp.setLayout(new BoxLayout(srp, BoxLayout.Y_AXIS));
                srp.add(sensorRangePanel);
                srp.add(rangeLikeChannel);
                sensorRangeBorder.setTitleJustification(TitledBorder.CENTER);
                srp.setBorder(sensorRangeBorder);
                this.add(srp, new Cell(2,8,2));
            }else {
                rangeLikeChannel.setSelected(false);
            }

            this.add(rangePanel, new Cell(0,9));
            this.add(allowableErrorPanel, new Cell(0,10));

            Cell buttonCell = new Cell(0,11,new Insets(20,0,0,0), 3);
            buttonCell.fill = Cell.NONE;
            this.add(saveAndCalculateButton, buttonCell);

            JPanel buttonsPanel = new JPanel();
            buttonsPanel.setBackground(Color.WHITE);
            buttonsPanel.add(negativeButton);
            if (oldChannel != null){
                buttonsPanel.add(removeButton);
                buttonsPanel.add(resetButton);
            }
            buttonsPanel.add(positiveButton);
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