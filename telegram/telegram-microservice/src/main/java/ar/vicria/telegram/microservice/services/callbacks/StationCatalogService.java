package ar.vicria.telegram.microservice.services.callbacks;

import ar.vicria.subte.dto.StationDto;
import ar.vicria.telegram.microservice.services.RestToSubte;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * Каталог станций метро. Загружается один раз при старте.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class StationCatalogService {

    private final List<StationDto> stations = new CopyOnWriteArrayList<>();
    private final RestToSubte restToSubte;

    /**
     * Загрузка станций из subte при старте приложения.
     * Обновление происходит через планировщик.
     */
    @PostConstruct
    @Scheduled(cron = "${ar.victorian.adapter.telegram.refresh-cron:0 0 0 * * *}")
    public void loadStations() {
        try {
            List<StationDto> stationsByRestToStub = restToSubte.get();
            stations.clear();
            stations.addAll(stationsByRestToStub);
            log.info("Станций загружены в количестве: {}", stations.size());
        } catch (Exception e) {
            log.error("Не удалось обновить обновить станции через планировщик", e);
        }
    }

    /**
     * Все станции.
     *
     * @return список станций
     */
    public List<StationDto> getStations() {
        return List.copyOf(stations);
    }

    /**
     * Станции по ключу название + линия.
     *
     * @return станции по ключу
     */
    public Map<String, StationDto> getStationsByToStringMethod() {
        return stations.stream().collect(Collectors.toMap(StationDto::toString, dto -> dto));
    }

    /**
     * Уникальные линии станций.
     *
     * @return названия линий
     */
    public List<String> getLinesOfStations() {
        return stations.stream().map(StationDto::getLine).distinct().collect(Collectors.toList());
    }

    /**
     * Станции по линиям.
     *
     * @return станции по линиям
     */
    public Map<String, List<StationDto>> getStationByLine() {
        return stations.stream().collect(Collectors.groupingBy(StationDto::getLine, Collectors.toList()));
    }
}
