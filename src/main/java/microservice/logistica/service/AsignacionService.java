package microservice.logistica.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import microservice.logistica.model.Asignacion;
import microservice.logistica.repository.AsignacionRepository;

@Service
@Transactional
public class AsignacionService {

    @Autowired
    private AsignacionRepository asignacionRepository;

    public List<Asignacion> obtenerTodas() {
        return asignacionRepository.findAll();
    }

    public Asignacion obtenerPorId(Integer id) {
        return asignacionRepository.findById(id).orElse(null);
    }

    public Asignacion guardar(Asignacion asignacion) {
        return asignacionRepository.save(asignacion);
    }

    public Asignacion actualizar(Integer id, Asignacion asignacion) {
        Asignacion existente = asignacionRepository.findById(id).orElse(null);
        if (existente != null) {
            existente.setNecesidadId(asignacion.getNecesidadId());
            existente.setCantidadAsignada(asignacion.getCantidadAsignada());
            existente.setFechaAsignacion(asignacion.getFechaAsignacion());
            existente.setResponsableUid(asignacion.getResponsableUid());
            return asignacionRepository.save(existente);
        }
        return null;
    }

    public Asignacion actualizarParcial(Integer id, Asignacion asignacion) {
        Asignacion existente = asignacionRepository.findById(id).orElse(null);
        if (existente != null) {
            if (asignacion.getNecesidadId() != null) 
                existente.setNecesidadId(asignacion.getNecesidadId());
            if (asignacion.getCantidadAsignada() != null) 
                existente.setCantidadAsignada(asignacion.getCantidadAsignada());
            if (asignacion.getFechaAsignacion() != null) 
                existente.setFechaAsignacion(asignacion.getFechaAsignacion());
            if (asignacion.getResponsableUid() != null) 
                existente.setResponsableUid(asignacion.getResponsableUid());
            return asignacionRepository.save(existente);
        }
        return null;
    }

    public void eliminar(Integer id) {
        asignacionRepository.deleteById(id);
    }

    public List<Asignacion> obtenerPorNecesidad(Integer necesidadId) {
        return asignacionRepository.findByNecesidadId(necesidadId);
    }

    public List<Asignacion> obtenerPorResponsable(String responsableUid) {
        return asignacionRepository.findByResponsableUid(responsableUid);
    }
}