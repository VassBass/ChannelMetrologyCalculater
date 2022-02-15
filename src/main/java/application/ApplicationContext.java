package application;

import service.*;
import service.impl.*;
import ui.mainScreen.MainScreen;

import javax.swing.*;
import java.awt.*;

public class ApplicationContext {
    public static final String CANCEL = "Відміна";
    public static final String IMPORT = "Імпорт";
    public static final String FILE_NAME = "Назва файлу";
    public static final String TYPES_OF_FILES = "Типи файлів";
    public static final String DIRECTORY = "Директорія";
    public static final String ALL_FILES = "Всі файли";
    public static final String CLOSE_WINDOW = "Закрити вікно";
    public static final String OPEN = "Відкрити";
    public static final String OPEN_SELECTED_DIRECTORY = "Відкрити обрану директорію";
    public static final String OPEN_SELECTED_FILE = "Відкрити обраний файл";
    public static final String TABLE = "Таблиця";
    public static final String DATE_LAST_CHANGE = "Редагувалося";
    public static final String NAME = "Назва";
    public static final String SIZE = "Розмір";
    public static final String TYPE = "Тип";
    public static final String TO_HOME = "На головну";
    public static final String LIST = "Список";
    public static final String CREATE_NEW_FOLDER = "Створити нову папку";
    public static final String UPDATE = "Оновити";
    public static final String UP_FOLDER = "На один рівень вверх";
    public static final String MODE_OF_VIEW = "Відображення";

    public final MainScreen mainScreen = new MainScreen();

    public final ChannelService channelService = new ChannelServiceImpl();
    public final SensorService sensorService = new SensorServiceImpl();
    public final CalibratorService calibratorService = new CalibratorServiceImpl();
    public final PersonService personService = new PersonServiceImpl();
    public final DepartmentService departmentService = new DepartmentServiceImpl();
    public final AreaService areaService = new AreaServiceImpl();
    public final ProcessService processService = new ProcessServiceImpl();
    public final InstallationService installationService = new InstallationServiceImpl();
    public final MeasurementService measurementService = new MeasurementServiceImpl();
    public final ChannelSorter channelSorter = new ChannelSorter();

    public ApplicationContext(){
        this.localizationOfComponents();
    }

    private void localizationOfComponents(){
        //File chooser
        UIManager.put("FileChooser.cancelButtonText", CANCEL);
        UIManager.put("FileChooser.openButtonText", IMPORT);
        UIManager.put("FileChooser.fileNameLabelText", FILE_NAME);
        UIManager.put("FileChooser.filesOfTypeLabelText", TYPES_OF_FILES);
        UIManager.put("FileChooser.lookInLabelText", DIRECTORY);
        UIManager.put("FileChooser.acceptAllFileFilterText", ALL_FILES);
        UIManager.put("FileChooser.cancelButtonToolTipText", CLOSE_WINDOW);
        UIManager.put("FileChooser.directoryOpenButtonText", OPEN);
        UIManager.put("FileChooser.directoryOpenButtonToolTipText", OPEN_SELECTED_DIRECTORY);
        UIManager.put("FileChooser.openButtonToolTipText", OPEN_SELECTED_FILE);
        UIManager.put("FileChooser.detailsViewActionLabelText", TABLE);
        UIManager.put("FileChooser.detailsViewButtonToolTipText", TABLE);
        UIManager.put("FileChooser.fileDateHeaderText", DATE_LAST_CHANGE);
        UIManager.put("FileChooser.fileNameHeaderText", NAME);
        UIManager.put("FileChooser.fileSizeHeaderText", SIZE);
        UIManager.put("FileChooser.fileTypeHeaderText", TYPE);
        UIManager.put("FileChooser.homeFolderToolTipText", TO_HOME);
        UIManager.put("FileChooser.listViewActionLabelText", LIST);
        UIManager.put("FileChooser.listViewButtonToolTipText", LIST);
        UIManager.put("FileChooser.newFolderActionLabelText", CREATE_NEW_FOLDER);
        UIManager.put("FileChooser.newFolderToolTipText", CREATE_NEW_FOLDER);
        UIManager.put("FileChooser.refreshActionLabelText", UPDATE);
        UIManager.put("FileChooser.upFolderToolTipText", UP_FOLDER);
        UIManager.put("FileChooser.viewMenuLabelText", MODE_OF_VIEW);

        //ToolTip
        UIManager.put("ToolTip.background", Color.BLACK);
        UIManager.put("ToolTip.foreground", Color.WHITE);
    }
}