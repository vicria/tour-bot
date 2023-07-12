package ar.vicria.telegram.microservice.services.util;

import lombok.experimental.UtilityClass;

/**
 * Class for formatting text for send.
 */
@UtilityClass
public class FormatText {

    private static final String BOLD = "<b>";

    /**
     * create BOLD text.
     * @param text text for formatting
     * @return bolded text
     */
    public static String bold(String text) {
        StringBuilder sb = new StringBuilder();
        sb.append(BOLD);
        sb.append(text);
        sb.append(closeTag(BOLD));
        return sb.toString();
    }

    private static String closeTag(String tag) {
        StringBuilder sb = new StringBuilder();
        sb.append(tag.charAt(0));
        sb.append("/");
        sb.append(tag.substring(1));
        return sb.toString();
    }
}
