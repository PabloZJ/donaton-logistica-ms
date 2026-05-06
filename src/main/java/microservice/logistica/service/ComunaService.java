package microservice.logistica.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import microservice.logistica.model.Comuna;
import microservice.logistica.repository.ComunaRepository;

@Service
@Transactional
public class ComunaService {

    @Autowired
    private ComunaRepository repository;

    public List<Comuna> obtenerComunas() {
        return repository.findAll();
    }

    public Comuna obtenerPorId(Integer id) {
        return repository.findById(id).orElse(null);
    }

    public Comuna guardar(Comuna comuna) {
        return repository.save(comuna);
    }

    public Comuna actualizar(Integer id, Comuna comuna) {
        Comuna existente = repository.findById(id).orElse(null);

        if (existente != null) {
            existente.setNombre(comuna.getNombre());
            existente.setRegion(comuna.getRegion());
            return repository.save(existente);
        }

        return null;
    }

    public Comuna actualizarParcial(Integer id, Comuna comuna) {
        Comuna existente = repository.findById(id).orElse(null);

        if (existente != null) {

            if (comuna.getNombre() != null) {
                existente.setNombre(comuna.getNombre());
            }

            if (comuna.getRegion() != null) {
                existente.setRegion(comuna.getRegion());
            }

            return repository.save(existente);
        }

        return null;
    }

    public void eliminar(Integer id) {
        Comuna comuna = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comuna no encontrada"));

        repository.delete(comuna);
    }

    // filtro
    public List<Comuna> obtenerPorRegion(Integer regionId) {
        return repository.findByRegionId(regionId);
    }
}