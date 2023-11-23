package localization.message;

import localization.Localization;

public abstract class Messages {

    public String init_error;
    public String init_success;

    //Channel
    public String modifyChannel_error;

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
        public static final String SERVICE_RUNNING = "Service is running";
        public static final String MISSING_UI_MANAGER_ERROR = "Before use context you must register manager!";

        public static String missingImplementation(Class<?> c) {
            return String.format("Can't find implementation for %s", c.getName());
        }
    }
}
