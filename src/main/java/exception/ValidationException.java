package exception;

public class ValidationException extends RuntimeException {
    private final String uiMessage;

    private ValidationException(String logMessage, String uiMessage) {
        super(logMessage);
        this.uiMessage = uiMessage;
    }

    public String getUiMessage() {
        return uiMessage;
    }

    public static ValidationException A01(String logFieldName, String uiFieldName)  {
        return new ValidationException(
                String.format(ExceptionMessage.A01, logFieldName),
                String.format(ExceptionMessage.UI.A01, uiFieldName));
    }

    public static ValidationException A02(String logFieldName, String uiFieldName)  {
        return new ValidationException(
                String.format(ExceptionMessage.A02, logFieldName),
                String.format(ExceptionMessage.UI.A02, uiFieldName));
    }

    public static ValidationException B01(String logFieldName, String uiFieldName) {
        return new ValidationException(
                String.format(ExceptionMessage.B01, logFieldName),
                String.format(ExceptionMessage.UI.B01, uiFieldName));
    }
}
