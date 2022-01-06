package application;

import controller.*;
import ui.main.MainScreen;

public class ApplicationContext {
    public final MainScreen mainScreen;

    public final ChannelsController channelsController;
    public final SensorsController sensorsController;
    public final CalibratorsController calibratorsController;
    public final PersonsController personsController;
    public final DepartmentsController departmentsController;
    public final AreasController areasController;
    public final ProcessesController processesController;
    public final InstallationsController installationsController;
    public final MeasurementsController measurementsController;

    public ApplicationContext(){
        this.mainScreen = new MainScreen();

        this.channelsController = new ChannelsController();
        this.sensorsController = new SensorsController();
        this.calibratorsController = new CalibratorsController();
        this.personsController = new PersonsController();
        this.departmentsController = new DepartmentsController();
        this.areasController = new AreasController();
        this.processesController = new ProcessesController();
        this.installationsController = new InstallationsController();
        this.measurementsController = new MeasurementsController();
    }
}
