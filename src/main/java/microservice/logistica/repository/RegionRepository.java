package microservice.logistica.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import microservice.logistica.model.Region;

public interface RegionRepository extends JpaRepository<Region, Integer> {
}