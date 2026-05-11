package microservice.logistica.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import microservice.logistica.model.CentroAcopio;

public interface CentroAcopioRepository extends JpaRepository<CentroAcopio, Integer> {

    List<CentroAcopio> findByRegionId(Integer regionId);

    List<CentroAcopio> findByActivo(Boolean activo);
}