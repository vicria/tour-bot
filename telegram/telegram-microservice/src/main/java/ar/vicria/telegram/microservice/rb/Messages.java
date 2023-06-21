package ar.vicria.telegram.microservice.rb;

import lombok.Getter;

import java.util.Locale;

@Getter
public class Messages {

    private final MessageSource ms = new MessageSource();
    private final Locale locale;

    private final String adqTime;
    private final String adqDistance;
    private final String adqHide;

    private final String aqTime;
    private final String aqMoredetailed;

    private final String bqSelectbranch;
    private final String bqRoute;

    private final String sqSelectstation;
    private final String sqFrom;

    private final String rmessageSelectDirection;
    private final String rmessageRoute;
    private final String rmessageFrom;
    private final String rmessageTo;

    private final String smMenuSubte;
    private final String smRoute;
    private final String smFeedback;
    private final String smAboutCapabilities;

    private final String rmsgFrom;
    private final String rmsgTo;
    private final String rmsgSelect;
    private final String rmsgWillTake;
    private final String rmsgRoute;

    private final String tgcSelectItem;

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

    public static Messages getInitMessage(Locale locale) {

        if(locale.getLanguage().startsWith(Locale.forLanguageTag("ru").getLanguage())) {
            return new Messages(locale);
        } else {
            return new Messages(Locale.ENGLISH);
        }
    }


}