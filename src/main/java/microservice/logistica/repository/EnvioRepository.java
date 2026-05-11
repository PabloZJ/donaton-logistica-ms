package microservice.logistica.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import microservice.logistica.model.Envio;

public interface EnvioRepository extends JpaRepository<Envio, Integer> {

    List<Envio> findByEstadoId(Integer estadoId);

    List<Envio> findByCentroAcopioId(Integer centroId);

    List<Envio> findByComunaId(Integer comunaId);
}