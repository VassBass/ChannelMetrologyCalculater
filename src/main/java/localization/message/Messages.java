package localization.message;

import localization.Localization;

public abstract class Messages {

    public String init_mainScreen;
    public String init_repository;
    public String exec_channelListService;
    public String init_measurementService;
    public String init_calibratorService;
    public String init_importService;
    public String init_sensorErrorService;
    public String init_sensorsTypesService;
    public String init_controlPointsService;
    public String init_personService;
    public String init_convertorTCService;
    public String init_calculationMethodsService;
    public String init_converterService;
    public String init_archivingService;
    public String init_error;
    public String init_success;

    private static volatile Messages instance;

    private static final Localization DEFAULT_LOCALIZATION = Localization.UA;

    public static Messages getInstance() {
        if (instance == null) {
            synchronized (Messages.class) {
                if (instance == null) {
                    Localization localization = getCurrentLocalization();
                    switch (localization) {
                        case UA:
                            instance = new UkraineMessages();
                    }
                }
            }
        }
        return instance;
    }

    protected Messages() {
        init();
    }

    protected abstract void init();

    private static Localization getCurrentLocalization() {
        return DEFAULT_LOCALIZATION;
    }

    public static class Log {
        public static final String INIT_SUCCESS = "Initialization completed successfully";
        public static final String EXCEPTION_THROWN = "Exception was thrown!";
    }
}
