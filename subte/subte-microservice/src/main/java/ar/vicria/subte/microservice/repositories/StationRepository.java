package ar.vicria.subte.microservice.repositories;

import ar.vicria.subte.microservice.entities.Station;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Репозиторий станций.
 */
public interface StationRepository extends JpaRepository<Station, String> {

    /**
     * Поиск станции по имени и линии.
     * @param name - имя станции
     * @param line - имя и цвет станции
     * @return станция
     */
    Station getByNameAndLine(String name, String line);

}
