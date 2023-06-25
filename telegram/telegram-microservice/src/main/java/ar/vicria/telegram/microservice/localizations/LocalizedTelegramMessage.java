package ar.vicria.telegram.microservice.localizations;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Locale;

/**
 * All information from resource bundle 'messages'.
 */
@Getter
@ToString
@EqualsAndHashCode
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
        takeTimeWord = takeTime.substring(0, takeTime.indexOf(" ")).replaceAll("\n<b>", "");
    }


    /**
     * method.
     *
     * @param locale locale of a class
     * @return Messages get instance of Messages
     */
    public static LocalizedTelegramMessage getInitMessage(Locale locale) {
        if (locale.getLanguage().startsWith(Locale.forLanguageTag("ru").getLanguage())) {
            return new LocalizedTelegramMessage(locale);
        } else {
            return new LocalizedTelegramMessage(Locale.ENGLISH);
        }
    }
}