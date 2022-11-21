package ar.vicria.tourbot.tourpoint.microservice.repositories;

import ar.vicria.tourbot.tourpoint.microservice.entities.Point;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointRepository extends JpaRepository<Point, String> {
}
