package ar.vicria.telegram.microservice.rb;

import lombok.Getter;

import java.util.Locale;
import java.util.Objects;

/**
 * all information from resource bundle 'messages'.
 */
@Getter
public class Messages {

    private final MessageSource ms = new MessageSource();
    /**
     * current locale.
     */
    private final Locale locale;

    /**
     * time from AnswerDerailsQuery.
     */
    private final String adqTime;
    /**
     * distance from AnswerDerailsQuery.
     */
    private final String adqDistance;
    /**
     * hide from AnswerDerailsQuery.
     */
    private final String adqHide;
    /**
     * time from AnswerQuery.
     */
    private final String aqTime;
    /**
     * moredetailed from AnswerQuery.
     */
    private final String aqMoredetailed;
    /**
     * selectbranch from BranchQuery.
     */
    private final String bqSelectbranch;
    /**
     * route from BranchQuery.
     */
    private final String bqRoute;
    /**
     * selectstation from StationQuery.
     */
    private final String sqSelectstation;
    /**
     * "from" from StationQuery.
     */
    private final String sqFrom;
    /**
     * selectDirection from RoutMessage.
     */
    private final String rmessageSelectDirection;
    /**
     * rout from RoutMessage.
     */
    private final String rmessageRoute;
    /**
     * "from" from RoutMessage.
     */
    private final String rmessageFrom;
    /**
     * "to" from RoutMessage.
     */
    private final String rmessageTo;
    /**
     * menuSubte from StartMessage.
     */
    private final String smMenuSubte;
    /**
     * route from StartMessage.
     */
    private final String smRoute;
    /**
     * feedback from StartMessage.
     */
    private final String smFeedback;
    /**
     * aboutCapabilities from StartMessage.
     */
    private final String smAboutCapabilities;
    /**
     * "from" from RoutMsg.
     */
    private final String rmsgFrom;
    /**
     * "to" from RoutMsg.
     */
    private final String rmsgTo;
    /**
     * select from RoutMsg.
     */
    private final String rmsgSelect;
    /**
     * willTake from RoutMsg.
     */
    private final String rmsgWillTake;
    /**
     * route from RoutMsg.
     */
    private final String rmsgRoute;
    /**
     * selectItem from TelegramConnector.
     */
    private final String tgcSelectItem;

    /**
     * Конструктор.
     * @param locale current locale
     */
    public Messages(Locale locale) {
        this.locale = locale;
        adqTime = ms.getMessage("answerdetailsquery.time", locale);
        adqDistance = ms.getMessage("answerdetailsquery.distance", locale);
        adqHide = ms.getMessage("answerdetailsquery.hide", locale);

        aqTime = ms.getMessage("answerquery.time", locale);
        aqMoredetailed = ms.getMessage("answerquery.moredetailed", locale);

        bqSelectbranch = ms.getMessage("branchquery.selectabranch", locale);
        bqRoute = ms.getMessage("branchquery.route", locale);

        sqSelectstation = ms.getMessage("stationquery.selectastation", locale);
        sqFrom = ms.getMessage("stationquery.from", locale);

        rmessageSelectDirection = ms.getMessage("routmessage.selectadirection", locale);
        rmessageRoute = ms.getMessage("routmessage.route", locale);
        rmessageFrom = ms.getMessage("routmessage.from", locale);
        rmessageTo = ms.getMessage("routmessage.to", locale);

        smMenuSubte = ms.getMessage("startmessage.menusubte", locale);
        smRoute = ms.getMessage("startmessage.route", locale);
        smFeedback = ms.getMessage("startmessage.feedback", locale);
        smAboutCapabilities = ms.getMessage("startmessage.aboutthecapabilitiesofthebot", locale);

        rmsgFrom = ms.getMessage("routmsg.from", locale);
        rmsgTo = ms.getMessage("routmsg.to", locale);
        rmsgSelect = ms.getMessage("routmsg.select", locale);
        rmsgWillTake = ms.getMessage("routmsg.willtake", locale);
        rmsgRoute = ms.getMessage("routmsg.route", locale);

        tgcSelectItem = ms.getMessage("telegramconnector.selectanitemfromthemenu", locale);
    }

    /**
     * method.
     * @param locale locale of a class
     * @return Messages get instance of Messages
     * */
    public static Messages getInitMessage(Locale locale) {

        if (locale.getLanguage().startsWith(Locale.forLanguageTag("ru").getLanguage())) {
            return new Messages(locale);
        } else {
            return new Messages(Locale.ENGLISH);
        }
    }

    @Override
    public boolean equals(Object msg) {
        if (this == msg) {
            return true;
        }
        if (msg == null || getClass() != msg.getClass()) {
            return false;
        }
        Messages messages = (Messages) msg;
        return Objects.equals(adqTime, messages.adqTime)
                && Objects.equals(adqDistance, messages.adqDistance)
                && Objects.equals(adqHide, messages.adqHide)
                && Objects.equals(aqTime, messages.aqTime)
                && Objects.equals(aqMoredetailed, messages.aqMoredetailed)
                && Objects.equals(bqSelectbranch, messages.bqSelectbranch)
                && Objects.equals(bqRoute, messages.bqRoute)
                && Objects.equals(sqSelectstation, messages.sqSelectstation)
                && Objects.equals(sqFrom, messages.sqFrom)
                && Objects.equals(rmessageSelectDirection, messages.rmessageSelectDirection)
                && Objects.equals(rmessageRoute, messages.rmessageRoute)
                && Objects.equals(rmessageFrom, messages.rmessageFrom)
                && Objects.equals(rmessageTo, messages.rmessageTo)
                && Objects.equals(smMenuSubte, messages.smMenuSubte)
                && Objects.equals(smRoute, messages.smRoute)
                && Objects.equals(smFeedback, messages.smFeedback)
                && Objects.equals(smAboutCapabilities, messages.smAboutCapabilities)
                && Objects.equals(rmsgFrom, messages.rmsgFrom)
                && Objects.equals(rmsgTo, messages.rmsgTo)
                && Objects.equals(rmsgSelect, messages.rmsgSelect)
                && Objects.equals(rmsgWillTake, messages.rmsgWillTake)
                && Objects.equals(rmsgRoute, messages.rmsgRoute)
                && Objects.equals(tgcSelectItem, messages.tgcSelectItem);
    }

    @Override
    public int hashCode() {
        return Objects.hash(adqTime,
                adqDistance,
                adqHide,
                aqTime,
                aqMoredetailed,
                bqSelectbranch,
                bqRoute,
                sqSelectstation,
                sqFrom,
                rmessageSelectDirection,
                rmessageRoute,
                rmessageFrom,
                rmessageTo,
                smMenuSubte,
                smRoute,
                smFeedback,
                smAboutCapabilities,
                rmsgFrom,
                rmsgTo,
                rmsgSelect,
                rmsgWillTake,
                rmsgRoute,
                tgcSelectItem);
    }
}