package microservice.logistica.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import microservice.logistica.model.CentroAcopio;
import microservice.logistica.repository.CentroAcopioRepository;

@Service
@Transactional
public class CentroAcopioService {

    @Autowired
    private CentroAcopioRepository repository;

    public List<CentroAcopio> obtenerCentros() {
        return repository.findAll();
    }

    public CentroAcopio obtenerPorId(Integer id) {
        return repository.findById(id).orElse(null);
    }

    public CentroAcopio guardar(CentroAcopio centro) {
        return repository.save(centro);
    }

    public CentroAcopio actualizar(Integer id, CentroAcopio centro) {
        CentroAcopio existente = repository.findById(id).orElse(null);

        if (existente != null) {
            existente.setNombre(centro.getNombre());
            existente.setDireccion(centro.getDireccion());
            existente.setRegion(centro.getRegion());
            existente.setActivo(centro.getActivo());

            return repository.save(existente);
        }

        return null;
    }

    public CentroAcopio actualizarParcial(Integer id, CentroAcopio centro) {
        CentroAcopio existente = repository.findById(id).orElse(null);

        if (existente != null) {

            if (centro.getNombre() != null)
                existente.setNombre(centro.getNombre());

            if (centro.getDireccion() != null)
                existente.setDireccion(centro.getDireccion());

            if (centro.getRegion() != null)
                existente.setRegion(centro.getRegion());

            if (centro.getActivo() != null)
                existente.setActivo(centro.getActivo());

            return repository.save(existente);
        }

        return null;
    }

    public void eliminar(Integer id) {
        CentroAcopio centro = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Centro de acopio no encontrado"));

        repository.delete(centro);
    }

    // filtros

    public List<CentroAcopio> obtenerPorRegion(Integer regionId) {
        return repository.findByRegionId(regionId);
    }

    public List<CentroAcopio> obtenerActivos() {
        return repository.findByActivo(true);
    }
}