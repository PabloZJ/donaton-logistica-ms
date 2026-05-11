package microservice.logistica.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import microservice.logistica.model.Inventario;

@Repository
public interface InventarioRepository extends JpaRepository<Inventario, Integer> {

    // Buscar por centro de acopio
    List<Inventario> findByCentroAcopioId(Integer centroAcopioId);

    // Buscar por tipo de recurso
    List<Inventario> findByTipoRecursoId(Integer tipoRecursoId);

    // Buscar por centro y tipo recurso
    Optional<Inventario> findByCentroAcopioIdAndTipoRecursoId(Integer centroAcopioId, Integer tipoRecursoId);
}