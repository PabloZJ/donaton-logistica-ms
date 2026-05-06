package microservice.logistica.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import microservice.logistica.model.Comuna;

public interface ComunaRepository extends JpaRepository<Comuna, Integer> {

    List<Comuna> findByRegionId(Integer regionId);
}