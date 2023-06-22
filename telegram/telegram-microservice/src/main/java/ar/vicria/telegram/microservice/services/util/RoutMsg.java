package ar.vicria.telegram.microservice.services.util;

import ar.vicria.telegram.microservice.rb.Messages;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Data about rout from user.
 */
@Getter
@Setter
@NoArgsConstructor
public class RoutMsg {
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

    private Messages ms = Messages.getInitMessage(LocaleContextHolder.getLocale());

    private final String rmsgFrom = ms.getRmsgFrom();
    private final String rmsgTo = ms.getRmsgTo();
    private final String rmsgSelect = ms.getRmsgSelect();
    private final String rmsgWillTake = ms.getRmsgWillTake();
    private final static String SPACE = " ";

    /**
     * Constructor data from user msg.
     *
     * @param msg form user
     */
    public RoutMsg(String msg) {
        String end = msg.contains(rmsgSelect) ? rmsgSelect : rmsgWillTake;
        this.to = msg.contains(rmsgTo);
        this.from = msg.contains(rmsgFrom);
        if (this.to && this.from) {
            String substring = msg.substring(msg.indexOf(rmsgFrom), msg.indexOf(end)).trim();
            String findFrom = rmsgFrom + SPACE;
            String findTo = rmsgTo + SPACE;
            String from = substring.substring(findFrom.length(), substring.indexOf(findTo)).trim();
            String to = substring.substring(substring.indexOf(findTo) + findTo.length());
            setLineAndStation(from, true);
            setLineAndStation(to, false);
        } else if (this.from) {
            String from = msg.substring(msg.indexOf(rmsgFrom) + rmsgFrom.length(), msg.indexOf(end)).trim();
            setLineAndStation(from, true);
        } else if (this.to) {
            String to = msg.substring(msg.indexOf(rmsgTo) + rmsgTo.length(), msg.indexOf(end)).trim();
            setLineAndStation(to, false);
        }
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
        String from = answerRout(this.lineFrom, this.stationFrom, this.from, rmsgFrom);
        String to = answerRout(this.lineTo, this.stationTo, this.to, rmsgTo);
        return ms.getRmsgRoute() + from + to;
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

}
