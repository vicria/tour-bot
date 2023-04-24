package ar.vicria.subte.microservice.repositories;

import ar.vicria.subte.microservice.entities.Connection;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Репозиторий связей между станциями.
 */
public interface ConnectionRepository extends JpaRepository<Connection, String> {
}
