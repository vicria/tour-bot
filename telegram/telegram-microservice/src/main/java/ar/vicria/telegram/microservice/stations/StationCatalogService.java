package ar.vicria.telegram.microservice.stations;

import ar.vicria.telegram.microservice.services.RestToSubte;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * Сервис для загрузки и обновления каталога станций через планировщик.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class StationCatalogService {

    private final StationCatalogFactory stationCatalogFactory;
    private final RestToSubte restToSubte;

    @Getter
    private volatile StationCatalog catalog;

    /**
     * Загрузка станций из subte при старте приложения.
     * Валит старт приложения если список не удалось получить по REST запросу
     */
    @PostConstruct
    public void init() {
        catalog = stationCatalogFactory.create(restToSubte.get());
    }

    /**
     * Загрузка (обновление) станций из subte через планировщик.
     * При ошибке - пишет лог, приложение продолжит работу со старым снимком
     */
    @Scheduled(cron = "${ar.victorian.adapter.telegram.refresh-cron:0 0 0 * * *}")
    public void refresh() {
        try {
            catalog = stationCatalogFactory.create(restToSubte.get());
            log.info("Станций загружены в количестве: {}", catalog.getStations().size());
        } catch (Exception e) {
            log.error("Не удалось обновить обновить станции через планировщик", e);
        }
    }

}
