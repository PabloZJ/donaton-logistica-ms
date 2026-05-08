package microservice.logistica.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import microservice.logistica.model.Asignacion;

@Repository
public interface AsignacionRepository extends JpaRepository<Asignacion, Integer> {
    List<Asignacion> findByDonacionId(Integer donacionId);
    List<Asignacion> findByNecesidadId(Integer necesidadId);
    List<Asignacion> findByResponsableUid(String responsableUid);
}