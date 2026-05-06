package microservice.logistica.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import microservice.logistica.model.EstadoEnvio;

public interface EstadoEnvioRepository extends JpaRepository<EstadoEnvio, Integer> {
}