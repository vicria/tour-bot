package ar.vicria.subte.microservice.repositories;

import ar.vicria.subte.microservice.entities.Station;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StationRepository extends JpaRepository<Station, String> {
}
