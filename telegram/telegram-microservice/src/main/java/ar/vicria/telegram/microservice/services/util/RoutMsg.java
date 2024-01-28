package ar.vicria.telegram.microservice.services.util;

import ar.vicria.subte.dto.RouteDto;
import ar.vicria.subte.dto.StationDto;
import ar.vicria.telegram.microservice.localizations.LocalizedTelegramMessage;
import ar.vicria.telegram.microservice.localizations.LocalizedTelegramMessageFactory;
import ar.vicria.telegram.microservice.services.Localized;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static ar.vicria.telegram.microservice.services.util.FormatText.bold;

/**
 * Data about rout from user.
 */
@Getter
@Setter
@NoArgsConstructor
public class RoutMsg implements Localized {
    /**
     * from exist.
     */
    private boolean from;
    /**
     * to exist.
     */
    private boolean to;
    /**
     * line from - chosen.
     */
    private String lineFrom;
    /**
     * line to - chosen.
     */
    private String lineTo;
    /**
     * station from - chosen.
     */
    private String stationFrom;
    /**
     * station to - chosen.
     */
    private String stationTo;

    private final static String SPACE = " ";

    /**
     * Constructor data from user msg.
     *
     * @param msg form user
     */
    public RoutMsg(String msg) {
        if (msg == null) {
            return;
        }
        LocalizedTelegramMessage localized = localizedFactory().getLocalizedByWord(msg);
        String end = msg.contains(localized.getCommon()) ? localized.getCommon() : localized.getTakeTimeWord();
        this.to = msg.contains(localized.getButtonTo());
        this.from = msg.contains(localized.getButtonFrom());
        if (this.to && this.from) {
            String substring = msg.substring(msg.indexOf(localized.getButtonFrom()), msg.indexOf(end)).trim();
            String findFrom = localized.getButtonFrom() + SPACE;
            String findTo = localized.getButtonTo() + SPACE;
            String from = substring.substring(findFrom.length(), substring.indexOf(findTo)).trim();
            String to = substring.substring(substring.indexOf(findTo) + findTo.length());
            setLineAndStation(from, true);
            setLineAndStation(to, false);
        } else if (this.from) {
            String from = msg.substring(msg.indexOf(localized.getButtonFrom())
                    + localized.getButtonFrom().length(), msg.indexOf(end)).trim();
            setLineAndStation(from, true);
        } else if (this.to) {
            String to = msg.substring(msg.indexOf(localized.getButtonTo())
                    + localized.getButtonTo().length(), msg.indexOf(end)).trim();
            setLineAndStation(to, false);
        }
        return;
    }

    /**
     * Маппер из ответа кафки.
     *
     * @param response - ответ по пути.
     */
    public RoutMsg(RouteDto response) {
        this.from = true;
        this.to = true;
        StationDto start = response.getRoute().getFirst();
        int size = response.getRoute().size() - 1;
        StationDto end = response.getRoute().get(size);
        this.lineTo = end.getLine();
        this.lineFrom = start.getLine();
        this.stationTo = end.getName();
        this.stationFrom = start.getName();
    }

    /**
     * rout is complete.
     *
     * @return complete or not
     */
    public boolean isFull() {
        return this.lineFrom != null
                && this.lineTo != null
                && this.stationFrom != null
                && this.stationTo != null;
    }

    /**
     * util method for constructor of user msg.
     *
     * @param input  part of user msg
     * @param isFrom is from or to direction
     */
    private void setLineAndStation(String input, boolean isFrom) {
        String[] parts = input.split(SPACE);
        String line = parts[0].equals("-") ? null : parts[0];
        String station = null;
        if (parts.length > 1) {
            station = String.join(SPACE, Arrays.copyOfRange(parts, 1, parts.length));
        }
        if (isFrom) {
            this.lineFrom = line;
            this.stationFrom = station;
        } else {
            this.lineTo = line;
            this.stationTo = station;
        }
    }

    /**
     * Generation text for user.
     *
     * @return text
     */
    @Override
    public String toString() {
        LocalizedTelegramMessage localized = localizedFactory().getLocalized();
        String from = answerRout(this.lineFrom, this.stationFrom, this.from, localized.getButtonFrom());
        String to = answerRout(this.lineTo, this.stationTo, this.to, localized.getButtonTo());
        return bold(localized.getButtonRoute()) + from + to;
    }

    /**
     * Util for generation text for user. Line with station
     *
     * @param line      color of line
     * @param station   name
     * @param exist     direction
     * @param direction from or to
     * @return text
     */
    private String answerRout(String line, String station, boolean exist, String direction) {
        List<String> parts = new ArrayList<>();
        parts.add(line == null ? "-" : line);
        parts.add(station == null ? "" : station);
        return exist ? (String.format("\n" + direction + " %s ",
                String.join(" ", parts))) : "";
    }

    @Override
    public LocalizedTelegramMessageFactory localizedFactory() {
        return new LocalizedTelegramMessageFactory();//todo исправить на бин
    }
}
