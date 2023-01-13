package ui.mainScreen.searchPanel;

import factory.AbstractFactory;
import service.MainScreenEventListener;
import service.repository.repos.area.AreaRepository;
import service.repository.repos.department.DepartmentRepository;
import service.repository.repos.installation.InstallationRepository;
import service.repository.repos.measurement.MeasurementRepository;
import service.repository.repos.process.ProcessRepository;
import service.repository.repos.sensor.SensorRepository;
import ui.event.EventDataSource;
import ui.event.HashMapEventDataSource;
import ui.event.SingleEventDataSource;
import ui.event.EventSource;
import ui.model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Objects;

import static ui.mainScreen.MainScreen.*;

public class SearchPanel extends JPanel {
    public static final String TEXT_CODE = "Код каналу";
    public static final String TEXT_NAME = "Назва каналу";
    public static final String TEXT_MEASUREMENT_NAME = "Вид вимірювання";
    public static final String TEXT_MEASUREMENT_VALUE = "Одиниці вимірювання";
    public static final String TEXT_DEPARTMENT = "Цех";
    public static final String TEXT_AREA = "Ділянка";
    public static final String TEXT_PROCESS = "Процесс";
    public static final String TEXT_INSTALLATION = "Установка";
    public static final String TEXT_DATE = "Дата останньої перевірки";
    public static final String TEXT_FREQUENCY = "Міжконтрольний інтервал";
    public static final String TEXT_TECHNOLOGY_NUMBER = "Технологічний номер";
    public static final String TEXT_SENSOR_NAME = "Назва ПВП";
    public static final String TEXT_SENSOR_TYPE = "Тип ПВП";
    public static final String TEXT_PROTOCOL_NUMBER = "Номер протоколу(сертифікату)";
    public static final String TEXT_REFERENCE = "Номер довідки";
    public static final String TEXT_SUITABILITY = "Придатність";
    private static final String TEXT_SUITABLE = "Придатний";
    protected static final String TEXT_START_SEARCH = "Шукати (Alt + Enter)";
    private static final String TEXT_FINISH_SEARCH = "Відмінити";
    protected static final String TEXT_DEFAULT_DATE = "01.01.2020";
    protected static final String TEXT_DATE_TOOLTIP = "Введіть дату у форматі \"день.місяць.рік\"";
    protected static final String TEXT_DEFAULT_TOOLTIP = "Введіть слово(а) для пошуку";

    private static final String[] SEARCH_FIELDS = {
            TEXT_CODE, TEXT_NAME, TEXT_MEASUREMENT_NAME, TEXT_MEASUREMENT_VALUE, TEXT_DEPARTMENT, TEXT_AREA, TEXT_PROCESS,
            TEXT_INSTALLATION, TEXT_DATE, TEXT_FREQUENCY, TEXT_TECHNOLOGY_NUMBER, TEXT_SENSOR_NAME, TEXT_SENSOR_TYPE,
            TEXT_PROTOCOL_NUMBER, TEXT_REFERENCE, TEXT_SUITABILITY
    };

    public static final int TEXT_MODE = 0;
    public static final int LIST_MODE = 1;
    public static final int CHECK_MODE = 3;

    public DefaultButton btnSearch;
    protected StringsList listSearchFields;
    protected InputField inputValue;
    protected StringsList listValues;
    protected CheckBox checkSuitability;

    private final AbstractFactory repositoryFactory;

    public SearchPanel(MainScreenEventListener eventListener,
                       AbstractFactory repositoryFactory){
        super(new GridBagLayout());
        this.repositoryFactory = repositoryFactory;

        this.btnSearch = new DefaultButton(TEXT_START_SEARCH);
        this.listSearchFields = new StringsList(StringsList.CENTER, SEARCH_FIELDS);
        this.inputValue = new InputFieldText(InputField.CENTER);
        this.listValues = new StringsList(false, StringsList.CENTER);
        this.checkSuitability = new CheckBox(TEXT_SUITABLE, CheckBox.CENTER);

        this.add(this.listSearchFields, new CellBuilder().y(0).create());
        this.add(this.inputValue, new CellBuilder().y(1).create());
        this.add(this.btnSearch, new CellBuilder().y(2).create());

        this.btnSearch.addActionListener(eventListener.clickSearchButton(getDataForSearch()));
        this.listSearchFields.addItemListener(eventListener.changeSearchField(
                new SingleEventDataSource<>(KEY_SEARCH_FIELD_TEXT, Objects.requireNonNull(listSearchFields.getSelectedItem()).toString())));
        this.inputValue.addFocusListener(inputValueFocusListener);
        this.listSearchFields.addKeyListener(keyListener);
        this.inputValue.addKeyListener(keyListener);
        this.listValues.addKeyListener(keyListener);
        this.checkSuitability.addKeyListener(keyListener);
        this.btnSearch.addKeyListener(keyListener);
    }

