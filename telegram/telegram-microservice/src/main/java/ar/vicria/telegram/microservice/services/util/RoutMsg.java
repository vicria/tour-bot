package ar.vicria.telegram.microservice.services.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Data about rout from user.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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

    //todo перенести в ресурсы и добавить локализацию
    private final static String FROM = "от";
    private final static String TO = "до";
    private static String CHOOSE = "Выберите";
    private static String TIME = "займет";
    private final static String SPACE = " ";

    /**
     * Constructor data from user msg.
     *
     * @param msg form user
     */
    public RoutMsg(String msg) {
        String end = msg.contains(CHOOSE) ? CHOOSE : TIME;
        this.to = msg.contains(TO);
        this.from = msg.contains(FROM);
        if (this.to && this.from) {
            //todo бага при кнопке подробнее
            String substring = msg.substring(msg.indexOf(FROM), msg.indexOf(end)).trim();
            String find = TO + SPACE;
            String from = substring.substring(find.length(), substring.indexOf(find)).trim();
            String to = substring.substring(substring.indexOf(find) + find.length());
            setLineAndStation(from, true);
            setLineAndStation(to, false);
        } else if (this.from) {
            String from = msg.substring(msg.indexOf(FROM) + FROM.length(), msg.indexOf(end)).trim();
            setLineAndStation(from, true);
        } else if (this.to) {
            String to = msg.substring(msg.indexOf(TO) + TO.length(), msg.indexOf(end)).trim();
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
        String from = answerRout(this.lineFrom, this.stationFrom, this.from, FROM);
        String to = answerRout(this.lineTo, this.stationTo, this.to, TO);
        return "<b>Маршрут:</b>" + from + to;
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
