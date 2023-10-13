package localization.label;

import localization.Localization;

public abstract class Labels {

    public String applicationName = "ChannelMetrologyCalculater";

    public String cancel;
    public String importing;
    public String fileName;
    public String typesOfFiles;
    public String directory;
    public String allFiles;
    public String closeWindow;
    public String open;
    public String openSelectedDirectory;
    public String openSelectedFile;
    public String table;
    public String edited;
    public String name;
    public String size;
    public String type;
    public String toMain;
    public String list;
    public String lists;
    public String createNewFolder;
    public String update;
    public String upLevel;
    public String view;
    public String error;

    //Calibrator
    public String calibratorsList;

    //Control points
    public String controlPointsOfMeasurements;

    //Measurement
    public String measurementsList;

    //Method
    public String calculationMethods;

    //Person
    public String personsList;

    //Sensor
    public String sensorsErrorList;
    public String sensorTypesList;

    private static volatile Labels instance;

    private static final Localization DEFAULT_LOCALIZATION = Localization.UA;
    
    public static Labels getInstance() {
        if (instance == null) {
            synchronized (Labels.class) {
                if (instance == null) {
                    Localization localization = getCurrentLocalization();
                    switch (localization) {
                        case UA:
                            instance = new UkraineLabels();
                    }
                }
            }
        }
        return instance;
    }

    protected Labels() {
        init();
    }

    protected abstract void init();

    private static Localization getCurrentLocalization() {
        return DEFAULT_LOCALIZATION;
    }
}
