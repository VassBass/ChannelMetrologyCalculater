package service.certificate.converter.kt200k;

import localization.Messages;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class KT200KTranslate {

    private static final String APPROVE = "approve";
    private static final String NOT_APPROVE = "notApprove";
    private static final String NOTE = "note";

    private static final String ORIGIN_APPROVE = "Удовлетворяет заданному классу допуска";
    private static final String ORIGIN_NOT_APPROVE = "Не удовлетворяет заданному классу допуска";
    private static final String ORIGIN_NOTE = "Примечание:";

    private static final String CLEARANCE_CLASS_REGEX = "\\p{Lu}[.\\s]*$";

    public static String translateToLocale(String val) {
        if (val == null) return null;

        Map<String, String> messages = Messages.getMessages(KT200KTranslate.class);
        if (val.contains(ORIGIN_APPROVE)) {
            return messages.get(APPROVE);
        } else if (val.contains(ORIGIN_NOT_APPROVE)) {
            return messages.get(NOT_APPROVE);
        } else if (val.contains(ORIGIN_NOTE)) {
            Matcher matcher = Pattern.compile(CLEARANCE_CLASS_REGEX).matcher(val);
            String clearanceClass = matcher.find() ? matcher.group() : null;
            return String.format(messages.get(NOTE), clearanceClass);
        } else return val;
    }
}
