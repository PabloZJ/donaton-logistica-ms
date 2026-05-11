package microservice.logistica.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import microservice.logistica.model.DetalleEnvio;

public interface DetalleEnvioRepository extends JpaRepository<DetalleEnvio, Integer> {

    List<DetalleEnvio> findByEnvioId(Integer envioId);

    List<DetalleEnvio> findByTipoRecursoId(Integer tipoRecursoId);
}