    @Override
    public void setEnabled(boolean enabled) {
        btnSearch.setText(enabled ? TEXT_START_SEARCH : TEXT_FINISH_SEARCH);
        if (listSearchFields != null) listSearchFields.setEnabled(enabled);
        if (inputValue != null) inputValue.setEnabled(enabled);
        if (listValues != null) listValues.setEnabled(enabled);
        if (checkSuitability != null) checkSuitability.setEnabled(enabled);
    }

    private final KeyListener keyListener = new KeyAdapter() {
        @Override
        public void keyPressed(KeyEvent e) {
            if (e.isAltDown() && e.getKeyCode() == KeyEvent.VK_ENTER){
                btnSearch.doClick();
            }
        }
    };

    private EventDataSource<Void> getDataForSearch() {
        EventDataSource<Void> eventDataSource = new HashMapEventDataSource<>();
        eventDataSource.put(KEY_SEARCH_FIELD_TEXT, Objects.requireNonNull(listSearchFields.getSelectedItem()).toString());
        eventDataSource.put(KEY_SEARCH_VALUE_TEXT, inputValue.getText());
        eventDataSource.put(KEY_SEARCH_VALUE_LIST_ITEM_TEXT, Objects.requireNonNull(listValues.getSelectedItem()).toString());
        eventDataSource.put(KEY_SEARCH_VALUE_BOOLEAN, checkSuitability.isSelected());
        return eventDataSource;
    }

    public void selectAllValueText() {
        inputValue.selectAll();
    }

    private final FocusListener inputValueFocusListener = new FocusListener() {
        @Override
        public void focusGained(FocusEvent e) {
            inputValue.selectAll();
        }

        @Override
        public void focusLost(FocusEvent e) {
            searchPanelVT.valueLostFocus();
        }
    };

    public void rebuildPanel(int mode, String selectedField) {
        EventQueue.invokeLater(() -> {
            this.removeAll();
            this.add(this.listSearchFields, new CellBuilder().y(0).create());
            switch (mode) {
                default:
                    if (selectedField.equals(TEXT_DATE)) {
                        this.inputValue.setToolTipText(TEXT_DATE_TOOLTIP);
                    } else {
                        this.inputValue.setToolTipText(TEXT_DEFAULT_TOOLTIP);
                    }
                    this.add(this.inputValue, new CellBuilder().y(1).create());
                    break;
                case LIST_MODE:
                    setModelToComboBox(selectedField);
                    this.add(this.listValues, new CellBuilder().y(1).create());
                    break;
                case CHECK_MODE:
                    this.add(this.checkSuitability, new CellBuilder().y(1).create());
                    break;
            }

            this.add(this.btnSearch, new CellBuilder().y(2).create());
        });
    }

    private void setModelToComboBox(String selectedField){
        switch (selectedField){
            case TEXT_MEASUREMENT_NAME:
                MeasurementRepository measurementRepository = repositoryFactory.create(MeasurementRepository.class);
                this.listValues.setModel(new DefaultComboBoxModel<>(measurementRepository.getAllNames()));
                this.listValues.setEditable(false);
                break;
            case TEXT_MEASUREMENT_VALUE:
                measurementRepository = repositoryFactory.create(MeasurementRepository.class);
                this.listValues.setModel(new DefaultComboBoxModel<>(measurementRepository.getAllValues()));
                this.listValues.setEditable(false);
                break;
            case TEXT_DEPARTMENT:
                DepartmentRepository departmentRepository = repositoryFactory.create(DepartmentRepository.class);
                this.listValues.setModel(new DefaultComboBoxModel<>(departmentRepository.getAll().toArray(new String[0])));
                this.listValues.setEditable(true);
                break;
            case TEXT_AREA:
                AreaRepository areaRepository = repositoryFactory.create(AreaRepository.class);
                this.listValues.setModel(new DefaultComboBoxModel<>(areaRepository.getAll().toArray(new String[0])));
                this.listValues.setEditable(true);
                break;
            case TEXT_PROCESS:
                ProcessRepository processRepository = repositoryFactory.create(ProcessRepository.class);
                this.listValues.setModel(new DefaultComboBoxModel<>(processRepository.getAll().toArray(new String[0])));
                this.listValues.setEditable(true);
                break;
            case TEXT_INSTALLATION:
                InstallationRepository installationRepository = repositoryFactory.create(InstallationRepository.class);
                this.listValues.setModel(new DefaultComboBoxModel<>(installationRepository.getAll().toArray(new String[0])));
                this.listValues.setEditable(true);
                break;
            case TEXT_SENSOR_TYPE:
                SensorRepository sensorRepository = repositoryFactory.create(SensorRepository.class);
                this.listValues.setModel(new DefaultComboBoxModel<>(sensorRepository.getAllTypes().toArray(new String[0])));
                this.listValues.setEditable(false);
                break;
        }
    }
}