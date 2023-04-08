package ar.vicria.telegram.microservice.services.util;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class RoutMsg {

    private boolean from;
    private boolean to;
    private String lineFrom;
    private String lineTo;
    private String stationFrom;
    private String stationTo;

    private final static String FROM = "от";
    private final static String TO = "до";
    private static String CHOOSE = "Выберите";
    private static String TIME = "займет";
    private final static String SPACE = " ";

    public RoutMsg(String msg) {
        String end = msg.contains(CHOOSE) ? CHOOSE : TIME;
        this.to = msg.contains(TO);
        this.from = msg.contains(FROM);
        if (this.to && this.from) {
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

    public boolean isFull() {
        return this.lineFrom != null
                && this.lineTo != null
                && this.stationFrom != null
                && this.stationTo != null;
    }

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

    @Override
    public String toString() {
        String from = answerRout(this.lineFrom, this.stationFrom, this.from, FROM);
        String to = answerRout(this.lineTo, this.stationTo, this.to, TO);
        return "<b>Маршрут:</b>" + from + to;
    }

    private String answerRout(String line, String station, boolean exist, String direction) {
        List<String> parts = new ArrayList<>();
        parts.add(line == null ? "-" : line);
        parts.add(station == null ? "" : station);
        return exist ? (String.format("\n" + direction + " %s ",
                String.join(" ", parts))) : "";
    }

}
