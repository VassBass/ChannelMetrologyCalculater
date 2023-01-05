package ui.mainScreen.searchPanel;

import constants.Sort;
import converters.VariableConverter;
import org.apache.commons.validator.DateValidator;
import repository.MeasurementRepository;
import repository.PathElementRepository;
import repository.SensorRepository;
import repository.impl.*;
import ui.mainScreen.MainScreen;
import ui.model.CellBuilder;

import javax.swing.*;

import java.awt.*;

import static org.apache.commons.lang3.StringUtils.EMPTY;

public class SearchPanelVT extends SearchPanel {
    private final SearchPanel connect;

    private final PathElementRepository departmentRepository = DepartmentRepositorySQLite.getInstance();
    private final PathElementRepository areaRepository = AreaRepositorySQLite.getInstance();
    private final PathElementRepository processRepository = ProcessRepositorySQLite.getInstance();
    private final PathElementRepository installationRepository = InstallationRepositorySQLite.getInstance();
    private final SensorRepository sensorRepository = SensorRepositorySQLite.getInstance();
    private final MeasurementRepository measurementRepository = MeasurementRepositorySQLite.getInstance();

    public SearchPanelVT(SearchPanel connect) {
        super(null);
        this.connect = connect;
    }

    protected void clickSearch(boolean start) {
        if (start) {
            String field = connect.listSearchFields.getSelectedItem() == null ?
                    EMPTY :
                    connect.listSearchFields.getSelectedItem().toString();
            if (!field.equals(TEXT_CODE)) connect.setEnabled(false);
        } else {
            connect.setEnabled(true);
        }
    }

    protected void changeField() {
        Object selected = connect.listSearchFields.getSelectedItem();
        if (selected != null) {
            final String selectedField = selected.toString();
            EventQueue.invokeLater(() -> {
                switch (selectedField) {
                    default:
                        rebuildPanel(TEXT_MODE, selectedField);
                        connect.inputValue.setToolTipText(TEXT_DEFAULT_TOOLTIP);
                        break;
                    case TEXT_MEASUREMENT_NAME:
                    case TEXT_MEASUREMENT_VALUE:
                    case TEXT_SENSOR_TYPE:
                    case TEXT_DEPARTMENT:
                    case TEXT_AREA:
                    case TEXT_PROCESS:
                    case TEXT_INSTALLATION:
                        rebuildPanel(LIST_MODE, selectedField);
                        break;
                    case TEXT_DATE:
                        rebuildPanel(TEXT_MODE, selectedField);
                        connect.inputValue.setToolTipText(TEXT_DATE_TOOLTIP);
                        break;
                    case TEXT_SUITABILITY:
                        rebuildPanel(CHECK_MODE, selectedField);
                        break;
                }
                MainScreen.getInstance().refresh();
            });
        }
    }

    protected void valueLostFocus() {
        String str = connect.inputValue.getText();
        if (connect.listSearchFields.getSelectedIndex() == Sort.FREQUENCY){
            connect.inputValue.setText(VariableConverter.doubleString(str));
        }else if (connect.listSearchFields.getSelectedIndex() == Sort.DATE){
            String str1 = VariableConverter.commasToDots(str);
            DateValidator dateValidator = DateValidator.getInstance();
            if (dateValidator.isValid(str1,"dd.MM.yyyy", false)){
                connect.inputValue.setText(VariableConverter.stringToDateString(str));
            }else {
                connect.inputValue.setText(TEXT_DEFAULT_DATE);
            }
        }
    }

    private void rebuildPanel(int mode, String selectedField) {
        connect.removeAll();
        connect.add(connect.listSearchFields, new CellBuilder().y(0).create());
        switch (mode){
            default:
                connect.add(connect.inputValue, new CellBuilder().y(1).create());
                break;
            case LIST_MODE:
                setModelToComboBox(selectedField);
                connect.add(connect.listValues, new CellBuilder().y(1).create());
                break;
            case CHECK_MODE:
                connect.add(connect.checkSuitability, new CellBuilder().y(1).create());
                break;
        }

        connect.add(connect.btnSearch, new CellBuilder().y(2).create());
    }

    private void setModelToComboBox(String selectedField){
        switch (selectedField){
            case TEXT_MEASUREMENT_NAME:
                connect.listValues.setModel(new DefaultComboBoxModel<>(measurementRepository.getAllNames()));
                connect.listValues.setEditable(false);
                break;
            case TEXT_MEASUREMENT_VALUE:
                connect.listValues.setModel(new DefaultComboBoxModel<>(measurementRepository.getAllValues()));
                connect.listValues.setEditable(false);
                break;
            case TEXT_DEPARTMENT:
                connect.listValues.setModel(new DefaultComboBoxModel<>(departmentRepository.getAll().toArray(new String[0])));
                connect.listValues.setEditable(true);
                break;
            case TEXT_AREA:
                connect.listValues.setModel(new DefaultComboBoxModel<>(areaRepository.getAll().toArray(new String[0])));
                connect.listValues.setEditable(true);
                break;
            case TEXT_PROCESS:
                connect.listValues.setModel(new DefaultComboBoxModel<>(processRepository.getAll().toArray(new String[0])));
                connect.listValues.setEditable(true);
                break;
            case TEXT_INSTALLATION:
                connect.listValues.setModel(new DefaultComboBoxModel<>(installationRepository.getAll().toArray(new String[0])));
                connect.listValues.setEditable(true);
                break;
            case TEXT_SENSOR_TYPE:
                connect.listValues.setModel(new DefaultComboBoxModel<>(sensorRepository.getAllTypes().toArray(new String[0])));
                connect.listValues.setEditable(false);
                break;
        }
    }
}
