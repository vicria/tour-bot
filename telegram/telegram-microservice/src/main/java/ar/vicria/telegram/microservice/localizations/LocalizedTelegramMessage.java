package ar.vicria.telegram.microservice.localizations;

import lombok.Getter;

import java.util.Locale;

/**
 * All information from resource bundle 'messages'.
 *
 * @author abishkam
 * @since 1.0.0
 */
@Getter
public class LocalizedTelegramMessage {

    private final MessageSource ms = new MessageSource();
    /**
     * current locale.
     */
    private final Locale locale;

    /**
     * time from AnswerDetailsQuery.
     */
    private final String takeTime;
    /**
     * distance from AnswerDerailsQuery.
     */
    private final String distanceDetails;
    /**
     * hide from AnswerDerailsQuery.
     */
    private final String buttonHide;
    /**
     * moredetailed from AnswerQuery.
     */
    private final String buttonDetails;
    /**
     * selectbranch from BranchQuery.
     */
    private final String textSelectBranch;
    /**
     * route from BranchQuery.
     */
    private final String buttonRoute;
    /**
     * selectstation from StationQuery.
     */
    private final String textSelectRoute;
    /**
     * "from" from StationQuery.
     */
    private final String buttonFrom;
    /**
     * selectDirection from RoutMessage.
     */
    private final String textSelectDirection;
    /**
     * "to" from RoutMessage.
     */
    private final String buttonTo;
    /**
     * menuSubte from StartMessage.
     */
    private final String textStart;
    /**
     * feedback from StartMessage.
     */
    private final String buttonFeedback;
    /**
     * aboutCapabilities from StartMessage.
     */
    private final String buttonCapabilities;
    /**
     * select from RoutMsg.
     */
    private final String textSelectMenu;
    /**
     * willTake from RoutMsg.
     */
    private final String takeTimeWord;

    private final String common;

    /**
     * Конструктор.
     *
     * @param locale current locale
     */
    public LocalizedTelegramMessage(Locale locale) {
        this.locale = locale;
        takeTime = ms.getMessage("take-time", locale);
        distanceDetails = ms.getMessage("distance-details", locale);
        buttonHide = ms.getMessage("button.hide", locale);


        buttonDetails = ms.getMessage("button.details", locale);

        textSelectBranch = ms.getMessage("text.select-branch", locale);
        buttonRoute = ms.getMessage("button.route", locale);

        textSelectRoute = ms.getMessage("text.select-route", locale);
        buttonFrom = ms.getMessage("button.from", locale);
        buttonTo = ms.getMessage("button.to", locale);

        textSelectDirection = ms.getMessage("text.select-direction", locale);

        textStart = ms.getMessage("text.start", locale);
        buttonFeedback = ms.getMessage("button.feedback", locale);
        buttonCapabilities = ms.getMessage("button.capabilities", locale);

        textSelectMenu = ms.getMessage("text.select-menu", locale);
        this.common = getCommon(textSelectBranch, textSelectDirection);
        takeTimeWord = takeTime.substring(0, takeTime.indexOf(" ")).replaceAll("\n", "");
    }

    /**
     * Общая часть строк.
     * @param str1 строка 1
     * @param str2 строка 2
     * @return общая часть
     */
    static String getCommon(String str1, String str2) {
        String[] words1 = str1.replaceFirst("\\\\n", "").trim().split("\\s+");
        String[] words2 = str2.replaceFirst("\\\\n", "").trim().split("\\s+");

        for (String word1 : words1) {
            for (String word2 : words2) {
                if (word2.contains(word1)) {
                    return word1;
                }
            }
        }

        return "";
    }
}