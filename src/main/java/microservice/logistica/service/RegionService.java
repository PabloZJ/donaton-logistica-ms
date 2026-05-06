package microservice.logistica.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import microservice.logistica.model.Region;
import microservice.logistica.repository.RegionRepository;

@Service
@Transactional
public class RegionService {

    @Autowired
    private RegionRepository repository;

    public List<Region> obtenerRegiones() {
        return repository.findAll();
    }

    public Region obtenerPorId(Integer id) {
        return repository.findById(id).orElse(null);
    }

    public Region guardar(Region region) {
        return repository.save(region);
    }

    public Region actualizar(Integer id, Region region) {
        Region existente = repository.findById(id).orElse(null);

        if (existente != null) {
            existente.setNombre(region.getNombre());
            return repository.save(existente);
        }
        return null;
    }

    public Region actualizarParcial(Integer id, Region region) {
        Region existente = repository.findById(id).orElse(null);

        if (existente != null) {
            if (region.getNombre() != null) {
                existente.setNombre(region.getNombre());
            }
            return repository.save(existente);
        }
        return null;
    }

    public void eliminar(Integer id) {
        Region region = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Región no encontrada"));

        repository.delete(region);
    }
}